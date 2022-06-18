package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.UserService;
import uz.in_trade_map.utils.request_objects.UserRequest;

import java.util.Set;

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


}
