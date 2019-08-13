var create_win;
var change_win;
var delete_win;
$(document).ready(function(){
	init_etp()
	init_line()
	$("#btn-add").on('click',function(){
		create_win=layer.open({
			  type: 1,
			  title:'新增用户',
		      area: ['400px', '350px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#createUser")
		});
	});
	$("#btn-edit").on('click',function(){
		var x = $('#usemtable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#changeNo").html(x[0].USERID);
		$("#changeU").val(x[0].USERNAME);
		$("#No").val(x[0].USERID);
		var t;
		switch(x[0].ROLE){
		  case '管理员':t= 99;break;
		  case '设计人员': t= 1;break;
		  case '普通用户': t= 0;break;
		  default:break;
		}
		$(":radio[name='autority'][value='" + t + "']").prop("checked", "checked"); 
		change_win=layer.open({
			  type: 1,
			  title:'修改用户',
		      area: ['400px', '350px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#changeUser")
		});
	});
	$("#btn-delete").on('click',function(){
		var x = $('#usemtable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#del_role").html(x[0].ROLE);
		$("#del_name").html(x[0].USERNAME);
		$("#del_id").val(x[0].USERID);
		delete_win=layer.open({
			  type: 1,
			  title:'删除用户',
		      area: ['400px', '200px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#deleteUser")
		});
	});
	$('#usemtable').bootstrapTable({
			ajax:function(request){
				$.ajax({
			    	type:'GET',
			    	url:'getCompanyUser.do',
			    	success:function(data){
			    		//下面这条语句必须存在，不然加载不出数据……原因可能和源码有关 
			    		request.success({ row:data });
			    		var json =  eval("("+data+")");
			    		var dt = [];
			    		for(var key in json.list){
			    			var t =''  ;
			    			switch(json.list[key].ROLE){
			    			  case 99:t= '管理员';break;
			    			  case 1: t= '设计人员';break;
			    			  case 0: t= '普通用户';break;
			    			  default:break;
			    			}
			    			dt.push({
			    				USERID:json.list[key].USERID,
			    				USERNAME:json.list[key].USERNAME,
			    				ROLE:t
			    			});
			    		}
			    		$('#usemtable').bootstrapTable('load', dt);
			    	},
			    	error:function(e){
			    		layer.msg('加载失败，请刷新页面', {
							  icon: 2,
							  time: 1500 //2秒关闭（如果不配置，默认是3秒）
						});
			    	}
				});
			},
			pagination: true,
			toolbar:'#toolbar',
			singleSelect:true,
			queryParamsType : "undefined",
			clickToSelect:true,	
			pageSize: 10,
			pageNumber: 1,
			pageList: "[10, 25, 50, 100, All]",
			showToggle: true,
			showRefresh: true,
			search: true,
			maintainSelected:true,
			sidePagination:"client",
		    uniqueId: "USERID", //每一行的唯一标识，一般为主键列
		    columns: [
		                {	
		                    radio: true
		                }, {
		                    field: 'USERID',
		                    title: 'UID',
		                    sortable: true
		                }, {
		                    field: 'USERNAME',
		                    title: '用户名',
		                    sortable:true
		                }, {
		                    field: 'ROLE',
		                    title: '权限',
		                    sortable:true,
		                    cellStyle:function(value){
		        		    	if(value=="管理员"){
		        		    		return {css:{'color':'red'}};
		        		    	}
		        		    	if(value=="设计人员"){
		        		    		return {css:{'color':'blue'}};
		        		    	}
		        		    	return {};
		        		    }
		                }
		            ],
	});
});

function init_etp(){
	$.ajax({
		type:'GET',
    	url:'getSelfCompany.do',
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#companyid").append("<option>-请选择公司-</option>");
    		for(var key in json.list){
    			$("#companyid").append("<option value='"+json.list[key].ID+"'>"+json.list[key].NAME+"</option>");
    		}
    	}
	});
}

function init_line(){
	$.ajax({
		type:'GET',
		url:'getCompanyLines.do',
		success:function(data){
			var json =  eval("("+data+")");
			console.log(json);
			$("#lineid").append("<option value='0'>-请选择产线-</option>");
			for(var key in json.list){
				$("#lineid").append("<option value='"+json.list[key].ID+"'>"+json.list[key].INSTRUCTION+"</option>");
			}
		}
	});
}

function createUser(){
	var load;
	var formData = new FormData($("#createUser")[0]);
	if(checkvalidate($("#newU").val())){
		$.ajax({
			type:"POST",
			url:"createUser.do",
			data:formData,
			contentType: false, 
		    processData: false, 
			beforeSend:function(){
				load = layer.load(0, {shade: false});
			},
			success:function(data){
				layer.close(load);
				if(data=='0'){
					layer.msg('创建成功', {
						  icon: 1,
						  time: 2500 //2秒关闭（如果不配置，默认是3秒）
					});
					layer.close(create_win);
					$("#usemtable").bootstrapTable('refresh',{slient:true});
					$("#createUser")[0].reset();
				}else{
					layer.msg(data, {
						  icon: 2,
						  time: 2000 //2秒关闭（如果不配置，默认是3秒）
					});
				}
			},
		});
	}
}

function changeUser(){
	var load;
	var formData = new FormData($("#changeUser")[0]);
	if(checkvalidate($("#changeU").val())){
	$.ajax({
		type:"POST",
		url:"updateUser.do",
		data:formData,
		contentType: false, 
	    processData: false, 
		beforeSend:function(){
			load = layer.load(0, {shade: false});
		},
		success:function(data){
			layer.close(load);
			if(data=='0'){
				layer.msg('更改成功', {
					  icon: 1,
					  time: 2500 //2秒关闭（如果不配置，默认是3秒）
				});
				layer.close(change_win);
				$("#usemtable").bootstrapTable('refresh',{slient:true});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	});
	}
}

function checkvalidate(t){
	var reg = /^\w{6,16}$/;
	var msg = '';
	if(t==''||t==null){
		msg =  "请输入用户名";
	}else if(!t.match(reg)){
		msg = "请输入正确格式的用户名";
	}
	if(msg!=''){
		layer.tips(msg,'#new_ok',{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}

function closeDel(){
	layer.close(delete_win);
}

function deleteUser(){
	var formData = new FormData($("#deleteUser")[0]);
	$.ajax({
		type:"POST",
		url:"deleteUser.do",
		data:formData,
		contentType: false, 
	    processData: false, 
		beforeSend:function(){
			load = layer.load(0, {shade: false});
		},
		success:function(data){
			layer.close(load);
			if(data=='0'){
				layer.msg('删除成功', {
					  icon: 1,
					  time: 2500 //2秒关闭（如果不配置，默认是3秒）
				});
				layer.close(delete_win);
				$("#usemtable").bootstrapTable('refresh',{slient:true});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	});
}

