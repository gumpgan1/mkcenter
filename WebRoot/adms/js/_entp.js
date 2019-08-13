var create_win;
var change_win;
var delete_win;
var XY = new Array();
$(document).ready(function(){
	initXY();
	$("#btn-add").on('click',function(){
		create_win=layer.open({
			  type: 1,
			  title:'新增企业',
		      area: ['600px', '800px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#createEntp")
		});
	});
	$("#btn-edit").on('click',function(){
		$(':input','#changeEntp') 
		.not(':button, :submit, :reset, :hidden') 
		.val('') 
		.removeAttr('checked') 
		.removeAttr('selected');
		$("#ccity").empty();
		var x = $('#entptable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#change_id").val(x[0].ID);
		$("#change_name").val(x[0].NAME);
		$("#change_address").val(x[0].ADDRESS);
		$("#change_phone").val(x[0].PHONE);
		$("#change_href").val(x[0].HREF);
		$("#change_ins").val(x[0].INSTRUCTION);
		$("#change_Y").val(x[0].LOCATION_Y);
		$("#change_X").val(x[0].LOCATION_X);
		change_win=layer.open({
			  type: 1,
			  title:'修改用户',
		      area: ['600px', '800px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#changeEntp")
		});
	});
	$("#btn-delete").on('click',function(){
		var x = $('#entptable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#del_name").html(x[0].NAME);
		$("#del_id").val(x[0].ID);
		delete_win=layer.open({
			  type: 1,
			  title:'删除用户',
		      area: ['400px', '200px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#deleteCompany")
		});
	});
	$('#entptable').bootstrapTable({
			ajax:function(request){
				$.ajax({
			    	type:'GET',
			    	url:'getSelfCompany.do',
			    	success:function(data){
			    		//下面这条语句必须存在，不然加载不出数据……原因可能和源码有关 
			    		request.success({ row:data });
			    		var json =  eval("("+data+")");
			    		var dt = [];
			    		for(var key in json.list){
			    			dt.push({
			    				ID:json.list[key].ID,
			    				NAME:json.list[key].NAME,
			    				ADDRESS:json.list[key].ADDRESS,
			    				PHONE:json.list[key].PHONE,
			    				HREF:json.list[key].HREF,
			    				INSTRUCTION:json.list[key].INSTRUCTION,
			    				LOCATION_X:json.list[key].LOCATION_X,
			    				LOCATION_Y:json.list[key].LOCATION_Y
			    			});
			    		}
			    		$('#entptable').bootstrapTable('load', dt);
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
			showColumns:true,
			search: true,
			maintainSelected:true,
			sidePagination:"client",
		    uniqueId: "ID", //每一行的唯一标识，一般为主键列
		    columns: [
		                {	
		                    radio: true,
		                    
		                }, {
		                    field: 'ID',
		                    title: '序号',
		                    sortable: true,
		                    
		                }, {
		                    field: 'NAME',
		                    title: '企业名',
		                    sortable:true,
		                    
		                }, {
		                    field: 'ADDRESS',
		                    title: '地址',
		                   
		                }, {
		                    field: 'PHONE',
		                    title: '联系电话',
		                    
		                },{
		                    field: 'HREF',
		                    title: '官网',
		                  
		                }, {
		                    field: 'INSTRUCTION',
		                    title: '介绍',
		                }, {
		                    field: 'LOCATION_Y',
		                    title: '经度',
	                 
		                }, {
		                    field: 'LOCATION_X',
		                    title: '纬度',
		                   
		                }
		            ],
	});
});
//初始化省市经纬json
function initXY(){
		$.ajax({
			type:"GET",
			url:'js/china.json',
			success:function(data){
					$("#province").append("<option>-</option>");
					$("#cprovince").append("<option>-</option>");
					$("#city").append("<option></option>");
					$("#ccity").append("<option></option>");
				for( var key in data ){
					$("#province").append("<option>"+data[key].name+"</option>");
					$("#cprovince").append("<option>"+data[key].name+"</option>")
					for(var key2 in data[key].children){
						XY[data[key].name] = data[key].children;
					}
				}
			},
		});		
}

$("#province").on('change',function(){
	var t = $(this).find('option:selected').text();
	$("#city").empty();
	$("#city").append("<option></option>");
	for(var key in XY[t]){
		var loc = XY[t][key].log+";"+XY[t][key].lat;
		$("#city").append("<option>"+XY[t][key].name+"</option>");
	}
})
$("#cprovince").on('change',function(){
	var t = $(this).find('option:selected').text();
	$("#ccity").empty();
	$("#ccity").append("<option></option>");
	for(var key in XY[t]){
		var loc = XY[t][key].log+";"+XY[t][key].lat;
		$("#ccity").append("<option>"+XY[t][key].name+"</option>");
	}
})
$("#city").on('change',function(){
	var p = $("#province").find('option:selected').text();
	var c = $("#city").find('option:selected').text();
	for(var key in XY[p]){
		if(XY[p][key].name==c){	
			$("#new_X").val(XY[p][key].log);
			$("#new_Y").val(XY[p][key].lat);
		}
	}
})
$("#ccity").on('change',function(){
	var p = $("#cprovince").find('option:selected').text();
	var c = $("#ccity").find('option:selected').text();
	for(var key in XY[p]){
		if(XY[p][key].name==c){	
			$("#change_X").val(XY[p][key].log);
			$("#change_Y").val(XY[p][key].lat);
		}
	}
})
//重写重置按钮方法
$("#new_reset").on('click',function(){
	$(':input','#createEntp') 
	.not(':button, :submit, :reset, :hidden') 
	.val('') 
	.removeAttr('checked') 
	.removeAttr('selected');
	$("#city").empty();
})
function createCompany(){
	var load;
	var formData = new FormData($("#createEntp")[0]);
	if(checkName("#new_name",'#new_ok')&&checkLocation("#city",'#new_ok')&&checkXY("#new_X","#new_Y",'#new_ok')){
	$.ajax({
		type:"POST",
		url:"createCompany.do",
		data:formData,
		contentType: false, 
	    processData: false, 
		beforeSend:function(){
			load = layer.load(0, {shade: false});
		},
		success:function(data){
			layer.close(load);
			if(data=='0'){
				layer.msg('新建成功', {
					  icon: 1,
					  time: 2500 //2秒关闭（如果不配置，默认是3秒）
				});
				layer.close(create_win);
				$("#createEntp")[0].reset();
				$("#entptable").bootstrapTable('refresh',{slient:true});
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
function checkName(e,b){
	var msg = '';
	if($(e).val()==''||$(e).val()==null){
		msg='请填入企业名字';
	}
	if(msg!=''){
		layer.tips(msg,b,{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}
function checkLocation(e,b){
	var msg='';
	if($(e).val()==''||$(e).val()==null){
		msg='请选择一个城市'
	}
	if(msg!=''){
		layer.tips(msg,b,{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}
function checkXY(e,f,b){
	var msg='';
	var x = $(e).val();
	var y = $(f).val();
	var reg_x = /^[\-\+]?(0?\d{1,2}\.\d{1,5}|1[0-7]?\d{1}\.\d{1,5}|180\.0{1,5})$/;
	var reg_y = /^[\-\+]?([0-8]?\d{1}\.\d{1,5}|90\.0{1,5})$/;
	if(x==''||y==''||x==null||y==null){
		msg = '请输入经/纬值';
	}else if(!x.match(reg_x)){
		msg = '请输入正确的纬度';
	}else if(!y.match(reg_y)){
		msg = '请输入正确的经度';
	}
	if(msg!=''){
		layer.tips(msg,b,{
			time:2000
		});
		return false;
	}else{
		return true;
	}
	
}
function changeCompany(){
	var load;
	var formData = new FormData($("#changeEntp")[0]);
	if(checkName("#change_name",'#change_ok')&&checkXY("#change_X","#change_Y",'#change_ok')){
	$.ajax({
		type:"POST",
		url:"updateCompany.do",
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
				$("#entptable").bootstrapTable('refresh',{slient:true});
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

function closeDel(){
	layer.close(delete_win);
}

function deleteCompany(){
	var formData = new FormData($("#deleteCompany")[0]);
	$.ajax({
		type:"POST",
		url:"deleteCompany.do",
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
				$("#entptable").bootstrapTable('refresh',{slient:true});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	});
}
