window.numin = '0';
window.onload=function(){
	getLimo();
	getFactories();
	getStations();
	newdiv();		
	getStationData();
	re();
	var it = self.setInterval("refreshFactories()",2000);
	var li = self.setInterval("getLimo()",2000);
	initselect();

}
//引导参数！
var line_id = 5;
//初始化数据库已有数组情况
var divs = new Array();

//产线介绍+运行工况(初始化)

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
	    			var num = parseInt(key)+1;
	    			//console.log(num); num是1-96的数字
	    			var tag = 'L'+pad(num,4);
	    			//console.log(tag); 从L0001到L0145
	    			$('#'+tag).attr("title",json.list[key].NAME+"  单位："+json.list[key].UNIT);
	    		}
	    	},
	    	error:function(e){
	    		alert('加载工位信息失败');
	    	}
	 })
	 //return factories; 
};
//刷新工况
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

//获取图表和定位点
function getLimo(){
	$.ajax({
	    	type:'GET',
	    	url:'../jiangyin/getCompleteData.do',
	    	//async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		console.log(json); //原先的getnew这里的json是一个list(array),但只有一条array
	    		var pieData = [];
	    		for(var key in json.list){
	    			/*for(var i = 1;i<=145;i++){
	    				var l = "L" +pad(i,4);
	    				$("#"+l).html(json.list[key].NAME+" ："+json.list[key].VALUE+json.list[key].UNIT)
	    				//$("#"+l).html(((json.list[key])[l]).toFixed(2));
	    			}*/
	    			var num = parseInt(key)+1;
	    			//console.log(num); num是1-96的数字
	    			var tag = 'L'+pad(num,4);
	    			$("#"+tag).html(json.list[key].NAME+" ："+json.list[key].VALUE.toFixed(2)+json.list[key].UNIT);
	    		}
	    	},
	    	error:function(e){
	    		console.log('获取数据失败');
	    	}
	 })
	 //return factories; 
	 
};


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
	    			for(var key in map){	    				
						a.innerHTML = i;
						if(key=="STATION_ID"){
							a.id=map[key];
							divs[i]=map[key];
							console.log(divs[i]);
						}
								
						/* if(key =="POSITION_LEFT")
							a.style.left = map[key]+"px";
						if(key == "POSITION_TOP")
							a.style.top = map[key]+"px"; */
						 a.className = "draggable"; 
						
						//alert(123);
	    			}
	    			var parentdiv =document.getElementById('u189')
	    			parentdiv.appendChild(a);
	    			
	    		}
	    		
	    	},
	 })
}

function getStationData(){
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
	    			var a = document.getElementById(divs[i]);
	    			console.log(divs[i]);
	    			console.log(a);
	    			for(var key in map){	    				
	    				console.log(map[key]);
	    				a.style.position = 'absolute';	
						if(key =="POSITION_LEFT")
							a.style.left = map[key]+"px";
						if(key == "POSITION_TOP")
							a.style.top = map[key]+"px"; 

	    			}
	    			
	    			
	    		}
	    		
	    	},
	 })
}

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
    		for(var key in json.list){
    			$("#editable-select1").append('<option value='+json.list[key].STAT_ID+'>'+json.list[key].NAME+'</option>');
    		}
//    		$("#u434_input").append('<option value="out">总料位反馈值</option>');
    	},
    	error:function(e){
    		alert('初始化失败');
    	}
	});
}

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
	add();
}


function add(){
	var selectid=document.getElementById("editable-select1").value;
	var div = document.createElement('div');
	div.innerHTML = 111;
	div.position = 'absolute';
	console.log(selectid);
	div.id =selectid;
	div.className = "draggable";
	divs.splice(divs.length-1,0,div.id);
	document.getElementById('u189').appendChild(div);
	re();
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
	  var my = document.getElementById(del_id);
	    if (my != null && del_id.substring(0,1)=="L"){
	    	divs[divs.indexOf(del_id)]=null;
	    	divs.splice(divs.indexOf(null),1)
	    	my.parentNode.removeChild(my);
	    	
	    }
	        
	}


//保存当前页面的所有新建div信息
var divnum = new Array();
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




