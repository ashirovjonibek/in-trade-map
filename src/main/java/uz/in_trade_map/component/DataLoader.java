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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            Role admin = roleRepository.save(new Role(RoleName.ROLE_ADMIN));
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
            List<Quarter> quarters = new ArrayList<>();
            jsonNode.forEach(n -> {
                regions.add(new Region(n.get("name_uz").textValue(), n.get("name_ru").textValue(), null, n.get("name_oz").textValue()));
            });
            List<Region> regionList = regionRepository.saveAll(regions);
            jsonNode = objectMapper.readValue(district.getURL(), JsonNode.class);
            jsonNode.forEach(n -> {
                Region regionFl = regionList.stream().filter(region1 -> region1.getId().toString().equals(n.get("region_id").textValue())).collect(Collectors.toList()).get(0);
                districts.add(new District(
                        n.get("name_uz").textValue(),
                        n.get("name_ru").textValue(),
                        null,
                        n.get("name_oz").textValue(),
                        regionFl
                ));
            });
            List<District> districtList = districtRepository.saveAll(districts);
            jsonNode = objectMapper.readValue(quarter.getURL(), JsonNode.class);
            jsonNode.forEach(n -> {
                List<District> districtId = districtList.stream().
                        filter(
                                district1 ->
                                        district1.getId().toString()
                                                .equals(n.get("district_id").textValue()))
                        .collect(Collectors.toList());
                quarters.add(new Quarter(
                        n.get("name").textValue(),
                        districtId.size() > 0 ? districtId.get(0) : null
                ));
            });
            quarterRepository.saveAll(quarters);
        }
    }
}