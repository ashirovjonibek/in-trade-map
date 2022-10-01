package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.News;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import javax.persistence.Column;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {

    @FieldTypeFile(extension = ".png,.jpg,.jpeg", size = 1024 * 1024 * 5)
    private MultipartFile generalPhoto;

    @NotNull(maxLength = 255, minLength = 5)
    private String labelUz;

    @NotNull(maxLength = 255, minLength = 5)
    private String labelRu;

    @NotNull(maxLength = 255, minLength = 5)
    private String labelUzCry;

    @NotNull(maxLength = 255, minLength = 5)
    private String labelEn;

    @NotNull(minLength = 5)
    private String descriptionUz;

    @NotNull(minLength = 5)
    private String descriptionRu;

    @NotNull(minLength = 5)
    private String descriptionEn;

    @NotNull(minLength = 5)
    private String descriptionUzCry;

    @NotNull(maxLength = 1000, minLength = 5)
    private String shortDescriptionUz;

    @NotNull(maxLength = 1000, minLength = 5)
    private String shortDescriptionRu;

    @NotNull(maxLength = 1000, minLength = 5)
    private String shortDescriptionEn;

    @NotNull(maxLength = 1000, minLength = 5)
    private String shortDescriptionUzCry;

    private UUID oldPhotoId;

    public static News convertToEntity(News news, NewsRequest request) {
        news.setActive(true);
        news.setLabelRu(request.getLabelRu());
        news.setLabelEn(request.getLabelEn());
        news.setLabelUz(request.getLabelUz());
        news.setLabelUzCry(request.getLabelUzCry());
        news.setShortDescriptionEn(request.getShortDescriptionEn());
        news.setShortDescriptionRu(request.getShortDescriptionRu());
        news.setShortDescriptionUz(request.getShortDescriptionUz());
        news.setShortDescriptionUzCry(request.getShortDescriptionUzCry());
        news.setDescriptionEn(request.getDescriptionEn());
        news.setDescriptionRu(request.getDescriptionRu());
        news.setDescriptionUz(request.getDescriptionUz());
        news.setDescriptionUzCry(request.getDescriptionUzCry());
        return news;
    }
}
