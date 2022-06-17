package uz.in_trade_map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.utils.request_objects.LocationRequest;
import uz.in_trade_map.service.LocationService;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
public class LocationController {
    @Autowired
    LocationService locationService;

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String expand) {
        return locationService.findAllBySpec(regionId, districtId, address, size, page - 1, expand);
    }

    @PreAuthorize("hasAnyAuthority('create_location')")
    @PostMapping
    public HttpEntity<?> save(@RequestBody @Validated LocationRequest request) {
        return locationService.saveOrEdit(request);
    }

    @PreAuthorize("hasAnyAuthority('update_location')")
    @PutMapping
    public HttpEntity<?> edit(@RequestBody @Validated LocationRequest request) {
        return locationService.saveOrEdit(request);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable Integer id, @RequestParam(required = false) String expand) {
        return locationService.findById(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_location')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return locationService.deleteLocation(id);
    }
}
