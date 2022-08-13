package uz.in_trade_map.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Company implements Serializable {
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

    @Column(unique = true)
    private String brandName;

    private String name;

    @Column(columnDefinition = "text", length = 1000)
    private String shortDescriptionUz;

    @Column(columnDefinition = "text", length = 1000)
    private String shortDescriptionRu;

    @Column(columnDefinition = "text", length = 1000)
    private String shortDescriptionEn;

    @Column(columnDefinition = "text", length = 1000)
    private String shortDescriptionUzCry;

    @Column(columnDefinition = "text")
    private String descriptionUz;

    @Column(columnDefinition = "text")
    private String descriptionRu;

    @Column(columnDefinition = "text")
    private String descriptionEn;

    @Column(columnDefinition = "text")
    private String descriptionUzCry;

    @Column(unique = true)
    private String inn;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment image;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment logo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> certificates;

    @OneToOne(fetch = FetchType.EAGER)
    private ContactData data;

    private boolean productAlwaysConfirm = false;

    private boolean blocked = false;

    public Company(Application application, boolean active) {
        this.brandName = application.getBrandName();
        this.name=application.getCompanyName();
        this.shortDescriptionUz = application.getShortDescriptionUz();
        this.shortDescriptionRu = application.getShortDescriptionRu();
        this.shortDescriptionEn = application.getShortDescriptionEn();
        this.shortDescriptionUzCry = application.getShortDescriptionUzCry();
        this.inn = application.getInn();
        this.active = active;
    }
}
