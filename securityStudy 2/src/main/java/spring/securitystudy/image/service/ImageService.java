package spring.securitystudy.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.image.entity.Image;
import spring.securitystudy.image.repository.ImageRepository;
import spring.securitystudy.post.entity.Post;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.image.path}")
    private String uploadPath;

    @Transactional
    public void uploadImage(ImageUploadDto imageDto, Post post){
        if (imageDto.getFiles() != null && !imageDto.getFiles().isEmpty()){
            for (MultipartFile file : imageDto.getFiles()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid.toString();
                String fullPath = uploadPath + imageFileName;

                try {
                    file.transferTo(new File(fullPath));

                    Image image = new Image();
                    image.createNewImage(imageFileName, post);

                    post.addImage(image);
                    imageRepository.save(image);
                } catch (IOException e){
                    throw new RuntimeException("이미지 업로드 중 오류 발생", e);
                }
            }
        }
    }
}
