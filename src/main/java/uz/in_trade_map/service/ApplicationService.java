package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.*;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.*;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.ApplicationRejectedRequest;
import uz.in_trade_map.utils.request_objects.ApplicationRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;

import static uz.in_trade_map.service.specifications.ApplicationSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class ApplicationService extends Validator<ApplicationRequest> {
    private final SmsService smsService;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordActions passwordActions;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserPasswordsRepository userPasswordsRepository;

    public ResponseEntity<?> sendSms(Integer id, String phoneNumber) {
        CheckPhone checkPhone = smsService.sendSms(id, phoneNumber);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", checkPhone.getId());
        resp.put("sent", checkPhone.getCreatedAt());
        resp.put("expire", checkPhone.getExpireDate());
        return AllApiResponse.response(1, "Sms sent!", resp);
    }

    public ResponseEntity<?> checkPhone(Integer id, String code, String phoneNumber) {
        Integer idRes = smsService.checkPhone(id, code, phoneNumber);
        if (idRes != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("id", idRes);
            return AllApiResponse.response(1, "Phone number is checked!", resp);
        } else return AllApiResponse.response(500, 0, "Error check phone number!");
    }

    public ResponseEntity<?> create(ApplicationRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(request.getBossPhone());
            boolean existsByEmail = userRepository.existsByEmail(request.getBossEmail());
            boolean existsByBrandNameAndActiveTrue = companyRepository.existsByBrandNameAndActiveTrue(request.getBrandName());
            boolean existsByInnAndActiveTrue = companyRepository.existsByInnAndActiveTrue(request.getInn());
            if (existsByPhoneNumber) valid.put("bossPhone", "Boss phone is exists!");
            if (existsByEmail) valid.put("bossEmail", "Boss email is exists!");
            if (existsByBrandNameAndActiveTrue) valid.put("brandName", "Brand name is exists!");
            if (existsByInnAndActiveTrue) valid.put("inn", "Inn is exists!");
            if (valid.size() == 0) {
                CheckPhone checked = smsService.getChecked(request.getCheckPhoneId());
                if (checked != null && checked.isCheck() && checked.getPhoneNumber().replace(" ", "").equals(request.getBossPhone())) {
                    Application application = ApplicationRequest.convertToApplication(request);
                    application.setActive(true);
                    application.setIsConfirm(0);
                    applicationRepository.save(application);
                    return AllApiResponse.response(1, "Application accepted successfully!");
                } else return AllApiResponse.response(422, 0, "Phone number is not checked!");
            } else return AllApiResponse.response(422, 0, "Validator errors!", valid);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error create application!", e.getMessage());
        }
    }

    public ResponseEntity<?> confirmApplication(Integer id) {
        try {
            Optional<Application> appOptional = applicationRepository.findByIdAndActiveTrue(id);
            if (appOptional.isPresent()) {
                Application application = appOptional.get();
                if (application.getIsConfirm() == 0) {
                    application.setIsConfirm(1);
                    Map<String, Object> userAndCompany = createUserAndCompany(application);
                    applicationRepository.save(application);
                    return AllApiResponse.response(1, "Application successfully confirm!", userAndCompany);
                } else return AllApiResponse.response(422, 0, "The application has already been approved!");
            } else return AllApiResponse.response(404, 0, "Application not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error confirm application!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(
            String brandName,
            String inn,
            String firstName,
            String lastName,
            String middleName,
            String bossPhone,
            String bossEmail,
            Integer isConfirm,
            int page,
            int size,
            String expand
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Application> applications = applicationRepository.findAll(
                    where(findByInn(inn)
                            .and(findByBossEmail(bossEmail))
                            .and(findByBossPhone(bossPhone))
                            .and(findByBrandName(brandName))
                            .and(findByFirsName(firstName))
                            .and(findByLastName(lastName))
                            .and(findByMiddleName(middleName))
                            .and(findByIsConfirm(isConfirm))
                            .and(activeTrue())
                    ), pageable);
            Map<String, Object> response = new HashMap<>();
            Meta meta = new Meta(applications.getTotalElements(), applications.getTotalPages(), page, size);
            response.put("meta", meta);
            response.put("items", applications.stream().map(application -> DtoConverter.applicationDto(application, expand)));
            return AllApiResponse.response(1, "App applications!", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all application!", e.getMessage());
        }
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Application> optionalApplication = applicationRepository.findByIdAndActiveTrue(id);
            if (optionalApplication.isPresent()) {
                return AllApiResponse.response(0, "Application with id!", DtoConverter.applicationDto(optionalApplication.get(), expand));
            } else
                return AllApiResponse.response(404, 0, "Application not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one application", e.getMessage());
        }
    }

    public ResponseEntity<?> deleteApplication(ApplicationRejectedRequest rejectedRequest) {
        try {
            Validator<ApplicationRejectedRequest> validator = new Validator<>();
            Map<String, Object> valid = validator.valid(rejectedRequest);
            if (valid.size() > 0)
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            Optional<Application> optionalApplication = applicationRepository.findByIdAndActiveTrueAndIsConfirm(rejectedRequest.getApplicationId(), 0);
            if (optionalApplication.isPresent()) {
                Application application = optionalApplication.get();
                application.setActive(false);
                applicationRepository.save(application);
                String message = "Hurmatli " + application.getFirstName() + " " + application.getLastName() + " " + application.getMiddleName() + " sizning http://intrademap.uz saytiga bergan arizangiz rad etildi.\nIzoh: " + rejectedRequest.getRejectMessage();
                smsService.sendSms(application.getBossPhone(), message);
                return AllApiResponse.response(1, "Application rejected!");
            } else return AllApiResponse.response(404, 0, "Application not found or status confirmed!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error reject application", e.getMessage());
        }
    }

    private Map<String, Object> createUserAndCompany(Application application) {
        try {
            Company company = new Company(application, true);
            Company savedCompany = companyRepository.save(company);
            Set<Role> roles = new HashSet<>();
            Optional<Role> optionalRole = roleRepository.findByRoleNameAndActiveTrue(RoleName.ROLE_COMPANY_DIRECTOR.name());
            optionalRole.ifPresent(roles::add);
            String password = passwordActions.generatePassword(8);
            User user = new User(
                    true,
                    "intrademap_user_" + application.getBossPhone().substring(1),
                    passwordEncoder.encode(password),
                    savedCompany,
                    roles,
                    application);
            User savedUser = userRepository.save(user);
            UserPasswords userPasswords = new UserPasswords(passwordActions.encodePassword(password), savedUser);
            userPasswordsRepository.save(userPasswords);
            Map<String, Object> resp = new HashMap<>();
            resp.put("user", DtoConverter.userDto(savedUser, null));
            resp.put("company", DtoConverter.companyDto(savedCompany, null));
            String text = "Hutmatli " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getMiddleName() + " sizning https://intrademap.uz saytida qoldirgan arizangiz ko'rib chiqib tasdiqlandi! Tizimga kirish uchun loginingiz: " +
                    "intrademap_user_" + application.getBossPhone().substring(1)
                    + "\n Parolingiz: " + password + "\n" +
                    "Havola: https://intrademap.uz/login";
            smsService.sendSms(savedUser.getPhoneNumber(), text);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error create user and company!");
        }
    }
}
