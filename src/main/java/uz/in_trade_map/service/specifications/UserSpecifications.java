package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;

import java.util.List;
import java.util.Set;

@Component
public class UserSpecifications {
    public static Specification<User> findByRegionId(Integer regionId) {
        return (root, query, builder) -> regionId != null ? builder.equal(root
                .get("location")
                .get("district")
                .get("region")
                .get("id"), regionId) : query.getGroupRestriction();
    }

    public static Specification<User> findByDistrictId(Integer districtId) {
        return (root, query, builder) -> districtId != null ? builder.equal(root
                .get("location")
                .get("district")
                .get("id"), districtId) : query.getGroupRestriction();
    }

    public static Specification<User> findByLocationId(Integer locationId) {
        return (root, query, builder) -> locationId != null ? builder.equal(root
                .get("location")
                .get("id"), locationId) : query.getGroupRestriction();
    }

    public static Specification<User> findByUsername(String username) {
        return (root, query, builder) -> username != null ? builder.like(root
                .get("username"), "%" + username + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByUsernameNot(String username) {
        return (root, query, builder) -> username != null ? builder.notEqual(root
                .get("username"), username) : query.getGroupRestriction();
    }

    public static Specification<User> findByFirstName(String firstName) {
        return (root, query, builder) -> firstName != null ? builder.like(root
                .get("firstName"), "%" + firstName + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByRoles(Set<String> roles) {
        return (root, query, builder) -> roles != null ? root.join("roles")
                .get("roleName").in(roles) : query.getGroupRestriction();
    }

    public static Specification<User> findByRolesNot(Set<String> roles) {
        return (root, query, builder) -> roles != null ? builder.not(root
                .join("roles").get("roleName").in(roles)) : query.getGroupRestriction();
    }

    public static Specification<User> findByLastName(String lastName) {
        return (root, query, builder) -> lastName != null ? builder.like(root
                .get("lastName"), "%" + lastName + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByMiddleName(String middleName) {
        return (root, query, builder) -> middleName != null ? builder.like(root
                .get("middleName"), "%" + middleName + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByEmail(String email) {
        return (root, query, builder) -> email != null ? builder.like(root
                .get("email"), "%" + email + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByPhoneNumber(String phoneNumber) {
        return (root, query, builder) -> phoneNumber != null ? builder.like(root
                .get("phoneNumber"), "%" + phoneNumber + "%") : query.getGroupRestriction();
    }

    public static Specification<User> findByCompanyId(Integer companyId) {
        return (root, query, builder) -> companyId != null ? builder.equal(root
                .get("companyId"), companyId) : query.getGroupRestriction();
    }

    public static Specification<User> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}