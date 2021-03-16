<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" >
		$(function () {
			//将本窗口设为顶级窗口
			if(window.top!=window){
				window.top.location=window.location;
			}
			//页面加载完毕后，将用户框的内容清空
			$("#loginAct").val("");
			//光标自动闪烁
			$("#loginAct").focus();


			// 为登陆按钮添加事件
			$("#submitBtn").click(function () {
					test();
			})

			//为整个窗口添加回车登陆事件
			$(window).keydown(function (event) {
				if(event.keyCode==13){
					test();
				}
			})
		})
			//普通的自定义函数要写在$()外面
		test=function () {
			//账户密码不能为空
			//去掉前后空格用函数$.trim()
			var loginAct=$.trim($("#loginAct").val());
			var loginPwd=$.trim($("#loginPwd").val());
			if(loginAct==''||loginPwd==''){
				$("#msg").html("账户密码不能为空!");
				return false;
			}
			$.ajax({
				"url": "settings/user/login.do",
				"data":{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				} ,
				"type":"post",  //涉及到密码，用post
				"dataType":"json" ,
				"success":function (data) {
					//返回一个{"success":true/false  ,"msg":"失败原因"}
					//如果登陆成功
					if(data.success){
						window.location.href="workbench/index.jsp"
						//如果登陆失败
					}else{
						$("#msg").html(data.msg);
					}
				}
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg"> </span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>