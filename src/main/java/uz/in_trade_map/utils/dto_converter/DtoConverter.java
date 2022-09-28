package uz.in_trade_map.utils.dto_converter;

import org.springframework.security.core.Authentication;
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
        resp.put("roleName", role.getRoleName());
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
        response.put("brandName", company.getBrandName());
        response.put("inn", company.getInn());
        response.put("name", company.getName());
        response.put("shortDescriptionUz", company.getShortDescriptionUz());
        response.put("shortDescriptionRu", company.getShortDescriptionRu());
        response.put("shortDescriptionEn", company.getShortDescriptionEn());
        response.put("shortDescriptionUzCry", company.getShortDescriptionUzCry());
        response.put("descriptionUz", company.getDescriptionUz());
        response.put("descriptionRu", company.getDescriptionRu());
        response.put("descriptionEn", company.getDescriptionEn());
        response.put("alwaysConfirmProducts", company.isProductAlwaysConfirm() ? 1 : 0);
        response.put("descriptionUzCry", company.getDescriptionUzCry());
        response.put("createdById", company.getCreatedBy() != null ? company.getCreatedBy().getId() : null);
        response.put("updatedById", company.getUpdatedBy() != null ? company.getUpdatedBy().getId() : null);
        response.put("logoId", company.getLogo() != null ? company.getLogo().getId() : null);
        response.put("imageId", company.getImage() != null ? company.getImage().getId() : null);
        response.put("certificates", company.getCertificates() != null
                ? company.getCertificates().stream().map(Attachment::getId).collect(Collectors.toList())
                : null);
        if (expand != null) {
            if (expand.contains("contactData")) {
                String s = null;
                if (expand.contains("contactData.location.district")) s = "district";
                Map<String, Object> contactData = new HashMap<>();
                contactData.put("socialMedia", company.getData() != null ? company.getData().getSocialMedia() : null);
                contactData.put("location", company.getData() != null ? DtoConverter.locationDto(company.getData().getLocation(), s) : null);
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

    public static Map<String, Object> applicationDto(Application application, String expand) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> companyInfo = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        response.put("id", application.getId());
        response.put("isConfirm", application.getIsConfirm());
        companyInfo.put("brandName", application.getBrandName());
        companyInfo.put("inn", application.getInn());
        companyInfo.put("name", application.getCompanyName());
        companyInfo.put("shortDescriptionUz", application.getShortDescriptionUz());
        companyInfo.put("shortDescriptionRu", application.getShortDescriptionRu());
        companyInfo.put("shortDescriptionEn", application.getShortDescriptionEn());
        companyInfo.put("shortDescriptionUzCry", application.getShortDescriptionUzCry());
        companyInfo.put("createdById", application.getCreatedBy() != null ? application.getCreatedBy().getId() : null);
        companyInfo.put("createdAt", application.getCreatedAt());
        companyInfo.put("updatedById", application.getUpdatedBy() != null ? application.getUpdatedBy().getId() : null);
        companyInfo.put("updatedAt", application.getUpdatedAt());
        userInfo.put("firstName", application.getFirstName());
        userInfo.put("lastName", application.getLastName());
        userInfo.put("middleName", application.getMiddleName());
        userInfo.put("phone", application.getBossPhone());
        userInfo.put("email", application.getBossEmail());
        response.put("userInfo", userInfo);
        response.put("companyInfo", companyInfo);
        if (expand != null) {
            if (expand.contains("createdBy") && application.getCreatedBy() != null) {
                response.put("createdBy", DtoConverter.createdUpdatedDto(application.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && application.getUpdatedBy() != null) {
                response.put("updatedBy", DtoConverter.createdUpdatedDto(application.getUpdatedBy()));
            }
        }
        return response;
    }

    public static Map<String, Object> newsDto(News news) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", news.getId());
        response.put("generalPhoto", news.getPhoto() != null ? news.getPhoto().getId() : null);
        response.put("labelUz", news.getLabelUz());
        response.put("labelRu", news.getLabelRu());
        response.put("labelEn", news.getLabelEn());
        response.put("labelUzCry", news.getLabelUzCry());
        response.put("shortDescriptionUz", news.getShortDescriptionUz());
        response.put("shortDescriptionRu", news.getShortDescriptionRu());
        response.put("shortDescriptionEn", news.getShortDescriptionEn());
        response.put("shortDescriptionUzCry", news.getShortDescriptionUzCry());
        response.put("descriptionUz", news.getDescriptionUz());
        response.put("descriptionRu", news.getDescriptionRu());
        response.put("descriptionEn", news.getDescriptionEn());
        response.put("descriptionUzCry", news.getDescriptionUzCry());
        return response;
    }

    public static Map<String, Object> emptySpacesDto(EmptySpace emptySpace, String expand) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", emptySpace.getId());
        response.put("descriptionUz", emptySpace.getDescriptionUz());
        response.put("descriptionRu", emptySpace.getDescriptionRu());
        response.put("descriptionEn", emptySpace.getDescriptionEn());
        response.put("descriptionUzCry", emptySpace.getDescriptionUzCry());
        response.put("phoneNumber", emptySpace.getPhoneNumber());
        response.put("fieldSurface", emptySpace.getFieldSurface());
        response.put("functionality", emptySpace.getFunctionality());
        response.put("startingPrice", emptySpace.getStartingPrice());
        response.put("annualPrice", emptySpace.getAnnualPrice());
        response.put("address", emptySpace.getAddress());
        response.put("lat", emptySpace.getLat());
        response.put("isBuild", emptySpace.getIsBuild());
        response.put("isStateProperty", emptySpace.getIsStateProperty());
        response.put("lng", emptySpace.getLng());
        response.put("districtId", emptySpace.getDistrict() != null ? emptySpace.getDistrict().getId() : null);
        response.put("createdById", emptySpace.getCreatedBy() != null ? emptySpace.getCreatedBy().getId() : null);
        response.put("updatedById", emptySpace.getUpdatedBy() != null ? emptySpace.getUpdatedBy().getId() : null);
        response.put("createdAt", emptySpace.getCreatedAt());
        response.put("updatedAt", emptySpace.getUpdatedAt());
        response.put("photos", emptySpace.getPhotos() != null
                ? emptySpace.getPhotos().stream().map(Attachment::getId).collect(Collectors.toList())
                : null);
        if (expand != null) {
            if (expand.contains("district")) {
                response.put("district", emptySpace.getDistrict());
            }
            if (expand.contains("createdBy") && emptySpace.getCreatedBy() != null) {
                response.put("createdBy", DtoConverter.createdUpdatedDto(emptySpace.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && emptySpace.getUpdatedBy() != null) {
                response.put("updatedBy", DtoConverter.createdUpdatedDto(emptySpace.getUpdatedBy()));
            }
        }
        return response;
    }

    public static Map<String, Object> productDto(Product product, String expand) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", product.getId());
        response.put("nameUz", product.getNameUz());
        response.put("nameRu", product.getNameRu());
        response.put("nameEn", product.getNameEn());
        response.put("nameUzCry", product.getNameUzCry());
        response.put("shortDescriptionUz", product.getShortDescriptionUz());
        response.put("shortDescriptionRu", product.getShortDescriptionRu());
        response.put("shortDescriptionEn", product.getShortDescriptionEn());
        response.put("shortDescriptionUzCry", product.getShortDescriptionUzCry());
        response.put("descriptionUz", product.getDescriptionUz());
        response.put("descriptionRu", product.getDescriptionRu());
        response.put("descriptionEn", product.getDescriptionEn());
        response.put("descriptionUzCry", product.getDescriptionUzCry());
        response.put("priceUZS", product.getPriceUZS());
        response.put("priceUSD", product.getPriceUSD());
        response.put("exportPriceUZS", product.getExportPriceUZS());
        response.put("exportPriceUSD", product.getExportPriceUSD());
        response.put("minWeight", product.getMinWeight());
        response.put("weight", product.getWeight());
        response.put("materialType", product.getMaterialType());
        response.put("view", product.getViews());
        response.put("confirmStatus", product.getConfirmStatus());
        response.put("categoryId", product.getCategory() != null ? product.getCategory().getId() : null);
        response.put("companyId", product.getCompany() != null ? product.getCompany().getId() : null);
        response.put("createdById", product.getCreatedBy() != null ? product.getCreatedBy().getId() : null);
        response.put("updatedById", product.getUpdatedBy() != null ? product.getUpdatedBy().getId() : null);
        response.put("createAt", product.getCreatedAt());
        response.put("updatedAt", product.getUpdatedAt());
        response.put("photos", product.getPhotos() != null
                ? product.getPhotos().stream().map(Attachment::getId).collect(Collectors.toList())
                : null);
        if (expand != null) {
            if (expand.contains("createdBy") && product.getCreatedBy() != null) {
                response.put("createdBy", DtoConverter.createdUpdatedDto(product.getCreatedBy()));
            }
            if (expand.contains("company") && product.getCompany() != null) {
                response.put("company", DtoConverter.companyDto(product.getCompany(), expand));
            }
            if (expand.contains("category") && product.getCategory() != null) {
                response.put("category", DtoConverter.categoryDto(product.getCategory(), expand));
            }
            if (expand.contains("updatedBy") && product.getUpdatedBy() != null) {
                response.put("updatedBy", DtoConverter.createdUpdatedDto(product.getUpdatedBy()));
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
        dto.put("roles", user.getRoles().stream().map(DtoConverter::roleDto));
        user.getRoles()
                .forEach(role -> permissions.addAll(role.getPermissions().stream().map(Permissions::getName).collect(Collectors.toList())));
        dto.put("permissions", permissions);
//        dto.put("address", user.getLocation() != null ? DtoConverter.locationDto(user.getLocation(), expand) : null);
        dto.put("companyId", user.getCompany() != null ? user.getCompany().getId() : null);
        if (expand != null) {
            if (expand.contains("company") && user.getCompany() != null) {
                dto.put("company", DtoConverter.companyDto(user.getCompany(), expand));
            }
            if (expand.contains("createdBy") && user.getCreatedBy() != null) {
                dto.put("createdBy", DtoConverter.createdUpdatedDto(user.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && user.getUpdatedBy() != null) {
                dto.put("updatedBy", DtoConverter.createdUpdatedDto(user.getUpdatedBy()));
            }
        }
        return dto;
    }

    public static Map<String, Object> createdUpdatedDto(User user) {
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", user.getId() != null ? user.getId() : "");
            resp.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
            resp.put("lastName", user.getLastName() != null ? user.getLastName() : "");
            resp.put("username", user.getUsername() != null ? user.getUsername() : "");
            return resp;
        } else return null;
    }
}
