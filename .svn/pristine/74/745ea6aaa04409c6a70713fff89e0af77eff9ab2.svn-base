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
			  title:'新增参数',
		      area: ['600px', '600px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#createPara")
		});
	});
	$("#btn-edit").on('click',function(){
		$(':input','#changePara') 
		.not(':button, :submit, :reset, :hidden') 
		.val('') 
		.removeAttr('checked') 
		.removeAttr('selected');
		var x = $('#paratable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$.ajax({
			type:"GET",
			url:"",
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
		$("#change_source").val(x[0].SOURCE);
		change_win=layer.open({
			  type: 1,
			  title:'修改参数',
		      area: ['600px', '600px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#changePara")
		});
	});
	$("#btn-delete").on('click',function(){
		var x = $('#paratable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		$("#del_line").html(x[0].LINE);
		$("#del_para").html(x[0].NAME);
		$("#del_id").val(x[0].ID);
		delete_win=layer.open({
			  type: 1,
			  title:'删除参数',
		      area: ['400px', '200px'],
		      anim:0,
		      shadeClose: true, //点击遮罩关闭
		      content: $("#deletePara")
		});
	});
	$('#paratable').bootstrapTable({
			ajax:function(request){
				$.ajax({
			    	type:'GET',
			    	url:'',
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
			    			dt.push({
			    				ID:json.list[key].ID,
			    				LINE:json.list[key].INSTRUCTION,
			    				STAT_ID:json.list[key].STAT_ID,
			    				NAME:json.list[key].NAME,
			    				NICKNAME:json.list[key].NICKNAME,
			    				UNIT:json.list[key].UNIT,
			    				SOURCE:json.list[key].SOURCE
			    			});
			    		}
			    		$('#paratable').bootstrapTable('load', dt);
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
		                    title: '参数标识',
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
		                    field: 'SOURCE',
		                    title: '参数组成',
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

function createPara() {
	
}

function changePara(){
	
}

function deletePara() {
	
}


