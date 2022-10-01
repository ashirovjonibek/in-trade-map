package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.Banner;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequest {
    private String titleUz;

    private String titleRu;

    private String titleEn;

    private String titleUzCry;

    private String textUz;

    private String textRu;

    private String textEn;

    private String textUzCry;

    @FieldTypeFile(extension = ".png,.jpg,.jpeg", size = 1024 * 1024 * 5)
    private MultipartFile photo;

    @NotNull
    private UUID productId;

    private UUID oldPhotoId;

    public static Banner convertToEntity(BannerRequest request, Banner banner) {
        banner.setActive(true);
        banner.setTextEn(request.getTextEn());
        banner.setTextUz(request.getTextUz());
        banner.setTextUzCry(request.getTextUzCry());
        banner.setTextRu(request.getTextRu());
        banner.setTitleEn(request.getTitleEn());
        banner.setTitleUz(request.getTitleUz());
        banner.setTitleUzCry(request.getTitleUzCry());
        banner.setTitleRu(request.getTitleRu());
        return banner;
    }
}
