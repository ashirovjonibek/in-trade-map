package uz.in_trade_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CategoryRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.CategoryRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                return AllApiResponse.response(1, "Category successfully saved!", save);
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
                if (request.getCategoryId() != null) {
                    Optional<Category> byId = categoryRepository.findById(request.getCategoryId());
                    if (byId.isPresent()) {
                        category.setCategory(byId.get());
                    } else return AllApiResponse.response(404, 0, "Category not found with id!");
                }
                try {
                    Category save = categoryRepository.save(category);
                    return AllApiResponse.response(1, "Category successfully saved!", save);
                } catch (Exception e) {
                    e.printStackTrace();
                    return AllApiResponse.response(500, 0, "Error saved category", e.getMessage());
                }
            } else return AllApiResponse.response(422, 0, "Validator errors", valid);
        } else return AllApiResponse.response(404, 0, "Category not found for edit!");
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Category> byId = categoryRepository.findById(id);
            if (byId.isPresent()) {
                Category category = byId.get();
                Map<String, Object> stringObjectMap = DtoConverter.categoryDto(category, expand);
                if (expand.contains("children")) {
                    List<Category> all = categoryRepository.findAllByCategoryId(category.getId());
                    stringObjectMap.put("children", all.stream().map(all1 -> DtoConverter.categoryDto(all1, expand)));
                }
                return AllApiResponse.response(1, "Success", stringObjectMap);
            } else {
                return AllApiResponse.response(404, 0, "Category not found with id!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one category", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(String expand) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all category", e.getMessage());
        }
    }
}
