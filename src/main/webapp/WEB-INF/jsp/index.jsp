<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script src="jquery.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="js/jquery-easyui-1.4.1/jquery.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WebSocket</title>
</head>
<body>
	<h1>test</h1>
	<input id="sendTxt" type="text">
	<button id="sentBtn">发送</button>
	<div id="recv"></div>

	<a href="http://localhost:8080/elsfk/show">分享</a>
	<button id="btn" onclick="share()">分享</button>
</body>
<script type="text/javascript">
	
	function share() {
		window.alert("确认分享？");
		 
		$("$btn").ajax({
			url : "http://localhost:8080/elsfk/show", 
			dataType : "customer", //返回格式为json  
			async : true,//请求是否异步，默认为异步，这也是ajax重要特性  
			/* data : {
				"id" : "value"
			}, //参数值   */
			type : "POST", //请求方式 get 或者post  
			beforeSend : function() {
				//请求前的处理  
			},
			success : function(req) {
				//请求成功时处理  
			},
			complete : function() {
				//请求完成的处理  
			},
			error : function() {
				//请求出错处理  
			}
		})

	}

	var websocket = new WebSocket("ws://echo.websocket.org/");
	websocket.onopen = function() {
		console.log('websocket open');
		document.getElementById("recv").innerHtml = "Connected";
	}
	websocket.onclose = function() {
		console.log('websocket close');
	}
	websocket.onmessage = function(e) {
		console.log(e.data);
		document.getElementById("recv").innerHtml = e.data;
	}
	document.getElementById("sendBtn").onclick = function() {
		var txt = document.getElementById("sendTxt").value;
		websocket.send(txt);
	};
</script>




<script type="text/javascript">
	function share() {
		var wx;
		//分享朋友圈
		wx.onMenuShareTimeline({
			title : '', // 分享标题
			link : '', // 分享链接
			imgUrl : '', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

		//分享朋友
		wx.onMenuShareAppMessage({
			title : '', // 分享标题
			desc : '', // 分享描述
			link : '', // 分享链接
			imgUrl : '', // 分享图标
			type : '', // 分享类型,music、video或link，不填默认为link
			dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});
	}
</script>
</html>