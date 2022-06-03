package uz.in_trade_map.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import uz.in_trade_map.entity.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    private Integer id;

    @NonNull
    private String address;

    private Float lat;

    private Float lng;

    @NonNull
    private Integer quarterId;

    public static Location request(LocationRequest request) {
        return Location.builder()
                .id(request.getId())
                .address(request.getAddress())
                .lat(request.getLat())
                .lng(request.getLng())
                .build();
    }
}
