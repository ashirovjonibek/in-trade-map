package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.EmptySpace;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmptySpaceRequest {
    @NotNull
    private String descriptionUz;

    @NotNull
    private String descriptionRu;

    @NotNull
    private String descriptionEn;

    @NotNull
    private String descriptionUzCry;

    @NotNull
    private String fieldSurface;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String functionality;

    @NotNull
    private String address;

    @NotNull
    private Integer districtId;

    @NotNull
    private Float lat;

    @NotNull
    private Float lng;

    private Float startingPrice;

    private Float annualPrice;

    @FieldTypeArray
    @FieldTypeFile(extension = ".png,.jpeg,.jpg", size = 3 * 1024 * 1024)
    private List<MultipartFile> photos;

    public static EmptySpace convertToEmptySpace(EmptySpaceRequest request) {
        return EmptySpace.builder()
                .descriptionEn(request.getDescriptionEn())
                .descriptionRu(request.getDescriptionRu())
                .descriptionUz(request.getDescriptionUz())
                .descriptionUzCry(request.getDescriptionUzCry())
                .fieldSurface(request.getFieldSurface())
                .phoneNumber(request.getPhoneNumber())
                .functionality(request.getFunctionality())
                .address(request.getAddress())
                .lat(request.getLat())
                .lng(request.getLng())
                .startingPrice(request.getStartingPrice())
                .annualPrice(request.getAnnualPrice())
                .build();
    }
}
