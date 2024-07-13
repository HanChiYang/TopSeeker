package com.topseeker.act.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.actpicture.model.ActPictureService;
import com.topseeker.actpicture.model.ActPictureVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.news.model.NewsVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;




@Controller
@RequestMapping("/act")
public class ActController {

	@Autowired
	ActService actSvc;
	
	@Autowired
	ActPictureService actPictureSvc;

	@Autowired
	MemberService memberSvc;
	
	@Autowired
    private SessionFactory sessionFactory;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addAct")
	public String addAct(ModelMap model) {
		ActVO actVO = new ActVO();
		actVO.setActCurrentCount(0);
		actVO.setActCheckCount(0);
		actVO.setActStatus(0);
		model.addAttribute("actVO", actVO);
		return "front-end/act/addAct";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ActVO actVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(actVO, result, "actPic");

		// 設置預設值	    
        actVO.setActCurrentCount(0);   
        actVO.setActStatus(0);     
        actVO.setActCheckCount(0);
	    
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			
			model.addAttribute("errorMessage", "照片: 請上傳照片");
			
		} else {
			List<ActPictureVO> picSet = new ArrayList<>();
			
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();			
				ActPictureVO actPictureVO = new ActPictureVO();
				actPictureVO.setActPic(buf);
				actPictureVO.setActVO(actVO);				
				picSet.add(actPictureVO);								
			}
			
			actVO.setActPictures(picSet);
			
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
//			System.out.println(result);
			return "front-end/act/addAct";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.addAct(actVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/act/listAllAct"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("updateActByMem")
	public String getOne_For_Update(@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("actVO", actVO);
		return "front-end/act/updateActByMem"; // 查詢完成後轉交update_emp_input.html
	}
	
	@PostMapping("UpdateActByEmp")
	public String UpdateByEmp(@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("actVO", actVO);
		return "front-end/act/updateActByEmp"; // 查詢完成後轉交updateActByEmp.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ActVO actVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(actVO, result, "actPic");

		
		    List<ActPictureVO> picSet = new ArrayList<>();
		    
		    for (MultipartFile multipartFile : parts) {
		        byte[] buf = multipartFile.getBytes();
		        
		        ActPictureVO actPictureVO = new ActPictureVO();
		        actPictureVO.setActPic(buf);
		        actPictureVO.setActVO(actVO); // 設置關聯到活動
		        
		        picSet.add(actPictureVO); // 添加到集合中
		    }
		    actVO.setActPictures(picSet); // 設置活動的圖片集合
		
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "front-end/act/update_act_input";
//		}
		if (result.hasErrors()) {
			return "front-end/act/updateActByMem";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.updateAct(actVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		actVO = actSvc.getOneAct(Integer.valueOf(actVO.getActNo()));
		model.addAttribute("actVO", actVO);
		return "front-end/act/updateOneAct"; // 修改成功後轉交listOneEmp.html
	}
	@PostMapping("updateByEmp")
	public String updateByEmp(@Valid ActVO actVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(actVO, result, "actPic");
//		
//			List<ActPictureVO> picSet = new ArrayList<>();
//			
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				
//				ActPictureVO actPictureVO = new ActPictureVO();
//				actPictureVO.setActPic(buf);
//				actPictureVO.setActVO(actVO); // 設置關聯到活動
//				
//				picSet.add(actPictureVO); // 添加到集合中
//			}
//			actVO.setActPictures(picSet); // 設置活動的圖片集合
//		
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/act/updateActByEmp";
//		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.updateAct(actVO);
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		actVO = actSvc.getOneAct(Integer.valueOf(actVO.getActNo()));
		model.addAttribute("actVO", actVO);
		return "front-end/act/updateOneAct"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.deleteAct(Integer.valueOf(actNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/act/listAllAct"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ActVO actVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(actVO, "actVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
//	@PostMapping("listActs_ByCompositeQuery")
//	public String listAllAct(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<ActVO> list = actSvc.getAll(map);
//		model.addAttribute("actListData", list); // for listAllEmp.html 第85行用	
//		
//		return "back-end/act/listAllAct";
//	}
	@PostMapping("listActs_ByCompositeQuery")
	public String listAllAct(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);

        // 處理日期區間查詢條件
        String actStart = req.getParameter("actStart");
        String actEnd = req.getParameter("actEnd");

        if (actStart != null && !actStart.trim().isEmpty() && actEnd != null && !actEnd.trim().isEmpty()) {
            queryParams.put("actDateRange", new String[]{actStart + "," + actEnd});
        }

        Session session = sessionFactory.openSession();
        List<ActVO> list = HibernateUtil_CompositeQuery_Act.getAllC(queryParams, session);
        model.addAttribute("actListData", list);
        return "front-end/act/listAllAct";
    }

	//copy from IndexController
	@GetMapping("/ActSelect")
	public String ActSelect(Model model) {
		return "front-end/act/ActSelect";
	}
	
	@GetMapping("/select_page")
	public String select_page(Model model) {
		return "front-end/act/select_page";
	}
    
    @GetMapping("/listAllAct")
	public String listAllAct(Model model) {
		return "front-end/act/listAllAct";
	}
    
    @GetMapping("/memMyAct")
	public String memMyAct(Model model) {
		return "front-end/act/memMyAct";
	}
    
    @GetMapping("/actBackEnd")
    public String actBackEnd(Model model) {
    	return "back-end/act/actBackEnd";
    }
    
    @GetMapping("/memMyActAct")
    public String memMyActAct(Model model) {
    	return "front-end/act/memMyAct";
    }
    
    @ModelAttribute("actListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ActVO> referenceListData(Model model) {
		
    	List<ActVO> list = actSvc.getAll();
		return list;
	}
    
    @ModelAttribute("memActListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
    protected List<ActVO> memActListData(Model model) {
    	model.addAttribute("actVO", new ActVO()); // for select_page.html 第133行用
		List<ActVO> list = actSvc.getAll();
		return list;
    	
    }
 
    
	@ModelAttribute("memberListData") // for select_page.html 第135行用
	protected List<MemberVO> referenceListData_Member(Model model) {
		model.addAttribute("memberVO", new MemberVO()); // for select_page.html 第133行用
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	

	@PostMapping("ajaxSearch")
	public String ajaxSearch(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        // 處理日期區間查詢條件
        String actStart = req.getParameter("actStart");
        String actEnd = req.getParameter("actEnd");

        if (actStart != null && !actStart.trim().isEmpty() && actEnd != null && !actEnd.trim().isEmpty()) {
            queryParams.put("actDateRange", new String[]{actStart + "," + actEnd});
        }

        Session session = sessionFactory.openSession();
        List<ActVO> list = HibernateUtil_CompositeQuery_Act.getAllC(queryParams, session);
        model.addAttribute("actListData", list);
        return "front-end/act/listAllActFragment :: resultsList";
	}
//	@PostMapping("ajaxSearch")
//	public String ajaxSearch(HttpServletRequest req, Model model, HttpSession session) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<ActVO> list = actSvc.getAll(map);
//		model.addAttribute("actListData", list);
//		return "back-end/act/listAllActFragment :: resultsList";
//	}
	//活動列表頁面自動載入全部活動
	@GetMapping("ajaxSearchAll")
    public String getAllAct(HttpServletRequest req,Model model) {
//        List<ActVO> actList = actSvc.getAll();
		Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        Session session = sessionFactory.openSession();
        List<ActVO> list = HibernateUtil_CompositeQuery_Act.getAllC(queryParams, session);
        model.addAttribute("actListData", list);
        return "front-end/act/listAllActFragment :: resultsList";
    }
	
//	//活動列表單張圖
//    @GetMapping("/listAllActFragment")
//	public String listAllActFragment(ModelMap model) {
//    	return "front-end/act/listAllActFragment";
//    }
	
  

}