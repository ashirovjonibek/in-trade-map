package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment extends AbsEntity {
    private Long size;

    private String fileName;

    private String extension;

    private String contentType;

    public Attachment(Long size, String fileName, String extension, String contentType) {
        this.size = size;
        this.fileName = fileName;
        this.extension = extension;
        this.contentType = contentType;
    }

    private String filePath;
}
