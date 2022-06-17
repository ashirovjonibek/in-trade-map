package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Permissions;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String nameUz;

    private String nameRu;

    private String nameUzCry;

    private String nameEn;

    @FieldTypeArray(minLength = 1)
    private List<Integer> permissions;

    @NotNull
    private String roleName;

    public static Role convertToRole(RoleRequest request) {
        return Role.builder()
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .nameUzCry(request.getNameUzCry())
                .roleName(request.getRoleName())
                .active(true)
                .build();
    }
}
