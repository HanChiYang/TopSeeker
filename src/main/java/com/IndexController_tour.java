package com;

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
import com.topseeker.tourArea.model.TourAreaService;
import com.topseeker.tourArea.model.TourAreaVO;
import com.topseeker.tourDetail.model.TourDetailService;
import com.topseeker.tourGroup.model.TourGroupService;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_tour {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了TourService --> 供第66使用
	@Autowired
	TourService tourSvc;
	
	@Autowired
	TourAreaService tourAreaSvc;
	
	@Autowired
	TourGroupService tourGroupSvc;
	
	@Autowired
	TourDetailService tourDetailSvc;
	
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
    
  
//    =========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/tour/select_page.html 與 listAllTour.html 要使用的資料 ===================   
    @GetMapping("/tour/test")
	public String test(Model model) {
		return "front-end/tour/test";
	}
    
    @GetMapping("/tourOrder/historical_Order")
	public String test2(Model model) {
		return "front-end/tourOrder/historical_Order";
	}
    
    @GetMapping("/tour/indexTour")
	public String indextour(Model model) {
		return "front-end/tour/indexTour";
	}
    
    @GetMapping("/tour/tourQuery")
	public String tourQuery(Model model) {
		return "front-end/tour/tourQuery";
	}
    
    @GetMapping("/tour/tourCol")
	public String tourCol(Model model) {
		return "front-end/tour/tourCol";
	}

    @GetMapping("/tour/tourDetail")
	public String tourDetail(Model model) {
		return "front-end/tour/tourDetail";
	}
    
    
//    =================    後台    =================
    @GetMapping("/tour/backindextour")
	public String backindextour(Model model) {
		return "back-end/tour/backindextour";
	}
    
    
    
    @GetMapping("/tour/select_page")
	public String select_page(Model model) {
		return "back-end/tour/select_page";
	}
    
    @GetMapping("/tour/listAllTour")
	public String listAllTour(Model model) {
		return "back-end/tour/listAllTour";
	}
    
    @ModelAttribute("tourListData")  // for select_page.html 第97 109行用 // for listAllTour.html 第85行用
	protected List<TourVO> referenceListData(Model model) {
		
    	List<TourVO> list = tourSvc.getAll();
		return list;
	}
    
	@ModelAttribute("tourAreaListData") // for select_page.html 第135行用
	protected List<TourAreaVO> referenceListData_TourArea(Model model) {
		model.addAttribute("tourAreaVO", new TourAreaVO()); // for select_page.html 第133行用
		List<TourAreaVO> list = tourAreaSvc.getAll();
		return list;
	}
	
	
	
	
	
	
//	===================TourGroup===================
	
//    @GetMapping("/tourGroup/select_page")
//	public String select_pageTourGroup(Model model) {
//		return "back-end/tourGroup/select_page";
//	}
//    
//    @GetMapping("/tourGroup/listAllTourGroup")
//	public String listAllTourGroup(Model model) {
//		return "back-end/tourGroup/listAllTourGroup";
//	}
//    
//    @ModelAttribute("/tourGroup/tourGroupListData")  // for select_page.html 第97 109行用 // for listAllTour.html 第85行用
//	protected List<TourGroupVO> referenceListDataTourGroup(Model model) {
//		
//    	List<TourGroupVO> list = tourGroupSvc.getAll();
//		return list;
//	}
//    
//	@ModelAttribute("/tourGroup/tourListData") // for select_page.html 第135行用
//	protected List<TourVO> referenceListData_Tour(Model model) {
//		model.addAttribute("tourVO", new TourVO()); // for select_page.html 第133行用
//		List<TourVO> list = tourSvc.getAll();
//		return list;
//	}
	
	
	
//	==================TourDetail===================
//	@GetMapping("/tourDetail/select_page")
//	public String select_page_TourDetail(Model model) {
//		return "back-end/tourDetail/select_page";
//	}
//    
//    @GetMapping("/tourDetail/listAllTourDetail")
//	public String listAllTourDetail(Model model) {
//		return "back-end/tourDetail/listAllTourDetail";
//	}
//    
//    @ModelAttribute("/tourDetail/tourDetailListData")  // for select_page.html 第97 109行用 // for listAlltourDetail.html 第85行用
//	protected List<TourDetailVO> referenceListData_TourDetail(Model model) {
//		
//    	List<TourDetailVO> list = tourDetailSvc.getAll();
//		return list;
//	}
//    
//	@ModelAttribute("/tourDetail/tourListData") // for select_page.html 第135行用
//	protected List<TourVO> referenceListData_TourVO(Model model) {
//		model.addAttribute("tourVO", new TourVO()); // for select_page.html 第133行用
//		List<TourVO> list = tourSvc.getAll();
//		return list;
//	}

}