package uz.in_trade_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.entity.Location;
import uz.in_trade_map.entity.Quarter;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.payload.ApiResponse;
import uz.in_trade_map.payload.LocationRequest;
import uz.in_trade_map.repository.LocationRepository;
import uz.in_trade_map.repository.QuarterRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    QuarterRepository quarterRepository;

    public ResponseEntity<?> saveOrEdit(LocationRequest request) {
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

    }

    public ResponseEntity<?> findAll(Pageable pageable, String expand) {
        try {
            Page<Location> locationPage = locationRepository.findAll(pageable);
            List<Map<String, Object>> locations = locationPage.stream().map(location -> {
                Map<String, Object> loc = new HashMap<>();
                loc.put("id", location.getId());
                loc.put("address", location.getAddress());
                loc.put("lat", location.getLat());
                loc.put("lng", location.getLng());
                if (expand != null && expand.contains("quarter")) {
                    loc.put("quarter", location.getQuarter());
                }
                return loc;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse(1, "success", locations));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "error"));
        }
    }

    public ResponseEntity<?> findById(Integer id, String expand) {
        try {
            Optional<Location> byId = locationRepository.findById(id);
            if (byId.isPresent()) {
                Map<String, Object> location = new HashMap<>();
                location.put("id", byId.get().getId());
                location.put("address", byId.get().getAddress());
                location.put("lat", byId.get().getLat());
                location.put("lng", byId.get().getLng());
                if (expand != null && expand.contains("quarter")) {
                    location.put("quarter", byId.get().getQuarter());
                }
                return AllApiResponse.response(1, "Success", location);
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
}
