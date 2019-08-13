window.numin = '0';
window.onload=function(){

	
}
//引导参数！
var line_id = 5;
//产线介绍+运行工况(初始化)

var divs = new Array();
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
	    				console.log(map[key]);
						a.innerHTML = i;
						if(key=="STATION_ID"){
							a.id=map[key];
							divs[i]=map[key];
						}
								
						/* if(key =="POSITION_LEFT")
							a.style.left = map[key]+"px";
						if(key == "POSITION_TOP")
							a.style.top = map[key]+"px"; */
						 a.className = "draggable"; 
						
						//alert(123);
	    			}
	    			document.body.appendChild(a);
	    			
	    		}
	    		
	    	},
	 })
}


//初始化div

function addElement(){
	add();
}

var divs = new Array();
var i=0;
function add(){
	var selectid=document.getElementById("editable-select1").value;
	var div = document.createElement('div');
	div.innerHTML = "加载中>>>>";
	div.position = 'absolute';
	console.log(selectid);
	div.id =selectid;
	div.className = "draggable";
	document.getElementById('u189').appendChild(div);
	divs[i]=div.id;
	i++;
	re();
}

function re() {
	  var $draggable = $('.draggable').draggabilly();
	  // Draggabilly instance
	  var draggie = $draggable.data('draggabilly');
	  $draggable.on( 'dragMove', function() {
	    console.log( 'dragMove', draggie.position.x, draggie.position.y );
	  });
	  }

$(function(){
    $(document).click(function (e) {  
        console.log($(e.target).attr('id'));  // 获取id
        del_id=$(e.target).attr('id');
    })
})

function dropElement(){
  var my = document.getElementById(del_id);
    if (my != null && del_id.substring(0,1)=="L"){
    	divs[divs.indexOf(del_id)]=null;
    	divs.splice(divs.indexOf(null),1)
    	i--;
    	my.parentNode.removeChild(my);
    	
    }
        
}

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