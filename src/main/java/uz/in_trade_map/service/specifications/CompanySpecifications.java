package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.Location;

@Component
public class CompanySpecifications {
    public static Specification<Company> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId != null ? builder.equal(root
                .get("data")
                .get("location")
                .get("district")
                .get("region")
                .get("id"), regionId) : query.getGroupRestriction();
    }

    public static Specification<Company> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId != null ? builder.equal(root
                .get("data")
                .get("location")
                .get("district")
                .get("id"), districtId) : query.getGroupRestriction();
    }

    public static Specification<Company> findByLocationId(Integer locationId) {
        return (root, query, builder) -> locationId != null ? builder.equal(root
                .get("data")
                .get("location")
                .get("id"), locationId) : query.getGroupRestriction();
    }

    public static Specification<Company> findByAddress(String address) {
        return (root, query, builder) -> address != null ? builder.like(root
                .get("data")
                .get("location")
                .get("address"), "%" + address + "%") : query.getGroupRestriction();
    }

    public static Specification<Company> findByInn(String inn) {
        return (root, query, builder) -> inn != null ? builder.like(root
                .get("inn"), "%" + inn + "%") : query.getGroupRestriction();
    }

    public static Specification<Company> findByBrandName(String brandName) {
        return (root, query, builder) -> brandName != null ? builder.like(root
                .get("brandName"), "%" + brandName + "%") : query.getGroupRestriction();
    }

//    public static Specification<Company> findByNameUz(String nameUz) {
//        return (root, query, builder) -> nameUz != null ? builder.like(root
//                .get("nameUz"), "%" + nameUz + "%") : query.getGroupRestriction();
//    }
//
//    public static Specification<Company> findByNameRu(String nameRu) {
//        return (root, query, builder) -> nameRu != null ? builder.like(root
//                .get("nameRu"), "%" + nameRu + "%") : query.getGroupRestriction();
//    }
//
//    public static Specification<Company> findByNameEn(String nameEn) {
//        return (root, query, builder) -> nameEn != null ? builder.like(root
//                .get("nameEn"), "%" + nameEn + "%") : query.getGroupRestriction();
//    }
//
//    public static Specification<Company> findByNameUzCry(String nameUzCry) {
//        return (root, query, builder) -> nameUzCry != null ? builder.like(root
//                .get("nameUzCry"), "%" + nameUzCry + "%") : query.getGroupRestriction();
//    }

    public static Specification<Company> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}