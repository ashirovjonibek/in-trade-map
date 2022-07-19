package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.EmptySpace;

@Component
public class EmptySpaceSpecifications {
    public static Specification<EmptySpace> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId != null ? builder.equal(root
                .get("district")
                .get("region")
                .get("id"), regionId) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId != null ? builder.equal(root
                .get("district")
                .get("id"), districtId) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByAddress(String address) {
        return (root, query, builder) -> address != null ? builder.like(root
                .get("address"), "%" + address + "%") : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByDescriptionUz(String search) {
        return (root, query, builder) -> search != null ? builder.like(root
                .get("descriptionUz"), "%" + search + "%") : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByDescriptionRu(String search) {
        return (root, query, builder) -> search != null ? builder.like(root
                .get("descriptionRu"), "%" + search + "%") : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByDescriptionEn(String search) {
        return (root, query, builder) -> search != null ? builder.like(root
                .get("descriptionEn"), "%" + search + "%") : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findByDescriptionUzCry(String search) {
        return (root, query, builder) -> search != null ? builder.like(root
                .get("descriptionUzCry"), "%" + search + "%") : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByGreaterStartingPrice(Float minSPrice) {
        return (root, query, builder) -> minSPrice != null ? builder.greaterThanOrEqualTo(root
                .get("startingPrice"), minSPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByLessStartingPrice(Float maxSPrice) {
        return (root, query, builder) -> maxSPrice != null ? builder.greaterThanOrEqualTo(root
                .get("startingPrice"), maxSPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByGreaterAnnualPrice(Float minAPrice) {
        return (root, query, builder) -> minAPrice != null ? builder.lessThanOrEqualTo(root
                .get("annualPrice"), minAPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByLessAnnualPrice(Float maxAPrice) {
        return (root, query, builder) -> maxAPrice != null ? builder.lessThanOrEqualTo(root
                .get("annualPrice"), maxAPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}