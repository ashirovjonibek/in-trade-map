package uz.in_trade_map.utils.dto_converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.utils.AuthUser;

import java.util.*;
import java.util.stream.Collectors;

public class DtoConverter {
    Authentication user = AuthUser.getCurrentUser();

    public static Map<String, Object> roleDto(Role role) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", role.getId());
        resp.put("nameUz", role.getNameUz());
        resp.put("nameRu", role.getNameRu());
        resp.put("nameEn", role.getNameEn());
        resp.put("nameUzCry", role.getNameUzCry());
        resp.put("RoleName", role.getRoleName());
        return resp;
    }

    public static Map<String, Object> locationDto(Location location, String expand) {
        Map<String, Object> loc = new HashMap<>();
        loc.put("id", location.getId());
        loc.put("address", location.getAddress());
        loc.put("lat", location.getLat());
        loc.put("lng", location.getLng());
        if (expand != null && expand.contains("district")) {
            loc.put("district", location.getDistrict());
        }
        return loc;
    }

    public static Map<String, Object> categoryDto(Category category, String expand) {
        if (category != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", category.getId());
            resp.put("nameUz", category.getNameUz());
            resp.put("nameRu", category.getNameRu());
            resp.put("nameEn", category.getNameEn());
            resp.put("nameUzCry", category.getNameUzCry());
            resp.put("descriptionUz", category.getDescriptionUz());
            resp.put("descriptionRu", category.getDescriptionRu());
            resp.put("descriptionEn", category.getDescriptionEn());
            resp.put("descriptionUzCry", category.getDescriptionUzCry());
            resp.put("categoryId", category.getCategory() != null ? category.getCategory().getId() : null);
            resp.put("createdAt", category.getCreatedAt());
            resp.put("updatedAt", category.getUpdatedAt());
            resp.put("createdById", category.getCreatedBy() != null ? category.getCreatedBy().getId() : null);
            resp.put("updatedById", category.getUpdatedBy() != null ? category.getUpdatedBy().getId() : null);
            if (expand != null) {
                if (expand.contains("createdBy")) {
                    resp.put("createdBy", DtoConverter.createdUpdatedDto(category.getCreatedBy()));
                }
                if (expand.contains("updatedBy")) {
                    resp.put("updatedBy", DtoConverter.createdUpdatedDto(category.getUpdatedBy()));
                }
                if (expand.contains("category")) {
                    resp.put("category", DtoConverter.categoryDto(category.getCategory(), null));
                }
            }
            return resp;
        } else return null;
    }

    public static Map<String, Object> companyDto(Company company, String expand) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", company.getId());
        response.put("nameUz", company.getNameUz());
        response.put("nameRu", company.getNameRu());
        response.put("nameEn", company.getNameEn());
        response.put("nameUzCry", company.getNameUzCry());
        response.put("brandName", company.getBrandName());
        response.put("inn", company.getInn());
        response.put("shortDescriptionUz", company.getShortDescriptionUz());
        response.put("shortDescriptionRu", company.getShortDescriptionRu());
        response.put("shortDescriptionEn", company.getShortDescriptionEn());
        response.put("shortDescriptionUzCry", company.getShortDescriptionUzCry());
        response.put("descriptionUz", company.getDescriptionUz());
        response.put("descriptionRu", company.getDescriptionRu());
        response.put("descriptionEn", company.getDescriptionEn());
        response.put("descriptionUzCry", company.getDescriptionUzCry());
        response.put("createdById", company.getCreatedBy() != null ? company.getCreatedBy().getId() : null);
        response.put("updatedById", company.getUpdatedBy() != null ? company.getUpdatedBy().getId() : null);
        response.put("logoId", company.getLogo() != null ? company.getLogo().getId() : null);
        response.put("imageId", company.getImage() != null ? company.getImage().getId() : null);
        response.put("certificates", company.getCertificates() != null
                ? company.getCertificates().stream().map(cer -> cer.getId()).collect(Collectors.toList())
                : null);
        if (expand != null) {
            if (expand.contains("contactData")) {
                String s = null;
                if (expand.contains("contactData.location.district")) s = "district";
                Map<String, Object> contactData = new HashMap<>();
                contactData.put("socialMedia", company.getData().getSocialMedia());
                contactData.put("location", DtoConverter.locationDto(company.getData().getLocation(), s));
                response.put("contactData", contactData);
            }
            if (expand.contains("createdBy") && company.getCreatedBy() != null) {
                response.put("createdBy", DtoConverter.createdUpdatedDto(company.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && company.getUpdatedBy() != null) {
                response.put("updatedBy", DtoConverter.createdUpdatedDto(company.getUpdatedBy()));
            }
        }
        return response;
    }

    public static Map<String, Object> roleDto(Role role, String expand) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", role.getId());
        response.put("nameUz", role.getNameUz());
        response.put("nameRu", role.getNameRu());
        response.put("nameEn", role.getNameEn());
        response.put("nameUzCry", role.getNameUzCry());
        response.put("roleName", role.getRoleName());
        response.put("createdAt", role.getCreatedAt());
        response.put("updatedAt", role.getUpdatedAt());

        if (expand != null) {
            if (expand.contains("permissions") && role.getPermissions() != null) {
                response.put("permissions", role.getPermissions().stream().map(Permissions::getName).collect(Collectors.toList()));
            }

            if (expand.contains("createdBy") && role.getCreatedBy() != null) {
                response.put("createdBy", DtoConverter.createdUpdatedDto(role.getCreatedBy()));
            }

            if (expand.contains("updatedBy") && role.getUpdatedBy() != null) {
                response.put("updatedBy", DtoConverter.createdUpdatedDto(role.getUpdatedBy()));
            }
        }
        return response;
    }

    public static Map<String, Object> userDto(User user, String expand) {
        Map<String, Object> dto = new HashMap<>();
        Set<String> permissions = new HashSet<>();
        dto.put("id", user.getId());
        dto.put("firstName", user.getFirstName());
        dto.put("lastName", user.getLastName());
        dto.put("middleName", user.getMiddleName());
        dto.put("username", user.getUsername());
        dto.put("email", user.getEmail());
        dto.put("phoneNumber", user.getPhoneNumber());
        dto.put("image", user.getImage() != null ? user.getImage().getId() : null);
        dto.put("roles", user.getRoles().stream().map(role -> {
            permissions.addAll(role.getPermissions().stream().map(Permissions::getName).collect(Collectors.toSet()));
            return DtoConverter.roleDto(role);
        }));
        dto.put("permissions", permissions);
        dto.put("address", user.getLocation() != null ? DtoConverter.locationDto(user.getLocation(), expand) : null);
        dto.put("companyId", user.getCompany() != null ? user.getCompany().getId() : null);
        if (expand != null) {
            if (expand.contains("company") && user.getCompany() != null) {
                dto.put("company", DtoConverter.companyDto(user.getCompany(), expand));
            }
        }
        return dto;
    }

    public static Map<String, Object> createdUpdatedDto(User user) {
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
            resp.put("lastName", user.getLastName() != null ? user.getLastName() : "");
            resp.put("username", user.getUsername() != null ? user.getUsername() : "");
            return resp;
        } else return null;
    }
}
