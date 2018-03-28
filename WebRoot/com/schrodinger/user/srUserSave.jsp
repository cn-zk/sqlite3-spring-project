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
<jsp:include page="/alter.jsp"></jsp:include>
<head>
<style type="text/css">
.col-md-2{
	padding-top: 10px;
}
.form-control1{
	font-family:'微软雅黑';
	font-weight: bold;
}
.row{
	margin:10px;
}
</style>
<script type="text/javascript">
	var id = '${param.id}';
	$(function(){
		if(id != ''){
			ajaxData('user/findUser',
				{
					id:id
				},function(d){
					$('#form').form('load',d.dto);
			});
		}
	});

	function onSave(){
		var d = formData('form', true);
		d.status = isEmpty(d.quit) ? '1' : '3';
		
		var flag = true;
		$.ajax({
			url:'user/userUpdate',
			data:{
				dtos:JSON.stringify(d)
			},
			type:'post',
			async:false,
			dataType:'json',
			success:function(r){
				if(r.flag!="success"){
					parent.warning('操作失败！');
					flag = false;
				}
			}
		});
		
		if(flag){
			parent.info('操作成功！');
			parent.searchData();
		}
		
		return flag;
	}
</script>

</head>
<body class="cbp-spmenu-push">
	<div class="main-content">
		<!-- //header-ends -->
		<!-- main content start-->
		
		<div class="main-page">
			<div class="grids widget-shadow" style="margin: 0px;padding:1px;">
				<form id="form" onsubmit="">
					<input type="hidden" name="id" />
					<div class="form-group">
						<div class="row">
							<label class="col-md-2">姓名：</label>
							<div class="col-md-4">
								<input type="text" name="name" class="form-control1" placeholder="(rd)">
							</div>
							<div class="col-md-2">
								<label>登陆名：</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="login_name" class="form-control1" placeholder="rd-">
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-2">
								<label>技能：</label>
							</div>
							<div class="col-md-4">
								<select name="skill" class="form-control1 b-combobox" placeholder=""
									data-options="lookup:'_USER_SKILL'">
								</select>
							</div>
							<div class="col-md-2">
								<label>入职：</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="entry" class="form-control1" placeholder="">
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-2">
								<label>级别：</label>
							</div>
							<div class="col-md-4">
								<select name="level" class="form-control1 b-combobox" placeholder=""
									data-options="lookup:'_USER_LEVEL'">
								</select>
							</div>
							<div class="col-md-2">
								<label>所属小组：</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="_group_name" class="form-control1" placeholder="">
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-2">
								<label>性别：</label>
							</div>
							<div class="col-md-4">
								<select name="sex" class="form-control1 b-combobox" placeholder=""
									data-options="lookup:'_USER_SEX'">
								</select>
							</div>
							<div class="col-md-2">
								<label>联系方式：</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="phone" class="form-control1" placeholder="">
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-2">
								<label>邮箱：</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="email" class="form-control1" placeholder="">
							</div>
							<div class="col-md-2">
								<label>IP</label>
							</div>
							<div class="col-md-4">
								<input type="text" name="ip" class="form-control1" placeholder="">
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-2">
								<label>备注：</label>
							</div>
							<div class="col-md-10">
								<input type="text" name="remark" class="form-control1" placeholder="">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>