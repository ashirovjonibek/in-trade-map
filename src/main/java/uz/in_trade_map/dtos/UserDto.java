package uz.in_trade_map.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
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

    private List<String> roles;

    public static UserDto response(User user) {
        UserDto userDto = new UserDto();
        if (user.getCreatedAt() != null) userDto.setCreatedAt(user.getCreatedAt());
        if (user.getCreatedBy() != null) userDto.setCreatedById(user.getCreatedBy().getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setId(user.getId());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRoles(user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList()));
        if (user.getUpdatedAt() != null) userDto.setUpdatedAt(user.getUpdatedAt());
        if (user.getUpdatedBy() != null) userDto.setUpdatedById(user.getUpdatedBy().getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
