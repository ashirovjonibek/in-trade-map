package uz.in_trade_map.controller;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.payload.ApiResponse;
import uz.in_trade_map.service.CompanyService;
import uz.in_trade_map.utils.request_objects.CompanyRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController extends Validator<CompanyRequest> {
    private final CompanyService companyService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> save(@ModelAttribute CompanyRequest request) {
        Map<String, Object> valid = valid(request);
        if (valid.size() > 0) {
            return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } else {
            return companyService.save(request);
        }
    }
}
