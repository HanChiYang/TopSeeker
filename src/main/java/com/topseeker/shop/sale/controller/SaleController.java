package com.topseeker.shop.sale.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.notification.model.NotificationService;
import com.topseeker.notification.model.NotificationVO;
import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@RequestMapping("/shop/sale")
public class SaleController {
	
	@Autowired
	SaleService saleSvc;
	
	@Autowired
	NotificationService notiSvc;
	
	@Autowired
	MemberService memSvc;
	
	//日期格式轉換用
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, true));
    }
	
 
	
	//前台用    
  	@GetMapping("saleLists")
  	public String saleLists(Model model) {
  		List<SaleVO> saleLists = saleSvc.getCurrentSales();
  		model.addAttribute("saleLists", saleLists);
  		return "front-end/shop/saleList";
  	}
    
        
    //後台用 
    @GetMapping("select_page")
 	public String select_page_sale(Model model) {
    	List<SaleVO> saleLists = saleSvc.getCurrentSales();
		model.addAttribute("saleLists", saleLists);
 		return "back-end/shop/sale/select_page";
 	}
  	
//	@GetMapping("saleListbd")
//  	public String saleListbd(Model model) {
//  		List<SaleVO> saleLists = saleSvc.getCurrentSales();
//  		model.addAttribute("saleLists", saleLists);
//  		return "back-end/shop/sale/saleListbd";
//  	}
    
	@GetMapping("listAllSale")
	public String listAllSale(Model model) {
		return "back-end/shop/sale/listAllSale";
	}
	    
	@ModelAttribute("saleListData")
	protected List<SaleVO> referenceListData(Model model) {
			
	  	List<SaleVO> list = saleSvc.getAll();
		return list;
	}
	

////////////////////////////////////////////////////////////////////////	
//新增
	@GetMapping("addSale")
	public String addSale(ModelMap model) {
		SaleVO saleVO = new SaleVO();
		model.addAttribute("saleVO" , saleVO);
		return "back-end/shop/sale/addSale";
	}
	
	//在addSale頁面執行insert
	@PostMapping("insert")
	public String insert(@Valid SaleVO saleVO, BindingResult result, ModelMap model
			)throws IOException {
		
		if(!saleVO.isStartDateBeforeEndDate(saleVO.getSaleStdate(), saleVO.getSaleEddate())) {
			model.addAttribute("errorMessage", "活動起始日期不得晚於結束日期");
			return "back-end/shop/sale/addSale";
		}
		
		if (result.hasErrors()) {
			return "back-end/shop/sale/addSale";
		}
		
		saleSvc.addSale(saleVO);
		
		//新增優惠活動後發通知給會員
		List<MemberVO> memList = memSvc.getAll();
		for(MemberVO mem : memList) {
			NotificationVO notificationVO = new NotificationVO();
			notificationVO.setMemNo(mem.getMemNo());
			notificationVO.setNotiContent(
					"不可能吧!!" + 
					saleVO.getSaleName() + 
					"優惠活動竟然來了!! 自" + 
					stDateFormat(saleVO) + 
					" 至 " + 
					edDateFormat(saleVO) + 
					"，全館消費金額滿 " + 
					saleVO.getSaleAmount() + 
					"，即可享有" + 
					discountFormat(saleVO) + 
					"折優惠，快來商城看看吧。");
			notificationVO.setNotiTime(new Timestamp(System.currentTimeMillis()));
			notificationVO.setNotiStatus((byte)0);
			notiSvc.addNoti(notificationVO);	
		}
		
		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);
		return "redirect:/shop/sale/listAllSale";
	}

//修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("saleNo") String saleNo, ModelMap model) {
		SaleVO saleVO = saleSvc.getOneSale(Integer.valueOf(saleNo));  
		model.addAttribute("saleVO", saleVO);
		return "back-end/shop/sale/updateSale";// 查詢完成後轉交updateSale.html
	}
	
	@PostMapping("update")
	public String update(@Valid SaleVO saleVO, BindingResult result, ModelMap model
			) throws IOException {
		
		if(!saleVO.isStartDateBeforeEndDate(saleVO.getSaleStdate(), saleVO.getSaleEddate())) {
			model.addAttribute("errorMessage", "活動起始日期不得晚於結束日期");

			return "back-end/shop/sale/updateSale";
		}
	
		if (result.hasErrors()) {
			return "back-end/shop/sale/updateSale";
		}
		saleSvc.updateSale(saleVO);
		
		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);
		return "back-end/shop/sale/listAllSale";
		
	}
	
//刪除
	@PostMapping("delete")
	public String delete(@RequestParam("saleNo") String saleNo, ModelMap model) {
		
		//檢查該優惠是否被使用
	    if (saleSvc.isSaleUsed(Integer.valueOf(saleNo))) {
	        model.addAttribute("errorMessage", "該優惠活動已被使用，無法刪除");
	    } else {
	    	saleSvc.deleteSale(Integer.valueOf(saleNo));
	    	model.addAttribute("successMessage", "優惠活動已成功刪除");
	    }
		
		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);
		return "back-end/shop/sale/listAllSale";
	}

//取出促銷方案(購物車用)
	@GetMapping("/applicableSales")
	@ResponseBody
	public ResponseEntity<List<SaleVO>> applicableSales(@RequestParam("totalAmount") Integer totalAmount, @RequestParam("currentTime") Timestamp currentTime) {
		
		List<SaleVO> sales = saleSvc.getApplicableSales(totalAmount, currentTime);
		System.out.println(sales);
		
		return sales.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(sales, HttpStatus.OK);
	} 
	
	
//	通知時間格式轉型用
	public String stDateFormat(SaleVO saleVO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String stDate = sdf.format(saleVO.getSaleStdate());        
        return stDate;
	}
	public String edDateFormat(SaleVO saleVO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String edDate = sdf.format(saleVO.getSaleEddate());        
		return edDate;
	}
	
//	通知折扣計算
	public String discountFormat(SaleVO saleVO) {
		Double discountNew;
		DecimalFormat df = new DecimalFormat("0.#");
		
		if(saleVO.getSaleDiscount()*100 % 10 == 0 ) {
			discountNew = saleVO.getSaleDiscount()*10;
		}else {
			discountNew = saleVO.getSaleDiscount()*100;
		}
		
		return df.format(discountNew);
		
	}
	
}
