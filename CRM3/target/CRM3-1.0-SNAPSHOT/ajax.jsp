<%--
  Created by IntelliJ IDEA.
  User: 84089
  Date: 2021/2/16
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

$.ajax({
url: ,
data:{} ,
type:"get",   //增删改、登陆操作用post，正常取值用get
dataType:"json" ,
success:function (data) {

}
})



//创建时间为当前系统时间
String createTime= DateTimeUtil.getSysTime();
//创建人为当前登录的用户
String createBy =((User)request.getSession().getAttribute("user")).getName();



//日历pick
$(function(){
$(".time").datetimepicker({
minView: "month",
language:  'zh-CN',
format: 'yyyy-mm-dd',
autoclose: true,
todayBtn: true,
pickerPosition: "bottom-left"
})
})

</body>
</html>
