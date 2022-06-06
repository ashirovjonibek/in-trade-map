package uz.in_trade_map.utils.dto_converter;

import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.entity.Role;

import java.util.HashMap;
import java.util.Map;

public class DtoConverter {
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
        if (expand != null && expand.contains("quarter")) {
            loc.put("quarter", location.getQuarter());
        } else {
            loc.put("quarterId", location.getQuarter().getId());
        }
        return loc;
    }

    public static Map<String, Object> categoryDto(Category category, String expand) {
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
        resp.put("createdAt", category.getCreatedAt());
        resp.put("updatedAt", category.getUpdatedAt());
        if (expand != null) {
            if (category.getCreatedBy() != null && expand.contains("createdBy")) {
                resp.put("createdBy", category.getCreatedBy());
            }

            if (category.getUpdatedBy() != null && expand.contains("updatedBy")) {
                resp.put("updatedBy", category.getUpdatedBy());
            }

            if (category.getCategory() != null && expand.contains("category")) {
                resp.put("category", category.getCategory());
            }
        }
        if (category.getCreatedBy() != null) {
            resp.put("createdById", category.getCreatedBy().getId());
        }

        if (category.getUpdatedBy() != null) {
            resp.put("updatedById", category.getUpdatedBy().getId());
        }

        if (category.getCategory() != null) {
            resp.put("categoryId", category.getCategory().getId());
        }
        return resp;
    }
}
