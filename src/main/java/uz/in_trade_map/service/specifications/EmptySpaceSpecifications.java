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

    public static Specification<EmptySpace> findAllByGreaterStartingPrice(Float maxPrice) {
        return (root, query, builder) -> maxPrice != null ? builder.greaterThanOrEqualTo(root
                .get("startingPrice"), maxPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByLessStartingPrice(Float minPrice) {
        return (root, query, builder) -> minPrice != null ? builder.greaterThanOrEqualTo(root
                .get("startingPrice"), minPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByGreaterAnnualPrice(Float maxPrice) {
        return (root, query, builder) -> maxPrice != null ? builder.lessThanOrEqualTo(root
                .get("annualPrice"), maxPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> findAllByLessAnnualPrice(Float minPrice) {
        return (root, query, builder) -> minPrice != null ? builder.lessThanOrEqualTo(root
                .get("annualPrice"), minPrice) : query.getGroupRestriction();
    }

    public static Specification<EmptySpace> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}