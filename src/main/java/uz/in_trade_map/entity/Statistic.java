package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Statistic implements Serializable {
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

    private String titleUz;

    private String titleRu;

    private String titleEn;

    private String titleUzCry;

    @Column(columnDefinition = "text")
    private String textUz;

    @Column(columnDefinition = "text")
    private String textRu;

    @Column(columnDefinition = "text")
    private String textEn;

    @Column(columnDefinition = "text")
    private String textUzCry;

    private short percent;

    private String additional;
}
