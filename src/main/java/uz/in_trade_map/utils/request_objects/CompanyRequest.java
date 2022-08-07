package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {

    @NotNull
    private String brandName;

    private String shortDescriptionUz;

    private String shortDescriptionRu;

    private String shortDescriptionEn;

    private String shortDescriptionUzCry;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEn;

    private String descriptionUzCry;

    @NotNull(message = "Inn should not be empty!")
    private String inn;

    @FieldTypeFile(extension = ".jpg,.jpeg,.png", size = 1024 * 1024)
    private MultipartFile image;

    @FieldTypeFile(extension = ".jpg,.jpeg,.png", size = 1024 * 1024)
    private MultipartFile logo;

    @FieldTypeArray(maxLength = 10)
    @FieldTypeFile(extension = ".pdf,.jpg,.jpeg,.png", size = 5 * 1024 * 1024)
    private MultipartFile[] certificates;

    private String socialMedia;

    @NotNull
    private Integer districtId;

    @NotNull
    private String address;

    private Float lat;

    private Float lng;

    private boolean productAlwaysConfirm;

    public static Company convertCompany(CompanyRequest request) {
        return Company.builder()
                .brandName(request.getBrandName())
                .descriptionEn(request.getDescriptionEn())
                .descriptionRu(request.getDescriptionRu())
                .descriptionUz(request.getDescriptionUz())
                .descriptionUzCry(request.getDescriptionUzCry())
                .shortDescriptionEn(request.getShortDescriptionEn())
                .shortDescriptionRu(request.getShortDescriptionRu())
                .shortDescriptionUz(request.getShortDescriptionUz())
                .shortDescriptionUzCry(request.getShortDescriptionUzCry())
                .inn(request.getInn())
                .active(true)
                .blocked(false)
                .productAlwaysConfirm(request.isProductAlwaysConfirm())
                .build();
    }
}
