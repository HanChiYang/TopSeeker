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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.actpicture.model.ActPictureVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.news.model.NewsService;
import com.topseeker.news.model.NewsVO;
import com.topseeker.news.model.RedisService;
import com.topseeker.newspic.model.NewsPicService;
import com.topseeker.newspic.model.NewsPicVO;
import com.topseeker.shop.productpic.model.ShopProductPicVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Act;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_News;




@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	NewsService newsSvc;
	
	@Autowired
	NewsPicService newsPicSvc;

	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
    RedisService redisService;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addNews")
	public String addNews(ModelMap model) {
		NewsVO newsVO = new NewsVO();
		model.addAttribute("newsVO", newsVO);
		return "back-end/news/addNews";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid NewsVO newsVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts
			) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsVO, result, "newsPic");
	    
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			
			model.addAttribute("errorMessage", "照片: 請上傳照片");
			
		} else {
			List<NewsPicVO> picSet = new ArrayList<>();
			
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();			
				NewsPicVO newsPicVO = new NewsPicVO();
				newsPicVO.setNewsImg(buf);
				newsPicVO.setNewsVO(newsVO);				
				picSet.add(newsPicVO);								
			}
			
			newsVO.setNewsPic(picSet);
			
		}
		if (result.hasErrors()) {
			model.addAttribute("newsVO", newsVO);
			return "back-end/news/addNews";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsSvc.addNews(newsVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<NewsVO> list = newsSvc.getAll();
		model.addAttribute("newsListData", list);
		return "redirect:/news/newsBackEnd?success=insert";
	}
	

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("newsNo") String newsNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		NewsVO newsVO = newsSvc.getOneNews(Integer.valueOf(newsNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsVO", newsVO);
		return "back-end/news/update_news_input";
	}

	@PostMapping("update")
	public String update(@Valid NewsVO newsVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsVO, result, "newsImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
		} else {
		    List<NewsPicVO> picSet = new ArrayList<>();
		    
		    for (MultipartFile multipartFile : parts) {
		        byte[] buf = multipartFile.getBytes();
		        
		        NewsPicVO newsPicVO = new NewsPicVO();
		        newsPicVO.setNewsImg(buf);
		        newsPicVO.setNewsVO(newsVO); // 設置關聯到商品
		        
		        picSet.add(newsPicVO); // 添加到集合中
		    }
		    newsVO.setNewsPic(picSet); // 設置商品的圖片集合
		}
		if (result.hasErrors()) {
			model.addAttribute("newsVO", newsVO);
			return "back-end/news/update_news_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		newsSvc.updateNews(newsVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		newsVO = newsSvc.getOneNews(Integer.valueOf(newsVO.getNewsNo()));
		model.addAttribute("newsVO", newsVO);
		return "redirect:/news/newsBackEnd?success=update";// 修改成功後重定向到新聞列表頁面
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
		return "back-end/news/newsBackEnd"; // 刪除完成後轉交listAllEmp.html
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
      
    @GetMapping("/newsBackEnd")
  	public String listAllNews(Model model) {
  		return "back-end/news/newsBackEnd";
  	}
    
   
    @ModelAttribute("newsListData")
  	protected List<NewsVO> referenceNewsListData(Model model) {
  		
      	List<NewsVO> list = newsSvc.getAll();
  		return list;
  	}
    
    @GetMapping("/listAllNews")
    public String newsIndex(Model model) {
    	List<NewsVO> list = newsSvc.getAll();
        model.addAttribute("newsListData", list);

        // 準備一個 Map 存放每則新聞的觀看次數
        Map<Integer, Integer> viewCounts = new HashMap<>();
        if (list != null && !list.isEmpty()) {
	        for (NewsVO news : list) {
	        	Integer viewCount = redisService.getViewCount(news.getNewsNo());
	            viewCounts.put(news.getNewsNo(), viewCount);
	            System.out.println("newsVO.newsNo: " + news.getNewsNo() + " (Type: " + news.getNewsNo().getClass().getName() + ")");
	        }
        }
        model.addAttribute("viewCounts", viewCounts);
        System.out.println(viewCounts);
        return "front-end/news/listAllNews";
    } 
    
    @PostMapping("newsSearch")
	public String ajaxSearch(HttpServletRequest req, Model model) {
    	Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        // 處理日期區間查詢條件
        if (queryParams.containsKey("timeFilter")) {
            String timeFilter = queryParams.get("timeFilter")[0];
            if (!timeFilter.equals("all")) {
                String[] dateRange = getDateRange(timeFilter);
                queryParams.put("startDate", new String[] { dateRange[0] });
                queryParams.put("endDate", new String[] { dateRange[1] });
            }
        }
        Session session = sessionFactory.openSession();
        List<NewsVO> list = HibernateUtil_CompositeQuery_News.getAllC(queryParams, session);
        
        // 準備一個 Map 存放每則新聞的觀看次數
        Map<Integer, Integer> viewCounts = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (NewsVO news : list) {
                Integer viewCount = redisService.getViewCount(news.getNewsNo());
                viewCounts.put(news.getNewsNo(), viewCount);
            }
        }
        
        model.addAttribute("newsListData", list);
        model.addAttribute("viewCounts", viewCounts);
        return "front-end/news/newsFragment :: resultsList";
	}
    
    //進入新聞頁面先自動載入全部新聞
    @GetMapping("newsSearch")
    public String getAllNews(HttpServletRequest req,Model model) {
    	Map<String, String[]> map = req.getParameterMap();
        Map<String, String[]> queryParams = new HashMap<>(map);
        Session session = sessionFactory.openSession();
        List<NewsVO> newslist = HibernateUtil_CompositeQuery_News.getAllC(queryParams, session);        
        
        // 準備一個 Map 存放每則新聞的觀看次數
        Map<Integer, Integer> viewCounts = new HashMap<>();
        if (newslist != null && !newslist.isEmpty()) {
            for (NewsVO news : newslist) {
                Integer viewCount = redisService.getViewCount(news.getNewsNo());
                viewCounts.put(news.getNewsNo(), viewCount);
            }
        }
        model.addAttribute("newsListData", newslist);
        model.addAttribute("viewCounts", viewCounts);
        return "front-end/news/newsFragment :: resultsList";
    }
    // 處理篩選日期區間的方法
    private String[] getDateRange(String timeFilter) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate;
        switch (timeFilter) {
            case "24h":
                startDate = endDate.minusDays(1);
                break;
            case "1w":
                startDate = endDate.minusWeeks(1);
                break;
            case "1y":
                startDate = endDate.minusYears(1);
                break;
            default:
                return new String[] { "", "" };
        }
        return new String[] { startDate.toString(), endDate.toString() };
    }
    
}