package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.LocationRepository;
import uz.in_trade_map.repository.UserRepository;
import uz.in_trade_map.utils.request_objects.UserRequest;
import uz.in_trade_map.utils.validator.Validator;

@Service
@RequiredArgsConstructor
public class UserService extends Validator<UserRequest> {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;

    public ResponseEntity<?> createUser(UserRequest request) {
        try {
            boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getPhoneNumber());
            boolean existsByUsername = userRepository.existsByUsername(request.getUsername());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create user");
        }
    }
}
