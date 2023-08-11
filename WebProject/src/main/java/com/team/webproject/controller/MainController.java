package com.team.webproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team.webproject.dto.MD_getPosterDTO;
import com.team.webproject.dto.MDrecomDTO;
import com.team.webproject.dto.MembersDTO;
import com.team.webproject.mapper.LoginMapper;
import com.team.webproject.mapper.MD_RecomMapper;
import com.team.webproject.service.CouponService;


@Controller
@RequestMapping("/main")
public class MainController {
	
	@Autowired
	CouponService couponService;

	@Autowired
	LoginMapper loginMapper;

	
	@Autowired
	MD_RecomMapper mdMapper;
	
	@GetMapping(value = { "/", "" })
	String main(Model model, HttpServletRequest request) {
		
		// 전체 securityholder 내용
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//		System.out.println(principal);
//		UserDetails user = (UserDetails)principal;
////		

//		System.out.println("username : "+user.getUsername());
//		
//		MembersDTO member = loginMapper.checklogin(user.getUsername());
//		System.out.println("member code: "+ member.getMember_code());

//		System.out.println("main logout check:"+ SecurityContextHolder.getContext());
//		System.out.println("main principal : " + principal.toString());
//		System.out.println("user name: "+ (UserDetails)principal);
		
		// 권한이 anonymousUser 이면 
		if (principal.equals("anonymousUser")) {
			model.addAttribute("userId", null);
			return "main/main";
		}
		String username = null;
		//List<GrantedAuthority> authority = new ArrayList<>(((UserDetails) principal).getAuthorities());
		List<GrantedAuthority> authority = (List<GrantedAuthority>)((MembersDTO) principal).getAuthorities();
		// 권한이 User 이면
		//if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_member]")) {
		if (authority.get(0).getAuthority().equals("ROLE_member")) {
			username = ((UserDetails) principal).getUsername();
			System.out.println("main username : " + username);
			model.addAttribute("userId", username);
			// 생일이 있는 달인지 파악해서 쿠폰을 전달해주는 로직
			if(!couponService.checkIfBirthDayCouponExists(username)) {
				couponService.giveBirthDayCoupon(username);
			}
			return "main/main";
		// 권한이 admin 이면
		} else {
			username = ((UserDetails) principal).getUsername();
			System.out.println("main username : " + username);
			model.addAttribute("userId", username);
			return "admin/api";
		}
		
	}
	
	@GetMapping("/mdrecom")
	String mdrecommend(Model model) {
		List<MDrecomDTO> mdDto = mdMapper.getMDrecom();
		List<MD_getPosterDTO> postDto = mdMapper.getMDposter();
		System.out.println(mdDto.toString());
		System.out.println(postDto.toString());
		model.addAttribute("md_list", mdDto);
		model.addAttribute("md_poster_li", postDto);
		
		return "/main/mdrecommend";
	}

}
