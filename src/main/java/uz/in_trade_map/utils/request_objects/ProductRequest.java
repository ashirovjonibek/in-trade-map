package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull(minLength = 15, maxLength = 240)
    private String nameUz;

    @NotNull(minLength = 15, maxLength = 240)
    private String nameRu;

    @NotNull(minLength = 15, maxLength = 240)
    private String nameEn;

    @NotNull(minLength = 15, maxLength = 240)
    private String nameUzCry;

    @NotNull(minLength = 15, maxLength = 999)
    private String shortDescriptionUz;

    @NotNull(minLength = 15, maxLength = 999)
    private String shortDescriptionRu;

    @NotNull(minLength = 15, maxLength = 999)
    private String shortDescriptionEn;

    @NotNull(minLength = 15, maxLength = 999)
    private String shortDescriptionUzCry;

    @NotNull(minLength = 15)
    private String descriptionUz;

    @NotNull(minLength = 15)
    private String descriptionRu;

    @NotNull(minLength = 15)
    private String descriptionEn;

    @NotNull(minLength = 15)
    private String descriptionUzCry;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer companyId;

    @FieldTypeFile(extension = ".png,.jpg,.jpeg", size = 2 * 1024 * 1024)
    @FieldTypeArray(minLength = 2, maxLength = 6)
    private List<MultipartFile> photos;

    @NotNull
    private Float exportPriceUZS;

    @NotNull
    private Float exportPriceUSD;

    @NotNull
    private Float priceUZS;

    @NotNull
    private Float priceUSD;

    private String minWeight;

    private String weight;

    private String materialType;

    private List<UUID> oldPhotoIds;

    private Integer confirmStatus;

    public static Product convertToProduct(ProductRequest request) {
        return Product.builder()
                .descriptionEn(request.getDescriptionEn())
                .descriptionRu(request.getDescriptionRu())
                .descriptionUz(request.getDescriptionUz())
                .descriptionUzCry(request.getDescriptionUzCry())
                .shortDescriptionEn(request.getShortDescriptionEn())
                .shortDescriptionRu(request.getShortDescriptionRu())
                .shortDescriptionUz(request.getShortDescriptionUz())
                .shortDescriptionUzCry(request.getShortDescriptionUzCry())
                .nameEn(request.getNameEn())
                .nameRu(request.getNameRu())
                .nameUz(request.getNameUz())
                .nameUzCry(request.getNameUzCry())
                .priceUSD(request.getPriceUSD())
                .priceUZS(request.getPriceUZS())
                .exportPriceUSD(request.getExportPriceUSD())
                .exportPriceUZS(request.getExportPriceUZS())
                .minWeight(request.getMinWeight())
                .weight(request.getWeight())
                .materialType(request.getMaterialType())
                .views(0)
                .active(true)
                .build();
    }

    public static Product convertToProduct(ProductRequest request, Product product) {
        product.setDescriptionEn(request.getDescriptionEn());
        product.setDescriptionRu(request.getDescriptionRu());
        product.setDescriptionUz(request.getDescriptionUz());
        product.setDescriptionUzCry(request.getDescriptionUzCry());
        product.setShortDescriptionEn(request.getShortDescriptionEn());
        product.setShortDescriptionRu(request.getShortDescriptionRu());
        product.setShortDescriptionUz(request.getShortDescriptionUz());
        product.setShortDescriptionUzCry(request.getShortDescriptionUzCry());
        product.setNameEn(request.getNameEn());
        product.setNameRu(request.getNameRu());
        product.setNameUz(request.getNameUz());
        product.setNameUzCry(request.getNameUzCry());
        product.setPriceUSD(request.getPriceUSD());
        product.setPriceUZS(request.getPriceUZS());
        product.setExportPriceUSD(request.getExportPriceUSD());
        product.setExportPriceUZS(request.getExportPriceUZS());
        product.setMinWeight(request.getMinWeight());
        product.setWeight(request.getWeight());
        product.setMaterialType(request.getMaterialType());
        return product;
    }
}
