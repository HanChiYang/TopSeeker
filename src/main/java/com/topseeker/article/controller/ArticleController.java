package com.topseeker.article.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.article.model.ArticleVO;
import com.topseeker.artpic.model.ArtPicService;
import com.topseeker.artpic.model.ArtPicVO;
import com.topseeker.artreport.model.ArtReportService;
import com.topseeker.artreport.model.ArtReportVO;
import com.topseeker.artcomment.model.ArtCommentVO;
import com.topseeker.article.model.ArticleService;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

@Controller
@Validated
@RequestMapping("protected/article")
public class ArticleController {

    @Autowired
    ArticleService articleSvc;

    @Autowired
    MemberService memberSvc;

    @Autowired
    ArtPicService artpicSvc;
    
    
    @Autowired
    ArtReportService artreportSvc;
    
    
    @ModelAttribute("articleListData")
    protected List<ArticleVO> referenceListData() {
        List<ArticleVO> list = articleSvc.getAll();
        return list;
    }

    @ModelAttribute("memberListData")
    protected List<MemberVO> referenceListData2() {
        List<MemberVO> list = memberSvc.getAll();
        return list;
    }

    @ModelAttribute("artPicListData")
    protected List<ArtPicVO> referenceListData3() {
        List<ArtPicVO> list = artpicSvc.getAll();
        return list;
    }

    
    //新增所需要的mapping
    @GetMapping("addArticle")
    public String addArticle(ModelMap model) {
        ArticleVO articleVO = new ArticleVO();
        model.addAttribute("articleVO", articleVO);
        return "front-end/article/addArticle";
    }

    @PostMapping("insert")
    public String insert(@Valid ArticleVO articleVO, BindingResult result, ModelMap model, @RequestParam("artPics") MultipartFile[] artPics, HttpSession session) throws IOException {
        result = removeFieldError(articleVO, result, "artPic");
        
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        if (loggedInMember != null) {
            articleVO.setMemberVO(loggedInMember);
        } else {
            model.addAttribute("errorMessage", "需要先登入才能新增文章");
            return "front-end/member/loginMem";
        }

        articleSvc.addArticle(articleVO);
        
        Set<ArtPicVO> artPicVOs = new HashSet<>();
        for (MultipartFile file : artPics) {
            if (!file.isEmpty()) {
                ArtPicVO artPicVO = new ArtPicVO();
                artPicVO.setArticleVO(articleVO);
                artPicVO.setArtPic(file.getBytes());
                artpicSvc.addArtPic(artPicVO);
                artPicVOs.add(artPicVO);
            }
        }
        articleVO.setArtPics(artPicVOs);
        articleSvc.updateArticle(articleVO);
        
        
        List<ArticleVO> list = articleSvc.getAll();
        model.addAttribute("articleListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/article/listAllArticle";
    }
    
    //新增留言所需要的mapping
    @GetMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("artNo") String artNo, ModelMap model) {
        ArticleVO articleVO = articleSvc.getOneArticle(Integer.valueOf(artNo));
        model.addAttribute("articleVO", articleVO);
        model.addAttribute("artcommentVO", new ArtCommentVO()); 
        return "front-end/article/update_Article_input";
    }

    @PostMapping("update")
    public String update(@Valid ArticleVO articleVO, BindingResult result, ModelMap model, MultipartFile[] artPics , HttpSession session) throws IOException {
        result = removeFieldError(articleVO, result, "artPic");

        if (result.hasErrors()) {
            model.addAttribute("artcommentVO", new ArtCommentVO()); 
            return "front-end/article/update_Article_input";
        }
        
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        if (loggedInMember != null) {
            articleVO.setMemberVO(loggedInMember);
        } else {
            model.addAttribute("errorMessage", "需要先登入才能修改文章");
            return "front-end/member/loginMem";
        }
        
            
//        Set<ArtPicVO> artPicVOs = new HashSet<>();
//        for (MultipartFile file : artPics) {
//            if (!file.isEmpty()) {
//                ArtPicVO artPicVO = new ArtPicVO();
//                artPicVO.setArticleVO(articleVO);
//                artPicVO.setArtPic(file.getBytes());
//                artpicSvc.addArtPic(artPicVO);
//                artPicVOs.add(artPicVO);
//            }
//        }
//        articleVO.setArtPics(artPicVOs);
        articleSvc.updateArticle(articleVO);
        
        
        

        model.addAttribute("success", "- (修改成功)");
        articleVO = articleSvc.getOneArticle(Integer.valueOf(articleVO.getArtNo()));
        model.addAttribute("articleVO", articleVO);
        model.addAttribute("artcommentVO", new ArtCommentVO()); // 确保 artcommentVO 被传递
        return "front-end/article/listOneArticle";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("artNo") String artNo, ModelMap model) {
        articleSvc.deleteArticle(Integer.valueOf(artNo));
        List<ArticleVO> list = articleSvc.getAll();
        model.addAttribute("articleListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "front-end/article/listAllArticle";
    }



   

    public BindingResult removeFieldError(ArticleVO articleVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(articleVO, "articleVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    

    @GetMapping("/article/{artNo}")
    public String getArticle(@PathVariable Integer artNo, Model model) {
        ArticleVO article = articleSvc.findById(artNo);
        model.addAttribute("articleVO", article);
        model.addAttribute("artcommentVO", new ArtCommentVO()); 
        return "listOneArticle";
    }
    
    
    //轉道自己所發的文章的mapping
    @GetMapping("listMyArticles")
    public String listMyArticles(HttpSession session, Model model) {
    	 MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
         if (loggedInMember == null) {
             return "redirect:/front-end/member/loginMem";
         }

         List<ArticleVO> myArticles = articleSvc.getAllIncludingStatusZero().stream()
                 .filter(article -> article.getMemberVO().getMemNo().equals(loggedInMember.getMemNo()))
                 .collect(Collectors.toList());

         model.addAttribute("myArticles", myArticles);
         return "front-end/article/listMyArticles";
    }
    
    
    //傳遞檢舉文章後可以更改status狀態 讓被檢舉的文章無法顯示
    @PostMapping("/artreport/update")
    public String updateArtReport(@Valid ArtReportVO artReportVO, BindingResult result, Model model) {
        // 更新檢舉狀態邏輯
        if (result.hasErrors()) {
            model.addAttribute("artReportVO", artReportVO);
            return "front-end/artreport/update_ArtReport_input";
        }

        // 檢查檢舉狀態是否為檢舉屬實，如果是，則將對應文章狀態設為隱藏（0）
        if (artReportVO.getArtReportStatus() == 1) {
            ArticleVO articleVO = artReportVO.getArticleVO();
            articleVO.setArtStatus(0);
            articleSvc.updateArticle(articleVO);
        }

        // 保存檢舉狀態更新
        artreportSvc.updateArtReport(artReportVO);

        return "redirect:/artreport/listAllArtReports";
    }
}
