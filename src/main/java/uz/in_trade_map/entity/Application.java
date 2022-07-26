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

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Application implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    @CreatedBy
    private User createdBy;

    @LastModifiedBy
    private User updatedBy;

    private boolean active = true;

    private String companyNameUz;
    private String companyNameUzCry;
    private String companyNameRu;
    private String companyNameEn;
    private String brandName;
    private String inn;
    private String shortDescriptionUz;
    private String shortDescriptionUzCry;
    private String shortDescriptionRu;
    private String shortDescriptionEn;

    /**
     * Director data
     **/
    private String firstName;
    private String lastName;
    private String middleName;
    private String bossPhone;
    private String bossEmail;

    private int isConfirm;
}
