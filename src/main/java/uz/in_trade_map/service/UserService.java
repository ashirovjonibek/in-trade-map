package uz.in_trade_map.service;

//import com.querydsl.jpa.impl.JPAQuery;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.entity.UserPasswords;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.*;
import uz.in_trade_map.utils.AuthUser;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.UserRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static uz.in_trade_map.service.specifications.UserSpecifications.*;

@Service
@RequiredArgsConstructor
public class UserService extends Validator<UserRequest> {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    //    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;
    //    private final DistrictRepository districtRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordActions passwordActions;
    private final UserPasswordsRepository userPasswordsRepository;
    private final PermissionsRepository permissionsRepository;

    public ResponseEntity<?> createUser(UserRequest request) {
        try {
            boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getPhoneNumber());
            boolean existsByUsername = userRepository.existsByUsername(request.getUsername());
            Map<String, Object> valid = valid(request);
            if (existsByEmail && request.getEmail() != null) {
                List<String> err = new ArrayList<>();
                err.add("This email already exists!");
                valid.put("email", err);
            }
            if (existsByPhoneNumber && request.getPhoneNumber() != null) {
                List<String> err = new ArrayList<>();
                err.add("This phone already exists!");
                valid.put("phone", err);
            }
            if (existsByUsername && request.getUsername() != null) {
                List<String> err = new ArrayList<>();
                err.add("This username already exists!");
                valid.put("username", err);
            }
            if (request.getPassword() != null && request.getPassword().length() < 8) {
                List<String> err = new ArrayList<>();
                err.add("The password value must be at least 8 characters");
                valid.put("password", err);
            }
            if (request.getPassword() != null && request.getPassword().length() > 30) {
                List<String> err = new ArrayList<>();
                err.add("The password value must be at more 30 characters");
                valid.put("password", err);
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else {
                UserPasswords userPasswords = new UserPasswords();
                User convertToUser = UserRequest.convertToUser(request);
                if (request.getImage() != null) {
                    convertToUser.setImage(attachmentService.uploadFile(request.getImage()));
                }
                Optional<Company> optionalCompany = companyRepository.findByIdAndActiveTrue(request.getCompanyId());
                if (optionalCompany.isPresent()) {
                    convertToUser.setCompany(optionalCompany.get());
                } else return AllApiResponse.response(404, 0, "Company not fount with id!");

//                Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
//                if (optionalDistrict.isPresent()) {
//                    Location save = locationRepository.save(new Location(optionalDistrict.get(), request.getAddress(), request.getLat(), request.getLng()));
//                    convertToUser.setLocation(save);
//                } else return AllApiResponse.response(404, 0, "District not fount with id!");
                Set<String> collect = request.getRoles().stream().filter(name -> !name.equals(RoleName.ROLE_ADMIN.name())).collect(Collectors.toSet());
                if (collect.size() > 0) {
                    Set<Role> roles = roleRepository.findAllByRoleNameIn(collect);
                    convertToUser.setRoles(roles);
                } else return AllApiResponse.response(404, 0, "This role not found!");
                if (request.getPassword() != null) {
                    convertToUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    userPasswords.setPassword(passwordActions.encodePassword(request.getPassword()));
                } else {
                    String password = passwordActions.generatePassword(8);
                    System.out.println(password);
                    convertToUser.setPassword(passwordEncoder.encode(password));
                    userPasswords.setPassword(passwordActions.encodePassword(password));
                }
                User user = userRepository.save(convertToUser);
                userPasswords.setUser(user);
                userPasswordsRepository.save(userPasswords);
                System.out.println(userPasswords.getPassword());
                System.out.println(passwordActions.decodePassword(userPasswords.getPassword()));
                return AllApiResponse.response(1, "Successfully created user!", DtoConverter.userDto(user, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create user");
        }
    }

    public ResponseEntity<?> updateUser(UUID id, UserRequest request) {
        try {
            Optional<User> optionalUser = userRepository.findByIdAndActiveTrue(id);

            if (optionalUser.isPresent() && optionalUser.get().getRoles().stream().filter(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN)).count() == 0) {
                Optional<UserPasswords> op = userPasswordsRepository.findByUserId(id);
                boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
                boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getPhoneNumber());
                boolean existsByUsername = userRepository.existsByUsername(request.getUsername());
                Map<String, Object> valid = valid(request);
                if (existsByEmail
                        && request.getEmail() != null
                        && !optionalUser.get().getEmail().equals(request.getEmail())
                ) {
                    List<String> err = new ArrayList<>();
                    err.add("This email already exists!");
                    valid.put("email", err);
                }
                if (existsByPhoneNumber
                        && request.getPhoneNumber() != null
                        && !optionalUser.get().getPhoneNumber().equals(request.getPhoneNumber())
                ) {
                    List<String> err = new ArrayList<>();
                    err.add("This phone already exists!");
                    valid.put("phone", err);
                }
                if (existsByUsername
                        && request.getUsername() != null
                        && !optionalUser.get().getUsername().equals(request.getUsername())
                ) {
                    List<String> err = new ArrayList<>();
                    err.add("This username already exists!");
                    valid.put("username", err);
                }
                if (request.getPassword() != null && request.getPassword().length() < 8) {
                    List<String> err = new ArrayList<>();
                    err.add("The password value must be at least 8 characters");
                    valid.put("password", err);
                }
                if (request.getPassword() != null && request.getPassword().length() > 30) {
                    List<String> err = new ArrayList<>();
                    err.add("The password value must be at more 30 characters");
                    valid.put("password", err);
                }
                if (valid.size() > 0) {
                    return AllApiResponse.response(422, 0, "Validator errors!", valid);
                } else {
                    UserPasswords userPasswords = new UserPasswords();
                    op.ifPresent(passwords -> userPasswords.setId(passwords.getId()));
                    User convertToUser = UserRequest.convertToUser(request, optionalUser.get());
                    if (request.getImage() != null) {
                        convertToUser.setImage(attachmentService.uploadFile(request.getImage()));
                    } else {
                        convertToUser.setImage(optionalUser.get().getImage());
                    }
                    Optional<Company> optionalCompany = companyRepository.findByIdAndActiveTrue(request.getCompanyId());
                    if (optionalCompany.isPresent()) {
                        convertToUser.setCompany(optionalCompany.get());
                    } else return AllApiResponse.response(404, 0, "Company not fount with id!");

//                    Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
//                    if (optionalDistrict.isPresent()) {
//                        Location save = locationRepository.save(new Location(optionalDistrict.get(), request.getAddress(), request.getLat(), request.getLng()));
//                        convertToUser.setLocation(save);
//                    } else return AllApiResponse.response(404, 0, "District not fount with id!");
                    Set<String> collect = request.getRoles().stream().filter(name -> !name.equals(RoleName.ROLE_ADMIN.name())).collect(Collectors.toSet());
                    if (collect.size() > 0) {
                        Set<Role> roles = roleRepository.findAllByRoleNameIn(collect);
                        convertToUser.setRoles(roles);
                    } else return AllApiResponse.response(404, 0, "This role not found!");
                    if (request.getPassword() != null) {
                        convertToUser.setPassword(passwordEncoder.encode(request.getPassword()));
                        userPasswords.setPassword(passwordActions.encodePassword(request.getPassword()));
                    } else {
                        if (op.isPresent()) {
                            convertToUser.setPassword(passwordEncoder.encode(passwordActions.decodePassword(op.get().getPassword())));
                            userPasswords.setPassword(op.get().getPassword());
                        } else {
                            String s = passwordActions.generatePassword(8);
                            convertToUser.setPassword(passwordEncoder.encode(s));
                            userPasswords.setPassword(passwordActions.encodePassword(s));
                        }
                    }
                    convertToUser.setId(id);
                    User user = userRepository.save(convertToUser);
                    userPasswords.setUser(user);
                    userPasswordsRepository.save(userPasswords);
                    return AllApiResponse.response(1, "Successfully updated user", DtoConverter.userDto(user, null));
                }
            } else {
                return AllApiResponse.response(404, 0, "User not fount with id!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error update user");
        }
    }

    public ResponseEntity<?> getAll(
            Integer companyId,
            String firstName,
            String lastName,
            String middleName,
            Set<String> roleNames,
            Integer districtId,
            Integer regionId,
            String email,
            String phoneNumber,
            String username,
            int page,
            int size,
            String expand
    ) {
        try {
            Set<Role> roleNameIn = null;
            Set<String> admin = new HashSet<>();
            admin.add(RoleName.ROLE_ADMIN.name());
            Set<Role> roleAdmin = roleRepository.findAllByRoleNameIn(admin);
            if (roleNames != null && !roleNames.isEmpty()) {
                roleNameIn = roleRepository.findAllByRoleNameIn(roleNames);
            }
            User user = (User) AuthUser.getCurrentUser().getPrincipal();
            Specification<User> where = where(findByCompanyId(companyId)
                    .and(findByDistrictId(districtId))
                    .and(findByEmail(email))
                    .and(findByFirstName(firstName))
                    .and(findByLastName(lastName))
                    .and(findByMiddleName(middleName))
                    .and(findByPhoneNumber(phoneNumber))
                    .and(findByRegionId(regionId))
                    .and(findByUsername(username))
                    .and(findByRoles(roleNames))
                    .and(findByUsernameNot(user.getUsername()))
                    .and(findByRolesNot(admin))
                    .and(activeTrue())
                    .and(companyActiveTrue())
            );

            Page<User> all = userRepository.findAll(where,
                    PageRequest.of(page, size)
            );

            Map<String, Object> response = new HashMap<>();
            response.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), page + 1, size));
            response.put("items", all.stream().map(user1 -> DtoConverter.userDto(user1, expand)).collect(Collectors.toList()));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all users!", e.getMessage());
        }
    }

    public ResponseEntity<?> getUserPasswordWithId(UUID id) {
        try {
            Optional<UserPasswords> optionalUserPasswords = userPasswordsRepository.findByUserId(id);
            if (optionalUserPasswords.isPresent() && optionalUserPasswords.get().getUser().isActive()) {
                Map<String, Object> response = new HashMap<>();
                response.put("username", optionalUserPasswords.get().getUser().getUsername());
                response.put("password", passwordActions.decodePassword(optionalUserPasswords.get().getPassword()));
                return AllApiResponse.response(1, "Success", response);
            } else return AllApiResponse.response(404, 0, "Password not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get password", e.getMessage());
        }
    }

    public ResponseEntity<?> getOneUser(UUID id, String expand) {
        try {
            Optional<User> op = userRepository.findByIdAndActiveTrue(id);
            User user = (User) AuthUser.getCurrentUser().getPrincipal();
            if (op.isPresent() && (user.getId().equals(op.get().getId()) || op.get().getRoles().stream().filter(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN.name())).count() == 0)) {
                return AllApiResponse.response(1, "One user", DtoConverter.userDto(op.get(), expand));
            } else {
                return AllApiResponse.response(404, 0, "User not fount with id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get user", e.getMessage());
        }
    }

    public ResponseEntity<?> deleteUser(UUID id) {
        try {
            Optional<User> op = userRepository.findByIdAndActiveTrue(id);
            User user = (User) AuthUser.getCurrentUser().getPrincipal();
            if (op.isPresent() && !user.getId().equals(op.get().getId()) && op.get().getRoles().stream().filter(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN)).count() == 0) {
                User user1 = op.get();
                user1.setActive(false);
                userRepository.save(user1);
                return AllApiResponse.response(1, "User deleted successfully");
            } else {
                return AllApiResponse.response(404, 0, "User not fount with id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete user", e.getMessage());
        }
    }

    public ResponseEntity<?> updatePassword(UUID userId, String newPassword, String oldPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByIdAndActiveTrue(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Optional<UserPasswords> optionalUserPasswords = userPasswordsRepository.findByUserId(userId);
                if (optionalUserPasswords.isPresent()) {
                    UserPasswords userPasswords = optionalUserPasswords.get();
                    String decodePassword = passwordActions.decodePassword(userPasswords.getPassword());
                    if (decodePassword.equals(oldPassword)) {
                        user.setPassword(passwordEncoder.encode(newPassword));
                        userPasswords.setPassword(passwordActions.encodePassword(newPassword));
                        userRepository.save(user);
                        userPasswordsRepository.save(userPasswords);
                        return AllApiResponse.response(1, "Password updated successfully!");
                    } else return AllApiResponse.response(422, 0, "Old password not match user password!");
                } else return AllApiResponse.response(422, 0, "You have not access this action!");
            } else return AllApiResponse.response(404, 0, "User not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error update password", e.getMessage());
        }
    }
}
