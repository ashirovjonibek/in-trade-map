package uz.in_trade_map.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.repository.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.sql.init.mode}")
    String mode;

    @Value("classpath:data/region.json")
    Resource region;

    @Value("classpath:data/district.json")
    Resource district;

    @Value("classpath:data/quarter.json")
    Resource quarter;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    QuarterRepository quarterRepository;

    @Autowired
    PermissionsRepository permissionsRepository;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            List<Permissions> permissions = new ArrayList<>();
            permissions.add(new Permissions("create_location", "Create location"));
            permissions.add(new Permissions("update_location", "Update location"));
            permissions.add(new Permissions("delete_location", "Delete location"));
            permissions.add(new Permissions("create_category", "Create category"));
            permissions.add(new Permissions("update_category", "Update category"));
            permissions.add(new Permissions("delete_category", "Delete category"));
            permissions.add(new Permissions("create_company", "Create company"));
            permissions.add(new Permissions("update_company", "Update company"));
            permissions.add(new Permissions("delete_company", "Delete company"));
            permissions.add(new Permissions("create_role", "Create role"));
            permissions.add(new Permissions("update_role", "Update role"));
            permissions.add(new Permissions("get_all_role", "Get all role"));
            permissions.add(new Permissions("get_one_role", "Get one role"));
            permissions.add(new Permissions("delete_role", "delete role"));
            permissions.add(new Permissions("permissions", "Permissions"));
            permissions.add(new Permissions("get_all_user", "Get all user"));
            permissions.add(new Permissions("get_one_user", "Get one user"));
            permissions.add(new Permissions("create_user", "Create user"));
            permissions.add(new Permissions("update_user", "Update user"));
            permissions.add(new Permissions("delete_user", "Delete user"));
            permissions.add(new Permissions("get_user_password", "Get user password"));
            permissions.add(new Permissions("update_user_password", "Update user password"));
            permissions.add(new Permissions("create_product", "Create product"));
            permissions.add(new Permissions("update_product", "Update product"));

            List<Permissions> permissionsList = permissionsRepository.saveAll(permissions);

            List<Role> roles = new ArrayList<>();
            roles.add(new Role("Tekshiruvchi admin", "Администратор чекера", "Текширувчи админ", "Checker admin", null, RoleName.ROLE_CHECKER_ADMIN.name()));
            roles.add(new Role("Direktor", "Директор", "Директор", "Director", null, RoleName.ROLE_COMPANY_DIRECTOR.name()));
            roles.add(new Role("Writer", "Писатель", "Ёзувчи", "Writer", null, RoleName.ROLE_WRITER.name()));
            Role admin = roleRepository.save(new Role("Admin", "Администратор", "Админ", "Admin", permissions, RoleName.ROLE_ADMIN.name()));
            roleRepository.saveAll(roles);
            userRepository.save(new User(
                    "Admin",
                    "Admin",
                    "+998912345678",
                    "admin123",
                    passwordEncoder.encode("admin123"),
                    Collections.singleton(admin)
            ));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(region.getURL(), JsonNode.class);
            List<Region> regions = new ArrayList<>();
            List<District> districts = new ArrayList<>();
            jsonNode.forEach(n -> {
                regions.add(new Region(n.get("name_uz").textValue(), n.get("name_ru").textValue(), "", n.get("name_oz").textValue()));
            });
            List<Region> regionList = regionRepository.saveAll(regions);
            jsonNode = objectMapper.readValue(district.getURL(), JsonNode.class);
            jsonNode.forEach(n -> {
                Region regionFl = regionList.stream().filter(region1 -> region1.getId().toString().equals(n.get("region_id").textValue())).collect(Collectors.toList()).get(0);
                districts.add(new District(
                        n.get("name_uz").textValue(),
                        n.get("name_ru").textValue(),
                        "",
                        n.get("name_oz").textValue(),
                        regionFl
                ));
            });
            List<District> districtList = districtRepository.saveAll(districts);
        } else {
            List<Permissions> permissions = new ArrayList<>();
            List<Permissions> permissionsList = permissionsRepository.findAll();
            permissions.add(new Permissions("get_all_user", "Get all user"));
            permissions.add(new Permissions("get_one_user", "Get one user"));
            permissions.add(new Permissions("create_user", "Create user"));
            permissions.add(new Permissions("update_user", "Update user"));
            permissions.add(new Permissions("delete_user", "Delete user"));
            permissions.add(new Permissions("get_user_password", "Get user password"));
            permissions.add(new Permissions("update_user_password", "Update user password"));
            permissions.add(new Permissions("create_product", "Create product"));
            permissions.add(new Permissions("update_product", "Update product"));

            permissions.forEach(perm -> {
                if (permissionsList.stream().filter(perm1 -> perm.getName().equals(perm1.getName())).count() == 0) {
                    Permissions save = permissionsRepository.save(perm);
                    permissionsList.add(save);
                }
            });
            Optional<Role> optionalRole = roleRepository.findByRoleNameAndActiveTrue(RoleName.ROLE_ADMIN.name());
            if (optionalRole.isPresent() && permissionsList.size() > optionalRole.get().getPermissions().size()) {
                Role role = optionalRole.get();
                role.setPermissions(permissionsList);
                roleRepository.save(role);
            }
        }
    }


}
