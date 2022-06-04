package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Location;

@Component
public class LocationSpecifications {
    public static Specification<Location> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId!=null?builder.equal(root.get("quarter").get("district").get("region").get("id"), regionId):query.getGroupRestriction();
    }

    public static Specification<Location> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId!=null?builder.equal(root.get("quarter").get("district").get("id"), districtId):query.getGroupRestriction();
    }

    public static Specification<Location> findByQuarterId(Integer quarterId) {
        return (root, query, builder) -> quarterId!=null?builder.equal(root.get("quarter").get("id"), quarterId):query.getGroupRestriction();
    }

    public static Specification<Location> findByAddress(String address) {
        return (root, query, builder) -> address!=null?builder.like(root.get("address"), "%" + address + "%"):query.getGroupRestriction();
    }
}