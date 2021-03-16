<%--
  Created by IntelliJ IDEA.
  User: 84089
  Date: 2021/2/25
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function () {
            var myChart=echarts.init(document.getElementById("main"));
            $.ajax({
                url:"workbench/chart/transaction/getChart.do" ,
                type:"get",   //增删改、登陆操作用post，正常取值用get
                dataType:"json" ,
                success:function (data) {

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '漏斗图',
                            subtext: '纯属虚构'
                        },
                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                // {value: 60, name: '访问'},
                                // {value: 40, name: '咨询'},
                                // {value: 20, name: '订单'},
                                // {value: 80, name: '点击'},
                                // {value: 100, name: '展现'}


                            }
                        ]
                    };
                    myChart.setOption(option);
                }
            })


        })
    </script>
</head>
<body>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
