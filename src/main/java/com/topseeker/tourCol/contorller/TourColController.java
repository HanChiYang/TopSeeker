package com.topseeker.tourCol.contorller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColService;
import com.topseeker.tourCol.model.TourColVO;

@Controller
@RequestMapping("/tourCol")
public class TourColController {

    @Autowired
    TourColService tourColSvc;

    @Autowired
    TourService tourSvc;

    @Autowired
    MemberService memberSvc;

    
 // 收藏頁面
 		@GetMapping("tourCol")
 		public String tourCol(ModelMap model, HttpSession session) {

 		    // 獲取登入者的會員編號
 			MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
 			if (loggedInMember == null) {
 				// 如果未登入，重定向到登入頁面
 				return "redirect:/member/loginMem";
 			}
 			Integer loggedInMemberNo = loggedInMember.getMemNo();
 			

 			// 獲取所有收藏
 			List<TourColVO> tourColListData = tourColSvc.findAllTourCols(loggedInMemberNo);
 			model.addAttribute("tourColListData", tourColListData);

 			return "front-end/tour/tourCol";

 		}
 		
 		
 		
 		
    
    
    



    @ResponseBody
    @PostMapping("/insert")
    public String insert(@RequestParam("tourNo") Integer tourNo, HttpSession session) {
    	// 獲取登入者的會員編號
			MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//			if (loggedInMember == null) {
//				// 如果未登入，重定向到登入頁面
//				return "redirect:/member/loginMem";
//			}
    	System.out.println("tourNO" + tourNo);
        Integer loggedInMemberNo = loggedInMember.getMemNo();

        // 確認是否已被收藏過
        if (tourColSvc.checkTourColVO(loggedInMemberNo, tourNo).isPresent()) {
            return "{\"message\":\"已收藏\"}";
        } else {
            // 若未收藏過，則新增於資料庫
            tourColSvc.addTourCol(loggedInMemberNo, tourNo);
            return "{\"message\":\"收藏成功\"}";
        }
    }

//    @PostMapping("/remove")
//    public String delete(@RequestParam("tourNo") Integer tourNo, ModelMap model, HttpSession session) {
//        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//        Integer loggedInMemberNo = loggedInMember.getMemNo();
//
//        tourColSvc.deleteTourCol(loggedInMemberNo, tourNo);
//
//        List<TourColVO> tourColListData = tourColSvc.findAllTourCols(loggedInMemberNo);
//			model.addAttribute("tourColListData", tourColListData);
////        model.addAttribute("success", "- (刪除成功)");
//        return "front-end/tour/tourCol";
//    }

 		
 	
    
    
    /*
	 * This method will be called on listAlltourCol.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(HttpSession session,@RequestParam("tourNo") Integer tourNo, 
             ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// tourColService tourColSvc = new tourColService();
		
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
			
			Integer loggedInMemberNo = loggedInMember.getMemNo();
	    tourColSvc.deleteCol(loggedInMemberNo,tourNo); // <-- 修改：使用 ColNo 刪除對應的 TourColVO
	    
		
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
	    List<TourColVO> tourColListData = tourColSvc.findAllTourCols(loggedInMemberNo);
		model.addAttribute("tourColListData", tourColListData);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/tour/tourCol"; // 刪除完成後轉交listAlltourCol.html
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
 		
//    @ModelAttribute("tourListData")
//    protected List<TourVO> referenceListData_Tour() {
//        return tourSvc.getAll();
//    }
//
//    @ModelAttribute("memberListData")
//    protected List<MemberVO> referenceListData_Member() {
//        return memberSvc.getAll();
//    }

    public BindingResult removeFieldError(TourColVO tourColVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(tourColVO, "tourColVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
}
