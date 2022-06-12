package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.in_trade_map.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }

}
