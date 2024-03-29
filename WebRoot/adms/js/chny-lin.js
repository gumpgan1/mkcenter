window.onload = function(){
	loading = layer.load(1, {shade: false});
	setTime();
	setInterval("setTime()",1000);
	login();
}
var self_win;
var loading;
function setTime(){
	var time = new Date().format('hh:mm:ss');		
	$("#time").html(time);
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
    	url:'../checkPr.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		if(data=='0'){
    			alert('请先登录');
        		window.location.href='/mkcenter';
        		return;
    		}
    		var t = data.split('&');
    		$("#user").html(t[0]);
    		initSN(t[1]);
    		console.log(t[1]);
    		initIndex(t[1],t[0]);
    		$("#"+pageName()).addClass('selected');
    		initPage(pageName());
    	},
    	error:function(e){
    	}
	});
}
function initPage(e){
	if(e=='_index'){
		layer.close(loading);
	}
	if(e=='_self'){
		$.ajax({
	    	type:'POST',
	    	url:'getUser.do',
	    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
	    	beforeSend:function(){
			},
	    	success:function(data){
	    		var json =  eval("("+data+")");
	    		$("#uid").html(json.list[0].USERID);
	    		$("#username").html(json.list[0].USERNAME);
	    		var t='';
	    		switch(json.list[0].ROLE){
		    		case  0: t= '用户'; break;
		    		case  1: t= '设计员'; break;
		    		case 99: t= '管理员'; break;
		    		default: break;
	    		}
	    		$("#prev").html(t);
	    		$("#name").val(json.list[0].NAME);
	    		$(":radio[name='sex'][value='" + json.list[0].SEX + "']").prop("checked", "checked"); 
	    		$("#phone").val(json.list[0].PHONE);
	    		$("#email").val(json.list[0].EMAIL);
	    		$("#address").val(json.list[0].ADDRESS);
	    		$("#instruction").val(json.list[0].INSTRUCTION);
	    		var img = arrayBufferToBase64(json.list[0].PHOTO);
	    		$("#selfImg").attr('src','data:image/png;base64,'+img);
	    	},
	    	complete:function(e){
	    		layer.close(loading);
	    	}
		});
	}
	if(e=='_usem'){
		layer.close(loading);
	}
	if(e=='_entp'){
		layer.close(loading);
	}
	if(e=='_line'){
		layer.close(loading);
	}
	if(e=='_stat'){
		layer.close(loading);
	}
	if(e=='_para'){
		layer.close(loading);
	}
	if(e=='_screen'){
		layer.close(loading);
	}
}

function arrayBufferToBase64(buffer) {
    var binary = '';
    var bytes = new Uint8Array(buffer);
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
        binary += String.fromCharCode( bytes[ i ] );
    }
    return window.btoa( binary );
}
function logout(){
	$(window).off('beforeunload');
	$.cookie("ticket", "", {expires: -1,path:'/'}); 
    window.location.href='/mkcenter';
    alert("成功登出");
}
function initSN(e){
	var f='';
	if(e=='0'){
		return;
	}else if(e=='1'){//设计人员视图
		f += '<li id="_usem"><a href="_usem.html"><i class="fa fa-users"></i> 用户管理</a></li>';
		f +='<li id="_entp"><a href="_entp.html"><i class="fa fa-institution"></i> 企业管理</a></li>';
        f +='<li id="_line"><a href="_line.html"><i class="fa fa-list-ol"></i> 产线管理</a></li>';
        f +='<li id="_stat"><a href="_stat.html"><i class="fa fa-gears"></i> 工位管理</a></li>';
        f +='<li id="_para"><a href="_para.html"><i class="fa fa-pencil-square"></i> 参数管理</a></li>';
        f +='<li id="_screen"><a href="_screen.html"><i class="fa fa-window-restore"></i> 大屏配置</a></li>';
		$("#active").append(f);
	}else if(e=='99'){
		f += '<li id="_usem"><a href="_usem.html"><i class="fa fa-users"></i> 用户管理</a></li>';
		f +='<li id="_entp"><a href="_entp.html"><i class="fa fa-institution"></i> 企业管理</a></li>';
        f +='<li id="_line"><a href="_line.html"><i class="fa fa-list-ol" style="margin-left:1px"></i> 产线管理</a></li>';
        f +='<li id="_stat"><a href="_stat.html"><i class="fa fa-gears"></i> 工位管理</a></li>';
        f +='<li id="_para"><a href="_para.html"><i class="fa fa-pencil-square"></i> 参数管理</a></li>';
        f +='<li id="_screen"><a href="_screen.html"><i class="fa fa-window-restore"></i> 大屏配置</a></li>';
		$("#active").append(f);
	}else{
		console.log('?');
		return;
	}
}
function initIndex(e,f){
	var t ='';
	switch(e){
	case '0': t= '用户'; break;
	case '1': t= '设计员'; break;
	case '99': t= '管理员'; break;
	default: break;
	}
	$("#role").html(t);
	$("#name").html(f);
}
function pageName()
{
  var a = location.href;
  var b = a.split("/");
  var c = b.slice(b.length-1, b.length).toString(String).split(".");
  //console.log(c);//c是一个数组，根据url得出的
  return c.slice(0, 1);
}
$("#changePassword").on('click',function(){
	self_win=layer.open({
		  type: 1,
		  title:'修改密码',
	      area: ['400px', '300px'],
	      shadeClose: true, //点击遮罩关闭
	      content: $("#changeP")
	})
})
$("#uploadimage").on('change',function(){
	var t = $("#uploadimage").val();
		if(t==''||t==null){
			return;
		}else{
			var type = $("#uploadimage")[0].files[0].type;
			if(type=='image/jpeg'||type=='image/jpg'||type=='image/png'){
				var url = window.URL.createObjectURL($("#uploadimage")[0].files[0]);
				$("#selfImg").attr('src',url);
			}else{
				layer.tips('请选择正确的格式','#uploadimage',{
					time:2000
				});
			}
		}
})
function checkP(){
	var reg = /^\w{6,16}$/;
	var oldP = $("#oldP").val();
	var newP = $("#newP").val();
	var reP  = $("#reP").val();
	var msg='';
	if(oldP==''||newP==''||reP==''){
		msg="密码不能为空！";
	}else if(oldP == newP){
		msg="新旧密码相同,请检查!";
	}else if(!newP.match(reg)){
		msg="密码不符合规则，请重新设定;"
	}else if(newP!=reP){
		msg="两次新密码不相同，请检查!";
	}
	if(msg!=''){
		layer.tips(msg,'#btn_cp',{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}
function changeP(){
	var load;
	if(checkP()){
		$.ajax({
			type:"POST",
			url:"changeP.do",
			data:$("#changeP").serialize(),
			beforeSend:function(){
				load = layer.load(0, {shade: false});
			},
			success:function(data){
				layer.close(load);
				if(data=='修改成功！'){
					layer.msg(data, {
						  icon: 1,
						  time: 1500 //2秒关闭（如果不配置，默认是3秒）
					});
					setTimeout(function(){
						layer.close(self_win);
					},2500);
				}else{
					layer.msg(data, {
						  icon: 2,
						  time: 3000 //2秒关闭（如果不配置，默认是3秒）
					});
				}
				
			},
		});
	}
}
function checkTel(){
	var reg = /^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\d{8}$/;
	var tel = $("#phone").val();
	if(tel==''||tel==null){
		return true;
	}
	if(!tel.match(reg)){
		layer.tips('请输入正确的手机号码','#phone',{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}
function checkEmail(){
	var reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	var mail = $("#email").val();
	if(mail==''||mail==null){
		return true;
	}
	if(!mail.match(reg)){
		layer.tips('请输入正确的邮箱地址','#email',{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}
function undateSelf(){
	var load;
	if($("#uploadimage").val()!='' && $("#uploadimage").val()!=null){
		var type = $("#uploadimage")[0].files[0].type;
		if(type!='image/jpeg'&&type!='image/jpg'&&type!='image/png'){
			layer.tips('请选择正确的格式','#uploadimage',{
				time:2000
			});
			return;
		}
	}
	var formData = new FormData($("#selfInfo")[0]);
	if(checkTel()&&checkEmail()){
		$.ajax({
			type:"POST",
			url:"changeSelf.do",
			data:formData,
			contentType: false, 
		    processData: false, 
			beforeSend:function(){
				load = layer.load(0, {shade: false});
			},
			success:function(data){
				layer.close(load);
				if(data=='修改成功！'){
					layer.msg(data, {
						  icon: 1,
						  time: 1500 //2秒关闭（如果不配置，默认是3秒）
					});
				}else{
					layer.msg(data, {
						  icon: 2,
						  time: 3000 //2秒关闭（如果不配置，默认是3秒）
					});
				}
			},
		});
	}
}
//----------------------usem Page Start