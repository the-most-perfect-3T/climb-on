package com.ohgiraffers.climbon.community.controller;

import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/community")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 목록 페이지 - 페이지네이션 및 카테고리 필터링 추가 // 검색, 정렬순서 , D-Day(dday)추가
    @GetMapping
    public String getAllPosts(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String category, @RequestParam(required = false)
    String searchKeyword, @RequestParam(defaultValue = "latest") String sort , @RequestParam(required = false) String dday, Model model){  // page 파라미터와 pageSize를 사용해 해당 페이지에 맞는 게시글 목록을 조회
        // & category 파라미터를 받아 해당 카테고리의 게시글만 조회하도록 설정하고, 카테고리 파라미터가 없으면 모든 게시글 조회
        //  searchKeyword 파라미터를 추가해서 검색어가 있을 때만 검색 결과를 반환하도록 함.
        // 유효성 로직은 templates의 post.html에 있다.
        int pageSize = 15;
        List<PostDTO> posts = postService.getPostsByPageAndCategoryAndSearch(page, pageSize, category, searchKeyword, sort, dday);
        int totalPosts = postService.getTotalPostCount(category, searchKeyword); // 전체 게시글 수   //전체 게시글 수를 가져와 페이지수를 계산
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize); // 전체 페이지 수 계산  //ceil 함수는 올림을 해줌

        model.addAttribute("posts", posts);  // 뷰에 데이터 전달  (키, 객체)  //Thymeleaf는 ${키}로 입력하고 객체를 받음
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);                       // 선택된 정렬 조건을 전달
        model.addAttribute("dday", dday);    //뷰로 dday 값 전달

//        for (PostDTO post: posts) {   게시글에 DTO 값들이 잘 담기는지 확인
//            System.out.println(post.getTitle());
//            System.out.println(post.getCreatedAt());
//        }

        return "community/communityPost";  //template/community/post로 반환 (Spring MVC에서 뷰의 이름)
    }

    // 특정 게시글 상세 페이지
    @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") Integer id, Model model){// previousPost 와 nextPost 정보를 추가로 조회
        // 조회수 증가
        postService.incrementViewCount(id);

        PostDTO post = postService.getPostById(id);
        // postService의 메소드를사용하여 이전,다음 게시글 정보 가져온다.
        PostDTO previousPost = postService.getPreviousPost(id); // 이전 게시글
        PostDTO nextPost = postService.getNextPost(id); // 다음 게시글
        model.addAttribute("post", post);
        model.addAttribute("previousPost", previousPost);  // 모델에 추가하여 postDetail.html에서 접근할 수 있게한다.
        model.addAttribute("nextPost", nextPost);
        return "community/communityPostDetail"; // 상세보기용 communityPostDetail.html 템플릿 반환
    }

    // 게시글 작성 폼 페이지
    @GetMapping("/new")
    public String CreatePostForm(@RequestParam(required = false) String category, Model model){
        PostDTO post = new PostDTO();
        post.setCategory(category);
        model.addAttribute("post", new PostDTO()); // 빈 PostDTO 객체 전달
        return "community/communityPostForm"; // communityPostForm.html 템플릿 반환
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // "yyyy-MM-dd" 형식의 문자열을 java.sql.Date로 변환하도록 설정  ☆★ 이걸로 오류해결 결국 String으로 입력 받고, 여기서 변환 시켜주면 소식,카테고리 둘다 게시글 작성이 가능해진다. eventdate 필드가 빈값으로 받아서 sql.date 변환하려는것이  (기존은 DTO 임포트를 java.sql.Date로했음 이걸로 하지말자 호환이 잘 안된다.)적용 안됨!
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  // true는 필드가 null 허용
    }


    // 게시글 작성 처리
    @PostMapping("/new")
    public String createpost(@ModelAttribute PostDTO post, Principal principal){
        // 로그인한 사용자의 userId 가져오기
        String userId = principal.getName(); //principal을 통해 로그인한 사용자 ID 가져오기  //principal : Spring security에서 현재 인증된 사용자의 정보를 담고 있는 객체, 이 객체를 사용하여 로그인한 사용자의 ID나 이름 같은 정보를 갖고 올 수 있다.
        post.setUserId(userId); // post 객체에 userId 설정                                //Principal 객체를 통해 현재 로그인한 사용자의 ID 또는 username을 쉽게 가져올 수 있다. ,로그인한 사용자에게만 특정 데이터를 보여주거나, 해당 사용자가 작성한 게시글 등을 처리할 수 있다. (이 객체는 세션 내에서 관리되기 때문에, 사용자 정보를 안전하게 다룰 수 있다.)

//        // '소식' 카테고리가 아닌 경우, 날짜 필드를 null로 설정  // 이거 안해주면 소식이 아닌 카테고리 게시글 등록할 때 eventstartdate 와 eventenddate 필드가 비어 있어서 이걸 Spring 이 sql.date로 변환하려다 오류남 null로 채워줘야함
//        // 이제 이거 필요없다 initBinder가 해결한다!
//        if (!"소식".equals(post.getCategory())){
//            post.setEventStartDate(null);
//            post.setEventEndDate(null);
//        } else {
//            // '소식' 카테고리인데 날짜 값이 빈 문자열인 경우에도 null로 설정
//            if (post.getEventStartDate() == null || post.getEventStartDate().toString().isEmpty()) {
//                post.setEventStartDate(null);
//            }
//            if (post.getEventEndDate() == null || post.getEventEndDate().toString().isEmpty()) {
//                post.setEventEndDate(null);
//            }
//        }

        postService.insertPost(post); // 새로운 게시글 DB에 추가
        return "redirect:/community"; // 작성 후 게시글 목록으로 리다이렉트
    }

    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Integer id, @ModelAttribute PostDTO post){
        post.setId(id);
        postService.updatePost(post);
        return "redirect:/community/" + id;
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Integer id){
       postService.deletePost(id);
       return "redirect:/community";
    }

}
