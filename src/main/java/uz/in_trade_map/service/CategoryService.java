package uz.in_trade_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CategoryRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.CategoryRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService extends Validator<CategoryRequest> {
    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity<?> save(CategoryRequest request) {
        Map<String, Object> valid = valid(request);
        if (valid.size() == 0) {
            Category category = CategoryRequest.request(request);
            if (request.getCategoryId() != null) {
                Optional<Category> byId = categoryRepository.findById(request.getCategoryId());
                if (byId.isPresent()) {
                    category.setCategory(byId.get());
                } else return AllApiResponse.response(404, 0, "Category not found with id!");
            }
            try {
                Category save = categoryRepository.save(category);
                return AllApiResponse.response(1, "Category successfully saved!", DtoConverter.categoryDto(save, null));
            } catch (Exception e) {
                e.printStackTrace();
                return AllApiResponse.response(500, 0, "Error saved category", e.getMessage());
            }
        } else return AllApiResponse.response(422, 0, "Validator errors", valid);
    }

    public ResponseEntity<?> edit(CategoryRequest request) {
        Optional<Category> byId1 = categoryRepository.findById(request.getId());
        if (byId1.isPresent()) {
            Map<String, Object> valid = valid(request);
            if (valid.size() == 0) {
                Category category = CategoryRequest.request(request);
                category.setId(request.getId());
                if (request.getCategoryId() != null) {
                    Optional<Category> byId = categoryRepository.findById(request.getCategoryId());
                    if (byId.isPresent()) {
                        category.setCategory(byId.get());
                    } else return AllApiResponse.response(404, 0, "Category not found with id!");
                }
                try {
                    Category save = categoryRepository.save(category);
                    return AllApiResponse.response(1, "Category successfully updated!", DtoConverter.categoryDto(save, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    return AllApiResponse.response(500, 0, "Error saved category", e.getMessage());
                }
            } else return AllApiResponse.response(422, 0, "Validator errors", valid);
        } else return AllApiResponse.response(404, 0, "Category not found for edit!");
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Category> byId = categoryRepository.findByIdAndActiveTrue(id);
            if (byId.isPresent()) {
                return AllApiResponse.response(1, "Success", DtoConverter.categoryDto(byId.get(), expand));
            } else return AllApiResponse.response(404, 0, "Category not found with id");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one category", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(Pageable pageable, String expand) {
        try {
            Page<Category> all = expand != null && expand.contains("children") ?
                    categoryRepository.findAllByCategoryNullAndActiveTrue(pageable) :
                    categoryRepository.findAllByActiveTrue(pageable);
            List<Map<String, Object>> collect = all.stream().map(category -> {
                Map<String, Object> categoryDto = DtoConverter.categoryDto(category, expand);
                if (expand != null && expand.contains("children")) {
                    List<Category> categoryIdAndActiveTrue = categoryRepository.findAllByCategoryIdAndActiveTrue(category.getId());
                    categoryDto.put("children", categoryIdAndActiveTrue.stream().map(category1 -> DtoConverter.categoryDto(category1, null)));
                }
                return categoryDto;
            }).collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("items", collect);
            response.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), pageable.getPageNumber() + 1, pageable.getPageSize()));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all categories", e.getMessage());
        }
    }

    public ResponseEntity<?> getAllByCategoryId(Integer id, Pageable pageable, String expand) {
        try {
            Page<Category> all = categoryRepository.findAllByCategoryIdAndActiveTrue(id, pageable);
            List<Map<String, Object>> collect = all.stream().map(category -> DtoConverter.categoryDto(category, expand)).collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("items", collect);
            response.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), pageable.getPageNumber() + 1, pageable.getPageSize()));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all categories", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<Category> byId = categoryRepository.findByIdAndActiveTrue(id);
            if (byId.isPresent()) {
                Category category = byId.get();
                category.setActive(false);
                categoryRepository.save(category);
                return AllApiResponse.response(1, "Category deleted successfully");
            } else {
                return AllApiResponse.response(404, 0, "Category not found with id!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete category", e.getMessage());
        }
    }

    public Long getCategoryCount() {
        return categoryRepository.count();
    }
}
