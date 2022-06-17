package uz.in_trade_map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.payload.ApiResponse;
import uz.in_trade_map.repository.DistrictRepository;
import uz.in_trade_map.repository.QuarterRepository;
import uz.in_trade_map.repository.RegionRepository;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressController {
    @Autowired
    RegionRepository regionRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    QuarterRepository quarterRepository;

    @GetMapping("/regions")
    public HttpEntity<?> regions() {
        return ResponseEntity.ok(new ApiResponse(1, "success", regionRepository.findAll()));
    }

    @GetMapping("/districts/{id}")
    public HttpEntity<?> districts(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse(1, "success", districtRepository.findAllByRegionId(id)));
    }

//    @GetMapping("/quarters/{id}")
//    public HttpEntity<?> quarters(@PathVariable Integer id){
//        return ResponseEntity.ok(new ApiResponse(1,"success",quarterRepository.findAllByDistrictId(id)));
//    }
}
