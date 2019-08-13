window.numin = '0';
window.onload=function(){
	getLimo();
	getStations();
	newdiv();
	getConfig();
	refresh();
	initrqchart();
	initbox();//初始化异常推送
	roll(200);//报警信息2s刷新一次
	var abnormal=self.setInterval("initbox()",2000*30);
	//getStationData();
	var li = self.setInterval("getLimo()",2000);
	
}

//引导参数！
var line_id = 5;
//初始化数据库已有数组情况
var divs = new Array();
//初始化容器信息变量
var chartid= new Array(3);
var chartname=new Array(3);
var chartposition= new Array(3);
var charttype= new Array(3);
//初始化放最新数据的map
var tmp={};



//初始化工位标签详细信息
function getStations(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getStation.do?line_id='+line_id,
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		console.log(json);
	    		for(var key in json.list){
	    			var num = parseInt(key)+1;
	    			//console.log(num); num是1-96的数字
	    			var tag = 'L'+pad(num,4);
	    			//console.log(tag); 从L0001到L0145
	    			$('#'+tag+'data').attr("title",json.list[key].NAME+"  单位："+json.list[key].UNIT);
	    		}
	    		$("#OUTVALUEdata").attr("title",json.list[96].NAME+"  单位："+json.list[96].UNIT);
	    	},
	    	error:function(e){
	    		alert('加载工位信息失败');
	    	}
	 })
	 //return factories; 
};


//获取最新工位点信息
function getLimo(){
	var list=[];
	$.ajax({
	    	type:'GET',
	    	url:'../jiangyin/getCompleteData.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		//console.log(json); //原先的getnew这里的json是一个list(array),但只有一条array
	    		var pieData = [];
	    		for(var key in json.list){
	    			var num = parseInt(key)+1;
	    			//console.log(num); num是1-96的数字
	    			var tag = 'L'+pad(num,4);
	    			//$("#"+tag).html(json.list[key].NAME+" ："+json.list[key].VALUE.toFixed(2)+json.list[key].UNIT);
	    			$("#"+tag+"name").html(json.list[key].NICKNAME);
	    			$('#'+tag+"data").html(json.list[key].VALUE.toFixed(2)+json.list[key].UNIT);
	    			$("#OUTVALUEname").html(json.list[96].NICKNAME);
	    			$("#OUTVALUEdata").html(json.list[96].VALUE.toFixed(2)+json.list[96].UNIT);
	    		}
	    	},
	    	error:function(e){
	    		console.log('获取数据失败');
	    	}
	 })
	 //return factories; 
	 
};

//生成可拖动框
function newdiv(){
	$.ajax({
	    	type:'GET',
	    	url:'../jiangyin/getStationData.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		//alert(data);
	    		var listMap =  eval("("+data+")");
	    		console.log(listMap);
	    		
	    		for(var i=0;i < listMap.length;i++){
	    			var map = listMap[i];
	    			var a = document.createElement('div');
	    			a.style.position='absolute';
	    			for(var key in map){	    				
						if(key=="STATION_ID"){
							a.id=map[key];
							divs[i]=map[key];
							//console.log(divs[i]);
							name=a.id+"name";
							num=a.id+"data";
						}
								
						if(key =="POSITION_LEFT"){
							a.style.left = map[key]+"px";
						}
							
						if(key == "POSITION_TOP"){
							a.style.top = map[key]+"px";
						}
							 
						 a.className = "draggable"; 
						
	    			}
	    			var parentdiv =document.getElementById('u189')
	    			parentdiv.appendChild(a);
	    			$("#"+a.id).append('<p><span style="color:#65c1ff;font-weight:700" id='+name+'></span><span id='+num+'></span></p>');
	    			
	    		}
	    		
	    	},
	 })
}

//将数字变成L00n的小函数
function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

//在div里创建span样式
function addspan(){
	var selectid=document.getElementById("editable-select1").value;
	name=selectid+"name";
	num=selectid+"data";
	$("#"+selectid).append('<p><span style="color:#65c1ff;font-weight:700" id='+name+'></span><span id='+num+'></span></p>');
}

function getConfig(){
	$.ajax({
    	type:'GET',
    	url:'../chart/getConfig.do',
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		list =  eval("("+data+")");
    		for(var i=0;i<list.length;i++){
    			chartid[i]=(list[i])["CHARTID"];
    			charttype[i]=(list[i])["TYPE"];
    			chartposition[i]=(list[i])["POSITION"];
    			chartname[i]=(list[i])["CHARTNAME"];
    			
    		}
    		console.log(charttype);
    	},
    	error:function(e){
    		console.log('获取数据失败');
    	}
 })
}

//调用函数初始化图
function initrqchart(){
	
	for(var i=0;i<3;i++){
		var type=charttype[i];
		var position=chartposition[i];
		var ids=chartid[i];
		var names=chartname[i];
		var titlename="";
		
		if(position=="rqchart1"){
			chart1.type=type;
		}else if(position=="rqchart2"){
			chart2.type=type;
		}else if(position=="rqchart3"){
			chart3.type=type;
		}
		
		
		
		if(type =="spline"){
			 var a= ids.split(',');
			 var b= names.split(',');
			 console.log(position);
			 initspline(position,a,b);
			 for(var j=0;j<5;j++){
				 	if(b[i]!=""){
				 		titlename=titlename+b[i];
				 		}					
				}
			 $("#title"+position.charAt(7)+"_text").empty();
			 $("#title"+position.charAt(7)+"_text").append('<p><span>'+b+'</span></p>');
		}else{
			var a=(ids.split(','))[0];
			var b=(names.split(','))[0];
			initcolumn(position,a,b);
			$("#title"+position.charAt(7)+"_text").empty();
			$("#title"+position.charAt(7)+"_text").append('<p><span>'+b+'</span></p>');
		}
	
	}
	
	
	
	



}


function splinedata(e,a,b){		
	var series0Data = [];
	var series1Data = [];
	var series2Data = [];
	var series3Data = [];
	var series4Data = [];
	$.ajax({
	    	type:'GET',
	    	url:'../jiangyin/get1.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");//将字符串转成json对象
	    		for(var key in json){
	    			//左下模块的水分占比数据
	    			var map=json[key];//map的格式是json格式
	    			//console.log(map);
	    			for(var item in map){
	    				if(a[0]==item){
	    					series0Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(a[1]==item){
	    					series1Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(a[2]==item){
	    					series2Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(a[3]==item){
	    					series3Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(a[4]==item){
	    					series4Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}
	    			};
	    		}
	    		//console.log(series0Data);
	    		//console.log(series1Data);
	    		//console.log(series2Data);
	    		chartrq.series[2].setData(series2Data);
	    		chartrq.series[2].setName(b[2]);
	    		chartrq.series[1].setData(series1Data);
	    		chartrq.series[1].setName(b[1]);
	    		chartrq.series[0].setData(series0Data);
	    		chartrq.series[0].setName(b[0]);	    			    		
	    		chartrq.series[3].setData(series3Data);
	    		chartrq.series[3].setName(b[3]);
	    		chartrq.series[4].setData(series4Data);
	    		chartrq.series[4].setName(b[4]);
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
}

function columndata(e,a,b){	
	var series0Data = [];
	$.ajax({
	    	type:'GET',
	    	url:'../jiangyin/gethourout1.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");//将字符串转成json对象
	    		console.log(json);
	    		for(var key in json){
	    			//左下模块的水分占比数据
	    			var map=json[key];//map的格式是json格式
	    			//console.log(map);
	    			for(var item in map){
	    				if(a==item){
	    					series0Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}
	    				
	    			};
	    		}
	    		console.log(series0Data);
	    		chartrq.series[0].setData(series0Data);
	    		chartrq.series[0].setName(b);	    			    		
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
}


function refresh(){
	$.ajax({
    	type:'GET',
    	url:'../jiangyin/getNew.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		for(var t in json.list[0]){
    			//console.log(t);
    			tmp[t]= (json.list[0])[t];
    		}
    		var date = new Date(json.list[0].TIMESTAMP-8*3600*1000);
    	},
    	error:function(e){
//    		alert('获取数据失败');
    	}
	});
}

function roll(t) {
    var ul1 = document.getElementById("ul1");
    var ul2 = document.getElementById("ul2");
    var box = document.getElementById("box");
    ul2.innerHTML = ul1.innerHTML;
    box.scrollTop = 0;
    var timer = setInterval(rollStart, t);
    console.log(1);
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

function initbox(){
	$.ajax({
    	type:'GET',
    	url:'../jiangyin/getAbnormalInfo.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var list =  eval("("+data+")");
   		//	console.log(list);
    		if(list != null){
    			$("#ul1").find("li").remove();    		
    			for(var i=0;i<list.length;i++){
    				if(list[i]!=null){
    					$("#ul1").append('<li sytle="color:red;">'+list[i]+'</li>');
    					}
    			}
    		}
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
}


var chart1 = Highcharts.chart("rqchart1",{
	chart: {
		backgroundColor:'transparent',
		marginRight: 10,
		zoomType:'x',			
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
					return this.value + "unit";
				},
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			min:null,
			

	},
	credits: {
		enabled: false
	},
	
	tooltip: {
		enabled:true,
		formatter: function () {
					return 	this.series.name+
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
			name:[],
			data:[],
			color:'	#FFCC22',
			
	},
		{
			name:[],
			data:[],
			color:'	#00AA55',
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},]
});
var chart2 = Highcharts.chart("rqchart2",{
	chart: {
		backgroundColor:'transparent',
		marginRight: 10,
		zoomType:'x',			
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
					return this.value + "unit";
				},
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			min:null,
			

	},
	credits: {
		enabled: false
	},
	
	tooltip: {
		enabled:true,
		formatter: function () {
					return 	this.series.name+
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
			name:[],
			data:[],
			color:'	#FFCC22',
			
	},
		{
			name:[],
			data:[],
			color:'	#00AA55',
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},]
});
var chart3 = Highcharts.chart("rqchart3",{
	chart: {
		backgroundColor:'transparent',
		marginRight: 10,
		zoomType:'x',			
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
					return this.value + "unit";
				},
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			min:null,
			

	},
	credits: {
		enabled: false
	},
	
	tooltip: {
		enabled:true,
		formatter: function () {
					return 	this.series.name+
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
			name:[],
			data:[],
			color:'	#FFCC22',
			
	},
		{
			name:[],
			data:[],
			color:'	#00AA55',
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},
		{
			name:[],
			data:[],
	},]
});
function addpoint(){
	
}