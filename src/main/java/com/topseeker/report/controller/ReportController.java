package com.topseeker.report.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.employee.model.EmployeeService;
import com.topseeker.employee.model.EmployeeVO;
import com.topseeker.participant.model.ParticipantService;
import com.topseeker.report.model.ReportVO;
import com.topseeker.report.model.ReportService;

@Controller
@Validated
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	ReportService reportSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	ActService actSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	EmployeeService employeeSvc;
	
	
	
	@ModelAttribute("reportListData")
	protected List<ActVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ActVO> list = actSvc.getAll();
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
	
	
	@ModelAttribute("actListData")
	protected List<ActVO> referenceListData4() {
		// DeptService deptSvc = new DeptService();
		List<ActVO> list = actSvc.getAll();
		return list;
	}
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addReport")
	public String addReport(@RequestParam("actNo") Integer actNo, ModelMap model, HttpSession session) {
	    MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
	    if (loggedInMember == null) {
	        return "redirect:/member/loginMem";
	    }

	    ReportVO reportVO = new ReportVO();
	    ActVO actVO = actSvc.getOneAct(actNo);
	    reportVO.setActVO(actVO);
	    reportVO.setMemberVO(loggedInMember);

	    model.addAttribute("reportVO", reportVO);
	    model.addAttribute("actTitle", actVO.getActTitle());
	    model.addAttribute("memAccount", loggedInMember.getMemAccount());
	    return "front-end/report/addReport";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ReportVO reportVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(reportVO, result, "upFiles");
		/*************************** 2.開始新增資料 *****************************************/
		reportSvc.addReport(reportVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/act/listAllAct"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	 @PostMapping("getOne_For_Update")
	    public String getOne_For_Update(@RequestParam("actRpNo") Integer actRpNo, ModelMap model, HttpSession session) {
	        ReportVO reportVO = reportSvc.getOneReport(actRpNo);
	        reportVO.setMemberVO(memberSvc.getOneMem(reportVO.getMemberVO().getMemNo()));
	        reportVO.setActVO(actSvc.getOneAct(reportVO.getActVO().getActNo()));

	        EmployeeVO loggedInEmployee = (EmployeeVO) session.getAttribute("loggedInEmployee");
	        if (loggedInEmployee != null) {
	            reportVO.setEmployeeVO(loggedInEmployee); 
	        }

	        model.addAttribute("reportVO", reportVO);
	        return "back-end/report/update_Report_input";
	    }

	 @PostMapping("update")
	 public String update(@Valid ReportVO reportVO, BindingResult result, ModelMap model, HttpSession session) throws IOException {
	     result = removeFieldError(reportVO, result, "upFiles");

	     if (result.hasErrors()) {
	         model.addAttribute("reportVO", reportVO);
	         return "back-end/report/update_Report_input";
	     }

	     // 確保會員信息已存在
	     if (reportVO.getMemberVO() == null || reportVO.getMemberVO().getMemNo() == null) {
	         model.addAttribute("error", "會員信息缺失，無法更新檢舉信息");
	         return "back-end/report/update_Report_input";
	     } else {
	         reportVO.setMemberVO(memberSvc.getOneMem(reportVO.getMemberVO().getMemNo()));
	     }

	     if (reportVO.getEmployeeVO() != null && reportVO.getEmployeeVO().getEmpNo() != null) {
	         reportVO.setEmployeeVO(employeeSvc.getOneEmp(reportVO.getEmployeeVO().getEmpNo()));
	     }

	     if (reportVO.getHandleCheck() == 1) {
	         actSvc.updateActStatus(reportVO.getActVO().getActNo(), 3);
	     }

	     reportSvc.updateReport(reportVO);
	     model.addAttribute("success", "- (修改成功)");
	     List<ReportVO> list = reportSvc.getAll();
	     model.addAttribute("reportListData", list);
	     return "back-end/report/listAllReport";
	 }
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actRpNo") String actRpNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		reportSvc.deleteReport(Integer.valueOf(actRpNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ReportVO> list = reportSvc.getAll();
		model.addAttribute("reportListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/report/listAllReport"; // 刪除完成後轉交listAllEmp.html
	}


	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ReportVO reportVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(reportVO, "reportVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listParticipants_ByCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ReportVO> list = reportSvc.getAll(map);
		model.addAttribute("reportListData", list); // for listAllEmp.html 第85行用
		return "back-end/report/listAllReport";
	}
	
	@GetMapping("myAllReport")
    public String myAllReport(ModelMap model, HttpSession session) {
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return "redirect:/member/loginMem";
        }
        
        List<ReportVO> myReports = reportSvc.getReportsByMember(loggedInMember.getMemNo());
        model.addAttribute("reportListData", myReports);
        return "front-end/report/myAllReport";
    }


}
