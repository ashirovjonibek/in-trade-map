package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.*;
import uz.in_trade_map.utils.AuthUser;
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
    private final UserRepository userRepository;

    public ResponseEntity<?> save(CompanyRequest request) {
        try {
            boolean existsByBrandNameAndActiveTrue = companyRepository.existsByBrandNameAndActiveTrue(request.getBrandName());
            boolean existsByInnAndActiveTrue = companyRepository.existsByInnAndActiveTrue(request.getInn());
            Map<String, String> valid = new HashMap<>();
            if (existsByBrandNameAndActiveTrue) {
                valid.put("brandName", "This brand name already exists");
            }
            if (existsByInnAndActiveTrue) {
                valid.put("inn", "This inn already exists");
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Valid error!", valid);
            }
            Optional<District> byId = districtRepository.findById(request.getDistrictId());
            if (byId.isPresent()) {
                Location location = locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()));
                ContactData data = new ContactData();
                data.setLocation(location);
                data.setSocialMedia(request.getSocialMedia());
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

    public ResponseEntity<?> edit(Integer id, CompanyRequest request, UUID[] oldPhotoIds, Integer oldImage, Integer oldLogo) {
        try {
            Optional<Company> companyOptional = companyRepository.findByIdAndActiveTrue(id);
            Optional<Company> existsByBrandNameAndActiveTrue = companyRepository.findByBrandNameAndActiveTrue(request.getBrandName());
            Optional<Company> existsByInnAndActiveTrue = companyRepository.findByInnAndActiveTrue(request.getInn());
            Map<String, String> valid = new HashMap<>();
            if (existsByBrandNameAndActiveTrue.isPresent() && !existsByBrandNameAndActiveTrue.get().getId().equals(id)) {
                valid.put("brandName", "This brand name already exists");
            }
            if (existsByInnAndActiveTrue.isPresent()&& !existsByInnAndActiveTrue.get().getId().equals(id)) {
                valid.put("inn", "This inn already exists");
            }
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Valid error!", valid);
            }
            if (companyOptional.isPresent()) {
                Optional<District> byId = districtRepository.findById(request.getDistrictId());
                if (byId.isPresent()) {
                    Company company1 = companyOptional.get();
                    if (company1.getData() != null) {
                        ContactData data = company1.getData();
                        data.setSocialMedia(request.getSocialMedia());
                        Location location;
                        if (data.getLocation() != null) {
                            location = data.getLocation();
                            location.setDistrict(byId.get());
                            location.setAddress(request.getAddress());
                            location.setLat(request.getLat());
                            location.setLng(request.getLng());
                        } else {
                            location = new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng());
                        }
                        data.setLocation(locationRepository.save(location));
                        company1.setData(contactDataRepository.save(data));
                    } else {
                        ContactData contactData = contactDataRepository.save(new ContactData(request.getSocialMedia(), locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()))));
                        company1.setData(contactData);
                    }
                    Company company = CompanyRequest.convertCompany(request, company1);
                    if (request.getLogo() != null) {
                        company.setLogo(attachmentService.uploadFile(request.getLogo()));
                    } else {
                        if (oldLogo != null && oldLogo == 1) {
                            company.setLogo(companyOptional.get().getLogo());
                        } else company.setLogo(null);
                    }
                    if (request.getImage() != null) {
                        company.setImage(attachmentService.uploadFile(request.getImage()));
                    } else {
                        if (oldImage != null && oldImage == 1) {
                            company.setImage(companyOptional.get().getImage());
                        } else company.setImage(null);
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
            String expand,
            String address,
            int size,
            int page
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            AuthUser authUser = new AuthUser();
            Page<Company> companies = companyRepository.findAll(
                    where(
                            findByBrandName(brandName))
                            .and(findByName(search))
                            .and(findIds(authUser.getUser() != null ? authUser.isAdmin() ? null : new HashSet<>(Collections.singleton(authUser.getUser().getCompany().getId())) : null))
                            .and(findByAddress(address))
                            .and(findByLocationId(locationId))
                            .and(findByInn(inn))
                            .and(findByRegionId(regionId))
                            .and(findByDistrictId(districtId))
                            .and(activeTrue()),
                    pageable

            );
            List<Map<String, Object>> collect = companies.stream().map(company -> {
                Map<String, Object> dto = DtoConverter.companyDto(company, expand);
                if (expand != null && expand.contains("employees")) {
                    List<User> users = userRepository.findAllByCompanyIdAndActiveTrue(company.getId());
                    dto.put("employees", users.stream().map(user1 -> DtoConverter.userDto(user1, expand)).collect(Collectors.toList()));
                }
                return dto;
            }).collect(Collectors.toList());
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

    public HttpEntity<?> setConfirmStatus(Integer id, Integer status) {
        try {
            Optional<Company> byIdAndActiveTrue = companyRepository.findByIdAndActiveTrue(id);
            if (byIdAndActiveTrue.isPresent()) {
                Company company = byIdAndActiveTrue.get();
                company.setProductAlwaysConfirm(status == 1);
                companyRepository.save(company);
                return AllApiResponse.response(1, "Company status changes successfully");
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
