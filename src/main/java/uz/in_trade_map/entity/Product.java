package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String nameUzCry;

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

    @ManyToOne
    private Category category;

    @ManyToOne
    private Company company;

    @OneToMany
    private List<Attachment> photos;

    private Float exportPriceUZS;

    private Float exportPriceUSD;

    private Float priceUZS;

    private Float priceUSD;

    private String minWeight;

    private String weight;

    private String materialType;

    private int views = 0;
}
