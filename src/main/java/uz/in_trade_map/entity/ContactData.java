package uz.in_trade_map.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContactData extends AbsNameEntity {

    @JsonIgnore
    private String socialMedia;

    @OneToOne(fetch = FetchType.EAGER)
    private Location location;
}
