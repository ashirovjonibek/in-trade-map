package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.Banner;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.BannerRepository;
import uz.in_trade_map.repository.ProductRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.BannerRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BannerService extends Validator<BannerRequest> {
    private final BannerRepository bannerRepository;
    private final ProductRepository productRepository;
    private final AttachmentService attachmentService;

    public ResponseEntity<?> save(BannerRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (request.getPhoto() == null) {
                valid.put("photo", "Field is required!");
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            }
            Optional<Product> byIdAndActiveTrue = productRepository.findByIdAndActiveTrue(request.getProductId());
            if (byIdAndActiveTrue.isEmpty()) {
                return AllApiResponse.response(422, 0, "Product not fount with id!");
            }
            Banner banner = BannerRequest.convertToEntity(request, new Banner());
            banner.setProduct(byIdAndActiveTrue.get());
            banner.setPhoto(attachmentService.uploadFile(request.getPhoto()));
            Banner save = bannerRepository.save(banner);
            return AllApiResponse.response(1, "Banner created successfully!", DtoConverter.bannerDto(save,null));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error saving banner!", e.getMessage());
        }
    }

    public ResponseEntity<?> update(Integer id, BannerRequest request, UUID oldPhotoId) {
        try {
            Optional<Banner> optionalBanner = bannerRepository.findByIdAndActiveTrue(id);
            if (optionalBanner.isEmpty()) return AllApiResponse.response(422, 0, "Banner not fount with id!");
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            }
            Optional<Product> byIdAndActiveTrue = productRepository.findByIdAndActiveTrue(request.getProductId());
            if (byIdAndActiveTrue.isEmpty()) {
                return AllApiResponse.response(422, 0, "Product not fount with id!");
            }
            Banner banner = BannerRequest.convertToEntity(request, optionalBanner.get());
            if (oldPhotoId != null) {
                List<Attachment> attachments = attachmentService.getAttachments(Collections.singletonList(oldPhotoId));
                banner.setPhoto(attachments.size() > 0 ? attachments.get(0) : null);
            } else banner.setPhoto(null);
            if (request.getPhoto() != null) {
                banner.setPhoto(attachmentService.uploadFile(request.getPhoto()));
            }
            banner.setProduct(byIdAndActiveTrue.get());
            Banner save = bannerRepository.save(banner);
            return AllApiResponse.response(1, "Banner updated successfully!", DtoConverter.bannerDto(save,null));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error updating banner!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(int size, int page, String expand) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Banner> all = bannerRepository.findAllByActiveTrueOrderByCreatedAtDesc(pageable);
            Map<String, Object> resp = new HashMap<>();
            resp.put("items", all.stream().map(banner -> DtoConverter.bannerDto(banner, expand)));
            resp.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), page, size));
            return AllApiResponse.response(1, "Success", resp);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all banners!");
        }
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Banner> op = bannerRepository.findByIdAndActiveTrue(id);
            if (op.isEmpty()) {
                return AllApiResponse.response(404, 0, "Banner not fount with id!");
            }
            return AllApiResponse.response(1, "Success", DtoConverter.bannerDto(op.get(), expand));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one banner!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<Banner> op = bannerRepository.findByIdAndActiveTrue(id);
            if (op.isEmpty()) {
                return AllApiResponse.response(404, 0, "Banner not fount with id!");
            }
            Banner banner = op.get();
            banner.setActive(false);
            bannerRepository.save(banner);
            return AllApiResponse.response(1, "Banner deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one banner!", e.getMessage());
        }
    }
}
