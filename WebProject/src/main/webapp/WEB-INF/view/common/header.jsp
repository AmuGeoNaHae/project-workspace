<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> <!-- 시큐리티 -->
<div class="header-container">

	<!-- 상단 로고, 로그인 / 회원가입 / 고객센터 -->
	<div id="main-title">
		<div></div>
		<div id="main-logo">
			<img src="/resources/common/image/logo.png" alt="로고">
		</div>
		<div id="main-user-nav">
			<div></div><!-- 로고 좌측 여백입니다 -->
			<!-- 로그인 성공시 로그아웃, 마이페이지, 고객센터로 바뀌어야함 -->
			<ul id="user-nav">
				<li class="user-nav-item">
				
					<!-- 권한이 있는 사용자 (로그인) / 권한별로 멤버, 관리자로 나눌수도 있음 : hasRole('admin') -->					 
					 <sec:authorize access="isAnonymous()">
					    <button class="user-nav-btn" id="login" onclick="location.href='/login'">로그인</button>
						<button class="user-nav-btn" id="join" onclick="location.href='/user/newJoin'">회원가입</button>
					</sec:authorize>
					<!-- 권한이 없는 사용자 (비로그인) -->
					<sec:authorize access="isAuthenticated()">
					    <button class="user-nav-btn" id="mypage" onclick="location.href='/mypage'">마이페이지</button>
					    <button class="user-nav-btn" id="logout" onclick="location.href='/logout'">로그아웃</button>
					</sec:authorize>
					
				</li>
				
				<li class="user-nav-item">
					<button class="user-nav-btn" id="as">고객센터</button>
				</li>
			</ul>
		</div>
	</div>
	<!-- 네비바 -->
	<div id="main-nav">
		<div id="main-nav-item">
			<div class="main-nav-btn">
				<button class="menu-btn" data-id="performance">공연</button>
				<div class="dropdown-content"><!-- 추후 드롭다운 메뉴를 구현할 때 사용 예정입니다 -->
					<div class="genrenm" id="drama">연극</div>
					<div class="genrenm" id="musical">뮤지컬</div>
					<div class="genrenm" id="magic">서커스/마술</div>
					<div class="genrenm" id="concert">콘서트</div>
				</div>
			</div>
			<div class="main-nav-btn">
				<button class="menu-btn" data-id="exhibition">전시</button>
			</div>
			<div class="main-nav-btn">
				<button class="menu-btn" data-id="music">음악</button>
				<div class="dropdown-content">
					<div class="genrenm" id="koreanMusic">국악</div>
					<div class="genrenm" id="classic">클래식</div>
					<div class="genrenm" id="complex">복합</div>
				</div>
			</div>
			<div class="main-nav-btn">
				<button class="menu-btn" data-id="kids">키즈</button>
			</div>
		</div>
		<div class="md-choice">MD 추천작</div>
		<div class="search">
			<!-- 나중에 input 태그 form 추가하세요 -->
			<form id="search" action="">
				<input type="text" id="search-box">
				<input type="submit" id="search-submit" value=""/>
			</form>
		</div>
	</div>

</div>