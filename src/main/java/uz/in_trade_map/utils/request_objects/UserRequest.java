package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.utils.validator.annotations.Email;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
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

    @NotNull(minLength = 13,maxLength = 13)
    private String phoneNumber;

    @NotNull(minLength = 8,maxLength = 30)
    private String username;

//    @NotNull(minLength = 8,maxLength = 30)
    private String password;

    @NotNull
    @Email
    private String email;

//    @NotNull
//    private Integer districtId;
//
//    @NotNull
//    private String address;
//
//    @NotNull
//    private Float lat;
//
//    @NotNull
//    private Float lng;

    @FieldTypeFile(extension = ".jpg,.jpeg,.png", size = 2 * 1024 * 1024)
    private MultipartFile image;

    private Integer companyId;

    @NotNull
    @FieldTypeArray(minLength = 1)
    private Set<String> roles;

    public static User convertToUser(UserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .username(request.getUsername())
                .active(true)
                .build();
    }
}
