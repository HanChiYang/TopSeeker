package com.topseeker.employee.controller;

import java.io.IOException;
import java.util.List;
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

import com.topseeker.authority.model.AuthorityRepository;
import com.topseeker.authority.model.AuthorityVO;
import com.topseeker.empauth.model.EmpAuthService;
import com.topseeker.empauth.model.EmpAuthVO;
import com.topseeker.employee.model.EmployeeService;
import com.topseeker.employee.model.EmployeeVO;

@Controller
@RequestMapping("/emp")
public class EmployeeController {

	@Autowired
	EmployeeService empSvc;

	@Autowired
	EmpAuthService empAuthSvc;


    @Autowired
    AuthorityRepository authorityRepository;

	//==================此處為新增處==========================
    @GetMapping("addEmpPage")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employeeVO", new EmployeeVO()); //新增一個員工VO
        model.addAttribute("authorities", authorityRepository.findAll()); //將所有權限查出，用在頁面上
        return "back-end/employee/addEmpAndAuth"; //轉到新增頁面
    }
    @PostMapping("addEmpTest")
    public String addEmployee(@ModelAttribute EmployeeVO employeeVO, @RequestParam List<Integer> authNoList) {
    	//接收到employee所有相關的參數後回傳emplyeeVO，另外接收到的權限list(名稱需對應前端的name)，多選會回傳list
    	empSvc.addEmployeeWithAuthorities(employeeVO, authNoList); //給自訂方法去新增emplyeeVO以及新增於員工權限表格
        return "back-end/employee/select_page"; //提交表單後，轉到select_page
    }
    
    //以下修改邏輯與新增相似
    @PostMapping("edEmp")
    public String showEditEmployeeForm(@RequestParam Integer empNo, Model model) {
		EmployeeVO empVO = empSvc.getOneEmp(Integer.valueOf(empNo));
		model.addAttribute("employeeVO", empVO);
        model.addAttribute("authorities", authorityRepository.findAll());
        return "back-end/employee/updateEmpAndAuth";
    }

    @PostMapping("editEmp")
    public String editEmployee(@ModelAttribute EmployeeVO employeeVO, @RequestParam List<Integer> authNoList) {
        empSvc.updateEmployeeWithAuthorities(employeeVO, authNoList);
        return "back-end/employee/select_page";
    }
    
	//==================以上為新增處==========================

    
    
    @GetMapping("select_page")
    public String loginMem(Model model) {
    	return "back-end/employee/select_page";
    }
    
//    @ModelAttribute("empListData")
//	protected List<EmployeeVO> referenceListData(Model model) {
//    	model.addAttribute("EmployeeVO", new EmployeeVO());
//    	List<EmployeeVO> list = empSvc.getAll();
//		return list;
//	}
    
	@GetMapping("addEmp")
	public String addEmp(ModelMap model) {
		EmployeeVO empVO = new EmployeeVO();
		model.addAttribute("empVO", empVO);
		return "back-end/employee/addEmp";
	}
	


	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid EmployeeVO empVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(empVO, result, "upFiles");

		if (result.hasErrors()) {
			return "back-end/employee/addEmpAndAuth";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		empSvc.addEmp(empVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<EmployeeVO> list = empSvc.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/employee/listAllEmp"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("empNo") String empNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		EmployeeVO empVO = empSvc.getOneEmp(Integer.valueOf(empNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("empVO", empVO);
		return "back-end/employee/updateEmpAndAuth"; // 查詢完成後轉交update_emp_input.html
	}

	@PostMapping("setAuthPage")
	public String setAuthPage(@RequestParam("empNo") String empNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		EmployeeVO empVO = empSvc.getOneEmp(Integer.valueOf(empNo));
		
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("employeeVO", empVO);
		return "back-end/employee/setAuthPage"; // 查詢完成後轉交update_emp_input.html
	}
	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid EmployeeVO empVO, @RequestParam("authNo") List<String> authNoList,
						BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(empVO, result, "upFiles");


		if (result.hasErrors()) {
			return "back-end/employee/setAuthPage";
		}
		/*************************** 2.開始修改資料 *****************************************/
		empVO.setEmpStatus((byte)1); //預設給值
		
		empSvc.updateEmp(empVO);//其他emp資料新增
		
		empAuthSvc.deleteEmpAuth(empVO.getEmpNo()); 
		//先刪除員工權限表格內該員工資料

		for (String authNo : authNoList) { 
			empAuthSvc.addEmpAuth(empVO.getEmpNo(),authNo);	
			//由於多選項的參數回傳為list，用for-each將值新增於表中		
		}
			
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		empVO = empSvc.getOneEmp(Integer.valueOf(empVO.getEmpNo()));
		List<EmpAuthVO> empAuthList = empAuthSvc.takeOneEmpAuth(empVO.getEmpNo());
		
		model.addAttribute("empVO", empVO);
		//給listOneEmp顯示用
		model.addAttribute("empAuthList", empAuthList); 
		//給listOneEmp顯示用
		return "back-end/employee/listOneEmp"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("empNo") String empNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		empSvc.deleteEmp(Integer.valueOf(empNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<EmployeeVO> list = empSvc.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/employee/listAllEmp"; // 刪除完成後轉交listAllEmp.html
	}

//	/*
//	 * 第一種作法 Method used to populate the List Data in view. 如 : 
//	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
//	 */
//	@ModelAttribute("deptListData")
//	protected List<DeptVO> referenceListData() {
//		// DeptService deptSvc = new DeptService();
//		List<DeptVO> list = deptSvc.getAll();
//		return list;
//	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("deptMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(EmployeeVO empVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(empVO, "empVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listEmps_ByCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<EmployeeVO> list = empSvc.getAll(map);
		List<EmployeeVO> list = empSvc.getAll();
		model.addAttribute("empListData", list); // for listAllEmp.html 第85行用
		return "back-end/employee/listAllEmp";
	}

}