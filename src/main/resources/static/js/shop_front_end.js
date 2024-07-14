/**
 * 商城前端JS
 */

 		//載入時檢查會員是否有收藏商品，變更愛心按鈕的樣式
		$(document).ready(function() {
			checkWishlistStatus();
		});

		function checkWishlistStatus() {
			$('.product-card').each(
					function() {
						var prodNo = $(this).data('prod-no');
						var $wishlistBtn = $(this).find('.wishlist-btn i');

						$.ajax({
							type : "POST",
							url : "/shop/checkWishlistStatus",
							data : JSON.stringify({
								prodNo : prodNo
							}),
							contentType : "application/json",
							success : function(response) {

								if (response.isInWishlist) {
									$wishlistBtn.removeClass(
											'fa-regular fa-heart').addClass(
											'fa-solid fa-heart');
								} else {
									$wishlistBtn.removeClass(
											'fa-solid fa-heart').addClass(
											'fa-regular fa-heart');
								}
							},
							error : function(xhr, status, error) {
								console.error('收藏清單錯誤: ', error);
							}
						});
					});
		}

		//愛心按鈕切換新增或刪除收藏
		function toggleWishlist(button) {
			var prodNo = $(button).data("id");
			var $icon = $(button).find('i');

			$.ajax({
				type : "POST",
				url : "/shop/toggleWishlist",
				data : JSON.stringify({
					prodNo : prodNo
				}),
				contentType : "application/json",
				success : function(response) {
					if (response.success) {
						if (response.action === "added") {
							$icon.removeClass('fa-regular fa-heart').addClass(
									'fa-solid fa-heart');
							alert("已加入收藏");
						} else if (response.action === "removed") {
							$icon.removeClass('fa-solid fa-heart').addClass(
									'fa-regular fa-heart');
							alert("已移除收藏");
						}
					} else {
						alert(response.message || "操作失敗，請稍後再試。");
					}
				},
				error : function() {
					alert("發生錯誤，請稍後再試。");
				}
			});
		}
		
		
		//新增至購物車
	      function addToCart(button) {
	        var prodNo = $(button).data("id");
	        var prodQty = 1; // 假設每次添加一個商品

	        $.ajax({
	            type: "POST",
	            url: "/protected/cart/saveOrUpdateCart",
	            data: JSON.stringify({ prodNo: prodNo, prodQty: prodQty }),
	            contentType: "application/json",
	            success: function(response) {
	                if (response.success) {
	                    alert(response.message);
	                } else {
	                    alert("請先登入會員");
	                }
	            },
	            error: function(xhr) {
	                if (xhr.status === 401) {
	                    window.location.href = "/member/loginMem";
	                } else {
	                    alert("發生錯誤，請稍後再試。");
	                }
	            }
	        });
	    }