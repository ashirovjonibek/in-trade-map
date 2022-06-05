package uz.in_trade_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.springframework.data.jpa.domain.Specification.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.entity.Quarter;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.payload.ApiResponse;
import uz.in_trade_map.utils.request_objects.LocationRequest;
import uz.in_trade_map.repository.LocationRepository;
import uz.in_trade_map.repository.QuarterRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.validator.Validator;

import static uz.in_trade_map.service.specifications.LocationSpecifications.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService extends Validator<LocationRequest> {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    QuarterRepository quarterRepository;

    public ResponseEntity<?> saveOrEdit(LocationRequest request) {
        Map<String, Object> valid = valid(request);
        if (valid.size() == 0) {

            Optional<Quarter> quarterOptional = quarterRepository.findById(request.getQuarterId());
            if (quarterOptional.isPresent()) {
                try {
                    Location location = null;
                    if (request.getId() != null) {
                        Optional<Location> byId = locationRepository.findById(request.getId());
                        if (byId.isPresent()) {
                            Location location1 = byId.get();
                            if (request.getLat() != null) {
                                location1.setLat(request.getLat());
                            }
                            if (request.getLng() != null) {
                                location1.setLng(request.getLng());
                            }
                            location1.setQuarter(quarterOptional.get());
                            location1.setAddress(request.getAddress());
                            location = location1;
                        } else {
                            return ResponseEntity.status(404).body(new ApiResponse(0, "Location not found with id"));
                        }
                    } else {
                        location = LocationRequest.request(request);
                        location.setQuarter(quarterOptional.get());
                    }
                    Location save = locationRepository.save(location);
                    return ResponseEntity.ok(new ApiResponse(1, "Saved successfully", save));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error save location", e.getMessage()));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Quarter not found with id"));
            }
        }else return AllApiResponse.response(422,0,"Validator errors",valid);

    }

    public ResponseEntity<?> findById(Integer id, String expand) {
        try {
            Optional<Location> byId = locationRepository.findById(id);
            if (byId.isPresent()) {
                return AllApiResponse.response(1, "Success", DtoConverter.locationDto(byId.get(),expand));
            } else return AllApiResponse.response(404, 0, "Location not found with id");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error find location", e.getMessage());
        }
    }

    public ResponseEntity<?> deleteLocation(Integer id) {
        try {
            Optional<Location> byId = locationRepository.findById(id);
            if (byId.isPresent()) {
                Location location = byId.get();
                locationRepository.delete(location);
                return AllApiResponse.response(0, "Location deleted successfully");
            } else return AllApiResponse.response(404, 0, "Location not found with id");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error delete location", e.getMessage());
        }
    }

    public ResponseEntity<?> findAllBySpec(Integer regionId, Integer districtId, Integer quarterId, String address, int size, int page, String expand) {
        Pageable pageable = PageRequest.of(page, size == 0 ? (int) locationRepository.count() : size);
        Page<Location> locations = locationRepository.findAll(
                where(findByRegionId(regionId))
                        .and(findByDistrictId(districtId))
                        .and(findByQuarterId(quarterId))
                        .and(findByAddress(address)),
                pageable
        );
        Map<String, Object> resp = new HashMap<>();
        resp.put("items", locations.stream().map(location -> DtoConverter.locationDto(location,expand)).collect(Collectors.toList()));
        resp.put("meta", new Meta(locations.getTotalElements(), locations.getTotalPages(), page + 1, size));

        return AllApiResponse.response(1, "success", resp);
    }
}
