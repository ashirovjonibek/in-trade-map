package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Application;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    private String companyNameUz;
    private String companyNameUzCry;
    private String companyNameRu;
    private String companyNameEn;
    private String brandName;
    private String inn;
    private String email;
    private String shortDescriptionUz;
    private String shortDescriptionUzCry;
    private String shortDescriptionRu;
    private String shortDescriptionEn;

    /**
     * Director data
     **/
    private String firstName;
    private String lastName;
    private String middleName;
    private String bossPhone;
    private String bossEmail;

    private Integer checkPhoneId;

    public static Application convertToApplication(ApplicationRequest request) {
        return Application.builder()
                .active(true)
                /* Boss */
                .bossEmail(request.getBossEmail())
                .bossPhone(request.getBossPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .brandName(request.getBrandName())
                /* Company */
                .companyNameEn(request.getCompanyNameEn())
                .companyNameRu(request.getCompanyNameRu())
                .companyNameUz(request.getCompanyNameUz())
                .companyNameUzCry(request.getCompanyNameUzCry())
                .email(request.getEmail())
                .inn(request.getInn())
                .shortDescriptionEn(request.getShortDescriptionEn())
                .shortDescriptionRu(request.getShortDescriptionRu())
                .shortDescriptionUz(request.getShortDescriptionUz())
                .shortDescriptionUzCry(request.getShortDescriptionUzCry())
                .build();
    }
}
