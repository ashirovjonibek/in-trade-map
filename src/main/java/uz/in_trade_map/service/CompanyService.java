package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.*;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.CompanyRequest;

import java.util.*;
import java.util.stream.Collectors;

import static uz.in_trade_map.service.specifications.CompanySpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactDataRepository contactDataRepository;
    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;
    private final DistrictRepository districtRepository;

    public ResponseEntity<?> save(CompanyRequest request) {
        try {
            Optional<District> byId = districtRepository.findById(request.getDistrictId());
            if (byId.isPresent()) {
                Location location = locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()));
                ContactData data = new ContactData();
                data.setLocation(location);
//                data.setSocialMedia(request.getSocialMedia());
                ContactData contactData = contactDataRepository.save(data);
                Company company = CompanyRequest.convertCompany(request);
                company.setCertificates(attachmentService.uploadFile(Arrays.asList(request.getCertificates())));
                company.setImage(attachmentService.uploadFile(request.getImage()));
                company.setLogo(attachmentService.uploadFile(request.getLogo()));
                company.setData(contactData);
                Company save = companyRepository.save(company);
                return AllApiResponse.response(1, "Company saved successfully!", DtoConverter.companyDto(save, null));
            } else return AllApiResponse.response(404, 0, "District not fount!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for save company", e.getMessage());
        }
    }

    public ResponseEntity<?> edit(Integer id, CompanyRequest request, UUID[] oldPhotoIds) {
        try {
            Optional<Company> companyOptional = companyRepository.findByIdAndActiveTrue(id);
            if (companyOptional.isPresent()) {
                Optional<District> byId = districtRepository.findById(request.getDistrictId());
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
                } else return AllApiResponse.response(404, 0, "District not found!");
            } else return AllApiResponse.response(404, 0, "Company not found with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for update company", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(
            String search,
            Integer locationId,
            String inn,
            String brandName,
            Integer regionId,
            Integer districtId,
            Integer quarterId,
            String expand,
            String address,
            int size,
            int page
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Company> companies = companyRepository.findAll(
                    where(
                            findByNameUz(search))
                            .or(findByNameRu(search))
                            .or(findByNameEn(search))
                            .or(findByNameUzCry(search))
                            .and(findByAddress(address))
                            .and(findByBrandName(brandName))
                            .and(findByLocationId(locationId))
                            .and(findByInn(inn))
                            .and(findByRegionId(regionId))
                            .and(findByDistrictId(districtId))
                            .and(activeTrue()),
                    pageable

            );
            List<Map<String, Object>> collect = companies.stream().map(company -> DtoConverter.companyDto(company, expand)).collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("items", collect);
            response.put("meta", new Meta(companies.getTotalElements(), companies.getTotalPages(), page, size));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all company!", e.getMessage());
        }
    }

    public HttpEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Company> byIdAndActiveTrue = companyRepository.findByIdAndActiveTrue(id);
            if (byIdAndActiveTrue.isPresent()) {
                return AllApiResponse.response(1, "Success", DtoConverter.companyDto(byIdAndActiveTrue.get(), expand));
            } else return AllApiResponse.response(404, 0, "Company not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for get company!");
        }
    }

    public HttpEntity<?> delete(Integer id) {
        try {
            Optional<Company> company = companyRepository.findByIdAndActiveTrue(id);
            if (company.isPresent()) {
                Company company1 = company.get();
                company1.setActive(false);
                companyRepository.save(company1);
                return AllApiResponse.response(1, "Company deleted successfully");
            } else {
                return AllApiResponse.response(404, 0, "Company not fount with id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete company", e.getMessage());
        }
    }
}
