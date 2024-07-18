package com.topseeker.tour.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
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

import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourArea.model.TourAreaService;
import com.topseeker.tourArea.model.TourAreaVO;

@Controller
@RequestMapping("/tour")
public class TourController {

	@Autowired
	TourService tourSvc;

	@Autowired
	TourAreaService tourAreaSvc;

	@GetMapping("addTour")
	public String addTour(ModelMap model) {
		TourVO tourVO = new TourVO();
		model.addAttribute("tourVO", tourVO);
		return "back-end/tour/addTour";
	}

	@PostMapping("insert")
	public String insert(@Valid TourVO tourVO, BindingResult result, ModelMap model,
			@RequestParam("tourPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中tourPic欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(tourVO, result, "tourPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "行程圖片: 請上傳圖片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				tourVO.setTourPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/tour/addTour";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// TourService tourSvc = new TourService();
		tourSvc.addTour(tourVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TourVO> list = tourSvc.getAll();
		model.addAttribute("tourListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/tour/listAllTour"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/tour/listAllTour")
	}

	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("tourNo") String tourNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// TourService tourSvc = new TourService();
		TourVO tourVO = tourSvc.getOneTour(Integer.valueOf(tourNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("tourVO", tourVO);
		return "back-end/tour/update_tour_input"; // 查詢完成後轉交update_tour_input.html
	}

	/*
	 * This method will be called on update_tour_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid TourVO tourVO, BindingResult result, ModelMap model,
			@RequestParam("tourPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中tourPic欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(tourVO, result, "tourPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// TourService tourSvc = new TourService();
			byte[] tourPic = tourSvc.getOneTour(tourVO.getTourNo()).getTourPic();
			tourVO.setTourPic(tourPic);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] tourPic = multipartFile.getBytes();
				tourVO.setTourPic(tourPic);
			}
		}
		if (result.hasErrors()) {
			return "back-end/tour/update_tour_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// TourService tourSvc = new TourService();
		tourSvc.updateTour(tourVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		tourVO = tourSvc.getOneTour(Integer.valueOf(tourVO.getTourNo()));
		model.addAttribute("tourVO", tourVO);
		return "back-end/tour/listOneTour"; // 修改成功後轉交listOneTour.html
	}

	/*
	 * This method will be called on listAllTour.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("tourNo") String tourNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// TourService tourSvc = new TourService();
		tourSvc.deleteTour(Integer.valueOf(tourNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<TourVO> list = tourSvc.getAll();
		model.addAttribute("tourListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/tour/listAllTour"; // 刪除完成後轉交listAllTour.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="areaNo" id="areaNo" items="${tourAreaListData}" itemValue="areaNo" itemLabel="areaName" />
	 */
	@ModelAttribute("tourAreaListData")
	protected List<TourAreaVO> referenceListData() {
		// TourAreaService tourAreaSvc = new TourAreaService();
		List<TourAreaVO> list = tourAreaSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="areaNo" id="areaNo" items="${depMapData}" />
	 */
	@ModelAttribute("tourAreaMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "日本");
		map.put(2, "韓國");
		map.put(3, "台灣");
		map.put(4, "中國");
		map.put(5, "歐洲");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(TourVO tourVO, BindingResult result, String removedFielareaName) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fielareaName -> !fielareaName.getField().equals(removedFielareaName))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(tourVO, "tourVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listTours_ByCompositeQuery")
	public String listAllTour(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourVO> list = tourSvc.getAll(map);
		model.addAttribute("tourListData", list); // for listAllTour.html 第85行用
		return "back-end/tour/listAllTour";
	}
	
	@PostMapping("listTours_ByCompositeQuery_front")
	public String listAllTourFront(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourVO> list = tourSvc.getAll(map);
		model.addAttribute("tourListData", list); // for listAllTour.html 第85行用
		return "front-end/tour/tourQuery";
	}

}