
const domain_name = "CodeRecall"
const URLDOMAIN = location.origin + "/" + domain_name;
const URLPrefix = location.origin + "/" + domain_name;
const ImageURLPrefix = location.origin + "/";
const URL_Logout = URLPrefix + "/action/logout";
const URL_Get_Current_User_Instance = URLPrefix + "/action/get_current_user";
const URL_Login_Normal = URLPrefix + "/action/login";
const URL_Send_Item_Comment = URLPrefix + "/action/send_item_comment"
const URL_Remove_Item_Comment = URLPrefix + "/action/remove_item_comment"
const URL_Get_Item_Comments = URLPrefix + "/action/get_item_comments";
const URL_Marke_Item_Notice_AS_Read = URLPrefix + "/action/marke_item_notice_asread";
const URL_Get_Item_Notices = URLPrefix + "/action/get_item_notices";
const URL_GET_ITEMVERSION_LOGS = URLPrefix + "/action/get_item_single_version";
const URL_Get_Log_Status = URLPrefix + "/action/check_log_status";
const URL_Extend_Log_Status = URLPrefix + "/action/extend_log_status";
const URL_GET_Single_Post = URLPrefix + "/action/get_single_post";
const URL_Get_Instance_Followings = URLPrefix + "/action/get_user_followings_all";
const URL_Get_Instance_Followers = URLPrefix + "/action/get_instance_followers";


const goodColor = "#66cc66";
const badColor = "#ff6666";

function getCookieAttribute(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2)
        return parts.pop().split(";").shift();
}

var showLoadingMoreButton = function() {
    var block = $('#main-instance-block').find('[data-name="main-content-block"]');
    var alls = [];
    block.find('[data-name="item-single-instance"]').map(function(index, instance) {
        alls.push($(instance).attr("data-id"));
    });

    if (alls.length !== 0 && alls.length % 6 == 0) {
        $('#btn-rightbot-more').show();
    } else {
        $('#btn-rightbot-more').hide();
    }

}

function setModalAnimation(instance, value) {
    /* <select class="form-control" id="entrance">
    <optgroup label="Attention Seekers">
      <option value="bounce">bounce</option>
      <option value="flash">flash</option>
      <option value="pulse">pulse</option>
      <option value="rubberBand">rubberBand</option>
      <option value="shake">shake</option>
      <option value="swing">swing</option>
      <option value="tada">tada</option>
      <option value="wobble">wobble</option>
      <option value="jello">jello</option>
    </optgroup>
    <optgroup label="Bouncing Entrances">
      <option value="bounceIn" selected>bounceIn</option>
      <option value="bounceInDown">bounceInDown</option>
      <option value="bounceInLeft">bounceInLeft</option>
      <option value="bounceInRight">bounceInRight</option>
      <option value="bounceInUp">bounceInUp</option>
    </optgroup>
    <optgroup label="Fading Entrances">
      <option value="fadeIn">fadeIn</option>
      <option value="fadeInDown">fadeInDown</option>
      <option value="fadeInDownBig">fadeInDownBig</option>
      <option value="fadeInLeft">fadeInLeft</option>
      <option value="fadeInLeftBig">fadeInLeftBig</option>
      <option value="fadeInRight">fadeInRight</option>
      <option value="fadeInRightBig">fadeInRightBig</option>
      <option value="fadeInUp">fadeInUp</option>
      <option value="fadeInUpBig">fadeInUpBig</option>
    </optgroup>
    <optgroup label="Flippers">
      <option value="flipInX">flipInX</option>
      <option value="flipInY">flipInY</option>
    </optgroup>
    <optgroup label="Lightspeed">
      <option value="lightSpeedIn">lightSpeedIn</option>
    </optgroup>
    <optgroup label="Rotating Entrances">
      <option value="rotateIn">rotateIn</option>
      <option value="rotateInDownLeft">rotateInDownLeft</option>
      <option value="rotateInDownRight">rotateInDownRight</option>
      <option value="rotateInUpLeft">rotateInUpLeft</option>
      <option value="rotateInUpRight">rotateInUpRight</option>
    </optgroup>
    <optgroup label="Sliding Entrances">
      <option value="slideInUp">slideInUp</option>
      <option value="slideInDown">slideInDown</option>
      <option value="slideInLeft">slideInLeft</option>
      <option value="slideInRight">slideInRight</option>
    </optgroup>
    <optgroup label="Zoom Entrances">
      <option value="zoomIn">zoomIn</option>
      <option value="zoomInDown">zoomInDown</option>
      <option value="zoomInLeft">zoomInLeft</option>
      <option value="zoomInRight">zoomInRight</option>
      <option value="zoomInUp">zoomInUp</option>
    </optgroup>
    
    <optgroup label="Specials">
      <option value="rollIn">rollIn</option>
    </optgroup>
  </select>*/
    $(instance).find('.modal-dialog').attr('class', 'modal-dialog  ' + value + '  animated');
}

function setCookieAttribute(name) {
    document.cookie = "tid=" + name + ";path=/" + domain_name;
}

function setCookie(cname, cvalue, path) {
	  var d = new Date();
	  d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000));
	  var expires = "expires="+d.toUTCString();
	  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/"+path;
}

var delete_cookie = function() {
    document.cookie = 'tid=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/' + domain_name;
}


var showLoadingAnimation = function() {
    $('.ml9 .letters').each(function() {
        $(this).html($(this).text().replace(/([^\x00-\x80]|\w)/g, "<span class='letter'>$&</span>"));
    });

    anime.timeline({
            loop: true
        })
        .add({
            targets: '.ml9 .letter',
            scale: [0, 1],
            duration: 1500,
            elasticity: 600,
            delay: function(el, i) {
                return 45 * (i + 1)
            }
        }).add({
            targets: '.ml9',
            opacity: 0,
            duration: 1000,
            easing: "easeOutExpo",
            delay: 1000
        });
}

var SessionModal = function() {
    var session = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" id="exampleModal" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
        '<div class="modal-dialog" role="document">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title" id="exampleModalLabel">登录过期提醒</h5>\n' +
        '</div>\n' +
        '<div class="modal-body">\n' +
        '<span>你的页面即将过期</span><span data-name="expire_timer"></span>\n' +
        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-secondary" data-name="logout">退出登录</button>\n' +
        '<button type="button" class="btn btn-primary" data-name="login">继续登录</button>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>');

    // 加载slimscroll

    (function() {
        var timer;
        var timeFlag;
        var out_time = 120000;
        var total = 30;

        session.find('[data-name="login"]').on('click',
            function() {
                clearInterval(timer);
                ajaxPostAction(URL_Extend_Log_Status, {}, function(result) {
                    messageAlert("系统发生错误，请重新登录", "clear", function() {
                        window.location.href = URLDOMAIN;
                    });
                }, function(result) {
                	setCookie("uid", result.uid, result.path);
                    setCookie("token", result.token, result.path);
                    session.modal('hide');
                });
            });

        session.find('[data-name="logout"]').on('click',
            function() {
                clearInterval(timer);
                ajaxPostAction(URL_Logout, {}, function(result) {
                    messageAlert("请求发送失败，请重新尝试", "clear", function() {});
                }, function(result) {
                    window.location.href = URLDOMAIN;
                });
            });

        session.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });
        setInterval(function() {
            ajaxPostAction(URL_Get_Log_Status, {}, function(result) {
                messageAlert("系统发生错误，请重新登录", "clear", function() {
                    window.location.href = URLDOMAIN;
                });
            }, function(result) {
                console.log(result);
                if (result == 2) {
                    session.modal('show');
                    timer = setInterval(function() {
                        if (total < 0) {
                            clearInterval(timer);
                            ajaxPostAction(URL_Logout, {}, function(result) {
                                messageAlert("请求发送失败，请重新尝试", "clear", function() {});
                            }, function(result) {
                                window.location.href = URLDOMAIN;
                            });
                        } else {
                            session.find('[data-name="expire_timer"]').text(total + "秒");
                            total--;
                        }

                    }, 1000);
                }
            });
        }, out_time);

    })();

}

var updateImage = function(id, url, callback) {
    var html = $('<div class="modal fade" data-backdrop="static" data-keyboard="false">\n' +
        '	<div class="modal-dialog">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="main-content">\n' +
        '					<div class="modal-header">\n' +
        '						<h4 class="modal-title">上传图片</h4>\n' +
        '						<button type="button" class="close" data-dismiss="modal">×</button>\n' +
        '					</div>\n' +
        '					<div style="margin: 15px;">\n' +
        '					<input name="file_data" type="file" class="file-loading" accept="image">\n' +
        '					</div>\n' + '				</div>\n' + '			</div>\n' + '		</div>\n' +
        '	</div>\n' + '</div>');

    // 加载bootstrap-fileinput插件
    html.find('input').fileinput({
        language: "zh",
        theme: "explorer",
        uploadAsync: false,
        uploadUrl: url + '?id=' + id,
        allowedFileExtensions: ['jpg', 'png', 'JPEG', 'JPG', 'PNG'],
        maxFileCount: 1,
    });

    html.find('input').on('filebatchuploadsuccess', function(event, data, previewId, index) {
        if (data.response == 0) {
            messageAlert('图片上传出现错误！', 'clear', function() {});
        } else {
            messageAlert('图片更新成功！', 'done', function() {
                html.modal("hide");
                callback(data.response.src);
            });
        }
    });

    html.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });

    html.modal('show');
};


var googleColorRandomPicker = function() {
    var color_list = ['rgb(55, 141, 59)', 'rgb(65, 65, 65)',
        'rgb(29, 135, 228)', 'rgb(119, 143, 155)', 'rgb(91, 106, 191)',
        'rgb(125, 86, 193)', 'rgb(248, 167, 36)', 'rgb(255, 111, 66)',
        'rgb(235, 63, 121)', 'rgb(229, 57, 53)', 'rgb(140, 109, 98)'
    ];
    var random = Math.floor(Math.random() * (color_list.length));
    // console.log(color_list[random]);
    return color_list[random];
};

var ajaxGetAction = function(url, data, errfunction, execution) {
    $.ajax({
        url: url,
        data: data,
        type: "GET",
        dataType: 'json',
        success: function(instance) {
            if (instance === 0) {
                errfunction();
            } else if (instance == -1) {
                window.location.href = URLDOMAIN;
            } else if (instance == -2) {

            } else {
                execution(instance);
            }
        },
        error: function(err) {
            messageAlert("Error", "clear", function() {});
        }
    });
}

var ajaxPostAction = function(url, data, errfunction, execution) {
    $.ajax({
        url: url,
        data: data,
        type: "POST",
        dataType: 'json',
        success: function(instance) {
            if (instance === 0) {
                errfunction();
            } else if (instance == -1) {
                window.location.href = URLDOMAIN;
            } else if (instance == -2) {

            } else {
                execution(instance);
            }
        },
        error: function(err) {
            messageAlert("Error", "clear", function() {});
        }
    });
}

var getStarRating = function(value) {
    var result = "";
    var fs = parseInt(value / 10);
    var es = value % 10;
    var index = 0;
    for (index = 0; index < fs; index++) {
        result += '<span class="fa fa-star" style="color:orange;"></span>';
    }
    if (es >= 5) {
        result += '<span class="fa fa-star-half-o" style="color:orange;"></span>';
        index++;
    }
    for (index; index < 5; index++) {
        result += '<span class="fa fa-star-o"></span>';
    }
    return result;
}

var loadingPageShow = function(flag) {
    if (flag) {
        $("#overlay_page").show();
        //		setTimeout(function(){ 
        //			messageAlert("正在努力获取数据中，请耐心等待！","warning", function(){
        //				setTimeout(function(){ 
        //					messageAlert("获取数据出现错误，请重新刷新页面！","clear", function(){$("#overlay_page").hide();});
        //				}, 6000);
        //			});
        //		}, 6000);
    } else {
        $("#overlay_page").hide();
    }
}



/* 2. 提示框 */
var messageAlert = function(text, flag, callback) {
    var myvar = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">' +
        '  <div class="modal-dialog" role="document">' +
        '    <div class="modal-content">' +
        '      <div class="modal-header" data-name="info-header">' +
        '        <h5 class="modal-title" data-name="info-title" style="color:white;">Modal title</h5>' +
        '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '          <span aria-hidden="true">×</span>' +
        '        </button>' +
        '      </div>' +
        '      <div class="modal-body" data-name="mesg-info">' +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '</div>');

    myvar.find('[data-name="mesg-info"]').html(text);
    if (flag == "done") {
        myvar.find('[data-name="info-title"]').html('Done');
        myvar.find('[data-name="info-header"]').css("background", "#28a745");
    } else if (flag == "warning") {
        myvar.find('[data-name="info-title"]').html('Warnning');
        myvar.find('[data-name="info-header"]').css("background", "#ffc107");
        //dialogInstance2.setType(BootstrapDialog.TYPE_WARNING);
    } else if (flag == "clear" || flag == "error") {
        myvar.find('[data-name="info-header"]').css("background", "#dc3545");
        myvar.find('[data-name="info-title"]').html('Error');
    }
    myvar.on('show.bs.modal', function(e) {
        setModalAnimation(myvar, "fadeIn");

    });
    myvar.on('hidden.bs.modal', function(e) {
        $("body").css("padding-right", "0px");
        setModalAnimation(myvar, "zoomOut");
    });

    myvar.modal("show");
    setTimeout(function() {
        myvar.modal("hide");
        callback();
    }, 1500);
};


/* 3. 确认框 */
var showLogModal = function(logs) {
    var modal = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog">\n' +
        '<div class="modal-dialog" role="document" style="min-width:65vw; max-height:700px;">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title">程序运行的log</h5>\n' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '<span aria-hidden="true">&times;</span>\n' +
        '</button>\n' +
        '</div>\n' +
        '<div class="modal-body" data-name="log-content" style="overflow:scroll;">\n' +

        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-secondary" data-action="cancel" data-dismiss="modal">关闭</button>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>');
    $.each(logs, function(index, log) {
        modal.find('[data-name="log-content"]').append("<div>" + log + "</div></br>");
    });
    modal.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
    modal.modal("show");
};

/* 3. 确认框 */
var callConfirm = function(title, text, actionConfirm, actionCancel) {
    var modal = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog">\n' +
        '<div class="modal-dialog" role="document">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title">' + title + '</h5>\n' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '<span aria-hidden="true">&times;</span>\n' +
        '</button>\n' +
        '</div>\n' +
        '<div class="modal-body">\n' +
        '  <p>' + text + '</p>\n' +
        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-primary" data-action="confirm">确定</button>\n' +
        '<button type="button" class="btn btn-secondary" data-action="cancel" data-dismiss="modal">取消</button>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>');
    modal.on('click', '[data-action="confirm"]', function() {
        modal.modal("hide");
        actionConfirm();
    });
    modal.on('click', '[data-action="cancel"]', function() {
        actionCancel();
    });

    modal.on('show.bs.modal', function(e) {
        setModalAnimation(modal, "zoomIn");

    });
    modal.on('hidden.bs.modal', function(e) {
        $("body").css("padding-right", "0px");
        setModalAnimation(modal, "zoomOut");
    });

    modal.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
    modal.modal("show");
};



/* 3. 确认框 */
var callConfirm_Save = function(title, actionConfirm, actionCancel) {
    var modal = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog">\n' +
        '<div class="modal-dialog" role="document">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title">' + title + '</h5>\n' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '<span aria-hidden="true">&times;</span>\n' +
        '</button>\n' +
        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-primary" data-action="confirm">保存并关闭</button>\n' +
        '<button type="button" class="btn btn-secondary" data-action="cancel" data-dismiss="modal">关闭</button>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>');
    modal.on('click', '[data-action="confirm"]', function() {

        modal.modal("hide");
        actionConfirm();
    });
    modal.on('click', '[data-action="cancel"]', function() {
        modal.modal("hide");
        actionCancel();

    });

    modal.on('show.bs.modal', function(e) {
        setModalAnimation(modal, "zoomIn");

    });
    modal.on('hidden.bs.modal', function(e) {
        $("body").css("padding-right", "0px");
        setModalAnimation(modal, "zoomOut");
    });

    modal.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
    modal.modal("show");
};


/* 3. 确认框 */
var callConfirm_Save_Button = function(title, actionName, cancelName, actionConfirm, actionCancel) {
    var modal = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog">\n' +
        '<div class="modal-dialog" role="document">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title">' + title + '</h5>\n' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '<span aria-hidden="true">&times;</span>\n' +
        '</button>\n' +
        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-primary" data-action="confirm">' + actionName + '</button>\n' +
        '<button type="button" class="btn btn-secondary" data-action="cancel" data-dismiss="modal">' + cancelName + '</button>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>');
    modal.on('click', '[data-action="confirm"]', function() {

        modal.modal("hide");
        actionConfirm();
    });
    modal.on('click', '[data-action="cancel"]', function() {
        modal.modal("hide");
        actionCancel();

    });

    modal.on('show.bs.modal', function(e) {
        setModalAnimation(modal, "zoomIn");

    });
    modal.on('hidden.bs.modal', function(e) {
        $("body").css("padding-right", "0px");
        setModalAnimation(modal, "zoomOut");
    });

    modal.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
    modal.modal("show");
};


var callConfirm_Name = function(title, text, actionConfirm, actionCancel) {
    $.confirm({
        title: title,
        content: text,
        buttons: {
            "保存后关闭": function() {
                actionConfirm();
                /* callAlert('操作完成', 'done'); */
            },
            "直接关闭": function() {
                actionCancel();
            }
        }
    });
};



/* 8. 重写时间 */
var formatDatetime = function(datetime) {
    datetime = datetime.split(" ");
    var eles = datetime[0].split("-");
    var later = datetime[1].split(":");
    return eles[0] + "年" + eles[1] + "月" + eles[2] + "号 " + datetime[1];
};

var getDormatDate = function(datetime) {
    datetime = datetime.split(" ");
    var eles = datetime[0].split("-");
    var later = datetime[1].split(":");
    return eles[0] + "年" + eles[1] + "月" + eles[2] + "号 ";
};



function isExisted(flag, item, all) {
    var result = 0;
    if (all == null || all.length == 0) {
        return 0;
    }
    if (flag == 0) {
        $.each(all, function(i, user) {
            if (user == item) {
                result = 1;
                return false;
            }
        });
        return result;
    } else {
        $.each(all, function(i, user) {
            if (user.id == item.id) {
                result = 1;
                return false;
            }
        });
        return result;
    }
}

var TwinRow_Tag = function(title, selected, execution) {
    var controller = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">\n' +
        '<div class="modal-dialog" role="document">\n' +
        '<div class="modal-content">\n' +
        '<div class="modal-header">\n' +
        '<h5 class="modal-title" data-name="title"></h5>\n' +
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '<span aria-hidden="true">&times;</span>\n' +
        '</button>\n' +
        '</div>\n' +
        '<div class="modal-body">\n' +
        '<div class="container-fluid" style="padding-left:0px; padding-right:0px;">\n' +
        '<div class="row" style="margin-top:0px;">\n' +
        '<div class="col-md-8 col-sm-8" data-name="left-block-list" style="margin-top:0px; border-right:1px solid #e5e5e5">\n' +
        '<div class="form-group">\n' +
        '<span class="fa fa-plus" data-action="add-single" data-name="add-button" style="font-size: 30px; color:black;padding-top: 3px;"></span>\n' +
        '<input type="text" class="form-control" placeholder="搜索...(区分大小写)" data-action="searchbot" data-name="searchbutton" style="float:right;width:88%"/ >\n' +
        '</div>\n' +
        '<div class="clearfix" data-name="delete-selected"></div>\n' +
        '<div class="row-bot" data-name="all-data"><ul class="list-group" data-name="all-data-content" id="all_data_content" style="margin-top:10px;"></ul>\n' +
        '</div>\n' +
        '</div>\n' +
        '<div class="col-md-4 col-sm-4"  style="background-color:#f6f7f9;">\n' +
        '<div  data-name="right-selected-area"><span style="float:left; margin-top:5px;">选择的</span><span style="float:right; margin-top:5px;" data-name="selected-number">0</span><ul data-name="selected-data" class="list-group" style="width:145px; background:transparent;"></ul></div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '<div class="modal-footer">\n' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>\n' +
        '<button type="button" class="btn btn-primary" data-action="submit">确定</button>\n' +
        '</div>\n' + '</div>\n' + ' </div>\n' + '</div>');

    /* 初始化 */
    (function() {
        // 加载第一行搜索
        controller.find('[data-name="title"]').html(title);
        controller.find('[data-name="add-button"]').on('click',
            function() {
                var diag = SingleRowInput("创建新的标签", "请输入标签名称", function(name) {
                    ajaxGetAction(URL_ADD_TAG, {
                        tag: name
                    }, function() {
                        messageAlert("发送请求失败， 请重新尝试", "clear", function() {});
                    }, function(result) {
                        if (result == 1) {
                            messageAlert("此名称已经存在", "warning", function() {});
                        } else if (result == 2) {
                            messageAlert("创建成功", "done", function() {});
                            diag.close();
                            controller.find('[data-name="selected-data"]').prepend(
                                '<li class="list-group-item list-item-twin-row" style="padding-left:0px; padding-right:0px; background:transparent;" data-cid="' + name + '" data-action="delete-selected">\n' +
                                '<span class="fa fa-tag"></span>\n' +
                                '		<span name="name">' + name + '</span>\n' +
                                '		<span class="pull-right">\n' +
                                '		<i class="fa fa-window-close" style="position:relative; top:3px; color:#2489c5" data-name="close"></i></div>\n' +
                                '		</span>\n' +
                                '</li>'
                            );

                            var info = '<li class="list-group-item  list-item-twin-row" data-name="' + name + '" clicked="1" data-id="' + name + '" data-action="add-selected" data-type="string">\n' +
                                '<span class="fa fa-tag"></span>\n' +
                                '		<span name="name">' + name + '</span>\n' +
                                '		<span class="pull-right"  data-name="item-check-info">\n';
                            info += '<i class="fa fa-check-square" style="color:#2489c5"></i>\n';
                            info += '</span></li>';
                            controller.find('[data-name="all-data-content"]').prepend(info);
                        }
                    })
                });

            });

        controller.find('[data-name="all-data"]').slimScroll({
            height: '400px'
        });

        controller.find('[data-name="right-selected-area"]').slimScroll({
            height: '446px'
        });

        $.each(selected, function(i, user) {
            controller.find('[data-name="selected-data"]').append(
                '<li class="list-group-item list-item-twin-row" style="padding-left:0px; padding-right:0px; background:transparent;" data-cid="' + user + '" data-action="delete-selected">\n' +
                '<span class="fa fa-tag"></span>\n' +
                '		<span name="name">' + user + '</span>\n' +
                '		<span class="pull-right">\n' +
                '		<i class="fa fa-window-close" style="position:relative; top:3px; color:#2489c5" data-name="close"></i></div>\n' +
                '		</span>\n' +
                '</li>'
            );

        });

        if (selected != null) {
            controller.find('[data-name="selected-number"]').text(selected.length);
        }

        ajaxGetAction(URL_GET_TAGS, {},
            function(result) {
                messageAlert("获取数据失败", "error", function() {});
            },
            function(result) {
                $.each(result, function(i, user) {
                    var checked = false;
                    checked = isExisted(0, user, selected);
                    var info = '<li class="list-group-item  list-item-twin-row" data-name="' + user + '" clicked="' + checked + '" data-id="' + user + '" data-action="add-selected" data-type="string">\n' +
                        '<span class="fa fa-tag"></span>\n' +
                        '		<span name="name">' + user + '</span>\n' +
                        '		<span class="pull-right"  data-name="item-check-info">\n';
                    if (checked == 0) {
                        info += '<i class="fa fa-square-o"></i>\n';
                    } else {
                        info += '<i class="fa fa-check-square" style="color:#2489c5"></i>\n';
                    }

                    info += '</span></li>';
                    controller.find('[data-name="all-data-content"]').append(info);
                });

                controller.find('[data-action="add-selected"]').on('click',
                    function() {
                        //console.log($(this).find('.icheckbox_square-blue').attr("aria-checked"));
                        if ($(this).attr("clicked") == "1") {
                            $(this).attr('clicked', "0");
                            $(this).find('[data-name="item-check-info"]').html('<i class="fa fa-square-o"></i>');
                            controller.find('[data-cid="' + $(this).attr("data-id") + '"]').remove();
                            // this.remove();
                            var value = controller.find('[data-name="selected-number"]').text();
                            value = parseInt(value) - 1;
                            controller.find('[data-name="selected-number"]').text(value);
                        } else {
                            $(this).attr("clicked", "1");
                            $(this).find('[data-name="item-check-info"]').html('<i class="fa fa-check-square" style="color:#2489c5"></i>');
                            controller.find('[data-name="selected-data"]').append(
                                '<li class="list-group-item list-item-twin-row" style="padding-left:0px; padding-right:0px; background:transparent;" data-cid="' + $(this).attr("data-id") + '" data-action="delete-selected">\n' +
                                '<span class="fa fa-tag"></span>\n' +
                                '		<span name="name">' + $(this).attr("data-id") + '</span>\n' +
                                '		<span class="pull-right">\n' +
                                '		<i class="fa fa-window-close" style="position:relative; top:3px; color:#2489c5" data-name="close"></i></div>\n' +
                                '		</span>\n' +
                                '</li>'
                            );
                            var value = controller.find('[data-name="selected-number"]').text();
                            value = parseInt(value) + 1;
                            controller.find('[data-name="selected-number"]').text(value);
                        }

                    });
            });

        controller.find('[data-action="submit"]').on('click',
            function() {
                //var name = controller.find('[data-name="created-name"]').val();
                var c_data = [];
                var collection = controller.find('[data-name="selected-data"]');

                $.each(collection.find('li'), function(i, item) {
                    c_data.push($(item).attr("data-cid"));
                });
                if (c_data.length > 3) {
                    messageAlert("最多选择3个标签！", "clear", function() {});
                } else {
                    execution(c_data);
                    controller.modal('hide');
                }

                // console.log(c_data);

            });


        controller.on('click', '[data-action="delete-selected"]', function() {
            var value = controller.find('[data-name="selected-number"]').text();
            value = parseInt(value) - 1;
            controller.find('[data-name="selected-number"]').text(value);
            var item = controller.find('[data-id="' + $(this).attr("data-cid") + '"]');
            $(item).find('[data-name="item-check-info"]').html('<i class="fa fa-square-o"></i>');
            console.log("test");
            $(this).remove();
        });


        controller.find('[data-name="searchbutton"]').on("keyup", function() {
            var value = $(this).val();
            $("#all_data_content li").filter(function() {
                var filter = $(this).attr("data-name").indexOf(value) > -1
                $(this).toggle(filter);
            });
        });

        controller.modal('show');

    })();
};


var SingleRowInput = function(title, mesg, execution) {


    var modal = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">' +
        '  <div class="modal-dialog" role="document">' +
        '    <div class="modal-content">' +
        '      <div class="modal-header">' +
        '        <h5 class="modal-title" id="exampleModalLabel">' + title + '</h5>' +
        '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '          <span aria-hidden="true">×</span>' +
        '        </button>' +
        '      </div>' +
        '      <div class="modal-body">' +
        '        <div class="input-group mb-3">' +
        '          <div class="input-group-prepend">' +
        '             <span class="input-group-text" id="inputGroup-sizing-default">' + mesg + '</span>' +
        '          </div>' +
        '          <input type="text" class="form-control" data-name="input_value" aria-label="Default" aria-describedby="inputGroup-sizing-default">' +
        '       </div>' +
        '      </div>' +
        '      <div class="modal-footer">' +
        '        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>' +
        '        <button type="button" class="btn btn-primary" data-name="data_submit">确定</button>' +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '</div>');

    modal.on('click', '[data-name="data_submit"]', function() {
        var name = modal.find('[data-name="input_value"]').val();
        if (name == "") {
            messageAlert("输入内容不能为空", "clear", function() {});
        } else {
            execution(name, function() {
                modal.modal("hide");
            });
        }
    });
    modal.modal("show");
}

var findSmallestIndex = function(array) {
    if (!Array.isArray(array) || array.length == 0) {
        return -1;
    } else {
        var index = 0;
        var value = array[0];
        for (i = 1; i < array.length; i++) {
            if (array[i] < value) {
                value = array[i];
                index = i;
            }
        };
        return index;
    }
};



var CreateCommentController = function(itemid, type, tuid, callback) {
    var html = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" >\n' +
        '	<div class="modal-dialog" style="min-width:60vw;">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="main-content">\n' +
        '					<div class="modal-header">\n' +
        '						<h4 class="modal-title" data-name="modal-title">发表评论</h4>\n' +
        '						<button type="button" class="close" data-dismiss="modal">&times</button>\n' +
        '					</div>\n' +
        '					<div style="margin: 10px;">\n' +
        '						<div class="form-group" data-name="comment-star-div">\n' +
        '						    <span>您的评分</span>\n' +
        '							<div style="display: inline;position: relative;top: 5px;">\n' +
        '                               <span class="fa fa-star" data-id="comment-star-1" data-name="comment-star" value="1" style="font-size:30px;cursor:pointer;"></span>\n' +
        '                               <span class="fa fa-star" data-id="comment-star-2" data-name="comment-star" value="2" style="font-size:30px;cursor:pointer;"></span>\n' +
        '                               <span class="fa fa-star" data-id="comment-star-3" data-name="comment-star" value="3" style="font-size:30px;cursor:pointer;"></span>\n' +
        '                               <span class="fa fa-star" data-id="comment-star-4" data-name="comment-star" value="4" style="font-size:30px;cursor:pointer;"></span>\n' +
        '                               <span class="fa fa-star" data-id="comment-star-5" data-name="comment-star" value="5" style="font-size:30px;cursor:pointer;"></span>\n' +
        '                               <span data-name="comment-star-value" value="0" style="font-size:25px;">0/5</span>\n' +
        '                            </div>\n' +
        '						</div>\n' +
        '					    <div class="form-group" data-name="comment_block">\n' +
        '					    </div>\n' +
        '					</div>\n' +
        '				</div>\n' +
        '				<div class="modal-footer">\n' +
        '                  <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>\n' +
        '                  <button type="button" class="btn btn-primary" data-action="submit">确定</button>\n' +
        '				</div>\n' +
        '			</div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>');
    var getData = function() {
        var rate = 0;
        if (type == 1) {
            rate = html.find('[data-name="comment-star-value"]').attr("value");
        } else {
            rate = 0;
        }
        return {
            rate: rate,
            type: type,
            to_uid: tuid,
            content: html.find('.editor').html(),
            refer_id: itemid
        };
    };
    // 初始化
    (function() {

        html.find('[data-name="comment_block"]').append(getEditor(itemid));
        if (type == 1) {
            html.find('[data-name="modal-title"]').text("发表您的评论");
            //html.find('[data-name="star-value"]').rating();
            html.find('[data-name="comment-star"]').hover(function() {
                var value = $(this).attr("value");
                html.find('[data-name="comment-star-value"]').text(value + "/5");
                html.find('[data-name="comment-star-value"]').attr("value", value);
                for (var i = 1; i <= 5; i++) {
                    var id = '[data-id="comment-star-' + i + '"]';
                    if (i <= value) {
                        html.find(id).css("color", "orange");
                    } else {
                        html.find(id).css("color", "black");
                    }

                }
            });
        } else {
            html.find('[data-name="modal-title"]').text("发送您的回复");
            html.find('[data-name="comment-star-div"]').hide();
        }

        html.find('[data-action="submit"]').on(
            'click',
            function() {
                var data = getData();
                if(data.content=="")
                {
                	messageAlert("内容不能为空", "error",
                            function() {});
                	return;
                }
                if (data.rate == 0 && data.type == 1) {
                    callConfirm("再次确认", "确定您的评分为0？", function() {
                        ajaxPostAction(URL_Send_Item_Comment, data,
                            function() {
                                messageAlert("评论失败", "error",
                                    function() {});
                            },
                            function(result) {
                                messageAlert("评论成功", "done",
                                    function() {
                                        html.modal('hide');
                                        callback();

                                    });

                            });
                    }, function() {});
                } else {
                    ajaxPostAction(URL_Send_Item_Comment, data,
                        function() {
                            messageAlert("评论失败", "error",
                                function() {});
                        },
                        function(result) {
                            messageAlert("评论成功", "done",
                                function() {
                                    html.modal('hide');
                                    callback();

                                });

                        });
                }

            });
        html.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });

        html.modal('show');
    })();
};

var commentBlock = function(comment, callback) {
    var card = $(
        '<div class="post clearfix col-12">\n' +
        '    <div class="user-block">\n' +
        '      <img class="img-circle img-bordered-sm" data-name="comment-from-uimage" src="" alt="">\n' +
        '      <span class="username">\n' +
        '        <a href="#" data-name="comment-from-uname"></a>\n' +
        '        <a href="#" data-name="comment-from-and"></a>\n' +
        '        <a href="#" data-name="comment-to-uname"></a>\n' +
        '        <a href="#" data-name="comment-rate-info"></a>\n' +
        '        <a href="#" class="float-right btn-tool" data-name="remove-comment" style="display:none;"><i class="fa fa-times"></i></a>\n' +
        '      </span>\n' +
        '      <span class="description" data-name="comment-datetime"></span>\n' +
        '    </div>\n' +
        '    <div data-name="comment-content" class="comment-content" style="margin-bottom:10px;"></div>\n' +
        '    <p style="margin-bottom:0px;">\n' +
        '      <a href="javascript:void(0);" data-name="comment-action" data-action="report" class="link-black text-sm mr-2" style="color:#007bff"><i class="fa fa-frown-o"></i>举报</a>\n' +
        '      <a href="javascript:void(0);" data-name="comment-action" data-action="comment-back" class="link-black text-sm mr-2" style="color:#007bff"><i class="fa fa-commenting-o" aria-hidden="true"></i>回复</a>\n' +
        '    </p>\n' +
        '</div>');
    if (comment.isOwner == 1) {
        card.find('[data-name="comment-action"]').hide();
        card.find('[data-name="remove-comment"]').show();
    }
    if (comment.hasLogin == 0) {
        card.find('[data-name="comment-action"]').hide();
        card.find('[data-name="remove-comment"]').hide();
    }
    if (comment.type == 1) {
        card.find('[data-name="comment-from-uimage"]').attr("src", comment.from_uimage);
        card.find('[data-name="comment-from-uname"]').text(comment.from_uname);
        card.find('[data-name="comment-rate-info"]').append(getStarRating(comment.rate));
        card.find('[data-name="comment-datetime"]').text(formatDatetime(comment.datetime));
        card.find('[data-name="comment-content"]').html(comment.content);
    } else {
        card.find('[data-name="comment-from-uimage"]').attr("src", comment.from_uimage);
        card.find('[data-name="comment-from-uname"]').text(comment.from_uname);
        card.find('[data-name="comment-from-and"]').text("@");
        card.find('[data-name="comment-to-uname"]').text(comment.to_uname);
        card.find('[data-name="comment-datetime"]').append(formatDatetime(comment.datetime));
        card.find('[data-name="comment-content"]').html(comment.content);
    }

    card.on('click', '[data-name="remove-comment"]', function() {
        var obj = $(this);
        callConfirm('删除评论', '您确定要删除此评论吗？',
            function() {
                ajaxGetAction(URL_Remove_Item_Comment, {
                    id: comment.id
                }, function() {
                    messageAlert("删除失败", "error",
                        function() {});
                }, function(data) {
                    messageAlert("删除成功", "done",
                        function() {
                            card.remove();
                        });
                });
            },
            function() {});
    });

    card.on('click', '[data-action="comment-back"]', function() {
        var commentBack = new CreateCommentController(comment.refer_id, 2, comment.from_uid, function() {
            callback();
        });
    });
    return card;
}

var createMenu = function(data, position, icon) {
    if (data == null || data == undefined) {
        console.log('data is null or undefined');
    } else if (data.length == 0) {
        return '';
    } else {
        // Menu 框架
        if (icon == undefined || icon == null) {
            icon = '<i class="fa fa-cog"></i>'
        }
        var pclass = ""
        if (position == "right") {
            pclass = "dropdown-menu-right";
        }
        var menu = $('<div class="dropdown">\n' +
            '	<a href="#"  data-toggle="dropdown" style="color: rgba(255,255,255,1); margin-left: 20px;">' +
            icon + '</a>\n' + '	<ul class="dropdown-menu ' + pclass +
            '">\n' + '	</ul>\n' + '</div>');

        // 加载menu item
        $.each(data, function(index, d) {
            if (d.sublist == undefined) {
                // 无子菜单
                var item = $('<li><a href="javascript:void(0);">' + d.name +
                    '</a></li>');
                item.on('click', function() {
                    d.action();
                })

                menu.find('> ul').append(item);
            } else {
                // 子菜单， 仅支持二级菜单
                var item = $('<li class="dropdown-submenu">\n' +
                    '	<a class="subtoggle" href="#">' + d.name +
                    ' <span class="fa fa-caret-right"></span></a>\n' +
                    '	<ul class="dropdown-menu">\n' + '	</ul>\n' +
                    '</li>');

                // 子菜单item
                $.each(d.sublist, function(i, sub) {
                    var subitem = $('<li><a href="javascript:void(0);">' +
                        sub.name + '</a></li>');
                    subitem.on('click', function() {
                        sub.action();
                    })
                    item.find('> .dropdown-menu').append(subitem);
                });

                // 关闭其他子菜单
                item.on("click", function(e) {
                    menu.find('.dropdown-submenu ul').hide();
                    $(this).find('> ul').toggle();
                    e.stopPropagation();
                    e.preventDefault();
                });

                menu.find('> ul').append(item);
            }
        });

        // 关闭主菜单时,自动关闭子菜单

        menu.on('hidden.bs.dropdown', function() {
            $(this).find('.dropdown-submenu ul').hide();
        });
        // var left=menu.offset().left;
        // var top=menu.offset().top;
        // if(option==1)
        // {
        // menu.on('hidden.bs.dropdown', function () {
        // $('.self-menu').append(menu.css({
        // position:"relative", left:left, top:top
        // }).detach());
        // });
        // menu.on('show.bs.dropdown', function () {
        // $('body').append(menu.css({
        // position:'absolute',
        // left:menu.offset().left,
        // top:menu.offset().top
        // }).detach());
        // });
        // }
        // else
        // {
        //			
        //
        //		    
        // }
        return menu;
    }
};


var initSearchFuntion = function(URL,type, key) {
    var module = $("#main_body");
    if (key !== "") {
        module.find('[data-name="instance-search-input"]').val(key);
    }
    module.find('[data-name="instance-search-button"]').on(
        'click',
        function(e) {
            var value = $(module.find('[data-name="instance-search-input"]')).val();
            if (value !== "") {
                window.location.href = URL +"?type="+type+ "&q=" + value;
            } else {
                messageAlert('Plese input the query word!', 'clear', function() {});
            }
        });
    module.find('[data-name="instance-search-input"]').keydown(function(e) {
        // e.preventDefault();
        if (e.keyCode == 13) {
            e.preventDefault();
        }
    });
    module.find('[data-name="instance-search-input"]').keyup(
        function(e) {
            e.preventDefault();
            if (e.keyCode == 13) {
                var value = $(module.find('[data-name="instance-search-input"]')).val();
                if (value !== "") {
                    window.location.href = URL +"?type="+type+ "&q=" + value;
                } else {
                    messageAlert('Plese input the query word!!', 'clear', function() {});
                }
            }
        });
}