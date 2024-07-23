package com.topseeker.tourDetail.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.topseeker.tourDetail.model.TourDetailService;
import com.topseeker.tourDetail.model.TourDetailVO;

@Controller
@RequestMapping("/tourDetail")
public class TourDetailController {

	@Autowired
	TourDetailService tourDetailSvc;

	@Autowired
	TourService tourSvc;

	/*
	 * This method will serve as addtourDetail.html handler.
	 */
	
	
	
	
	@GetMapping("addTourDetail")
	public String addTourDetail(ModelMap model) {
		TourDetailVO tourDetailVO = new TourDetailVO();
		model.addAttribute("tourDetailVO", tourDetailVO);
		return "back-end/tourDetail/addTourDetail";
	}

	/*
	 * This method will be called on addtourDetail.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid TourDetailVO tourDetailVO, BindingResult result, ModelMap model,
			@RequestParam("detailPic") MultipartFile[] parts,@RequestParam("tourNo") Integer tourNo
			,@RequestParam("detailDay") Integer detailDay) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中detailPic欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(tourDetailVO, result, "detailPic");
		result = removeFieldError(tourDetailVO, result, "tourVO");
		
		System.out.println("Tourno" + tourNo);
		
		// 檢查是否已經存在相同的 tourNo 和 detailDay
//        if (tourDetailSvc.existsByTourNoAndDetailDay(tourNo, detailDay)) {
//            model.addAttribute("errorMessage", "已經有相同的資料");
//            return "back-end/tourDetail/addTourDetail";  // 回到新增頁面
//        }

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "行程詳情照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				tourDetailVO.setDetailPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/tourDetail/addTourDetail";
		}
		
//		System.out.println("tourno:" + tourDetailVO.getTourVO().getTourNo());
//		TourDetailVO.TourDetailPK id = new TourDetailVO.TourDetailPK();
//	    id.setTourNo(tourDetailVO.getTourNo());
//	    id.setDetailDay(tourDetailVO.getDetailDay());
		// 假設這裡從 tourDetailVO 中獲取 tour_no 的值
//	    Integer tourNo = tourDetailVO.getTourNo();
//	    // 確保 tour_no 不為空，根據你的邏輯來確定如何獲取這個值
		TourVO tourVO=tourSvc.getOneTour(tourNo);
	    if (tourVO != null) {
	    	// 設置 tour_no 到 tourDetailVO 對象	中
	    	tourDetailVO.setTourVO(tourVO);
		}
	   
		/*************************** 2.開始新增資料 *****************************************/
		// tourDetailService tourDetailSvc = new tourDetailService();
		tourDetailSvc.addTourDetail(tourDetailVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TourDetailVO> list = tourDetailSvc.getAll();
		model.addAttribute("tourDetailListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/tourDetail/listAllTourDetail"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/tourDetail/listAlltourDetail")
	}

	
	
	
	/*
	 * This method will be called on listAlltourDetail.html form submission, handling POST request
	 */
	
	
	
	

	
//	 @PostMapping("add123")
//	    public ResponseEntity<?> add123(@RequestParam("tourNo") String tourNo) {
//	  
//	  Integer tourNo = Integer.valueOf(tourNo);
//	  if ( tourSvc.addTour(tourNo) ) {
//	   return ResponseEntity.status(HttpStatus.OK).body("success");
//	  } else {
//	   return ResponseEntity.status(HttpStatus.OK).body("已有相同資料");
//	  }
//	 }
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("detailNo") Integer detailNo, 
           ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// tourDetailService tourDetailSvc = new tourDetailService();
		
//		TourDetailVO.TourDetailPK id = new TourDetailVO.TourDetailPK();
//		id.setTourNo(Integer.valueOf(tourNo));
//		id.setDetailDay(Integer.valueOf(detailDay));
////

	    TourDetailVO tourDetailVO = tourDetailSvc.getByDetailNo(detailNo); // <-- 修改：使用 detailNo 查詢 TourDetailVO
	    

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("tourDetailVO", tourDetailVO);
		return "back-end/tourDetail/update_tourDetail_input"; // 查詢完成後轉交update_tourDetail_input.html
	}

	/*
	 * This method will be called on update_tourDetail_input.html form submission, handling POST request It also validates the user input
	 */
//	@PostMapping("update")
//	public String update(@RequestParam("detailNo") Integer detailNo, @Valid TourDetailVO tourDetailVO, BindingResult result, ModelMap model,
//			@RequestParam("detailPic") MultipartFile[] parts) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		 // 去除BindingResult中detailPic欄位的FieldError紀錄 --> 見第172行
//	    result = removeFieldError(tourDetailVO, result, "detailPic");
//	    
//	    // 用 detailNo 查詢現有的 TourDetailVO
//	    TourDetailVO existingTourDetailVO = tourDetailSvc.getByDetailNo(detailNo); // <-- 修改：使用 detailNo 查詢 TourDetailVO
//
//	    if (existingTourDetailVO == null) {
//	        model.addAttribute("errorMessage", "查無資料");
//	        return "back-end/tourDetail/update_tourDetail_input";
//	    }
//		
//	    // 根據是否上傳新圖片來處理圖片
//	    if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//	        byte[] detailPic = existingTourDetailVO.getDetailPic(); // 使用原有的圖片
//	        tourDetailVO.setDetailPic(detailPic);
//	    } else {
//	        for (MultipartFile multipartFile : parts) {
//	            byte[] detailPic = multipartFile.getBytes();
//	            tourDetailVO.setDetailPic(detailPic);
//	        }
//	    }
//		if (result.hasErrors()) {
//			return "back-end/tourDetail/update_tourDetail_input";
//		}
//		/*************************** 2.開始修改資料 *****************************************/
//		// tourDetailService tourDetailSvc = new tourDetailService();
//		tourDetailSvc.updateTourDetail(tourDetailVO);
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "- (修改成功)");
//		tourDetailVO = tourDetailSvc.getByDetailNo(detailNo); // 確保最新的資料回傳到前端		model.addAttribute("tourDetailVO", tourDetailVO);
//		return "back-end/tourDetail/listOneTourDetail"; // 修改成功後轉交listOnetourDetail.html
//	}
	
	
	@PostMapping("update")
	public String update(@RequestParam("detailNo") Integer detailNo, @Valid TourDetailVO tourDetailVO,
	        BindingResult result, ModelMap model, @RequestParam("detailPic") MultipartFile[] parts,@RequestParam("tourNo") Integer tourNo) throws IOException {

	    // 去除 BindingResult 中 detailPic 欄位的 FieldError 紀錄
	    result = removeFieldError(tourDetailVO, result, "detailPic");
	    
	    // 用 detailNo 查詢現有的 TourDetailVO
	    TourDetailVO existingTourDetailVO = tourDetailSvc.getByDetailNo(detailNo); // <-- 修改：使用 detailNo 查詢 TourDetailVO

	    if (existingTourDetailVO == null) {
	        model.addAttribute("errorMessage", "查無資料");
	        return "back-end/tourDetail/update_tourDetail_input";
	    }

	    // 根據是否上傳新圖片來處理圖片
	    if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
	        byte[] detailPic = existingTourDetailVO.getDetailPic(); // 使用原有的圖片
	        tourDetailVO.setDetailPic(detailPic);
	    } else {
	        for (MultipartFile multipartFile : parts) {
	            byte[] detailPic = multipartFile.getBytes();
	            tourDetailVO.setDetailPic(detailPic);
	        }
	    }

	    if (result.hasErrors()) {
	        return "back-end/tourDetail/update_tourDetail_input";
	    }
	    
	    TourVO tourVO=tourSvc.getOneTour(tourNo);
	    if (tourVO != null) {
	    	// 設置 tour_no 到 tourDetailVO 對象	中
	    	tourDetailVO.setTourVO(tourVO);
		}
	    
	    
	    // 更新資料庫中的 TourDetailVO
	    tourDetailSvc.updateTourDetail(tourDetailVO);
	    
	    // 修改成功後轉交到詳細頁面
	    model.addAttribute("success", "- (修改成功)");
	    tourDetailVO = tourDetailSvc.getByDetailNo(detailNo); // 確保最新的資料回傳到前端
	    
//	    System.out.println(tourDetailVO.getTourVO().getTourNo()==null);
//	    System.out.println(tourDetailVO.getTourVO().getTourNo());
	    
	    model.addAttribute("tourDetailVO", tourDetailVO);
	    System.out.println("Tour name:" + tourDetailVO.getTourVO().getTourName());

	    return "back-end/tourDetail/listOneTourDetail";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * This method will be called on listAlltourDetail.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("detailNo") Integer detailNo, 
             ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// tourDetailService tourDetailSvc = new tourDetailService();

	    tourDetailSvc.deleteTourDetail(detailNo); // <-- 修改：使用 detailNo 刪除對應的 TourDetailVO

		
		//		TourDetailVO.TourDetailPK id = new TourDetailVO.TourDetailPK();
//		id.setTourNo(Integer.valueOf(tourNo));
//		id.setDetailDay(Integer.valueOf(detailDay));
//		tourDetailSvc.deleteTourDetail(id);
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<TourDetailVO> list = tourDetailSvc.getAll();
		model.addAttribute("tourDetailListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/tourDetail/listAllTourDetail"; // 刪除完成後轉交listAlltourDetail.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="tourNo" id="tourNo" items="${tourListData}" itemValue="tourNo" itemLabel="dname" />
	 */
	@ModelAttribute("tourListData")
	protected List<TourVO> referenceListData() {
		// tourService tourSvc = new tourService();
		List<TourVO> list = tourSvc.getAll();
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
	public BindingResult removeFieldError(TourDetailVO tourDetailVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(tourDetailVO, "tourDetailVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
//	@PostMapping("listTourDetails_ByCompositeQuery")
//	public String listAllTourDetail(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		
//		// 构建复合主键对象
//		Integer tourNo = null;
//	    Integer detailDay = null;
//	    TourDetailVO.TourDetailPK id = new TourDetailVO.TourDetailPK();
//	    if (map.containsKey("tourNo")) {
//	        id.setTourNo(Integer.parseInt(map.get("tourNo")[0]));
//	    }
//	    if (map.containsKey("detailDay")) {
//	        id.setDetailDay(Integer.parseInt(map.get("detailDay")[0]));
//	    }
//		
//	    List<TourDetailVO> list;
//	    if (tourNo != null && detailDay != null) {
//	        list = tourDetailSvc.getAllByCompositeKey(tourNo, detailDay);
//	    } else {
//	        list = tourDetailSvc.getAll();
//	    }
//	    
//		model.addAttribute("tourDetailListData", list); // for listAlltourDetail.html 第85行用
//		return "back-end/tourDetail/listAllTourDetail";
//	}
	@PostMapping("listTourDetails_ByCompositeQuery")
	public String listAllTourDetail(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourVO> list = tourSvc.getAll(map);
//		List<TourDetailVO> list = tourDetailSvc.getAll(map);
		model.addAttribute("tourDetailListData", list); // for listAllTour.html 第85行用
		return "back-end/tourDetail/listAllTourDetail";
	}
	

}