package com.topseeker.act.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.topseeker.act.controller.validation.MemGroup;
import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.actpicture.model.ActPictureService;
import com.topseeker.actpicture.model.ActPictureVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.notification.model.NotificationService;
import com.topseeker.notification.model.NotificationVO;
import com.topseeker.participant.model.ParticipantVO;
import com.topseeker.participant.model.ParticipantService;
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
	private ParticipantService participantSvc;
	
	@Autowired
	NotificationService notiSvc;
	
	@Autowired
    private SessionFactory sessionFactory;
	

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addAct")
	public String addAct(ModelMap model,HttpSession session) {
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
	public String insert(
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			
			@Validated(MemGroup.class)ActVO actVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts, HttpSession session) throws IOException {
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(actVO, result, "actPic");
		
	
		// 設置預設值	    
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

		actVO.setMemberVO(loggedInMember);
		actVO.setActCurrentCount(0);   
        actVO.setActStatus(0);     
        actVO.setActCheckCount(0);
        actVO.setActRateSum(0);
        actVO.setEvalSum(0);
	    
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
			System.out.println(result);
			return "front-end/act/addAct";
		}
		/*************************** 2.開始新增資料 *****************************************/

		actSvc.addAct(actVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		model.addAttribute("successMessage", "活動新增成功!");
		return "redirect:/act/memMyAct?success=true";
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
	
	@PostMapping("updateActByEmp")
	public String UpdateByEmp(@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("actVO", actVO);
		return "back-end/act/updateActByEmp"; // 查詢完成後轉交updateActByEmp.html
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

				if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
					model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
				} else {
				    List<ActPictureVO> picSet = new ArrayList<>();
				    
				    for (MultipartFile multipartFile : parts) {
				        byte[] buf = multipartFile.getBytes();
				        
				        ActPictureVO actPictureVO = new ActPictureVO();
				        actPictureVO.setActPic(buf);
				        actPictureVO.setActVO(actVO); // 設置關聯到活動
				        
				        picSet.add(actPictureVO); // 添加到集合中
				    }
				    actVO.setActPictures(picSet); // 設置活動圖片集合
				}
		/*************************** 2.開始修改資料 *****************************************/
		actSvc.updateAct(actVO);
		// 獲取所有參與該活動的會員
	    List<ParticipantVO> participants = participantSvc.findParticipantsByActNo(actVO.getActNo());
	    if (participants != null && !participants.isEmpty()) {
	        String notificationContent = "您報名的活動 \"" + actVO.getActTitle() + "\" 已被修改，請檢查最新活動資訊。";
	        for (ParticipantVO participant : participants) {
	            NotificationVO notification = new NotificationVO();
	            notification.setMemNo(participant.getMemberVO().getMemNo());
	            notification.setNotiContent(notificationContent);
	            notification.setNotiTime(new Timestamp(System.currentTimeMillis()));
	            notification.setNotiStatus((byte) 0);
	            notiSvc.addNoti(notification);
	        }
	    }

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		actVO = actSvc.getOneAct(Integer.valueOf(actVO.getActNo()));
		model.addAttribute("actVO", actVO);
		return "front-end/act/updateOneAct"; // 修改成功後轉交updateOneAct.html
	}
	@PostMapping("updateByEmp")
	public String updateByEmp(@Valid ActVO actVO, BindingResult result, ModelMap model
			) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.updateAct(actVO);
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		actVO = actSvc.getOneAct(Integer.valueOf(actVO.getActNo()));
		model.addAttribute("actVO", actVO);
		return "back-end/act/updateOneActByEmp"; // 修改成功後轉交updateOneActByEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@Valid ActVO actVO, BindingResult result,@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 從資料庫中檢索活動以獲取完整的 `actVO`
	    ActVO actVO1 = actSvc.getOneAct(Integer.valueOf(actNo));
	    if (actVO1 == null) {
	        model.addAttribute("errorMessage", "活動不存在");
	        return "redirect:/act/memMyAct";
	    }
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.deleteAct(Integer.valueOf(actNo));
		// 獲取所有參與該活動的會員
	    List<ParticipantVO> participants = participantSvc.findParticipantsByActNo(actVO1.getActNo());
	    if (participants != null && !participants.isEmpty()) {
	        String notificationContent = "您報名的活動 \"" + actVO1.getActTitle() + "\" 已被取消。";
	        for (ParticipantVO participant : participants) {
	            NotificationVO notification = new NotificationVO();
	            notification.setMemNo(participant.getMemberVO().getMemNo());
	            notification.setNotiContent(notificationContent);
	            notification.setNotiTime(new Timestamp(System.currentTimeMillis()));
	            notification.setNotiStatus((byte) 0);
	            notiSvc.addNoti(notification);
	        }
	    }
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/act/memMyAct"; // 刪除完成後轉交memMyAct.html
	}
	
	
	@PostMapping("deleteByBackEnd")
	public String deleteByBackEnd(@RequestParam("actNo") String actNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actSvc.deleteAct(Integer.valueOf(actNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/act/actBackEnd"; // 刪除完成後轉交actBackEnd.html
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
    
    @GetMapping("/listAllAct")
	public String listAllAct(Model model) {
		return "front-end/act/listAllAct";
	}
    
    @GetMapping("/memMyAct")
	public String memMyAct(HttpSession session, ModelMap model, String memNo) {
    	//抓取seesion內已登入會員的編號
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
	        return "redirect:/member/loginMem";
	    }

	    List<ActVO> list = actSvc.getAll().stream()
	    		//判斷登入的會員
	            .filter(p -> p.getMemberVO().getMemNo().equals(loggedInMember.getMemNo()))
	            
	            .collect(Collectors.toList());//加入集合

	    Map<Integer, Double> ratingMap = new HashMap<>();
		//計算評價
	    for (ActVO act : list) {
	        double rating = 0;
	        if (act.getEvalSum() != 0) {
	        	// 四捨五入到小數點後一位
	            rating = Math.round((double) 
	            		act.getActRateSum() / act.getEvalSum() * 10.0) / 10.0;
	        }
	        ratingMap.put(act.getActNo(), rating); 
	    }
	    model.addAttribute("ratingMap", ratingMap);  // 傳遞 ratingMap 到前端
	    model.addAttribute("memMyAct", list);
		return "front-end/act/memMyAct";
	}    
    
    @GetMapping("/actBackEnd")
    public String actBackEnd(Model model) {
    	return "back-end/act/actBackEnd";
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
	
	//活動列表的ajax
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
     // 按報名截止日升序排列
        list.sort((a, b) -> a.getActEnrollEnd().compareTo(b.getActEnrollEnd()));
        model.addAttribute("actListData", list);
        return "front-end/act/listAllActFragment :: resultsList";
	}
	

	//進入活動列表頁面先自動載入全部活動
	@GetMapping("ajaxSearchAll")
    public String getAllAct(HttpServletRequest req,Model model) {
		Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        Session session = sessionFactory.openSession();
        List<ActVO> list = HibernateUtil_CompositeQuery_Act.getAllC(queryParams, session);
        // 按報名截止日升序排列
        list.sort((a, b) -> a.getActEnrollEnd().compareTo(b.getActEnrollEnd()));
        model.addAttribute("actListData", list);
        return "front-end/act/listAllActFragment :: resultsList";
    }
	
	@GetMapping("/memActList")
    public String memAllAct(Model model, Integer memNo) {
		List<ActVO> list = actSvc.getActsByMem(memNo);
		Map<Integer, Double> ratingMap = new HashMap<>();
		//計算評價
	    for (ActVO act : list) {
	        double rating = 0;
	        if (act.getEvalSum() != 0) {
	        	// 四捨五入到小數點後一位
	            rating = Math.round((double) 
	            		act.getActRateSum() / act.getEvalSum() * 10.0) / 10.0;
	        }
	        ratingMap.put(act.getActNo(), rating); 
	    }
	    //取得團主資料
		MemberVO memberVO = memberSvc.getOneMem(memNo); 
	    // 添加資料到模型中
		model.addAttribute("memberVO", memberVO);
	    model.addAttribute("memMyAct", list);
	    model.addAttribute("ratingMap", ratingMap);  // 傳遞 ratingMap 到前端
    	return "front-end/act/memActList";
    }

}