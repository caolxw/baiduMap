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
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=mbkCmeVLe13mxThXLBeTET4WMUV9QwN9"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
	<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
	<title>点聚合显示</title>
	<link rel="shortcut icon" href="img/bitbug_favicon2.ico" />
	<style type="text/css"> 
		html{height:100%} 
		body{height:100%;margin:0px;padding:0px} 
		#allmap{height:100%} 
	</style>
</head>
<body>
	<div id="allmap"></div>
	<div id="info"></div>
	<script type="text/javascript">
		// 百度地图API功能
		var map = new BMap.Map("allmap");    						// 创建Map实例
		map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);  	// 初始化地图,设置中心点坐标和地图级别
		//添加地图类型控件
		map.addControl(new BMap.MapTypeControl({
			mapTypes:[
	            BMAP_NORMAL_MAP,
	            BMAP_HYBRID_MAP
	        ]}));
		map.enableScrollWheelZoom(true);     			//开启鼠标滚轮缩放
		map.addControl(new BMap.NavigationControl()); 	//添加平移缩放控件
		map.enableContinuousZoom();						//开启连续缩放效果
		map.enableInertialDragging();					//开启惯性拖拽效果
		
		//添加城市列表控件
		var size = new BMap.Size(100,20);
		map.addControl(new BMap.CityListControl({
			anchor: BMAP_ANCHOR_TOP_LEFT,
	    	offset: size,
		}));
		
		reload();							//初始化地图信息
		var opts = {    
			    width : 50,     			// 信息窗口宽度    
			    height: 20,     			// 信息窗口高度    
			    title : "ip", 				// 信息窗口标题
			    enableMessage:true			//设置允许信息窗发送短息
		}
		
		map.addEventListener("zoomend", function(){	//缩放地图事件
			var zoom = map.getZoom();
			if(zoom < 14){
				map.clearOverlays();
			}else{
				map.clearOverlays();
				reload();
			}
		});
		
		map.addEventListener("dragend", function(){	//拖拽地图事件
			var zoom = map.getZoom();
			if(zoom < 14){
				map.clearOverlays();
			}else{
				map.clearOverlays();
				reload();
			}
			
		});
		
		function reload(){
			var bounds = map.getBounds();
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast(); 
			var markers = [];
			
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
						console.log(jsonData[i].wgLat);
						console.log(jsonData[i].wgLon);	
						console.log(jsonData[i].ip);
						//var str = "(" + jsonData[i].wgLon + "," + jsonData[i].wgLat + ")<br>";
						//$("#info").append(str);
						var point = new BMap.Point(jsonData[i].wgLon,jsonData[i].wgLat);
						var marker = new BMap.Marker(point);
						markers.push(marker);					
						var content = jsonData[i].ip;
						//map.addOverlay(marker);
						addClickHandler(content,marker);
					}
					var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					alert(errorThrown + ':' + textStatus); // 错误处理
				}
			});
		}
		
		function addClickHandler(content,marker){
			marker.addEventListener("click",function(e){
				openInfo(content,e);
			});
		}
		
		function openInfo(content,e){
			var p = e.target;
			var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			map.openInfoWindow(infoWindow,point); //开启信息窗口
		}
	</script>
</body>
</html>