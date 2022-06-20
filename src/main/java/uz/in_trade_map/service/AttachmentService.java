package uz.in_trade_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.entity.Attachment;
import uz.in_trade_map.entity.AttachmentContent;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.AttachmentContentRepository;
import uz.in_trade_map.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Value("${file.saved.url}")
    private String uploadFolder;

    @Value("${file.saved.type}")
    private String savedType;

    public List<Attachment> uploadFile(List<MultipartFile> files) {
        List<Attachment> attachments = new ArrayList<>();
        if (files != null) {
            files.forEach(file -> {
                Date date = new Date();
                File folder = new File(String.format("%s/%d/%d/%d", uploadFolder, 1900 + date.getYear(), 1 + date.getMonth(), date.getDate()));
                if (!folder.exists() && folder.mkdirs()) {
                    System.out.println("folder created!!!");
                }
                if (savedType.equals("database")) {
                    Attachment attachment = new Attachment(file.getSize(), file.getOriginalFilename(), getExt(Objects.requireNonNull(file.getOriginalFilename())), file.getContentType());
                    Attachment save = attachmentRepository.save(attachment);
                    try {
                        AttachmentContent attachmentContent = new AttachmentContent(save, file.getBytes());
                        attachmentContentRepository.save(attachmentContent);
                        attachments.add(save);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw (new IllegalStateException("Error saved file!"));
                    }
                } else {
                    Attachment attachment = new Attachment(
                            file.getSize(),
                            file.getOriginalFilename(),
                            getExt(Objects.requireNonNull(file.getOriginalFilename())),
                            file.getContentType(),
                            uploadFolder

                    );
                    Attachment savedAttachment = attachmentRepository.save(attachment);
                    savedAttachment.setFilePath(String.format("%d/%d/%d/%s.%s", 1900 + date.getYear(), 1 + date.getMonth(), date.getDate(),
                            savedAttachment.getId(),
                            savedAttachment.getExtension()
                    ));
                    folder = folder.getAbsoluteFile();
                    File file1 = new File(folder, String.format("%s.%s", savedAttachment.getId(), savedAttachment.getExtension()));
                    try {
                        file.transferTo(file1);
                        attachments.add(attachmentRepository.save(savedAttachment));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw (new IllegalStateException("Error saved file!"));
                    }
                }
            });
        }
        return attachments.size() > 0 ? attachments : null;
    }

    public Attachment uploadFile(MultipartFile file) {
        Attachment attachmentRes = null;
        if (file != null) {
            Date date = new Date();
            File folder = new File(String.format("%s/%d/%d/%d", uploadFolder, 1900 + date.getYear(), 1 + date.getMonth(), date.getDate()));
            if (!folder.exists() && folder.mkdirs()) {
                System.out.println("folder created!!!");
            }
            if (savedType.equals("database")) {
                Attachment attachment = new Attachment(file.getSize(), file.getOriginalFilename(), getExt(Objects.requireNonNull(file.getOriginalFilename())), file.getContentType());
                attachmentRes = attachmentRepository.save(attachment);
                try {
                    AttachmentContent attachmentContent = new AttachmentContent(attachmentRes, file.getBytes());
                    attachmentContentRepository.save(attachmentContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Attachment attachment = new Attachment(
                        file.getSize(),
                        file.getOriginalFilename(),
                        getExt(Objects.requireNonNull(file.getOriginalFilename())),
                        file.getContentType(),
                        uploadFolder

                );
                Attachment savedAttachment = attachmentRepository.save(attachment);
                savedAttachment.setFilePath(String.format("%d/%d/%d/%s.%s", 1900 + date.getYear(), 1 + date.getMonth(), date.getDate(),
                        savedAttachment.getId(),
                        savedAttachment.getExtension()
                ));
                folder = folder.getAbsoluteFile();
                File file1 = new File(folder, String.format("%s.%s", savedAttachment.getId(), savedAttachment.getExtension()));
                try {
                    file.transferTo(file1);
                    attachmentRes = attachmentRepository.save(savedAttachment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return attachmentRes;
    }

    public ResponseEntity<?> getFile(UUID id) {
        Optional<Attachment> attachment = attachmentRepository.findByIdAndActiveTrue(id);
        if (attachment.isPresent()) {
            if (savedType.equals("database")) {
                Optional<AttachmentContent> attachmentContent = attachmentContentRepository.findByAttachmentId(id);
                if (attachmentContent.isPresent()) {
                    return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.get().getContentType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.get().getFileName() + "\"")
                            .body(attachmentContent.get().getContent());
                } else {
                    throw new IllegalArgumentException("Data not found with file id!");
                }
            } else {
                FileUrlResource fileUrlResource = null;
                try {
                    fileUrlResource = new FileUrlResource(String.format("%s/%s", uploadFolder, attachment.get().getFilePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(attachment.get().getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.get().getFileName() + "\"")
                        .body(fileUrlResource);
            }
        } else {
            throw new IllegalArgumentException("Data not found with file id!");
        }

    }

    public List<Attachment> getAttachments(List<UUID> attachmentIds) {
        return attachmentRepository.findAllById(attachmentIds);
    }

    public ResponseEntity<?> deleteAttachment(UUID id) {
        try {
            Optional<Attachment> byId = attachmentRepository.findById(id);
            if (byId.isPresent()) {
                Attachment attachment = byId.get();
                attachment.setActive(false);
                attachmentRepository.save(attachment);
                if (savedType.equals("database")) {
                    Optional<AttachmentContent> attachmentContent = attachmentContentRepository.findByAttachmentId(byId.get().getId());
                    if (attachmentContent.isPresent()) {
                        AttachmentContent attachmentContent1 = attachmentContent.get();
                        attachmentContent1.setActive(false);
                        attachmentContentRepository.save(attachmentContent1);
                    } else {
                        return AllApiResponse.response(404, 0, "File not fount with id!");
                    }
                }
                return AllApiResponse.response(1, "File deleted successfully!");
            } else {
                return AllApiResponse.response(404, 0, "File not fount with id!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AllApiResponse.response(500, 0, "Error for delete file!");
        }
    }

    private String getExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
