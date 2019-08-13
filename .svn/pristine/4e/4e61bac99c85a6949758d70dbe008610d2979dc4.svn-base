var create_win;
var change_win;
var delete_win;
$(document).ready(function(){
	init_etp();
	$("#btn-add").on('click',function(){
		create_win=layer.open({
			  type: 1,
			  title:'新增产线',
		      area: ['600px', '630px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#createLine")
		});
	});
	$("#btn-edit").on('click',function(){
		$(':input','#changeLine') 
		.not(':button, :submit, :reset, :hidden') 
		.val('') 
		.removeAttr('checked') 
		.removeAttr('selected');
		var x = $('#linetable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#change_id").val(x[0].ID);
		$("#changeEntp").html(x[0].COMPANY);
		$("#change_name").val(x[0].NAME);
		$("#change_status").html(status(x[0].STATUS));
		$("#change_time").html(times(x[0].LAST_UPDATE));
		$("#cE_MIN").val(x[0].E_MIN);
		$("#cE_MAX").val(x[0].E_MAX);
		$("#cS_MIN").val(x[0].S_MIN);
		$("#cS_MAX").val(x[0].S_MAX);
		change_win=layer.open({
			  type: 1,
			  title:'修改产线',
		      area: ['600px', '630px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#changeLine")
		});
	});
	$("#btn-delete").on('click',function(){
		var x = $('#linetable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#del_company").html(x[0].COMPANY);
		$("#del_line").html(x[0].NAME);
		$("#del_id").val(x[0].ID);
		delete_win=layer.open({
			  type: 1,
			  title:'删除产线',
		      area: ['400px', '200px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#deleteLine")
		});
	});
	$('#linetable').bootstrapTable({
			ajax:function(request){
				$.ajax({
			    	type:'GET',
			    	url:'getCompanyLines.do',
			    	success:function(data){
			    		//下面这条语句必须存在，不然加载不出数据……原因可能和源码有关 
			    		request.success({ row:data });
			    		var json =  eval("("+data+")");
			    		var dt = [];
			    		for(var key in json.list){
			    			var e = new Array();
			    			var s = new Array();
			    			if(json.list[key].ENERS!=''&&json.list[key].ENERS!=null){
			    				e = json.list[key].ENERS.split(",");
			    			}
			    			if(json.list[key].SHAKS!=''&&json.list[key].SHAKS!=null){
			    				s = json.list[key].SHAKS.split(",");
			    			}
			    			dt.push({
			    				ID:json.list[key].ID,
			    				COMPANY:json.list[key].NAME,
			    				NAME:json.list[key].INSTRUCTION,
			    				STATUS:json.list[key].STATUS,
			    				LAST_UPDATE:json.list[key].LAST_UPDATE,
			    				INFO:json.list[key].INFO,
			    				E_MIN:e[0],
			    				E_MAX:e[1],
			    				S_MIN:s[0],
			    				S_MAX:s[1]
			    			});
			    		}
			    		$('#linetable').bootstrapTable('load', dt);
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
			sortName: "ID",
		    uniqueId: "ID", //每一行的唯一标识，一般为主键列
		    columns: [
		                {	
		                    radio: true,      
		                }, {
		                    field: 'ID',
		                    title: '序号',
		                    sortable: true,
		                }, {
		                    field: 'COMPANY',
		                    title: '所属企业',
		                    sortable:true,   
		                },{
		                	 field: 'NAME',
			                 title: '名称',
			                 sortable: true,
		                },{
		                	 field: 'STATUS',
			                 title: '当前状态',
			                 sortable: true,
			                 formatter:function(value){
			                	 if(value=='-1'){
			                		 return '维修中';
			                	 }
			                	 if(value=='0'){
			                		 return '待机中';
			                	 }
			                	 if(value=='1'){
			                		 return '正常运行';
			                	 }
			                 },
			                 cellStyle:function(value){
			        		    	if(value=="-1"){
			        		    		return {css:{'color':'red'}};
			        		    	}
			        		    	if(value=="0"){
			        		    		return {css:{'color':'yellow'}};
			        		    	}
			        		    	if(value=="1"){
			        		    		return {css:{'color':'#00ff00'}};
			        		    	}
			        		    	return {};
			        		    }
		                },{
		                	 field: 'LAST_UPDATE',
			                 title: '最近更新时间',
			                 sortable: true,
			                 formatter:function(value){
			                	 var time = new Date(parseInt(value-8*3600*1000));
			                	 var year = time.getFullYear();
			                	 var month = time.getMonth()+1;
			                	 var day = time.getDate();
			                	 var h = time.getHours();
			                	 var min = time.getMinutes();
			                	 var s = time.getSeconds();
			                	 return year+'-'+month+'-'+day+' '+h+':'+min+':'+s;
			                 },
		                },{
		                	 field: 'E_MIN',
			                 title: '能耗阈值下<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'该值为产线能耗指数的下阈值，在业务面板中的能耗曲线显示'
		                },{
		                	 field: 'E_MAX',
			                 title: '能耗阈值上<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'该值为产线能耗指数的上阈值，在业务面板中的能耗曲线显示'
		                },{
		                	 field: 'S_MIN',
			                 title: '振动阈值下<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'该值为产线振动指数的下阈值，在业务面板中的振动曲线显示'
		                },{
		                	 field: 'S_MAX',
			                 title: '振动阈值上<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'该值为产线振动指数的上阈值，在业务面板中的振动曲线显示'
		                },{
		                	 field: 'INFO',
			                 title: '介绍',
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
    		$("#companyid").append("<option></option>");
    		for(var key in json.list){
    			$("#companyid").append("<option value='"+json.list[key].ID+"'>"+json.list[key].NAME+"</option>");
    		}
    	}
	});
}
function status(value){
	if(value=='-1'){
		 return '维修中';
	 }
	 if(value=='0'){
		 return '待机中';
	 }
	 if(value=='1'){
		 return '正常运行';
	 }
}
function times(e){
	 var time = new Date(parseInt(e-8*3600*1000));
	 var year = time.getFullYear();
	 var month = time.getMonth()+1;
	 var day = time.getDate();
	 var h = time.getHours();
	 var min = time.getMinutes();
	 var s = time.getSeconds();
	 return year+'-'+month+'-'+day+' '+h+':'+min+':'+s;
}
function createLine(){
	var load;
	var formData = new FormData($("#createLine")[0]);
	if(checkCompany("#companyid","#new_ok")&&checkName("#new_name","#new_ok")&&checkNumber("#E_MIN","#E_MAX","#S_MIN","#S_MAX","#new_ok")){
	$.ajax({
		type:"POST",
		url:"createLine.do",
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
				$("#createLine")[0].reset();
				$("#linetable").bootstrapTable('refresh',{slient:true});
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
function checkCompany(e,b){
	var msg='';
	if($(e).val()==''||$(e).val()==null){
		msg='请选择一个企业'
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
function checkName(e,b){
	var msg = '';
	if($(e).val()==''||$(e).val()==null){
		msg='请填入产线名称';
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
function checkNumber(e,f,g,h,b){
	var msg='';
	var regix = /^[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$/;
	var emin = $(e).val()+'';
	var emax = $(f).val()+'';
	var smin = $(g).val()+'';
	var smax = $(h).val()+'';
	if($(e).val()==''||$(e).val()==null||$(f).val()==''||$(f).val()==null||$(g).val()==''||$(g).val()==null||$(h).val()==''||$(h).val()==null){
		msg='阈值不能为空！';
	}else if(!emin.match(regix)||!emax.match(regix)||!smin.match(regix)||!smax.match(regix)){
		msg ='请输入正确的数字格式！';
	}else if(parseFloat(emin)>=parseFloat(emax)){
		msg='能耗指数上阈值需大于下阈值';
	}else if(parseFloat(smin)>=parseFloat(smax)){
		msg='振动指数上阈值需大于下阈值';
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
function changeLine(){
	var load;
	var formData = new FormData($("#changeLine")[0]);
	if(checkName("#change_name","#change_ok")&&checkNumber("#cE_MIN","#cE_MAX","#cS_MIN","#cS_MAX","#change_ok")){
	$.ajax({
		type:"POST",
		url:"updateLine.do",
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
				$("#linetable").bootstrapTable('refresh',{slient:true});
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

function deleteLine(){
	var formData = new FormData($("#deleteLine")[0]);
	$.ajax({
		type:"POST",
		url:"deleteLine.do",
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
				$("#linetable").bootstrapTable('refresh',{slient:true});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	});
}
