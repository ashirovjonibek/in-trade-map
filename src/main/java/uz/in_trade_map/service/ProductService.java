package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CategoryRepository;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.ProductRepository;
import uz.in_trade_map.repository.RejectedMessageRepository;
import uz.in_trade_map.utils.AuthUser;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.ProductRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

import static uz.in_trade_map.service.specifications.ProductSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class ProductService extends Validator<ProductRequest> {
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentService attachmentService;
    private final RejectedMessageRepository rejectedMessageRepository;

    public User user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))) {
            return ((User) authentication.getPrincipal());
        }
        return null;
    }

    public ResponseEntity<?> create(ProductRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else {
                Product product = ProductRequest.convertToProduct(request);
                product.setConfirmStatus(user().getCompany() == null ? 1 : user().getCompany() != null && user().getCompany().isProductAlwaysConfirm() ? 1 : 0);
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
                    Product product = ProductRequest.convertToProduct(request, optionalProduct.get());
                    product.setConfirmStatus(user().getCompany() == null ? 1 : user().getCompany() != null && user().getCompany().isProductAlwaysConfirm() ? 1 : 0);
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
            Float maxPriceUZS,
            Float minPriceUZS,
            Float minPriceUSD,
            Float maxPriceUSD,
            Float maxExportPriceUZS,
            Float minExportPriceUZS,
            Float minExportPriceUSD,
            Float maxExportPriceUSD,
            Integer confirmStatus,
            int size,
            int page,
            String expand
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            User user = user();
            if (user == null) {
                confirmStatus = 1;
            }
            List<Integer> allChildIds = null;
            if (categoryId != null) {
                List<Category> categoryList = categoryRepository.findAllByCategoryIdAndActiveTrue(categoryId);
                allChildIds = categoryList.size() > 0 ? categoryList.stream().map(Category::getId).collect(Collectors.toList()) : new ArrayList<>();
                allChildIds.add(categoryId);
            }
            Page<Product> products = productRepository.findAll(where(
                    findByCompanyId(user() != null && user().getCompany() != null ? user().getCompany().getId() : companyId))
                            .and(findByCategoryId(allChildIds))
                            .and(findByDistrictId(districtId))
                            .and(findByRegionId(regionId))
                            .and(findByBrandName(brandName))
                            .and(findByNameUz(search))
                            .or(findByNameEn(search))
                            .or(findByNameRu(search))
                            .or(findByNameUzCry(search))
                            .and(findByConfirmStatus(confirmStatus))
                            .and(findAllByGreaterPriceUZS(minPriceUZS))
                            .and(findAllByLessPriceUZS(maxPriceUZS))
                            .and(findAllByGreaterPriceUSD(minPriceUSD))
                            .and(findAllByLessPriceUSD(maxPriceUSD))
                            .and(findAllByGreaterExportPriceUZS(minExportPriceUZS))
                            .and(findAllByLessExportPriceUZS(maxExportPriceUZS))
                            .and(findAllByGreaterExportPriceUSD(minExportPriceUSD))
                            .and(findAllByLessExportPriceUSD(maxExportPriceUSD))
                            .and(companyActiveTrue())
                            .and(activeTrue()),
                    pageable
            );
            Map<String, Object> response = new HashMap<>();
            response.put("meta", new Meta(products.getTotalElements(), products.getTotalPages(), page, size));
            response.put("items", products.stream().map(product -> {
                Map<String, Object> map = DtoConverter.productDto(product, expand);
                if (product.getConfirmStatus() == 2 && user() != null) {
                    List<RejectedMessage> rejectedMessageList = rejectedMessageRepository.findAllByActiveTrueAndTableNameAndTableRowId("product", product.getId().toString());
                    map.put("rejectMessage", rejectedMessageList.stream().map(RejectedMessage::getMessage));
                }
                return map;
            }));
            response.put("maxMinPrices", productRepository.maxMinPrices());
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all products!", e.getMessage());
        }

    }

    public ResponseEntity<?> getOneProduct(UUID id, String expand) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
            if (optionalProduct.isPresent() && (optionalProduct.get().getConfirmStatus() == 1 || user() != null)) {
                Product product = optionalProduct.get();
                product.setViews(product.getViews() + 1);
                Product save = productRepository.save(product);
                Map<String, Object> map = DtoConverter.productDto(save, expand);
                if (save.getConfirmStatus() == 2 && user() != null) {
                    List<RejectedMessage> rejectedMessageList = rejectedMessageRepository.findAllByActiveTrueAndTableNameAndTableRowId("product", save.getId().toString());
                    map.put("rejectMessage", rejectedMessageList.stream().map(RejectedMessage::getMessage));
                }
                return AllApiResponse.response(1, "Success", map);
            } else return AllApiResponse.response(404, "Product not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one product!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(UUID id) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setActive(false);
                productRepository.save(product);
                return AllApiResponse.response(1, "Product deleted successfully!");
            } else return AllApiResponse.response(404, 1, "Product not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete product!", e.getMessage());
        }
    }

    public ResponseEntity<?> updateConfirm(UUID id, Integer confirmStatus, String message) {
        try {
            Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                if (confirmStatus == 1 || confirmStatus == 0) {
                    product.setConfirmStatus(confirmStatus);
                    Optional<RejectedMessage> rejectedMessage = rejectedMessageRepository.findByActiveTrueAndTableNameAndTableRowId("product", id.toString());
                    if (rejectedMessage.isPresent()) {
                        RejectedMessage message1 = rejectedMessage.get();
                        message1.setActive(false);
                        rejectedMessageRepository.save(message1);
                    }
                } else if (confirmStatus == 2) {
                    if (message != null && !message.equals("") && message.length() > 15) {
                        product.setConfirmStatus(confirmStatus);
                        rejectedMessageRepository.save(new RejectedMessage(message, "product", id.toString()));
                    } else {
                        return AllApiResponse.response(400, 0, "It is mandatory to send a message text for this status!");
                    }
                } else return AllApiResponse.response(400, 0, "You are sending the wrong status!");
                productRepository.save(product);
                return AllApiResponse.response(1, "Product status updated successfully!");
            } else return AllApiResponse.response(404, 1, "Product not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete product!", e.getMessage());
        }
    }


}
