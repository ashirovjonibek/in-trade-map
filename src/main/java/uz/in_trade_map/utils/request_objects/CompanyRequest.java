package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.ContactData;
import uz.in_trade_map.entity.Quarter;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String nameUzCry;

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

    @FieldTypeFile(extension = ".img,.png",size = 1024*1024)
    private MultipartFile image;

    @FieldTypeFile(extension = ".img,.png",size = 1024*1024)
    private MultipartFile logo;

    @FieldTypeArray(maxLength = 10)
    @FieldTypeFile(extension = ".pdf,.img,.png",size = 5*1024*1024)
    private MultipartFile[] certificates;

    private String socialMedia;

    @NotNull
    private Integer quarterId;

    @NotNull
    private String address;

    private Float lat;

    private Float lng;
}
