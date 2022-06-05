package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.in_trade_map.entity.enums.RoleName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameUz;

    public Role(String nameUz, String nameRu, String nameUzCry, String nameEn, RoleName roleName) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameUzCry = nameUzCry;
        this.nameEn = nameEn;
        this.roleName = roleName;
    }

    private String nameRu;

    private String nameUzCry;

    private String nameEn;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
