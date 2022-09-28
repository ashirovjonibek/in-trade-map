package uz.in_trade_map.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.dtos.Meta;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.News;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.NewsRepository;
import uz.in_trade_map.utils.dto_converter.DtoConverter;
import uz.in_trade_map.utils.request_objects.NewsRequest;
import uz.in_trade_map.utils.validator.Validator;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NewsService extends Validator<NewsRequest> {
    @Value("${file.saved.url}")
    private String uploadFolder;

    private final NewsRepository newsRepository;
    private final AttachmentService attachmentService;

    public ResponseEntity<?> save(NewsRequest request) {
        try {
            Map<String, Object> valid = valid(request);
            if (valid.size() == 0) {
                Attachment photo = attachmentService.uploadFile(request.getGeneralPhoto());
                News news = new News();
                NewsRequest.convertToEntity(news, request);
                news.setPhoto(photo);
                News save = newsRepository.save(news);
                return AllApiResponse.response(1, "Success", DtoConverter.newsDto(save));
            } else {
                return AllApiResponse.response(429, 0, "Validator error!", valid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error save news!", e.getMessage());
        }
    }

    public ResponseEntity<?> edit(Integer id, NewsRequest request, UUID photoId) {
        try {
            Optional<News> newsOptional = newsRepository.findByIdAndActiveTrue(id);
            if (newsOptional.isPresent()) {
                Map<String, Object> valid = valid(request);
                if (valid.size() == 0) {
                    News news = NewsRequest.convertToEntity(newsOptional.get(), request);
                    if (request.getGeneralPhoto() != null && !request.getGeneralPhoto().isEmpty()) {
                        Attachment photo = attachmentService.uploadFile(request.getGeneralPhoto());
                        news.setPhoto(photo);
                    } else if (photoId != null) {
                        List<UUID> ids = new ArrayList<>();
                        ids.add(photoId);
                        List<Attachment> attachments = attachmentService.getAttachments(ids);
                        if (attachments.size() > 0) {
                            news.setPhoto(attachments.get(0));
                        }
                    } else news.setPhoto(null);
                    News save = newsRepository.save(news);
                    return AllApiResponse.response(1, "News updated successfully!", DtoConverter.newsDto(save));
                } else {
                    return AllApiResponse.response(429, 0, "Validator error!", valid);
                }
            }
            return AllApiResponse.response(404, 0, "News not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error save news!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll(int size, int page) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<News> all = newsRepository.findAllByActiveTrueOrderByCreatedAtDesc(pageable);
            Map<String, Object> response = new HashMap<>();
            response.put("items", all.stream().map(DtoConverter::newsDto));
            response.put("meta", new Meta(all.getTotalElements(), all.getTotalPages(), page, size));
            return AllApiResponse.response(1, "Success", response);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get all news!", e.getMessage());
        }
    }

    public ResponseEntity<?> getOne(Integer id) {
        try {
            Optional<News> byId = newsRepository.findByIdAndActiveTrue(id);
            if (byId.isPresent()) {
                return AllApiResponse.response(1, "Success", DtoConverter.newsDto(byId.get()));
            } else return AllApiResponse.response(404, 0, "News not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get news!", e.getMessage());
        }
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<News> byId = newsRepository.findByIdAndActiveTrue(id);
            if (byId.isPresent()) {
                News news = byId.get();
                news.setActive(false);
                newsRepository.save(news);
                return AllApiResponse.response(1, "News deleted successfully!");
            } else return AllApiResponse.response(404, 0, "News not fount with id!");
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error get news!", e.getMessage());
        }
    }

}
