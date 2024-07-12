package com.topseeker.news.controller;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.news.model.NewsService;
import com.topseeker.news.model.NewsVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_News;




@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	NewsService newsSvc;

	@Autowired
    private SessionFactory sessionFactory;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addNews")
	public String addNews(ModelMap model) {
		NewsVO newsVO = new NewsVO();
//		actVO.setActCurrentCount(0);
//		actVO.setActCheckCount(0);
//		actVO.setActStatus(0);
		model.addAttribute("newsVO", newsVO);
		return "front-end/news/addNews";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid NewsVO newsVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(actVO, result, "upFiles");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				actVO.setUpFiles(buf);
//			}
//		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/act/addAct";
//		}
		if (result.hasErrors()) {
			return "front-end/news/addNews";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsSvc.addNews(newsVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<NewsVO> list = newsSvc.getAll();
		model.addAttribute("newsListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/news/listAllNews"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("newsNo") String newsNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		NewsVO newsVO = newsSvc.getOneNews(Integer.valueOf(newsNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsVO", newsVO);
		return "front-end/news/update_news_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid NewsVO newsVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(empVO, result, "upFiles");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] upFiles = empSvc.getOneEmp(empVO.getEmpno()).getUpFiles();
//			empVO.setUpFiles(upFiles);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				empVO.setUpFiles(upFiles);
//			}
//		}
//		if (result.hasErrors()) {
//			return "back-end/emp/update_emp_input";
//		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsSvc.updateNews(newsVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		newsVO = newsSvc.getOneNews(Integer.valueOf(newsVO.getNewsNo()));
		model.addAttribute("newsVO", newsVO);
		return "front-end/news/listOneNews"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("newsNo") String newsNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsSvc.deleteNews(Integer.valueOf(newsNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<NewsVO> list = newsSvc.getAll();
		model.addAttribute("newsListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/news/listAllNews"; // 刪除完成後轉交listAllEmp.html
	}


	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(NewsVO newsVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(newsVO, "newsVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	@GetMapping("/select_page")
  	public String select_page_news(Model model) {
  		return "front-end/news/select_page";
  	}
      
    @GetMapping("/listAllNews")
  	public String listAllNews(Model model) {
  		return "front-end/news/listAllNews";
  	}
    
   
    @ModelAttribute("newsListData")
  	protected List<NewsVO> referenceNewsListData(Model model) {
  		
      	List<NewsVO> list = newsSvc.getAll();
  		return list;
  	}
    
    @GetMapping("/newsIndex")
    public String newsIndex(Model model) {
    	return "front-end/news/newsIndex";
    }
      
    
    @PostMapping("newsSearch")
	public String ajaxSearch(HttpServletRequest req, Model model) {
    	Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        // 處理日期區間查詢條件
//        String actStart = req.getParameter("actStart");
//        String actEnd = req.getParameter("actEnd");
//
//        if (actStart != null && !actStart.trim().isEmpty() && actEnd != null && !actEnd.trim().isEmpty()) {
//            queryParams.put("actDateRange", new String[]{actStart + "," + actEnd});
//        }

        Session session = sessionFactory.openSession();
        List<NewsVO> list = HibernateUtil_CompositeQuery_News.getAllC(queryParams, session);
        model.addAttribute("newsListData", list);       
        return "front-end/news/newsFragment :: resultsList";
	}
    
    @GetMapping("newsSearch")
    public String getAllNews(Model model) {
        List<NewsVO> newsList = newsSvc.getAll();
        model.addAttribute("newsListData", newsList);
        return "front-end/news/newsFragment :: resultsList";
    }

    
}