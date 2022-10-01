package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.BannerService;
import uz.in_trade_map.utils.request_objects.BannerRequest;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String expand
    ) {
        return bannerService.getAll(size, page, expand);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(
            @PathVariable Integer id,
            @RequestParam(required = false) String expand
    ) {
        return bannerService.getOne(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('create_banner')")
    @PostMapping
    public HttpEntity<?> save(@ModelAttribute BannerRequest request) {
        return bannerService.save(request);
    }

    @PreAuthorize("hasAnyAuthority('update_banner')")
    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable Integer id, @ModelAttribute BannerRequest request) {
        return bannerService.update(id, request, request.getOldPhotoId());
    }

    @PreAuthorize("hasAnyAuthority('delete_banner')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return bannerService.delete(id);
    }
}
