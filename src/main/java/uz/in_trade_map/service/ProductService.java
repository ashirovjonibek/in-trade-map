package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.Category;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CategoryRepository;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.ProductRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.ProductRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;

import static uz.in_trade_map.service.specifications.ProductSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class ProductService extends Validator<ProductRequest> {
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentService attachmentService;

    public ResponseEntity<?> create(ProductRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else {
                Product product = ProductRequest.convertToProduct(request);
                product.setPhotos(attachmentService.uploadFile(request.getPhotos()));
                Optional<Company> optionalCompany = companyRepository.findByIdAndActiveTrue(request.getCompanyId());
                if (optionalCompany.isPresent()) {
                    product.setCompany(optionalCompany.get());
                } else return AllApiResponse.response(404, 0, "Company not fount with id!");
                Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(request.getCategoryId());
                if (optionalCategory.isPresent()) {
                    product.setCategory(optionalCategory.get());
                } else return AllApiResponse.response(404, 0, "Category not fount with id!");
                Product save = productRepository.save(product);
                return AllApiResponse.response(1, "Product created successfully!", DtoConverter.productDto(save, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create product!", e.getMessage());
        }
    }

    public ResponseEntity<?> update(UUID id, ProductRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else {
                Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
                if (optionalProduct.isPresent()) {
                    Product product = ProductRequest.convertToProduct(request);
                    List<Attachment> attachments = attachmentService.uploadFile(request.getPhotos());
                    if (request.getOldPhotoIds() != null && request.getOldPhotoIds().size() > 0) {
                        if (attachments != null) {
                            attachments.addAll(attachmentService.getAttachments(request.getOldPhotoIds()));
                        } else {
                            attachments = new ArrayList<>(attachmentService.getAttachments(request.getOldPhotoIds()));
                        }
                    }
                    product.setPhotos(attachments);
                    Optional<Company> optionalCompany = companyRepository.findByIdAndActiveTrue(request.getCompanyId());
                    if (optionalCompany.isPresent()) {
                        product.setCompany(optionalCompany.get());
                    } else return AllApiResponse.response(404, 0, "Company not fount with id!");
                    Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(request.getCategoryId());
                    if (optionalCategory.isPresent()) {
                        product.setCategory(optionalCategory.get());
                    } else return AllApiResponse.response(404, 0, "Category not fount with id!");
                    product.setId(id);
                    Product save = productRepository.save(product);
                    return AllApiResponse.response(1, "Product updated successfully!", DtoConverter.productDto(save, null));
                } else return AllApiResponse.response(404, 0, "Product not found with id!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error update product!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(
            String search,
            Integer districtId,
            Integer regionId,
            Integer categoryId,
            Integer companyId,
            String brandName,
            int size,
            int page,
            String expand
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Product> products = productRepository.findAll(where(findByCompanyId(companyId))
                            .and(findByCategoryId(categoryId))
                            .and(findByDistrictId(districtId))
                            .and(findByRegionId(regionId))
                            .and(findByBrandName(brandName))
                            .and(findByNameUz(search))
                            .or(findByNameEn(search))
                            .or(findByNameRu(search))
                            .or(findByNameUzCry(search))
                            .and(activeTrue()),
                    pageable
            );
            Map<String, Object> response = new HashMap<>();
            response.put("meta", new Meta(products.getTotalElements(), products.getTotalPages(), page, size));
            response.put("items", products.stream().map(product -> DtoConverter.productDto(product, expand)));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all products!", e.getMessage());
        }

    }

    public ResponseEntity<?> getOneProduct(UUID id, String expand) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
            if (optionalProduct.isPresent()) {
                return AllApiResponse.response(1, "Success", DtoConverter.productDto(optionalProduct.get(), expand));
            } else return AllApiResponse.response(404, "Product not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one product!", e.getMessage());
        }
    }

}
