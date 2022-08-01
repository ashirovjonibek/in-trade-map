package uz.in_trade_map.controller;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.chat.ChatMessage;
import uz.in_trade_map.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final SocketIONamespace namespace;

    @Autowired
    public AttachmentController(SocketIOServer server,AttachmentService attachmentService) {
        this.attachmentService=attachmentService;
        this.namespace = server.addNamespace("/chat");
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }

    @PostMapping
    public HttpEntity<?> post(@RequestPart MultipartFile file) {
        namespace.getBroadcastOperations().sendEvent("groups", file.getOriginalFilename());
        return ResponseEntity.ok(attachmentService.uploadFile(file));
    }

}
