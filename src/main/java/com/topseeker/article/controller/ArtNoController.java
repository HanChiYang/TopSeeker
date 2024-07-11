package com.topseeker.article.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	


	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(@RequestParam("artNo") String artNo, ModelMap model) {
	    ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(artNo));
	    
	    if (articleVO == null) {
	        model.addAttribute("errorMessage", "查無資料");
	        return "back-end/article/select_page";
	    }

	    model.addAttribute("articleVO", articleVO);
	    model.addAttribute("artcommentVO", new ArtCommentVO());
	    List<ArticleVO> list = articleSvc.getAll();
	    model.addAttribute("articleListData", list);
	    model.addAttribute("memberVO", new MemberVO());
	    List<MemberVO> list2 = memberSvc.getAll();
	    model.addAttribute("memberListData", list2);
	    List<ArtPicVO> list3 = artpicSvc.getAll();
	    model.addAttribute("artpicListData", list3);

	    return "back-end/article/listOneArticle";
	}

	@PostMapping("/artcomment/addComment")
	public String addComment(@ModelAttribute ArtCommentVO artcommentVO, @RequestParam("artNo") ArticleVO artNo, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "back-end/article/listOneArticle";
	    }
	    // 設置 artNo
	    artcommentVO.setArticleVO(artNo);

	    artcommentSvc.addArtComment(artcommentVO);
	    return "redirect:/article/getOne_For_Display";
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<ArticleVO> list = articleSvc.getAll();
		model.addAttribute("articleListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/article/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
	
	

}
