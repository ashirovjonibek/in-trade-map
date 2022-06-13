package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.ContactDataRepository;
import uz.in_trade_map.repository.LocationRepository;
import uz.in_trade_map.repository.QuarterRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.CompanyRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactDataRepository contactDataRepository;
    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;
    private final QuarterRepository quarterRepository;

    public ResponseEntity<?> save(CompanyRequest request) {
        try {
            Optional<Quarter> byId = quarterRepository.findById(request.getQuarterId());
            if (byId.isPresent()) {
                Location location = locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()));
                ContactData contactData = contactDataRepository.save(new ContactData(request.getSocialMedia(), location));
                Company company = CompanyRequest.convertCompany(request);
                company.setCertificates(attachmentService.uploadFile(Arrays.asList(request.getCertificates())));
                company.setImage(attachmentService.uploadFile(request.getImage()));
                company.setLogo(attachmentService.uploadFile(request.getLogo()));
                company.setData(contactData);
                Company save = companyRepository.save(company);
                return AllApiResponse.response(1, "Company saved successfully!", DtoConverter.companyDto(save, null));
            } else return AllApiResponse.response(404, 0, "Quarter not fount!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for save company", e.getMessage());
        }
    }

    public ResponseEntity<?> edit(Integer id, CompanyRequest request, UUID[] oldPhotoIds) {
        try {
            Optional<Company> companyOptional = companyRepository.findByIdAndActiveTrue(id);
            if (companyOptional.isPresent()) {
                Optional<Quarter> byId = quarterRepository.findById(request.getQuarterId());
                if (byId.isPresent()) {
                    Location location = locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()));
                    ContactData contactData = contactDataRepository.save(new ContactData(request.getSocialMedia(), location));
                    Company company = CompanyRequest.convertCompany(request);
                    if (request.getLogo() != null) {
                        company.setLogo(attachmentService.uploadFile(request.getLogo()));
                    } else {
                        company.setImage(companyOptional.get().getImage());
                    }
                    if (request.getImage() != null) {
                        company.setLogo(attachmentService.uploadFile(request.getImage()));
                    } else {
                        company.setLogo(companyOptional.get().getLogo());
                    }
                    if (request.getCertificates() != null && request.getCertificates().length > 0) {
                        List<Attachment> attachments = attachmentService.uploadFile(Arrays.asList(request.getCertificates()));
                        if (oldPhotoIds.length > 0) {
                            attachments.addAll(
                                    companyOptional.get().getCertificates().stream().filter(certificate -> {
                                        List<UUID> collect = Arrays.stream(oldPhotoIds).filter(oldPhotoId -> oldPhotoId.equals(certificate.getId())).collect(Collectors.toList());
                                        return collect.size() > 0;
                                    }).collect(Collectors.toList())
                            );
                        }
                        company.setCertificates(attachments);
                    } else {
                        if (oldPhotoIds.length > 0) {
                            List<Attachment> attachments = companyOptional.get().getCertificates().stream().filter(certificate -> {
                                List<UUID> collect = Arrays.stream(oldPhotoIds).filter(oldPhotoId -> oldPhotoId.equals(certificate.getId())).collect(Collectors.toList());
                                return collect.size() > 0;
                            }).collect(Collectors.toList());
                            company.setCertificates(attachments);
                        } else {
                            company.setCertificates(companyOptional.get().getCertificates());
                        }
                    }
                    company.setId(id);
                    company.setData(contactData);
                    Company save = companyRepository.save(company);
                    return AllApiResponse.response(1, "Company updated successfully!", DtoConverter.companyDto(save, null));
                } else return AllApiResponse.response(404, 0, "Quarter not found!");
            } else return AllApiResponse.response(404, 0, "Company not found with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for update company", e.getMessage());
        }
    }
}
