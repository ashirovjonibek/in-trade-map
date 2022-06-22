package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.District;
import uz.in_trade_map.entity.EmptySpace;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.DistrictRepository;
import uz.in_trade_map.repository.EmptySpaceRepository;
import uz.in_trade_map.utils.request_objects.EmptySpaceRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
                return AllApiResponse.response(1, "Empty space created successfully", save);
            } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create empty space!", e.getMessage());
        }
    }

    public ResponseEntity<?> update(Integer id, EmptySpaceRequest request, List<UUID> oldPhotoIds) {
        try {
            Optional<EmptySpace> optionalEmptySpace = emptySpaceRepository.findByIdAndActiveTrue(id);
            if (optionalEmptySpace.isPresent()) {
                Map<String, Object> valid = valid(request);
                if (valid.size() == 0) {
                    EmptySpace emptySpace = EmptySpaceRequest.convertToEmptySpace(request);
                    Optional<District> optionalDistrict = districtRepository.findById(request.getDistrictId());
                    if (optionalDistrict.isPresent()) {
                        emptySpace.setDistrict(optionalDistrict.get());
                    } else return AllApiResponse.response(404, 0, "District not fount with id!");
                    if (request.getPhotos() != null) {
                        List<Attachment> attachments = attachmentService.uploadFile(request.getPhotos());
                        if (oldPhotoIds != null && oldPhotoIds.size() > 0) {
                            List<Attachment> attachmentList = attachmentService.getAttachments(oldPhotoIds);
                            if (attachmentList != null && attachmentList.size() > 0)
                                attachments.addAll(attachmentList);
                        }
                        emptySpace.setPhotos(attachments);
                    } else {
                        if (oldPhotoIds != null && oldPhotoIds.size() > 0) {
                            List<Attachment> attachmentList = attachmentService.getAttachments(oldPhotoIds);
                            if (attachmentList != null && attachmentList.size() > 0)
                                emptySpace.setPhotos(attachmentList);
                            else emptySpace.setPhotos(null);
                        }
                    }

                    emptySpace.setId(id);
                    EmptySpace save = emptySpaceRepository.save(emptySpace);
                    return AllApiResponse.response(1, "Empty space created successfully", save);
                } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
            } else return AllApiResponse.response(404, 0, "Empty not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create empty space!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll() {
        return null;
    }
}
