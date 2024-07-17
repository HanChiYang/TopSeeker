package com.topseeker.tourCol.contorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColService;
import com.topseeker.tourCol.model.TourColVO;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_tourCol {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了tourColService --> 供第66使用
	@Autowired
	TourColService tourColSvc;
	
	@Autowired
	TourService tourSvc;
	
	@Autowired
	MemberService memberSvc;
	
    // inject(注入資料) via application.properties
    @Value("${welcome.message}")
    private String message;
	
//    private List<String> myList = Arrays.asList("Spring Boot Quickstart 官網 : https://start.spring.io", "IDE 開發工具", "直接使用(匯入)官方的 Maven Spring-Boot-demo Project + pom.xml", "直接使用官方現成的 @SpringBootApplication + SpringBootServletInitializer 組態檔", "依賴注入(DI) HikariDataSource (官方建議的連線池)", "Thymeleaf", "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)");
//    @GetMapping("/")
//    public String index(Model model) {
//    	model.addAttribute("message", message);
//        model.addAttribute("myList", myList);
//        return "index"; //view
//    }
//    
//    // http://......../hello?name=peter1
//    @GetMapping("/hello")
//    public String indexWithParam(
//            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
//        model.addAttribute("message", name);
//        return "index"; //view
//    }
//    
  
    //=========== 以下第63~75行是提供給 /src/main/resources/ttourCollates/back-end/tourCol/select_page.html 與 listAlltourCol.html 要使用的資料 ===================   
    @GetMapping("/tourCol/select_page")
	public String select_page(Model model) {
		return "back-end/tourCol/select_page";
	}
    
    @GetMapping("/tourCol/listAllTourCol")
	public String listAllTourCol(Model model) {
		return "back-end/tourCol/listAllTourCol";
	}
    
    @ModelAttribute("tourColListData")  // for select_page.html 第97 109行用 // for listAlltourCol.html 第85行用
	protected List<TourColVO> referenceListData(Model model) {
		
    	List<TourColVO> list = tourColSvc.getAll();
		return list;
	}
    
	@ModelAttribute("tourListData") // for select_page.html 第135行用
	protected List<TourVO> referenceListData_Tour(Model model) {
		model.addAttribute("tourVO", new TourVO()); // for select_page.html 第133行用
		List<TourVO> list = tourSvc.getAll();
		return list;
	}
	
	@ModelAttribute("memberListData") // for select_page.html 第135行用
	protected List<MemberVO> referenceListData_Member(Model model) {
		model.addAttribute("memberVO", new MemberVO()); // for select_page.html 第133行用
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}

}