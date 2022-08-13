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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class EmptySpace implements Serializable {
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

    @Column(columnDefinition = "text")
    private String descriptionUz;

    @Column(columnDefinition = "text")
    private String descriptionRu;

    @Column(columnDefinition = "text")
    private String descriptionEn;

    @Column(columnDefinition = "text")
    private String descriptionUzCry;

    private String fieldSurface;

    private String phoneNumber;

    private String functionality;

    private String address;

    private Float lat;

    private Float lng;

    private Integer isStateProperty;

    private Integer isBuild;

    @ManyToOne
    private District district;

    private Float startingPrice;

    private Float annualPrice;

    @OneToMany
    private List<Attachment> photos;

}
