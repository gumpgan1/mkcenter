window.numin = '0';
window.onload=function(){
	getLimo();
	getFactories();
	getStations();
	newdiv();		
	//getStationData();
	var it = self.setInterval("refreshFactories()",2000);
	var li = self.setInterval("getLimo()",2000);
	initselect();
initchosen();
}
//引导参数！
var line_id = 5;
//初始化数据库已有数组情况
var divs = new Array();
//初始化容器信息变量
var chartid= new Array(5);
var chartname=new Array(5);
var chartposition;
var charttype;

//产线介绍+运行工况(初始化)

function getFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id='+line_id,
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var t = '0';
	    			$("#fac_name").html(json.list[key].INSTRUCTION);
	    			$("#fac_inc").html(json.list[key].NAME);
	    			$("#fac_add").html(json.list[key].ADDRESS);
	    			$("#fac_ins").html(json.list[key].INFO);
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
	    			if(parseInt(key)<96){
	    				var num = parseInt(key)+1;
	    			}
	    			else{
	    				var num = parseInt(key);
	    			}
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
//刷新工况（针对产线
function refreshFactories(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/getLine.do?id='+line_id,
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		for(var key in json.list){
	    			var t = '0';
	    			$("#sta_time").html(t);
	    		}
	    	},
	    	error:function(e){
	    		console.log('获取数据失败');
	    	}
	 })
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
	    		console.log(json); //原先的getnew这里的json是一个list(array),但只有一条array
	    		var pieData = [];
	    		for(var key in json.list){
	    			if(parseInt(key)<96){
	    				var num = parseInt(key)+1;
	    			}
	    			else{
	    				var num = parseInt(key);
	    			}
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
							console.log(divs[i]);
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

//初始化两个下拉框
function initselect(){
	$.ajax({
    	type:'GET',
    	url:'../jiangyin/getCompleteData.do',
    	//timeout:2000,
//    	 async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		console.log(json);
    		$("#editable-select1").append('<option value=""></option>');
    		$("#select2").append('<option value=""></option>');
    		$("#select2").trigger('chosen:updated');//将更新添加到前端去
    		for(var key in json.list){
    			$("#select2").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			$("#select2").trigger('chosen:updated');
    			$("#editable-select1").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    		}
//    		$("#u434_input").append('<option value="out">总料位反馈值</option>');
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
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

//新建div
function addElement(){
	adddiv();
	addspan();
}

//在大屏上创建div
function adddiv(){
	var selectid=document.getElementById("editable-select1").value;
	var div = document.createElement('div');
/*	div.innerHTML = 111;*/
	div.position = 'absolute';
	console.log(selectid);
	div.id =selectid;
	div.className = "draggable";	
	divs.splice(divs.length-1,0,selectid);
	document.getElementById('u189').appendChild(div);
	re();
}
//在div里创建span样式
function addspan(){
	var selectid=document.getElementById("editable-select1").value;
	name=selectid+"name";
	num=selectid+"data";
	$("#"+selectid).append('<p><span style="color:#65c1ff;font-weight:700" id='+name+'></span><span id='+num+'></span></p>');
}

//刷新div的可拖动属性
function re() {
	  var $draggable = $('.draggable').draggabilly();
	  // Draggabilly instance
	  var draggie = $draggable.data('draggabilly');
	  $draggable.on( 'dragMove', function() {
	    console.log( 'dragMove', draggie.position.x, draggie.position.y );
	  });
	  }

//获取当前div的id
$(function(){
  $(document).click(function (e) {  
      console.log($(e.target).attr('id'));  // 获取id
      del_id=$(e.target).attr('id');
  })
})


//在前端页面删除对应div,并在array中删除id
function dropElement(){
	if(del_id == "OUTVALUEdata" || del_id == "OUTVALUEname"){
		del_id=del_id.substring(0,8);
		var my = document.getElementById(del_id);
		if (my != null){
		  divs[divs.indexOf(del_id)]=null;
		  divs.splice(divs.indexOf(null),1)
		  my.parentNode.removeChild(my);
		    	
		}
	}
    del_id=del_id.substring(0,5);
    console.log(del_id);
	var my = document.getElementById(del_id);
	if (my != null && del_id.substring(0,1)=="L"){
	   divs[divs.indexOf(del_id)]=null;
	   divs.splice(divs.indexOf(null),1)
	   my.parentNode.removeChild(my);
	    	
	  }
}


//保存当前页面的所有新建div信息
var divnum = new Array();
//保存所有已配置信息
function save(){
	console.log(divs);
	var savedata = new Array();
	for(a=0;a<divs.length;a++){
		var b=document.getElementById(divs[a]);
		var station_id = b.id;
		var position_left = b.offsetLeft;
		var position_top = b.offsetTop;
		var map={};
		map={"station_id":""+station_id+"","position_left":""+position_left+"","position_top":""+position_top+""};
		savedata.push(map);
	}
	console.log(savedata);
	console.log(JSON.stringify(savedata));
	
 	 $.ajax({
		type:'POST',
		url:'../jiangyin/saveStationData.do',
		data:JSON.stringify(savedata),
		dataType: "json",
		contentType: "application/json", 
	    processData: false, 
		
		success:function(result){
				if(result==0){
					alert("success");
			}else{
				 alert("failed");
				}
				}
				});   
}


function initchosen(){
	$('.selectmultiple').chosen();
}


//调用函数初始化图
function initrqchart(){
	//将下拉框的值放入数组中
	var j=0;
	var titlename="";
	for(var i=0;i<select2.options.length;i++){
	if(select2.options[i].selected){
			chartid[j]=select2.options[i].value;
			chartname[j]=select2.options[i].text;
			titlename=titlename+select2.options[i].text;
			j++;
		}
	}
	charttype=document.getElementById("select4").value;
	chartposition=document.getElementById("select3").value;
	$("#title"+chartposition+"_text").empty();
	$("#title"+chartposition+"_text").append('<p><span>'+titlename+'</span></p>');
	switchchart(chartposition,charttype);
}

function switchchart(position,type){
	
	switch(type){
		case "spline":
			initspline(position);break;
		case "column":
			initcolumn(position);break;
	}
}

function initspline(e){
	var chartrq = Highcharts.chart("rqchart"+e,{
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
				color:'#00AA55',
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
	    			console.log(map);
	    			for(var item in map){
	    				if(chartid[0]==item){
	    					series0Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(chartid[1]==item){
	    					series1Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(chartid[2]==item){
	    					series2Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(chartid[3]==item){
	    					series3Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}else if(chartid[4]==item){
	    					series4Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}
	    			};
	    		}
	    		console.log(series0Data);
	    		console.log(series1Data);
	    		console.log(series2Data);
	    		chartrq.series[2].setData(series2Data);
	    		chartrq.series[2].setName(chartname[2]);
	    		chartrq.series[1].setData(series1Data);
	    		chartrq.series[1].setName(chartname[1]);
	    		chartrq.series[0].setData(series0Data);
	    		chartrq.series[0].setName(chartname[0]);	    			    		
	    		chartrq.series[3].setData(series3Data);
	    		chartrq.series[3].setName(chartname[3]);
	    		chartrq.series[4].setData(series4Data);
	    		chartrq.series[4].setName(chartname[4]);
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
}

function initcolumn(e){
	var chartrq = Highcharts.chart("rqchart"+e,{
		chart: {
			type: 'column',
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
						return this.value + "unit";
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
//				maxPadding:0.4,
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
		}]
	});
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
	    			console.log(map);
	    			for(var item in map){
	    				if(chartid[0]==item){
	    					series0Data.push({
	    						x:  map["TIMESTAMP"]-0,
	    						y : map[item],
	    	    			});
	    				}
	    				
	    			};
	    		}
	    		console.log(series0Data);
	    		chartrq.series[0].setData(series0Data);
	    		chartrq.series[0].setName(chartname[0]);	    			    		
	    	},
	    	error:function(e){
	    		alert('获取数据失败');
	    	}
	 })
}



function savechart(){
	var chartids=chartid.join(",");
	var chartnames=chartname.join(",");
	var position="rqchart"+chartposition;
	var map={};
	map={"chartids":""+chartids+"","chartnames":""+chartnames+"","position":""+position+"","type":charttype};
	
	$.ajax({
		type:'POST',
		url:'../chart/updateConfig.do',
		data:JSON.stringify(map),
		dataType: "json",
		contentType: "application/json", 
	    processData: false, 
		
		success:function(result){
				if(result==0){
					alert("success");
			}else{
				 alert("failed");
				}
				}
				});
}