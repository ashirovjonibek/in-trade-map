package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.CategoryService;
import uz.in_trade_map.utils.request_objects.CategoryRequest;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        if (size == 0) size = Math.toIntExact(categoryService.getCategoryCount());
        Pageable pageable = PageRequest.of(page - 1, size);
        return categoryId == null ? categoryService.getAll(pageable, expand) : categoryService.getAllByCategoryId(categoryId, pageable, expand);
    }

    @PreAuthorize("hasAnyAuthority('create_category')")
    @PostMapping
    public HttpEntity<?> save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    @PreAuthorize("hasAnyAuthority('update_category')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody CategoryRequest request) {
        request.setId(id);
        return categoryService.edit(request);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id, @RequestParam(required = false) String expand) {
        return categoryService.getOne(id, expand);
    }

    @PreAuthorize("hasAnyAuthority('delete_category')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return categoryService.delete(id);
    }
}
