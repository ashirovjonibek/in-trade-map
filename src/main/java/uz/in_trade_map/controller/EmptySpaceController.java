package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.EmptySpaceService;
import uz.in_trade_map.utils.request_objects.EmptySpaceRequest;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empty-spaces")
@RequiredArgsConstructor
public class EmptySpaceController {
    private final EmptySpaceService emptySpaceService;

    @PreAuthorize("hasAnyAuthority('create_empty_space')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> create(@ModelAttribute EmptySpaceRequest request) {
        return emptySpaceService.create(request);
    }

    @PreAuthorize("hasAnyAuthority('update_empty_space')")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> update(@PathVariable Integer id, @ModelAttribute EmptySpaceRequest request) {
        return emptySpaceService.update(id, request);
    }

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Float minStartingPrice,
            @RequestParam(required = false) Float maxStartingPrice,
            @RequestParam(required = false) Float minAnnualPrice,
            @RequestParam(required = false) Float maxAnnualPrice,
            @RequestParam(required = false) Integer isStateProperty,
            @RequestParam(required = false) Integer isBuild
    ) {
        return emptySpaceService.getAll(page, size, expand, regionId, districtId, address, search, minStartingPrice, maxStartingPrice, minAnnualPrice, maxAnnualPrice,isStateProperty,isBuild);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id, @RequestParam(required = false) String expand) {
        return emptySpaceService.getOne(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_empty_space')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return emptySpaceService.delete(id);
    }
}
