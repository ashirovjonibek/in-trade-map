package uz.in_trade_map.utils.dto_converter;

import org.springframework.stereotype.Component;
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
        }
        return loc;
    }
}
