package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.Banner;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.entity.Statistic;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.BannerRepository;
import uz.in_trade_map.repository.ProductRepository;
import uz.in_trade_map.repository.StatisticRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.BannerRequest;
import uz.in_trade_map.utils.request_objects.StatisticRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticService extends Validator<StatisticRequest> {
    private final StatisticRepository statisticRepository;

    public ResponseEntity<?> save(StatisticRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            }
            Statistic statistic = StatisticRequest.convertToStatistic(request, new Statistic());
            Statistic save = statisticRepository.save(statistic);
            return AllApiResponse.response(1, "Statistic created successfully!", DtoConverter.statisticDto(save,null));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error saving statistic!", e.getMessage());
        }
    }

    public ResponseEntity<?> update(Integer id, StatisticRequest request) {
        try {
            Optional<Statistic> optionalStatistic = statisticRepository.findByIdAndActiveTrue(id);
            if (optionalStatistic.isEmpty()) return AllApiResponse.response(422, 0, "Statistic not fount with id!");
            Map<String, Object> valid = valid(request);
            if (valid.size() > 0) {
                return AllApiResponse.response(422, 0, "Validator errors!", valid);
            }
            Statistic statistic = StatisticRequest.convertToStatistic(request, optionalStatistic.get());
            Statistic save = statisticRepository.save(statistic);
            return AllApiResponse.response(1, "Statistic updated successfully!", DtoConverter.statisticDto(save,null));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error updating statistic!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(int size, int page, String expand) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Statistic> all = statisticRepository.findAllByActiveTrueOrderByCreatedAtDesc(pageable);
            Map<String, Object> resp = new HashMap<>();
            resp.put("items", all.stream().map(banner -> DtoConverter.statisticDto(banner, expand)));
            resp.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), page, size));
            return AllApiResponse.response(1, "Success", resp);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all statistic!");
        }
    }

    public ResponseEntity<?> getOne(Integer id, String expand) {
        try {
            Optional<Statistic> op = statisticRepository.findByIdAndActiveTrue(id);
            if (op.isEmpty()) {
                return AllApiResponse.response(404, 0, "Statistic not fount with id!");
            }
            return AllApiResponse.response(1, "Success", DtoConverter.statisticDto(op.get(), expand));
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one statistic!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<Statistic> op = statisticRepository.findByIdAndActiveTrue(id);
            if (op.isEmpty()) {
                return AllApiResponse.response(404, 0, "Statistic not fount with id!");
            }
            Statistic statistic = op.get();
            statistic.setActive(false);
            statisticRepository.save(statistic);
            return AllApiResponse.response(1, "Statistic deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get one statistic!", e.getMessage());
        }
    }
}
