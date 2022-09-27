package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import javax.persistence.Column;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {
    @FieldTypeFile(extension = ".png,.jpg,.jpeg",size = 1024*1024*2)
    private MultipartFile generalPhoto;

    @NotNull(maxLength = 255,minLength = 5)
    private String labelUz;

    @NotNull(maxLength = 255,minLength = 5)
    private String labelRu;

    @NotNull(maxLength = 255,minLength = 5)
    private String labelUzCry;

    @NotNull(maxLength = 255,minLength = 5)
    private String labelUzEn;

    @NotNull(minLength = 5)
    private String descriptionUz;

    @NotNull(minLength = 5)
    private String descriptionRu;

    @NotNull(minLength = 5)
    private String descriptionEn;

    @NotNull(minLength = 5)
    private String descriptionUzCry;

    @NotNull(maxLength = 1000,minLength = 5)
    private String shortDescriptionUz;

    @NotNull(maxLength = 1000,minLength = 5)
    private String shortDescriptionRu;

    @NotNull(maxLength = 1000,minLength = 5)
    private String shortDescriptionEn;

    @NotNull(maxLength = 1000,minLength = 5)
    private String shortDescriptionUzCry;
}
