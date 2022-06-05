package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    private Integer id;

    @NotNull(message = "Address is not be empty!",minLength = 15)
    private String address;

    private Float lat;

    private Float lng;

    @NotNull(message = "QuarterId is not be empty!")
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
