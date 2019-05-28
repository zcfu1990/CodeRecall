/**
 * 
 */
const URL_Get_Customer_Page = URLPrefix + "/";
const URL_Get_Recall_Search_Customer = URLPrefix + "/action/get_recall_search";
const URL_Get_Recall_Search_Customer_Tag = URLPrefix + "/action/get_recall_search_tag";
const URL_Get_Customer_User_Instance = URLPrefix + "/action/get_single_user_customer";
const URL_Get_Customer_Search_Pre = URLPrefix + "/search";
const URL_Get_Recall_Posts_Customer= URLPrefix + "/action/get_recommend_recalls_customer";
const URL_Get_Star_Recalls_Customer=URLPrefix + "/action/get_star_recalls";
const URL_Update_Instance_Basic_Info = URLPrefix + "/action/update_instance_settings";
$(document).ready(function() {
    var mainPage = $("#main_body");
    var search_type = "";
    var filter = 0;
    if (mainPage.attr("data-id") == "recall") {
        filter = 1;
        search_type = "recall";
    } else if (mainPage.attr("data-id") == "search_recall") {
        filter = 2;
        search_type = "recall";
    } else if (mainPage.attr("data-id") == "search_recall_tag") {
        filter = 21;
        search_type = "recall";
    } else if (mainPage.attr("data-id") == "user") {
        filter = 3;
        search_type = "user";
    }
    
    initSearchFuntion(URL_Get_Customer_Search_Pre, search_type, mainPage.attr("data-key"));
    
    var mainController = new MainBlockController();
    getTopUserLane(mainController);
     if (filter == 1) {
        initNavList(filter);
        loadingPageShow(true);
        showPageSection("post");
        ajaxPostAction(URL_Get_Recall_Posts_Customer, {
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not get data", "error",
                function() {});
        }, function(data) {
            loadingPageShow(false);
            mainController.loadPosts(data, $("#template-all-block"));
        });
    } else if (filter == 3) {
        ajaxGetAction(URL_Get_Customer_User_Instance, {
        }, function() {
            messageAlert("Can not get data", "error",
                function() {});
        }, function(data) {
            //$("#item_search_subtopic").hide();
            initNavList(filter, data.name);
            showPageSection("user-profile");
            $('#user-profile-info-block').attr("data-id", data.id);
            $('#user-profile-info-block').attr("data-type", data.type);
            $('#user-profile-info-block').attr("data-role", data.role);
            $("#user-profile-info-block").find('[data-name="instance-profile"]').empty();
            $("#user-profile-info-block").find('[data-name="instance-profile"]').append(profileInstanceCard(data, mainController));
            mainController.loadPosts_Ajax(data.id, 1, $("#user-profile-info-block").find('[data-name="instance-template-block"]'));
        });
    } else if (filter == 2) {
    	initNavList(filter);
        showPageSection("post");
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Recall_Search_Customer, {
            key: mainPage.attr("data-key"),
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not get data", "error",
                function() {});
        }, function(data) {
            loadingPageShow(false);
            $("#template-all-block").empty();
            mainController.loadPosts(data, $("#template-all-block"));
        });
    }else if (filter == 21) {
    	initNavList(filter);
        showPageSection("post");
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Recall_Search_Customer_Tag, {
            key: mainPage.attr("data-key"),
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not get data", "error",
                function() {});
        }, function(data) {
            loadingPageShow(false);
            $("#template-all-block").empty();
            mainController.loadPosts(data, $("#template-all-block"));
        });
    } 
    mainPage.on('click', '[data-action="nav_action"]', function() {
        var id = $(this).attr("data-id");
        var name = $(this).attr("data-name");
    });
    
    mainPage.on('click', '[data-name="go-home"]', function() {
        window.location.href=URLPrefix+"/recalls";
    });


    mainPage.on('click', '[data-action="instance-tab-action"]', function() {
    	$('#btn-rightbot-more').hide();
    	var dataname = $(this).attr("data-name");
        var parent = $(this).parent().parent();
        var current_active = parent.attr("data-current");
        parent.find('[data-name="' + current_active + '"]').removeClass("active");
        parent.find('[data-name="' + current_active + '"]').removeClass("show");
        parent.find('[data-name="' + dataname + '"]').addClass("active");
        parent.find('[data-name="' + dataname + '"]').addClass("show");
        parent.attr("data-current", dataname);
        var current_page = $("#nav_project_header-name").attr("current-name");

        if (dataname == "instance-recalls") {
            loadingPageShow(true);
            ajaxPostAction(URL_Get_Star_Recalls_Customer, {
                ids: '[]'
            }, function() {
                loadingPageShow(false);
                messageAlert("Can not get data", "error",
                    function() {});
            }, function(data) {
                loadingPageShow(false);
                $("#template-tab-block").find('[data-name="instance-template-block"]').empty();
                mainController.loadPosts(data, $("#template-tab-block").find('[data-name="instance-template-block"]'));
            });
        }
    });
    
   
    $('#loading-more-instances').on('click', function() {
        if (filter != 0) {
            var alls = [];
            mainPage.find('.instance-card:visible').map(function(index, instance) {
                alls.push($(instance).attr("data-id"));
            });
                $('#loading-more-instances').html('<i class="fa fa-refresh fa-spin fa-3x fa-fw" style="color:white;font-size:28px;position:relative;left:-10px;"></i>');
                var url = "";
                var parms = {};
                if (filter == 2) {
                    url = URL_Get_Recall_Search_Customer;
                    parms = {
                        ids: JSON.stringify(alls),
                        key: $("#main_body").attr("data-key")
                    }
                } if (filter == 21) {
                    url = URL_Get_Recall_Search_Customer_Tag;
                    parms = {
                        ids: JSON.stringify(alls),
                        key: $("#main_body").attr("data-key")
                    }
                }else if (filter == 1) {
                	var url=URL_Get_Recall_Posts_Customer;
                    parms = {
                        ids: JSON.stringify(alls)
                    };
                } else if (filter == 3) {
                	var url=URL_Get_Star_Recalls_Customer;
                    parms = {
                        ids: JSON.stringify(alls)
                    };
                }
                ajaxPostAction(url, parms, function() {
                    messageAlert("Can not get data，请刷新页面", "clear", function() {});
                    $('#loading-more-instances').html('<i class="fa fa-angle-double-down" style="color:white;font-size:28px;"></i>');
                }, function(data) {
                    $('#loading-more-instances').html('<i class="fa fa-angle-double-down" style="color:white;font-size:28px;"></i>');
                    if (filter == 1||filter == 2||filter == 21) {
                        mainController.loadPosts(data, $("#template-tab-block").find('[data-name="instance-template-block"]'), false);
                    } if (filter == 3) {
                        mainController.loadPosts(data, $("#user-profile-info-block").find('[data-name="instance-template-block"]'));
                    } 
                });
            
        }
    });
});


var showPageSection = function(key) {
    if (key == "post") {
        $('#user-profile-info-block').hide();
        $("#user-profile-info-block").find('[data-name="instance-template-block"]').empty();
        $("#template-all-block").show();
        $("#template-all-block").empty();
    } else if (key == "user-profile") {
       
        $("#template-all-block").hide();
        $("#template-all-block").empty();
        $('#user-profile-info-block').show();
        $("#user-profile-info-block").find('[data-name="instance-template-block"]').empty();
    }
}

var getTopUserLane = function(mainController) {
    var refreshFlag = true;
    ajaxPostAction(URL_Get_Current_User_Instance, {}, function() {
        messageAlert("Can not get data", "error",
            function() {});
    }, function(data) {
        $("#user-top-lane").empty();
        var uinstance = '<li class="nav-item dropdown" style="cursor:pointer;">\n' +
            '   <a class="nav-link dropdown-toggle" data-toggle="dropdown" aria-expanded="false">\n' +
            '      <img style="width:30px; height:30px;" src="' + data.user.image + '" class="img-circle user-image">\n' +
            '      <span class="hidden-xs"></span>\n' +
            '   </a>\n' +
            '   <ul class="dropdown-menu dropdown-menu-right" style="width:280px;padding-top:0px;">\n' +
            '     <li class="user-header" style="text-align:center; padding-top:10px; background-color:#3c8dbc;">\n' +
            '       <img src="' + data.user.image + '" class="img-circle" style="width:100px; height:100px;" alt="User Image">\n' +
            '       <p style="font-size:24px; color:white;">' + data.user.name + '</p>\n' +
            '     </li>\n' +
            '     <li class="user-footer" style="padding:10px;">\n' +
            '       <div class="pull-left" style="margin-bottom:10px; display:inline;">\n' +
            '          <a href="javascript:void(0);" class="btn btn-default btn-flat" data-name="go-user-home">Your Profile</a>\n' +
            '       </div>\n' +
            '       <div class="pull-right" style="display:inline;">\n' +
            '          <a href="javascript:void(0);" class="btn btn-default btn-flat" data-name="sign-out">Logout</a>\n' +
            '       </div>\n' +
            '    </li>\n' +
            '   </ul>\n' +
            '</li>';
        $("#user-top-lane").append(uinstance);
        $("#user-top-lane").find('[data-name="sign-out"]').on('click', function(e) {
            callConfirm("Logout", "Are your sure to log out？", function() {
                ajaxPostAction(URL_Logout, {}, function(result) {
                    messageAlert("Error", "clear", function() {});
                }, function(result) {
                    window.location.href = URLDOMAIN;
                });
            }, function() {});
        });
        
        $("#user-top-lane").find('[data-name="go-user-home"]').on('click', function(e) {
        	   window.location.href = URLPrefix+"/user";   
        });
        
        $("#user-top-lane").find(".dropdown").on("show.bs.dropdown", function(event) {
            refreshFlag = false;
        });
        $("#user-top-lane").find(".dropdown").on("hidden.bs.dropdown", function(event) {
            refreshFlag = true;
        });
    });
}

var initNavList = function(option, name) {
    var target = $("#nav_project_header_name");
    target.empty();
    if (option == 1) {
        target.html('<a href="#" class="nav-link my_project_header_name" style="color:white;">Recalls</a>');
        $("#nav_project_header_name").attr("current-name", "recommend-post");

    } else if (option ==3) {
        target.html('<a href="#" class="my_project_header_name nav-link" style="color:white;">'+name+'</a>');
        $("#nav_project_header_name").attr("current-name", "user-instance");
    } else if (option == 2) {
        target.html('<a href="#" class="my_project_header_name nav-link" style="color:white;">Search Recalls</a>');
        $("#nav_project_header_name").attr("current-name", "post-search");

    } else if (option == 21) {
        target.html('<a href="#" class="my_project_header_name nav-link" style="color:white;">Search Recalls</a>');
        $("#nav_project_header_name").attr("current-name", "post-search");

    }

}

var instancePostCard_None = function() {
    var card = $(
        '<div class="card my-cus-card">\n' +
        '  <div class="card-body" style="padding:0px;">\n' +
        '    <div class="row" style="max-height:40px;margin-right:0px;margin-left:0px;">\n' +
        '      <div class="col-md-12" style="padding-left:0px;padding-right:0px;cursor:pointer;"><input  data-action="add_card_post" class="form-control input-lg" style="border-top: none;border-left: none;border-right: none;cursor:pointer;"  readonly placeholder="No Recall Has Been Collected!" />\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>\n' +
        ' </div>');
    return card;
}

var instancePostCard_Create = function() {
    var card = $(
        '<div class="card my-cus-card">\n' +
        '  <div class="card-body" style="padding:0px;">\n' +
        '    <div class="row" style="max-height:40px;margin-right:0px;margin-left:0px;">\n' +
        '      <div class="col-md-12" style="padding-left:0px;padding-right:0px;cursor:pointer;"><input  data-action="add_card_post" class="form-control input-lg" style="border-top: none;border-left: none;border-right: none;cursor:pointer;"  readonly placeholder="发布状态" />\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>\n' +
        ' </div>');
    return card;
}

var initLeftSideTreeMenu = function(option, mainController) {
    if (option == 1) {
        $("#main_body").find('[data-name="instance-post-menu"]').addClass("active");
    }
    if (option == 2) {
        $("#main_body").find('[data-name="instance-user-menu"]').addClass("active");
    }
    if (option == 3) {
        $("#main_body").find('[data-name="instance-group-menu"]').addClass("active");
    }
    if (option == 4) {
        $("#main_body").find('[data-name="instance-myself-menu"]').addClass("active");
    }
    $("#main_body").find('[data-action="sider-bar-action"]').on('click',
        function() {
            var type = $(this).attr("data-name");

            if (type == "instance-post-menu") {
                window.location.href = URL_Get_Customer_Page + "recalls";
            }
            if (type == "instance-user-menu") {
                window.location.href = URL_Get_Customer_Page + "user";
            }
        });
}

var CreateGroupInstanceController = function(callback) {
    var html = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" style="overflow:auto;">\n' +
        '	<div class="modal-dialog">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="main-content">\n' +
        '					<div class="modal-header">\n' +
        '						<h4 class="modal-title">新建专题</h4>\n' +
        '						<button type="button" class="close" data-dismiss="modal">&times</button>\n' +
        '					</div>\n' +
        '				<div class="background-img" >\n' +
        '					<img data-target="face_image" style="width:100%; max-height:300px;"  src=""/>\n' +
        '				</div>\n' +
        '				<div class="background-edit">\n' +
        '					<a class="pull-right" title="添加背景"><i class="fa fa-camera" style="font-size:20px; float:right;cursor:pointer;"></i>\n' +
        '                       <input style="position: relative;width: 100%;top: -18px;opacity: 0;cursor:pointer;" data-name="fileinput-image" type="file" accept="image/gif, image/jpeg, image/png"/>\n' +
        '                   </a>\n' +
        '				</div>\n' +
        '					<div style="margin: 10px;">\n' +
        '						<div class="form-group">\n' +
        '							<input class="form-control input-lg" data-name="instance-name" style="border-top: none;border-left: none;border-right: none;" placeholder="请输入昵称" />\n' +
        '							<div class="err-msg" data-target="name" style="color: rgba(255,0,0,0.8);font-size: 12px;font-weight: 400;margin-top:5px;padding-left: 15px;display: none;">\n' +
        '							</div>\n' +
        '						</div>\n' +
        '				  <div class="form-group" data-name="share-option">\n' +
        '				     <label>允许关注: </label>\n' +
        '					 <label class="pull-right switch">\n' +
        '					    <input data-type="allowFollower" type="checkbox" checked>\n' +
        '					    <div class="slider round"></div>\n' +
        '					 </label>\n' +
        '				  </div>\n' +
        '					<div class="form-group">\n' +
        '						<label style="margin-right:10px;">标签:</label><div data-name="tag-list" style="display:inline;"></div>' +
        '						<input data-name="tag-input" style="width:100%; margin-top:10px;" type="text" placeholder="按回车键输入标签">\n' +
        '					</div>\n' +
        '					<div class="form-group">\n' +
        '						<p><i class="fa fa-pencil-square"></i>简介</p>\n' +
        '						<textarea class="form-control input-lg" data-name="introduction"></textarea>\n' +
        '					</div>\n' +
        '					</div>\n' +
        '				</div>\n' +
        '				<div class="modal-footer">\n' +
        '					<a class="btn-submit" href="javascript:void(0);" style="font-weight: 600;">确认</a>\n' +
        '				</div>\n' +
        '			</div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>');


    var getData = function() {
        var tags = [];
        $.each(html.find('[data-name="tag"]'), function(index, tag) {
            tags.push($(tag).attr("data-value"));
        });
        return {
            name: html.find('[data-name="instance-name"]').val(),
            allowFollower: html.find('[data-type="allowFollower"]').is(':checked') ? 1 : 0,
            introduction: html.find('[data-name="introduction"]').val(),
            tags: JSON.stringify(tags),
            image: html.find('[data-target="face_image"]').attr('src')
        };
    };

    // 验证表单
    var validation = function(data) {
        if (data.name == "") {
            html.find('.err-msg[data-target="name"]').text('昵称不能为空');
            html.find('.err-msg[data-target="name"]').show();
            return false;
        }

        if (data.image == "") {
            messageAlert("请上传封面图片", "error",
                function() {});
            return false;
        }

        if (data.name.length > 50) {
            html.find('.err-msg[data-target="name"]').text('昵称不得超过50个字');
            html.find('.err-msg[data-target="name"]').show();
            return false;
        }
        if (data.tags == null || data.tags == "" || data.tags == "[]" || data.tags.length == 0) {
            messageAlert("标签不能为空", "error",
                function() {});
            return false;
        }
        return true;
    };

    // 初始化
    (function() {
        html.find('[data-name="fileinput-image"]').on(
            'change',
            function() {
                var input = this;
                if (input.files && input.files[0]) {
                    var FileSize = input.files[0].size / 1024 / 1024;
                    if (FileSize >= 1) {
                        messageAlert("图片的大小不能超过1MB", "clear", function() {});
                    } else {
                        var reader = new FileReader();
                        reader.onload = function(e) {
                            html.find('[data-target="face_image"]').prop('src', e.target.result);
                        };
                        reader.readAsDataURL(input.files[0]);
                    }

                }
            });
        html.find('[data-name="tag-input"]').keyup(function(event) {
            if (event.keyCode === 13) {
                var value = html.find('[data-name="tag-input"]').val();
                if (value == "") {
                    messageAlert("标签不能为空", "error", function() {});
                } else {
                    var flag = true,
                        number = 0;
                    $.each(html.find('[data-name="tag"]'), function(index, tag) {
                        if ($(tag).attr("data-value") == value) {
                            messageAlert("此标签已经存在", "error", function() {});
                            flag = false;
                        }
                        number++;
                    });
                    if (number >= 3) {
                        messageAlert("最多添加三个标签", "error", function() {});
                    }
                    if (flag && number < 3) {
                        var $tagContainer = html.find('[data-name="tag-list"]');
                        var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + value + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                            value + '</span><span  style="background: black;position: relative;left: -5px;font-size: 21px;top: 2px;color: white; cursor:pointer;"class="fa fa-times" data-action="remove_tag"></span></span>');
                        $tagContainer.append($tag);
                        html.find('[data-name="tag-input"]').val("");
                    }
                }
            }
        });

        html.on('click', '[data-action="remove_tag"]',
            function() {
                $(this).parent().remove();
            });
        html.find('.btn-submit').on(
            'click',
            function() {
                // 获取数据
                var data = getData();

                // 验证
                if (validation(data)) {
                    // 提交
                    ajaxPostAction(URL_Create_Group, data,
                        function() {
                            messageAlert("创建失败", "error",
                                function() {});
                        },
                        function(result) {
                            if (result === 1) {
                                messageAlert("已经存在此昵称", "error",
                                    function() {
                                        // html.modal('hide');
                                    });
                            } else {
                                messageAlert("创建成功", "done",
                                    function() {
                                        html.modal('hide');
                                        callback();

                                    });
                            }

                        });
                } else {}
            });
        html.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });

        html.modal('show');
    })();
};


var profileInstanceCard = function(instance, mainController) {
    var card = $('<div class="card card-primary card-outline">' +
        '              <div class="card-body box-profile">' +
        '                <div class="text-center"><img data-name="face-image" alt="profile" style="width:100%; min-height:300px;" src="' + instance.image + '"></div>\n' +
        '                <h3 class="profile-username text-center" data-name="instance-name">' + instance.name + '</h3>' +
        '                <p class="text-muted text-center" data-name="instance-intro">' + instance.introduction + '</p>' +
        '                <p class="text-muted" data-name="instance-tag"></p>' +
        '                <a href="javascript:void(0);" class="btn btn-primary btn-block" data-action="instance-action" data-name="instance-update-info-action" style="display:none;"><b>Settings</b></a>' +
        '              </div>' +
        '            </div>');

    card.find('[data-name="instance-tag"]').empty();
    var loadTags = function(list) {
        card.find('[data-name="instance-tag"]').empty();
        if (!Array.isArray(list)) {
            console.log(list, 'list is not array');
        } else if (instance.tags.length == 0)
            card.find('[data-name="instance-tag"]').append(
                '<span style="color: rgba(0,0,0,0.54); margin-left: 15px;">None</span>');
        else
            $.each(list, function(i, t) {
                card.find('[data-name="instance-tag"]').append(
                    '<span class="badge" data-name="tag" data-action="tag-search" data-value="'+t+'" style="cursor:pointer; color:white; margin-right: 5px; background-color: ' +
                    googleColorRandomPicker() + ';">' + t +
                    '</span>');
            });
    };
    loadTags(instance.tags);
	card.find('[data-name="instance-update-info-action"]').show();


//    card.on('click', '[data-action="go-instance"]', function() {
//        if (instance.type == 2) {
//            window.location.href = URLPrefix + "/user/" + instance.uid;
//        }
//    });
//    
//    card.on('click', '[data-action="tag-search"]', function() {
//    	var tag=$(this).attr("data-value");
//    	window.location.href=URLPrefix + "/search" +"?type=tag_user"+ "&q=" + tag;
//    });
    
    card.on('click', '[data-action="instance-action"]', function() {
        var option = $(this).attr("data-name");
        if (option == "instance-send-post") {
            CreatePostInstanceController(instance.id, instance.type, function() {
                mainController.loadPosts_Ajax(instance.id, instance.type, $("#user-profile-info-block").find('[data-name="instance-template-block"]'));
            });
        } else if (option == "instance-update-info-action") {
            var url = URL_Get_Customer_User_Instance;
            ajaxGetAction(url, {
            }, function() {
                messageAlert("Error", "error",
                    function() {});
            }, function(data) {
                var updateItemProfile = new UpdateInstanceBasicInfoController(data, function(post_data) {
                    card.find('[data-name="face-image"]').attr('src', post_data.image);
                    card.find('[data-name="instance-intro"]').html(post_data.introduction);
                    card.find('[data-name="instance-name"]').text(post_data.name);
                    // 加载专题兴趣
                    loadTags(JSON.parse(post_data.tags));
                });
            });
        } else if (option == "instance-following-action") {
            var isFollower = card.find('[data-name="instance-following-action"]').attr("data-value");
            ajaxGetAction(URL_Do_Following_Action, {
                id: instance.id,
                type: instance.type,
                value: isFollower
            }, function() {
                messageAlert("Error", "error",
                    function() {});
            }, function(data) {
                if (isFollower == 1) {
                    messageAlert("Error", "done", function() {
                        location.reload();
                    });
                } else {
                    messageAlert("Done", "done", function() {
                        location.reload();
                    });
                }

            });
        } 
    });

    return card;
}


var instanceCard = function(instance, delete_name, delete_type, delete_url) {
    var card = $(
        '<div class="col-xl-2 col-md-4  col-sm-6 instance-card" style="cursor: pointer;" data-id="' + instance.id + '">\n' +
        '<div class="card my-cus-card">\n' +
        '  <div class="card-body" style="padding:0px;" data-action="go-instance">\n' +
        '    <div class="row" style="min-height:110px;margin-right:0px;margin-left:0px;">\n' +
        '      <div class="col-md-12 imgzoom" style="overflow:hidden;padding:0px;"><img style="max-height:140px; height:140px;" src="' + instance.image + '" />\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>\n' +
        '  <div class="card-footer" data-name="foot-area" style="padding:0px; min-height:57px;">\n' +
        '    <div class="row" data-name="row-name" style="margin-left:0px;margin-right:0px;" >\n' +
        '      <div class="col-md-12" style="margin-top:5px;">\n' +
        '        <div class="description-name"  data-action="go-instance" style="font-weight:  bold;overflow:hidden; text-overflow:  ellipsis;word-wrap: break-word;white-space: nowrap;width: 50%;float: left;" title="' + instance.name + '">' + instance.name + '</div>\n' +
        '	     <span data-action="go-instance" style="margin-right: 2px;float: right;opacity:0.8; display:none;" data-name="following-name">' + instance.followers.length + '个关注</span>\n' +
        '        <span class="badge" style="color:white; margin-right: 2px;float: right; background-color:red; display:none; cursor:pointer;" data-name="delete-name"></span>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '    <div class="row" data-name="row-tag-name" style="margin-left:0px;margin-right: 10px;padding-bottom: 5px; ">\n' +
        '      <div class="col-md-12" style="margin-top:5px;overflow: hidden;font-weight: bold;overflow: hidden;text-overflow: ellipsis;word-wrap: break-word;white-space: nowrap;" data-name="instance-tags"></div>\n' +
        '    </div>\n' +
        ' </div>\n' +
        '</div></div>');
    if(delete_name!==undefined)
    {
    	card.find('[data-name="delete-name"]').show();
    	if(delete_type=="show_following_name")
    	{
    		if(instance.type==1)
        	{
        		card.find('[data-name="delete-name"]').html("个人");
        		card.find('[data-name="delete-name"]').css("background-color", "purple");

        	}
        	else
        	{
        		card.find('[data-name="delete-name"]').html("专题");
        		card.find('[data-name="delete-name"]').css("background-color", "rgb(29, 135, 228)");
        	}
    	}
    	else if(delete_type=="show_group_member"&&instance.role==1)
        {
        	card.find('[data-name="delete-name"]').html("创建者");
        }
    	else if(delete_type=="show_group_member"&&instance.role==2)
        {
        	card.find('[data-name="delete-name"]').html("成员");
        }
    	else if(delete_type=="delete_group_member"&&instance.role==1)
        {
        	card.find('[data-name="delete-name"]').html("创建者");
        }
    	else if(delete_type=="add_group_blacklist"&&instance.role==1)
        {
        	card.find('[data-name="delete-name"]').html("创建者");
        }
    	else if(delete_type=="add_group_blacklist"&&instance.role==2)
        {
        	card.find('[data-name="delete-name"]').html("成员");
        }
        else
        {
        	card.find('[data-name="delete-name"]').attr("title", delete_name);
        	if(delete_type=="delete_group_member"||delete_type=="unfollow_user"||delete_type=="delete_user_blacklist"||delete_type=="delete_group_blacklist")
        	{
        		card.find('[data-name="delete-name"]').html('<i class="fa fa-trash" aria-hidden="true"></i>');
        	}
        	else if(delete_type=="add_group_blacklist"||delete_type=="add_user_blacklist")
        	{
        		card.find('[data-name="delete-name"]').html('<i class="fa fa-user-times" aria-hidden="true"></i>');
        	}
        }
    	
    	if(instance.tags.length!=0)
        {
        	card.find('[data-name="delete-name"]').css("margin-top","5px");
        }
    	
    }
    else
    {
    	card.find('[data-name="following-name"]').show();
    }
    card.find('[data-name="instance-tags"]').empty();
    $.each(instance.tags, function(i, t) {
        card.find('[data-name="instance-tags"]').append(
            '<span class="badge" data-action="tag-search" data-value="'+t+'" style="cursor:pointer; color:white; margin-right: 5px; background-color: ' +
            googleColorRandomPicker() + ';">' + t +
            '</span>');
    });
    if(instance.tags.length==0)
    {
    	card.find('[data-name="foot-area"]').css("padding-top","12px");
    }

    card.on('click', '[data-action="go-instance"]', function() {
        if (instance.type == 1) {
            window.location.href = URLPrefix + "/user/" + instance.id;
        } else {
            window.location.href = URLPrefix + "/group/" + instance.id;

        }
    });
    
    card.on('click', '[data-action="tag-search"]', function() {
    	var tag=$(this).attr("data-value");
    	 if (instance.type == 1) {
    	    	window.location.href=URLPrefix + "/search" +"?type=tag_user"+ "&q=" + tag;
         } else {
         	window.location.href=URLPrefix + "/search" +"?type=tag_group"+ "&q=" + tag;

         }
    });
    
    
    
    card.on('click', '[data-name="delete-name"]', function() {
    	if(delete_type=="show_group_member")
        {
        	return true;
        }else if(delete_type=="show_following_name")
        {
        	return true;
        }
        else if(delete_type=="delete_group_member"&&instance.role==1)
        {
        	return true;
        }
    	else if(delete_type=="add_group_blacklist"&&instance.role==1)
        {
    		return true;
        }
    	else if(delete_type=="add_group_blacklist"&&instance.role==2)
        {
    		return true;
        }
        callConfirm(delete_name, '您确定要进行此操作？',
            function() {
        	    var parms;
        	    if(delete_type=="unfollow_user")
        	    {
        	    	parms={id:instance.id, type:instance.type};
        	    }else if(delete_type=="delete_group_member")
        	    {
        	    	parms={id:$('#group-profile-info-block').attr("data-id"), tuid:instance.id};
        	    }
        	    else if(delete_type=="delete_user_blacklist")
        	    {
        	    	parms={tuid:instance.id};
        	    }
        	    else if(delete_type=="delete_group_blacklist")
        	    {
        	    	parms={id:$('#group-profile-info-block').attr("data-id"), tuid:instance.id};
        	    }else if(delete_type=="add_user_blacklist")
        	    {
        	    	parms={tuid:instance.id};
        	    }
        	    else if(delete_type=="add_group_blacklist")
        	    {
        	    	parms={id:$('#group-profile-info-block').attr("data-id"), tuid:instance.id};
        	    }
                ajaxGetAction(delete_url,parms, function() {
                    messageAlert("操作失败", "error",
                        function() {});
                }, function(data) {
                    messageAlert("操作成功", "done",
                        function() {
                            card.remove();
                        });
                });
            },
            function() {});
    });
    
    return card;
}

var instanceCommentCard_Create = function(itemid, callback) {
    var card = $(
        '<div class="post clearfix col-12">\n' +
        '      <div class="row"><div class="col-12">\n' +
        '        <button type="submit" class="btn btn-danger" data-name="send-item-comment">发表评论</button>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '</div>');
    card.on('click', '[data-name="send-item-comment"]', function() {
        var sendComment = new CreateCommentController(itemid, 1, "", callback);
    });
    return card;
}

var MainBlockController = function() {
    var obj = this;
    
    var renderPost=function(data, data_index, instances, heights, flag, callback)
    {
    	var current_index=0;
    	if(data.length==data_index)
    	{
    		checkLoadingMoreButton();
    		return true;
    	}
    	else
    	{
    		if(heights[0]<=heights[1]&&heights[0]<=heights[2])
            {
            	current_index=0;	
            }else if(heights[1]<=heights[2]&&heights[1]<=heights[0])
            {
            	current_index=1;
            }
            else if(heights[2]<=heights[1]&&heights[2]<=heights[0])
            {
            	current_index=2;
            }
        	var card=post_Block(data[data_index], flag, function() {callback();});
    		instances[current_index].append(card);
    		if(card.attr("has_image")==1)
    		{
    			var img=card.find('[data-name="post-head-image"]');
    			var image = new Image();
    			image.src = $(img).attr("src");
    			image.onload = function() {
    				var height=card.outerHeight();
                	card.attr("data-height", height)
                	heights[current_index]+=height;
                instances[current_index].attr("data-height", heights[current_index]);
            		data_index+=1;
            		renderPost(data, data_index, instances, heights, flag, callback);
    			};
    			image.onerror = function() {
    				var height=card.outerHeight();
                	card.attr("data-height", height)
                	heights[current_index]+=height;
                    instances[current_index].attr("data-height", heights[current_index]);
            		data_index+=1;
            		renderPost(data, data_index, instances, heights, flag, callback);
    			}; 
    		}
    		else
    		{
            	var height=card.outerHeight();
            	if(height>520)
        		{
        			height=520;
        		}
        		heights[current_index]+=height;
                instances[current_index].attr("data-height", heights[current_index]);
        		data_index+=1;
        		renderPost(data, data_index, instances, heights, flag, callback)
    		}
    	}
    }
    
    this.loadPosts_Ajax = function(id, type, target) {
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Star_Recalls_Customer, {
            ids: "[]"
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not get data", "clear", function() {});
        }, function(data) {
            loadingPageShow(false);
            target.empty();
            target.append('<div class="col-lg-4" data-col-0 data-name="first-col" data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-1 data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-2 data-height="0"></div>');
            var instances=[target.find("[data-col-0]"), target.find("[data-col-1]"), target.find("[data-col-2]")];
            var heights=[parseInt(instances[0].attr("data-height")),parseInt(instances[1].attr("data-height")),parseInt(instances[2].attr("data-height"))]
            var data_index=0;
            renderPost(data, data_index, instances, heights, true, function(){
            	obj.loadPosts_Ajax(id, type, target);
            });

            if (data == null || data == undefined || data.length == 0) {
                target.find('[data-name="first-col"]').prepend(instancePostCard_None);
            }
        });
    }
    
    this.loadPostDrafts_Ajax = function(id, target) {
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Post_Drafts_Customer, {
            id: id
        }, function() {
            loadingPageShow(false);
            messageAlert("获取动态失败，请刷新页面", "clear", function() {});
        }, function(data) {
            loadingPageShow(false);
            target.empty();
            target.append('<div class="col-lg-4" data-col-0 data-name="first-col" data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-1 data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-2 data-height="0"></div>');
            var instances=[target.find("[data-col-0]"), target.find("[data-col-1]"), target.find("[data-col-2]")];
            var heights=[parseInt(instances[0].attr("data-height")),parseInt(instances[1].attr("data-height")),parseInt(instances[2].attr("data-height"))]
            var data_index=0;
            renderPost(data, data_index, instances, heights, true,  function(){
            	obj.loadPostDrafts_Ajax(id, target);
            });

            if (data == null || data == undefined || data.length == 0) {
                target.find('[data-name="first-col"]').prepend(instancePostCard_None);
            }
        });
    }

    this.loadPosts = function(data, target, flag) {
        var post_div = target.find('[data-name="first-col"]');
        if (post_div.length == 0) {
            target.append('<div class="col-lg-4" data-col-0 data-name="first-col" data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-1 data-height="0"></div>\n' +
                '<div class="col-lg-4" data-col-2 data-height="0"></div>');
        }
        var instances=[target.find("[data-col-0]"), target.find("[data-col-1]"), target.find("[data-col-2]")];
        var heights=[parseInt(instances[0].attr("data-height")),parseInt(instances[1].attr("data-height")),parseInt(instances[2].attr("data-height"))]
        var data_index=0;
        renderPost(data, data_index, instances, heights, flag, function(){});
        if ((data == null || data == undefined || data.length == 0) && flag == undefined) {
            target.find('[data-name="first-col"]').prepend(instancePostCard_None);
        }
    }

    this.loadUsers = function(data, target) {
        $.each(data, function(index, instance) {
            target.append(
                instanceCard(instance));
        });
        checkLoadingMoreButton();
    }

    this.loadGroups = function(data, target) {
        $.each(data, function(index, instance) {
            target.append(
                instanceCard(instance));
        });
        checkLoadingMoreButton();
    }
}

var checkLoadingMoreButton = function() {
    var alls = 0;
    $("#main_body").find('.instance-card:visible').map(function(index, instance) {
        alls++;
    });
    if ((alls%12>=10||alls % 12 == 0)&&alls!==0) {
    	$('#btn-rightbot-more').show();
    }
    else
    {
    	$('#btn-rightbot-more').hide();
    }
}


var updateFileInstance = function(title, type, list, callback) {
    var html = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false">\n' +
        '	<div class="modal-dialog">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="main-content">\n' +
        '					<div class="modal-header">\n' +
        '						<h4 class="modal-title">' + title + '</h4>\n' +
        '						<button type="button" class="close" data-dismiss="modal">×</button>\n' +
        '					</div>\n' +
        '					<div style="margin: 15px;">\n' +
        '					<input name="file_datas" type="file" multiple class="file-loading" accept="image">\n' +
        '					</div>\n' +
        '				</div>\n' +
        '			</div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>'
    );
    var isMinimize = 0;
    var url;
    var parentid = $('#main_body').attr("data-id");
    url = UPLOAD_PROJECT_FILE + "?type=" + type + "&parentid=" + parentid;
    html.find('input').fileinput({
        language: "zh",
        theme: "explorer",
        uploadUrl: url,
        minFileSize: 1,
        //        allowedFileTypes: list,
        allowedFileExtensions: list,
        layoutTemplates: {
            actions: '<div class="file-actions">\n' +
                '    <div class="file-footer-buttons">\n' +
                '        {delete}' +
                '    </div>\n' +
                '    {drag}\n' +
                '    <div class="file-upload-indicator" title="{indicatorTitle}">{indicator}</div>\n' +
                '    <div class="clearfix"></div>\n' +
                '</div>',
            actionDelete: '<button type="button" class="kv-file-remove {removeClass}" title="{removeTitle}"{dataUrl}{dataKey}>{removeIcon}</button>\n',
        }
    });

    // 上传完成后方法
    html.find('input').on('filebatchuploadcomplete', function(event, data, previewId, index) {
        if (data.response == 0) {
            messageAlert('错误！', 'clear', function() {});
        } else {
            messageAlert('上传成功！', 'done', function() {
                html.modal("hide");
                callback();
            });
        }
    });

    // 同步进度条
    var sycProgress = function() {
        var percentige = Math.floor(html.find('.kv-upload-progress .progress-bar').width() / html.find('.kv-upload-progress .progress').width() * 100);

        $('#progressbar-leftbot .progress-bar').css({
            width: percentige + '%'
        }).text(percentige + '%');

    };

    var timer = setInterval(sycProgress, 500);


    html.on('click', '.minimize', function() {
        var $modal = $(this).closest('.modal');
        // $modal.modal('hide');
        $modal.css('visibility', 'hidden');
        $('.modal-backdrop').css('visibility', 'hidden');

        $('#progressbar-leftbot').show(400);
    });

    $('#progressbar-leftbot .maximize').unbind('click').click(function() {
        $(this).closest('#progressbar-leftbot').hide(400);
        html.css('visibility', 'visible');
        $('.modal-backdrop').css('visibility', 'visible');
    });


    html.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
        $('#progressbar-leftbot .progress-bar').css({
            width: '0%'
        }).text('0%');
    });

    html.modal('show');
}



var UpdateInstanceBasicInfoController = function(data, callback) {
    var modal = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" id="modal-group-edit" style="overflow:auto;">\n' +
        '	<div class="modal-dialog" style="min-width:350px;">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="background-img" >\n' +
        '					<img data-target="face_image" style="width:100%; max-height:300px;" />\n' +
        '				</div>\n' +
        '				<div class="background-edit">\n' +
        '					<a class="pull-right" title="Change Image"><i class="fa fa-camera" style="font-size:20px; float:right;cursor:pointer;"></i>\n' +
        '                       <input style="position: relative;width: 100%;top: -18px;opacity: 0;cursor:pointer;" data-name="fileinput-image" type="file" accept="image/gif, image/jpeg, image/png"/>\n' +
        '                   </a>\n' +
        '				</div>\n' +
        '				<div class="main-content" style="margin-top:25px;">\n' +
        '					<div class="form-group">\n' +
        '						<p><i class="fa fa-user-o"></i>Name:</p>\n' +
        '						<input class="form-control input-lg" name="name" />\n' +
        '					</div>\n' +
        '					<div class="form-group">\n' +
        '						<label style="margin-right:10px;">Tag:</label><div data-name="tag-list" style="display:inline;"></div>' +
        '						<input data-name="tag-input" style="width:100%; margin-top:10px;" type="text" placeholder="Press Enter to Fill a Tag">\n' +
        '					</div>\n' +
        '					<div class="form-group">\n' +
        '						<p><i class="fa fa-pencil-square"></i>Introduction</p>\n' +
        '						<textarea class="form-control input-lg" name="introduction" ></textarea>\n' +
        '					</div>\n' +

        '				    <div class="form-group">\n' +
        '					<span class="close pull-right" data-action="submit" style="opacity:1;margin-left:50px;">Submit</span>\n' +
        '					<span class="close pull-right" data-dismiss="modal" style="opacity:1;">Cancel</span>\n' +
        '					<div class="clearfix"></div>\n' + '				</div>\n' +
        '				</div>\n' + '			</div>\n' + '		</div>\n' + '	</div>\n' +
        '</div>');

    var getData = function() {
        var info = {
            name: modal.find('[name="name"]').prop('value'),
            introduction: modal.find('[name="introduction"]').val()
        };
        var tags = [];
        $.each(modal.find('[data-name="tag"]'), function(index, tag) {
            tags.push($(tag).attr("data-value"));
        });
        info.tags = JSON.stringify(tags);
        info.image = modal.find('[data-target="face_image"]').attr('src');
        return info;
    };

    // 初始化
    (function() {
        // 加载slimscroll
    	if(data.type==1)
    	{
    		 modal.find('[data-name="user-gender-info"]').show();
    	}
        modal.find('[name="introduction"]').slimScroll({
            width: '100%',
            height: '80px'
        });

        modal.find('[data-target="face_image"]').prop('src', data.image);
        modal.find('[name="name"]').prop('value', data.name);
        modal.find('[name="introduction"]').text(data.introduction);

        // 更新背景图片
        modal.find('[data-name="fileinput-image"]').on(
            'change',
            function() {
                var input = this;
                if (input.files && input.files[0]) {
                    var FileSize = input.files[0].size / 1024 / 1024;
                    if (FileSize >= 1) {
                        messageAlert("Image Size can not exceed 1MB", "clear", function() {});
                    } else {
                        var reader = new FileReader();
                        reader.onload = function(e) {
                            modal.find('[data-target="face_image"]').prop('src', e.target.result);
                        };
                        reader.readAsDataURL(input.files[0]);
                    }

                }
            });

        modal.find('[data-name="tag-input"]').keyup(function(event) {
            if (event.keyCode === 13) {
                var value = modal.find('[data-name="tag-input"]').val();
                if (value == "") {
                    messageAlert("Tag should not be empty", "error", function() {});
                } else {
                    var flag = true,
                        number = 0;
                    $.each(modal.find('[data-name="tag"]'), function(index, tag) {
                        if ($(tag).attr("data-value") == value) {
                            messageAlert("The tag already exist", "error", function() {});
                            flag = false;
                        }
                        number++;
                    });
                    if (number >= 3) {
                        messageAlert("You at most add 3 tags for your personality", "error", function() {});
                    }
                    if (flag && number < 3) {
                        var $tagContainer = modal.find('[data-name="tag-list"]');
                        var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + value + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                            value + '<i class="fa fa-times" aria-hidden="true" style="margin-left: 10px;cursor: pointer;" data-action="remove_tag" title="Delete"></i></span>');
                        $tagContainer.append($tag);
                        modal.find('[data-name="tag-input"]').val("");
                    }
                }
            }
        });

        modal.on('click', '[data-action="remove_tag"]',
            function() {
                $(this).parent().remove();
            });
        var $tagContainer = modal.find('[data-name="tag-list"]');
        $.each(data.tags, function(index, tag) {
            var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + tag + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                tag + '<i class="fa fa-times" aria-hidden="true" style="margin-left: 10px;cursor: pointer;" data-action="remove_tag" title="Delete"></i></span>');
            $tagContainer.append($tag);
        });

        // submit
        modal.find('[data-action="submit"]').on('click', function() {
            var post_data = getData();
            post_data.id = data.id;
            post_data.type = data.type;
            ajaxPostAction(URL_Update_Instance_Basic_Info, post_data, function(result) {
                    messageAlert("Error", "clear", function() {});
                },
                function(result) {
                    messageAlert("Done", "done", function() {
                        callback(post_data);
                        modal.modal("hide");
                    });

                });
        });

        // 关闭模态框时，自动删除
        modal.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });

        modal.modal("show");
    })();

}