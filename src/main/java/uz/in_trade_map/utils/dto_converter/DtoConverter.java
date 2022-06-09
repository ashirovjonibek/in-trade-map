package uz.in_trade_map.utils.dto_converter;

import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;

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
    }


    public static Map<String, Object> createdUpdatedDto(User user) {
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
            resp.put("lastName", user.getLastName() != null ? user.getLastName() : "");
            resp.put("username", user.getUsername() != null ? user.getUsername() : "");
            resp.put("id", user.getId());
            return resp;
        } else return null;
    }
}
