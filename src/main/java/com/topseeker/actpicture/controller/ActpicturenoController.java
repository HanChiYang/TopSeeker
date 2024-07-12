package com.topseeker.actpicture.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.actpicture.model.ActPictureService;
import com.topseeker.actpicture.model.ActPictureVO;




@Controller
@Validated
@RequestMapping("/actpicture")
public class ActpicturenoController {
		
	@Autowired
	ActPictureService actPictureSvc;

	@Autowired
	ActService actSvc;
	
	
}