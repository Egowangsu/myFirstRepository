<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">


	$(function () {
		//为创建按钮自行创建一个事件，打开动态窗口
		$("#addBtn").click(function () {

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

				//先过后台，拿取数据，在下拉框显示数据
				$("#create-owner").empty();
				$.ajax({
					url:"workbench/activity/getUserList.do",
					data:{} ,
					type:"get",
					dataType:"json" ,			//List<user> uList
					success:function (data) {  //传过来的是一个json数组[{用户1}，{用户2}，...]
						//遍历出来的每一个n都是一个user对象
						var html="<option></option>";
						$.each(data,function (i,n) {
							//$("#create-marketActivityOwner").append("<option value='"+n.id+"'>"+n.name+"</option>");
							html+="<option value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#create-owner").html(html);
					}

				})

				//EL表达式用在html中正常，在js中就需要用引号括起来，当作字符串才能使用

				var id ="${user.id}";
				$("#create-owner").val(id);   //有问题



				//将创建下拉框默认选中登陆者的name

				/*
                    * 操作模态窗口的方法
                    * 先获得模态窗口的jQuery对象，调用modal方法，为该方法传递参数
                    * show ：打开模态窗口  hide:关闭模态窗口
                    */
				$
				$("#createActivityModal").modal("show");

			})


		});
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do" ,
				data:{
					owner:$.trim($("#create-owner").val()),
					name:$.trim($("#create-name").val()),
					startDate:$.trim($("#create-startDate").val()),
					endDate:$.trim($("#create-endDate").val()),
					cost:$.trim($("#create-cost").val()),
					description:$.trim($("#create-description").val())

				} ,
				type:"post",   //增删改、登陆操作用post，正常取值用get
				dataType:"json" ,
				success:function (data) {
					//只是一个查询操作，返回成功或失败即可
					/*
							{success:false/true}

					 */
					if(data.success){
						//添加成功
						//刷新局部页面
						//清空模态窗口中的数据,先得拿到from表单的jQuery对象
						//jQuery对象虽然有reset方法，但没有效果，用原生的dom对象的reset可以
						//这个数组里只有一个表单对象
						//$("#activityAddForm")[0].reset();
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						window.alert("信息添加成功")
						pageList(1
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert("市场活动添加失败!")
					}
				}
			})


		})
             //市场活动页面加载完毕后应该刷新一次数据
			pageList(1,2);
		    //查询按钮添加点击事件,也要刷新一次数据
			$("#searchBtn").click(function () {
				//点击搜索后存储搜索数据到隐藏域中
				$("#hidden-name").val($.trim($("#search-name").val()))
				$("#hidden-owner").val($.trim($("#search-owner").val()))
				$("#hidden-startDate").val($.trim($("#search-startDate").val()))
				$("#hidden-endDate").val($.trim($("#search-endDate").val()))
			pageList(1,2);
			})

		//为全选的复选框注册时间

			//动态生成的元素用传统的绑定方式绑定不了事件
				//解决方法，用$(document).on(绑定的事件,绑定事件的标签,回调函数)
		$(document).on("click",$("input[name='danxuan']"),function (e) {
			//如果单选框的数量等于单选框选中(checked)的数量，则返回的是true，则selectAll就打上勾了
			//这里查找被选中的单选框利用了表单筛选器：checked
			$("#selectAll").prop("checked",$("input[name='danxuan']").length==$("input[name='danxuan']:checked").length)

		})


		//执行删除操作,未删除按钮绑定事件
		$("#deleteBtn").click(function () {
			//找出被选中的单选框
			var $checked=$("input[name='danxuan']:checked")
			//发送ajax请求，将选中的标签的value， 也就是id值传过去,应为有多个
			if($checked.length==0){
				alert("请选择需要删除的记录")
			}else{
				if(confirm("确定要删除吗?")){    //提示窗口
					//应为传入的是多个id，用传统的传入方式?id=xxx&id=xxx 比较方便
					//若是json方式，就会比较麻烦，key需要唯一，下面拼接字符参数
					var param=""
					for(var i = 0; i <$checked.length ; i++) {
						param+="id="+$checked[i].value+"&"
					}
					param=param.substring(0,param.length-1);
					$.ajax({
						url: "workbench/activity/delete.do",
						data:param,
						type:"post",   //增删改、登陆操作用post，正常取值用get
						dataType:"json" ,
						success:function (data) {
							if(data.success){
								alert("删除成功");
								//删除成功后需要刷新局部页面
								pageList(1
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除失败")
							}
						}
					})
				}

			}



		})


		//为修改按钮添加事件,要先判断勾选情况
			$("#editBtn").click(function () {
				var $checked=$("input[name=danxuan]:checked");
				if($checked.length==0){
					alert("请选择需要修改的数据")
					return ;
				}else if($checked.length>1){
					alert("一次只能修改一条数据，请重新选择")
					return;
				}
				//ajax请求
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do" ,
					data:{
						id:$checked.val(),
					} ,
					type:"get",   //增删改、登陆操作用post，正常取值用get
					dataType:"json" ,
					success:function (data) {
						//返回的数据需要user对象的信息和市场活动
						//因为用户名称是一个下拉框，所有的名字要显示出来，所以用user对象集合
						//{"uList":{对象1，对象2...},"activity"：市场活动}
						$.each(data.uList,function (i,n) {
							$("#edit-owner").append("<option value='"+n.id+"'>"+n.name+"</option>")
						})
						$("#edit-id").val(data.activity.id);
						$("#edit-Name").val(data.activity.name);
						$("#edit-owner").val(data.activity.owner);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-description").val(data.activity.description);
					}
				})

							//打开模态窗口
					$("#editActivityModal").modal("show");

			})

				//为更新按钮添加事件
			$("#update").click(function () {

				$.ajax({
					url:"workbench/activity/update.do" ,
					data:{
						id:$.trim($("#edit-id").val()),
						owner:$.trim($("#edit-owner").val()),
						name:$.trim($("#edit-Name").val()),
						startDate:$.trim($("#edit-startDate").val()),
						endDate:$.trim($("#edit-endDate").val()),
						cost:$.trim($("#edit-cost").val()),
						description:$.trim($("#edit-description").val())

					} ,
					type:"post",   //增删改、登陆操作用post，正常取值用get
					dataType:"json" ,
					success:function (data) {
						//只是一个查询操作，返回成功或失败即可
						/*
                                {success:false/true}

                         */
						if(data.success){

							//关闭模态窗口
							$("#editActivityModal").modal("hide");
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						}else{
							alert("市场活动修改失败!")
						}
					}
				})


			})

	})
	//局部刷新请求
	function pageList(pageNo,pageSize){
		//每次执行前将全选复选框的钩去掉
		$("#selectAll").prop("checked",false);
		//每次执行前将原来的查询结果清空
		$("#search-body").empty();
		//先获取藏在隐藏于的搜索数据，避免没有搜索点下一页就查询的情况
		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hideen-owner").val()))
		$("#search-startDate").val($.trim($("#hidden-startDate").val()))
		$("#search-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val()),

			} ,
			type:"get",   //增删改、登陆操作用post，正常取值用get
			dataType:"json" ,
			success:function (data) {
				//传回来的数据{ "total":总条数   ，"pageList":{"name":...}        }
			$.each(data.dataList,function (i,n) {
					//一个n就是一个activity对象
				$("#search-body").append(
				'<tr class="active">'+
					'<td><input type="checkbox"  name="danxuan" value="'+n.id+'"/></td>'+
					'<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'+
					'<td>'+n.owner+'</td>'+
					'<td>'+n.startDate+'</td>'+
					'<td>'+n.endDate+'</td>'+
				'</tr>'
			   )
			}

			)
				var totalPages=Math.ceil(data.total/pageSize);
				$("#activityPage").bs_pagination({  //这是插件提供的方法，建立分页
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//回调函数，点击了下面的页数，会触发pageList函数，参数不可改动，插件提供的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				})

			}
		}

	)

	}

	
</script>
</head>
<body>
<%--	创建四个隐藏域来存储搜索框的值--%>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
					<div class="form-group">
						<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="edit-owner">

							</select>
						</div>
						<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-Name" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-startDate">
						</div>
						<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-endDate" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-cost" class="col-sm-2 control-label">成本</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-cost" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-describe" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="edit-description"></textarea>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="update">更新</button>
			</div>
		</div>
	</div>
</div>




<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner" name="1236">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="search-body">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;" >
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>