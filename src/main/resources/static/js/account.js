const URL_Registration_Action = URLPrefix + "/action/registration";
const URL_Registration_Action_Bus = URLPrefix + "/action/registration_bus";

const URL_Registration_Validation_Code = URLPrefix + "/action/get_validation_code";

$(document).ready(function() {
    var register = $('#register_modal');
    var validatePass = function() {
        var password = document.getElementById("inputPassword").value;
        var confirm_password = document.getElementById("inputPasswordConfirm").value;
        var message = document.getElementById("inputPasswordConfirm_Error");
        var psmessage = document.getElementById("inputPassword_Error");

        var re = /^[a-zA-Z0-9_\s]{6,10}$/;
		if(!(re.test(password)&&/\d/.test(password)&&/[a-z]/.test(password)&&/[A-Z]/.test(password)))
		{
		    document.getElementById("inputPassword").style.backgroundColor = badColor;
		    psmessage.style.color = badColor;
		    psmessage.innerHTML = "Your password should be combined with digits letter, and the length should be 6-10！At least one Captial and lower case letter and a digit!";
            return false;
		}
        
        if (password == confirm_password && password !== "") {
            document.getElementById("inputPasswordConfirm").style.backgroundColor = goodColor;
            return true;
        } else {
            document.getElementById("inputPasswordConfirm").style.backgroundColor = badColor;
            message.style.color = badColor;
            message.innerHTML = "Passwords do not match！";
            return false;
        }
    }
    $("#inputPassword").on('input',
            function() {
                $("#inputPassword").css("backgroundColor", goodColor);
                $("#inputPassword_Error").html("");
                //$("#inputPassword").html("");
            });

        $("#inputPasswordConfirm").on('input',
            function() {
                $("#inputPasswordConfirm").css("backgroundColor", goodColor);
                $("#inputPasswordConfirm").html("");
                //$("#inputPassword").html("");
            });
    
    var validateUserName= function() {
        var name = document.getElementById("inputName").value;
        var re = /^[a-zA-Z0-9_]{1,20}$/;
        var message = document.getElementById("inputName_Error");

		if(!re.test(name))
		{
		    document.getElementById("inputName").style.backgroundColor = badColor;
            message.style.color = badColor;
            message.innerHTML = "Your name should be combined with digits, letter, and the length should be 6-20";
            return false;
		}
        return true;
    }
    $("#inputName").on('input',
            function() {
                $("#inputName").css("backgroundColor", goodColor);
                $("#inputName_Error").html("");
                //$("#inputPassword").html("");
            });
    
    $("#inputEmail").on('input',
        function() {
            $("#inputEmail").css("backgroundColor", goodColor);
            $("#inputEmail_Error").html("");
        });

    function validatePhone() {
        var phone = $("#inputPhone").val();
        var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;

        if (phone.length !== 11) {
            $("#inputPhone").css("backgroundColor", badColor);
            $("#inputPhone_Error").css("color", badColor);
            $("#inputPhone_Err0r").html("请输入11位手机号码！");
            return false;
        } else {
            return true;
        }
    }

    function validateCode() {
        var vcode = $("#validCode").val();
        var rightCode = $("#validCode").attr("code");
        if (vcode == "" || vcode !== rightCode) {
            $("#validCode").css("backgroundColor", badColor);
            $("#validateCode_Error").css("color", badColor);
            $("#validateCode_Error").html("Your code is not correct or expired");
            return false;
        } else {
            $("#validCode").css("backgroundColor", goodColor);
            return true;
        }
    }
    $("#validCode").on('input',
        function() {
            $("#validCode").css("backgroundColor", goodColor);
            $("#validateCode_Error").html("");
        });

    function validateAgree() {
        var isAgreement = document.getElementById("agreement-button").checked;
        if (!isAgreement) {
            $("#agreement_Error").html("请确定你同意我的协议");
        }
        return isAgreement;
    }

    register.find('[data-action="register_new_account"]').on(
        'click',
        function() {
            if (validateUserName() && validateEmail() &&
                validateCode() && validatePass()) {
                var email = document.getElementById("inputEmail").value;
                var password = document.getElementById("inputPassword").value;
        	        var icon='<i class="fa fa-spinner fa-pulse fa-3x fa-fw" style="font-size:25px;"></i>';
        	        $(this).html(icon);
        	        var obj=this;
                ajaxPostAction(URL_Registration_Action, {
                    email: email,
                    password: password,
                    name: $("#inputName").val()
                }, function(result) {
                	   //$(obj).html("注册");
                    messageAlert("Error", "clear",
                        function() {});
                }, function(result) {
                   	//$(obj).html("注册");
                    if (result == 2) {
                        $("#inputEmail_Err").css("color", badColor);
                        $("#inputEmail").css("backgroundColor", goodColor);
                        $("#inputEmail_Err").html("This email has been registered！");
                    }
                    else if (result == 5) {
                        $("#inputPassword_Err").css("backgroundColor",
                            badColor);
                        $("#inputPassword_Err").css(
                            "backgroundColor", badColor);
                        $("#inputPassword_Err").html("Your password should be combined with digits letter, and the length should be 6-10");
                    }
                    else if (result == 6) {
                        $("#inputName_Err").css("backgroundColor",
                            badColor);
                        $("#inputName_Err").css(
                            "backgroundColor", badColor);
                        $("#inputName_Err").html("Your name should be combined with digits, letter, and the length should be 6-20");
                    }
                    else if (result == 1) {
                        messageAlert("Done",
                            "done",
                            function() {
                                window.location.href = location.origin + "/Recall";
                            });
                    }
                });
            }
        });
    
    
    
    register.find('[data-action="register_new_account_bus"]').on(
            'click',
            function() {
                if (validateUserName() && validateEmail() &&
                    validateCode() && validatePass() && validateAgree()) {
                    var email = document.getElementById("inputEmail").value;
                    var password = document.getElementById("inputPassword").value;
                    var domain = $(this).attr("data-domain");
                    var company=document.getElementById("user_company").value;
                    if(company=="")
                    {
                    	messageAlert("机构名称不能为空", "clear",
                                 function() {});
                    	return false;
                    }
                    var gender=document.getElementById("user_gender").value;               
            	        var icon='<i class="fa fa-spinner fa-pulse fa-3x fa-fw" style="font-size:25px;"></i>';
            	        $(this).html(icon);
            	        var obj=this;
                    ajaxPostAction(URL_Registration_Action_Bus, {
                        email: email,
                        password: password,
                        gender:gender,
                        company:company,
                        name: $("#inputName").val()
                    }, function(result) {
                    	   $(obj).html("注册");
                        messageAlert("注册请求发生错误，请重新尝试", "clear",
                            function() {});
                    }, function(result) {
                       	$(obj).html("注册");
                        if (result == 2) {
                            $("#inputEmail_Err").css("color", badColor);
                            $("#inputEmail").css("backgroundColor", goodColor);
                            $("#inputEmail_Err").html("此邮箱已被注册！");
                        }
                        else if (result == 3) {
                            $("#inputPhone").css("backgroundColor",
                                badColor);
                            $("#inputPhone_Err").css(
                                "backgroundColor", badColor);
                            $("#inputPhone_Err").html("此手机已被注册！");
                        }
                        else if (result == 4) {
                            $("#inputWechat").css("backgroundColor",
                                badColor);
                            $("#inputWechat_Err").css(
                                "backgroundColor", badColor);
                            $("#inputWechat_Err").html("此微信已被注册！");
                        }
                        else if (result == 5) {
                            $("#inputPassword_Err").css("backgroundColor",
                                badColor);
                            $("#inputPassword_Err").css(
                                "backgroundColor", badColor);
                            $("#inputPassword_Err").html("密码格式不正取，密码有数字，字母，下划线组成，长度为6-10");
                        }
                        else if (result == 6) {
                            $("#inputName_Err").css("backgroundColor",
                                badColor);
                            $("#inputName_Err").css(
                                "backgroundColor", badColor);
                            $("#inputName_Err").html("名称格式不正取，名称有数字，字母，汉字组成，长度不超过10");
                        }
                        else if (result == 1) {
                            messageAlert("注册申请已经成功提交，申请结果会发送到您的邮箱，请注意查看！",
                                "done",
                                function() {
                                    window.location.href = location.origin + "/Utopia"
                                });
                        }
                    });
                }
            });
    
    register.find('[data-action="get-validate-code"]').on('click',
        function() {
           
            getValidCode();
        });


});




function validatephone(phone) {
    var maintainplus = '';
    var numval = phone.value
    if (numval.charAt(0) == '+') {
        var maintainplus = '';
    }
    curphonevar = numval.replace(
        /[\\A-Za-z!"£$%^&\,*+_={};:'@#~,.Š\/<>?|`¬\]\[]/g, '');
    phone.value = maintainplus + curphonevar;
    var maintainplus = '';
    phone.focus;
}

function validateUserAccount(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z0-9_'\n\r.]+/g, '');
}

function getValidCode() {
    if (validateEmail()) {
    	    var icon='<i class="fa fa-spinner fa-pulse fa-3x fa-fw" style="font-size:25px;"></i>';
    	    $('#btn-validCode').html(icon);
        var email = $("#inputEmail").val();
        ajaxGetAction(URL_Registration_Validation_Code, {
            email: email
        }, function(result) {
            messageAlert("Error", "clear", function() {});
        }, function(result) {
            if (result.exist == 1) {
                $("#inputEmail").css("backgroundColor", badColor);
                $("#inputEmail_Error").css("color", badColor);
                $("#inputEmail_Error").html("This email has been registered！");
                $('#btn-validCode').html('Get Code');
            } else {
                $('#btn-validCode').html('Code Sent');
                $("#validCode").attr("code", result.code);
                $("#validCode").prop("disabled", false);
                $('#btn-validCode').prop("disabled", true);
                messageAlert("Code has been sent to your email!", "done", function() {});
                timedCount(300);
            }
        });
    }
}

function validateEmail() {
    var email = document.getElementById("inputEmail").value;
    var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
    if (!regMail.test(email)) {
        $("#inputEmail").css("backgroundColor", badColor);
        $("#inputEmail_Error").css("color", badColor);
        $("#inputEmail_Error").html("The format of your is email is not correct！");
        return false;
    } else {
        return true;
    }
}

function timedCount(time) {
    $('#btn-validCode').text(time + ' s');
    // console.log(c);
    if (time-- != 0)
        var t = setTimeout(timedCount, 1000, time--);
    else {
        $('#btn-validCode').html('Re-get the code');
        $('#btn-validCode').prop("disabled", false);
        $("#validCode").prop("disabled", true);
        $("#validCode").attr("code", "");
    }
}