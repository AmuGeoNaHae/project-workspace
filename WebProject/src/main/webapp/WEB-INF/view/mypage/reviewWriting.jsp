<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>�����ۼ�</title>
  <link rel="stylesheet" href="/resources/mypage/css/star.css">
</head>
<body>
	<div class="review-box">
    <h3>������ �����ϼ̳���?</h3>
    <div class="stars" id="starContainer">
      <img class="star gray" src="../resource/gray_star.PNG" alt="1��" data-rating="0">
      <img class="star gray" src="../resource/gray_star.PNG" alt="2��" data-rating="0">
      <img class="star gray" src="../resource/gray_star.PNG" alt="3��" data-rating="0">
      <img class="star gray" src="../resource/gray_star.PNG" alt="4��" data-rating="0">
      <img class="star gray" src="../resource/gray_star.PNG" alt="5��" data-rating="0">
    </div>
    <h3>������ �κ��� �����ּ���</h3>
    <textarea id="writeNote" style="resize:none;" rows="10" cols="55" form="form"></textarea>
    <h3>�������� �����ϸ� ���� ���ƿ�</h3>
    <input type="file" id="imageInput" accept="image/*" onchange="previewImage()">
    <div id="imagePreview"></div>
    <button class="submit" id="popupButton" onclick="showPopup()" type="submit" form="form">�ۼ��Ϸ�</button>
    <form action="/product/reviews" method="post" id="form"></form>
  </div>
</body>
</html>