package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.BannerService;
import uz.in_trade_map.service.StatisticService;
import uz.in_trade_map.utils.request_objects.BannerRequest;
import uz.in_trade_map.utils.request_objects.StatisticRequest;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String expand
    ) {
        return statisticService.getAll(size, page, expand);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(
            @PathVariable Integer id,
            @RequestParam(required = false) String expand
    ) {
        return statisticService.getOne(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('create_statistic')")
    @PostMapping
    public HttpEntity<?> save(@ModelAttribute StatisticRequest request) {
        return statisticService.save(request);
    }

    @PreAuthorize("hasAnyAuthority('update_statistic')")
    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable Integer id, @ModelAttribute StatisticRequest request) {
        return statisticService.update(id, request);
    }

    @PreAuthorize("hasAnyAuthority('delete_statistic')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return statisticService.delete(id);
    }
}
