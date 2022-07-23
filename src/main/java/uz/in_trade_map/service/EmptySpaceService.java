package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.District;
import uz.in_trade_map.entity.EmptySpace;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.DistrictRepository;
import uz.in_trade_map.repository.EmptySpaceRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.EmptySpaceRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static uz.in_trade_map.service.specifications.EmptySpaceSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class EmptySpaceService extends Validator<EmptySpaceRequest> {
    private final EmptySpaceRepository emptySpaceRepository;
    private final AttachmentService attachmentService;
    private final DistrictRepository districtRepository;

    public ResponseEntity<?> create(EmptySpaceRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() == 0) {
                EmptySpace emptySpace = EmptySpaceRequest.convertToEmptySpace(request);
                Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
                if (optionalDistrict.isPresent()) {
                    emptySpace.setDistrict(optionalDistrict.get());
                } else return AllApiResponse.response(404, 0, "District not fount with id!");
                if (request.getPhotos() != null) {
                    emptySpace.setPhotos(attachmentService.uploadFile(request.getPhotos()));
                }
                EmptySpace save = emptySpaceRepository.save(emptySpace);
                return AllApiResponse.response(1, "Empty space created successfully", DtoConverter.emptySpacesDto(save, null));
            } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create empty space!", e.getMessage());
        }
    }

    public ResponseEntity<?> update(Integer id, EmptySpaceRequest request) {
        try {
            Optional<EmptySpace> optionalEmptySpace = emptySpaceRepository.findByIdAndActiveTrue(id);
            if (optionalEmptySpace.isPresent()) {
                Map<String, Object> valid = valid(request);
                if (valid.size() == 0) {
                    EmptySpace emptySpace = EmptySpaceRequest.convertToEmptySpace(request, optionalEmptySpace.get());
                    Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
                    if (optionalDistrict.isPresent()) {
                        emptySpace.setDistrict(optionalDistrict.get());
                    } else return AllApiResponse.response(404, 0, "District not fount with id!");
                    if (request.getPhotos() != null) {
                        List<Attachment> attachments = attachmentService.uploadFile(request.getPhotos());
                        if (request.getOldPhotoIds() != null && request.getOldPhotoIds().size() > 0) {
                            List<Attachment> attachmentList = attachmentService.getAttachments(request.getOldPhotoIds());
                            if (attachmentList != null && attachmentList.size() > 0)
                                attachments.addAll(attachmentList);
                        }
                        emptySpace.setPhotos(attachments);
                    } else {
                        if (request.getOldPhotoIds() != null && request.getOldPhotoIds().size() > 0) {
                            List<Attachment> attachmentList = attachmentService.getAttachments(request.getOldPhotoIds());
                            if (attachmentList != null && attachmentList.size() > 0)
                                emptySpace.setPhotos(attachmentList);
                            else emptySpace.setPhotos(null);
                        }
                    }

                    emptySpace.setId(id);
                    EmptySpace save = emptySpaceRepository.save(emptySpace);
                    return AllApiResponse.response(1, "Empty space updated successfully", DtoConverter.emptySpacesDto(save, null));
                } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else return AllApiResponse.response(404, 0, "Empty space not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error update empty space!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(
            int page, int size, String expand, Integer regionId,
            Integer districtId, String address, String search,
            Float minStartingPrice, Float maxStartingPrice,
            Float minAnnualPrice, Float maxAnnualPrice,
            Integer isStateProperty, Integer isBuild
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<EmptySpace> all = emptySpaceRepository.findAll(where(findByRegionId(regionId))
                            .and(findByDistrictId(districtId))
                            .and(findByAddress(address))
                            .and(findByDescriptionUz(search))
                            .or(findByDescriptionRu(search))
                            .or(findByDescriptionEn(search))
                            .or(findByDescriptionUzCry(search))
                            .and(findAllByGreaterStartingPrice(minStartingPrice))
                            .and(findAllByLessStartingPrice(maxStartingPrice))
                            .and(findAllByGreaterAnnualPrice(minAnnualPrice))
                            .and(findAllByLessAnnualPrice(maxAnnualPrice))
                            .and(findByIsBuildField(isBuild))
                            .and(findByIsStatePropertyField(isStateProperty))
                            .and(activeTrue()),
                    pageable
            );
            Stream<Map<String, Object>> items = all.stream().map(emptySpace -> DtoConverter.emptySpacesDto(emptySpace, expand));
            Meta meta = new Meta(all.getTotalElements(), all.getTotalPages(), page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("items", items);
            response.put("meta", meta);
            response.put("maxMinPrices", emptySpaceRepository.maxMinPrices());
            return AllApiResponse.response(1, "All empty spaces!", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all empty spaces!", e.getMessage());
        }
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<EmptySpace> optionalEmptySpace = emptySpaceRepository.findByIdAndActiveTrue(id);
            if (optionalEmptySpace.isPresent()) {
                return AllApiResponse.response(1, "Empty space!", DtoConverter.emptySpacesDto(optionalEmptySpace.get(), expand));
            } else return AllApiResponse.response(404, 0, "Empty space not fount!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one empty space!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<EmptySpace> optionalEmptySpace = emptySpaceRepository.findByIdAndActiveTrue(id);
            if (optionalEmptySpace.isPresent()) {
                EmptySpace emptySpace = optionalEmptySpace.get();
                emptySpace.setActive(false);
                emptySpaceRepository.save(emptySpace);
                return AllApiResponse.response(1, "Empty space deleted successfully!");
            } else return AllApiResponse.response(404, 0, "Empty space not fount!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete empty space!", e.getMessage());
        }
    }

}
