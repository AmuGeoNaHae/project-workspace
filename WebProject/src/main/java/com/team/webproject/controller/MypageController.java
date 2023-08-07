package com.team.webproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {
	
	// 메인 겸 예매내역
	@GetMapping("/mypage")
	String gotoMypage(Model model) {
		
		return "/mypage/mypage-ticket";
	}
	
	// 쿠폰
	@GetMapping("/mypage/coupon")
	String gotoCouponPage(Model model) {
		return "/mypage/mypage-coupon";
	}
	
	// 찜목록
	@GetMapping("/mypage/wishlist")
	String gotoWishlistPage(Model model) {
		return "/mypage/mypage-wishlist";
	}
	
	// 환불
	@GetMapping("/mypage/refund")
	String gotoRefundPage(Model model) {
		return "/mypage/mypage-refund";
	}
	
	// 리뷰
	@GetMapping("/mypage/review")
	String gotoReviewPage(Model model) {
		return "/mypage/mypage-review";
	}
	
	// 나의 QnA
	@GetMapping("/mypage/qna")
	String gotoQnAPage(Model model) {
		return "/mypage/mypage-qna";
	}
	
	// 나의 정보 수정
	@GetMapping("/mypage/info")
	String gotoQInfoPage(Model model) {
		return "/mypage/mypage-info";
	}
}