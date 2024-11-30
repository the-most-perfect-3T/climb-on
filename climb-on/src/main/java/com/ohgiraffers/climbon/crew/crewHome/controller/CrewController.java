package com.ohgiraffers.climbon.crew.crewHome.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.*;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewBoardService;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewService;
import com.ohgiraffers.climbon.crew.mycrew.Enum.CrewRole;
import com.ohgiraffers.climbon.crew.mycrew.dto.UserCrewDTO;
import com.ohgiraffers.climbon.crew.mycrew.service.MyCrewService;
import com.ohgiraffers.climbon.user.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/crew")
public class CrewController {

    @Autowired
    private CrewBoardService crewBoardService;

    @Autowired
    CrewService crewService;

    @Autowired
    MyCrewService myCrewService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public ModelAndView getAllPosts(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String category,
                              @RequestParam(required = false) String searchKeyword, @RequestParam(defaultValue = "latest") String sort,
                              /*@RequestParam(required = false) String dday,*/ @RequestParam(defaultValue = "list") String viewMode,
                              @Param("status") Boolean status, ModelAndView mv){
        List<CrewDTO> crewList = crewService.getRecruitingCrews();

        // page 파라미터와 pageSize를 사용해 해당 페이지에 맞는 게시글 목록을 조회
        // & category 파라미터를 받아 해당 카테고리의 게시글만 조회하도록 설정하고, 카테고리 파라미터가 없으면 모든 게시글 조회
        // searchKeyword 파라미터를 추가해서 검색어가 있을 때만 검색 결과를 반환하도록 함.
        // 유효성 로직은 templates의 post.html에 있다.

        int pageSize = 15;

        // 전체 게시글 수   // 전체 게시글 수를 가져와 페이지수를 계산
        int totalPosts = crewBoardService.getTotalPostCount(category, searchKeyword);
        // 전체 페이지 수 계산  // ceil 함수는 올림을 해줌
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        // '전체' 카테고리를 처리
        if ("전체".equals(category)) {
            category = null; // null로 설정하여 MyBatis에서 필터 무시
        }

        Map<String, List<CrewPostDTO>> postsWithPinned = crewBoardService.getPostsWithPinned(
                page, pageSize, category, searchKeyword, sort, status);

        mv.addObject("crews", crewList);
        mv.addObject("pinnedNoticePosts", postsWithPinned.get("pinnedNoticePosts"));
        mv.addObject("generalPosts", postsWithPinned.get("generalPosts"));
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.addObject("category", category != null ? category : "전체");
        mv.addObject("searchKeyword", searchKeyword);
        mv.addObject("sort", sort);
        mv.addObject("viewMode", viewMode);

        mv.setViewName("/crew/crewHome/crewHome");
        return mv;
    }

    // 특정 게시글 상세 페이지
    @GetMapping("/post/{postId}")
    public ModelAndView getPostById(@PathVariable("postId") Integer postId, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails){// previousPost 와 nextPost 정보를 추가로 조회
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "크루 게시글은 해당 크루멤버만 조회 가능합니다.  \n로그인해주세요.");
            mv.setViewName("/auth/login");
        }
        else{
            Integer userId = userDetails.getLoginUserDTO().getId(); // 현재 사용자 ID 가져오기
            CrewPostDTO post = crewBoardService.getPostById(postId, userId); // 좋아요 여부 포함


            String userProfilePic = crewBoardService.getUserProfilePicById(post.getUserId());
            post.setUserProfilePic(userProfilePic);

            List<CrewCommentDTO> comments = crewBoardService.getCommentsByPostId(postId); // 댓글 목록 가져오기


            // 이전,다음 게시글 정보 가져온다.
            CrewPostDTO previousPost = crewBoardService.getPreviousPost(postId); // 이전 게시글
            CrewPostDTO nextPost = crewBoardService.getNextPost(postId); // 다음 게시글
            mv.addObject("post", post);
            mv.addObject("previousPost", previousPost);  // 모델에 추가하여 postDetail.html에서 접근할 수 있게한다.
            mv.addObject("nextPost", nextPost);
            mv.addObject("comments", comments);
            mv.addObject("currentUserId", userId); // 현재 사용자 ID를 추가 // 현재 로그인된 사용자의 userId를 템플릿으로 넘긴다. 그리고 템플릿에서 post.userId와 직접 비교 (수정, 삭제권한위해)
            mv.setViewName("crew/crewHome/crewPostDetail");
        }
        return mv;
    }

    // 댓글 작성 처리
    @PostMapping("/post/{postId}/comment")
    public ModelAndView addComment(@PathVariable("postId") Integer postId, @RequestParam String content, @AuthenticationPrincipal AuthDetail userDetails, ModelAndView mv){

        // 댓글 글자 수 제한 확인
        if (content.length() > 500) {
            throw new IllegalArgumentException("댓글은 500자를 초과할 수 없습니다.");
        }

        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "크루 게시글에 댓글 작성은 해당 크루멤버만 가능합니다.  \n로그인해주세요.");
            mv.setViewName("/auth/login");
        }
        else{
            // 1. 로그인된 사용자의 user_id(고유키) 가져오기
            Integer userId = userDetails.getLoginUserDTO().getId();

            CrewCommentDTO comment = new CrewCommentDTO();
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setContent(content);
            int result = crewBoardService.insertComment(comment);

            mv.setViewName("redirect:/crew/post/" + postId);
        }
        return mv;
    }

    // 댓글 수정
    @PostMapping("/post/{postId}/comment/{commentId}/edit")
    public ModelAndView modifyComment(@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId, @ModelAttribute CrewCommentDTO comment, @AuthenticationPrincipal AuthDetail userDetails, ModelAndView mv){
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "크루 게시글에 댓글 수정은 해당 크루멤버만 가능합니다.  \n로그인해주세요.");
            mv.setViewName("/auth/login");
        }
        else{
            Integer userId = userDetails.getLoginUserDTO().getId();
            comment.setUserId(userId);
            comment.setId(commentId);
            comment.setUpdatedAt(java.time.LocalDateTime.now());
            int result = crewBoardService.updateComment(comment);
            mv.setViewName("redirect:/crew/post/" + postId);
        }
        return mv;
    }

    // 댓글 삭제
    @PostMapping("/post/{postId}/comment/{commentId}/delete")
    public ModelAndView deleteComment(@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId, @ModelAttribute CrewCommentDTO comment, @AuthenticationPrincipal AuthDetail userDetails, ModelAndView mv){
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            mv.addObject("message", "크루 게시글에 댓글 삭제는 해당 크루멤버만 가능합니다.  \n로그인해주세요.");
            mv.setViewName("/auth/login");
        }
        else{
            comment.setId(commentId);
            int result = crewBoardService.deleteComment(comment);
            mv.setViewName("redirect:/crew/post/" + postId);
        }
        return mv;
    }

    // 좋아요(하트) 증가 API (컨트롤러에서 AJAX 요청을 받아 좋아요 증가 처리)
    @PostMapping("/{postId}/heart")
    public ResponseEntity<String> toggleHeart(@PathVariable("postId") int postId, @AuthenticationPrincipal AuthDetail userDetails) {
        Integer userId = userDetails.getLoginUserDTO().getId();
        crewBoardService.toggleLike(postId, userId); // 좋아요 추가/취소 처리
        return ResponseEntity.ok("Heart toggled");
    }


    // 크루게시판 글 작성 (권한 확인)
    @GetMapping("/writepost")
    public ModelAndView writePost( ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails) {
        if(Objects.isNull(userDetails)) {
            mv.setViewName("auth/login");
        } else {
            if(Objects.isNull(crewService.hasCrew(userDetails.getLoginUserDTO().getId()))){
                mv.setViewName("redirect:/crew/crewlist");
            }else {
                UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(userDetails.getLoginUserDTO().getId());
                CrewRole role = userCrewDTO.getRole();
                String crewRole = role.getRole();

                mv.addObject("role", userDetails.getLoginUserDTO().getUserRole());
                mv.addObject("crewRole", crewRole);
                mv.addObject("nickname", userDetails.getLoginUserDTO().getNickname());
                mv.setViewName("crew/crewBoardWritePost");
            }
        }
        return mv;
    }

    // 크루 소속인지 확인하는 Api
    @GetMapping("/checkHasCrew")
    public ResponseEntity<String> checkCrewName(@AuthenticationPrincipal AuthDetail userDetails){
        if(Objects.isNull(userDetails)) {
            return ResponseEntity.ok("로그인 부터 하세요.");
        }else{
            if(Objects.isNull(crewService.hasCrew(userDetails.getLoginUserDTO().getId()))) {
                return ResponseEntity.ok("소속된 크루X");
            }else {
                return ResponseEntity.ok("okay");
            }
        }
    }


    @PostMapping("/writepost")
    public String registPost(CrewBoardDTO crewBoardDTO, @AuthenticationPrincipal AuthDetail userDetails) {
        int id = userDetails.getLoginUserDTO().getId();
        Integer crew_code = crewBoardService.getCrewCode(id);

        crewBoardDTO.setCrewCode(crew_code);
        crewBoardDTO.setUserId(id);
        int result = crewBoardService.insertPost(crewBoardDTO);
        if (result == 0) {
            String message = "등록 실패";
        }
        int postId = crewBoardService.getJustAddedPostById(id);
        return "redirect:/crew/post/" + postId;
    }

    @GetMapping("/updatepost/{id}")
    public ModelAndView updatePost(@PathVariable int id, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails) {
        CrewBoardDTO crewBoardDTO = crewBoardService.selectOnePostById(id);
        UserCrewDTO userCrewDTO = myCrewService.getMyCrewCodeAndRole(userDetails.getLoginUserDTO().getId());
        CrewRole role = userCrewDTO.getRole();
        String crewRole = role.getRole();

        mv.addObject("crewRole", crewRole);
        mv.addObject("crewBoardDTO", crewBoardDTO);
        mv.addObject("role", userDetails.getLoginUserDTO().getUserRole());
        mv.addObject("nickname", userDetails.getLoginUserDTO().getNickname());
        mv.setViewName("crew/crewBoardUpdatePost");
        return mv;
    }

    @PostMapping("/updatepost/{id}")
    public String updatePost(@PathVariable int id, CrewBoardDTO crewBoardDTO, ModelAndView mv) {
        crewBoardDTO.setId(id);
        int result = crewBoardService.updatePostById(crewBoardDTO);
        return "redirect:/crew/post/" + id;
    }



    @GetMapping("/crewlist")
    public ModelAndView crewList(ModelAndView mv) {
        // 총 크루수 조회
        int totalCount = crewService.countAllCrews();
        // 크루정보 조회 (5개만 보내준다)
        List<CrewDTO> crews = crewService.selectCrews();

        if(Objects.isNull(crews)){
            mv.addObject("message", "조회 가능한 크루가 없습니다.");
        }else{
            mv.addObject("crews", crews);
            mv.addObject("totalCount", totalCount);
        }
        mv.setViewName("crew/crewHome/crewList");
        return mv;
    }

    @GetMapping("/crewlistMore")
    public ResponseEntity<?> crewList(
            @RequestParam Integer page,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) List<String> areas) {

        int offset = page * 5;
        Integer count = null;

        count = crewService.countCrewsFilteredByAreas(areas);

        List<CrewDTO> crews = crewService.selectFiveCrews(offset, sort, areas);

        /*if (crews == null || crews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("더이상 조회할 크루가 없습니다.");
        } else {*/
            CrewListWithCount crewListWithCount = new CrewListWithCount(count, crews);
            return ResponseEntity.ok(crewListWithCount);
//        }
    }

    @GetMapping("/checkCrewName")
    public ResponseEntity<String> checkCrewName(@RequestParam Map<String, Object> parameters){
        String crewName = (String)parameters.get("crewName");
        if(crewService.isCrewNameExists(crewName)){
            return ResponseEntity.ok("중복된 크루 이름 입니다. \n다시 입력해주세요.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("/registerCrew")
    public ModelAndView registerCrew(CrewDTO crewDTO, ModelAndView mv, @AuthenticationPrincipal AuthDetail userDetails) {
        int id = userDetails.getLoginUserDTO().getId();
        crewDTO.setCaptainId(id);
        int resultForCrewRegister = crewService.registerCrew(crewDTO);

        if(resultForCrewRegister == 0){
            mv.addObject("message", "크루 등록중에 문제가 발생했습니다.");
            mv.setViewName("/");
        }else{
            int crewCode = crewService.getCrewCodeFromCrewsByUserId(id);
            int resultForUserCrewRoleInsert = crewService.crewCaptainInsert(id, crewCode);
            mv.setViewName("redirect:/crew/crewlist");
        }
        return mv;
    }


}
