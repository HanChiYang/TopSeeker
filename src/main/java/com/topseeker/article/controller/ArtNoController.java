package com.topseeker.article.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.artcomment.model.ArtCommentService;
import com.topseeker.artcomment.model.ArtCommentVO;
import com.topseeker.article.model.ArticleService;
import com.topseeker.article.model.ArticleVO;
import com.topseeker.artpic.model.ArtPicService;
import com.topseeker.artpic.model.ArtPicVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

@Controller
@Validated
@RequestMapping("/article")
public class ArtNoController {
    
    @Autowired
    ArticleService articleSvc;
    
    @Autowired
    MemberService memberSvc;
    
    @Autowired
    ArtPicService artpicSvc;
    
    @Autowired
    ArtCommentService artcommentSvc;
    
    @GetMapping("getOne_For_Display")
    public String getOne_For_Display(@RequestParam("artNo") Integer artNo, ModelMap model, HttpServletRequest request) {
        ArticleVO articleVO = articleSvc.getOneArticle(artNo);
        
        if (articleVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "front-end/article/select_page";
        }
        
        HttpSession session = request.getSession();
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        
        model.addAttribute("articleVO", articleVO);
        model.addAttribute("artcommentVO", new ArtCommentVO());
        List<ArticleVO> list = articleSvc.getAll();
        model.addAttribute("articleListData", list);
        model.addAttribute("memberVO", new MemberVO());
        List<MemberVO> list2 = memberSvc.getAll();
        model.addAttribute("memberListData", list2);
        List<ArtPicVO> list3 = artpicSvc.getAll();
        model.addAttribute("artpicListData", list3);

        return "front-end/article/listOneArticle";
    }

    @GetMapping("/artcomment/addComment")
    public String addComment(@ModelAttribute ArtCommentVO artcommentVO, @RequestParam("artNo") Integer artNo, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "front-end/article/listOneArticle";
        }
        
        HttpSession session = request.getSession();
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

        if (loggedInMember != null) {
            artcommentVO.setMemberVO(loggedInMember);
        } else {
            model.addAttribute("errorMessage", "需要先登入才能留言");
            return "front-end/member/loginMem";
        }

        ArticleVO articleVO = articleSvc.getOneArticle(artNo);
        artcommentVO.setArticleVO(articleVO);

        artcommentSvc.addArtComment(artcommentVO);
        return "redirect:/article/getOne_For_Display?artNo=" + artNo;
    }
    
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
              strBuilder.append(violation.getMessage() + "<br>");
        }
        List<ArticleVO> list = articleSvc.getAll();
        model.addAttribute("articleListData", list);
        model.addAttribute("memberVO", new MemberVO());
        List<MemberVO> list2 = memberSvc.getAll();
        model.addAttribute("memberListData", list2);
        String message = strBuilder.toString();
        return new ModelAndView("front-end/article/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
