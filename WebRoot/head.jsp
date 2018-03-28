<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--left-fixed -navigation-->
<div class=" sidebar" role="navigation">
          <div class="navbar-collapse">
		<nav class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-left" id="cbp-spmenu-s1">
			<ul class="nav" id="side-menu">
				<li>
					<a href="login/doit"><i class="fa fa-home nav_icon"></i>Home</a>
				</li>
				<li class="">
					<a href="javascript:void(0);"><i class="fa fa-book nav_icon"></i>人员管理 <span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="login/doit?path=com/schrodinger/user/srUserList.jsp">人员设置</a>
							<!-- <span class="nav-badge">7</span> -->
						</li>
						<li>
							<a href="login/doit?path=com/schrodinger/user/srUserList.jsp">人员查看</a>
						</li>
						<li>
							<a href="general.html">回收站</a>
						</li>
					</ul>
					<!-- /nav-second-level -->
				</li>
				<li>
					<a href="javascript:void(0);"><i class="fa fa-check-square-o nav_icon"></i>工时管理<span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="forms.html">工时同步</a>
						</li>
					</ul>
					<!-- //nav-second-level -->
				</li>
				<li>
					<a href="javascript:void(0);"><i class="fa fa-file-text-o nav_icon"></i>设备管理<span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="login.html">台式机设置</a>
						</li>
						<li>
							<a href="signup.html">显示器设置</a>
						</li>
						<li>
							<a href="blank-page.html">笔记本设置</a>
						</li>
						<li>
							<a href="blank-page.html">借还设置</a>
						</li>
						<li>
							<a href="blank-page.html">借还查询</a>
						</li>
					</ul>
					<!-- //nav-second-level -->
				</li>
				<li>
					<a href="javascript:void(0);"><i class="fa fa-cogs nav_icon"></i>系统设置<span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="media.html">菜单管理</a>
						</li>
						<li>
							<a href="media.html">账户管理</a>
						</li>
						<li>
							<a href="media.html">权限配置</a>
						</li>
						<li>
							<a href="media.html">通用代码</a>
						</li>
						<li>
							<a href="media.html">日志查看</a>
						</li>
					</ul>
					<!-- /nav-second-level -->
				</li>
				<li>
					<a href="javascript:void(0);"><i class="fa fa-cogs nav_icon"></i>数据维护 <span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="media.html">快照管理</a>
						</li>
						<li>
							<a href="media.html">数据管理</a>
						</li>
					</ul>
					<!-- /nav-second-level -->
				</li>
				<li>
					<a href="charts.html" class="chart-nav">统计视图 <span class="fa arrow"></span></a>
					<ul class="nav nav-second-level collapse">
						<li>
							<a href="media.html">综合统计</a>
						</li>
						<li>
							<a href="media.html">年度分析</a>
						</li>
					</ul>
				</li>
			</ul>
			<div class="clearfix"> </div>
			<!-- //sidebar-collapse -->
		</nav>
	</div>
</div>
<!--left-fixed -navigation-->
<!-- header-starts -->
<div class="sticky-header header-section ">
	<div class="header-left">
		<!--toggle button start-->
		<button id="showLeftPush"><i class="fa fa-bars"></i></button>
		<!--toggle button end-->
		<!--logo -->
		<div class="logo">
			<a href="index.html">
				<h1>Schrodinger</h1>
				<span>AdminPanel</span>
			</a>
		</div>
		<!--//logo-->
		<!--search-box-->
		<div class="search-box">
			<form class="input">
				<input class="sb-search-input input__field--madoka" placeholder="Search..." type="search" id="input-31" />
				<label class="input__label" for="input-31">
					<svg class="graphic" width="100%" height="100%" viewBox="0 0 404 77" preserveAspectRatio="none">
						<path d="m0,0l404,0l0,77l-404,0l0,-77z"/>
					</svg>
				</label>
			</form>
		</div><!--//end-search-box-->
		<div class="clearfix"> </div>
	</div>
	<div class="header-right">
		<div class="profile_details_left"><!--notifications of menu start -->
			<ul class="nofitications-dropdown">
				<li class="dropdown head-dpdn">
					<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-envelope"></i><span class="badge">3</span></a>
					<ul class="dropdown-menu">
						<li>
							<div class="notification_header">
								<h3>You have 3 new messages</h3>
							</div>
						</li>
						<li><a href="javascript:void(0);">
						   <div class="user_img"><img src="images/1.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet</p>
							<p><span>1 hour ago</span></p>
							</div>
						   <div class="clearfix"></div>	
						</a></li>
						<li class="odd"><a href="javascript:void(0);">
							<div class="user_img"><img src="images/2.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet </p>
							<p><span>1 hour ago</span></p>
							</div>
						  <div class="clearfix"></div>	
						</a></li>
						<li><a href="javascript:void(0);">
						   <div class="user_img"><img src="images/3.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet </p>
							<p><span>1 hour ago</span></p>
							</div>
						   <div class="clearfix"></div>	
						</a></li>
						<li>
							<div class="notification_bottom">
								<a href="javascript:void(0);">See all messages</a>
							</div> 
						</li>
					</ul>
				</li>
				<li class="dropdown head-dpdn">
					<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-bell"></i><span class="badge blue">3</span></a>
					<ul class="dropdown-menu">
						<li>
							<div class="notification_header">
								<h3>You have 3 new notification</h3>
							</div>
						</li>
						<li><a href="javascript:void(0);">
							<div class="user_img"><img src="images/2.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet</p>
							<p><span>1 hour ago</span></p>
							</div>
						  <div class="clearfix"></div>	
						 </a></li>
						 <li class="odd"><a href="javascript:void(0);">
							<div class="user_img"><img src="images/1.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet </p>
							<p><span>1 hour ago</span></p>
							</div>
						   <div class="clearfix"></div>	
						 </a></li>
						 <li><a href="javascript:void(0);">
							<div class="user_img"><img src="images/3.png" alt=""></div>
						   <div class="notification_desc">
							<p>Lorem ipsum dolor amet </p>
							<p><span>1 hour ago</span></p>
							</div>
						   <div class="clearfix"></div>	
						 </a></li>
						 <li>
							<div class="notification_bottom">
								<a href="javascript:void(0);">See all notifications</a>
							</div> 
						</li>
					</ul>
				</li>	
				<li class="dropdown head-dpdn">
					<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-tasks"></i><span class="badge blue1">15</span></a>
					<ul class="dropdown-menu">
						<li>
							<div class="notification_header">
								<h3>You have 8 pending task</h3>
							</div>
						</li>
						<li><a href="javascript:void(0);">
							<div class="task-info">
								<span class="task-desc">Database update</span><span class="percentage">40%</span>
								<div class="clearfix"></div>	
							</div>
							<div class="progress progress-striped active">
								<div class="bar yellow" style="width:40%;"></div>
							</div>
						</a></li>
						<li><a href="javascript:void(0);">
							<div class="task-info">
								<span class="task-desc">Dashboard done</span><span class="percentage">90%</span>
							   <div class="clearfix"></div>	
							</div>
							<div class="progress progress-striped active">
								 <div class="bar green" style="width:90%;"></div>
							</div>
						</a></li>
						<li><a href="javascript:void(0);">
							<div class="task-info">
								<span class="task-desc">Mobile App</span><span class="percentage">33%</span>
								<div class="clearfix"></div>	
							</div>
						   <div class="progress progress-striped active">
								 <div class="bar red" style="width: 33%;"></div>
							</div>
						</a></li>
						<li><a href="javascript:void(0);">
							<div class="task-info">
								<span class="task-desc">Issues fixed</span><span class="percentage">80%</span>
							   <div class="clearfix"></div>	
							</div>
							<div class="progress progress-striped active">
								 <div class="bar  blue" style="width: 80%;"></div>
							</div>
						</a></li>
						<li>
							<div class="notification_bottom">
								<a href="javascript:void(0);">See all pending tasks</a>
							</div> 
						</li>
					</ul>
				</li>	
			</ul>
			<div class="clearfix"> </div>
		</div>
		<!--notification menu end -->
		<div class="profile_details">		
			<ul>
				<li class="dropdown profile_details_drop">
					<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
						<div class="profile_img">	
							<span class="prfil-img"><img src="images/a.png" style="width: 45px;" alt=""> </span> 
							<div class="user-name">
								<p>${name }</p>
								<span>${role_name }</span>
							</div>
							<i class="fa fa-angle-down lnr"></i>
							<i class="fa fa-angle-up lnr"></i>
							<div class="clearfix"></div>	
						</div>	
					</a>
					<ul class="dropdown-menu drp-mnu">
						<li> <a href="javascript:void(0);"><i class="fa fa-cog"></i> Settings</a> </li> 
						<li> <a href="javascript:void(0);"><i class="fa fa-user"></i> Profile</a> </li> 
						<li> <a href="javascript:window.location='<%=request.getContextPath() %>/login/quit_'"><i class="fa fa-sign-out"></i> Logout</a> </li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="clearfix"> </div>	
	</div>
	<div class="clearfix"> </div>
</div>
<!-- //header-ends -->
<jsp:include page="/alter.jsp"></jsp:include>
<script>
	var menuLeft = document.getElementById( 'cbp-spmenu-s1' ),
		showLeftPush = document.getElementById( 'showLeftPush' ),
		body = document.body;
		
	showLeftPush.onclick = function() {
		classie.toggle( this, 'active' );
		classie.toggle( body, 'cbp-spmenu-push-toright' );
		classie.toggle( menuLeft, 'cbp-spmenu-open' );
		disableOther( 'showLeftPush' );
	};
	
	function disableOther( button ) {
		if( button !== 'showLeftPush' ) {
			classie.toggle( showLeftPush, 'disabled' );
		}
	}
</script>
