package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.*;
import uz.in_trade_map.utils.request_objects.UserRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService extends Validator<UserRequest> {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;
    private final DistrictRepository districtRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createUser(UserRequest request) {
        try {
            boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getPhoneNumber());
            boolean existsByUsername = userRepository.existsByUsername(request.getUsername());
            Map<String, Object> valid = valid(request);
            if (existsByEmail) {
                List<String> err = new ArrayList<>();
                err.add("This email already exists!");
                valid.put("email", err);
            }
            if (existsByPhoneNumber) {
                List<String> err = new ArrayList<>();
                err.add("This phone already exists!");
                valid.put("phone", err);
            }
            if (existsByUsername) {
                List<String> err = new ArrayList<>();
                err.add("This username already exists!");
                valid.put("username", err);
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else {
                User convertToUser = UserRequest.convertToUser(request);
                if (request.getImage() != null) {
                    convertToUser.setImage(attachmentService.uploadFile(request.getImage()));
                }
                Optional<Company> optionalCompany = companyRepository.findByIdAndActiveTrue(request.getCompanyId());
                if (optionalCompany.isPresent()) {
                    convertToUser.setCompany(optionalCompany.get());
                } else return AllApiResponse.response(404, 0, "Company not fount with id!");

                Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
                if (optionalDistrict.isPresent()) {
                    Location save = locationRepository.save(new Location(optionalDistrict.get(), request.getAddress(), request.getLat(), request.getLng()));
                    convertToUser.setLocation(save);
                } else return AllApiResponse.response(404, 0, "District not fount with id!");
                Set<Role> roles = roleRepository.findAllByRoleNameIn(request.getRoles());
                convertToUser.setRoles(roles);
                convertToUser.setPassword(passwordEncoder.encode(request.getPassword()));
                User user = userRepository.save(convertToUser);
                return AllApiResponse.response(1, "Success", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create user");
        }
    }
}
