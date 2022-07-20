package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.EmptySpace;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.utils.AuthUser;

@Component
public class ProductSpecifications {
    public static Specification<Product> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId != null ? builder.equal(root
                .get("company")
                .get("data")
                .get("location")
                .get("district")
                .get("region")
                .get("id"), regionId) : query.getGroupRestriction();
    }

    public static Specification<Product> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId != null ? builder.equal(root
                .get("company")
                .get("data")
                .get("location")
                .get("district")
                .get("id"), districtId) : query.getGroupRestriction();
    }

    public static Specification<Product> findByBrandName(String brandName) {
        return (root, query, builder) -> brandName != null ? builder.like(root
                .get("company")
                .get("brandName"), "%" + brandName + "%") : query.getGroupRestriction();
    }

    public static Specification<Product> findByCategoryId(Integer categoryId) {
        return (root, query, builder) -> categoryId != null ? builder.equal(root
                .get("category")
                .get("id"), categoryId) : query.getGroupRestriction();
    }

    public static Specification<Product> findByCompanyId(Integer companyId) {
        return (root, query, builder) -> companyId != null ? builder.equal(root
                .get("company")
                .get("id"), companyId) : query.getGroupRestriction();
    }

    public static Specification<Product> findByNameUz(String nameUz) {
        return (root, query, builder) -> nameUz != null ? builder.like(root
                .get("nameUz"), "%" + nameUz + "%") : query.getGroupRestriction();
    }

    public static Specification<Product> findByNameRu(String nameRu) {
        return (root, query, builder) -> nameRu != null ? builder.like(root
                .get("nameRu"), "%" + nameRu + "%") : query.getGroupRestriction();
    }

    public static Specification<Product> findByNameEn(String nameEn) {
        return (root, query, builder) -> nameEn != null ? builder.like(root
                .get("nameEn"), "%" + nameEn + "%") : query.getGroupRestriction();
    }

    public static Specification<Product> findByNameUzCry(String nameUzCry) {
        return (root, query, builder) -> nameUzCry != null ? builder.like(root
                .get("nameUzCry"), "%" + nameUzCry + "%") : query.getGroupRestriction();
    }

    public static Specification<Product> findByConfirmStatus(Integer status) {
        System.out.println(status);
        return (root, query, builder) -> status != null ? builder.equal(root
                .get("confirmStatus"), status) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByLessPriceUZS(Float maxPriceUZS) {
        return (root, query, builder) -> maxPriceUZS != null ? builder.lessThanOrEqualTo(root
                .get("priceUZS"), maxPriceUZS) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByLessPriceUSD(Float maxPriceUSD) {
        return (root, query, builder) -> maxPriceUSD != null ? builder.lessThanOrEqualTo(root
                .get("priceUSD"), maxPriceUSD) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByGreaterPriceUZS(Float minPriceUZS) {
        return (root, query, builder) -> minPriceUZS != null ? builder.greaterThanOrEqualTo(root
                .get("priceUZS"), minPriceUZS) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByGreaterPriceUSD(Float minPriceUSD) {
        return (root, query, builder) -> minPriceUSD != null ? builder.greaterThanOrEqualTo(root
                .get("priceUSD"), minPriceUSD) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByLessExportPriceUZS(Float maxExportPriceUZS) {
        return (root, query, builder) -> maxExportPriceUZS != null ? builder.lessThanOrEqualTo(root
                .get("exportPriceUZS"), maxExportPriceUZS) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByLessExportPriceUSD(Float maxExportPriceUSD) {
        return (root, query, builder) -> maxExportPriceUSD != null ? builder.lessThanOrEqualTo(root
                .get("exportPriceUSD"), maxExportPriceUSD) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByGreaterExportPriceUZS(Float minExportPriceUZS) {
        return (root, query, builder) -> minExportPriceUZS != null ? builder.greaterThanOrEqualTo(root
                .get("exportPriceUZS"), minExportPriceUZS) : query.getGroupRestriction();
    }

    public static Specification<Product> findAllByGreaterExportPriceUSD(Float minExportPriceUSD) {
        return (root, query, builder) -> minExportPriceUSD != null ? builder.greaterThanOrEqualTo(root
                .get("exportPriceUSD"), minExportPriceUSD) : query.getGroupRestriction();
    }

    public static Specification<Product> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}