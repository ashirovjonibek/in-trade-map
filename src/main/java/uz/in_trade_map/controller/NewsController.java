package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.NewsService;
import uz.in_trade_map.utils.request_objects.NewsRequest;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> save(@ModelAttribute NewsRequest request) {
        return newsService.save(request);
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        return newsService.getAll();
    }
}
