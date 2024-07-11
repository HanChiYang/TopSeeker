package com;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.artcomment.model.ArtCommentService;
import com.topseeker.artcomment.model.ArtCommentVO;
import com.topseeker.article.model.ArticleService;
import com.topseeker.article.model.ArticleVO;
import com.topseeker.artpic.model.ArtPicService;
import com.topseeker.artpic.model.ArtPicVO;
import com.topseeker.artreport.model.ArtReportService;
import com.topseeker.artreport.model.ArtReportVO;
import com.topseeker.employee.model.EmployeeService;
import com.topseeker.follow.model.FollowService;
import com.topseeker.follow.model.FollowVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.message.model.MessageService;
import com.topseeker.message.model.MessageVO;
import com.topseeker.notification.model.NotificationService;
import com.topseeker.participant.model.ParticipantService;
import com.topseeker.participant.model.ParticipantVO;
import com.topseeker.replymessage.model.ReplyMessageService;
import com.topseeker.replymessage.model.ReplyMessageVO;
import com.topseeker.report.model.ReportService;
import com.topseeker.report.model.ReportVO;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了EmpService --> 供第66使用
	@Autowired
	MemberService memSvc;
	
	@Autowired
	NotificationService notiSvc;

	@Autowired
	ParticipantService participantSvc;
	
	@Autowired
	ActService actSvc;
	
	@Autowired
	ReportService reportSvc;
	
	@Autowired
	MessageService messageSvc;
	
	@Autowired
	ReplyMessageService replymessageSvc;
	
	@Autowired
	EmployeeService employeeSvc;
	
	@Autowired
	ArticleService articleSvc;
	
	@Autowired
	ArtCommentService artcommentSvc;
	
	@Autowired
	ArtReportService artreportSvc;
	
	@Autowired
	ArtPicService artpicSvc;
	
//	@Autowired
//	FollowService followSvc;
	
	
    @GetMapping("/")
    public String index(Model model) {
        return "front-end/index";
    }

    
    //=============== 會員登入 ===================  
    
    @GetMapping("/member/registrationMem")
	public String registrationMem(Model model) {
    	
		return "front-end/member/registrationMem";
	}
    @GetMapping("/member/loginMem")
    public String loginMem(Model model) {
    	return "front-end/member/loginMem";
    }
    
    @ModelAttribute("memListData") // for select_page.jsp 第96 108行用 // for listAllEmp.jsp 第85行用
  	protected List<MemberVO> referenceListDataMem(Model model) {
      	model.addAttribute("memberVO", new MemberVO());
      	List<MemberVO> list = memSvc.getAll();
  		return list;
  	}
    //=============== 會員專區 ===================  
    
    @GetMapping("/protected/member/indexMem")
    public String indexMem(Model model) {
    	return "front-end/member/indexMem";
    }
    
    //=============== 後台管理首頁 ===================  

    @GetMapping("/backend_protected/backEndIndex")
    public String backEndIndex(Model model) {
    	return "back-end/back_end_index";
    }

    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/participant/select_page")
 	public String select_page(Model model) {
 		return "back-end/participant/select_page";
 	}
    
    @GetMapping("/participant/listAllParticipant")
 	public String listAllParticipant(Model model) {
 		return "back-end/participant/listAllParticipant";
 	}
    
    @ModelAttribute("participantListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ParticipantVO> referenceListData(Model model) {
 		
    	List<ParticipantVO> list = participantSvc.getAll();
 		return list;
 	}
    
 	@ModelAttribute("actListData") // for select_page.html 第135行用
 	protected List<ActVO> referenceListData_Dept(Model model) {
 		model.addAttribute("actVO", new ActVO()); // for select_page.html 第133行用
 		List<ActVO> list = actSvc.getAll();
 		return list;
 	}
 	
 	  //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/report/select_page")
 	public String select_page1(Model model) {
 		return "back-end/report/select_page";
 	}
    
    @GetMapping("/report/listAllReport")
 	public String listAllReport(Model model) {
 		return "back-end/report/listAllReport";
 	}
    
    @ModelAttribute("reportListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ReportVO> referenceListData1(Model model) {
 		
    	List<ReportVO> list = reportSvc.getAll();
 		return list;
 	}
     
 	  //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/message/select_page")
 	public String select_page2(Model model) {
 		return "back-end/message/select_page";
 	}
    
    @GetMapping("/message/listAllMessage")
 	public String listAllMessage(Model model) {
 		return "back-end/message/listAllMessage";
 	}
    
    @ModelAttribute("messageListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<MessageVO> referenceListData2(Model model) {
 		
    	List<MessageVO> list = messageSvc.getAll();
 		return list;
 	}
     
 	 // =========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/replymessage/select_page")
 	public String select_page3(Model model) {
 		return "back-end/replymessage/select_page";
 	}
    
    @GetMapping("/replymessage/listAllReplyMessage")
 	public String listAllReplyMessage(Model model) {
 		return "back-end/replymessage/listAllReplyMessage";
 	}
    
    @ModelAttribute("replymessageListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ReplyMessageVO> referenceListData3(Model model) {
 		
    	List<ReplyMessageVO> list = replymessageSvc.getAll();
 		return list;
 	}
     
 	  //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
     @GetMapping("/article/select_page")
 	public String select_page4(Model model) {
 		return "back-end/article/select_page";
 	}
     
     @GetMapping("/article/listAllArticle")
 	public String listAllArticle(Model model) {
 		return "back-end/article/listAllArticle";
 	}
     
     @ModelAttribute("articleListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ArticleVO> referenceListData4(Model model) {
 		
     	List<ArticleVO> list = articleSvc.getAll();
 		return list;
 	}
     
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/artcomment/select_page")
 	public String select_page5(Model model) {
 		return "back-end/artcomment/select_page";
 	}
    
    @GetMapping("/artcomment/listAllArtComment")
 	public String listAllArtComment(Model model) {
 		return "back-end/artcomment/listAllArtComment";
 	}
    
    @ModelAttribute("artcommentListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ArtCommentVO> referenceListData5(Model model) {
 		
    	List<ArtCommentVO> list = artcommentSvc.getAll();
 		return list;
 	}
    
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/artreport/select_page")
 	public String select_page6(Model model) {
 		return "back-end/artreport/select_page";
 	}
    
    @GetMapping("/artreport/listAllArtReport")
 	public String listAllArtReport(Model model) {
 		return "back-end/artreport/listAllArtReport";
 	}
    
    @ModelAttribute("artreportListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ArtReportVO> referenceListData6(Model model) {
 		
    	List<ArtReportVO> list = artreportSvc.getAll();
 		return list;
 	}
    
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/artpic/select_page")
 	public String select_page7(Model model) {
 		return "back-end/artpic/select_page";
 	}
    
    @GetMapping("/artpic/listAllArtPic")
 	public String listAllArtPic(Model model) {
 		return "back-end/artpic/listAllArtPic";
 	}
    
    @ModelAttribute("artpicListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 	protected List<ArtPicVO> referenceListData7(Model model) {
 		
    	List<ArtPicVO> list = artpicSvc.getAll();
 		return list;
 	}
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
//    @GetMapping("/follow/select_page")
// 	public String select_page8(Model model) {
// 		return "back-end/follow/select_page";
// 	}
//    
//    @GetMapping("/follow/listAllFollow")
// 	public String listAllFollow(Model model) {
// 		return "back-end/follow/listAllFollow";
// 	}
//    
//    @ModelAttribute("followListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
// 	protected List<FollowVO> referenceListData8(Model model) {
// 		
//    	List<FollowVO> list = followSvc.getAll();
// 		return list;
// 	}
    
    
}