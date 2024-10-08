package com.topseeker.participant.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.participant.model.ParticipantService;
import com.topseeker.participant.model.ParticipantVO;
import java.util.HashMap;

@Controller
@Validated
@RequestMapping("/participant")
public class ParticipantController {
	
	@Autowired
	ParticipantService participantSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	ActService actSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;
	
	/*
	 * This method will serve as addEmp.html handler.
	 */
	 @GetMapping("addParticipant")
	    public String addParticipant(@RequestParam("actNo") Integer actNo, ModelMap model, HttpSession session) {
	        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
	        if (loggedInMember == null) {
	            return "redirect:/member/loginMem";
	        }

	        ParticipantVO participantVO = new ParticipantVO();
	        ActVO actVO = actSvc.getOneAct(actNo);
	        participantVO.setActVO(actVO);
	        participantVO.setMemberVO(loggedInMember);

	        model.addAttribute("participantVO", participantVO);
	        model.addAttribute("actTitle", actVO.getActTitle());
	        model.addAttribute("memName", loggedInMember.getMemName());
	        return "front-end/participant/addParticipant";
	    }

	    @PostMapping("insert")
	    public String insert(@Valid ParticipantVO participantVO, BindingResult result, ModelMap model,
	                         MultipartFile[] parts, HttpSession session) throws IOException {
//	        if (result.hasErrors()) {
//	            return "front-end/participant/addParticipant";
//	        }
//
//	        participantSvc.addParticipant(participantVO);

	    	ActVO actVO = actSvc.getOneAct(participantVO.getActVO().getActNo());
		    if (actVO != null) {
		        int currentCount = actVO.getActCurrentCount();
		        int checkCount = actVO.getActCheckCount();
		        int totalParticipants = currentCount + checkCount;

		     // 檢查參與人數是否超過限制
		        if (participantVO.getActJoinCount() > (actVO.getActMaxCount() - totalParticipants)) {
		            model.addAttribute("errorMessage", "參與人數超過活動可接受人數限制");
		            model.addAttribute("actTitle", actVO.getActTitle());

		            MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		            if (loggedInMember != null) {
		                model.addAttribute("memName", loggedInMember.getMemName());
		            }

		            model.addAttribute("participantVO", participantVO); // 保持參團資訊
		            return "front-end/participant/addParticipant";  // 返回表單頁面，並顯示錯誤信息
		        }

		        // 新增參與者並更新活動待審核人數
		        participantSvc.addParticipant(participantVO);
		        actSvc.updateActCheckCount(actVO.getActNo(), participantVO.getActJoinCount());
		    }
	        model.addAttribute("participantListData", participantSvc.getAll());
	        model.addAttribute("success", "- (新增成功)");
	        return "redirect:/participant/listMyAllParticipant";
	    }

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("actPartNo") String actPartNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ParticipantVO participantVO = participantSvc.getOneParticipant(Integer.valueOf(actPartNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("participantVO", participantVO);
		return "front-end/participant/update_participant_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@RequestParam("actPartNo") Integer actPartNo,
	                     @RequestParam("actJoinCount") Integer actJoinCount,
	                     @RequestParam("actCommit") Integer actCommit,
	                     @RequestParam("actStar") Integer actStar,
	                     @RequestParam("actEva") String actEva,
	                     ModelMap model) {
	    // 获取现有的 ParticipantVO
	    ParticipantVO participantVO = participantSvc.getOneParticipant(actPartNo);

	    // 更新需要的字段
	    if (actJoinCount != null) {
	        participantVO.setActJoinCount(actJoinCount);
	    }
	    if (actCommit != null) {
	        participantVO.setActCommit(actCommit);
	    }
	    if (actStar != null) {
	        participantVO.setActStar(actStar);
	    }
	    if (actEva != null) {
	        participantVO.setActEva(actEva);
	    }

	    // 更新 ParticipantVO
	    participantSvc.updateParticipant(participantVO);

	    // 获取关联的 ActVO
	    ActVO actVO = actSvc.getOneAct(participantVO.getActVO().getActNo());
	    
	    // 確保 actRateSum 和 evalSum 不為 null
	    if (actVO.getActRateSum() == null) {
	        actVO.setActRateSum(0);
	    }

	    // 更新 ActVO 的 actRateSum 和 evalSum
	    if (actStar != null) {
	        int newActRateSum = actVO.getActRateSum() + actStar;
	        int newEvalSum = actVO.getEvalSum() + 1;
	        actVO.setActRateSum(newActRateSum);
	        actVO.setEvalSum(newEvalSum);
	        actSvc.updateAct(actVO);
	    }

	    // 添加活动标题到模型
	    model.addAttribute("participantVO", participantVO);
	    model.addAttribute("actTitle", actVO.getActTitle());

	    return "redirect:/participant/listMyAllParticipant"; // 修改成功后转交视图
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actPartNo") String actPartNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		participantSvc.deleteParticipant(Integer.valueOf(actPartNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ParticipantVO> list = participantSvc.getAll();
		model.addAttribute("participantListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/participant/listAllParticipant"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("actListData")
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


	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("actMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "阿里山趴趴走");
		map.put(2, "來去草嶺古道看芒草");
		map.put(3, "阿朗壹古道探險之旅部");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ParticipantVO participantVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(participantVO, "participantVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listParticipants_ByCompositeQuery")
	public String listAllParticipant(HttpServletRequest req, Model model, HttpSession session) {
	    MemberVO loggedInMember = getCurrentMember(session);
	    if (loggedInMember == null) {
	        return "redirect:/member/loginMem";
	    }

	    Map<String, String[]> map = req.getParameterMap();
	    List<ParticipantVO> list = participantSvc.getAll(map).stream()
	            .filter(p -> p.getMemberVO().getMemNo().equals(loggedInMember.getMemNo()))
	            .collect(Collectors.toList());

	    model.addAttribute("participantListData", list);
	    return "front-end/participant/listAllParticipant";
	}
	
	private MemberVO getCurrentMember(HttpSession session) {
        return (MemberVO) session.getAttribute("loggedInMember");
    }
	
	@GetMapping("listMyAllParticipant")
	public String listMyAllParticipant(HttpSession session, Model model) {
	    MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
	    if (loggedInMember == null) {
	        return "redirect:/member/loginMem";
	    }

	    List<ParticipantVO> list = participantSvc.getAll().stream()
	            .filter(p -> p.getMemberVO().getMemNo().equals(loggedInMember.getMemNo()))
	            .collect(Collectors.toList());

	    model.addAttribute("participantListData", list);
	    return "front-end/participant/listMyAllParticipant";
	}
	
	@PostMapping("updateStatus")
	public String updateStatus(@RequestParam("actPartNo") Integer actPartNo, @RequestParam("actCommit") Integer actCommit, ModelMap model) {
	    ParticipantVO participantVO = participantSvc.getOneParticipant(actPartNo);
	    
	    Integer previousStatus = participantVO.getActCommit();
	    Integer joinCount = participantVO.getActJoinCount();
	    
	    participantVO.setActCommit(actCommit);
	    participantSvc.updateParticipant(participantVO);

	    // 取得對應的活動資料
	    ActVO actVO = actSvc.getOneAct(participantVO.getActVO().getActNo());
	    
	    // 如果狀態從"待審核"變為"審核通過"
	    if (previousStatus == 0 && actCommit == 1) {
	        // 更新活動的已參與人數
	        actVO.setActCurrentCount(actVO.getActCurrentCount() + joinCount);
	        // 更新活動的待審核人數
	        actVO.setActCheckCount(actVO.getActCheckCount() - joinCount);
	        // 如果已參與人數等於已報名人數上限，將活動狀態設為1
	        if (actVO.getActCurrentCount().equals(actVO.getActMaxCount())) {
	            actVO.setActStatus(1);
	        }
	    }
	    // 如果狀態從"待審核"變為"審核不通過"
	    else if (previousStatus == 0 && actCommit == 2) {
	        // 更新活動的待審核人數
	        actVO.setActCheckCount(actVO.getActCheckCount() - joinCount);
	    }

	    // 更新活動資料
	    actSvc.updateAct(actVO);
	    
	    List<ParticipantVO> list = participantSvc.getAll();
	    model.addAttribute("participantListData", list);
	    return "front-end/participant/participantCheck";
	}

	@GetMapping("participantCheck")
	public String checkParticipants(ModelMap model) {
	    List<ParticipantVO> list = participantSvc.getAll();
	    model.addAttribute("participantListData", list);
	    return "front-end/participant/participantCheck"; 
	}
	
	@PostMapping("/checkIfSignedUp")
	@ResponseBody
	public Map<String, Object> checkIfSignedUp(@RequestParam("actNo") Integer actNo, @RequestParam("memNo") Integer memNo) {
	    Map<String, Object> response = new HashMap<>();
	    List<ParticipantVO> participants = participantSvc.getAll();
	    boolean signedUp = participants.stream()
	                                    .anyMatch(p -> p.getActVO().getActNo().equals(actNo) && p.getMemberVO().getMemNo().equals(memNo));
	    response.put("signedUp", signedUp);
	    return response;
	}

}
