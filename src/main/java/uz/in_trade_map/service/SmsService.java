package uz.in_trade_map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uz.in_trade_map.entity.CheckPhone;
import uz.in_trade_map.entity.EskizToken;
import uz.in_trade_map.repository.CheckPhoneRepository;
import uz.in_trade_map.repository.EskizTokenRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final CheckPhoneRepository checkPhoneRepository;
    private final PasswordActions passwordActions;
    private final EskizTokenRepository eskizTokenRepository;

    @Value("${sms.shlyuz.email}")
    private String smsEmail;

    @Value("${sms.shlyuz.key}")
    private String smsKey;


    public CheckPhone sendSms(Integer id, String phoneNumber) {
        try {
            Optional<CheckPhone> optionalCheckPhone = Optional.empty();
            if (id != null) {
                optionalCheckPhone = checkPhoneRepository.findById(id);
            }
            if (!phoneNumber.trim().startsWith("+")) {
                String s = phoneNumber.replaceAll(" ", "");
                phoneNumber = "+" + s;
            }
            String shortCode = passwordActions.generateShortCode(6);
            String sms = "http://intrademap.uz saytida raqamni tasdiqlash kodi: " + shortCode + ". Iltimos xabar maxfiyligini saqlang!";
            Map<String, String> smsEskiz = sentSmsEskiz(phoneNumber, sms);
            Timestamp created = new Timestamp(new Date(new java.util.Date().getTime()).getTime());
            Timestamp expired = new Timestamp(new Date(new java.util.Date().getTime() + 5 * 60 * 1000).getTime());
            if (smsEskiz.get("status").equals("error")) {
                throw new IllegalStateException("Error sending sms code. Try again!");
            } else {
                CheckPhone build = optionalCheckPhone.isPresent() ? optionalCheckPhone.get() : CheckPhone.builder()
                        .check(false)
                        .createdAt(created)
                        .expireDate(expired)
                        .code(shortCode)
                        .phoneNumber(phoneNumber)
                        .build();

                if (optionalCheckPhone.isPresent()) {
                    build.setCode(shortCode);
                }
                return checkPhoneRepository.save(build);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error send sms code!");
        }
    }

    public boolean sendSms(String phoneNumber, String text) {
        try {
            Map<String, String> smsEskiz = sentSmsEskiz(phoneNumber, text);
            if (smsEskiz.get("status").equals("error")) {
                throw new IllegalStateException("Error sending sms code. Try again!");
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error send sms code!");
        }
    }

    public Integer checkPhone(Integer id, String code, String phoneNumber) {
        try {
            Optional<CheckPhone> optionalCheckPhone = checkPhoneRepository.findById(id);
            if (optionalCheckPhone.isPresent()) {
                CheckPhone checkPhone = optionalCheckPhone.get();
                if (checkPhone.getCode().equals(code)
                        && checkPhone.getPhoneNumber().replaceAll(" ", "").equals(phoneNumber)
                        && !checkPhone.isCheck()
                        && checkPhone.getExpireDate().getTime() > new java.util.Date().getTime()
                ) {
                    checkPhone.setCheck(true);
                    checkPhoneRepository.save(checkPhone);
                    return id;
                } else return null;
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CheckPhone getChecked(Integer id) {
        try {
            Optional<CheckPhone> optionalCheckPhone = checkPhoneRepository.findById(id);
            return optionalCheckPhone.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getToken() {
        List<EskizToken> all = eskizTokenRepository.findAll();
        if (all.size() > 0) {
            if (!checkTokenTime(all.get(0))) {
                return all.get(0).getToken();
            } else {
                eskizTokenRepository.deleteAll();
                return getToken();
            }
        } else {
            String url = "http://notify.eskiz.uz/api/auth/login";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
            data.add("email", smsEmail);
            data.add("password", smsKey);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(data, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            System.out.println(response.getBody());
            response.getBody();
            EskizToken eskizToken = new EskizToken();
            JsonNode jsonNode = response.getBody();
            if (jsonNode != null) {
                eskizToken.setToken(jsonNode.get("data").get("token").textValue());
            } else getToken();
            eskizToken.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
            EskizToken save = eskizTokenRepository.save(eskizToken);
            return save.getToken();
        }
    }

    private Map<String, String> sentSmsEskiz(String phoneNumber, String text) {
        String url1 = "http://notify.eskiz.uz/api/message/sms/send";
        RestTemplate restTemplate1 = new RestTemplate();
        Map<String, String> resp = new HashMap<>();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers1.add("Authorization", "Bearer " + getToken());
        MultiValueMap<String, String> data1 = new LinkedMultiValueMap<String, String>();
        data1.add("mobile_phone", phoneNumber.substring(1));
        data1.add("message", text);
        data1.add("from", "4546");
        HttpEntity<MultiValueMap<String, String>> requestEntity1 = new HttpEntity<MultiValueMap<String, String>>(data1, headers1);
        try {
            ResponseEntity<JsonNode> exchange = restTemplate1.exchange(url1, HttpMethod.POST, requestEntity1, JsonNode.class);
            resp.put("id", exchange.getBody().get("id").textValue());
            resp.put("status", exchange.getBody().get("status").textValue());
            resp.put("message", exchange.getBody().get("message").textValue());
            return resp;
        } catch (IllegalStateException e) {
            resp.put("status", "error");
            e.printStackTrace();
            return resp;
        }
    }

    private boolean checkTokenTime(EskizToken eskizToken) {
        Timestamp date = new Timestamp(new java.util.Date().getTime());
        Timestamp exp = new Timestamp(eskizToken.getCreatedDate().getTime() + (30 * 86400 * 1000L));
        System.out.println(exp);
        System.out.println(date);
        long l = exp.getTime() - date.getTime();
        System.out.println(l);
        return l < 5;
    }
}
