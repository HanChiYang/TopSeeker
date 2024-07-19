package com.topseeker.tourGroup.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;

@Controller
@RequestMapping("/tourGroup")
public class TourGroupController {

	@Autowired
	TourGroupService tourGroupSvc;

	@Autowired
	TourService tourSvc;
	
	@Autowired
    private SessionFactory sessionFactory;

	/*
	 * This method will serve as addtourGroup.html handler.
	 */
	@GetMapping("addTourGroup")
	public String addTourGroup(ModelMap model) {
		TourGroupVO tourGroupVO = new TourGroupVO();
		model.addAttribute("tourGroupVO", tourGroupVO);
		return "back-end/tourGroup/addTourGroup";
	}

	/*
	 * This method will be called on addtourGroup.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid TourGroupVO tourGroupVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中tourPic欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(tourGroupVO, result, "tourPic");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "行程圖片: 請上傳圖片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				tourGroupVO.getTourVO().setTourPic(buf);
//			}
//		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/tourGroup/addTourGroup";
//		}
		/*************************** 2.開始新增資料 *****************************************/
//		 TourGroupService tourGroupSvc = new tourGroupService();
		tourGroupSvc.addTourGroup(tourGroupVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TourGroupVO> list = tourGroupSvc.getAll();
		model.addAttribute("tourGroupListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/tourGroup/listAllTourGroup"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/tourGroup/listAlltourGroup")
	}

	/*
	 * This method will be called on listAlltourGroup.html form submission, handling POST request
	 */
	
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("groupNo") String groupNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// tourGroupService tourGroupSvc = new tourGroupService();
		TourGroupVO tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(groupNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("tourGroupVO", tourGroupVO);
		return "back-end/tourGroup/update_tourGroup_input"; // 查詢完成後轉交update_tourGroup_input.html
	}

	/*
	 * This method will be called on update_tourGroup_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid TourGroupVO tourGroupVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中tourPic欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(tourGroupVO, result, "tourPic");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// tourGroupService tourGroupSvc = new tourGroupService();
//			byte[] tourPic = tourGroupSvc.getOneTourGroup(tourGroupVO.getgroupNo()).gettourPic();
//			tourVO.setTourPic(tourPic);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] tourPic = multipartFile.getBytes();
//				tourVO.setTourPic(tourPic);
//			}
//		}
//		if (result.hasErrors()) {
//			return "back-end/tourGroup/update_tourGroup_input";
//		}
		/*************************** 2.開始修改資料 *****************************************/
		// tourGroupService tourGroupSvc = new tourGroupService();
		tourGroupSvc.updateTourGroup(tourGroupVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(tourGroupVO.getGroupNo()));
		model.addAttribute("tourGroupVO", tourGroupVO);
		return "back-end/tourGroup/listOneTourGroup"; // 修改成功後轉交listOnetourGroup.html
	}

	/*
	 * This method will be called on listAlltourGroup.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("groupNo") String groupNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// tourGroupService tourGroupSvc = new tourGroupService();
		tourGroupSvc.deleteGroupNo(Integer.valueOf(groupNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<TourGroupVO> list = tourGroupSvc.getAll();
		model.addAttribute("tourGroupListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/tourGroup/listAllTourGroup"; // 刪除完成後轉交listAlltourGroup.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="tourno" id="tourno" items="${tourListData}" itemValue="tourno" itemLabel="dname" />
	 */
	@ModelAttribute("tourListData")
	protected List<TourVO> referenceListData() {
		// tourService tourSvc = new tourService();
		List<TourVO> list = tourSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="tourno" id="tourno" items="${depMapData}" />
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
	public BindingResult removeFieldError(TourGroupVO tourGroupVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(tourGroupVO, "tourGroupVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listTourGroups_ByCompositeQuery")
	public String listAllTourGroup(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourGroupVO> list = tourGroupSvc.getAll(map);
		model.addAttribute("tourGroupListData", list); // for listAlltourGroup.html 第85行用
		return "back-end/tourGroup/listAllTourGroup";
	}

	@PostMapping("listTourGroups_ByCompositeQuery_front")
	public String listAllTourGroupFront(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		
//        Map<String, String[]> queryParams = new HashMap<>(map);
//        String groupBegin = req.getParameter("groupBegin");
//        String groupEnd = req.getParameter("groupEnd");
//        if (groupBegin != null && !groupBegin.trim().isEmpty() && groupEnd != null && !groupEnd.trim().isEmpty()) {
//            queryParams.put("groupDateRange", new String[]{groupBegin + "," + groupEnd});
//        }
//        Session session = sessionFactory.openSession();
//        
		List<TourGroupVO> list = tourGroupSvc.getAll(map);
		model.addAttribute("tourGroupListData", list); // for listAlltourGroup.html 第85行用
		return "back-end/tourGroup/listAllTourGroup";
	}

}