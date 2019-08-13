var red  = 'rgba(255,0,0,0.8)';
var yellow = 'rgba(255,229,0,0.8)';
var green = 'rgba(0,255,0,0.8)';
window.selected=[];
window.t = 0;
function setColor(e){
	if(e=='1'){
		return green;
	}else if(e=='0'){
		return yellow;
	}else if(e=='-1'){
		return red;
	}else{
		alert('数据有问题1！');
	}
}
function check(e){
	var flag = 1;
	$.ajax({
    	type:'POST',
    	url:'../checkLogin.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data=='0'){
    			alert('请先登录');
        		window.location.href='/mkcenter';
        		flag=0;
    		}
    	},
    	error:function(e){
    		alert('初始化失败，将返回主页！');
    		window.location.href='/mkcenter';
    		flag=0;
    	}
	});
	if(flag ==0){
		return false;
	}else{
		return true;
	}
}
function setStatus(e){
	if(e=='1'){
		return 'u58.png';
	}else if(e=='0'){
		return 'u68.png';
	}else if(e=='-1'){
		return 'u63.png';
	}else{
		alert('数据有问题2！');
	}
}
function setChosen(e){
	if(e==window.t){
		for(var i=0;i<window.selected.length;i++){
			var old =window.document.getElementById(window.selected[i]);
			old.style.backgroundColor='transparent';
			old.style.color='#cccccc';
		}
		window.selected=[];
		window.t=0;
	}else{
	$.ajax({
    	type:'GET',
    	url:'../limo/point.do?company_id='+e,
    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json =  eval("("+data+")");
    		if(window.selected.length>0){
    			for(var i=0;i<window.selected.length;i++){
    				var old =window.document.getElementById(window.selected[i]);
    				old.style.backgroundColor='transparent';
    				old.style.color='#cccccc';
    			}
    			window.selected=[];
    		}
    		for(var key in json.list){
    			var tmp = window.document.getElementById(json.list[key].ID);
    			tmp.style.backgroundColor = '#cccccc';
				tmp.style.color='#000000';
				window.selected.push(json.list[key].ID);
    		}
    		window.t=e;
    	},
    	error:function(e){
    		alert('获取数据失败');
    	}
		})
	}
}
window.onload=function(){
	getLines();
	getTime();
	var time = self.setInterval("getTime()",1000);
	var line = self.setInterval("refreshLines()",2000);
	var fact = self.setInterval("getFactories()",5000);
}

function getTime(){ 
    function p(s) {
        return s < 10 ? '0' + s: s;
    }
	var date=new Date();   
	var year=date.getFullYear(); //获取当前年份   
	var mon=date.getMonth()+1; //获取当前月份   
	var da=date.getDate(); //获取当前日   
	var day=date.getDay(); //获取当前星期几   
	var h=date.getHours(); //获取小时   
	var m=date.getMinutes(); //获取分钟   
	var s=date.getSeconds(); //获取秒   
	var d=document.getElementById('showtime');   
	var d1;
	switch(day){
		case 1:
			d1="一";break;
		case 2:
			d1="二";break;
		case 3:
			d1="三";break;
		case 4:
			d1="四";break;
		case 5:
			d1="五";break;
		case 6:
			d1="六";break;
		case 0:
			d1="日";break;
	}
	console.log(d1);
	d.innerHTML='当前时间 : '+year+'年 '+mon+'月 '+da+'日 '+'星期'+d1+' '+h+':'+p(m)+':'+p(s);   
}

//获取工厂信息
function getFactories(){
	var factories=[];
	$.ajax({
	    	type:'GET',
	    	url:'../limo/factories.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		var num = 0;
	    		for(var key in json.list){
	    			//console.log(json.list[key]);
	    			factories.push({
	    				id:json.list[key].ID,
	    				lon:json.list[key].LOCATION_X,
	    				lat:json.list[key].LOCATION_Y,
	    				name:json.list[key].NAME,
	    				color:setColor(json.list[key].FLAG),
	    				address:json.list[key].ADDRESS,
	    				ins:json.list[key].INSTRUCTION,
	    				numin:(json.list[key].NUMIN).toFixed(2),
	    				numout:(json.list[key].NUMOUT).toFixed(2),
	    				suml:json.list[key].SUML
	    			});
	    			num++;
	    		}
	    		map.series[1].setData(factories);
	    		$("#e_num").html(num);
	    	},
	    	error:function(e){
//	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};
//获取厂线信息(初始化)
function getLines(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/lines.do?',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		var num_1 = 0,num_2=0,num_3=0;
	    		var num = 0;
	    		var out = 0;
	    		for(var key in json.list){
	    			//console.log(json.list[key]);
	    			if(json.list[key].STATUS=='1'){
	    				num_1++;
	    			}else if(json.list[key].STATUS=='0'){
	    				num_2++;
	    			}else if(json.list[key].STATUS=='-1'){
	    				num_3++;
	    			}else{
	    				alert('数据有问题3！');
	    			}
	    			num++;
	    			out+=json.list[key].NUM_OUT;
	    			var imgsrc = setStatus(json.list[key].STATUS);
	    			//var time = json.list[key].LAST_UPDATE-json.list[key].LAST_BEGIN;
	    			var time = new Date().getTime()-json.list[key].LAST_BEGIN+8*3600*1000;
	    			var t='0';
	    			if(json.list[key].STATUS=='1'){
	    				var h = Math.floor(time/1000/3600);
		    			var m =	Math.floor((time/1000-h*3600)/60);
		    			var s = Math.floor(time/1000-h*3600-m*60);
		    			var ms = time-h*3600*1000-m*60*1000-s*1000;
		    			t = h+":"+m+":"+s+":"+ms;
	    			}
	    			var row = '<tr id='+json.list[key].ID+'><td><img id="img'+json.list[key].ID+'" src="images/index/'+imgsrc+'"></td><td>';
	    			if(json.list[key].HREF!=null){
	    				row+='<a target="_blank" href="'+json.list[key].HREF+'" onclick="return check();">'+json.list[key].INSTRUCTION+'</a>';
	    			}else{
	    				row+=json.list[key].INSTRUCTION;
	    			}
	    			row+='</td><td title='+json.list[key].NAME+'>'+json.list[key].NAME+'</td><td id="in'+json.list[key].ID+'">'+json.list[key].NUM_IN.toFixed(2)+'</td><td id="out'+json.list[key].ID+'">'+ json.list[key].NUM_OUT.toFixed(2)+'</td><td id="time'+json.list[key].ID+'">'+t+'</td></tr>';
	    			$("#tfhover tbody:last").append(row);
	    		}
	    		$("#l_num").html(num);
	    		$("#t_out").html(out.toFixed(0));
	    		$("#num_1").html(num_1);
	    		$("#num_3").html(num_2);
	    		$("#num_2").html(num_3);
	    	},
	    	error:function(e){
	    		alert("初始化失败！")
	    	}
	 })
	 //return factories; 
};
//获取厂线信息(刷新)
function refreshLines(){
	$.ajax({
	    	type:'GET',
	    	url:'../limo/lines.do?',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		var num_1 = 0,num_2=0,num_3=0;
	    		var num = 0;
	    		var out = 0;
	    		for(var key in json.list){
	    			//console.log(json.list[key]);
	    			if(json.list[key].STATUS=='1'){
	    				num_1++;
	    			}else if(json.list[key].STATUS=='0'){
	    				num_2++;
	    			}else if(json.list[key].STATUS=='-1'){
	    				num_3++;
	    			}else{
	    				alert('数据有问题4！');
	    			}
	    			num++;
	    			out+=json.list[key].NUM_OUT;
	    			var imgsrc = setStatus(json.list[key].STATUS);
	    			//var time = json.list[key].LAST_UPDATE-json.list[key].LAST_BEGIN;
	    			var t='0';
	    			var time = new Date().getTime()-json.list[key].LAST_BEGIN+8*3600*1000;
	    			if(json.list[key].STATUS=='1'){
	    				var h = Math.floor(time/1000/3600);
		    			var m =	Math.floor((time/1000-h*3600)/60);
		    			var s = Math.floor(time/1000-h*3600-m*60);
		    			var ms = time-h*3600*1000-m*60*1000-s*1000;
		    			t = h+":"+m+":"+s+":"+ms;
	    			}
	    			if($(json.list[key].ID).length>0){
	    				$('#img'+json.list[key].ID).attr("src",'images/index/'+imgsrc);
	    				$('#in'+json.list[key].ID).html(json.list[key].NUM_IN.toFixed(2));
	    				$('#out'+json.list[key].ID).html(json.list[key].NUM_OUT.toFixed(2));
	    				$('#time'+json.list[key].ID).html(t);
	    			}else{
	    				var row = '<tr id='+json.list[key].ID+'><td><img id="img'+json.list[key].ID+'" src="images/index/'+imgsrc+'"></td><td>';
		    			if(json.list[key].HREF!=null){
		    				row+='<a href="'+json.list[key].HREF+'" onclick="check()>'+json.list[key].INSTRUCTION+'</a>';
		    			}else{
		    				row+=json.list[key].INSTRUCTION;
		    			}
		    			row+='</td><td title='+json.list[key].NAME+'>'+json.list[key].NAME+'</td><td id="in'+json.list[key].ID+'">'+json.list[key].NUM_IN.toFixed(2)+'</td><td id="out'+json.list[key].ID+'">'+ json.list[key].NUM_OUT.toFixed(2)+'</td><td id="time'+json.list[key].ID+'">'+t+'</td></tr>';
		    			$("#tfhover tbody:last").append(row);
	    			}
	    		}
	    		$("#l_num").html(num);
	    		$("#t_out").html(out.toFixed(0));
	    		$("#num_1").html(num_1);
	    		$("#num_3").html(num_2);
	    		$("#num_2").html(num_3);
	    	},
	    	error:function(e){
//	    		alert('获取数据失败');
	    	}
	 })
	 //return factories; 
};
//地图初始化
var map = new Highcharts.Map('container_map', {
	title: {
		text:null
	},
	chart:{
		backgroundColor:'transparent',
	},
	credits:{
		enabled:false
	},
	legend:{
		enabled:false
	},
	mapNavigation: {
		enabled: true,
		buttonOptions: {
			verticalAlign: 'bottom',
			theme:{
				fill:'rgba(204,204,204,0.1)',
				states:{
					hover:{
						fill:'rgba(204,204,204,0.8)'
					}
				}
			}
		}
	},
	tooltip: {
		useHTML: true,
		followPointer:false,
		formatter: function() {//提示框格式化
			return '<b>'+this.point.name+'</b><br/>'+this.point.address+'<br/>产线数量：<b>'+this.point.suml+'&nbsp;&nbsp;|&nbsp;&nbsp;</b>当前产量:<b>'+this.point.numout+'</b>';
		}
	},
	plotOptions: {//extends from Scatter.PlotOptions
		series: {
			dataLabels: {
				enabled: false
			},
			marker: {
				radius: 5
			},
			events:{
			click:function(e){
				//parent.window.document.getElementById('u16_state0').scrollTop=52*4;
				setChosen(e.point.id);				
			}
		},
		}
	},
	series: [{
		// 空数据列，用于展示底图
		mapData: Highcharts.maps['cn/china'],
		name:'BaseMap',
		nullColor:'rgba(204,204,204,0.1)',
		showInLegend: false
	},{
		type: 'mappoint',
		
		data:getFactories()
	}]
});

 
