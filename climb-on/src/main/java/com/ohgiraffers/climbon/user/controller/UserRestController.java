/*
*   2024-11-22 최초 작성
*   작성자: 최정민
* */

package com.ohgiraffers.climbon.user.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.community.dto.CommentDTO;
import com.ohgiraffers.climbon.community.dto.PostDTO;
import com.ohgiraffers.climbon.community.service.PostService;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewBoardService;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.ReviewDTO;
import com.ohgiraffers.climbon.facilities.service.FacilitiesService;
import com.ohgiraffers.climbon.facilities.service.ReviewService;
import com.ohgiraffers.climbon.user.dto.UserDTO;
import com.ohgiraffers.climbon.user.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestController {


    @Autowired
    private UserService userService;
    @Autowired
    private FacilitiesService facilitiesService;
    @Autowired
    private PostService postService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CrewBoardService crewBoardService;


    @PostMapping("/registFacility")
    public ResponseEntity<Object> registFacility(@AuthenticationPrincipal AuthDetail userDetails,
                                                 @RequestBody Map<String, String> requestBody){


        // 로그인 정보 없으면
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인 정보가 없습니다."));
        }

        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();

        String facilityName = requestBody.get("facilityName");
        System.out.println(key + " " + facilityName);
        int facilityId = facilitiesService.getFacilityIdByName(facilityName);
        System.out.println("facilityId = " + facilityId);
        int result = userService.updateFacility(key, facilityId);
        if(result > 0){
            // 성공
            return ResponseEntity.ok(Map.of("message", "홈짐이 등록되었습니다."));
        }else {
            return ResponseEntity.status(500).body(Map.of("message", "홈짐 등록에 실패했습니다."));
        }

    }


    @GetMapping("/favorite")
    public ResponseEntity<Object> selectFavorite(@AuthenticationPrincipal AuthDetail userDetails){
        System.out.println("즐겨찾기 요청");

        // 로그인 정보 없으면
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인 정보가 없습니다."));
        }

        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();
        List<FacilitiesDTO>  facilities = facilitiesService.getFacilitiesByUserFavorite(key);

        if (facilities == null || facilities.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "저장된 즐겨찾기가 없습니다."));
        }


        return ResponseEntity.ok(facilities);
    }


    @GetMapping("/review")
    public ResponseEntity<Object> selectReview(@AuthenticationPrincipal AuthDetail userDetails){
        System.out.println("리뷰 요청");

        // 로그인 정보 없으면
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인 정보가 없습니다."));
        }

        // 유저 pk
        Integer key = userDetails.getLoginUserDTO().getId();
        List<ReviewDTO> reviewList = reviewService.getReviewByUserId(key);

        if (reviewList == null || reviewList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "작성한 리뷰가 없습니다."));
        }

        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/board")
    public ResponseEntity<Object> getAllPosts(@AuthenticationPrincipal AuthDetail userDetails,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(required = false) String category,
                                              @RequestParam(required = false) String searchKeyword,
                                              @RequestParam(defaultValue = "latest") String sort,
                                              @RequestParam(required = false) String dday,
                                              @Param("status") Boolean status){

        /*int pageSize = 15;*/
        int pageSize = postService.getTotalPostCount(category, searchKeyword); // status 1 만 불러옴
        System.out.println("pageSize = " + pageSize);

        // 일반 게시글
        /*List<PostDTO> posts = postService.getPostsByPageAndCategoryAndSearch(page, pageSize, category, searchKeyword, sort, dday, status);*/

        /*int totalPages = (int) Math.ceil((double) totalPosts / pageSize);*/

        /*for (PostDTO post : posts) {
            String userNickname =  postService.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }*/


        Map<String, List<PostDTO>> postsWithPinned = postService.getPostsWithPinned(
                page, pageSize, category, searchKeyword, sort, dday, status);

        if(postsWithPinned == null || postsWithPinned.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "작성한 게시글이 없습니다."));
        }

//        Map<String, Object> resultMap = new HashMap<>();

/*
        List<PostDTO> pinnedNoticePosts = postsWithPinned.get("pinnedNoticePosts");
        System.out.println("pinnedNoticePosts = " + pinnedNoticePosts);

        if(pinnedNoticePosts != null || !pinnedNoticePosts.isEmpty()) {
            Iterator<PostDTO> iterator = pinnedNoticePosts.iterator();
            while (iterator.hasNext()) {
                PostDTO post = iterator.next();
                if (post.getUserId() != userId) {
                    System.out.println("해당 아이디 삭제");
                    iterator.remove();
                } else {
                    System.out.println("같은거 있음");
                }
            }
        }



        List<PostDTO> pinnedGuidePosts = postsWithPinned.get("pinnedGuidePosts");System.out.println("pinnedGuidePosts = " + pinnedGuidePosts);

        if(pinnedGuidePosts != null || !pinnedGuidePosts.isEmpty()) {
            Iterator<PostDTO> iterator = pinnedGuidePosts.iterator();
            while (iterator.hasNext()) {
                PostDTO post = iterator.next();
                if (post.getUserId() != userId) {
                    System.out.println("해당 아이디 삭제");
                    iterator.remove();
                } else {
                    System.out.println("같은거 있음");
                }
            }
        }*/


        /*resultMap.put("generalPosts", generalPosts);*/


        // 유저 아이디
        Integer userId = userDetails.getLoginUserDTO().getId();

        // 일반 포스트
        List<PostDTO> generalPosts = postsWithPinned.get("generalPosts");
//        System.out.println("generalPosts = " + generalPosts);
        if(generalPosts != null || !generalPosts.isEmpty()) {
            Iterator<PostDTO> iterator = generalPosts.iterator();
            while (iterator.hasNext()) {
                PostDTO post = iterator.next();
                if (post.getUserId() != userId) { // 해당 유저가 아니면 삭제
                    iterator.remove();
                }
            }
        }

        /*System.out.println("generalPosts = " + generalPosts);
        System.out.println("generalPosts = " + generalPosts.size());*/


        if(generalPosts.isEmpty()){
            return ResponseEntity.ok(Map.of("message", "작성한 게시글이 없습니다."));
        }


        return ResponseEntity.ok(generalPosts);
    }




    @GetMapping("/crew")
        public ResponseEntity<Object> getAllCrewPosts(@AuthenticationPrincipal AuthDetail userDetails,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(required = false) String category,
                                                      @RequestParam(required = false) String searchKeyword,
                                                      @RequestParam(defaultValue = "latest") String sort,
                                                      @Param("status") Boolean status){

        // 전체 게시글
        int pageSize = crewBoardService.getTotalPostCount(category, searchKeyword);
        System.out.println("pageSize = " + pageSize);

        Map<String, List<CrewPostDTO>> postsWithPinned = crewBoardService.getPostsWithPinned(
                page, pageSize, category, searchKeyword, sort, status);

        if(postsWithPinned == null || postsWithPinned.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "작성한 게시글이 없습니다."));
        }

        // 유저 아이디
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        Integer userId = userDetails.getLoginUserDTO().getId();

        // 일반 포스트
        List<CrewPostDTO> generalPosts = postsWithPinned.get("generalPosts");
        System.out.println("generalPosts = " + generalPosts);
        System.out.println("generalPosts.size() = " + generalPosts.size());
        if(generalPosts != null && !generalPosts.isEmpty()) {
            Iterator<CrewPostDTO> iterator = generalPosts.iterator();
            while (iterator.hasNext()) {
                CrewPostDTO post = iterator.next();
                if (post.getUserId() != userId) { // 해당 유저가 아니면 삭제
                    iterator.remove();
                }
            }
        }


        if(generalPosts.isEmpty()){
            return ResponseEntity.ok(Map.of("message", "작성한 게시글이 없습니다."));
        }


        System.out.println("generalPosts = " + generalPosts);
        System.out.println("generalPosts.size() = " + generalPosts.size());


        return ResponseEntity.ok(generalPosts);
    }


    @GetMapping("comments")
    public ResponseEntity<Object> getComments(@AuthenticationPrincipal AuthDetail userDetails){

        // 유저 아이디
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        Integer id = userDetails.getLoginUserDTO().getId();
        /*System.out.println("id = " + id);*/

        // 댓글 목록 가져오기
        List<CommentDTO> comments = postService.getCommentsById(id);
        for (CommentDTO comment : comments) {
            String commentUserNickname = postService.getUserNicknameById(comment.getUserId());
            String commentUserProfilePic = postService.getUserProfilePicById(comment.getUserId());
            comment.setUserNickname(commentUserNickname);
            comment.setUserProfilePic(commentUserProfilePic);
        }

        System.out.println("comments = " + comments);

        if(comments.isEmpty() || comments == null){
            return ResponseEntity.ok(Map.of("comments", "작성한 댓글이 없습니다."));
        }

        return ResponseEntity.ok(comments);
    }


    /*@GetMapping("crewComments")
    public ResponseEntity<Object> getCrewComments(@AuthenticationPrincipal AuthDetail userDetails){

        // 유저 아이디
        if (userDetails == null || userDetails.getLoginUserDTO() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        Integer id = userDetails.getLoginUserDTO().getId();
        System.out.println("id = " + id);

    }*/

    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<Object> getUserModal(@PathVariable("userId") Integer userId){

        System.out.println("요청받음");
        UserDTO user = userService.findByKey(userId);
        System.out.println("user = " + user);

        if(user == null){
            ResponseEntity.ok(Map.of("message", "해당 내용이 없습니다."));
        }

        String crewName = userService.findCrewName(userId);
        String homeName = userService.findHomeName(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("crewName", crewName);
        response.put("homeName", homeName);


        return ResponseEntity.ok(response);
    }

}
