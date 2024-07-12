package com.topseeker.artpic.controller;

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

import com.topseeker.article.model.ArticleService;
import com.topseeker.article.model.ArticleVO;
import com.topseeker.artpic.model.ArtPicService;
import com.topseeker.artpic.model.ArtPicVO;



@Controller
@RequestMapping("/artpic")
public class ArtPicController {
	

	@Autowired
	ArtPicService artpicSvc;

	@Autowired
	ArticleService articleSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addArtPic")
	public String addEmp(ModelMap model) {
		ArtPicVO artpicVO = new ArtPicVO();
		model.addAttribute("artpicVO", artpicVO);
		return "front-end/artpic/addArtPic";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ArtPicVO artpicVO, BindingResult result, ModelMap model,
			@RequestParam("artPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(artpicVO, result, "artPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "論壇用圖片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				artpicVO.setArtPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "front-end/artpic/addArtPic";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artpicSvc.addArtPic(artpicVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ArtPicVO> list = artpicSvc.getAll();
		model.addAttribute("artpicListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/artpic/listAllArtPic"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("artPicNo") String artPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ArtPicVO artpicVO = artpicSvc.getOneArtPic(Integer.valueOf(artPicNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("artpicVO", artpicVO);
		return "front-end/artpic/update_ArtPic_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ArtPicVO artpicVO, BindingResult result, ModelMap model,
			@RequestParam("artPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(artpicVO, result, "artPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] artPic = artpicSvc.getOneArtPic(artpicVO.getArtPicNo()).getArtPic();
			artpicVO.setArtPic(artPic);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] ArtPic = multipartFile.getBytes();
				artpicVO.setArtPic(ArtPic);
			}
		}
		if (result.hasErrors()) {
			return "front-end/artpic/update_ArtPic_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artpicSvc.updateArtPic(artpicVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		artpicVO = artpicSvc.getOneArtPic(Integer.valueOf(artpicVO.getArtPicNo()));
		model.addAttribute("artpicVO", artpicVO);
		return "front-end/artpic/listOneArtPic"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("artPicNo") String artPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artpicSvc.deleteArtPic(Integer.valueOf(artPicNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ArtPicVO> list = artpicSvc.getAll();
		model.addAttribute("artpicListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/artpic/listAllArtPic"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("articleListData")
	protected List<ArticleVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ArticleVO> list = articleSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("articleMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "南三段水源通報");
		map.put(2, "雪霸6月起開放雪山登山口到七卡山莊單日健行");
		map.put(3, "桃園虎頭山公園健行7.5公里o型路線分享");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ArtPicVO artpicVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(artpicVO, "artpicVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listArtPics_ByCompositeQuery")
	public String listAllArtPic(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ArtPicVO> list = artpicSvc.getAll(map);
		model.addAttribute("artpicListData", list); // for listAllEmp.html 第85行用
		return "front-end/artpic/listAllArtPic";
	}


}
