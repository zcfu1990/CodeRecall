/**
 * 
 */
const URL_Get_Welcome_Page = URLPrefix + "/";
const URL_Get_Public_Search_Pre = URLPrefix + "/search";
const URL_Get_Recalls_Public = URLPrefix + "/action/get_recalls_public";
const URL_Get_Single_Recall_Public = URLPrefix + "/action/get_single_recall";
const URL_Get_Recall_Search_Public = URLPrefix + "/action/get_recall_search";
const URL_Get_Recall_Search_Public_Tag= URLPrefix + "/action/get_recall_search_tag";


var filter = 0;
$(document).ready(function() {
    var mainPage = $("#main_body");
    var search_type = "";
    if (mainPage.attr("data-id") == "recall") {
        filter = 1;
        search_type = "recall";
    } else if (mainPage.attr("data-id") == "search_recall") {
        filter = 2;
        search_type = "recall";
    }else if (mainPage.attr("data-id") == "search_recall_tag") {
        filter = 3;
        search_type = "recall";
    } 
    initSearchFuntion(URL_Get_Public_Search_Pre, search_type, mainPage.attr("data-key"));
    var mainController = new MainBlockController();
    mainPage.find('[data-name="sign-in"]').on('click',
        function() {
            new loginModal();
        });
    mainPage.find('[data-name="sign-up"]').on('click',
        function() {
            var win = window.open(URLPrefix + "/registration", '_blank');
            win.focus();
        });
    
    mainPage.find('[data-name="go_home"]').on('click',
            function() {
                window.location.href=URLDOMAIN;
            });
    
    if (filter == 1) {
        initNavList(filter);
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Recalls_Public, {
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not access database", "error",
                function() {});
        }, function(data) {
            loadingPageShow(false);
            $("#template-all-block").empty();
            mainController.loadPosts(data, $("#template-all-block"));
        });
    } else if (filter == 2) {
        initNavList(filter);
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Recall_Search_Public, {
            key: mainPage.attr("data-key"),
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not access database", "error",
                function() {});
        }, function(data) {
            loadingPageShow(false);
            $("#template-all-block").empty();
            mainController.loadPosts(data, $("#template-all-block"));
        });
    } else if (filter ==3) {
        initNavList(filter);
        loadingPageShow(true);
        ajaxPostAction(URL_Get_Recall_Search_Public_Tag, {
            key: mainPage.attr("data-key"),
            ids: '[]'
        }, function() {
            loadingPageShow(false);
            messageAlert("Can not access database", "error",
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

    $('#loading-more-instances').on('click',function() {
            if (filter != 0) {
                var alls = [];
                mainPage.find('.instance-card:visible').map(function(index, instance) {
                    alls.push($(instance).attr("data-id"));
                });
                loadingPageShow(true);
                    var url = "";
                    var parms={};
                    if (filter == 2) {
                        url = URL_Get_Recall_Search_Public;
                        parms={
                                ids: JSON.stringify(alls),
                                key: $("#main_body").attr("data-key")
                            }
                    } else  if (filter == 3) {
                        url = URL_Get_Recall_Search_Public_Tag;
                        parms={
                                ids: JSON.stringify(alls),
                                key: $("#main_body").attr("data-key")
                            }
                    }else if (filter == 1) {
                        url = URL_Get_Recalls_Public;
                        parms={
                            ids: JSON.stringify(alls)                        };
                    }
                    ajaxPostAction(url, parms, function() {
                    	loadingPageShow(false);
                        messageAlert("Can not access database，please refresh the page", "clear", function() {});                       
                    }, function(data) {
                    	loadingPageShow(false);
                    	if(data.length==0)
                    	{
                    		$('#show_more_div').hide();
                    	}
                        mainController.loadPosts(data, $("#template-all-block"), false);
                    });
                
            }
        });

});

var instancePostCard_None = function() {
    var card = $(
        '<div class="card my-cus-card">\n' +
        '  <div class="card-body" style="padding:0px;">\n' +
        '    <div class="row" style="max-height:40px;margin-right:0px;margin-left:0px;">\n' +
        '      <div class="col-md-12" style="padding-left:0px;padding-right:0px;cursor:pointer;"><input  data-action="add_card_post" class="form-control input-lg" style="border-top: none;border-left: none;border-right: none;cursor:pointer;"  readonly placeholder="No Recall Yet!" />\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>\n' +
        ' </div>');
    return card;
}

var initNavList = function(option, name) {
    var target = $("#nav_project_header-name");
    target.empty();
    if (option == 1) {
        target.html('<a href="#" class="nav-link  my_project_header_name" style="color:white;">Recalls</a>');
        $("#nav_project_header-name").attr("current-name", "recommend-post");
    } else if (option == 2) {
        target.html('<a href="#" class="my_project_header_name nav-link" style="color:white;">Recalls</a>');
        $("#nav_project_header-name").attr("current-name", "search-post");
    }

}


var MainBlockController = function() {
    var obj = this;
    var renderPost=function(data, data_index, instances, heights)
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
        	var card=post_Block(data[data_index], false, function() {});
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
            		renderPost(data, data_index, instances, heights);
    			};
    			image.onerror = function() {
    				var height=card.outerHeight();
                	card.attr("data-height", height)
                	heights[current_index]+=height;
                    instances[current_index].attr("data-height", heights[current_index]);
            		data_index+=1;
            		renderPost(data, data_index, instances, heights);
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
        		renderPost(data, data_index, instances, heights)
    		}
    	}
    }
    
    this.loadPosts = function(data, target, flag) {
    	var post_div=target.find('[data-name="first-col"]');
    	if(post_div.length==0)
    	{
            target.append('<div class="col-lg-4" data-col-0 data-name="first-col" data-height="0"></div>\n' +
                    '<div class="col-lg-4" data-col-1 data-height="0"></div>\n' +
                    '<div class="col-lg-4" data-col-2 data-height="0"></div>');
    	}
    	 var instances=[target.find("[data-col-0]"), target.find("[data-col-1]"), target.find("[data-col-2]")];
         var heights=[parseInt(instances[0].attr("data-height")),parseInt(instances[1].attr("data-height")),parseInt(instances[2].attr("data-height"))]
         var data_index=0;
         renderPost(data, data_index, instances, heights);
        if ((data == null || data == undefined || data.length == 0)&&flag==undefined) {
            target.find('[data-name="first-col"]').prepend(instancePostCard_None);
        }
        checkLoadingMoreButton();
    }
}

var checkLoadingMoreButton=function()
{
	var alls =0;
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

var loginModal = function() {
    var login = $(
        '<div class="modal fade" id="login_modal" role="dialog" style="border: none; background-color: transparent">\n' +
        '   <div class="modal-dialog">\n' +
        '      <div class="modal-content">\n' +
        '         <div class="modal-header" style="background: #66a0fd; color: white;">\n' +
        '            <h4 class="modal-title">Log In</h4>\n' +
        '            <button type="button" class="close" data-dismiss="modal">&times;</button>\n' +
        '         </div>\n' +
        '         <div class="modal-body">\n' +
        '            <form role="form" method="post">\n' +
        '               <div class="form-group">\n' +
        '                  <label for="username"><span class="fa fa-user"></span>Email</label>\n' +
        '                  <input type="text" class="form-control" id="email" placeholder="Your Email" name="email">\n' +
        '               </div>\n' +
        '               <div class="form-group">\n' +
        '                  <label for="password"><span class="fa fa-eye"></span>Password</label>\n' +
        '                  <input type="password" class="form-control" id="password" placeholder="Your Password" name="password">\n' +
        '               </div>\n' +
        '               <button type="button" class="btn btn-primary btn-block" data-action="login-action">Login</button>\n' +
        '               <p style="text-align:center;margin-top:10px;">\n' +
        '                  <a href="javascript:void(0);" data-action="show-forget-password">Forget Password?</a>\n' +
        '               </p>\n' +
        '            </form>\n' +
        '         </div>\n' +
        '      </div>\n' +
        '   </div>\n' +
        '</div>');

    // 加载slimscroll

    (function() {
        login
            .find('[data-action="login-action"]')
            .on(
                'click',
                function() {
                    var uemail = login.find("#email").val();
                    var password =login.find("#password").val(); 
                    if (uemail == "" || password == "") {
                        messageAlert("Please type your email and password!", "clear", function() {});
                    } else {
                        ajaxPostAction(URL_Login_Normal, {
                            email: uemail,
                            password: password
                        }, function(result) {
                            messageAlert("Your password or email is not correct!", "clear",
                                function() {});
                        }, function(result) {
                        	setCookie("uid_recall", result.uid, result.path);
                            setCookie("token_recall", result.token, result.path);
                            window.location.href = URLPrefix+"/recalls";

                        });
                    }
                });
        login.find('[data-action="show-forget-password"]').on('click', function() {
            var email = document.getElementById("uemail").value;
            var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
            if (!regMail.test(email)) {
                messageAlert("Please make sure your email format is correct!", "clear",
                    function() {});
                return false;
            } else {
                var obj = this;
                var url = ImageURLPrefix + "Recall/action/forget_password";
                var icon = '<i class="fa fa-spinner fa-pulse fa-3x fa-fw" style="font-size:25px;"></i>';
                $(this).html(icon);
                ajaxGetAction(url, {
                    email: email
                }, function(result) {
                    $(obj).html("Forget Password?");
                    messageAlert("Opeartion Error!", "clear",
                        function() {});
                }, function(result) {
                    $(obj).html("Forget Password?");
                    messageAlert("The link for resetting your password has been sent to your email! Please verify the link ASAP!", "done",
                        function() {});
                });
            }
        });

        login.find('.row-top').slimScroll({
            height: '20vh'
        });
        login.find('.row-bot').slimScroll({
            height: '40vh'
        });

        login.on('hidden.bs.modal', function() {
            $(this).remove();
        });
        login.modal('show');
    })();

}