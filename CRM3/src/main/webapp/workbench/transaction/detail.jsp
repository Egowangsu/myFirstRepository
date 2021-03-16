<%@ page import="com.wyx.crm.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.wyx.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
	List<DicValue> stageList= (List<DicValue>) application.getAttribute("stage");  //数据字典，type-lsit
	Map<String,String> pMap= (Map<String, String>) application.getAttribute("pMap"); //阶段-可能性的缓存
	Set<String> set=pMap.keySet();
	int point=0;
	for(int i=0;i<stageList.size();i++){
				DicValue dic=stageList.get(i);  //list根据下标取值
		String stage=dic.getValue();   //value值就是stage
		String possibility=pMap.get(stage);
		if("0".equals(possibility)){
			point=i;      //拿的断点下标，也就是可能性为0和不为0的分界下标
			break;
		}
	}
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });

		//页面加载完毕后展现历史交易列表
		showHistoryList();
	});
	showHistoryList=function () {
		$.ajax({
			url:"workbench/transaction/getHistoryListByTranId.do",
			data:{
				"tranId":"${t.id}"  //当前交易的id，已经存入到request域中，用el表达式取
			} ,
			type:"get",   //增删改、登陆操作用post，正常取值用get
			dataType:"json" ,
			success:function (data) {
				$.each(data,function (i,n) {
				$("#history-body").append(
					'<tr>'+
					'<td>'+n.stage+'</td>'+
					'<td>'+n.money+'</td>'+
					'<td>'+n.possibility+'</td>'+
					'<td>'+n.expectedDate+'</td>'+
					'<td>'+n.createTime+'</td>'+
					'<td>'+n.createBy+'</td>'+
					'</tr>'
				)
				})
			}
		})
	}
	changeStage=function (stage,i) {
		$.ajax({
			url:"workbench/transaction/changeStage.do",
			data:{
				id:"${t.id}",
				stage:stage,
				money:"${t.money}",
				expectedDate:"${t.expectedDate}"
			} ,
			type:"get",   //增删改、登陆操作用post，正常取值用get
			dataType:"json" ,
			success:function (data) {
				if(data.success){
					//更改阶段后，改变详细页上的stage,possibility,editBY,editTime
					$("#stage").html(data.t.stage);
					$("#possibility").html(data.t.possibility);
					$("#editBy").html(data.t.editBy);
					$("#editTime").html(data.t.editTime);
					//更新阶段的图标
					changeIcon(stage,i);
				}else{
					alert("更改失败");
				}
			}
		})
	}
	changeIcon=function (stage,index) {
			//alert("当前所处阶段是"+stage);
			//alert("当前所处阶段下标是"+index);
		//简单类型=基本数据类型+String
		//简单类型转为js变量，必须加引号 ,如下point;
			//alert("当前的分界点下标是"+"<%=point%>");
			var possibility=$("#possibility").html;//当前的可能性
			var point="<%=point%>";
			if(possibility=="0"){  //当前可能性等于0
				for(var i=0;i<point;i++){ //遍历前7个可能性不为0的图标
					//黑圈
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-record mystage");
					$("#"+i).css("color","#000000")
				}
				for(var i=point;i<<%=stageList.size()%>;i++){ //遍历后两个可能性为0的图标
						if(index==i){ //如果是当前阶段
							//红叉
							$("#"+i).removeClass();
							$("#"+i).addClass("glyphicon glyphicon-remove mystage");
							$("#"+i).css("color","#FF0000");
						}else{
							//黑叉

							$("#"+i).removeClass();
							$("#"+i).addClass("glyphicon glyphicon-remove mystage");
							$("#"+i).css("color","#000000");
						}
				}

			}else{  //当前可能性不等于0

				for(var i=0;i<point;i++){ //遍历前七个
					if(i<index){
						//绿圈

						$("#"+i).removeClass();
						$("#"+i).addClass("glyphicon glyphicon-ok-circle mystage");
						$("#"+i).css("color","#90F790");
					}else if(i==index){
						//等待绿圈
						$("#"+i).removeClass();
						$("#"+i).addClass("glyphicon glyphicon-map-marker mystage");
						$("#"+i).css("color","#90F790");
					}else{
						//黑圈
						$("#"+i).removeClass();
						$("#"+i).addClass("glyphicon glyphicon-record mystage");
						$("#"+i).css("color","#000000");
					}
				}

				for(var i=point;i<<%=stageList.size()%>;i++){
					//黑叉
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-remove mystage");
					$("#"+i).css("color","#000000");
				}


			}

	}
	
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${t.customerId}-${t.name}<small>￥${t.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			//获取当前的stage和可能性
			Tran t= (Tran) request.getAttribute("t");
			String currentStage=t.getStage();
			String currentPossibility=pMap.get(currentStage);
			if("0".equals(currentPossibility)){
				//说明是在第八或第九个阶段，可能性都为0
				for(int i=0;i<stageList.size();i++){
					DicValue dicValue=stageList.get(i);
					String listStage=dicValue.getValue();
					String listPossibility=pMap.get(listStage);
					if("0".equals(listPossibility)){
						//分两种情况，要么当前阶段是在第八个，则是红叉，要么第九个是红叉
						if(listStage.equals(currentStage)){//只需判断是不是当前阶段就好了
							//是在当前阶段
							//这里是红叉
							%>
						<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			  			data-toggle="popover" data-placement="bottom"
			  			data-content="<%=dicValue.getValue()%>" style="color: #FF0000;"></span>
						 ---------
		       <%
						}else{
							//不是当前阶段
							//这里是黑叉
						%>
							<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			  				 data-toggle="popover" data-placement="bottom"
			  				 data-content="<%=dicValue.getValue()%>" style="color: #000000;"></span>
							---------
								 <%
						}
					}else{

						//这里是前七个，都是黑圈
						%>
						<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
			  			 data-toggle="popover" data-placement="bottom"
			  			 data-content="<%=dicValue.getValue()%>" style="color: #000000;"></span>
						---------

						 <%
					}
				}

			}else{   //现阶段可能性不为0
				int index=0;
				for(int i=0;i<stageList.size();i++){
					DicValue dicValue = stageList.get(i);
					String listStage=dicValue.getValue();
					String listPossibility=pMap.get(listStage);
					if(listStage.equals(currentStage)) {
						index=i;  //标记当前位置


					}
				}

				for(int i=0;i<stageList.size();i++){
					DicValue dicValue = stageList.get(i);
					String listStage=dicValue.getValue();
					String listPossibility=pMap.get(listStage);
					if("0".equals(listPossibility)){
						//这里是后两个，全部黑叉

						  %>
				<span  id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
			   data-toggle="popover" data-placement="bottom"
			   data-content="<%=dicValue.getValue()%>" style="color: #000000;"></span>
				---------
			<%
					}else{
						if(i<index){
							//如果当前下标小于所属阶段的下标，则全是绿圈
					%>
				<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-ok-circle mystage"
					   data-toggle="popover" data-placement="bottom"
					   data-content="<%=dicValue.getValue()%>" style="color: #90F790;"></span>
				---------
					<%
						}else if(index==i){
							//如果当前下标等于所属阶段的下标，则是
				%>
				<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-map-marker mystage"
			   data-toggle="popover" data-placement="bottom"
			   data-content="<%=dicValue.getValue()%>" style="color: #90F790;"></span>
				---------
				<%
						}else{
							//如果当前下标大于所属阶段的下标，则全是灰圈
					%>
					<span id="<%=i%>" onclick="changeStage('<%=listStage%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
			  		 data-toggle="popover" data-placement="bottom"
			  		 data-content="<%=dicValue.getValue()%>" style="color: #000000;"></span>
					---------
			<%
						}

					}


				}

			}






		%>
<%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>--%>
<%--		-------------%>
<%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>--%>
<%--		-------------%>
<%--		<span class="closingDate">2010-10-10</span>--%>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.customerId}-${t.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${t.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.type}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possibility">${possibility}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.activityId}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.createBy}</b><small style="font-size: 10px; color: gray;">2017-01-18 10:10:10</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">zhangsan&nbsp;&nbsp;</b><small  id="editTime" style="font-size: 10px; color: gray;">2017-01-19 10:10:10</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${t.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					&nbsp;${t.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="history-body">
<%--						<tr>--%>
<%--							<td>资质审查</td>--%>
<%--							<td>5,000</td>--%>
<%--							<td>10</td>--%>
<%--							<td>2017-02-07</td>--%>
<%--							<td>2016-10-10 10:10:10</td>--%>
<%--							<td>zhangsan</td>--%>
<%--						</tr>--%>
<%--						<tr>--%>
<%--							<td>需求分析</td>--%>
<%--							<td>5,000</td>--%>
<%--							<td>20</td>--%>
<%--							<td>2017-02-07</td>--%>
<%--							<td>2016-10-20 10:10:10</td>--%>
<%--							<td>zhangsan</td>--%>
<%--						</tr>--%>
<%--						<tr>--%>
<%--							<td>谈判/复审</td>--%>
<%--							<td>5,000</td>--%>
<%--							<td>90</td>--%>
<%--							<td>2017-02-07</td>--%>
<%--							<td>2017-02-09 10:10:10</td>--%>
<%--							<td>zhangsan</td>--%>
<%--						</tr>--%>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>