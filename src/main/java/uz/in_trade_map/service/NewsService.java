package uz.in_trade_map.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.News;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.NewsRepository;
import uz.in_trade_map.utils.request_objects.NewsRequest;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsService {
    @Value("${file.saved.url}")
    private String uploadFolder;

    private final NewsRepository newsRepository;
    private final AttachmentService attachmentService;

    public ResponseEntity<?> save(NewsRequest request) {
        try {
            Gson gson = new Gson();
            String s = gson.toJson(request);
            Attachment content = attachmentService.uploadFile(s);
            Attachment photo = attachmentService.uploadFile(request.getGeneralPhoto());
            News news = new News();
            news.setContent(content);
            news.setPhoto(photo);
            news.setActive(true);
            News save = newsRepository.save(news);
            return AllApiResponse.response(1, "Success", save);
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error save news!", e.getMessage());
        }
    }

    public ResponseEntity<?> getAll() {
        List<News> all = newsRepository.findAll();
        return AllApiResponse.response(1, "success", all.stream().map(e -> {
            Map<String, Object> a = new HashMap<>();
            a.put("id", e.getId());
            try {
                a.put("content", e.getContent() != null ? readFromInputStream(new FileInputStream(uploadFolder+"\\" + e.getContent().getFilePath())) : null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return a;
        }));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
