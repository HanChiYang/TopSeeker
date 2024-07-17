package com.topseeker.tourDetail.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourDetail.model.TourDetailService;
import com.topseeker.tourDetail.model.TourDetailVO;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_tourDetail {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了tourDetailService --> 供第66使用
	@Autowired
	TourDetailService tourDetailSvc;
	
	@Autowired
	TourService tourSvc;
	
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
  
    //=========== 以下第63~75行是提供給 /src/main/resources/ttourDetaillates/back-end/tourDetail/select_page.html 與 listAlltourDetail.html 要使用的資料 ===================   
    @GetMapping("/tourDetail/select_page")
	public String select_page(Model model) {
		return "back-end/tourDetail/select_page";
	}
    
    @GetMapping("/tourDetail/listAllTourDetail")
	public String listAllTourDetail(Model model) {
		return "back-end/tourDetail/listAllTourDetail";
	}
    
    @ModelAttribute("tourDetailListData")  // for select_page.html 第97 109行用 // for listAlltourDetail.html 第85行用
	protected List<TourDetailVO> referenceListData(Model model) {
		
    	List<TourDetailVO> list = tourDetailSvc.getAll();
		return list;
	}
    
	@ModelAttribute("tourListData") // for select_page.html 第135行用
	protected List<TourVO> referenceListData_Tour(Model model) {
		model.addAttribute("tourVO", new TourVO()); // for select_page.html 第133行用
		List<TourVO> list = tourSvc.getAll();
		return list;
	}

}