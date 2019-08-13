function logout(){
	console.log(123);
	$.cookie("ticket", "", {expires: -1,path:'/'});
    alert("成功登出");
    window.location.href='/mkcenter';
}
//初始化右上角的姓名和button
function initButton(data,j){
	$("#index").mouseover(function(){
		$("#index").css("background-color","rgba(87, 163, 243, 1)");
	});
	$("#index").mouseout(function(){
		$("#index").css("background-color","rgba(255, 255, 255, 0)");
	});
	$("#u240").mouseover(function(){
		$("#u240").css("background-color","rgba(87, 163, 243, 1)");
	});
	$("#u240").mouseout(function(){
		$("#u240").css("background-color","rgba(255, 255, 255, 0)");
	});
	$("#u240").click(function(){
		window.open("data.html?id="+j,"_self");
	});
	$("#u241").mouseover(function(){
		$("#u241").css("background-color","rgba(87, 163, 243, 1)");
	});
	$("#u241").mouseout(function(){
		$("#u241").css("background-color","rgba(255, 255, 255, 0)");
	});
	$("#u241").click(function(){
		window.open("board.html?id="+j,"_self");
	});
	$("#u242").mouseover(function(){
		$("#u242").css("background-color","rgba(87, 163, 243, 1)");
	});
	$("#u242").mouseout(function(){
		$("#u242").css("background-color","rgba(255, 255, 255, 0)");
	});
	$("#u242").click(function(){
		window.open("yewu.html?id="+j,"_self");
	});
	$("#username").html(data);	
}

function check2ad(e){
		var flag = 1;
		$.ajax({
	    	type:'POST',
	    	url:'../checkLogin.do',
	    	timeout:2000,
	    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	success:function(data){
	    		if(data=='0'){
	    			alert('请先登录');
	        		window.location.href='/mkcenter2';
	        		flag=0;
	    		}else{
//	    				window.location.href ='../adms/loading.html';
	    				window.open('../adms/loading.html');
	    		}
	    	},
	    	error:function(e){
	    		alert('初始化失败，将返回主页！');
	    		window.location.href='/mkcenter2';
	    		flag=0;
	    	}
		});
		if(flag ==0){
			return false;
		}else{
			return true;
		}
	}
