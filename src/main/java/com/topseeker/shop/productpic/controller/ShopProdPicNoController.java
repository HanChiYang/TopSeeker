package com.topseeker.shop.productpic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.productpic.model.ShopProductPicService;

@Controller
@Validated
@RequestMapping("/shop/prodpic")
public class ShopProdPicNoController {

	@Autowired
	ShopProductPicService shopProductPicSvc;
	
	@Autowired
	ShopProductService shopProductSvc;
	
	
	
	
}
