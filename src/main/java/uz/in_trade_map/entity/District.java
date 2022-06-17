package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class District implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String nameUzCry;

    @ManyToOne
    private Region region;

    public District(String nameUz, String nameRu, String nameEn, String nameUzCry, Region region) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.nameUzCry = nameUzCry;
        this.region = region;
    }
}
