window.numin = '0';
window.onload=function(){
	login();
}
Highcharts.setOptions({ global: { useUTC: false } });
//引导参数！
var line_id = 3;
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
    			initchart3();//初始化高压油压图
    			initchart2();//初始化水分子矿渣所占比
    			initchart1();//初始化立磨振动趋势图
    			//initbox();//初始化异常推送
    			roll(200);//报警信息2s刷新一次
    			getLimo();//获取所有值
    			getStations();//获取tag的属性attr
    			//var abnormal=self.setInterval("initbox()",2000*30);
    			var it = self.setInterval("refreshFactories()",2000);
    			var li = self.setInterval("getLimo()",2000);
    			//var lt = self.setInterval("minuteNew()", 2000*30);
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

//初始化工厂基本信息（产线id--产线的生产状态等基本情况，可以通用
function getFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id='+line_id,
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")"); 
	    		//console.log(json);
	    		for(var key in json.list){
	    			var t = '0';
	    			if(json.list[key].STATUS=='1'){
	    				t = setTime(new Date().getTime()-json.list[key].LAST_BEGIN+4*3600*1000);
	    			}
	    			$("#fac_name").html(json.list[key].INSTRUCTION);
	    			$("#fac_inc").html(json.list[key].NAME);
	    			$("#fac_add").html(json.list[key].ADDRESS);
	    			$("#fac_ins").html(json.list[key].INFO);
	    			$("#sta_now").html(setStatus(json.list[key].STATUS));
	    			$("#sta_time").html(t);
	    			window.numin = json.list[key].NUM_IN;
	    		}
	    	},
	    	error:function(e){
	    		alert('初始化失败');
	    	}
	 })
	 //return factories; 
};

//初始化工位标签详细信息，可以通用，根据lineid 在infostation查找该产线的工位基本信息
function getStations(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getStation.do?line_id='+line_id,
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
	    		alert('加载工位信息失败');
	    	}
	 })
	 //return factories; 
};

//刷新工况  根据lineid 刷新当前产线的工作时间和工作状态 ，可以通用
function refreshFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id='+line_id,
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var t = '0';
	    			if(json.list[key].STATUS=='1'){
	    				t = setTime(new Date().getTime()-json.list[key].LAST_BEGIN+4*3600*1000);
	    			}
	    			$("#sta_now").html(setStatus(json.list[key].STATUS));
	    			$("#sta_time").html(t);
	    		}
	    	},
	    	error:function(e){
	    		console.log('获取数据失败');
	    	}
	 })
};

//获取图表和定位点
function getLimo(){
	$.ajax({
	    	type:'GET',
	    	url:'../daying1/getNew.do?',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		//console.log(json);
	    		var pieData = [];
	    		for(var key in json.list){
	    			//左下模块----水分占比
	    			var x = json.list[key].TIMESTAMP-8*3600*1000;
	    			var y = json.list[key].L0007;
	    			var y1 = json.list[key].L0037;
	    			var y2=json.list[key].L0065;
	    			spl.series[0].addPoint([x,y1],true,true);
					chart3.series[0].addPoint([x,y2],true,true);
					lw.series[0].addPoint([x,y],true,true);
					
					$("#now").html(y1.toFixed(2));
					$("#sta_out").html(((json.list[key].L0002+json.list[key].L0005)*0.92).toFixed(2));
					//电流总量
	    			var a = json.list[key].L0007+json.list[key].L0049+json.list[key].L0064;
	    			if(window.numin=='0'){
	    				$("#sta_cos").html('0');
	    			}else{
	    				$("#sta_cos").html(parseFloat(window.numin/a).toFixed(2));
	    			}
	    			var date = new Date(json.list[key].TIMESTAMP-8*3600*1000);
	    			$("#time").html(date.format("yyyy-MM-dd hh:mm:ss"));	
	    			$("#OUTVALUE").html(parseFloat(json.list[key].OUTVALUE).toFixed(2));
	    			for(var i = 1;i<=100;i++){
	    				var l = "L"+pad(i,4);
	    				$("#"+l).html(((json.list[key])[l]).toFixed(2));
	    			}
	    		}
	    	},
	    	error:function(e){
	    		console.log('获取数据失败');
	    	}
	 });
	
	 
};


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

function initchart3(){
	var chart3Data=[];
	$.ajax({
	    	type:'GET',
	    	url:'../daying1/get.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		console.log(json);
	    		for(var key in json.list){
	    			chart3Data.push({
						x : json.list[key].TIMESTAMP-3600*1000*8,
						y : json.list[key].L0065
					});

	    		}
	    		chart3.series[0].setData(chart3Data);
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};


//初始化图水分在矿渣所占比数据
function initchart2(){
	var chart2Data=[];
	$.ajax({
	    	type:'GET',
	    	url:'../daying1/get.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			chart2Data.push({
						x : json.list[key].TIMESTAMP-3600*1000*8,
						y : json.list[key].L0037
					});
	    			
	    		}
	    		console.log(chart2Data);
	    		spl.series[0].setData(chart2Data);
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};

//初始化图一，获取数据
function initchart1(){
	var initData=[];
	$.ajax({
		type:'GET',
		url: '../daying1/get.do',
		success:function(data){
			var json=eval("("+data+")");
			//console.log(json);
			for(var key in json.list){
				initData.push({
    				x : json.list[key].TIMESTAMP-3600*1000*8,
					y : json.list[key].L0007
					
    			});
				//console.log("实时值"+(json.list[key].TIMESTAMP-3600*1000*8));
				
			}
			//console.log(initData);
			lw.series[0].setData(initData);
		},
		error:function(e){
			alert('获取数据失败');
		}
	});

}




//chart2
var spl = Highcharts.chart('percent',{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
			marginRight: 10,
			zoomType:'x'
    		},
		title: {
			floating:false,
			text:null,
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
						return this.value + ' %';
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
	//			maxPadding:0.4,
				minPadding:0.4,
				min:0,
				

		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return '<b>水分占比</b><br/>' +
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
				data:[]
		}]
    });
//chart1
var	lw = Highcharts.chart('hourout', {
		chart: {
				type: 'spline',
				backgroundColor:'transparent',
				marginRight: 10,
				zoomType:'x',
				
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
					},
					step:2
				},
		},
		yAxis: {
				title: {
					text: '',
					style:{
						color:'#cccccc'
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value + ' A';
					},
				},
//				maxPadding:0.4,
				minPadding:0.4,
				
		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return  this.series.name +
								Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
								Highcharts.numberFormat(this.y, 2)+"A";
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
			name:"立磨主电机电流实时值",
			data:[],
			color:'	#4682B4',

	}]
	});
//chart3
var	chart3 = Highcharts.chart('chart3', {
	chart: {
		type: 'spline',
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
			},
			step:2
		},
	},
	yAxis: {
		title: {
			text: '',
			style:{
				color:'#cccccc'
			},
		},
		gridLineColor:'rgba(156,156,156,0.4)',
		tickPixelInterval:25,
		labels: {
			style:{
				color:'#cccccc'
			},
			formatter: function () {
				return this.value + ' ℃';
			},
		},
//				maxPadding:0.4,
		minPadding:0.4,
		
	},
	credits: {
		enabled: false
	},
	tooltip: {
		enabled:true,
		formatter: function () {
			return '<b>高压油压</b><br/>'  +
			Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
			Highcharts.numberFormat(this.y, 2)+"℃";
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
		data:[]
	}]
});


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
	e = parseFloat(e);
	var h = Math.floor(e/1000/3600);
	var m =	Math.floor((e/1000-h*3600)/60);
	var s = Math.floor(e/1000-h*3600-m*60);
	var ms = e-h*3600*1000-m*60*1000-s*1000;
	var t = h+"小时"+m+"分"+s+"秒"+ms;
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
function roll(t) {
    var ul1 = document.getElementById("ul1");
    var ul2 = document.getElementById("ul2");
    var box = document.getElementById("box");
    ul2.innerHTML = ul1.innerHTML;
    box.scrollTop = 0;
    var timer = setInterval(rollStart, t);
    //console.log(1);
    box.onmouseover = function () {
        clearInterval(timer)
    }
    box.onmouseout = function () {
        timer = setInterval(rollStart, t);
    }
}

function rollStart() {
    if (box.scrollTop >= ul1.scrollHeight) {
        box.scrollTop = 0;
    } else {
        box.scrollTop++;
    }
}

/*function initbox(){
	$.ajax({
    	type:'GET',
    	url:'../daying1/getAbnormalInfo.do?id='+line_id,
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var list =  eval("("+data+")");
   		//	console.log(list);
    		if(list != null){
    			$("#ul1").find("li").remove();    		
    			for(var i=0;i<list.length;i++){
    				if(list[i]!=null){
    					$("#ul1").append('<li>'+list[i]+'</li>');
    					}
    			}
    		}
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
}*/
