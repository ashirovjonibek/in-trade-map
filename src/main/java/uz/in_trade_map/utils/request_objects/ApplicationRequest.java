package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Application;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {

    @NotNull
    private String brandName;

    @NotNull
    private String inn;

    @NotNull
    private String companyName;

    private String shortDescriptionUz;
    private String shortDescriptionUzCry;
    private String shortDescriptionRu;
    private String shortDescriptionEn;

    /**
     * Director data
     **/
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String middleName;

    @NotNull
    private String bossPhone;

    @NotNull
    private String bossEmail;

    @NotNull
    private Integer checkPhoneId;

    public static Application convertToApplication(ApplicationRequest request) {
        return Application.builder()
                .active(true)
                /* Boss */
                .bossEmail(request.getBossEmail())
                .companyName(request.getCompanyName())
                .bossPhone(request.getBossPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .brandName(request.getBrandName())
                /* Company */
                .inn(request.getInn())
                .shortDescriptionEn(request.getShortDescriptionEn())
                .shortDescriptionRu(request.getShortDescriptionRu())
                .shortDescriptionUz(request.getShortDescriptionUz())
                .shortDescriptionUzCry(request.getShortDescriptionUzCry())
                .build();
    }
}
