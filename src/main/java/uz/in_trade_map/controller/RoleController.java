package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.RoleService;
import uz.in_trade_map.utils.request_objects.RoleRequest;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('create_role')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public HttpEntity<?> create(@ModelAttribute RoleRequest request) {
        return roleService.createRole(request);
    }

    @PreAuthorize("hasAnyAuthority('update_role')")
    @PutMapping(path = "/{roleName}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public HttpEntity<?> update(@PathVariable String roleName, @ModelAttribute RoleRequest request) {
        request.setRoleName(roleName);
        return roleService.updateRole(request);
    }

    @PreAuthorize("hasAnyAuthority('get_all_role')")
    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String expand,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        return roleService.getAllRoles(search, expand, page, size);
    }

    @PreAuthorize("hasAnyAuthority('get_one_role')")
    @GetMapping("/{roleName}")
    public HttpEntity<?> getOne(@PathVariable String roleName, @RequestParam(required = false) String expand) {
        return roleService.getOneRole(roleName, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_role')")
    @DeleteMapping("/{roleName}")
    public HttpEntity<?> delete(@PathVariable String roleName) {
        return roleService.delete(roleName);
    }

    @PreAuthorize("hasAnyAuthority('permissions')")
    @GetMapping("/permissions")
    public HttpEntity<?> getPermissions() {
        return roleService.allPermissions();
    }
}
