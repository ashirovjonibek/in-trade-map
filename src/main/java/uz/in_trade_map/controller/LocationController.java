package uz.in_trade_map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.payload.LocationRequest;
import uz.in_trade_map.service.LocationService;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    LocationService locationService;

    @GetMapping
    public HttpEntity<?> getAll(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int pageSize, @RequestParam(required = false) String expand) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return locationService.findAll(pageable, expand);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> save(@RequestBody @Validated LocationRequest request) {
        return locationService.saveOrEdit(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public HttpEntity<?> edit(@RequestBody @Validated LocationRequest request) {
        return locationService.saveOrEdit(request);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Integer id, @RequestParam(required = false) String expand) {
        return locationService.findById(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return locationService.deleteLocation(id);
    }
}
