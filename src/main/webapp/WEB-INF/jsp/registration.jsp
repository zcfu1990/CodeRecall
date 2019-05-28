<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Registraion</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${head_path}plugins/iCheck/square/blue.css">
    <link rel="stylesheet" href="${head_path}plugins/font-awesome/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${head_path}dist/css/adminlte.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="${head_path}plugins/iCheck/square/blue.css">
    <!-- Google Font: Source Sans Pro -->
</head>

<body class="hold-transition register-page">
    <div class="register-box" style="margin-top:3%; width:480px;">
        <div class="register-logo">
            <a href="#"><b>Recall-Registration</b></a>
        </div>

        <div class="card">
            <div class="card-body register-card-body">
                <p class="login-box-msg">Registration</p>
                <form action="" method="post" role="form" id="register_modal">
                    <div class="form-group has-feedback">
                        <span>Name<br/>(The combination of number, letter, underline)</span><input type="text" class="form-control" id="inputName" data-pattern-error="Your name" placeholder="Your name" data-error="Your name format is not correct" required="">
                        <div class="help-block with-errors" id="inputName_Error"></div>
                    </div>
                    <div class="form-group has-feedback">
                        <span>Email</span><input type="email" class="form-control" id="inputEmail" placeholder="Email Address" data-error="请输入正确的邮箱地址" required="">
                        <div class="help-block with-errors" id="inputEmail_Error"></div>
                    </div>
                    <div class="form-group has-feedback">
                        <span>Password<br/>(The combination of number, letter, underline)</span><input type="password" class="form-control" id="inputPassword" data-minlength="6" placeholder="Your password" minlength="4" maxlength="16" required="">
                        <div class="help-block with-errors" id="inputPassword_Error"></div>
                    </div>
                    <div class="form-group has-feedback">
                        <span>Re-Passsword</span><input type="password" class="form-control" id="inputPasswordConfirm" minlength="4" maxlength="16" data-match="#inputPassword" data-match-error="Passwords do not match" placeholder="Re-enter the password" data-error="请确认密码" required="">
                        <div class="help-block with-errors" id="inputPasswordConfirm_Error"></div>
                    </div>
                    <div class="form-group has-feedback" style="height:40px;">
                            <input type="text" class="form-control" style="width:200px; float:left;" disabled id="validCode" placeholder="Your verified code" required="" code="">
                            <a id="btn-validCode" style="width:120px; float:right;color:white;" class="btn btn-primary btn-block btn-flat" data-action="get-validate-code">Get Code</a>
                           <div class="help-block with-errors" id="validateCode_Error"></div>
                    </div>
                    <div class="form-group">
                       <div>
                            <button type="button" class="btn btn-primary btn-block btn-flat" data-action="register_new_account">Submit</button>
                       </div>
                        
                    </div>
                </form>
            </div>
            <!-- /.form-box -->
        </div>
        <!-- /.card -->
    </div>
    <!-- /.register-box -->

    <!-- jQuery -->
    <script src="${head_path}plugins/jquery/jquery.min.js"></script>
    <!-- Bootstrap 4 -->
    <script src="${head_path}plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- iCheck -->
    <script src="${head_path}plugins/iCheck/icheck.min.js"></script>
    <script src="${head_path}js/gears.js"></script>
    <script src="${head_path}js/account.js"></script>
</body>
</html>