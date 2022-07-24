package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.CheckPhone;
import uz.in_trade_map.payload.AllApiResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final SmsService smsService;

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
}
