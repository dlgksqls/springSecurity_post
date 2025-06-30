    package spring.securitystudy.controller;

    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import spring.securitystudy.friendship.service.FriendShipService;
    import spring.securitystudy.member.MemberDetails;
    import spring.securitystudy.member.entity.Member;
    import spring.securitystudy.member.service.MemberService;
    import spring.securitystudy.post.dto.PostViewDto;
    import spring.securitystudy.post.entity.Post;
    import spring.securitystudy.post.service.PostService;

    import java.security.Principal;
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    @RequiredArgsConstructor
    public class IndexController {

        private final MemberService memberService;
        private final PostService postService;
        private final FriendShipService friendShipService;

        @GetMapping("/")
        public String index(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @AuthenticationPrincipal MemberDetails memberDetails) {
            if (memberDetails != null) {
                MemberDetails findMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                List<PostViewDto> allPost = postService.findAll();
                Page<PostViewDto> pagePosts = postService.findAllByPage(page, 10);
                List<String> friendList = friendShipService.findFriendShipList(findMember.getMember());

                model.addAttribute("username", memberDetails.getUsername());
                model.addAttribute("posts", pagePosts.getContent());
                model.addAttribute("friendList", friendList);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", pagePosts.getTotalPages());
            }

            return "index";
        }
    }
