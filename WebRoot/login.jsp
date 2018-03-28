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
<% String path = request.getContextPath(); %>
<head>
<script type="text/javascript">
function login_(){
	var form = formData('form');
	ajaxData('login/login_',form, function(r){
		if(r.flag == 'error'){
			$('#dialog-modal-sm').modal('show');
		}else{
			window.location="<%=path %>/login/doit";
		}
	});
	return false;
}
</script>
</head>
<body>
<!-- //header-ends -->
<!-- main content start-->
<div id="page-wrapper" style="padding-top: 4em;">
	<div class="main-page login-page ">
		<h3 class="title1">SignIn Page</h3>
		<div class="widget-shadow">
			<div class="login-top">
				<h4>Welcome back to Schrodinger !
<!-- 				<br> Not a Member? <a href="signup.html">  Sign Up »</a> </h4> -->
			</div>
			<div class="login-body">
				<form id="form" method="post" onsubmit="return login_()">
					<input type="text" class="user" name="login" placeholder="Enter your account" required="">
					<input type="password" name="pwd" class="lock" placeholder="password" required="">
					<input type="submit" name="Sign In" value="Sign In" >
					<div class="forgot-grid">
						<label class="checkbox"><input type="checkbox" name="checkbox" checked=""><i></i>Remember me</label>
						<div class="forgot">
							<a href="#">forgot password?</a>
						</div>
						<div class="clearfix"> </div>
					</div>
				</form>
			</div>
		</div>
		
		<div class="login-page-bottom">
			<h5> - OR -</h5>
			<div class="social-btn"><a href="javascript:void(0);"><i class="fa fa-facebook"></i><i>Sign In with Facebook</i></a></div>
			<div class="social-btn sb-two"><a href="javascript:void(0);"><i class="fa fa-twitter"></i><i>Sign In with Twitter</i></a></div>
		</div>
	</div>
	<div class="modal fade" id="dialog-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="panel panel-warning">
					<div class="panel-heading">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
						<h3 class="panel-title">Panel Warning</h3>
					</div>
					<div class="panel-body">登陆失败，账号或密码错误！</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--footer-->
<div class="footer">
   <p>Copyright &copy; 2018.Company name All rights reserved.More</p>
</div>
 <!--//footer-->
</body>
</html>