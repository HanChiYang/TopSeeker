package com.topseeker.artreport.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.artreport.model.ArtReportService;
import com.topseeker.artreport.model.ArtReportVO;
import com.topseeker.article.model.ArticleService;
import com.topseeker.article.model.ArticleVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.employee.model.EmployeeService;
import com.topseeker.employee.model.EmployeeVO;


@Controller
@Validated
@RequestMapping("/artreport")
public class ArtReportController {
	
	
	@Autowired
	ArtReportService artreportSvc;
	
	@Autowired
	EmployeeService employeeSvc;
	
	
	@Autowired
	ArticleService articleSvc;
	

	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;
	
	
	
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addArtReport")
	public String addArtReport(ModelMap model) {
		ArtReportVO artreportVO = new ArtReportVO();
		model.addAttribute("artreportVO", artreportVO);
		return "back-end/artreport/addArtReport";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ArtReportVO artreportVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(artreportVO, result, "upFiles");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				participantVO.setUpFiles(buf);
//			}
//		}
		
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artreportSvc.addArtReport(artreportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ArtReportVO> list = artreportSvc.getAll();
		model.addAttribute("artreportListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/artreport/listAllArtReport"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("artReportNo") String artReportNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ArtReportVO artreportVO = artreportSvc.getOneArtReport(Integer.valueOf(artReportNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("artreportVO", artreportVO);
		return "back-end/artreport/update_ArtReport_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ArtReportVO artreportVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(artreportVO, result, "upFiles");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] upFiles = participantSvc.getOneParticipant(participantVO.getActPartNo()).getUpFiles();
//			participantVO.setUpFiles(upFiles);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				participantVO.setUpFiles(upFiles);
//			}
//		}
		if (result.hasErrors()) {
			return "back-end/report/update_ArtReport_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artreportSvc.updateArtReport(artreportVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		artreportVO = artreportSvc.getOneArtReport(Integer.valueOf(artreportVO.getArtReportNo()));
		model.addAttribute("artreportVO", artreportVO);
		return "back-end/artreport/listOneArtReport"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("artReportNo") String artReportNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		artreportSvc.deleteArtReport(Integer.valueOf(artReportNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ArtReportVO> list = artreportSvc.getAll();
		model.addAttribute("artreportListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/artreport/listAllArtReport"; // 刪除完成後轉交listAllEmp.html
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
	
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData2() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	@ModelAttribute("employeeListData")
	protected List<EmployeeVO> referenceListData3() {
		// DeptService deptSvc = new DeptService();
		List<EmployeeVO> list = employeeSvc.getAll();
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
	public BindingResult removeFieldError(ArtReportVO artreportVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(artreportVO, "artreportVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listMessages_ByCompositeQuery")
	public String listAllArticle(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ArtReportVO> list = artreportSvc.getAll(map);
		model.addAttribute("artreportListData", list); // for listAllEmp.html 第85行用
		return "back-end/artreport/listAllArtReport";
	}


}
