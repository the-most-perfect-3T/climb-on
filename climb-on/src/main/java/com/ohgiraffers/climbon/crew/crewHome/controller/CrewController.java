package com.ohgiraffers.climbon.crew.crewHome.controller;

import com.ohgiraffers.climbon.auth.model.AuthDetail;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewListWithCount;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewPostDTO;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewBoardService;
import com.ohgiraffers.climbon.crew.crewHome.service.CrewService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/home")
    public ModelAndView getAllPosts(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String category,
                              @RequestParam(required = false) String searchKeyword, @RequestParam(defaultValue = "latest") String sort,
                              /*@RequestParam(required = false) String dday,*/ @RequestParam(defaultValue = "list") String viewMode,
                              @Param("status") Boolean status, ModelAndView mv){
        // page 파라미터와 pageSize를 사용해 해당 페이지에 맞는 게시글 목록을 조회
        // & category 파라미터를 받아 해당 카테고리의 게시글만 조회하도록 설정하고, 카테고리 파라미터가 없으면 모든 게시글 조회
        // searchKeyword 파라미터를 추가해서 검색어가 있을 때만 검색 결과를 반환하도록 함.
        // 유효성 로직은 templates의 post.html에 있다.

        int pageSize = 15;

        // 일반 게시글
        List<CrewPostDTO> posts = crewBoardService.getPostsByPageAndCategoryAndSearch(page, pageSize, category, searchKeyword, sort, /*dday,*/ status);
        // 전체 게시글 수   // 전체 게시글 수를 가져와 페이지수를 계산
        int totalPosts = crewBoardService.getTotalPostCount(category, searchKeyword);
        // 전체 페이지 수 계산  // ceil 함수는 올림을 해줌
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        for (CrewPostDTO post : posts) {
            // 각 게시글의 userId를 사용해 닉네임 조회 후 설정
            String userNickname =  crewBoardService.getUserNicknameById(post.getUserId());
            post.setUserNickname(userNickname);
        }

        // '전체' 카테고리를 처리
        if ("전체".equals(category)) {
            category = null; // null로 설정하여 MyBatis에서 필터 무시
        }

        Map<String, List<CrewPostDTO>> postsWithPinned = crewBoardService.getPostsWithPinned(
                page, pageSize, category, searchKeyword, sort, /*dday,*/ status);

        mv.addObject("pinnedNoticePosts", postsWithPinned.get("pinnedNoticePosts"));
//        mv.addObject("pinnedGuidePosts", postsWithPinned.get("pinnedGuidePosts"));
        mv.addObject("generalPosts", postsWithPinned.get("generalPosts"));

        mv.addObject("posts", posts);  // 뷰에 데이터 전달  (키, 객체)  //Thymeleaf는 ${키}로 입력하고 객체를 받음
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.addObject("category", category != null ? category : "전체");
        mv.addObject("searchKeyword", searchKeyword);
        mv.addObject("sort", sort);                       // 선택된 정렬 조건을 전달
//        mv.addObject("dday", dday);    //뷰로 dday 값 전달
        mv.addObject("viewMode", viewMode);

        mv.setViewName("/crew/crewHome/crewHome");
        return mv;
    }


    @GetMapping("/writepost")
    public String writePost( Model model) {
//        model.addAttribute("nickname", userDetails.getLoginUserDTO().getNickname());

        // crew 인지 확인하는 메소드 추가
        // 소속 crew 가 있어야만 게시글 작성 가능
        return "/crew/crewBoardWritePost";
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

        return "redirect:/crew/crewBoardList";
    }

    @GetMapping("/crewBoardList")
    public ModelAndView crewBoardList(ModelAndView mv) {
        CrewBoardDTO boardDTO = crewBoardService.selectLastPost();
        mv.addObject("boardDTO", boardDTO);
        mv.setViewName("crew/crewBoardList");
        return mv;
    }

    @GetMapping("/updatepost/{id}")
    public ModelAndView updatePost(@PathVariable int id, ModelAndView mv) {
        CrewBoardDTO crewBoardDTO = crewBoardService.selectOnePostById(id);
        System.out.println(crewBoardDTO);
        mv.addObject("crewBoardDTO", crewBoardDTO);
        mv.setViewName("crew/crewBoardUpdatePost");
        return mv;
    }

    @PostMapping("/updatepost/{id}")
    public String updatePost(@PathVariable int id, CrewBoardDTO crewBoardDTO, ModelAndView mv) {
        crewBoardDTO.setId(id);
        int result = crewBoardService.updatePostById(crewBoardDTO);
        return "redirect:/crew/updatedpost?id=" + id;
    }

    @GetMapping("/updatedpost")
    public ModelAndView updatedpost(@RequestParam int id, ModelAndView mv) {
        CrewBoardDTO crewBoardDTO = crewBoardService.selectOnePostById(id);
        mv.addObject("boardDTO", crewBoardDTO);
        mv.setViewName("crew/crewBoardList");
        return mv;
    }

    @GetMapping("/crewlist")
    public ModelAndView crewList(ModelAndView mv) {
        // 총 크루수 조회
        int totalCount = crewService.countAllCrews();
        System.out.println(totalCount);
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

        if (crews == null || crews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("더이상 조회할 크루가 없습니다.");
        } else {
            CrewListWithCount crewListWithCount = new CrewListWithCount(count, crews);
            return ResponseEntity.ok(crewListWithCount);
        }
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
    public ModelAndView registerCrew(CrewDTO crewDTO, ModelAndView mv) {
        System.out.println(crewDTO.getPermissionToJoin());
        System.out.println(crewDTO.getRecruitingStatus());
        int result = crewService.registerCrew(crewDTO);
        if(result == 0){
            mv.addObject("message", "크루 등록중에 문제가 발생했습니다.");
            mv.setViewName("/");
        }else{
            mv.setViewName("redirect:/crew/crewlist");
        }
        return mv;
    }


}
