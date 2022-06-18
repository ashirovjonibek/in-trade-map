package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private District district;

    private String address;

    private Float lat;

    private Float lng;

    public Location(District district, String address, Float lat, Float lng) {
        this.district = district;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }
}
