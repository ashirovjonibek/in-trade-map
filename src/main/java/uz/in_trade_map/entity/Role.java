package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import uz.in_trade_map.entity.enums.RoleName;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameUz;

    private String nameRu;

    private String nameUzCry;

    private String nameEn;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Permissions> permissions;

    @Column(unique = true)
    private String roleName;

    private boolean active = true;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreatedBy
    private User createdBy;

    @LastModifiedBy
    private User updatedBy;

    public Role(String nameUz, String nameRu, String nameUzCry, String nameEn, List<Permissions> permissions, String roleName) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameUzCry = nameUzCry;
        this.nameEn = nameEn;
        this.permissions = permissions;
        this.roleName = roleName;
    }
}
