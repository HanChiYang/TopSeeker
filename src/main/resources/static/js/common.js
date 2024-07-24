    $(document).ready(function(){

        $(".bellIcon").on("click", function(event){
            event.preventDefault();
			event.stopPropagation()
            var path = $(this).data("new-src");
            $(this).attr("src", path);
            // 加載通知數據
            $.ajax({
                url: '/notification/listNewNoti',
                method: 'GET',
                success: function(data) {
					if (data) {
                    $('#notificationContent').html(data);
                    $('#notificationContainer').toggle();						
					}else {
						alert("您尚無任何通知");
					}
                },
                error: function() {
                    alert('加載通知失敗');
                }
            });

        });

        $('#notification').DataTable({
            "lengthMenu": [10],
            "searching": false, // 搜尋功能, 預設是開啟
            "paging": false, // 分頁功能, 預設是開啟
            "ordering": false, // 排序功能, 預設是開啟
            "language": {
                "processing": "處理中...",
                "loadingRecords": "載入中...",
                "lengthMenu": "顯示 _MENU_ 筆結果",
                "zeroRecords": "沒有符合的結果",
                "info": "顯示第 _START_ 至 _END_ 筆結果，共<font color='red'> _TOTAL_ </font>筆",
                "infoEmpty": "顯示第 0 至 0 筆結果，共 0 筆",
                "infoFiltered": "(從 _MAX_ 筆結果中過濾)",
                "search": "搜尋:",
                "paginate": {
                    "first": "第一頁",
                    "previous": "上一頁",
                    "next": "下一頁",
                    "last": "最後一頁"
                },
                "aria": {
                    "sortAscending": ": 升冪排列",
                    "sortDescending": ": 降冪排列"
                }
            }
        });
    });
    

