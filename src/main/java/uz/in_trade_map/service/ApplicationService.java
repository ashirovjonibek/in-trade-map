package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.Application;
import uz.in_trade_map.entity.CheckPhone;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.ApplicationRepository;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.UserRepository;
import uz.in_trade_map.utils.request_objects.ApplicationRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationService extends Validator<ApplicationRequest> {
    private final SmsService smsService;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> sendSms(Integer id, String phoneNumber) {
        CheckPhone checkPhone = smsService.sendSms(id, phoneNumber);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", checkPhone.getId());
        resp.put("sent", checkPhone.getCreatedAt());
        resp.put("expire", checkPhone.getExpireDate());
        return AllApiResponse.response(1, "Sms sent!", resp);
    }

    public ResponseEntity<?> checkPhone(Integer id, String code, String phoneNumber) {
        Integer idRes = smsService.checkPhone(id, code, phoneNumber);
        if (idRes != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", idRes);
            return AllApiResponse.response(1, "Phone number is checked!", resp);
        } else return AllApiResponse.response(500, 0, "Error check phone number!");
    }

    public ResponseEntity<?> create(ApplicationRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getBossPhone());
            boolean existsByEmail = userRepository.existsByEmail(request.getBossEmail());
            boolean existsByBrandNameAndActiveTrue = companyRepository.existsByBrandNameAndActiveTrue(request.getBrandName());
            boolean existsByInnAndActiveTrue = companyRepository.existsByInnAndActiveTrue(request.getInn());
            if (existsByPhoneNumber) valid.put("bossPhone", "Boss phone is exists!");

            if (valid.size() == 0) {
                CheckPhone checked = smsService.getChecked(request.getCheckPhoneId());
                if (checked != null && checked.isCheck()) {
                    return null;
                } else return AllApiResponse.response(422, 0, "Phone number is not checked!");
            } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create application!", e.getMessage());
        }
    }
}
