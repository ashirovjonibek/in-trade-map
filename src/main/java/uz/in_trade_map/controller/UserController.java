package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.service.UserService;
import uz.in_trade_map.utils.request_objects.UserRequest;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('create_user')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> save(@ModelAttribute UserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasAnyAuthority('update_user')")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> update(@PathVariable UUID id, @ModelAttribute UserRequest request) {
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasAnyAuthority('get_all_user')")
    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) Set<String> roleNames,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String username,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String expand
    ) {
        return userService.getAll(companyId, firstName, lastName, middleName, roleNames, districtId, regionId, email, phoneNumber, username, page - 1, size, expand);
    }

    @PreAuthorize("hasAnyAuthority('get_user_password')")
    @GetMapping("/password/{id}")
    public HttpEntity<?> getPassword(@PathVariable UUID id) {
        return userService.getUserPasswordWithId(id);
    }

    @PreAuthorize("hasAnyAuthority('get_one_user')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOneUser(@PathVariable UUID id, @RequestParam(required = false) String expand) {
        return userService.getOneUser(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_user')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @PreAuthorize("hasAnyAuthority('update_user_password')")
    @PutMapping("/password/{id}")
    public HttpEntity<?> updateUserPassword(@PathVariable UUID id, @RequestPart String oldPassword, @RequestPart String newPassword) {
        Map<String, Object> valid = new HashMap<>();
        if (oldPassword.length() < 8) {
            List<String> err = new ArrayList<>();
            err.add("oldPassword length should not be less than 8 characters");
            valid.put("oldPassword", err);
        }
        if (newPassword.length() < 8) {
            List<String> err = new ArrayList<>();
            err.add("newPassword length should not be less than 8 characters");
            valid.put("newPassword", err);
        }
        if (valid.size() > 0) {
            return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } else
            return userService.updatePassword(id, newPassword, oldPassword);
    }


}
