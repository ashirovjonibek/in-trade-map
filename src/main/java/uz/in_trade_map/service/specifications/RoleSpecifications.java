package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.Role;

@Component
public class RoleSpecifications {
    public static Specification<Role> findByNameUz(String nameUz) {
        return (root, query, builder) -> nameUz != null ? builder.like(root
                .get("nameUz"), "%" + nameUz + "%") : query.getGroupRestriction();
    }

    public static Specification<Role> findByNameRu(String nameRu) {
        return (root, query, builder) -> nameRu != null ? builder.like(root
                .get("nameRu"), "%" + nameRu + "%") : query.getGroupRestriction();
    }

    public static Specification<Role> findByNameEn(String nameEn) {
        return (root, query, builder) -> nameEn != null ? builder.like(root
                .get("nameEn"), "%" + nameEn + "%") : query.getGroupRestriction();
    }

    public static Specification<Role> findByNameUzCry(String nameUzCry) {
        return (root, query, builder) -> nameUzCry != null ? builder.like(root
                .get("nameUzCry"), "%" + nameUzCry + "%") : query.getGroupRestriction();
    }

    public static Specification<Role> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}