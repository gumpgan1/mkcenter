var create_win;
var change_win;
var delete_win;
var x = new Array();
var line = new Array();
$(document).ready(function(){
	init_line();
	$("#btn-add").on('click',function(){
		create_win=layer.open({
			  type: 1,
			  title:'新增工位',
		      area: ['600px', '600px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#createStat")
		});
	});
	$("#btn-edit").on('click',function(){
		$(':input','#changeStat') 
		.not(':button, :submit, :reset, :hidden') 
		.val('') 
		.removeAttr('checked') 
		.removeAttr('selected');
		var x = $('#stattable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#crelate1n").empty();
		$("#crelate2n").empty();
		$("#crelate1n,#crelate2n").append("<option value=''></option>");
		$.ajax({
			type:"GET",
			url:"getStatByLine.do?lineid="+parseInt(line[x[0].LINE]),
			async:false,
			success:function(data){
				var json =  eval("("+data+")");
				for(var key in json.list){
					$("#crelate1n,#crelate2n").append("<option value='"+json.list[key].STAT_ID+"'>"+json.list[key].NAME+"</option>")
				}
			},
		})
		$("#change_id").val(x[0].ID);
		$("#change_line").html(x[0].LINE);
		$("#change_statid").html(x[0].STAT_ID);
		$("#change_name").val(x[0].NAME);
		$("#change_nickname").val(x[0].NICKNAME);
		$("#change_unit").val(x[0].UNIT);
		$("#change_instruction").val(x[0].INSTRUCTION);
		$("#cmin").val(x[0].MIN);
		$("#cmax").val(x[0].MAX);
		$("#crelate1n option:contains("+x[0].RELATE_1+")").attr('selected', true);
		$("#crelate1v").val(x[0].RELATE_1_VALUE);
		$("#crelate2n option:contains("+x[0].RELATE_2+")").attr('selected', true);
		$("#crelate2v").val(x[0].RELATE_2_VALUE);
		console.log(x[0].ABNORMAL);
		var t;
		switch(x[0].ABNORMAL){
		  case "否":t= 0;break;
		  case "是": t= 1;break;
		  default:break;
		}
		$(":radio[name='abnormal'][value='" + t + "']").prop("checked", "checked"); 
		change_win=layer.open({
			  type: 1,
			  title:'修改工位',
		      area: ['600px', '600px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#changeStat")
		});
	});
	$("#btn-delete").on('click',function(){
		var x = $('#stattable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#del_line").html(x[0].LINE);
		$("#del_stat").html(x[0].NAME);
		$("#del_id").val(x[0].ID);
		delete_win=layer.open({
			  type: 1,
			  title:'删除工位',
		      area: ['400px', '200px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#deleteStat")
		});
	});
	$("#btn-abnormal").on('click',function(){
		$(':input','#changeStat') 
		.not(':button, :submit, :reset, :hidden') 
		.val('') 
		.removeAttr('checked') 
		.removeAttr('selected');
		var x = $('#stattable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#ab_id").val(x[0].ID);
		$("#ab_statid1").val(x[0].STAT_ID);
		$("#ab_name1").val(x[0].NAME);
		$("#ab_line").html(x[0].LINE);
		$("#ab_statid").html(x[0].STAT_ID);
		$("#ab_name").html(x[0].NAME);
		$("#ab_unit").html(x[0].UNIT);
		$("#ab_min").html(x[0].MIN);
		$("#ab_max").html(x[0].MAX);
		change_win=layer.open({
			  type: 1,
			  title:'修改工位',
		      area: ['600px', '600px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#abForecast")
		});
	});
	$('#stattable').bootstrapTable({
			ajax:function(request){
				$.ajax({
			    	type:'GET',
			    	url:'getAllStats.do',
			    	success:function(data){
			    		//下面这条语句必须存在，不然加载不出数据……原因可能和源码有关 
			    		request.success({ row:data });
			    		var json =  eval("("+data+")");
			    		console.log(json);
			    		var dt = [];
			    		for(var key in json.list){
			    			x[json.list[key].SIGN+','+json.list[key].STAT_ID] = json.list[key].NAME;
			    		}
			    		for(var key in json.list){
			    			if(json.list[key].RELATE_1!=null&&json.list[key].RELATE_1!=''){
			    				var a = (json.list[key].RELATE_1).split(',');
			    			}
			    			if(json.list[key].RELATE_2!=null&&json.list[key].RELATE_2!=''){
			    				var b = (json.list[key].RELATE_2).split(',');
			    			}
			    			if(json.list[key].ABNORMAL == 1){
			    				var c = "是";
			    			}else{
			    				c = "否";
			    			}
			    			dt.push({
			    				ID:json.list[key].ID,
			    				LINE:json.list[key].INSTRUCTION,
			    				STAT_ID:json.list[key].STAT_ID,
			    				NAME:json.list[key].NAME,
			    				NICKNAME:json.list[key].NICKNAME,
			    				UNIT:json.list[key].UNIT,
			    				INSTRUCTION:json.list[key].INFO,
			    				MIN:parseFloat(json.list[key].MIN_THRESHOLD).toFixed(2),
			    				MAX:parseFloat(json.list[key].MAX_THRESHOLD).toFixed(2),
			    				RELATE_1:x[json.list[key].SIGN+','+a[0]],
			    				RELATE_1_VALUE:parseFloat(a[1]).toFixed(2),
			    				RELATE_2:x[json.list[key].SIGN+','+b[0]],
			    				RELATE_2_VALUE:parseFloat(b[1]).toFixed(2),
			    				ABNORMAL:c,
			    			});
			    		}
			    		$('#stattable').bootstrapTable('load', dt);
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
			pageSize: 15,
			pageNumber: 1,
			pageList: "[10, 25, 50]",
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
		                },{
		                    field: 'ID',
		                    title: '序号',
		                    sortable: true,
		                },{
		                    field: 'LINE',
		                    title: '所属产线',
		                    sortable: true,
		                },{
		                    field: 'STAT_ID',
		                    title: '工位标识',
		                    sortable: true,
		                },{
		                    field: 'NAME',
		                    title: '名称',
		                },{
		                    field: 'NICKNAME',
		                    title: '别名',
		                },{
		                    field: 'UNIT',
		                    title: '单位',
		                },{
		                    field: 'INSTRUCTION',
		                    title: '介绍',
		                },{
		                    field: 'MIN',
		                    title: '下预警值<i class="fa fa-question-circle-o"></i>',
		                    titleTooltip:'该值为工位的下预警值，在工位面板中的曲线显示'
		                },{
		                    field: 'MAX',
		                    title: '上预警值<i class="fa fa-question-circle-o"></i>',
		                    titleTooltip:'该值为工位的上预警值，在工位面板中的曲线显示'
		                },{
		                	 field: 'RELATE_1',
			                 title: '参数A<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'与本参数相关的其他参数'
		                },{
		                	 field: 'RELATE_1_VALUE',
			                 title: '相关系数A',
		                },{
		                	 field: 'RELATE_2',
			                 title: '参数B<i class="fa fa-question-circle-o"></i>',
			                 titleTooltip:'与本参数相关的其他参数'
		                },{
		                	 field: 'RELATE_2_VALUE',
			                 title: '相关系数B',			                
		                },{
		                	field: 'ABNORMAL',
			                title: '是否推送异常',	
		                }
		            ],
	});
});
function init_line(){
	$.ajax({
		type:'GET',
    	url:'getCompanyLines.do',
    	success:function(data){
    		var json =  eval("("+data+")");
    		$("#lineid").append("<option></option>");
    		for(var key in json.list){
    			$("#lineid").append("<option value='"+json.list[key].ID+"'>"+json.list[key].INSTRUCTION+"</option>");
    			line[json.list[key].INSTRUCTION]=json.list[key].ID;
    		}
    	}
	});
}
$("#lineid").on('change',function(){
	var id = $(this).find('option:selected').val();
	$("#relate1n").empty();
	$("#relate2n").empty();
	if(id==null||id==''){
		$("#relate1n,#relate2n").append("<option></option>");
	}else{
		$("#relate1n,#relate2n").append("<option></option>");
		$.ajax({
			type:"GET",
			url:"getStatByLine.do?lineid="+id,
			success:function(data){
				var json =  eval("("+data+")");
				for(var key in json.list){
					$("#relate1n,#relate2n").append("<option value='"+json.list[key].STAT_ID+"'>"+json.list[key].NAME+"</option>")
				}
			},
		})
	}
})
function resetnew(){
	$(':input','#createStat') 
	.not(':button, :submit, :reset, :hidden') 
	.val('') 
	.removeAttr('checked') 
	.removeAttr('selected');
	$("#relate1n,#relate2n").empty();
}
function resetchange(){
	$(':input','#changeStat') 
	.not(':button, :submit, :reset, :hidden') 
	.val('') 
	.removeAttr('checked') 
	.removeAttr('selected');
}
function ab_calculate(){
	var load;
	var formData=new FormData($("#abForecast")[0]);
	console.log(formData);
	console.log(formData.get("ab_statid"));
	$.ajax({
		type:"POST",
		url:"abCalculate.do",
		data:formData,
		contentType:false,
		processData:false,
		beforeSend:function(){
			load = layer.load(0, {shade: false});
		},
		success:function(data){
			layer.close(load);
			if(data=='0'){
				layer.msg('计算成功', {
					  icon: 1,
					  time: 2500 //2秒关闭（如果不配置，默认是3秒）
				});
				layer.close(change_win);
				$("#stattable").bootstrapTable('refresh',{slient:true});
			}else if(data=='1'){
				layer.msg('计算值为空', {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	})
	
}
function createStat(){
	var load;
	var formData = new FormData($("#createStat")[0]);
	if(checkLine("#lineid","#new_ok")&&checkSTATID("#new_statid", "#new_ok")&&checkName("#new_name", "#new_ok")&&checkNumber("#min", "#max", "#relate1v", "#relate2v", "#new_ok")){
	$.ajax({
		type:"POST",
		url:"createStat.do",
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
				$("#createStat")[0].reset();
				$("#stattable").bootstrapTable('refresh',{slient:true});
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
function checkLine(e,b){
	var msg='';
	if($(e).val()==''||$(e).val()==null){
		msg='请选择一个产线'
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
function checkSTATID(e,b){
	var msg = '';
	if($(e).val()==''||$(e).val()==null){
		msg='请填入工位标识';
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
		msg='请填入工位名称';
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
	var min = $(e).val()+'';
	var max = $(f).val()+'';
	var par1 = $(g).val()+'';
	var par2 = $(h).val()+'';
	if($(e).val()==''||$(e).val()==null||$(f).val()==''||$(f).val()==null){
		msg='预警值不能为空'
	}else if(!min.match(regix)||!max.match(regix)){
		msg ='请输入正确的数字格式！';
	}else if(parseFloat(min)>=parseFloat(max)){
		msg='预警值上限需大于下限';
	}else if(par1<0||par2<0||par1>1||par2>1){
		msg = '相关系数需满足[0,1]'
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
function changeStat(){
	var load;
	var formData = new FormData($("#changeStat")[0]);
	if(checkName("#change_name", "#change_ok")&&checkNumber("#cmin", "#cmax", "#crelate1v", "#crelate2v", "#change_ok")){
	$.ajax({
		type:"POST",
		url:"updateStat.do",
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
				$("#stattable").bootstrapTable('refresh',{slient:true});
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

function deleteStat(){
	var formData = new FormData($("#deleteStat")[0]);
	$.ajax({
		type:"POST",
		url:"deleteStat.do",
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
				$("#stattable").bootstrapTable('refresh',{slient:true});
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
		},
	});
}
