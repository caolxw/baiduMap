<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.List"%>
<%@page import="example.entry.Gps" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath %>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=IbR09r0fILQMnsRiGUMmj2jwfyzTEvod"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
	<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
	<title>海量点显示</title>
	<link rel="shortcut icon" href="img/bitbug_favicon2.ico" />
	<style type="text/css"> 
		html{height:100%} 
		body{height:100%;margin:0px;padding:0px} 
		#milkMap{height:100%} 
	</style>
</head>
<body>
	<div id="milkMap"></div>
	<script type="text/javascript"> 
		var map = new BMap.Map("milkMap",{minZoom:10}); 			// 创建地图实例并设置最小缩放等级
		map.centerAndZoom(new BMap.Point(116.404, 39.915),15); 		// 创建地图中心点和地图级别
		map.enableScrollWheelZoom(true);     						//开启鼠标滚轮缩放
		map.enableContinuousZoom();									//开启连续缩放效果
		map.enableInertialDragging();								//开启惯性拖拽效果
		//添加地图类型控件
		map.addControl(new BMap.MapTypeControl({
			mapTypes:[
	            BMAP_NORMAL_MAP,
	            BMAP_HYBRID_MAP
	        ]}));
		//添加城市列表控件
		var size = new BMap.Size(20,20);
		map.addControl(new BMap.CityListControl({
			anchor: BMAP_ANCHOR_TOP_LEFT,
	    	offset: size,
		}));
		
		if(document.createElement('canvas').getContext)	//判断当前浏览器是否支持绘制海量点
		{
			reload();
			map.addEventListener("zoomend", function(){	//缩放地图事件
				map.clearOverlays();
				reload();
			});
			
			map.addEventListener("dragend", function(){	//拖拽地图事件
				map.clearOverlays();
				reload();				
			});
			
		}else{
			alert("本浏览器不支持绘制海量点");
		}
		
		function reload(){
			var points = [];							//添加海量点数据
			var bounds = map.getBounds();
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast(); 
			
			var param = {
					"swlng" : sw.lng,
					"swlat" : sw.lat,
					"nelng" : ne.lng,
					"nelat" : ne.lat
				};
			
			$.ajax({
				type: "POST",
				url: "${pageContext.request.contextPath }/MapServlet",
				data: param,
				dataType: "json",
				success: function(jsonData){
					for(var i in jsonData){
						//console.log(jsonData[i].wgLat);
						//console.log(jsonData[i].wgLon);	
						//console.log(jsonData[i].ip);
						var point = new BMap.Point(jsonData[i].wgLon,jsonData[i].wgLat);
						points.push(point);
					}
					var options = {
				            size: BMAP_POINT_SIZE_NORMAL,
				            shape: BMAP_POINT_SHAPE_STAR,
				            color: '#d340c3'
				    }
					var pointCollection = new BMap.PointCollection(points, options);
					pointCollection.addEventListener("click",function(e){
						alert('该点的坐标为：' + e.point.lng + ',' + e.point.lat);
					});
					map.addOverlay(pointCollection);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					alert(errorThrown + ':' + textStatus); 	// 错误处理
				}
			});
		}
	</script>
</body>
</html>