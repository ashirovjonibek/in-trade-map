package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.Company;
import uz.in_trade_map.entity.ContactData;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.entity.Quarter;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.CompanyRepository;
import uz.in_trade_map.repository.ContactDataRepository;
import uz.in_trade_map.repository.LocationRepository;
import uz.in_trade_map.repository.QuarterRepository;
import uz.in_trade_map.utils.request_objects.CompanyRequest;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactDataRepository contactDataRepository;
    private final LocationRepository locationRepository;
    private final AttachmentService attachmentService;
    private final QuarterRepository quarterRepository;

    public ResponseEntity<?> save(CompanyRequest request){
        try{
            Optional<Quarter> byId = quarterRepository.findById(request.getQuarterId());
            if (byId.isPresent()){
                Location location = locationRepository.save(new Location(byId.get(), request.getAddress(), request.getLat(), request.getLng()));
                ContactData contactData = contactDataRepository.save(new ContactData(request.getSocialMedia(), location));
                Company company = CompanyRequest.convertCompany(request);
                company.setCertificates(attachmentService.uploadFile(Arrays.asList(request.getCertificates())));
                company.setImage(attachmentService.uploadFile(request.getImage()));
                company.setLogo(attachmentService.uploadFile(request.getLogo()));
                company.setData(contactData);
                Company save = companyRepository.save(company);
                return AllApiResponse.response(1,"Company saved successfully!",save);
            }else return AllApiResponse.response(404,0,"Quarter not fount!");
        }catch (Exception e){
            e.printStackTrace();
            return AllApiResponse.response(500,0,"Error for save company",e.getMessage());
        }
    }
}
