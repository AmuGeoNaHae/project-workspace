function toggleLike(event, buttonId) {
    event.stopPropagation();

    let $button = $('#'+buttonId);
    $button.toggleClass('liked');
    
    let $icon = $button.find('i.fa-heart');
    
    if ($button.hasClass('liked')) {
      $icon.removeClass('fa-regular').addClass('fa-solid').css('color','#e41b1b');
      console.log("button",$button.attr('id'));

      $.ajax({
			url: "addwishlist",
			type:"POST",
			data: {
				member_code : 51,
				performance_code : buttonId 
				},
			success: function (response) {
            // 서버로부터의 성공 응답 처리 (필요한 경우)
            console.log("찜 추가 성공:", response);
            }
		});
	} else {
      $icon.removeClass('fa-solid').addClass('fa-regular').css('color','#000000');
      $.ajax({
		  url : "delewishlist",
		  type: "POST",
		  data: {
				member_code : 51,
				performance_code : buttonId 
				},
		  success: function (response) {
            // 서버로부터의 성공 응답 처리 (필요한 경우)
           console.log("찜 삭제 성공:", response);
           }
	  });
    }
  };

$(document).ready(function(){
	$('.product-module').on('click', function(){
		console.log($(this).data('pk'));
		
		if ($(this).data('category') !== "전시") {
	    		location.href = '/product/product-detail?performance_code=' + $(this).data('pk');				
			} else {
				location.href = '/product/product-detail-ex?performance_code=' + $(this).data('pk');				
			}
		
	})
});


$(document).ready(function(){
	$('.submenu').on('click', function(){
		const urlParams = new URLSearchParams(window.location.search);
        const textContent = $(this).text();
        //console.log(textContent);
        // 파라미터 중 'area'를 바꾸기
        urlParams.set('area',textContent);
        
        // 새로운 URL 생성
        const newUrl = window.location.pathname + '?' + urlParams.toString();
        //console.log(newUrl);
        //console.log(urlParams);
        //console.log(window.location.pathname);
        // 새로운 URL로 리다이렉트
        window.location.href = newUrl;
		
	})
});
