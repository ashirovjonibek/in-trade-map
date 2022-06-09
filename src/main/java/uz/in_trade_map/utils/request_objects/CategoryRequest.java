package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Integer id;

    @NotNull(message = "The nameUz field must not be empty!")
    private String nameUz;

    @NotNull(message = "The nameRu field must not be empty!")
    private String nameRu;

    @NotNull(message = "The nameEn field must not be empty!")
    private String nameEn;

    @NotNull(message = "The nameUzCry field must not be empty!")
    private String nameUzCry;

    @NotNull(message = "The descriptionUz field must not be empty!", maxLength = 1000)
    private String descriptionUz;

    @NotNull(message = "The descriptionRu field must not be empty!", maxLength = 1000)
    private String descriptionRu;

    @NotNull(message = "The descriptionEn field must not be empty!", maxLength = 1000)
    private String descriptionEn;

    @NotNull(message = "The descriptionUzCry field must not be empty!", maxLength = 1000)
    private String descriptionUzCry;

    private Integer categoryId;

    public static Category request(CategoryRequest request) {
        return Category
                .builder()
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .nameUzCry(request.getNameUzCry())
                .descriptionUz(request.getDescriptionUz())
                .descriptionRu(request.getDescriptionRu())
                .descriptionEn(request.getDescriptionEn())
                .descriptionUzCry(request.getDescriptionUzCry())
                .build();
    }
}
