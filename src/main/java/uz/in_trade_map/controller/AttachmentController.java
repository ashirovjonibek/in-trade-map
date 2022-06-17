package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@CrossOrigin
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }

}
