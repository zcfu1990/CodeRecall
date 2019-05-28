<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>Recall</title>
<link rel="stylesheet"
	href="${head_path}plugins/bootstrap/css/bootstrap.min.css" />
<!-- Font Awesome Icons -->
<link rel="stylesheet"
	href="${head_path}plugins/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${head_path}plugins/rightclick/jquery.contextMenu.css"
	type="text/css" media="screen">
<!-- Theme style -->
<link rel="stylesheet" href="${head_path}dist/css/adminlte.min.css">
<link rel="stylesheet"
	href="${head_path}plugins/clockpicker/bootstrap-clockpicker.css">
<link rel="stylesheet"
	href="${head_path}plugins/datepicker/datepicker3.css">
<link rel="stylesheet" href="${head_path}plugins/iCheck/flat/red.css">
<link href="${head_path}plugins/select2/css_4_2/select2.min.css"
	rel="stylesheet" />

<link rel="stylesheet"
	href="${head_path}plugins/jquery-confirm/jquery-confirm.min.css"
	type="text/css">
<!-- bootstrap-fileinput -->
<link href="${head_path}plugins/bootstrap-fileinput/css/fileinput.css"
	media="all" rel="stylesheet" type="text/css" />
<link
	href="${head_path}plugins/bootstrap-fileinput/themes/explorer/theme.css"
	media="all" rel="stylesheet" type="text/css" />

<link
	href="${head_path}plugins/bootstrap-wysiwyg/external/google-code-prettify/prettify.css"
	rel="stylesheet">
<link href="${head_path}plugins/starComment/css/star-rating.min.css"
	media="all" rel="stylesheet" type="text/css" />
<link href="${head_path}plugins/color/spectrum.css" media="all"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${head_path}css/loading.css">

<link href="${head_path}css/editor.css" rel="stylesheet" />
<link rel="stylesheet" href="${head_path}css/animate.min.css">
<link rel="stylesheet" href="${head_path}css/controller.css" />
<style>
img {
	width: 100%;
}
.modal{
z-index:2000;
}

.select2-dropdown.select2-dropdown--below
{
z-index:100000;
}
</style>
</head>
<body class="fixed sidebar-collapse" id="main_body"
	data-id="${data_id}" data-type="${data_type}" data-key="${data_key}" data-uid="${data_uid}">
	<div id="overlay_page" style="display: none;">
		<div class="spinner">
			<div class="rect1"></div>
			<div class="rect2"></div>
			<div class="rect3"></div>
			<div class="rect4"></div>
			<div class="rect5"></div>
		</div>
	</div>
	<div class="wrapper">
		<!-- Navbar -->
		<header class="main-header" style="z-index:2000;">
		<nav class="navbar navbar-expand navbar-light border-bottom" style="background-color: #605ca8;">
			<!-- Left navbar links -->
			<ul class="navbar-nav">
			  <li class="navbar-nav">
			    <a class="nav-link"  href="javascript:void(0);" data-name="go-home" aria-expanded="false" style="color:white;">
			      <i class="fa fa-home user-header-icon" title="Home Page"></i>
			    </a>
			  </li>
				<li class="nav-item d-none d-sm-inline-block" id="nav_project_header_name"></li>
			</ul>
			<!-- SEARCH FORM -->
			<form class="form-inline ml-3">
				<div class="input-group input-group-sm">
					<input class="form-control form-control-navbar search_input" type="search"
						data-name="instance-search-input" placeholder="Search"
						aria-label="Search">
					<div class="input-group-append">
						<button class="btn btn-navbar" type="button"
							data-name="instance-search-button">
							<i class="fa fa-search"></i>
						</button>
					</div>
				</div>
			</form>
			<!-- Right navbar links -->
			<ul class="navbar-nav ml-auto" id="user-top-lane">
			    
			</ul>
		</nav>
		</header>
		<!-- /.navbar -->
		<!-- Main Sidebar Container -->
		<!--<div id="btn-rightbot-add-post">
			<a class="btn btn-success btn-lg switch-rightbot btn-rightbot" title="发送动态" id="send-new-post"><i class="fa fa-plus" style="color: white; font-size: 28px;position:relative;right:2px;"></i></a>
		</div>-->
	    <div id="btn-rightbot-more" style="display:none;"><a class="btn btn-success btn-lg switch-rightbot btn-rightbot" style="bottom:20px;" title="显示更多" id="loading-more-instances"><i class="fa fa-angle-double-down" style="color:white;font-size:28px;"></i></a></div>
		
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper" id="content_main_block" style="background:white; min-height:90vh;margin-top:75px;">
			<section class="content" id="main-instance-block">
				<div class="container-fluid create_instance_check">
					<div class="row" id="template-all-block" style="margin-top: 10px;"></div>
					<div class="row" id="user-profile-info-block"
						style="margin-top: 10px; display: none;" data-id="">
						<div class="col-md-3" data-name="instance-profile" data-type="1"></div>
						<div class="col-md-9" data-name="main-instance-block-info">
							<div class="card">
								<div class="card-header p-2">
									<ul class="nav nav-pills" data-name="tab-parent" data-type="user" data-current="instance-post" data-instance-type="1">
										<li class="nav-item"><a class="nav-link active show" style="cursor:pointer;"
											data-name="instance-recall" data-action="instance-tab-action">My Collection</a></li>
									</ul>
								</div>
								<!-- /.card-header -->
								<div class="card-body">
									<div class="tab-content">
										<div class="tab-pane active show">
											<div class="row" data-name="instance-template-block" data-type="1"></div>
										</div>
									</div>
									<!-- /.tab-content -->
								</div>
								<!-- /.card-body -->
							</div>
							<!-- /.nav-tabs-custom -->
						</div>
						<!-- /.col -->
					</div>
				</div>
			</section>
		</div>
		<!-- /.content-wrapper -->
		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Control sidebar content goes here -->
		</aside>
		<!-- /.control-sidebar -->
		<!-- Main Footer -->
		<footer class="main-footer">
			<!-- To the right -->
			<div class="float-right d-sm-none d-md-block">Anything you
				want, Utopia provides</div>
			<!-- Default to the left -->
			<strong>Copyright &copy; 2014-2018 <a href="#">Utopia</a>.
			</strong> All rights reserved.
		</footer>
	</div>
	<!-- ./wrapper -->
	<!-- REQUIRED SCRIPTS -->
	<!-- jQuery -->
	<script src="${head_path}plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="${head_path}plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE App -->
	<script src="${head_path}dist/js/adminlte.js"></script>
	<!-- PAGE PLUGINS -->
	<script src="${head_path}plugins/slimScroll/jquery.slimscroll.min.js"></script>
	<!-- JQuery Confirm -->
	<script src="${head_path}plugins/jquery-confirm/jquery-confirm.min.js"></script>
	<script
		src="${head_path}plugins/bootstrap-fileinput/js/plugins/sortable.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/bootstrap-fileinput/js/fileinput.js"
		type="text/javascript"></script>
	<script
		src="${head_path}plugins/bootstrap-fileinput/themes/explorer/theme.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/bootstrap-fileinput/js/locales/zh.js"
		type="text/javascript"></script>
	<script
		src="${head_path}plugins/bootstrap-wysiwyg/external/jquery.hotkeys.js"></script>
	<script
		src="${head_path}plugins/bootstrap-wysiwyg/bootstrap-wysiwyg.js"></script>
	<script src="${head_path}plugins/datepicker/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/clockpicker/bootstrap-clockpicker.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/iCheck/icheck.js"
		type="text/javascript"></script>

	<script src="${head_path}plugins/select2/js_4_2/select2.min.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/select2/js_4_2/i18n/zh-CN.js"
		type="text/javascript"></script>

	<script src="${head_path}plugins/starComment/js/star-rating.min.js"
		type="text/javascript"></script>
	<script src="${head_path}plugins/rightclick/jquery.ui.position.js"></script>
	<script src="${head_path}plugins/rightclick/jquery.contextMenu.js"></script>
	<script src="${head_path}plugins/color/spectrum.js"></script>

	<!-- PAGE SCRIPTS -->
	<script type="text/javascript" src="${head_path}js/editor.js"></script>

	<script src="${head_path}js/gears.js"></script>
	<script src="${head_path}js/post.js"></script>
	<script src="${head_path}js/customer.js"></script>
</body>
</html>