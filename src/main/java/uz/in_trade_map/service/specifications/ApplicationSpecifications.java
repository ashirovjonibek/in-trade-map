package uz.in_trade_map.service.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Application;
import uz.in_trade_map.entity.Company;

@Component
public class ApplicationSpecifications {

    public static Specification<Application> findByIsConfirm(Integer isConfirm) {
        return (root, query, builder) -> isConfirm != null ? builder.equal(root
                .get("isConfirm"),  isConfirm ) : query.getGroupRestriction();
    }

    public static Specification<Application> findByBossEmail(String bossEmail) {
        return (root, query, builder) -> bossEmail != null ? builder.like(root
                .get("bossEmail"), "%" + bossEmail + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByBossPhone(String bossPhone) {
        return (root, query, builder) -> bossPhone != null ? builder.like(root
                .get("bossPhone"), "%" + bossPhone + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByFirsName(String firstName) {
        return (root, query, builder) -> firstName != null ? builder.like(root
                .get("firstName"), "%" + firstName + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByLastName(String lastName) {
        return (root, query, builder) -> lastName != null ? builder.like(root
                .get("lastName"), "%" + lastName + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByMiddleName(String middleName) {
        return (root, query, builder) -> middleName != null ? builder.like(root
                .get("middleName"), "%" + middleName + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByInn(String inn) {
        return (root, query, builder) -> inn != null ? builder.like(root
                .get("inn"), "%" + inn + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByBrandName(String brandName) {
        return (root, query, builder) -> brandName != null ? builder.like(root
                .get("brandName"), "%" + brandName + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByNameUz(String companyNameUz) {
        return (root, query, builder) -> companyNameUz != null ? builder.like(root
                .get("companyNameUz"), "%" + companyNameUz + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByNameRu(String companyNameRu) {
        return (root, query, builder) -> companyNameRu != null ? builder.like(root
                .get("companyNameRu"), "%" + companyNameRu + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByNameEn(String companyNameEn) {
        return (root, query, builder) -> companyNameEn != null ? builder.like(root
                .get("companyNameEn"), "%" + companyNameEn + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> findByNameUzCry(String companyNameUzCry) {
        return (root, query, builder) -> companyNameUzCry != null ? builder.like(root
                .get("companyNameUzCry"), "%" + companyNameUzCry + "%") : query.getGroupRestriction();
    }

    public static Specification<Application> activeTrue() {
        return (root, query, builder) -> builder.equal(root
                .get("active"), true);
    }


}