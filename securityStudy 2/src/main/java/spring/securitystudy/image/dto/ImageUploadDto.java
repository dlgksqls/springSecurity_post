package spring.securitystudy.image.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageUploadDto {

    private List<MultipartFile> files;
}
