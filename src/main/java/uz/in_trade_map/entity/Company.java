package uz.in_trade_map.entity;

import lombok.*;
import uz.in_trade_map.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company extends AbsNameEntity {
    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String nameUzCry;

    private String brandName;

    private String shortDescriptionUz;

    private String shortDescriptionRu;

    private String shortDescriptionEn;

    private String shortDescriptionUzCry;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEn;

    private String descriptionUzCry;

    private String inn;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment image;

    @OneToOne(fetch = FetchType.EAGER)
    private Attachment logo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> certificates;

    @OneToOne(fetch = FetchType.EAGER)
    private ContactData data;
}
