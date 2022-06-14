package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String middleName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String email;

    private Integer quarterId;

    private String address;

    private Float lat;

    private Float lng;

    private Integer locationId;

    @FieldTypeFile(extension = ".jpg,.jpeg,.png", size = 2 * 1024 * 1024)
    private MultipartFile image;

    private Integer companyId;

    private Set<String> roles;

    public static User convertToUser(UserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .username(request.getUsername())
                .build();
    }
}
