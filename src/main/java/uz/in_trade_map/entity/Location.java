package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
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
