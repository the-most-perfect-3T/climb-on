package com.ohgiraffers.climbon.community.controller;

import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.community.service.PostService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/community")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 목록 페이지 - 페이지네이션 및 카테고리 필터링 추가 // 검색, 정렬순서 , D-Day(dday)추가
    @GetMapping
    public String getAllPosts(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String category, @RequestParam(required = false)
    String searchKeyword, @RequestParam(defaultValue = "latest") String sort , @RequestParam(required = false) String dday, @RequestParam(defaultValue = "list") String viewMode, @Param("status") Boolean status, Model model){  // page 파라미터와 pageSize를 사용해 해당 페이지에 맞는 게시글 목록을 조회
        // & category 파라미터를 받아 해당 카테고리의 게시글만 조회하도록 설정하고, 카테고리 파라미터가 없으면 모든 게시글 조회
        //  searchKeyword 파라미터를 추가해서 검색어가 있을 때만 검색 결과를 반환하도록 함.
        // 유효성 로직은 templates의 post.html에 있다.

        int pageSize = 15;

        // 일반 게시글
        List<PostDTO> posts = postService.getPostsByPageAndCategoryAndSearch(page, pageSize, category, searchKeyword, sort, dday, status);


        int totalPosts = postService.getTotalPostCount(category, searchKeyword); // 전체 게시글 수   //전체 게시글 수를 가져와 페이지수를 계산
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize); // 전체 페이지 수 계산  //ceil 함수는 올림을 해줌

        for (PostDTO post : posts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  postService.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        // '전체' 카테고리를 처리
        if ("전체".equals(category)) {
            category = null; // null로 설정하여 MyBatis에서 필터 무시
        }

        Map<String, List<PostDTO>> postsWithPinned = postService.getPostsWithPinned(
                page, pageSize, category, searchKeyword, sort, dday, status);

        model.addAttribute("pinnedNoticePosts", postsWithPinned.get("pinnedNoticePosts"));
        model.addAttribute("pinnedGuidePosts", postsWithPinned.get("pinnedGuidePosts"));
        model.addAttribute("generalPosts", postsWithPinned.get("generalPosts"));

        model.addAttribute("posts", posts);  // 뷰에 데이터 전달  (키, 객체)  //Thymeleaf는 ${키}로 입력하고 객체를 받음
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category != null ? category : "전체");
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);                       // 선택된 정렬 조건을 전달
        model.addAttribute("dday", dday);    //뷰로 dday 값 전달
        model.addAttribute("viewMode", viewMode);

        return "community/communityPost";  //template/community/post로 반환 (Spring MVC에서 뷰의 이름)
    }

    // 특정 게시글 상세 페이지
    @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") Integer id, Model model, Principal principal){// previousPost 와 nextPost 정보를 추가로 조회
        Integer userId = postService.getUserIdByUserName(principal.getName()); // 현재 사용자 ID 가져오기
        PostDTO post = postService.getPostById(id, userId); // 좋아요 여부 포함

        String userNickname =  postService.getUserNicknameById(post.getUserId());
        post.setUserNickname(userNickname);

        System.out.println(post.getEventStartDate());
        System.out.println(post.getEventEndDate());

        List<CommentDTO> comments = postService.getCommentsByPostId(id); // 댓글 목록 가져오기
        for (CommentDTO comment : comments) {
            comment.setUserNickname(userNickname);
        }

//        post.setUserNickname(postService.getUserNicknameById(userId));
        // postService의 메소드를 사용하여 이전,다음 게시글 정보 가져온다.
        PostDTO previousPost = postService.getPreviousPost(id); // 이전 게시글
        PostDTO nextPost = postService.getNextPost(id); // 다음 게시글
        model.addAttribute("post", post);
        model.addAttribute("previousPost", previousPost);  // 모델에 추가하여 postDetail.html에서 접근할 수 있게한다.
        model.addAttribute("nextPost", nextPost);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUserId", userId); // 현재 사용자 ID를 추가 // 현재 로그인된 사용자의 userId를 템플릿으로 넘긴다. 그리고 템플릿에서 post.userId와 직접 비교 (수정, 삭제권한위해)
        return "community/communityPostDetail"; // 상세보기용 communityPostDetail.html 템플릿 반환
    }

    // 게시글 작성 폼 페이지
    @GetMapping("/new")
    public String CreatePostForm(@RequestParam(required = false) String category, Model model){
        PostDTO post = new PostDTO();
        post.setCategory(category);
         model.addAttribute("post", new PostDTO()); // 빈 PostDTO 객체 전달 // 이렇게 하면 post 객체에 category를 설정했지만, 모델에 post 대신 새로 생성된 빈 PostDTO 객체를 전달하고 있다.
            // id를 null 값을 주기 위해 DTO에 자료형을 int 대신 integer를 썼다!
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
    public String createpost(@ModelAttribute PostDTO post, @RequestParam("isAnonymous") boolean isAnonymous, Principal principal) throws IOException {
        // principal.getName()이 이메일일 경우, 이를 기반으로 userId 조회
        String email = principal.getName(); //principal을 통해 로그인한 사용자 ID (로그인한 user_id가 이메일이라서 email변수를 씀) 가져오기  //principal : Spring security에서 현재 인증된 사용자의 정보를 담고 있는 객체, 이 객체를 사용하여 로그인한 사용자의 ID나 이름 같은 정보를 갖고 올 수 있다.

        // 이메일로 userId 조회
        Integer userId = postService.findUserIdByEmail(email); //DB의 user.id 조회 메소드 (솔직히 이건 짤 필요없다. 작성자부분과 관련있을줄 알고 짰다;)(아니였다 사실 ☆★여기서 로직을 구현해야 nickname이 DTO에 담겨서 구현이된다!!!)

        if (userId == null){
            throw new IllegalArgumentException("User ID not found for email: " + email);
        }

        // userId로 nickname 조회
        String userNickname = postService.getUserNicknameById(userId);
        if (userNickname == null){
            throw new IllegalArgumentException("User Nickname not found for ID: " + userId);
        }


        // PostDTO에 userId와 nickname 설정
        post.setUserId(userId);      //Principal 객체를 통해 현재 로그인한 사용자의 ID 또는 username을 쉽게 가져올 수 있다. ※대신에 principal.getName은 로그인한 이메일주소(유저아이디)만 가져올 수 있다! ,로그인한 사용자에게만 특정 데이터를 보여주거나, 해당 사용자가 작성한 게시글 등을 처리할 수 있다. (이 객체는 세션 내에서 관리되기 때문에, 사용자 정보를 안전하게 다룰 수 있다.)
        post.setUserNickname(userNickname);
        post.setStatus(true); // 게시글 상태 기본값 설정
        post.setAnonymous(isAnonymous);
//        System.out.println(userNickname); //닉네임 잘 담기는지확인 결과: 잘담긴다
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

//        // 이미지 업로드 처리
//        if(!imageFile.isEmpty()){
//            String uploadDir = "uploads/";
//            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//            File file = new File(uploadDir + fileName);
//            file.getParentFile().mkdirs(); // 디렉토리 생성
//            imageFile.transferTo(file);
//            post.setImageUrl("/" + uploadDir + fileName); // 이미지 URL 설정
//        }
        postService.insertPost(post); // 새로운 게시글 DB에 추가
        return "redirect:/community"; // 작성 후 게시글 목록으로 리다이렉트
    }

//  백업용 게시글 작성
//    // 게시글 작성 처리
//    @PostMapping("/new")
//    public String createpost(@ModelAttribute PostDTO post, Principal principal){
//        // 로그인한 사용자의 userId 가져오기
//        String userId = principal.getName(); //principal을 통해 로그인한 사용자 ID 가져오기  //principal : Spring security에서 현재 인증된 사용자의 정보를 담고 있는 객체, 이 객체를 사용하여 로그인한 사용자의 ID나 이름 같은 정보를 갖고 올 수 있다.
//        post.setUserId(userId); // post 객체에 userId 설정                                //Principal 객체를 통해 현재 로그인한 사용자의 ID 또는 username을 쉽게 가져올 수 있다. ,로그인한 사용자에게만 특정 데이터를 보여주거나, 해당 사용자가 작성한 게시글 등을 처리할 수 있다. (이 객체는 세션 내에서 관리되기 때문에, 사용자 정보를 안전하게 다룰 수 있다.)
//
//
//        postService.insertPost(post); // 새로운 게시글 DB에 추가
//        return "redirect:/community"; // 작성 후 게시글 목록으로 리다이렉트
//    }


    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Integer id, @ModelAttribute PostDTO post, @RequestParam("isAnonymous") boolean isAnonymous, Principal principal){
        post.setId(id);
        post.setAnonymous(isAnonymous);  // 수정 폼에도 따로 html에서의 isAnonymous값을 가져와야 하므로 여기서 set을 해준다! 다른 PostDTO들은 ModelAttribute로 받음
        postService.updatePost(post);
        return "redirect:/community/" + id;
    }

    // 게시글 수정 폼 페이지 (수정 버튼 눌렀을 때 호출 됨)
    @GetMapping("{id}/modify")
    public String modifyPostForm(@PathVariable Integer id, Model model, Principal principal){
        Integer userId = postService.getUserIdByUserName(principal.getName());
        PostDTO post = postService.getPostById(id, userId);
        model.addAttribute("post", post); // 가져온 PostDTO 객체를 "post"라는 이름으로 뷰에 전달 (그래서 게시글페이지에서 PostDTO 들의 필드값들을 볼 수 있다.)
        return "community/communityPostForm"; // 수정용 폼으로 반환
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Integer postId){
       postService.deletePost(postId);
       return "redirect:/community";
    }

    // 댓글 작성 처리
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") Integer postId, @RequestParam String content, Principal principal, Model model){

        // 1. 로그인된 사용자의 user_id(고유키) 가져오기
        Integer userId = postService.getUserIdByUserName(principal.getName());

        // 2. user_id로 users 테이블의 닉네임 갖오기
        String userNickname = postService.getUserNicknameById(userId); // userId를 가져오는 메소드

        CommentDTO comment = new CommentDTO();
        comment.setPostId(postId);
        comment.setUserId(userId); //  정수형 ID 사용
        comment.setContent(content);

        // 4. 모델에 닉네임 추가 (ex: 페이지 리다이렉트 전 표시)
        model.addAttribute("userNickname", userNickname);

        postService.insertComment(comment); // 댓글 추가
        return "redirect:/community/" + postId;
    }

    // 댓글 수정
    @PostMapping("/{postId}/comment/{id}/edit")
    public String modifyComment(@PathVariable("postId") Integer postId, @PathVariable("id") Integer commentId, @ModelAttribute CommentDTO comment, Principal principal){

        Integer userId = postService.getUserIdByUserName(principal.getName());
        comment.setUserId(userId);

        // 댓글 ID 설정 (URL에서 받은 ID를 사용)
        comment.setId(commentId);

        comment.setUpdatedAt(java.time.LocalDateTime.now());

        postService.updateComment(comment);

        return "redirect:/community/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{postId}/comment/{id}/delete")
    public String deleteComment(@PathVariable("postId") Integer postId, @PathVariable("id") Integer commentId, @ModelAttribute CommentDTO comment){
        comment.setId(commentId);
        postService.deleteComment(comment);
        return "redirect:/community/" + postId;
    }

    // 좋아요(하트) 증가 API (컨트롤러에서 AJAX 요청을 받아 좋아요 증가 처리)
    @PostMapping("/{id}/heart")
    public ResponseEntity<String> toggleHeart(@PathVariable("id") int postId, Principal principal) {
        Integer userId = postService.getUserIdByUserName(principal.getName()); // 로그인한 사용자 ID 갖고옴
        postService.toggleLike(postId, userId); // 좋아요 추가/취소 처리
        return ResponseEntity.ok("Heart toggled");
    }

}
