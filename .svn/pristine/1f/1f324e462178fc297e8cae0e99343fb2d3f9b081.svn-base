window.numin = '0';
window.onload=function(){
	login();
}
Highcharts.setOptions({ global: { useUTC: false } });
//引导参数！
var line_id = 1;
//产线介绍+运行工况(初始化)
function login(){
	$.ajax({
    	type:'POST',
    	url:'../checkLogin.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data!='0'){
//    			console.log(data);
    			initButton(data,line_id);
    			getFactories();
    			getLimo();
    			getStations();
    			initPie();
    			var it = self.setInterval("refreshFactories()",2000);
    			var li = self.setInterval("getLimo()",2000);
    			var lwt = self.setInterval("refreshlw()",2000);
    		}else{
    			alert('请先登录');
        		window.location.href='/mkcenter';
    		}
    	},
    	error:function(e){
    		alert('初始化失败，将返回主页！');
    		window.location.href='/mkcenter';
    		return;
    	}
	});
}
function getFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id=1',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		//console.log(json);
	    		for(var key in json.list){
	    			var t = '0';
	    			if(json.list[key].status=='1'){
	    				t = setTime(new Date().getTime()-json.list[key].LAST_BEGIN);
	    			}
	    			$("#fac_name").html(json.list[key].INSTRUCTION);
	    			$("#fac_inc").html(json.list[key].NAME);
	    			$("#fac_add").html(json.list[key].ADDRESS);
	    			$("#fac_ins").html(json.list[key].INFO);
	    			$("#sta_now").html(setStatus(json.list[key].STATUS));
	    			$("#sta_time").html(t);
	    			$("#sta_out").html(json.list[key].NUM_OUT);
	    			window.numin = json.list[key].NUM_IN;
	    		}
	    	},
	    	error:function(e){
	    		alert('初始化失败');
	    	}
	 })
	 //return factories; 
};
//初始化工位标签详细信息
function getStations(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getStation.do?line_id=1',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var num = parseInt(key)+1;
	    			var tag = 'L'+pad(num,4);
	    			$('#'+tag).attr("title",json.list[key].NAME+"  单位："+json.list[key].UNIT);
	    		}
	    	},
	    	error:function(e){
	    		alert('初始化失败');
	    	}
	 })
	 //return factories; 
};
//刷新工况
function refreshFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id=1',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var t = '0';
	    			if(json.list[key].status=='1'){
	    				t = setTime(new Date().getTime()-json.list[key].LAST_BEGIN);
	    			}
	    			$("#sta_now").html(setStatus(json.list[key].STATUS));
	    			$("#sta_time").html(t);
	    			$("#sta_out").html(json.list[key].NUM_OUT);
	    		}
	    	},
	    	error:function(e){
//	    		alert('获取数据失败');
	    	}
	 })
};
//向lw里添加新点
function refreshlw(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/update.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var x = json.list[key].TIMESTAMP-8*3600*1000;	    	
					var y = (json.list[key])['L0145'];
					lw.series[0].addPoint([x,y],true,true);
	    		}
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
};
//获取图表和定位点
function getLimo(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getNew.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		var pieData = [];
	    		for(var key in json.list){
	    			//console.log(json.list[key]);
	    			var a = json.list[key].L0018+json.list[key].L0067+json.list[key].L0117+json.list[key].L0121;
	    			if(window.numin=='0'){
	    				$("#sta_cos").html('0');
	    			}else{
	    				$("#sta_cos").html(window.numin/a);
	    			}
	    			$("#time").html(timestampToTime(json.list[key].TIMESTAMP-8*3600*1000));
	    			for(var i = 1;i<=145;i++){
	    				var l = "L"+pad(i,4);
	    				$("#"+l).html(((json.list[key])[l]).toFixed(2));
	    			}
	    			pie.setTitle({text: '当前给定料量<br/><b>'+json.list[key].L0091.toFixed(2)+'t</b><br/>'});
	    			pieData.push(
	    			{
	    				name : "I类砂岩", 
	    				y : json.list[key].L0098
	    			},
	    			{
	    				name : "II类砂岩", 
	    				y : json.list[key].L0099
	    			},
	    			{
	    				name : "炼钒废渣", 
	    				y : json.list[key].L0100
	    			},
	    			{
	    				name : "硫酸渣", 
	    				y : json.list[key].L0101
	    			},
	    			{
	    				name : "粉煤灰", 
	    				y : json.list[key].L0102
	    			},
	    			{
	    				name : "石灰石", 
	    				y : json.list[key].L0103
	    			}
	    			);
	    			pie.series[0].setData(pieData);
	    		}
	    	},
	    	error:function(e){
//	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};
//初始化料位图
function initlw(){
	var initData = [];
	$.ajax({
	    	type:'GET',
	    	url:'../limo/get.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		console.log(json);
	    		for(var key in json.list){
	    			initData.push({
	    				x : json.list[key].TIMESTAMP-8*3600*1000,
						y : (json.list[key])['L0145']
	    			});
	    		}
	    		lw.series[0].setData(initData);
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};
//初始化配料饼图
var pie = Highcharts.chart('percent', {
	chart: {
		spacing : [40, 0 , 40, 0],
		backgroundColor : 'transparent'
	},
	title: {
		floating:true,
		text: '当前总料量<br/><b></b><br/>',
		style:{
			color:'#cccccc'
		}
	},
	tooltip: {
//		pointFormat: '{point.x}: <b>{point.percentage:.1f}%</b>'
		formatter:function(){
			return '<b>'+this.point.name + '</b>:'+this.percentage.toFixed(2)+'<b>%</b>';
		}
	},
	credits:{
		enabled:false
	},
	plotOptions: {
		pie: {
			allowPointSelect: true,
			cursor: 'pointer',
			dataLabels: {
				enabled: false
			},
			showInLegend: true
		}
	},
	legend:{
		enabled:true,
		itemStyle:{
			color:"#cccccc"
		},
		useHTML:true
	},
	series: [{
		type: 'pie',
		innerSize: '80%',
		//data:
	}]
}, function(c) { // 图表初始化完毕后的会掉函数
	// 环形图圆心
	var centerY = c.series[0].center[1],
		titleHeight = parseInt(c.title.styles.fontSize);
	// 动态设置标题位置
	c.setTitle({
		y:centerY - titleHeight
	});
});
//初始化料位图
var lw;
function initPie(){
	lw = Highcharts.chart('box', {
		chart: {
				type: 'area',
				backgroundColor:'transparent',
				marginRight: 10,
				zoomType:'x'
		},
		title: {
			floating:true,
			text: ''
		},
		xAxis: {
				type: 'datetime',
				tickPixelInterval:70,
				maxPadding:0.5,
				labels: {
					style:{
						color:'#cccccc'
					}
				},
		},
		yAxis: {
				title: {
					text: '料位(%)',
					style:{
						color:'#cccccc'
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
				labels: {
					style:{
						color:'#cccccc'
					}
				},
//				maxPadding:0.4,
				minPadding:0.4,
				
		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:false,
			formatter: function () {
						return '<b>' + this.series.name + '</b><br/>' +
								Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
								Highcharts.numberFormat(this.y, 2);
				}
		},
		legend: {
				enabled: false
		},
		plotOptions: {
		
		series: {
			marker: {
				enabled: false
			}
		}
		},
		series: [{
				data:initlw()
		}]
	});
}

function setStatus(e){
	if(e=='1'){
		return '运行中';
	}else if(e=='0'){
		return '待机中';
	}else if(e=='-1'){
		return '待维修';
	}else{
		alert('数据有问题！');
	}
}

function setTime(e){
	var h = Math.floor(e/1000/3600);
	var m =	Math.floor((e/1000-h*3600)/60);
	var s = Math.floor(e/1000-h*3600-m*60);
	var ms = e-h*3600*1000-m*60*1000-s*1000;
	var t = h+":"+m+":"+s+":"+ms;
	return t;
}

//自动补0
function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

function timestampToTime(e){
	var date =new Date(e);
	var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}