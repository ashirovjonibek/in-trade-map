package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Permissions;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.PermissionsRepository;
import uz.in_trade_map.repository.RoleRepository;
import uz.in_trade_map.utils.AuthUser;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.RoleRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

import static uz.in_trade_map.service.specifications.RoleSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class RoleService extends Validator<RoleRequest> {
    private final RoleRepository roleRepository;
    private final PermissionsRepository permissionsRepository;

    public HttpEntity<?> createRole(RoleRequest request) {
        Map<String, Object> valid = valid(request);
        try {
            boolean b = roleRepository.existsByRoleNameAndActiveTrue(request.getRoleName());
            if (b) {
                List<String> errors = new ArrayList<>();
                errors.add("This roleName already exists");
                valid.put("roleName", errors);
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors", valid);
            } else {
                Role role = RoleRequest.convertToRole(request);
                List<Permissions> allById = permissionsRepository.findAllById(request.getPermissions());
                role.setPermissions(allById);
                Role save = roleRepository.save(role);
                return AllApiResponse.response(1, "Success", DtoConverter.roleDto(save));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create role");
        }
    }

    public ResponseEntity<?> updateRole(RoleRequest request) {
        if (!RoleName.ROLE_ADMIN.name().equals(request.getRoleName())) {
            try {
                Optional<Role> optionalRole = roleRepository.findByRoleNameAndActiveTrue(request.getRoleName());
                if (optionalRole.isPresent()) {
                    Map<String, Object> valid = new HashMap<>();
                    if (request.getPermissions().size() == 0) {
                        valid.put("permissions", "You need to attach at least one permission!");
                    }
                    if (valid.size() > 0) {
                        return AllApiResponse.response(422, 0, "Validator errors", valid);
                    } else {
                        Role role = RoleRequest.convertToRole(request);
                        List<Permissions> allById = permissionsRepository.findAllById(request.getPermissions());
                        role.setPermissions(allById);
                        role.setId(optionalRole.get().getId());
                        Role save = roleRepository.save(role);
                        return AllApiResponse.response(1, "Role updated successfully!", DtoConverter.roleDto(save));
                    }
                } else return AllApiResponse.response(404, 0, "Role not found with role name");
            } catch (Exception e) {
                e.printStackTrace();
                return AllApiResponse.response(500, 0, "Error create role");
            }
        } else return AllApiResponse.response(422, 0, "You cannot update this role!");
    }

    public ResponseEntity<?> getAllRoles(String search, String expand, int page, int size) {
        try {
            Page<Role> roles = roleRepository.findAll(
                    where(findByNameUz(search))
                            .or(findByNameEn(search))
                            .or(findByNameRu(search))
                            .or(findByNameUzCry(search))
                            .and(activeTrue()),
                    PageRequest.of(page - 1, size)
            );
            Map<String, Object> resp = new HashMap<>();
            List<Role> roleList = roles.stream().filter(role -> !role.getRoleName().equals(RoleName.ROLE_ADMIN.name())).collect(Collectors.toList());
            resp.put("items", roleList.stream().map(role -> DtoConverter.roleDto(role, expand)).collect(Collectors.toList()));
            resp.put("meta", new Meta(roles.getTotalElements(), roles.getTotalPages(), page, size));
            return AllApiResponse.response(1, "Success", resp);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error read all roles!", e.getMessage());
        }
    }

    public ResponseEntity<?> getOneRole(String roleName, String expand) {
        try {
            Optional<Role> optionalRole = roleRepository.findByRoleNameAndActiveTrue(roleName);
            if (optionalRole.isPresent() && !roleName.equals(RoleName.ROLE_ADMIN.name())) {
                return AllApiResponse.response(1, "Success", DtoConverter.roleDto(optionalRole.get(), expand));
            } else return AllApiResponse.response(404, 0, "Role not found with roleName!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one role!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(String roleName) {
        if (!roleName.equals(RoleName.ROLE_ADMIN.name())) {
            Optional<Role> optionalRole = roleRepository.findByRoleNameAndActiveTrue(roleName);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                role.setActive(false);
                roleRepository.save(role);
                return AllApiResponse.response(1, "Role deleted successfully!");
            } else {
                return AllApiResponse.response(404, 0, "Role not fount with roleName!");
            }
        } else {
            return AllApiResponse.response(422, 0, "You cannot delete this role!");
        }
    }

    public ResponseEntity<?> allPermissions() {
        try {
            List<Permissions> permissions = permissionsRepository.findAll();
            return AllApiResponse.response(1, "All permissions!", permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get permissions!", e.getMessage());
        }
    }
}