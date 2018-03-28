<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<title>Schrodinger</title>
<jsp:include page="/inclued.jsp"></jsp:include>
<head>

<script type="text/javascript">


(function($) {
	$.extend({
		message:{
			alert:function(tit, msg, fun){
				
			}
		}
	})
})(jQuery);

$.message.alert('tit','aaa');


function add(){
	opend('添加', 'com/schrodinger/user/srUserSave.jsp');
}
function edit(id){
	opend('编辑', 'com/schrodinger/user/srUserSave.jsp?id='+id);
}

// 'userUpdate', 'userDelete'
function del(){
	dels('grid', 'user/userBack');
}

function download(){
	confirm('是否下载？', null, function(){
		window.open("exp/downloadUser");
	});
}

function changeQuery(r){
	searchData(r.value);
}

function getFormData(status){
	var form = formData('form');
	if(status){
		form['#status-eq'] = status;
	}
	return form;
}
function searchData(status){

	if(!status){
		status = '1';
	}
	var set = {
		queryParams:getFormData(status)
	};
	if(status == '1'){
		$('#dd').attr('data-options',"field:'entry'");
		$('#dd').html('入职');
		set.sort = 'entry';
	}else{
		$('#dd').attr('data-options',"field:'quit'");
		$('#dd').html('离职');
		set.sort = 'quit';
	}
	
	$('#grid').datagrid(set);
	return false;
}

function formatSex(v, row){
	return '<div style="'+rgb+'">'+lookupName('_USER_SEX', v)+'</div>';
}
function formatHref(v, row){
	var rgb = '';
	if(row.sex == 1){
		rgb = 'background-color:rgb(255,220,180)';
	}
	return '<div style="cursor: pointer;'+rgb+'" onClick="edit(\''+row.id+'\')">'+v+'</div>';
}

</script>

</head>
<body class="cbp-spmenu-push">
	<div class="main-content">
		<jsp:include page="/head.jsp"></jsp:include>
		<!-- //header-ends -->
		<!-- main content start-->
		
		<div id="page-wrapper">
			<div class="main-page">
				<div class="grid_3 widget-shadow" style="padding:1em 1em;">
					<form id="form" onsubmit="return searchData()">
						<input type="hidden" name="#is_del-eq" value="0" />
			
						<div class="form-group">
							<div class="row">
								<div class="col-md-3 grid_box1">
									<button type="button" class="btn btn-default btn-block" onclick="add()" style="padding: 9px 5px;">添加</button>
								</div>
								<div class="col-md-3 grid_box1">
									<button type="button" class="btn btn-default btn-block" onclick="del()" style="padding: 9px 5px;">删除</button>
								</div>
								<div class="col-md-6">
									<select name="#status-eq" class="form-control1 b-combobox" placeholder=""
										data-options="lookup:'_USER_STATE',onSelect:changeQuery">
									</select>
								</div>
								<div class="clearfix"> </div>
							</div>
						</div>
						<div class="form-group">
							<div class="row">
								<div class="col-md-9 grid_box1">
									<input name="#name#login_name#email#remark#phone-index" class="form-control1" placeholder="名称/邮箱/备注/电话等">
								</div>
								<div class="col-md-3 grid_box1">
									<button type="button" class="btn btn-default btn-block" onclick="download()" style="padding: 9px 5px;">下载</button>
								</div>
								<div class="clearfix"> </div>
							</div>
						</div>
					</form>
				</div>
				<div class="bs-example widget-shadow" style="padding: 10px;">
					<table class="table table-bordered b-datagrid" id="grid" data-options="
							url:'user/queryUserList',
							queryParams:'getFormData',
							sort:'entry', 
							order:'desc'">
						<thead>
							<tr>
								<th data-options="field:'sn',type:'checkbox'" >#</th>
								<th data-options="field:'name',formatter:formatHref">姓名</th>
								<th data-options="field:'phone'">电话</th>
								<th data-options="field:'_group_name'">分组</th>
								<th data-options="field:'entry'" id="dd">入职</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
	</div>
</body>