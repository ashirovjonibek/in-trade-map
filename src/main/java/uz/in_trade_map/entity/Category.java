package uz.in_trade_map.entity;

import lombok.*;
import uz.in_trade_map.entity.template.AbsNameEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends AbsNameEntity {

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
