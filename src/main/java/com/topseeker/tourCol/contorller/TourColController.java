package com.topseeker.tourCol.contorller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	@GetMapping("addTourCol")
	public String addTourCol(ModelMap model) {
		TourColVO tourColVO = new TourColVO();
		model.addAttribute("tourColVO", tourColVO);
		return "back-end/tourCol/addTourCol";
	}

	@PostMapping("insert")
	public String insert(@Valid TourColVO tourColVO, BindingResult result, ModelMap model,
			@RequestParam("tourNo") Integer tourNo,@RequestParam("memNo") Integer memNo) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
	   
		/*************************** 2.開始新增資料 *****************************************/
		// tourColService tourColSvc = new tourColService();
		MemberVO memVo = memberSvc.getOneMem(memNo);
		tourColVO.setMemberVO(memVo);
		TourVO tourVo = tourSvc.getOneTour(tourNo);
		tourColVO.setTourVO(tourVo);
		tourColSvc.addTourCol(tourColVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TourColVO> list = tourColSvc.getAll();
		model.addAttribute("tourColListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/tourCol/listAllTourCol"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/tourCol/listAlltourCol")
	}

	
	@PostMapping("delete")
	public String delete(@RequestParam("colNo") Integer colNo, 
             ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// tourColService tourColSvc = new tourColService();

	    tourColSvc.deleteCol(colNo); // <-- 修改：使用 ColNo 刪除對應的 TourColVO

		
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<TourColVO> list = tourColSvc.getAll();
		model.addAttribute("tourColListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/tourCol/listAllTourCol"; // 刪除完成後轉交listAlltourCol.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="tourNo" id="tourNo" items="${tourListData}" itemValue="tourNo" itemLabel="dname" />
	 */
	@ModelAttribute("tourListData")
	protected List<TourVO> referenceListData_Tour() {
		// tourService tourSvc = new tourService();
		List<TourVO> list = tourSvc.getAll();
		return list;
	}
	
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData_Member() {
		// tourService tourSvc = new tourService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="tourNo" id="tourNo" items="${depMapData}" />
	 */
//	@ModelAttribute("tourMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
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
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
//	
//	@PostMapping("listTourCols_ByCompositeQuery")
//	public String listAllTourCol(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<TourColVO> list = tourColSvc.getAll(map);
//		model.addAttribute("tourColListData", list); // for listAlltourCol.html 第85行用
//		return "back-end/tourCol/listAllTourCol";
//	}

}