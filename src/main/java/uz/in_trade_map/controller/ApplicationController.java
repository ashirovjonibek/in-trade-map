package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.ApplicationService;
import uz.in_trade_map.service.SmsService;
import uz.in_trade_map.utils.request_objects.ApplicationRequest;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> createApp(@ModelAttribute ApplicationRequest request) {
        return applicationService.create(request);
    }

    @PreAuthorize("hasAnyAuthority('confirm_application')")
    @PutMapping("/confirm/{id}")
    public HttpEntity<?> confirm(@PathVariable Integer id) {
        return applicationService.confirmApplication(id);
    }

    @PreAuthorize("hasAnyAuthority('get_all_applications')")
    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String inn,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String bossPhone,
            @RequestParam(required = false) String bossEmail,
            @RequestParam(required = false) Integer isConfirm,
            @RequestParam(required = false) String expand,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size
    ) {
        return applicationService.getAll(search, brandName, inn, firstName, lastName, middleName, bossPhone, bossEmail, isConfirm, page, size, expand);
    }

    @PreAuthorize("hasAnyAuthority('get_one_application')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(
            @PathVariable Integer id,
            @RequestParam(required = false) String expand
    ) {
        return applicationService.getOne(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_application')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return applicationService.deleteApplication(id);
    }
}
