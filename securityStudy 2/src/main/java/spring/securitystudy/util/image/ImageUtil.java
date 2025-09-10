package spring.securitystudy.util.image;

import spring.securitystudy.image.entity.Image;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageUtil {

    public static Map<Long, List<String>>  getAllImages(List<Image> images){
        return images
                .stream()
                .collect(Collectors.groupingBy(
                        image -> image.getPost().getId(),
                        Collectors.mapping(Image::getUrl, Collectors.toList())
                ));
    }
}
