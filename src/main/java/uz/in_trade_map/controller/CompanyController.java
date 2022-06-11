package uz.in_trade_map.controller;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.utils.request_objects.CompanyRequest;

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
public class CompanyController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> save(@ModelAttribute CompanyRequest request) throws IOException {
        System.out.println(request.toString());
        Map<String, Object> resp = new HashMap<>();
        resp.put("image", request.getImage().getSize());
        resp.put("cert", Arrays.stream(request.getCertificates()).map((Function<MultipartFile, Object>) MultipartFile::getSize).collect(Collectors.toList()));
        resp.put("logo", request.getLogo().getSize());
        return ResponseEntity.ok(resp);
    }
}
