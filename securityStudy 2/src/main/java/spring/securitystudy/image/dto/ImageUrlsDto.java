package spring.securitystudy.image.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.securitystudy.image.entity.Image;

@Data
@AllArgsConstructor
public class ImageUrlsDto {

    private String url;

    public ImageUrlsDto(Image image){
        this.url = image.getUrl();
    }
}
