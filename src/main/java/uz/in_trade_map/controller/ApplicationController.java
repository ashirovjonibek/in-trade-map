package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.ApplicationService;
import uz.in_trade_map.service.SmsService;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/send-sms")
    public HttpEntity<?> sendSms(@RequestParam String phoneNumber, @RequestParam(required = false) Integer id) {
        return applicationService.sendSms(id, phoneNumber);
    }

    @PostMapping(value = "/check-phone", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> checkPhone(@RequestParam Integer id, @RequestParam String code, @RequestParam String phoneNumber) {
        return applicationService.checkPhone(id, code, phoneNumber);
    }
}
