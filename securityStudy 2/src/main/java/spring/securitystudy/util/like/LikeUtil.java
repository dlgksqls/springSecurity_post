package spring.securitystudy.util.like;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LikeUtil {

    public static Map<Long, Long> countLikesByPostsIds(List<Object[]> likeObjects){
        return likeObjects
                .stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> (Long) arr[1]
                ));
    }
}
