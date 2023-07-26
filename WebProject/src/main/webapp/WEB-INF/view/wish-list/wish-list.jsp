<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>임시 위시리스트 페이지</title>
<link rel="stylesheet" href="/resources/search-list/css/search-list.css">
<%@ include file="../common/commonCss.jsp"%>
<script src="https://kit.fontawesome.com/ad03eb7935.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div id="search-list-container">
		<div id="search-product-container">
			<div id="product">
				<c:forEach var="product" items="${userWishlist }">
					<div class="product-module" data-pk="${product.performance_code }">
						<img class="product-module-poster" src="${product.poster }"
							alt="포스터">
						<div class="product-module-top">
							<div class="place">
								<span>서울/종로구</span>
							</div>
							<button class="likeBtn" data-like="${product.performance_code }"
								id="${product.performance_code }"
								onclick="toggleLike(event, '${product.performance_code }')">
								<i class="fa-regular fa-heart" style="color: #000000;"></i>
								<!-- 로그인 상태가 아닐때는 하트가 안보여야함 -->
							</button>
						</div>
						<div class="product-module-title">${product.performance_name }</div>
						<div class="product-module-bottom">
							<div class="star">
								<i class="fa-solid fa-star" style="color: #ffcb0f;"></i>4.8(828)
							</div>
							<div class="price">${product.performance_price }원</div>
						</div>
					</div>
				</c:forEach>
			</div>
			<div class="scroll-up">
				<img src="https://timeticket.co.kr/mobile_img/btn_scrollup.png"
					alt="">
			</div>
		</div>
	</div>
	<%@ include file="../common/footer.jsp"%>
	<%@ include file="../common/commonJs.jsp"%>
	<script src="/resources/search-list/js/search-list.js"></script>
</body>
</html>