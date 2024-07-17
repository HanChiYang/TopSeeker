package com.topseeker.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topseeker.member.model.MemberVO;
import com.topseeker.notification.model.NotificationService;
import com.topseeker.notification.model.NotificationVO;

@Component
public class NotificationFilter implements Filter {

	@Autowired
	NotificationService notiSvc;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Notification filter init");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		HttpSession session = httpRequest.getSession();

		// 檢查是否已登入
		if (session.getAttribute("loggedInMember") != null) {
			MemberVO memberVO = (MemberVO) session.getAttribute("loggedInMember");
			
			//獲取資料庫是否有新通知
			List<NotificationVO> notiList = notiSvc.listNewNoti(memberVO.getMemNo());

		        if (!notiList.isEmpty()) {
		        	// 如果有新通知，設置一個 session 屬性
		            session.setAttribute("hasNewNotifications", true);
		        }else {
		            session.setAttribute("hasNewNotifications", false);
		        }
		        chain.doFilter(req, res);
		    } else {
	        chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {
	}
}