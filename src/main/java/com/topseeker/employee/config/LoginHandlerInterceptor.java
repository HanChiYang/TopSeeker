package com.topseeker.employee.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginHandlerInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		
		//登入成功之後，有用戶的session
		Object loginUser = request.getSession().getAttribute("loginUser");
		
		if(loginUser == null) {
			request.setAttribute("error", "沒有權限，請先登入");
			request.getRequestDispatcher("/login").forward(request,response);
			return false;
		}else
		{
			return true;
		}
		
		
	}
}
