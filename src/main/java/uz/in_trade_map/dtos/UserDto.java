package uz.in_trade_map.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Permissions;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private UUID createdById;

    private UUID updatedById;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String username;

    private List<Map<String, Object>> roles;

    private List<String> permissions;

    public static UserDto response(User user) {
        UserDto userDto = new UserDto();
        Set<String> perm = new HashSet<>();
        if (user.getCreatedAt() != null) userDto.setCreatedAt(user.getCreatedAt());
        if (user.getCreatedBy() != null) userDto.setCreatedById(user.getCreatedBy().getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setId(user.getId());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRoles(user.getRoles().stream().map(role -> {
            Map<String, Object> roleM = new HashMap<>();
            roleM.put("nameUz", role.getNameUz());
            roleM.put("nameEn", role.getNameEn());
            roleM.put("nameRu", role.getNameRu());
            roleM.put("nameUzCry", role.getNameUzCry());
            roleM.put("id", role.getId());
            roleM.put("roleName", role.getRoleName());
            perm.addAll(role.getPermissions().stream().map(Permissions::getName).collect(Collectors.toList()));
            return roleM;
        }).collect(Collectors.toList()));
        userDto.setPermissions(new ArrayList<>(perm));
        if (user.getUpdatedAt() != null) userDto.setUpdatedAt(user.getUpdatedAt());
        if (user.getUpdatedBy() != null) userDto.setUpdatedById(user.getUpdatedBy().getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
