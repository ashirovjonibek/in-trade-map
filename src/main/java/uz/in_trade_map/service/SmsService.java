package uz.in_trade_map.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.CheckPhone;
import uz.in_trade_map.repository.CheckPhoneRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final CheckPhoneRepository checkPhoneRepository;
    private final PasswordActions passwordActions;

    public CheckPhone sendSms(Integer id, String phoneNumber) {
        try {
            Optional<CheckPhone> optionalCheckPhone = Optional.empty();
            if (id != null) {
                optionalCheckPhone = checkPhoneRepository.findById(id);
            }
            String AUTH_TOKEN = "9292dd5331b7da4fb92c06b6101b77cf";
            String ACCOUNT_SID = "ACec33589aa758cf9b0622f5296b87b885";
            if (!phoneNumber.trim().startsWith("+")) {
                phoneNumber = "+" + phoneNumber;
            }
            String shortCode = passwordActions.generateShortCode(6);
            String sms = "InTradeMap saytida raqamni tasdiqlash kodi: " + shortCode;
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber("+13344542733"),
                    sms)
                    .create();
            Timestamp created = new Timestamp(new Date(new java.util.Date().getTime()).getTime());
            Timestamp expired = new Timestamp(new Date(new java.util.Date().getTime() + 5 * 60 * 1000).getTime());
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
}