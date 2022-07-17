package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> create(@ModelAttribute EmptySpaceRequest request) {
        return emptySpaceService.create(request);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> update(@PathVariable Integer id, @ModelAttribute EmptySpaceRequest request) {
        return emptySpaceService.update(id, request);
    }
}
