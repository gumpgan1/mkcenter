window.onload = function(){
	login();
}
var id=new UrlSearch().id; // 实例化 取出产线ID
var index = new Array();
var flag = 0;
var minthres = new Array();
var maxthres = new Array();
var minV= new Array();
var maxV= new Array();
var units = new Array();
var ins = new Array();
var pre;
var spline1 = new Array();
var spline2 = new Array();
var spline3 = new Array();
var spline4 = new Array();
var spline_1 = new Array();
var spline_2 = new Array();
var relate = new Array();
var par1N,par2N;
var speed;
//读取地址栏Request参数
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

function initPage(){
	var t1 = $("#u475_input").datetimepicker({
		firstDay:1,
		dateFormat:'yy-mm-dd',
		timeFormat:'HH:mm:ss',
		onClose:function(dateText,inst){
			$( "#u478_input" ).datepicker( "option", "minDate",t1.val());
		}
	});
	var t2 = $("#u478_input").datetimepicker({
		firstDay:1,
		dateFormat:'yy-mm-dd',
		timeFormat:'HH:mm:ss',
	});
	//初始化下拉菜单
	$.ajax({
    	type:'GET',
    	url:'../limo/lines.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#u467_input").append('<option value="">--请选择产线--</option>');
    		for(var key in json.list){
    			$("#u467_input").append('<option value='+json.list[key].ID+'>'+json.list[key].INSTRUCTION+'</option>');
    			index[json.list[key].ID]=(json.list[key].SIGN+"").toLowerCase();
    		}
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
}

function stations(){
	var lid = $("#u467_input").val();
	$("#u471_1_input").empty();
	$("#u471_2_input").empty();
	$("#u471_3_input").empty();
	$("#u471_4_input").empty();
	$.ajax({
    	type:'GET',
    	async:false,
    	url:'../limo/getStation.do?line_id='+lid,
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#u471_1_input").append('<option value=""></option>');
    		$("#u471_2_input").append('<option value=""></option>');
    		$("#u471_3_input").append('<option value=""></option>');
    		$("#u471_4_input").append('<option value=""></option>');
    		for(var key in json.list){
    			$("#u471_1_input").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			$("#u471_2_input").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			$("#u471_3_input").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			$("#u471_4_input").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    			minthres[json.list[key].STAT_ID]=json.list[key].MIN_THRESHOLD; //上预警值
    			maxthres[json.list[key].STAT_ID]=json.list[key].MAX_THRESHOLD; //下预警值
    			ins[json.list[key].STAT_ID]=json.list[key].INSTRUCTION;
    			minV[json.list[key].STAT_ID]=json.list[key].MIN_VALUE; //最小值
    			maxV[json.list[key].STAT_ID]=json.list[key].MAX_VALUE; //最大值
    			units[json.list[key].STAT_ID] = json.list[key].UNIT; //单位
    			//设置两个相关参数
    			if((json.list[key].RELATE_1)==null && (json.list[key].RELATE_2)==null){
    				relate[json.list[key].STAT_ID]={
        					name:json.list[key].NAME,
        					par1:"",
        					val1:"NAN",
        					par2:"",
        					val2:"NAN"
        			}
        			
    			}else{
    				var relate1 = (json.list[key].RELATE_1).split(',');
        			var relate2 = (json.list[key].RELATE_2).split(',');
        			console.log("par1"+relate1[0]+"val1"+relate1[1]);
        			console.log(relate1+"进入方法"+relate2);
        			relate[json.list[key].STAT_ID]={
        					name:json.list[key].NAME,
        					par1:relate1[0],
        					val1:relate1[1],
        					par2:relate2[0],
        					val2:relate2[1]
        			}
    			}
    			
    			
    		}
    	}
	});
	//获得时间戳范围
	$.ajax({
    	type:'GET',
    	url:'../'+index[lid]+'/getRange.do?',
    	success:function(data){
    		var json =  eval("("+data+")");
			$( "#u475_input" ).datepicker( "option", "minDate",new Date(json.list[0].MIN-8*3600*1000));
			$( "#u475_input" ).datepicker( "option", "maxDate",new Date(json.list[0].MAX-8*3600*1000));
			$( "#u478_input" ).datepicker( "option", "minDate",new Date(json.list[0].MIN-8*3600*1000));
			$( "#u478_input" ).datepicker( "option", "maxDate",new Date(json.list[0].MAX-8*3600*1000));
    	}
	});
}

function search(){
	if(flag==1){
		return;
	}
	var lid = $("#u467_input").val();
	if(lid==''||lid==null){
		alert('请选择一条产线！');
		return;
	}
	var source = $("#u467_1_input").val();
	var sid1 = $("#u471_1_input").val();
	var sid2 = $("#u471_2_input").val();
	var sid3 = $("#u471_3_input").val();
	var sid4 = $("#u471_4_input").val();
	var sid1_name =  $("#u471_1_input").find("option[value='"+sid1+"']").text();
	var sid2_name =  $("#u471_2_input").find("option[value='"+sid2+"']").text();
	var sid3_name =  $("#u471_3_input").find("option[value='"+sid3+"']").text();
	var sid4_name =  $("#u471_4_input").find("option[value='"+sid4+"']").text();
	if(sid1==''||sid1==null){
		alert('第一个工位必选!请选择工位！');
		return;
	}
	var rid_1 = relate[sid1].par1;
	var rid_1_name = $("#u471_1_input").find("option[value='"+rid_1+"']").text();
	var rid_2 = relate[sid1].par2;
	var rid_2_name = $("#u471_1_input").find("option[value='"+rid_2+"']").text();
	var start = new Date($("#u475_input").val()).getTime();
	var end = new Date($("#u478_input").val()).getTime();
	if(start>=end||isNaN(start)||isNaN(end)){
		alert('时间区间有误，请重新选择');
		return;
	}
	ruinAll();
	$("#search").html('加载中...');
	$("#u482").attr("disabled",true);
	$("#u481").attr("disabled",true);
	$.ajax({
    	type:'GET',
    	url:'../'+index[lid]+'/selectRange.do?start='+start+'&end='+end+'&source='+source,
    	success:function(data){
    		var json =  eval("("+data+")");
    		console.log(json);
    		console.log(sid1);
    		console.log(rid_1);
    		console.log(rid_2);
    		//比较数据 对左上模块赋值
    		var all = 0, sumar=0,avai = 0, max = -999999,min = 999999,safe = 0, unsafe = 0;
    		var sumar1 = 0,avai1 = 0,max1 = -999999,min1=999999;
    		var sumar2 = 0,avai2 = 0,max2 = -999999,min2=999999;
    		var maxt = maxthres[sid1];
			var mint = minthres[sid1];
    		for(var key in json.list){
    			var val = (json.list[key])[sid1];
    			var val2 = (json.list[key])[sid2];
    			var val3 = (json.list[key])[sid3];
    			var val4 = (json.list[key])[sid4];
    			var val_1 = (json.list[key])[rid_1];
    			var val_2 = (json.list[key])[rid_2];
    			all+=1;
    			if(val==null){
    				continue;
    			}else{
    				avai+=1;
    				sumar+=val;
    			}
    			if(val>max){
    				max = val;
    			}
    			if(val<min){
    				min=val;
    			}
    			if(val<maxt&&val>mint){
    				safe+=1;
    			}else{
    				unsafe+=1;
    			}
    			if(val_1!=null){
    				avai1+=1;
    				sumar1+=val_1;
    			}
    			if(val_1>max1){
    				max1 = val_1;
    			}
    			if(val_1<min1){
    				min1=val_1;
    			}
    			if(val_2!=null){
    				avai2+=1;
    				sumar2+=val_2;
    			}
    			if(val_2>max2){
    				max2= val_2;
    			}
    			if(val_2<min2){
    				min2=val_2;
    			}
				spline1.push({
					x:json.list[key].TIMESTAMP-0,
					y:val
				});
				spline2.push({
					x:json.list[key].TIMESTAMP-0,
					y:val2
				});
				spline3.push({
					x:json.list[key].TIMESTAMP-0,
					y:val3
				});
				spline4.push({
					x:json.list[key].TIMESTAMP-0,
					y:val4
				});
				spline_1.push({
					x:json.list[key].TIMESTAMP-0,
					y:val_1
				});
				spline_2.push({
					x:json.list[key].TIMESTAMP-0,
					y:val_2
				});
    			
    		}
    		console.log(spline1);
    		console.log(spline2);
    		if(max==-999999){
    			max = 'NaN';
    		}
    		if(min==999999){
    			min = 'NaN';
    		}
    		if(max1==-999999){
    			max1 = 'NaN';
    		}
    		if(min1==999999){
    			min1 = 'NaN';
    		}
    		if(max2==-999999){
    			max2 = 'NaN';
    		}
    		if(min2==999999){
    			min2 = 'NaN';
    		}
    		$("#val11").html(avai);
    		$("#val12").html(parseFloat(max).toFixed(2));
    		$("#val13").html(parseFloat(min).toFixed(2));
    		$("#val14").html((sumar/avai).toFixed(2));
    		$("#val15").html(maxt.toFixed(2));
    		$("#val16").html(mint.toFixed(2));
    		$("#val17").html(safe);
    		$("#val18").html(unsafe);
    		$("#ins").html(ins[sid1]);
    		/*$("#par1_name").html(rid_1_name);
    		$("#par1_1").html(parseFloat(relate[sid1].val1).toFixed(2));
    		$("#par1_2").html(avai1);
    		$("#par1_3").html(parseFloat(max1).toFixed(2));
    		$("#par1_4").html(parseFloat(min1).toFixed(2));
    		$("#par1_5").html((sumar1/avai1).toFixed(2));
    		$("#par2_name").html(rid_2_name);
    		$("#par2_name").attr("title",rid_2_name);
    		$("#par2_1").html(parseFloat(relate[sid1].val2).toFixed(2));
    		$("#par2_2").html(avai2);
    		$("#par2_3").html(parseFloat(max2).toFixed(2));
    		$("#par2_4").html(parseFloat(min2).toFixed(2));
    		$("#par2_5").html((sumar2/avai2).toFixed(2));*/
    		var piepar = new Array();
    		piepar.push({
    			name: '安全量',
				y: safe,
				color:'rgba(0,255,0,0.6)',
				sliced:true
    		},{
    			name: '非安全量',
				y: unsafe,
				color:'rgba(255,0,0,0.6)',
    		},{
    			name: '无效数据量',
				y: all-avai,
				color:'rgba(255,0,0,0)',
    		})
    		//加载左上数据饼图
    		initPie(piepar);
    		//加载右上实时数据图
    		initSpeed($("#u471_1_input").find("option:selected").text(),minV[sid1],maxV[sid1],minthres[sid1],maxthres[sid1],sid1,lid);
    		//加载左下角统计图
    		initSpline('container3',sid1_name,spline1,sid2_name,spline2,sid3_name,spline3,sid4_name,spline4,sid1,sid2,sid3,sid4);
    		//归一化处理
    		for(var i = 0 ;i < spline1.length;i++){
    			if(max!=min){
    				spline1[i].y = (spline1[i].y-min)/(max-min);
    			}else{
    				spline1[i].y = 0.5;
    			}
    			if(max1!=min1){
    				spline_1[i].y = (spline_1[i].y-min1)/(max1-min1);
    			}else{
    				spline_1[i].y = 0.5;
    			}
    			if(max2!=min2){
    				spline_2[i].y = (spline_2[i].y-min2)/(max2-min2);
    			}else{
    				spline_2[i].y = 0.5;
    			}
    			
    		}
//    		initSpline('container41',spline_1,minV[rid_1],maxV[rid_1],minthres[rid_1],maxthres[rid_1],rid_1);
    		console.log(rid_1_name);
    		//加载右下角图
    		//initMixSpline('container41',sid1_name,spline1,rid_1_name,spline_1,rid_2_name,spline_2);
    	},
    	beforeSend:function(e){
    		flag= 1;
    	},
    	error:function(e){
    		alert('加载失败');
    	},
    	complete:function(e){
    		$("#search").html('查询');
    		$("#u482").removeAttr("disabled");
    		$("#u481").removeAttr("disabled");
    		flag=0;
    	}
	});
}

function reset(){
	$("#u475_input").val('');
	$("#u478_input").val('');
}

//左上角数据饼图
function initPie(e){
	//数据质量饼图
	Highcharts.chart('container1', {
		chart:{
			backgroundColor:'transparent',
		},
		title: {
			text:'',
			floating:true
		},
		tooltip: {
			enabled:false,
			pointFormat: '<b>{point.percentage:.1f}%</b>',
			formatter: function () {
				return 	'<b>'+this.key+'</b><br/>'+this.percentage.toFixed(2)+'%</b>'
			},
		},
		credits:{
			enabled:false
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					distance:0,
					format: '<b>{point.name}</b><br/> {point.percentage:.1f} %',
					style:{
						color:'#cccccc'
					}
				}
			}
		},
		series: [{
			type: 'pie',
			name: '',
			data: e
		}]
	},function(e){
	});
}
//右上实时数据图
function initSpeed(a,b,c,d,e,f,lid){
	var mid = new Array();
	$.ajax({
    	type:'GET',
    	url:'../'+index[lid]+'/getNew.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		mid.push(parseFloat(((json.list[0])[f]).toFixed(2)));
    	}
	});
	var title = a;
	var min = b;
	var max = c;
	var lefting = d;
	var righting = e;
	var warn_lef = min-(max-min)*0.2;
	var warn_rig=  max + (max-min)*0.2;
    var chart = Highcharts.chart('container2',{
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
						rotation: 'auto',
				},
				title: {
						text: title+"<br/>"+units[f],
						y:20,
						style:{
							fontSize:12
						}
				},
				plotBands: [//绿 #55BF3B
				{
					from: warn_lef,
					to: min,
					color: '#DF5353' // 左红
				}, {
						from: min,
						to: lefting,
						color: '#DDDF0D' // 左黄
				},  {
					from:lefting ,
					to: righting,
					color: '#55BF3B' // 绿
				},{
						from: righting,
						to: max,
						color: '#DDDF0D' // 右黄
				}, {
						from: max,
						to: warn_rig,
						color: '#DF5353' // red
				}]
		},
		series: [{
				name: '数值',
				data: mid,
				tooltip: {
						valueSuffix:''
				}
		}]
    	}, function (chart) {
				speed = setInterval(function () {
					$.ajax({
				    	type:'GET',
				    	url:'../'+index[lid]+'/getNew.do',
				    	timeout:2000,
//				    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
				    	success:function(data){
				    		var json =  eval("("+data+")"); 
				    			if (chart.series) { // the chart may be destroyed
									var left = chart.series[0].points[0];
									var tmp = parseFloat((json.list[0])[f].toFixed(2));
//									console.log(tmp);
									left.update(tmp);				
				    			}		    		
				    	},
				    	error:function(e){
				    	}
					});
				}, 2000);
});
}
//左下角统计图
function initSpline(t,n1,g1,n2,g2,n3,g3,n4,g4,f1,f2,f3,f4){
	if(f2==''||f2==null){
		f2 = f1;
	}
	if(f3==''||f3==null){
		f3 = f1;
	}
	if(f4==''||f4==null){
		f4 = f1;
	}
    var chart = Highcharts.chart(t,{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
			zoomType:'x'
    		},
		title: {
			floating:false,
			text:'',
			style:{
				fontSize:'16px',
				fontWeight:'bold',
				color:'#cccccc',
				fontFamily:'微软雅黑'
			}
		},
		xAxis: {
				type: 'datetime',
//				tickPixelInterval:100,
				labels: {
					style:{
						color:'#cccccc'
					}
				},
		},
		yAxis: [{
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value + ' '+units[f1];
					},
					x:20
				},
				title: {
						style:{
							color:'#cccccc'
						},
						text:relate[f1].name,
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
				minPadding:0.4,
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value + ' '+units[f1];
					},
				}
		},{
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					if(f2==''||f2==null){
						return null;
					}
					return this.value + ' '+units[f2];
				},
				x:20
			},
			title: {
					style:{
						color:'#cccccc'
					},
					text:relate[f2].name,
					
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					if(f2==''||f2==null){
						return null;
					}
					return this.value + ' '+units[f2];
				},
			},
		},{
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[f3];
				},
				x:20
			},
			title: {
					style:{
						color:'#cccccc'
					},
					text:relate[f3].name,
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[f3];
				},
			},
			opposite: true
		},{
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[f4];
				},
				x:20
			},
			title: {
					style:{
						color:'#cccccc'
					},
					text:relate[f4].name,
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			tickPixelInterval:25,
			minPadding:0.4,
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[f4];
				},
			},
			opposite: true
		}],
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return '<b>' +
								Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '</b><br/>' +
								this.series.name + '<br/>' +
								Highcharts.numberFormat(this.y, 2);
				},
			crosshairs: [true, true]
		
		},
		legend: {
			enabled: true,
			itemStyle:{
				color:'#cccccc'
			}
		},
		plotOptions: {
			series: {
				marker: {
					enabled: false
				},
				turboThreshold: 0 // 或者更多，必须大于数组长度（1440）
			}
		},
		series: [{
					yAxis:0,
					name:n1,
					data:g1,
					color:'	#FFCC22'
				},
				{
					yAxis:1,
					name:n2,
					data:g2,
					color:'#DF5353'
				},
				{
					yAxis:2,
					name:n3,
					data:g3,
					color:'#00FF00'
				},
				{
					yAxis:3,
					name:n4,
					data:g4,
					color:'#8000FF'
				}
		],
	});
}
//右下角归一化图
function initMixSpline(t,n1,d1,n2,d2,n3,d3){
    var chart = Highcharts.chart(t,{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
			marginRight: 10,
			zoomType:'x'
    		},
    	colors:['#7cb5ec','#F2F710','#01DF3A'],
		title: {
			floating:false,
			text:'',
			style:{
				fontSize:'16px',
				fontWeight:'bold',
				color:'#cccccc',
				fontFamily:'微软雅黑'
			}
		},
		xAxis: {
				type: 'datetime',
				labels: {
					style:{
						color:'#cccccc'
					}
				},
		},
		yAxis: {
				title: {
						text: '均一化系数',
						style:{
							color:'#cccccc'
						},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				min:0,
				max:1,
				PixelInterval:25,
				minPadding:0.4,
				labels: {
					style:{
						color:'#cccccc'
					},
				},
		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			shared:true,
			crosshairs: true,
			dateTimeLabelFormats: {
				 hour:"%A, %b %e, %H:%M",
			},
			pointFormat:'<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}</b><br/>'
		},
		legend: {
				enabled: true,
				itemStyle:{
					color:'#cccccc'
				}
		},
		plotOptions: {
			series: {
				marker: {
					enabled: false
				},
				turboThreshold: 0 // 或者更多，必须大于数组长度（1440）
			}
		},
		series: [
				{	
					name:n1,
					data:d1
				},
				{
					name:n2,
					data:d2	
				},
				{
					name:n3,
					data:d3
				}
				]
    	});
}

function ruinAll(){
	$("#val11").html('');
	$("#val12").html('');
	$("#val13").html('');
	$("#val14").html('');
	$("#val15").html('');
	$("#val16").html('');
	$("#val17").html('');
	$("#val18").html('');
	$("#ins").html('');
	/*$("#par1_name").html('');
	$("#par1_1").html('');
	$("#par1_2").html('');
	$("#par1_3").html('');
	$("#par1_4").html('');
	$("#par1_5").html('');
	$("#par2_name").html('');
	$("#par2_name").removeAttr("title");
	$("#par2_1").html('');
	$("#par2_2").html('');
	$("#par2_3").html('');
	$("#par2_4").html('');
	$("#par2_5").html('');*/
	spline1 = [];
	spline2 = [];
	spline3 = [];
	spline4 = [];
	spline_1 = [];
	spline_2 = [];
	if($("#container1").highcharts()!=null){
		$("#container1").highcharts().destroy();
	}
	if($("#container2").highcharts()!=null){
		$("#container2").highcharts().destroy();
	}
	if($("#container3").highcharts()!=null){
		$("#container3").highcharts().destroy();
	}
	if($("#container41").highcharts()!=null){
		$("#container41").highcharts().destroy();
	}
	window.clearInterval(speed);   
}


function backToMain(){
	if(id == 3){
		window.location.href="daying1.html";
	}
	if(id == 4){
		window.location.href="daying2.html";
	}
	if(id == 5){
		window.location.href="jiangyin.html";
	}
}


