package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Company;

@Component
public class EmptySpaceSpecifications {
    public static Specification<Company> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId != null ? builder.equal(root
                .get("district")
                .get("region")
                .get("id"), regionId) : query.getGroupRestriction();
    }

    public static Specification<Company> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId != null ? builder.equal(root
                .get("district")
                .get("id"), districtId) : query.getGroupRestriction();
    }

    public static Specification<Company> findByAddress(String address) {
        return (root, query, builder) -> address != null ? builder.like(root
                .get("address"), "%" + address + "%") : query.getGroupRestriction();
    }
//
//    public static Specification<Company> findByAnnualPrice(String annualPrice) {
//        return (root, query, builder) -> annualPrice != null ? builder.th(root
//                .get("annualPrice"), "%" + annualPrice + "%") : query.getGroupRestriction();
//    }


    public static Specification<Company> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}