window.onload=function(){
	login();

}
/**
 * 变量定义
 */
var id=new UrlSearch().id; // 实例化
var table1_par = new Array();
var table2_par = new Array();
var table1_min = 0;
var table2_max = 0;
var shaks_min;
var shaks_max;
var eners_min;
var eners_max;
var index;
var minV = new Array();
var maxV = new Array();
var names = new Array();
var units = new Array();
var table3_par = new Array();
var table32_par = new Array();
var table33_par1 = new Array();
var table33_par2 = new Array();
var table41_par=new Array(),table42_par=new Array(),table43_par=new Array();
var config = new Array();var para = new Array();
Highcharts.setOptions(
		{ 
			global: { useUTC: false }		
		}
);
/**
 * 方法定义
 * @returns {UrlSearch}
 */
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
function afterSetExtremes(e){
	// container1 是喂料量
	var chart = $('#container1').highcharts(); 
	chart.showLoading('Loading data from server...');	
	var tmp = [];
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/updateyw.do?start=' + Math.round(e.min)+ '&end=' + Math.round(e.max),
//    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data!=null){
    			var json = eval("("+data+")");
	    		for(var key in json.list){
	    			tmp.push([
	    				json.list[key].TIMESTAMP-8*3600*1000,
	    				(json.list[key])[config[1]]
	    			]);
	    		}
	    		chart.series[0].setData(tmp,true);
	    		chart.hideLoading();
	    	}
    	},
    	error:function(e){
    		alert('获取数据失败');
    	}
	});
}
function afterSetExtremes2(e){
//  container21是出产量
	var chart = $('#container21').highcharts();
	chart.showLoading('Loading data from server...');	
	var tmp = [];
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/updateyw.do?start=' + Math.round(e.min)+ '&end=' + Math.round(e.max),
//    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data!=null){
    			var json = eval("("+data+")");
	    		for(var key in json.list){
	    			tmp.push([
	    				json.list[key].TIMESTAMP-8*3600*1000,
	    				(json.list[key])[config[2]]
	    			]);
	    		}
	    		chart.series[0].setData(tmp,true);
	    		chart.hideLoading();
	    	}
    	},
    	error:function(e){
    		alert('获取数据失败');
    	}
	});
}
function login(){
	$.ajax({
    	type:'POST',
    	url:'../checkLogin.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data!='0'){
    			console.log(id);
    			initConfig();
    			initButton(data,id);
    			initPage();
    			initTables();
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
//初始化配置的工位点信息/图表名字，和右下角的自定义栏名字（1,2,31,32,41,42,43）
function initConfig(){
	$.ajax({
    	type:'GET',
    	url:'../limo/getDefaultConfigYw.do?line_id='+id,
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		var n = (json.list[0].STAS).split(',');
    		var p = (json.list[0].PARS).split(',');
    		config[1] = n[0];
    		config[2] = n[1];
    		config[31] = n[2];
    		config[32] = n[3];
    		config[41] = n[4];
    		config[42] = n[5];
    		config[43] = n[6];
//    		ener_lef = parseFloat(p[0]);
//    		ener_rig = parseFloat(p[1]);
//    		shak_lef = parseFloat(p[2]);
//    		shak_rig = parseFloat(p[3]);
    		para[1] = p[0];
    		para[2] = p[1];
    		para[31] = p[2];
    		para[32] = p[3];
    		para[41] = p[4];
    		para[42] = p[5];
    		para[43] = p[6];
    		$("#init").html(n[7]);
    	},
    	error:function(e){
    		alert('初始化配置失败，请刷新！');
    	}
	});
}

//在对应的产线控制层下有getyw，getNew,getLatestWeek三个方法
function initPage(){
	var sums = 0;
	var sumDayCost = 0;
	var maxDayCost=-999999;
	var minDayCost=999999;
	var sumOut = 0;
	var minOut = 999999;
	var maxOut = -999999;
	
	$.ajax({
    	type:'GET',
    	url:'../limo/getLine.do?id='+id,
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		console.log(json);
    		var eners = (json.list[0].ENERS).split(',');
    		var shaks = (json.list[0].SHAKS).split(',');
    		eners_min = eners[0]; eners_max = eners[1];
    		shaks_min = shaks[0]; shaks_max = shaks[1];
    		$("#pageName").html(json.list[0].INSTRUCTION);
    		index = (json.list[0].ITEN).toLowerCase();
    		
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
	$.ajax({
    	type:'GET',
    	url:'../limo/getStation.do?line_id='+id,
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		for(var key in json.list){
    			minV[json.list[key].STAT_ID]=json.list[key].MIN_VALUE;
    			maxV[json.list[key].STAT_ID]=json.list[key].MAX_VALUE;
    			names[json.list[key].STAT_ID]=json.list[key].NAME;
    			units[json.list[key].STAT_ID]=json.list[key].UNIT;
    		}
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/getyw.do',
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		for(var key in json.list){
    			table1_par.push([
    				json.list[key].TIMESTAMP-8*3600*1000,
    				 parseFloat(((json.list[key])[config[1]]*para[1]).toFixed(2))
    			]);
    			table2_par.push([
    				json.list[key].TIMESTAMP-8*3600*1000,
    				 parseFloat(((json.list[key])[config[2]]*para[2]).toFixed(2))
    			]);
    			table43_par.push([
    			    json.list[key].TIMESTAMP-8*3600*1000,  
    			    parseFloat(((json.list[key])[config[43]]*para[43]).toFixed(2))
    			]);
    			if((json.list[key])[config[1]]>maxDayCost){
    				maxDayCost = (json.list[key])[config[1]]*para[1];
    			}
    			if((json.list[key])[config[1]]<minDayCost){
    				minDayCost = (json.list[key])[config[1]]*para[1];
    			}
    			if(!isNaN((json.list[key])[config[1]])){
    				sumDayCost =sumDayCost+(json.list[key])[config[1]]*para[1];
        			sums++;
    			}
    			if((json.list[key])[config[2]]>maxOut){
    				maxOut = (json.list[key])[config[2]]*para[2];
    			}
    			if((json.list[key])[config[2]]<minOut){
    				minOut = (json.list[key])[config[2]]*para[2];
    			}
    			if(!isNaN((json.list[key])[config[2]])){
    				sumOut =sumOut+(json.list[key])[config[2]]*para[2];
    			}	
    		}
    		$("#DayCost").html((sumDayCost/sums).toFixed(2));
    		$("#maxDayCost").html(maxDayCost.toFixed(2));
    		$("#minDayCost").html(minDayCost.toFixed(2));
    		$("#avgOut").html((sumOut/sums).toFixed(2));
    		$("#minOut").html(minOut.toFixed(2));
    		$("#maxOut").html(maxOut.toFixed(2));
//    		console.log(table1_par);
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/getNew.do?',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#out1").html((json.list[0])[config[2]].toFixed(2));
    		table41_par.push(parseFloat(((json.list[0])[config[41]]*para[41]).toFixed(2)));
    		table42_par.push(parseFloat(((json.list[0])[config[42]]*para[42]).toFixed(2)));
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
	
	$.ajax({
    	type:'GET',
    	url:'../'+index+'/getLatestWeek.do?timestamp='+new Date().getTime(),
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		var sta0=0,sta1=0;
    		for(var key in json.list){
    			var start = json.list[key].TIMESTAMP-3600000;
    			var end = json.list[key].TIMESTAMP-0;
    			if((json.list[key])[config[1]]==null||(json.list[key])[config[1]]==''||(json.list[key])[config[1]]<=10){
    				//给左下角的图注入数值
    				table3_par.push({
						x: start,
						x2: end,
						y: 1,
    				});
    				sta1+=1;
    				table33_par1.push({
	    				x:json.list[key].TIMESTAMP-8*3600*1000,
	    				y:0
	    			});
					table33_par2.push({
	    				x:json.list[key].TIMESTAMP-8*3600*1000,
	    				y:0
	    			})
    			}else{
					table3_par.push({
						x: start,
						x2: end,
						y: 0,
    				});
					sta0+=1; 
					table33_par1.push({
	    				x:json.list[key].TIMESTAMP-8*3600*1000,
	    				y:(json.list[key])[config[31]]*para[31]/((json.list[key])[config[1]]*para[1])
	    			});
					table33_par2.push({
	    				x:json.list[key].TIMESTAMP-8*3600*1000,
	    				y:(json.list[key])[config[32]]*para[32]
	    			})
	    			
    			}
    			
    		}
    		table32_par.push(
    		{
				name: '正常运行',
				y: sta0,
				color:'rgba(0,255,0,0.6)',
				sliced:true
    		},{
				name: '停工/维修',
				y: sta1,
				color:'rgba(255,0,16,0.6)',
    		}
    		);
    		},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
}
//初始化各个图（）
function initTables(){
	Highcharts.stockChart('container1', {
		chart: {
			zoomType: 'x',
			backgroundColor:'transparent',
		},
		credits:{
			enabled:false
		},
		scrollbar:{
			enabled:false
		},
		rangeSelector: {
			buttons: [
			{
				type: 'hour',
				count: 1,
				text: 'h'
			}, {
				type: 'day',
				count: 1,
				text: 'day'
			},{
				type: 'week',
				count: 1,
				text: 'week'
			},{
				type: 'all',
				text: 'All'
			}],
			inputEnabled: true, // it supports only days
			selected : 4, // all
			labelStyle:{
				color:'#cccccc'
			},
			inputStyle:{
				color:'#cccccc'
			},
			 buttonTheme: { // styles for the buttons
				 stroke:'#cccccc'  , 
				 'stroke-width':1,
				 fill: 'none',
		            border: '2px solid #4CAF50',
		            style: {
		                color: '#cccccc',
		            },
		            states: {
		                hover: {
		                	style:{
		                		color: '#000000'
		                	}
		                },
		                select: {
		                    fill: '#cccccc',
		                    style: {
		                        color: '#000000'
		                    }
		                }
		                
		            }
		        }
		},
		navigator:{
			xAxis:{
				labels:{
					style:{
						color:'#cccccc'
					}
				}
				
			},
			adaptToUpdatedData: false,
			series : {
				data : table1_par
			}
		},
		xAxis:{
			labels:{
				style:{
					color:'#cccccc'
				}
			},
			events : {
				afterSetExtremes : afterSetExtremes
			},
			minRange: 3600 * 1000 // one hour
		},
		yAxis: {
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[config[1]];
				},
				x:20
			},
			title: {
				text: names[config[1]],
				style:{
					color:'#cccccc'
				},
				align:'middle',
				x:15
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			crosshair:true
		},
		plotOptions: {
			series: {
				  turboThreshold: 2000 // 或者更多，必须大于数组长度（1440）
			}
		},
		tooltip: {
			formatter: function () {
				return 	'<b>'+Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '</b><br/>' +
						Highcharts.numberFormat(this.y, 2);
			},
			valueDecimals: 2,
			followTouchMove: false,
			split: true
		},
		series:[{
			type: 'column',
			data:table1_par
		}]
	});
	
	Highcharts.stockChart('container21', {
		chart: {
			zoomType: 'x',
			backgroundColor:'transparent',
		},
		credits:{
			enabled:false
		},
		scrollbar:{
			enabled:false
		},
		rangeSelector: {
			buttons: [
			{
				type: 'hour',
				count: 1,
				text: 'h'
			}, {
				type: 'day',
				count: 1,
				text: 'day'
			},{
				type: 'week',
				count: 1,
				text: 'week'
			},{
				type: 'all',
				text: 'All'
			}],
			inputEnabled: true, // it supports only days
			selected : 4, // all
			labelStyle:{
				color:'#cccccc'
			},
			inputStyle:{
				color:'#cccccc'
			},
			 buttonTheme: { // styles for the buttons
				 stroke:'#cccccc'  , 
				 'stroke-width':1,
				 fill: 'none',
		            border: '2px solid #4CAF50',
		            style: {
		                color: '#cccccc',
		            },
		            states: {
		                hover: {
		                	style:{
		                		color: '#000000'
		                	}
		                },
		                select: {
		                    fill: '#cccccc',
		                    style: {
		                        color: '#000000'
		                    }
		                }
		                
		            }
		        }
		},
		navigator:{
			xAxis:{
				labels:{
					style:{
						color:'#cccccc'
					}
				}
				
			},
			adaptToUpdatedData: false,
			series : {
				data : table2_par
			}
		},
		xAxis:{
			labels:{
				style:{
					color:'#cccccc'
				}
			},
			events : {
				afterSetExtremes : afterSetExtremes2
			},
			crosshair:true,
			minRange: 3600 * 1000 // one hour
		},
		yAxis: {
			labels: {
				style:{
					color:'#cccccc'
				},
				formatter: function () {
					return this.value + ' '+units[config[2]];
				},
				x:20
			},
			title:{
				text: names[config[2]],
				style:{
					color:'#cccccc'
				},
				align:'middle',
				x:15
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			crosshair:true
		},
		plotOptions: {
			series: {
				  turboThreshold: 2000 // 或者更多，必须大于数组长度（1440）
			}
		},
		tooltip: {
			formatter: function () {
				return 	'<b>'+Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '</b><br/>' +
						Highcharts.numberFormat(this.y, 2);
			},
			valueDecimals: 2,
			followTouchMove: false,
			split: true
		},
		series:[{
			type: 'column',
			data:table2_par
		}]
	});
	
	//均料质量饼图
	Highcharts.chart('container22', {
		chart:{
			backgroundColor:'transparent',
		},
		title: {
			text:'',
			floating:true
		},
		tooltip: {
			pointFormat: '<b>{point.percentage:.1f}%</b>',
			formatter: function () {
				return 	'<b>出料'+this.key+'</b><br/>'+this.percentage.toFixed(2)+'%</b>'
			},
		},
		credits:{
			enabled:false
		},
		plotOptions: {
			pie: {
				center:['40%','55%'],
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: false,
					format: '<b>{point.name}</b>: {point.percentage:.1f} %',
				}
			}
		},
		series: [{
			type: 'pie',
			name: '',
			data: [{
				name: '不合格率',
				y: 11,
				color:'rgba(65,105,225,0.1)',
				sliced:true
			}, {
				name: '合格率',
				y: 88,
				color:'rgba(0,255,0,0.6)'
			}]
		}]
	},function (chart) {
		chart.tooltip.refresh(chart.series[0].data[1]);
	});
	//甘特图
	Highcharts.chart('container31', {
		chart: {
			type: 'xrange',
			backgroundColor:'transparent'
		},
		colors:['#00FF00','rgba(255,0,16,0.6)'],
		credits:{
			enabled:false
		},
		legend:{
			enabled:false
		},
		title: {
			text: '',
			floating:true
		},
		plotOptions:{
			columnrange:{
				dataLabels:{
					enabled:false
				}
			}
		},
		xAxis: {
			type: 'datetime',
			dateTimeLabelFormats: {
				week: '%Y/%m/%d'
			},
			labels: {
				style:{
					color:'#cccccc'
				}
			}
		},
		yAxis: {
			title: {
				text: ''
			},
			labels: {
				style:{
					color:'#cccccc'
				}
			},
			gridLineColor:'rgba(156,156,156,0.4)',
			categories: ['正常运行', '停机/维护'],
			reversed: true,
		},
		tooltip: {
			dateTimeLabelFormats: {
				day: '%Y/%m/%d'
			}
		},
		series: [{
			name: '当前状态',
			// pointPadding: 0,
			// groupPadding: 0,
			borderColor: 'gray',
			pointWidth: 20,
			data:table3_par,
			dataLabels: {
				enabled: false
			}
		}]
	});
	// container 33 是能耗和震动
	var chart = Highcharts.chart('container33',{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
//			marginRight: 10,
			zoomType:'x'
    		},
    		colors:['#7cb5ec', '#d3b5ec'],
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
				tickPixelInterval:100,
				labels: {
					style:{
						color:'#cccccc'
					}
				},
		},
		yAxis: [{
				title: {
						text:'能耗系数',
						style:{
							color:'#cccccc'
						}
				},
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value +' A/t' ;
					},
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				plotLines:[
						{//下黄
							color:'#DDDF0D',
							dashStyle:'ShortDot',
							value:eners_min,
							width:2
						},
						{//上黄
							color:'#DDDF0D',
							dashStyle:'ShortDot',
							value:eners_max,
							width:2
						}]
				
			},
			{
				title: {
						text:'振动系数',
						style:{
							color:'#cccccc'
						}
				},
				opposite: true,
	//			lineWidth:1,
				gridLineColor:'rgba(156,156,156,0.4)',
				labels: {
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value +' mm' ;
					},
				},
				plotLines:[
							{//下黄
								color:'#DDDF0D',
								dashStyle:'ShortDot',
								value:shaks_min,
								width:2
							},
							{//上黄
								color:'#DDDF0D',
								dashStyle:'ShortDot',
								value:shaks_max,
								width:2
							}]
		}],
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return 	'<b>'+this.series.name+'</b><br/>'+Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
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
				 events: {
                     //监听图例的点击事件
                     legendItemClick: function (event) {
                         if(this.visible){
                        	 return false;
                         }else{
                        	 var seriesList = this.chart.series;
                        	 for (var i = 0; i < 2; i++) {
                                 if (seriesList[i].name == this.name) {
                                     this.chart.series[i].update({
                                         visible: true
                                     });
                                 } else {
                                     this.chart.series[i].update({
                                         visible: false
                                     }); 
                                 }
                             }
                         }
                         return false;
                     }
                 }
			}
		},
		series: [{
				name:'能耗系数',
				data:table33_par1
		},{
			name:'振动系数',
			data:table33_par2,
			yAxis:1
		}]
    	},function(e){
    		var ener = this.series[0];
    		var shak = this.series[1];
    		shak.hide();
    	})
	//均料质量饼图
	Highcharts.chart('container32', {
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
//				center:['40%','55%'],
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					distance:0,
					format: '<b>{point.name}率</b><br/> {point.percentage:.1f} %',
					style:{
						color:'#cccccc'
					}
				}
			}
		},
		series: [{
			type: 'pie',
			name: '',
			data: table32_par
		}]
	});
	
	//阀门开度和转速
	Highcharts.chart('container41', {
		chart: {
			type: 'solidgauge',
			backgroundColor:'transparent'
		},
		title:null,
		pane: {
			center: ['50%', '90%'],
			size: '180%',
			startAngle: -90,
			endAngle: 90,
			background: {
				backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
				innerRadius: '60%',
				outerRadius: '100%',
				shape: 'arc'
			}
		},
		tooltip: {
			enabled: false
		},
		yAxis: {
			min: minV[config[41]],
			max: maxV[config[41]],
			title: {
				text: null
			},
			stops: [
					[1, 'rgba(101,193,255,0.7)'] //#65C1FF
				],
			lineWidth: 0,
			minorTickInterval: null,
//			tickPixelInterval: (maxV[config[41]]-minV[config[41]]),
			tickWidth: 0,
			title: {
				y: -70
			},
			labels: {
				y: 16,
				style:{
					color:'#cccccc'
				}
			}
		},
		plotOptions: {
			solidgauge: {
				dataLabels: {
					y: 20,
					borderWidth: 0,
					useHTML: true
				}
			}
		},
		credits: {
			enabled: false
		},
		series: [{
			name: '',
			data:table41_par,
			dataLabels: {
				format: '<div style="text-align:center"><span style="font-size:25px;color:#65C1FF">{y}</span><br/>' +
				'<span style="font-size:12px;color:silver">'+names[config[41]]+'<br/>'+ units[config[41]]+'</span></div>'
			},
			tooltip: {
				valueSuffix: '%'
			}
		}]
	});
	
	Highcharts.chart('container42', {
		chart: {
			type: 'solidgauge',
			backgroundColor:'transparent'
		},
		title:null,
		pane: {
			center: ['50%', '90%'],
			size: '180%',
			startAngle: -90,
			endAngle: 90,
			background: {
				backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
				innerRadius: '60%',
				outerRadius: '100%',
				shape: 'arc'
			}
		},
		tooltip: {
			enabled: false
		},
		yAxis: {
			min: minV[config[42]],
			max: maxV[config[42]],
			title: {
				text: null
			},
			stops: [
					[1, 'rgba(101,193,255,0.7)'] //#65C1FF
				],
			lineWidth: 0,
			minorTickInterval: null,
//			tickPixelInterval: (maxV[config[42]]-minV[config[42]]),
			tickWidth: 0,
			title: {
				y: -70
			},
			labels: {
				y: 16,
				style:{
					color:'#cccccc'
				}
			}
		},
		plotOptions: {
			solidgauge: {
				dataLabels: {
					y: 20,
					borderWidth: 0,
					useHTML: true
				}
			}
		},
		credits: {
			enabled: false
		},
		series: [{
			name: '',
			data:table42_par,
			dataLabels: {
				format: '<div style="text-align:center"><span style="font-size:25px;color:#65C1FF">{y}</span><br/>' +
				'<span style="font-size:12px;color:silver">'+names[config[42]]+'<br/>'+ units[config[42]]+'</span></div>'
			},
			tooltip: {
				valueSuffix: '%'
			}
		}]
	});

	//CMH
	var chart = Highcharts.chart('container43',{
    	chart: {
			type: 'spline',
			backgroundColor:'transparent',
//			marginRight: 10,
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
//				maxPadding:0.5,
				crosshair:true,
				labels: {
					style:{
						color:'#cccccc'
					}
				}
		},
		yAxis: {
				title: {
						text: names[config[43]],
						style:{
							color:'#cccccc'
						}
				},
				gridLineColor:'rgba(156,156,156,0.4)',
				tickPixelInterval:25,
//				minPadding:0.4,
				labels:{
					style:{
						color:'#cccccc'
					},
					formatter: function () {
						return this.value + ' '+units[config[43]];
					},
				}
		},
		credits: {
			enabled: false
		},
		tooltip: {
			enabled:true,
			formatter: function () {
						return '<b>'+names[config[43]]+'</b><br/>' +
								Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
								Highcharts.numberFormat(this.y, 2);
				},
			crosshairs: [true, true]
		
		},
		legend: {
				enabled: false,
				itemStyle:{
					color:'#cccccc'
				}
		},
		plotOptions: {
			series: {
				marker: {
					enabled: false
				}
			}
		},
		series: [{
				data:table43_par
		}]
    	});
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