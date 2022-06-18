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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Category  implements Serializable {

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

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String nameUzCry;

    @Column(columnDefinition = "text", length = 1000)
    private String descriptionUz;

    @Column(columnDefinition = "text", length = 1000)
    private String descriptionRu;

    @Column(columnDefinition = "text", length = 1000)
    private String descriptionEn;

    @Column(columnDefinition = "text", length = 1000)
    private String descriptionUzCry;

    @ManyToOne
    private Category category;
}
