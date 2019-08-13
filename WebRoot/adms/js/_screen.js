//变量区
var configs = new Array();
var stats = $("#stat1,#stat2,#stat3,#stat4,#stat5,#stat6");
var stats_2 = $("#stat2_1,#stat2_2,#stat2_3,#stat2_4,#stat2_5,#stat2_6,#stat2_7,#stat2_8");
var pars = $("#par1,#par2,#par3,#par4,#par5,#par6");
var pars_2 = $("#par2_1,#par2_2,#par2_3,#par2_4,#par2_5,#par2_6,#par2_7");
//初始化
$(document).ready(function(){
	$.ajax({
    	type:'GET',
    	url:'../limo/lines.do',
    	timeout:2000,
    	async:false,//同步，保证有返回值(配合return使用)，为确保用户体验，使用异步+赋值的方法
    	success:function(data){
    		var json = eval("("+data+")");
    		$("#lines,#lines2").append("<option value=''></option>");
    		for(var key in json.list){
    			$("#lines,#lines2").append("<option value='"+json.list[key].ID+"'>"+json.list[key].INSTRUCTION+"</option>");
    		}
    	},
    	error:function(e){
    		layer.msg('加载失败，请刷新页面', {
				  icon: 2,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
    	}
	});
	$("#btn-config").on('click',function(){
		var x = $('#linetable').bootstrapTable('getSelections');
		if(x.length==0){
			layer.msg('请选择一条记录', {
				  icon: 3,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
			return;
		}
		window.location.href="../0729/"+x[0].SIGN.toLowerCase()+"_set.html";
	});
	$('#linetable').bootstrapTable({
		ajax:function(request){
			$.ajax({
		    	type:'GET',
		    	url:'../adms/getCompanyLines.do',
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
		    				SIGN:json.list[key].SIGN
		    			});
		    		}
		    		console.log(dt);
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
	                }
	            ],
		});
	
})

function selectConfigs(){
	$("#configs").empty();
	stats.empty();
	pars.empty();
	$("#configs").append("<option value=''></option>");
	$("#configs").append("<option value='ENERS'>能耗相关</option>");
	$("#configs").append("<option value='SHAKS'>振动相关</option>");
	var tmp = $("#lines").val();
	$("#cid").val('')
	if(tmp!=''){
		$.ajax({
			type:'GET',
	    	url:'../limo/getStation.do?line_id='+tmp,
	    	timeout:2000,
	    	async:false,
	    	success:function(data){
	    		stats.empty();
	    		var json = eval('('+data+')');
	    		stats.append("<option value=''></option>");
	    		for(key in json.list){
	    			stats.append("<option value='"+json.list[key].STAT_ID+"'>"+json.list[key].NAME+"</option>");
	    		}
	    		pars.append("<option value=''></option>").append("<option value='1'>折线图</option>").append("<option value='2'>仪表盘</option>").append("<option value='3'>VA图</option>");
	    		console.log(stats);
	    	},
	    	error:function(e){
	    		layer.msg('预加载工位信息失败', {
					  icon: 2,
					  time: 1500 //2秒关闭（如果不配置，默认是3秒）
				});
	    	}
		})
	}
}
function getConfigs(){
	var load =  layer.load(1, {shade: false});
	var id = $("#lines").val();
	var code = $("#configs").val();
	$("#code").val(code);
	stats.val('');
	pars.val('');
	if(id==''){
		layer.close(load);
		layer.msg('请选择一个产线', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	if(code==''){
		layer.close(load);
		layer.msg('请选择一个方案', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	$.ajax({
		type:'GET',
    	url:'../limo/getConfig.do?line_id='+id+'&code='+code,
    	timeout:2000,
    	success:function(data){
    		layer.close(load);
    		var json = eval('('+data+')');
    		$("#cid").val(json.list[0].ID);
    		$("#code").val(json.list[0].CODE);
    		var s = (json.list[0].STAS).split(',');
    		var p = (json.list[0].PARS).split(',');
    		for(var i = 1; i<=6;i++){
    			$("#stat"+i).val(s[i-1]);
    			$("#par"+i).val(p[i-1]);
    		}
    	},
    	error:function(e){
    		layer.msg('预加载工位信息失败', {
				  icon: 2,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
    	}	
	});
}
function selectConfigs2(){
	var load =  layer.load(1, {shade: false});
	stats_2.val('');
	pars_2.val('1.0');
	var id = $("#lines2").val();
	if(id==''){
		layer.close(load);
	}else{
		$.ajax({
			type:'GET',
	    	url:'../limo/getStation.do?line_id='+id,
	    	timeout:2000,
	    	async:false,
	    	success:function(data){
	    		stats_2.empty();
	    		var json = eval('('+data+')');
	    		stats_2.append("<option value=''></option>");
	    		for(key in json.list){
	    			stats_2.append("<option value='"+json.list[key].STAT_ID+"'>"+json.list[key].NAME+"</option>");
	    		}
	    	},
	    	error:function(e){
	    		layer.msg('预加载工位信息失败', {
					  icon: 2,
					  time: 1500 //2秒关闭（如果不配置，默认是3秒）
				});
	    	}
		});
	   $.ajax({
			type:'GET',
	    	url:'../limo/getConfig.do?line_id='+id+'&code=YWS',
	    	timeout:2000,
	    	success:function(data){
	    		layer.close(load);
	    		var json = eval('('+data+')');
	    		$("#cid2").val(json.list[0].ID);
	    		var s = json.list[0].STAS.split(',');
	    		var p = json.list[0].PARS.split(',');
	    		for(var i = 1;i<=8;i++){
	    			$("#stat2_"+i).val(s[i-1]);
	    			$("#par2_"+i).val(p[i-1]);
	    		}
	    	},
	    	error:function(e){
	    		layer.msg('预加载工位信息失败', {
					  icon: 2,
					  time: 1500 //2秒关闭（如果不配置，默认是3秒）
				});
	    	}
		})
	}
}
function reset1(){
	$("#code").val('');
	stats.val('');
	pars.val('');
	$("#box1")[0].reset();
}
function reset2(){
	$("#code2").val('');
	stats_2.val('');
	pars_2.val('1.0');
	$("#box2")[0].reset();
}
function submit1(){
	var s = '',p = '';
	for(var i =1 ;i <= 6;i++){
		s+=$("#stat"+i).val();
		p+=$("#par"+i).val();
		if(i!=6){
			s+=',';
			p+=',';
		}
	}
	$("#stats").val(s);
	$("#pars").val(p);
	if($("#lines").val()==''||$("#lines").val()==null){
		layer.msg('请选择一条产线', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	if($("#configs").val()==''||$("#configs").val()==null){
		layer.msg('请选择一个方案', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	var formData = new FormData($("#box1")[0]);
	$.ajax({
		type:'POST',
    	url:'../limo/updateConfig.do?',
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
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
    	},
    	error:function(e){
    		layer.msg('预加载工位信息失败', {
				  icon: 2,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
    	}	
	});
}
function submit2(){
	var s = '',p = '';
	for(var i =1 ;i <= 8;i++){
		s+=$("#stat2_"+i).val();
		if(i!=8){
			s+=',';
		}
	}
	for(var j = 1;j<=7;j++){
		if(checkNumber("#par2_"+j,$("#par2_"+j))){
			p+=$("#par2_"+j).val();
			if(j!=7){
				p+=',';
			}
		}else{
			return;
		}	
	}
	$("#code2").val("YWS");
	$("#stats2").val(s);
	$("#pars2").val(p);
	if($("#lines2").val()==''||$("#lines2").val()==null){
		layer.msg('请选择一条产线', {
			  icon: 2,
			  time: 1500 //2秒关闭（如果不配置，默认是3秒）
		});
		return;
	}
	var formData = new FormData($("#box2")[0]);
	$.ajax({
		type:'POST',
    	url:'../limo/updateConfig.do?',
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
			}else{
				layer.msg(data, {
					  icon: 2,
					  time: 2000 //2秒关闭（如果不配置，默认是3秒）
				});
			}
    	},
    	error:function(e){
    		layer.msg('预加载工位信息失败', {
				  icon: 2,
				  time: 1500 //2秒关闭（如果不配置，默认是3秒）
			});
    	}	
	});
}
function checkNumber(e,f){
	var msg='';
	var regix = /^[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$/;
	if($(e).val()==''||$(e).val()==null){
		msg='系数不能为空'
	}else if(!$(e).val().match(regix)){
		msg ='请输入正确的数字格式！';
	}
	if(msg!=''){
		layer.tips(msg,f,{
			time:2000
		});
		return false;
	}else{
		return true;
	}
}