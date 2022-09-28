package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.NewsService;
import uz.in_trade_map.utils.request_objects.NewsRequest;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PreAuthorize("hasAnyAuthority('create_news')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> create(@ModelAttribute NewsRequest request) {
        return newsService.save(request);
    }

    @PreAuthorize("hasAnyAuthority('update_news')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> update(@PathVariable Integer id, @ModelAttribute NewsRequest request) {
        return newsService.edit(id, request, request.getOldPhotoId());
    }

    @GetMapping
    public HttpEntity<?> getAll(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
        return newsService.getAll(size, page);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id) {
        return newsService.getOne(id);
    }

    @PreAuthorize("hasAnyAuthority('delete_news')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return newsService.delete(id);
    }
}
