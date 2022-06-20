package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.ProductService;
import uz.in_trade_map.utils.request_objects.ProductRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('create_product')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> create(@ModelAttribute ProductRequest request) {
        return productService.create(request);
    }

    @PreAuthorize("hasAnyAuthority('update_product')")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> update(@PathVariable UUID id, @ModelAttribute ProductRequest request) {
        return productService.update(id, request);
    }

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String expand
    ) {
        return productService.getAll(search, districtId, regionId, categoryId, companyId, brandName, size, page, expand);
    }
}
