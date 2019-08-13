window.onload = function(){
	login();
}
/**
 * 参数
 */
var id=new UrlSearch().id; // 实例化
var stas = new Array();
var pars = new Array();
var eners = new Array(); 
var eners_t =new Array();
var shaks = new Array();
var shaks_t = new Array();
var index;
var units = new Array();
var tmp = {};
var tmp_max ={};
var tmp_min ={};
var init=new Array(6);
var init_t=new Array(6);

var board = {};
// highchart全局无时差
Highcharts.setOptions(
		{ 
			global: { useUTC: false } 
		}
);
/**
 * 初始化左下角面板（未来考虑替换成监控视频）
 */
function getConfig(){
	board['L0068'] = 'u385_img';board['L0069'] = 'u385_img';board['L0070'] = 'u385_img';board['L0071'] = 'u385_img';board['L0072'] = 'u385_img';board['L0073'] = 'u385_img';
	board['L0074'] = 'u385_img';board['L0075'] = 'u385_img';board['L0076'] = 'u385_img';board['L0077'] = 'u385_img';board['L0078'] = 'u385_img';board['L0079'] = 'u385_img';
	board['L0080'] = 'u385_img';board['L0081'] = 'u385_img';board['L0082'] = 'u385_img';board['L0084'] = 'u389_img';board['L0085'] = 'u391_img';board['L0086'] = 'u399_img';	
	board['L0087'] = 'u398_img';board['L0088'] = 'u398_img';board['L0089'] = 'u387_img';board['L0090'] = 'u387_img';board['L0120'] = 'u390_img';board['L0121'] = 'u390_img';	
	board['L0062'] = 'u393_img';board['L0063'] = 'u393_img';board['L0067'] = 'u400_img';board['L0027'] = 'u386_img';board['L0028'] = 'u386_img';board['L0029'] = 'u386_img';	
	board['L0017'] = 'u412_img';board['L0003'] = 'u394_img';board['L0004'] = 'u394_img';board['L0005'] = 'u394_img';board['L0006'] = 'u394_img';board['L0001'] = 'u395_img';	
	board['L0116'] = 'u403_img';board['L0118'] = 'u404_img';board['L0119'] = 'u404_img';board['L0117'] = 'u406_img';board['L0139'] = 'u408_img';board['L0141'] = 'u409_img';	
	board['L0135'] = 'u402_img';board['L0134'] = 'u402_img';board['L0136'] = 'u401_img';board['L0137'] = 'u401_img';board['L0143'] = 'u407_img';board['L0144'] = 'u411_img';
	board['L0145'] = 'u405_img';
}
function setChosen(e){
	$("#"+board[e]).attr("src",'images/board/u386.png');
}
function setUnchosen(e){
	$("#"+board[e]).attr("src",'images/board/u385.png');
}
// 读取地址栏Request参数
function UrlSearch() {
   var name,value; 
   var str=location.href; // 取得整个地址栏
   var num=str.indexOf("?") 
   str=str.substr(num+1); // 取得所有参数 stringvar.substr(start [, length ]

   var arr=str.split("&"); // 各个参数放到数组里
   for(var i=0;i < arr.length;i++){ 
	   num=arr[i].indexOf("="); 
   if(num>0){ 
       name=arr[i].substring(0,num);
       value=arr[i].substr(num+1);
       this[name]=value;
   } 
  } 
} 
//全局刷新
function refresh(){
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/getNew.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		for(var t in json.list[0]){
    			//console.log(t);
    			tmp[t]= (json.list[0])[t];
    		}
    		var date = new Date(json.list[0].TIMESTAMP-8*3600*1000);
    		$("#clock").html(date.format("yyyy-MM-dd hh:mm:ss"));
    	},
    	error:function(e){
//    		alert('获取数据失败');
    	}
	});
}
Date.prototype.format =function(format)
{
	var o = {
	"M+" : this.getMonth()+1, //month
	"d+" : this.getDate(), //day
	"h+" : this.getHours(), //hour
	"m+" : this.getMinutes(), //minute
	"s+" : this.getSeconds(), //second
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter
	"S" : this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(format))
		format = format.replace(RegExp.$1,RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
	return format;
}
function login(){
	$.ajax({
    	type:'POST',
    	url:'../checkLogin.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data!='0'){
    			initButton(data,id);    			
    			initPage();  			
    			refresh();
    			//console.log(tmp);
    			getConfig();
    			initchart();
    			self.setInterval("refresh()",2000);
    			
    		}else{
    			alert('请先登录');
        		window.location.href='/mkcenter';
    		}
    	},
    	error:function(e){
    		alert('初始化失败，将返回主页！');
    		window.location.href='/mkcenter';
    	}
	});
}
// 默认配置页面初始化
function initPage(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id='+id,
	    	timeout:2000,
	    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			$("#pageName").html(json.list[key].INSTRUCTION);
	    			index = (json.list[key].ITEN).toLowerCase();
//	    			console.log(json.list[key].ITEN);
	    		}
	    	},
	    	error:function(e){
	    		alert('初始化失败');
	    		console.log(3);
	    	}
	 });
	// 工位(下拉框)初始化
	$.ajax({
    	type:'GET',
    	url:'../limo/getStation.do?line_id='+id,
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#u434_input").append('<option value=""></option>');
    		for(var key in json.list){
    			$("#u434_input").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			tmp_max[json.list[key].STAT_ID] = json.list[key].MAX_THRESHOLD;
    			tmp_min[json.list[key].STAT_ID] = json.list[key].MIN_THRESHOLD;
    			units[json.list[key].STAT_ID] = json.list[key].UNIT;
    		}
//    		$("#u434_input").append('<option value="out">总料位反馈值</option>');
    	},
    	error:function(e){
    		alert('初始化失败');
    		console.log(4);
    	}
	});
	// 类型(下拉框)初始化
	$("#u439_input").append('<option value=""></option>');
	$("#u439_input").append('<option value="1">折线图</option>');
	$("#u439_input").append('<option value="2">仪表盘</option>');
	$("#u439_input").append('<option value="3">VA图</option>');
	//var code = ENERS;
	// 默认配置导入
	$.ajax({
    	type:'GET',
    	url:'../board/getConfig.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		console.log(1);
    		var json =  eval("("+data+")");
    		for(var key in json.list){
    			if(json.list[key].CODE=='ENERS'){
    				eners = (","+json.list[key].STAS).split(',');
    				eners_t = (","+json.list[key].PARS).split(',');
    				console.log(eners);
    				//eners是工位id，eners_t是在前端的位置顺序，从后台拿出
    			}else if(json.list[key].CODE=='SHAKS'){
    				shaks = (","+json.list[key].STAS).split(',');
    				shaks_t = (","+json.list[key].PARS).split(',');
    			}else if(json.list[key].CODE=='init'){
    				init = (json.list[key].STAS).split(',');
    				init_t = (json.list[key].PARS).split(',');
    			}

    		}
    		console.log(init);
//    		$("#u434_input").append('<option value="out">总料位反馈值</option>');
    	},
    	error:function(e){
    		alert('初始化失败');
    		console.log(2);
    	}
	});
};
//box->list
function setNum(e){
	var g = $(e).children().find("span").attr("id");
	//console.log(g); 这个就是1-6的标识
	$("#num").html(g);
	$("#u434_input").val(stas[g]);
	$("#u439_input").val(pars[g]);
}

function reset(){
	$("#num").html('');
	$("#u434_input").val('');
	$("#u439_input").val('');
	ruinAll();
	for(var i = 1;i<=6;i++){
		$("#"+i).html(i+"");
	}
	for(var key in board){
		setUnchosen(key);
	}
	stas=[];
	pars=[];
}


function setBox(){
	var type = $("input[name='type']:checked").val();
	if(type=='ener'){
		stas = eners;
		//console.log(eners);
		pars = eners_t;
		for(var i = 1;i<=6;i++){
			var s = $("#u434_input option[value='"+stas[i]+"']").text();
			//console.log(stas[i]);  //工位点id
			//console.log(s); //工位名
			$("#"+i).html('<b>'+s+'</b>');//将工位名称显示在小面板p属性上
			var tg = i+422;       
			$("#u"+tg).attr("title",s);//将工位名作为attr显示在div上，当鼠标悬浮时有显示
		}
		for(var i=1;i<=6;i++){
			var t =pars[i];
			switch(t){
				case '1':
					initSpline("container"+i,stas[i]);	break;
				case '2':
					initSpeed("container"+i,stas[i]);	break;
				case '3':
					initVA("container"+i,stas[i]); break;
				default:
					break;
			}
			setChosen(stas[i]);
		}

	}else if(type=='shak'){
		stas = shaks;
		console.log(stas);
		pars = shaks_t;
		for(var i = 1;i<=6;i++){
			var s = $("#u434_input option[value='"+stas[i]+"']").text();
			$("#"+i).html('<b>'+s+'</b>');
			var tg = i+422;
			$("#u"+tg).attr("title",s);
		}
		for(var i=1;i<=6;i++){
			var t =pars[i];
			switch(t){
				case '1':
					initSpline("container"+i,stas[i]);	break;
				case '2':
					initSpeed("container"+i,stas[i]);	break;
				case '3':
					initVA("container"+i,stas[i]); break;
				default:
					break;
			}
			setChosen(stas[i]);
		}
	}else if(type=="init"){
		stas = init;
		pars = init_t;
		for(var i = 1;i<=6;i++){
			var s = $("#u434_input option[value='"+stas[i-1]+"']").text();
			$("#"+i).html('<b>'+s+'</b>');
			var tg = i+422;
			$("#u"+tg).attr("title",s);
		}
		for(var i=1;i<=6;i++){
			var t =pars[i-1];
			switch(t){
				case '1':
					initSpline("container"+i,stas[i-1]);	break;
				case '2':
					initSpeed("container"+i,stas[i-1]);	break;
				case '3':
					initVA("container"+i,stas[i-1]); break;
				default:
					break;
			}
			setChosen(stas[i]);
		}
		
	}
	
}

function initchart(){
	
	stas = eners;
	//console.log(eners);
	pars = eners_t;
	for(var i = 1;i<=6;i++){
		var s = $("#u434_input option[value='"+stas[i]+"']").text();
		//console.log(stas[i]);  //工位点id
		//console.log(s); //工位名
		$("#"+i).html('<b>'+s+'</b>');//将工位名称显示在小面板p属性上
		var tg = i+422;       
		$("#u"+tg).attr("title",s);//将工位名作为attr显示在div上，当鼠标悬浮时有显示
	}
	for(var i=1;i<=6;i++){
		var t =pars[i];
		switch(t){
			case '1':
				initSpline("container"+i,stas[i]);	break;
			case '2':
				initSpeed("container"+i,stas[i]);	break;
			case '3':
				initVA("container"+i,stas[i]); break;
			default:
				break;
		}
		setChosen(stas[i]);
	}
}
function setEner(){
	reset();
	$("#u434_input").attr("disabled",true);
	$("#u439_input").attr("disabled",true);
}
function setShak(){
	reset();
	$("#u434_input").attr("disabled",true);
	$("#u439_input").attr("disabled",true);
}
function setInit(){
	reset();
	$("#u434_input").removeAttr("disabled");
	$("#u439_input").removeAttr("disabled");
}
function setTime(e){
	var h = Math.floor(e/1000/3600);
	var m =	Math.floor((e/1000-h*3600)/60);
	var s = Math.floor(e/1000-h*3600-m*60);
	var ms = e-h*3600*1000-m*60*1000-s*1000;
	var t = h+":"+m+":"+s+":"+ms;
	return t;
}

//销毁单个图表
function ruin(e){
	$("#"+e).highcharts().destroy();
}
function ruinAll(){
	for(var i = 1;i<=6;i++){
		if($("#container"+i).highcharts()!=null){
			$("#container"+i).highcharts().destroy();
		}
	}
}
function SplineData(sta){
	var initData = [];
	$.ajax({
	    	type:'GET',
	    	url:'../'+index+'/get.do',
	    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			initData.push({
	    				x : json.list[key].TIMESTAMP-8*3600*1000,
						y : (json.list[key])[sta]
	    			});
	    		}
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 });
	return initData;
}
//VA图初始
function initVA(e,sta){
	var title = '<b>'+$("#u434_input option[value='"+sta+"']").text()+'</b><br/>'+tmp[sta].toFixed(2)+'<br/>'+units[sta];
	var min = tmp_min[sta];
	var max = tmp_max[sta];
	var lefting = min-(max-min)*0.1;
	var righting = max + (max-min)*0.1;
	var i = new Array();
	i.push(tmp[sta]);
	var chart = Highcharts.chart(e,{
	chart: {
		type: 'gauge',
		height: 250,
		backgroundColor:'transparent',
	},
	title:{
			text:null
		},
	credits: {
			enabled: false
	},
	pane: [{
		startAngle: -60,
		endAngle: 60,
		background: null,
		center: ['48%', '140%'], //仪表中心
		size: 500
	}],
	yAxis: [{
		min: lefting,
		max: righting,
		minorTickPosition: 'outside',
		tickPosition: 'outside',
		tickAmount:'7',
		tickColor:'#cccccc',//主刻度线   副刻度线minorColor
		tickWidth: 4,
		labels: {
			rotation: 'auto',
//			distance: 20,
			style:{
				color:'#cccccc'
			}
		},
		plotBands: [
		{//红色左
			from: -999999,
			to: lefting,
			color: '#DC143C',
			innerRadius: '100%',
			outerRadius: '105%'
		},{//黄色左
			from: lefting,
			to: min,
			color: '#DDDF0D',
			innerRadius: '100%',
			outerRadius: '105%'
		},{//黄色右
			from: max,
			to: righting,
			color: '#DDDF0D',
			innerRadius: '100%',
			outerRadius: '105%'
		},{//红色右
			from: righting,
			to: 999999,
			color: '#DC143C',
			innerRadius: '100%',
			outerRadius: '105%'
		}],
		pane: 0,
		title: {//标题字体设置
			text: title,
			y: -30,
			style:{
				color:'#cccccc',
				fontSize:'15px',
				fontFamily:'微软雅黑'
			}
		}
	}],
	plotOptions: {
		gauge: {
			dataLabels: {
				enabled: false
			},
			dial: {
				radius: '100%'
			}
		}
	},
	series: [{
		data:i,
		yAxis: 0,
		tooltip:{
			
		}
	}]
	}, function (chart) {
	var m = setInterval(function () {
		if (chart.series) { // the chart may be destroyed
			var left = chart.series[0].points[0];
			var new_title = '<b>'+$("#u434_input option[value='"+sta+"']").text()+'</b><br/>'+tmp[sta].toFixed(2)+'<br/>'+units[sta];
//			chart.title.update({text:new_title,useHTML:true});
			chart.yAxis[0].setTitle({text:new_title},true)
			left.update(tmp[sta], true);	
		}
	}, 2000);
	});
}
function initSpeed(e,sta){
	var title = $("#u434_input option[value='"+sta+"']").text();
	var min = tmp_min[sta];
	var max = tmp_max[sta];
	var lefting = min-(max-min)*0.1;
	var righting = max + (max-min)*0.1;
	var warn_lef = min-(max-min)*0.2;
	var warn_rig=  max + (max-min)*0.2;
	var i = new Array();
	//console.log(tmp);
	//console.log(sta);
	//console.log(tmp[sta]);
	i.push(parseFloat(tmp[sta].toFixed(2)));
    var chart = Highcharts.chart(e,{
		chart: {
				type: 'gauge',
				plotBackgroundColor: null,
				plotBackgroundImage: null,
				plotBorderWidth: 0,
				plotShadow: false,
				backgroundColor:'transparent'
		},
		title: {
				text: '',
				style:{
					color:'#cccccc',
					fontFamily:'微软雅黑'
				},
				floating:true
		},
		credits:{
			enabled:false
		},
		tooltip:{
			enabled:false
		},
		pane: {
				startAngle: -150,
				endAngle: 150,
				background: [{
						backgroundColor: {
								linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
								stops: [
										[0, '#FFF'],
										[1, '#333']
								]
						},
						borderWidth: 0,
						outerRadius: '109%'
				}, {
						backgroundColor: {
								linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
								stops: [
										[0, '#333'],
										[1, '#FFF']
								]
						},
						borderWidth: 0,
						outerRadius: '107%'
				}, {
						// default background
				}, {
						backgroundColor: '#DDD',
						borderWidth: 0,
						outerRadius: '105%',
						innerRadius: '103%'
				}],
				center: ['50%', '50%'], //仪表中心
				size:'90%'
		},
		// the value axis
		yAxis: {
				min: warn_lef,
				max: warn_rig,
				minorTickInterval: 'auto',
				minorTickWidth: 1,
				minorTickLength: 10,
				minorTickPosition: 'inside',
				minorTickColor: '#666',
				tickPixelInterval: 30,
				tickWidth: 2,
				tickPosition: 'inside',
				tickLength: 10,
				tickColor: '#666',
				labels: {
						step: 2,
						rotation: 'auto'
				},
				title: {
						text: title+"<br/>"+units[sta],
						y:20
				},
				plotBands: [//绿 #55BF3B
				{
					from: warn_lef,
					to: lefting,
					color: '#DF5353' // 左红
				}, {
						from: lefting,
						to: min,
						color: '#DDDF0D' // 左黄
				},  {
					from: min,
					to: max,
					color: '#55BF3B' // 绿
				},{
						from: max,
						to: righting,
						color: '#DDDF0D' // 右黄
				}, {
						from: righting,
						to: warn_rig,
						color: '#DF5353' // red
				}]
		},
		series: [{
				name: 'Speed',
				data: i,
		}]
    	}, function (chart) {
				var sp = setInterval(function () {
						if(typeof(chart.series)!="undefined"){
							var left = chart.series[0].points[0];
							left.update(parseFloat(tmp[sta].toFixed(2)));
//							console.log("speed:"+tmp[sta])		
						}else{
							clearInterval(sp)
						}					
				}, 2000);
});
}
function initSpline(e,sta){
	var title = $("#u434_input option[value='"+sta+"']").text();
	var min = tmp_min[sta];
	var max = tmp_max[sta];
	var lefting = min-(max-min)*0.05;
	var righting = max + (max-min)*0.05;
	var warn_lef = min-(max-min)*0.2;
	var warn_rig=  max + (max-min)*0.2;
    var chart = Highcharts.chart(e,{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
			marginRight: 10,
			zoomType:'x'
    		},
		title: {
			floating:false,
			text:title,
			style:{
				fontSize:'16px',
				fontWeight:'bold',
				color:'#cccccc',
				fontFamily:'微软雅黑'
			}
		},
		xAxis: {
				type: 'datetime',
				tickPixelInterval:100,
				maxPadding:0.5,
				labels: {
					style:{
						color:'#cccccc'
					}
				},
		},
		yAxis: {
				title: {
						text: null
				},
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value + ' '+units[sta];
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
	//			maxPadding:0.4,
				minPadding:0.4,
				min:lefting,
				max:righting,
				plotLines:[
//				{//下红
//					color:'#DF5353',
//					dashStyle:'ShortDot',
//					value:lefting,
//					width:1
//				},
				{//下黄
					color:'#DDDF0D',
					dashStyle:'ShortDot',
					value:min,
					width:1,
				},
				{//上黄
					color:'#DDDF0D',
					dashStyle:'ShortDot',
					value:max,
					width:1
				},
//				{//上红
//					color:'#DF5353',
//					dashStyle:'ShortDot',
//					value:righting,
//					width:1
//				}
				]
		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return '<b>' + title + '</b><br/>' +
								Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
								Highcharts.numberFormat(this.y, 2);
				},
			crosshairs: [true, true]
		
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
				data:SplineData(sta)
		}]
    	}, function (chart) {
				var cl = setInterval(function () {
					if (chart.series) { // the chart may be destroyed
						var x = tmp['TIMESTAMP']-8*3600*1000;	    	
						var y = tmp[sta];
						chart.series[0].addPoint([x,y],true,true);
						var points = chart.series[0].points;
						if(points.length>=1){
							chart.tooltip.refresh(points[points.length -1]);
						}else{
							clearInterval(cl)
						}
						
					}				
				}, 2000);
    	});
}

function save(){
	var num = $("#num").text();
	console.log(num);
	if(num==''||num==null){
		return;
	}
	var bef = stas[num];
	var sta = $("#u434_input").find("option:selected").val();
	var ser = $("#u439_input").find("option:selected").val();
	console.log(sta);
	init[num-1]=sta;
	init_t[num-1]=ser;
	var tg = parseInt(num)+422;
	if($("#container"+num).highcharts()!=null){
		ruin("container"+num);
	} 
	switch(ser){
	case '1':
		initSpline("container"+num,sta);
		break;
	case '2':
		initSpeed("container"+num,sta);
		break;
	case '3':
		initVA("container"+num,sta);
		break;
	  default:
	  	break;
	}
	setUnchosen(bef);
//	console.log(sta);
	setChosen(sta);
	$("#u"+tg).attr("title",$("#u434_input").find("option:selected").text());
	$("#"+num).html('<b>'+$("#u434_input").find("option:selected").text()+'</b>');
	
	s=init.join(',');
	console.log(s);
	p=init_t.join(',');
	console.log(init);
	console.log(s);
	console.log(p);
	/*var s = '',p = '';
	for(var i =1 ;i <= 6;i++){
		s+=$("#stat"+i).val();
		p+=$("#par"+i).val();
		if(i!=6){
			s+=',';
			p+=',';
		}
	}*/
	$("#stats").val(s);
	$("#pars").val(p);
/*	if($("#lines").val()==''||$("#lines").val()==null){
		layer.msg('请选择一条产线', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	if($("#configs").val()==''||$("#configs").val()==null){
		layer.msg('请选择一个方案', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}*/
	var type = $("input[name='type']:checked").val();
	$("#code").val(type);
	var formData = new FormData($("#box1")[0]);
	//console.log(formData);
	$.ajax({
		type:'POST',
    	url:'../board/updateConfig.do?',
    	data:formData,
		contentType: false, 
	    processData: false,
	    beforeSend:function(){
			load = layer.load(0, {shade: false});
		},
    	success:function(data){
    		layer.close(load);
    		if(data=='0'){
				layer.msg('更改成功', {
					  icon: 1,
					  time: 2500 //2秒关闭（如果不配置，默认是3秒）
				});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
    	},
    	error:function(e){
    		layer.msg('预加载工位信息失败', {
				  icon: 2,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
    	}	
	});
}

