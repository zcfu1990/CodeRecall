const URL_SEND_POST = URLPrefix + "/action/add_post";
const URL_Update_POST = URLPrefix + "/action/update_post";
const URL_Update_POST_Content = URLPrefix + "/action/update_post_content";
const URL_LIKE_POST = URLPrefix + "/action/like_recall_action";
const URL_Star_POST = URLPrefix + "/action/do_star_recall_action";
const URL_GET_Single_Recall = URLPrefix + "/action/get_single_recall";
const URL_GET_SINGLE_POST_Public = URLPrefix + "/action/get_single_post_public";
const URL_POST_TOP_ACTION = URLPrefix + "/action/make_post_top_action";
const URL_Delete_Post = URLPrefix + "/action/delete_post";
const URL_SEND_COMMENT = URLPrefix + "/action/send_post_comment";
const URL_GET_ALL_COMMENTS = URLPrefix + "/action/get_post_comments";
const URL_Delete_Post_Comment = URLPrefix + "/action/delete_post_comment";
const URL_SEND_Share_POST = URLPrefix + "/action/add_share_post";

var objIsEmpty = function(obj) {
    return Object.keys(obj).length === 0 && obj.constructor === Object || obj == undefined;
};



function getBase64Image(img) {
    var canvas = document.createElement('canvas'),
        ctx = canvas.getContext('2d');
    canvas.height = img.height;
    canvas.width = img.width;
    ctx.drawImage(img, 0, 0);
    dataURL = canvas.toDataURL("image/png");
    canvas.remove();
    return dataURL;
}

var CreatePostInstanceController = function(refer_id, callback) {
    var post_id = "";
    var $modal = $(
        '<div class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" style="overflow:auto;">\n' +
        '   <div class="modal-dialog" style="min-width:80%;">\n' +
        '	   <div class="modal-content">\n' +
        '			<div class="modal-header">\n' +
        '			    <h4 class="modal-title">Say Some Thing</h4>\n' +
        '			    <button type="button" class="close" data-dismiss="modal">&times</button>\n' +
        '		    </div>\n' +
        '			<div class="modal-body">\n' +
        '		       <div style="margin: 10px;">\n' +
        '			      <div class="form-group">\n' +
        '				     <label style="margin-right:10px;">Title: </label>' +
        '				     <input class="form-control input-lg" data-name="title" style="border-top: none;border-left: none;border-right: none;" placeholder="Please input the title" />\n' +
        '				  </div>\n' +
        '			      <div class="form-group">\n' +
        '				     <label style="margin-right:10px;">Company: </label>' +
        '				     <input class="form-control input-lg" data-name="company" style="border-top: none;border-left: none;border-right: none;" placeholder="Please input the company name" />\n' +
        '				  </div>\n' +
        '			      <div class="form-group">\n' +
        '				     <label style="margin-right:10px;">Device: </label>' +
        '				     <input class="form-control input-lg" data-name="device" style="border-top: none;border-left: none;border-right: none;" placeholder="Please input device name" />\n' +
        '				  </div>\n' +
        '					<div class="form-group">\n' +
        '						<label style="margin-right:10px;">Tags:</label><div data-name="tag-list" style="display:inline;"></div>' +
        '						<input data-name="tag-input" style="width:100%; margin-top:10px;" type="text" placeholder="Press Enter After Each Tag">\n' +
        '					</div>\n' +
        '				   <div class="form-group" data-name="editor">\n' +
        '				   </div>\n' +
        '		       </div>\n' +
        '		   </div>\n' +
        '		   <div class="modal-footer">\n' +
        '             <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>\n' +
        '             <button type="button" class="btn btn-primary" data-action="submit" data-name="release">Submit</button>\n' +
        '		  </div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>');

    // 获取数据
    var getPostData = function() {
        var tags = [];
        $.each($modal.find('[data-name="tag"]'), function(index, tag) {
            tags.push($(tag).attr("data-value"));
        });
        var warpContent = $('<div>' + $modal.find('.editor').html() + '</div>');
        var cover_image = "";
        var need_convert_image = 0;
        if (warpContent.find('img').length > 0) {
            cover_image = $(warpContent.find('img')[0]).attr('src');
            var re = /data:image\/([a-zA-Z]*);base64,([^\"]*)/;
            if (!re.test(cover_image)) {
                need_convert_image = 1;
            }
        }

        var result = {
            id: "",
            refer_id: refer_id,
            tags: JSON.stringify(tags),
            title: $modal.find('[data-name="title"]').val(),
            company: $modal.find('[data-name="company"]').val(),
            device: $modal.find('[data-name="device"]').val(),
            content: $modal.find('.editor').html(),
            cover_image: cover_image,
            need_convert_image: need_convert_image
        };
        return result;
    };
    // 验证表单
    var validation = function(data) {
        var max_size = 5000000;
        if ((encodeURI(JSON.stringify(data)).split(/%..|./).length - 1) > max_size) {
            messageAlert("The content can not exceed 5MB", "error", function() {});
            return false;
        }
        if (data.title == "") {
            messageAlert("Please input the title", "error", function() {});
            return false;
        }
        if (data.company == "") {
            messageAlert("Please input the company name", "error", function() {});
            return false;
        }
        if (data.device == "") {
            messageAlert("Please input the device name", "error", function() {});
            return false;
        }
        if (data.title.length > 50) {
            messageAlert("The title can not exceed 50 characters", "error", function() {});
            return false;
        }
        if (data.content == "") {
            messageAlert("The content can not be empty", "error", function() {});
            return false;
        }
        if (data.tags == null || data.tags == "" || data.tags == "[]" || data.tags.length == 0) {
            messageAlert("Tag cannot be empty", "error", function() {});
            return false;
        }

        return true;
    };

    // 初始化
    (function() {

        $modal.find('[data-name="tag-input"]').keyup(function(event) {
            if (event.keyCode === 13) {
                var value = $modal.find('[data-name="tag-input"]').val();
                if (value == "") {
                    messageAlert("Tag cannot be empty", "error", function() {});
                } else {
                    var flag = true,
                        number = 0;
                    $.each($modal.find('[data-name="tag"]'), function(index, tag) {
                        if ($(tag).attr("data-value") == value) {
                            messageAlert("The tag already exists", "error", function() {});
                            flag = false;
                        }
                        number++;
                    });
                    if (number >= 4) {
                        messageAlert("You can at most add 4 tags", "error", function() {});
                    }
                    if (flag && number < 4) {
                        var $tagContainer = $modal.find('[data-name="tag-list"]');
                        var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + value + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                            value + '<i class="fa fa-times" aria-hidden="true" style="margin-left: 10px;cursor: pointer;" data-action="remove_tag" title="Delete"></i></span>');
                        $tagContainer.append($tag);
                        $modal.find('[data-name="tag-input"]').val("");
                    }
                }
            }
        });

        $modal.on('click', '[data-action="remove_tag"]',
            function() {
                $(this).parent().remove();
            });

        $modal.find('[data-name="editor"]').append(getEditor(refer_id));
        $modal.find('[data-action="submit"]').on('click', function() {
            // 获取数据
            var data = getPostData();
            // 验证
            if (validation(data)) {
                // 提交
            	data.status = 1;
                var url = "";
                if (post_id == "") {
                    url = URL_SEND_POST;
                } else {
                    data.id = post_id;
                    url = URL_Update_POST;
                }
                loadingPageShow(true);
                ajaxPostAction(url, data,
                    function() {
                        loadingPageShow(false);
                        if (data.status == 1) {
                            messageAlert("Error", "error", function() {});
                        } else {
                            messageAlert("Done!", "error", function() {});
                        }
                    },
                    function(result) {
                        loadingPageShow(false);
                        if (result == 2) {
                            messageAlert("The content exceed 5MB", "error", function() {});
                        } else if (result == 3) {
                        	messageAlert("You can at most save 10 instances！", "error", function() {});
                        } else {
                        	messageAlert("Done", "done",
                                    function() {
                                        $modal.modal('hide');
                                        callback();
                                    });
                        }
                    });
            }
        });
        $modal.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });
        $modal.modal('show');
    })();
};

var post_Block = function(data, isEditor, callbackPost) {
    var instance = this;
    var $post = $(
        '<div class="post shadow-lg instance-card" style="padding-left:3px; padding-right:3px; padding-top:2px; padding-bottom:0px;" data-name="post-instance" data-id="' + data.id + '">\n' +
        '	<div class="user-block" data-type="user-title" style="margin-bottom:10px;">\n' +
        '		<span class="username" style="margin-bottom:5px;margin-left:2px;">\n' +
        '			<a href="javascript: void(0);" class="float-right btn-tool" data-action="view-action" title="View Detail"><i class="fa fa-external-link"></i></a>\n' +
        '			<div class="dropdown float-right btn-tool" data-name="show-info-action" style="display:none;">\n' +
        '				<a class="grey-icon" href="javascript: void(0);" data-toggle="dropdown" title="Options"><i class="fa fa-cog"></i></a>\n' +
        '				<ul class="dropdown-menu" data-name="post-actions">\n' +
        '				</ul>\n' +
        '			</div>\n' +
        '	        <div data-type="title" style="margin-bottom:8px;width:90%;display:inline;">\n' +
        '		       <a class="profile-username" href="javascript: void(0);" data-action="view-action"></a>\n' +
        '	        </div>\n' +
        '		</span>\n' +
        '	</div>\n' +
        '   <span class="description" data-name="datetime" style="text-align:right; margin-right:10px; float:right;"></span><br/>\n' +
        '	<div data-type="content" data-name="content" data-action="view-content-action" style="cursor:pointer;margin-top:10px;margin-bottom:10px;">\n' +
        '	</div>\n' +
        '	<a data-type="url" data-name="recall_url" style="margin-top:10px; margin-bottom:4px;word-wrap: break-word;white-space: nowrap;max-width:100%;text-overflow: ellipsis;display: inline-block;overflow: hidden;" href="" target="_blank"></a>\n' +
        '	<div data-type="device_types" data-name="dtypes" style="margin-top:5px;"></div>\n' +
        '	<div data-type="tagContainer" data-name="tagOption" style="margin-top:5px;"></div>\n' +
        '	<div class="row divider"></div>\n' +
        '   <p style="margin-left:5px; margin-right:5px;margin-top:8px; padding-bottom:8px;" data-type="user-bottom">\n' +
        '     <a href="javascript:void(0);"  data-action="report" class="link-black text-sm"><i class="fa fa-flag mr-1"></i>Report(<span data-num></span>)</a>\n' +
        '     <span class="float-right">\n' +
        '       <a href="javascript:void(0);"  data-action="like" class="link-black text-sm" style="margin-right:10px;" title="Like"><i class="fa fa-thumbs-o-up mr-1"></i>(<span data-num></span>)</a>\n' +
        '       <a href="javascript:void(0);"  data-action="star" class="link-black text-sm" style="margin-right:10px;" title="Collect"><i class="fa fa-star-o mr-1"></i>(<span data-num></span>)</a>\n' +
        '       <a href="javascript: void(0);" class="link-black text-sm" data-action="showComments" title="Comment"><i class="fa fa-comments-o mr-1"></i>(<span data-num></span>)\n' +
        '       </a>\n' +
        '     </span>\n' +
        '   </p>\n' +
        '</div>'
    );

    var loadPost_Block_Info = function(data) {
        $post.attr('data-id', data.id);

        $post.find('[data-action="showComments"] [data-num]').text(data.total_comments);

        var $postOptions = $post.find('[data-name="post-actions"]').empty();
        if (data.hasAuthority == 1) {
            $post.find('[data-name="show-info-action"]').show();
           	$o = $('<li class="dropdown-item" data-action="edit-action"><a href="javascript:void(0);">Eidt</a></li>');
            $postOptions.append($o);
            $o = $('<li class="dropdown-item" data-action="delete-action"><a href="javascript:void(0);">Delete</a></li>');
            $postOptions.append($o);
        } else {
            $post.find('[data-name="show-info-action"]').hide();
        }

        $post.find('[data-name="recall_url"]').attr({
            'href': data.url
        }).text(data.url);

        $post.find('[data-name="datetime"]').text(formatDatetime(data.datetime));

        
        var $dtypeContainer = $post.find('[data-type="device_types"]');
        $dtypeContainer.find('[data-type="tag"]').remove();
        $.each(data.device_types, function(index, tag) {
            var $tag = $('<span class="badge" data-action="tag-post" data-value="' + tag + '" style="cursor:pointer;color:white; margin-right: 5px;margin-bottom:4px; background-color: ' +
                googleColorRandomPicker() + ';">' + tag +
                '</span>');
            $dtypeContainer.append($tag);
        });
        
        var $tagContainer = $post.find('[data-type="tagContainer"]');
        $tagContainer.find('[data-type="tag"]').remove();
        $.each(data.tags, function(index, tag) {
            var $tag = $('<span class="badge" data-action="tag-post" data-value="' + tag + '" style="cursor:pointer;color:white; margin-right: 5px; margin-bottom:4px; background-color: ' +
                googleColorRandomPicker() + ';">' + tag +
                '</span>');
            $tagContainer.append($tag);
        });

        if (data.isLike == 1) {
            $post.find('[data-action="like"] i').css('color', "red");
        } else if (data.isLike == 0) {
            $post.find('[data-action="like"] i').css('color', "#666");
        } else {
            console.log('error');
        }
        if (data.isStar == 1) {
            $post.find('[data-action="star"] i').css('color', "red");
        } else if (data.isStar == 0) {
            $post.find('[data-action="star"] i').css('color', "#666");
        } else {
            console.log('error');
        }
        $post.find('[data-action="like"]').attr('data-value', data.isLike);
        $post.find('[data-action="like"] [data-num]').text(data.total_likes);

        $post.find('[data-action="star"]').attr('data-value', data.isStar);
        $post.find('[data-action="star"] [data-num]').text(data.total_stars);

        $post.find('[data-type="title"] a').text(data.title);
        var warpContent = $('<div style="max-height:340px;">' + data.content + '</div>');
        if (data.cover_image != undefined && data.cover_image != "") {
            $post.attr("has_image", 1);
            $post.find('[data-name="content"]').append(
                '<div style="margin-left:0px; margin-right:0px; overflow: hidden;text-align:center;">\n' +
                '	<img src="' + data.cover_image + '"class="img-responsive" data-name="post-head-image" style="max-width:100%;">\n' +
                '</div>'
            );
        } else if (warpContent.find('img').length > 0) {
            $post.attr("has_image", 1);
            $post.find('[data-name="content"]').append(
                '<div style="margin-left:0px; margin-right:0px; overflow: hidden;text-align:center;">\n' +
                '	<img src="' + $(warpContent.find('img')[0]).attr('src') + '"class="img-responsive" data-name="post-head-image" style="max-width:100%;">\n' +
                '</div>'
            );
        } else {
            $post.find('[data-name="content"]').append(warpContent);
            $post.find('[data-name="content"]').css({
                'max-height': '350px',
                'overflow': 'hidden'
            });
            $post.attr("has_image", 0);
        }
    };

    var initialize_Post_Block = function(data) {
        loadPost_Block_Info(data);
        $post.on('click', '[data-action]', function() {
            var actionName = $(this).attr('data-action');
            if (actionName == "view-action") {
            	post_Modal(data, callbackPost);
            } else if (actionName == "edit-action") {
                postEditor(data, $post, function() {
                    callbackPost();
                });
                $post.modal("hide");
            } else if (actionName == "delete-action") {
                callConfirm("Delete", "Confirm to delete？", function() {
                    ajaxGetAction(URL_Delete_Post, {
                        id: data.id
                    }, function() {
                        messageAlert("Error!", "error", function() {});
                    }, function(response) {
                        $post.modal("hide");
                        messageAlert(" Done!", "done", function() {
                            callbackPost();
                        });

                    });
                }, function() {});

            } else if (actionName == "like") {
                if (data.hasLogin == 1) {
                    var like_value = (parseInt($post.find('[data-action="like"]').attr('data-value')) + 1) % 2;
                    ajaxGetAction(URL_LIKE_POST, {
                        id: data.id,
                        value: like_value
                    }, function() {
                        messageAlert("Error!", "error", function() {});
                    }, function(response) {
                        if (response == 1) {
                            if (like_value == 1) {
                                $post.find('[data-action="like"] i').css('color', "red");
                                $post.find('[data-action="like"]').attr('data-value', like_value);
                                var total = parseInt($post.find('[data-action="like"] [data-num]').text()) + 1;
                                $post.find('[data-action="like"] [data-num]').text(total);

                            } else {
                                $post.find('[data-action="like"]').attr('data-value', like_value);
                                var total = parseInt($post.find('[data-action="like"] [data-num]').text()) - 1;
                                $post.find('[data-action="like"] [data-num]').text(total);
                                $post.find('[data-action="like"] i').css('color', "#666");
                            }
                        } else {
                            console.log('error');
                        }

                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            } else if (actionName == "star") {
                if (data.hasLogin == 1) {
                    var star_value = (parseInt($post.find('[data-action="star"]').attr('data-value')) + 1) % 2;
                    ajaxGetAction(URL_Star_POST, {
                        id: data.id,
                        value: star_value
                    }, function() {
                        messageAlert("Error!", "error", function() {});
                    }, function(response) {
                        if (response == 1) {
                            if (star_value == 1) {
                                $post.find('[data-action="star"] i').css('color', "red");
                                $post.find('[data-action="star"]').attr('data-value', star_value);
                                var total = parseInt($post.find('[data-action="star"] [data-num]').text()) + 1;
                                $post.find('[data-action="star"] [data-num]').text(total);

                            } else {

                                $post.find('[data-action="star"]').attr('data-value', star_value);
                                var total = parseInt($post.find('[data-action="star"] [data-num]').text()) - 1;
                                $post.find('[data-action="star"] [data-num]').text(total);
                                $post.find('[data-action="star"] i').css('color', "#666");
                                var current_page = $("#nav_project_header-name").attr("current-name");
                                var dataname = $("#template-tab-block").find('[data-name="tab-parent"]').attr("data-current");
                                if ($("#template-tab-block").is(':visible') && dataname == "instance-stars" && current_page == "recommend-post") {
                                    $post.remove();
                                }
                            }
                        } else {
                            console.log('error');
                        }

                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            } else if (actionName == "showComments") {
                if (data.hasLogin == 1) {
                    loadPostComments(data.id, data.refer_id, function(number) {
                        $post.find('[data-action="showComments"] [data-num]').text(number);
                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            }  else if (actionName == "report") {

            } else if (actionName == "tag-post") {
                var tag = $(this).attr('data-value');
                window.location.href = URLPrefix + "/search" + "?type=tag_post" + "&q=" + tag;
            }
        });
    };
    initialize_Post_Block(data);
    return $post;
}

var post_Modal = function(post, callbackPost) {
    var $post = $(
        '<div class="modal fade" style="overflow-y: auto;">\n' +
        '	<div class="modal-dialog" style="min-width: 70vw;">\n' +
        '		<div class="modal-content">\n' +
        '	      <div class="user-block" data-type="user-title" style="margin-bottom:10px; padding:12px;">\n' +
        '		    <span class="username" style="margin-bottom:5px;margin-left:2px;">\n' +
        '			   <a href="javascript:void(0);" data-name="poster-info" data-action="viewUser" style="color:#6c757d;cursor:pointer;"></a>\n' +
        '			   <a href="javascript: void(0);" class="float-right btn-tool close" data-dismiss="modal" title="Close" style="margin-left:10px;"><i class="fa fa-close"></i></a>\n' +
        '			   <div class="dropdown float-right btn-tool" data-name="show-info-action" style="display:none;">\n' +
        '				  <a class="grey-icon" href="javascript: void(0);" data-toggle="dropdown" title="Options"><i class="fa fa-cog"></i></a>\n' +
        '				  <ul class="dropdown-menu" data-name="post-actions">\n' +
        '				  </ul>\n' +
        '			   </div>\n' +
        '	           <div data-type="title" style="margin-bottom:8px;width:90%;display:inline;">\n' +
        '		          <a class="profile-username" href="javascript: void(0);" data-action="view-action"></a>\n' +
        '	           </div>\n' +
        '		    </span>\n' +
        '		    <span class="description" data-name="datetime" style="text-align:right; margin-right:10px;"></span>\n' +
        '	      </div>\n' +
        '		  <div class="modal-body" style="padding-top:0px;">\n' +
        '	           <div data-type="content" data-name="content" data-action="view-action" style="cursor:pointer;margin-bottom:10px;">\n' +
        ' 	           </div>\n' +
        '	           <a data-type="url" data-name="recall_url" style="margin-top:10px; margin-bottom:4px;word-wrap: break-word;white-space: nowrap;max-width:100%;text-overflow: ellipsis;display: inline-block;overflow: hidden;" href="" target="_blank"></a>\n' +
        '	           <div data-type="device_types" data-name="dtypes" style="margin-top:5px;"></div>\n' +
        '	           <div data-type="tagContainer" data-name="tagOption" style="margin-top:5px;">\n' +
        '	           </div>\n' +
        '	           <div class="row divider"></div>\n' +
        '              <p style="margin-left:5px; margin-right:5px;margin-top:8px; padding-bottom:8px;" data-type="user-bottom">\n' +
        '                 <a href="javascript:void(0);"  data-action="report" class="link-black text-sm"><i class="fa fa-flag mr-1"></i>Report</a>\n' +
        '                 <span class="float-right">\n' +
        '                    <a href="javascript:void(0);"  data-action="like" class="link-black text-sm" style="margin-right:10px;"><i class="fa fa-thumbs-o-up mr-1"></i>Like(<span data-num></span>)</a>\n' +
        '                    <a href="javascript:void(0);"  data-action="star" class="link-black text-sm" style="margin-right:10px;"><i class="fa fa-star-o mr-1"></i>Collect(<span data-num></span>)</a>\n' +
        '                    <a href="javascript: void(0);" class="link-black text-sm" data-action="showComments"><i class="fa fa-comments-o mr-1"></i>Comment(<span data-num></span>)</a>\n' +
        '                 </span>\n' +
        '               </p>\n' +
        '			</div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>'
    );

    // 0. 加载回到顶端按钮
    var appendToTopBtn = function() {
        var button = $('<a class="btn btn-default" id="toTop"><i class="fa fa-angle-double-up" style="font-size:30px;"></i></a>');
        button.on('click', function() {
            $post.animate({
                scrollTop: 0
            }, "slow");
            //post.scrollTop(0);
        });
        $post.scroll(function() {

        });
        button.css({
            'position': 'fixed',
            'bottom': '20px',
            'right': '12%',
            'z-index': '99'
        });
        $post.append(button);
    };

    $post.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
    loadingPageShow(true);

    ajaxGetAction(URL_GET_Single_Recall, {
        id: post.id
    }, function() {
        loadingPageShow(false);
        messageAlert("Error!", "error", function() {});
    }, function(data) {
        loadingPageShow(false);

        $post.attr('data-id', data.id);
        $post.find('[data-action="showComments"] [data-num]').text(data.total_comments);

        var $postOptions = $post.find('[data-name="post-actions"]').empty();
        if (data.hasAuthority == 1) {
            $post.find('[data-name="show-info-action"]').show();
            $o = $('<li class="dropdown-item" data-action="edit-action"><a href="javascript:void(0);">Edit</a></li>');
            $postOptions.append($o);
            $o = $('<li class="dropdown-item" data-action="delete-action"><a href="javascript:void(0);">Delete</a></li>');
            $postOptions.append($o);
        } else {
            $post.find('[data-name="show-info-action"]').hide();
        }

        $post.find('[data-name="datetime"]').text(formatDatetime(data.datetime));

        $post.find('[data-name="recall_url"]').attr({
            'href': data.url
        }).text(data.url);
        
        var $dtypeContainer = $post.find('[data-type="device_types"]');
        $dtypeContainer.find('[data-type="tag"]').remove();
        $.each(data.device_types, function(index, tag) {
            var $tag = $('<span class="badge" data-action="tag-post" data-value="' + tag + '" style="cursor:pointer;color:white; margin-right: 5px;margin-bottom:4px; background-color: ' +
                googleColorRandomPicker() + ';">' + tag +
                '</span>');
            $dtypeContainer.append($tag);
        });
        
        var $tagContainer = $post.find('[data-type="tagContainer"]');
        $tagContainer.find('[data-type="tag"]').remove();
        $.each(data.tags, function(index, tag) {
            var $tag = $('<span class="badge" data-action="tag-post" data-value="' + tag + '" style="cursor:pointer; color:white; margin-right: 5px;margin-bottom:4px; background-color: ' +
                googleColorRandomPicker() + ';">' + tag +
                '</span>');
            $tagContainer.append($tag);
        });

        if (data.isLike == 1) {
            $post.find('[data-action="like"] i').css('color', "red");
        } else if (data.isLike == 0) {
            $post.find('[data-action="like"] i').css('color', "#666");
        } else {
            console.log('error');
        }

        if (data.isStar == 1) {
            $post.find('[data-action="star"] i').css('color', "red");
        } else if (data.isStar == 0) {
            $post.find('[data-action="star"] i').css('color', "#666");
        } else {
            console.log('error');
        }

        $post.find('[data-action="like"]').attr('data-value', data.isLike);
        $post.find('[data-action="like"] [data-num]').text(data.total_likes);
        $post.find('[data-action="star"]').attr('data-value', data.isStar);
        $post.find('[data-action="star"] [data-num]').text(data.total_stars);

        $post.find('[data-type="title"] a').text(data.title);
        $post.attr("data-share", 0);
        $post.find('[data-type="title"] a').text(data.title);
        if(data.cover_image!="")
        {
        	$post.find('[data-name="content"]').append(
                    '<div style="margin-left:0px; margin-right:0px; overflow: hidden;text-align:center;">\n' +
                    '	<img src="' + data.cover_image + '"class="img-responsive" data-name="post-head-image" style="max-width:100%;">\n' +
                    '</div>\n<div>' + data.content + '</div>'
                );
        }
        else
        {
            var warpContent = $('<div>' + data.content + '</div>');
            $post.find('[data-name="content"]').append(warpContent);
        }
        
        

        $post.on('click', '[data-action]', function() {
            var actionName = $(this).attr('data-action');
            if (actionName == "edit-action") {
                postEditor(data, $post, function() {
                    callbackPost();
                });
                $post.modal("hide");
            } else if (actionName == "delete-action") {
                callConfirm("Delete", "Confirm to delete？", function() {
                    ajaxGetAction(URL_Delete_Post, {
                        id: data.id
                    }, function() {
                        messageAlert("Error", "error", function() {});
                    }, function(response) {
                        $post.modal("hide");
                        messageAlert("Done!", "done", function() {
                            location.reload();
                        });

                    });
                }, function() {});

            }  else if (actionName == "like") {
                if (data.hasLogin == 1) {
                    var like_value = (parseInt($post.find('[data-action="like"]').attr('data-value')) + 1) % 2;
                    ajaxGetAction(URL_LIKE_POST, {
                        id: data.id,
                        value: like_value
                    }, function() {
                        messageAlert("Error!", "error", function() {});
                    }, function(response) {
                        if (response == 1) {
                            if (like_value == 1) {
                                $post.find('[data-action="like"] i').css('color', "red");
                                $post.find('[data-action="like"]').attr('data-value', like_value);
                                var total = parseInt($post.find('[data-action="like"] [data-num]').text()) + 1;
                                $post.find('[data-action="like"] [data-num]').text(total);

                            } else {
                                $post.find('[data-action="like"]').attr('data-value', like_value);
                                var total = parseInt($post.find('[data-action="like"] [data-num]').text()) - 1;
                                $post.find('[data-action="like"] [data-num]').text(total);
                                $post.find('[data-action="like"] i').css('color', "#666");
                            }
                        } else {
                            console.log('error');
                        }

                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            } else if (actionName == "star") {
                if (data.hasLogin == 1) {
                    var star_value = (parseInt($post.find('[data-action="star"]').attr('data-value')) + 1) % 2;
                    ajaxGetAction(URL_Star_POST, {
                        id: data.id,
                        value: star_value
                    }, function() {
                        messageAlert("Error!", "error", function() {});
                    }, function(response) {
                        if (response == 1) {
                            if (star_value == 1) {
                                $post.find('[data-action="star"] i').css('color', "red");
                                $post.find('[data-action="star"]').attr('data-value', star_value);
                                var total = parseInt($post.find('[data-action="star"] [data-num]').text()) + 1;
                                $post.find('[data-action="star"] [data-num]').text(total);

                            } else {
                                $post.find('[data-action="star"]').attr('data-value', star_value);
                                var total = parseInt($post.find('[data-action="star"] [data-num]').text()) - 1;
                                $post.find('[data-action="star"] [data-num]').text(total);
                                $post.find('[data-action="star"] i').css('color', "#666");
                            }
                        } else {
                            console.log('error');
                        }

                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            } else if (actionName == "showComments") {
                if (data.hasLogin == 1) {
                    loadPostComments(data.id, data.refer_id, function(number) {
                        $post.find('[data-action="showComments"] [data-num]').text(number);
                    });
                } else {
                    messageAlert("Please log in", "warning", function() {});
                }
            } else if (actionName == "report") {

            } else if (actionName == "tag-post") {
                var tag = $(this).attr('data-value');
                window.location.href = URLPrefix + "/search" + "?type=tag_post" + "&q=" + tag;
            }
        });
        appendToTopBtn();
        $post.modal("show");
    });
};

var postEditor = function(data, instance, callback) {
    var $modal = $(
        '<div class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" style="overflow:auto;">\n' +
        '   <div class="modal-dialog" style="min-width:80%;">\n' +
        '	   <div class="modal-content">\n' +
        '			<div class="modal-header">\n' +
        '			    <h4 class="modal-title">Eidt Information</h4>\n' +
        '			    <button type="button" class="close" data-dismiss="modal">&times</button>\n' +
        '		    </div>\n' +
        '			<div class="modal-body">\n' +
        '		       <div style="margin: 10px;">\n' +
        '			      <div class="form-group">\n' +
        '				     <label>Title:</label>\n' +
        '				     <input class="form-control input-lg" data-name="title" style="border-top: none;border-left: none;border-right: none;" placeholder="Please type the title" />\n' +
        '				  </div>\n' +
        '				  <div class="form-group">\n' +
        '				     <label>Company:</label>\n' +
        '				     <input class="form-control input-lg" data-name="company" style="border-top: none;border-left: none;border-right: none;" placeholder="Please input the company name" />\n' +
        '				  </div>\n' +
        '				  <div class="form-group">\n' +
        '				     <label>Device:</label>\n' +
        '				     <input class="form-control input-lg" data-name="device" style="border-top: none;border-left: none;border-right: none;" placeholder="Please input the device name" />\n' +
        '				  </div>\n' +
        '				  <div class="form-group" >\n' +
        '				     <label>Class Level:</label>\n' +
        '					 <label class="pull-right">\n' +
        '					    <select data-name="post-level" style="width:90px;"><option value="1">Class I</option>\n' +
        '                           <option value="2">Class II</option>\n' +
        '                           <option value="3">Class III</option>\n' +
        '					    </select>\n' +
        '					 </label>\n' +
        '				   </div>\n' +
        '					<div class="form-group">\n' +
        '						<label style="margin-right:10px;">标签:</label><div data-name="tag-list" style="display:inline;"></div>' +
        '						<input data-name="tag-input" style="width:100%; margin-top:10px;" type="text" placeholder="Press Enter  ">\n' +
        '					</div>\n' +
        '				   <div class="form-group" data-name="editor">\n' +
        '				   </div>\n' +
        '		       </div>\n' +
        '		   </div>\n' +
        '		   <div class="modal-footer">\n' +
        '             <button type="button" class="btn btn-primary" data-action="submit" data-name="release" style="display:none;">Submit</button>\n' +
        '             <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>\n' +
        '		  </div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>');
    // 获取数据
    var getPostData = function(data) {
        var tags = [];
        $.each($modal.find('[data-name="tag"]'), function(index, tag) {
            tags.push($(tag).attr("data-value"));
        });
        var warpContent = $('<div>' + $modal.find('.editor').html() + '</div>');
        var cover_image = "";
        var need_convert_image = 0;
        if (warpContent.find('img').length > 0) {
            cover_image = $(warpContent.find('img')[0]).attr('src');
            var re = /data:image\/([a-zA-Z]*);base64,([^\"]*)/;
            if (!re.test(cover_image)) {
                need_convert_image = 1;
            }
        }

        var result = {
            id: data.id,
            level: $modal.find('[data-name="post-level"]').val(),
            company: $modal.find('[data-name="company"]').val(),
            device: $modal.find('[data-name="device"]').val(),
            tags: JSON.stringify(tags),
            title: $modal.find('[data-name="title"]').val(),
            content: $modal.find('.editor').html(),
            cover_image: cover_image,
            need_convert_image: need_convert_image,
            status: data.status
        };
        return result;
    };
    // 验证表单
    var validation = function(data) {
        var max_size = 5000000;
        if ((encodeURI(JSON.stringify(data)).split(/%..|./).length - 1) > max_size) {
            messageAlert("The content can not exceed5MB, 请缩减动态内容或图片大小", "error", function() {});
            return false;
        }
        if (data.title == "") {
            messageAlert("Please input the title", "error", function() {});
            return false;
        }
        if (data.title.length > 50) {
            messageAlert("The title can not exceed 50 characters", "error", function() {});
            return false;
        }
        if (data.content == "") {
            messageAlert("The content can not be empty", "error", function() {});
            return false;
        }
        if (data.tags == null || data.tags == "" || data.tags == "[]" || data.tags.length == 0) {
            messageAlert("标签不能为空", "error", function() {});
            return false;
        }
        return true;
    };

    // 初始化
    (function() {
        $modal.find('[data-name="tag-input"]').keyup(function(event) {
            if (event.keyCode === 13) {
                var value = $modal.find('[data-name="tag-input"]').val();
                if (value == "") {
                    messageAlert("标签不能为空", "error", function() {});
                } else {
                    var flag = true,
                        number = 0;
                    $.each($modal.find('[data-name="tag"]'), function(index, tag) {
                        if ($(tag).attr("data-value") == value) {
                            messageAlert("This tag already exists.", "error", function() {});
                            flag = false;
                        }
                        number++;
                    });
                    if (number >= 4) {
                        messageAlert("You can at most add 4 tags", "error", function() {});
                    }
                    if (flag && number < 4) {
                        var $tagContainer = $modal.find('[data-name="tag-list"]');
                        var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + value + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                            value + '<i class="fa fa-times" aria-hidden="true" style="margin-left: 10px;cursor: pointer;" data-action="remove_tag" title="delete"></i></span>');
                        $tagContainer.append($tag);
                        $modal.find('[data-name="tag-input"]').val("");
                    }
                }
            }
        });

        $modal.on('click', '[data-action="remove_tag"]',
            function() {
                $(this).parent().remove();
            });
        var $tagContainer = $modal.find('[data-name="tag-list"]');
        $.each(data.tags, function(index, tag) {
            var $tag = $('<span style="margin-right:5px;"><span data-name="tag"  data-value="' + tag + '" class="badge" style="color:white; font-size:15px; margin-right: 5px; background-color:' + googleColorRandomPicker() + '">' +
                tag + '<i class="fa fa-times" aria-hidden="true" style="margin-left: 10px;cursor: pointer;" data-action="remove_tag" title="delete"></i></span>');
            $tagContainer.append($tag);
        });

        $modal.find('[data-name="editor"]').append(getEditor(data.refer_id));
        var warpContent = $('<div>' + data.content + '</div>');
        $modal.find('[data-name="editor-block-content"]').html(warpContent);
        $modal.find('[data-name="title"]').val(data.title);
        $modal.find('[data-name="device"]').val(data.device);
        $modal.find('[data-name="company"]').val(data.company);
        $modal.find('[data-name="post-level"]').val(data.level),
            $modal.find('[data-action="submit"]').on('click', function() {
                var post = getPostData(data);
                if (validation(post)) {
                    post.status = 1;
                    loadingPageShow(true);
                    ajaxPostAction(URL_Update_POST, post,
                        function() {
                            loadingPageShow(false);
                            messageAlert("操作失败", "error",
                                function() {});
                        },
                        function(result) {
                            loadingPageShow(false);
                            if (result == 3) {
                                messageAlert("The content exceed 5MB", "error", function() {});
                            } else {
                                messageAlert("Done", "done",
                                    function() {
                                        $modal.modal('hide');
                                        callback();
                                    });
                            }
                        });
                } else {}
            });

        $modal.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });
        $modal.modal('show');
    })();
};


var loadPostComments = function(postid, refer_id, callback) {
    var controller = $(
        '<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">\n' +
        '   <div class="modal-dialog" role="document" style="min-width:70vw;">\n' +
        '      <div class="modal-content">\n' +
        '         <div class="modal-header">\n' +
        '            <h5 class="modal-title" id="exampleModalLabel">All Comments</h5>\n' +
        '            <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
        '               <span aria-hidden="true">&times;</span>\n' +
        '            </button>\n' +
        '         </div>\n' +
        '         <div class="modal-body" data-name="comments-div" style="max-height:85vh; min-height:400px; overflow-y:auto;">\n' +
        '         </div>\n' +
        '         <div data-name="showMoreComments" style="display:none;">\n' +
        '            <a class="btn btn-primary btn-lg switch-rightbot btn-rightbot" title="load more" data-action="showMoreComments"><i class="fa fa-angle-double-down"style="color: white; font-size: 28px;"></i></a>\n' +
        '         </div>\n' +
        '      </div>\n' +
        '   </div>\n' +
        '</div>');
    controller.modal("show");
    loadingPageShow(true);
    ajaxPostAction(URL_GET_ALL_COMMENTS, {
        pid: postid,
        ids: '[]'
    }, function() {
        loadingPageShow(false);
        messageAlert("Error", "clear", function() {});
    }, function(data) {
        loadingPageShow(false);
        var target = controller.find('[data-name="comments-div"]');
        target.empty();
        if (data.canComment == 1) {
            target.append(instancePostCommentCard_Create(postid, function() {
                controller.modal("hide");
                loadPostComments(postid, refer_id, callback);
            }));
        }
        callback(data.comments.length)
        checkLoadingMoreCommentsButton($(controller));
        $.each(data.comments, function(index, comment) {
            target.append(Post_Comment_Block(comment, function() {
                controller.modal("hide");
                loadPostComments(postid, refer_id, callback);
            }));
        });
    });
    controller.on('click', '[data-action="showMoreComments"]', function() {
        var ids = [];
        controller.find('.post-comment-block:visible').map(function(index, instance) {
            ids.push($(instance).attr("data-id"));
        });
        ajaxPostAction(URL_GET_ALL_COMMENTS, {
            pid: postid,
            ids: JSON.stringify(ids)
        }, function() {
            loadingPageShow(false);
            messageAlert("Error", "clear", function() {});
        }, function(data) {
            loadingPageShow(false);
            var target = controller.find('[data-name="comments-div"]');
            checkLoadingMoreCommentsButton($(controller));
            $.each(data.comments, function(index, comment) {
                target.append(Post_Comment_Block(comment, function() {
                    controller.modal("hide");
                    loadPostComments(postid, refer_id, callback);
                }));
            });
        });
    });
    controller.on('hidden.bs.modal', function() {
        $("body").css("padding-right", "0px");
        $(this).remove();
    });
}


var checkLoadingMoreCommentsButton = function(target) {
    var alls = 0;
    target.find('.post-comment-block:visible').map(function(index, instance) {
        alls++;
    });
    if (alls % 24 == 0 && alls !== 0) {
        target.find('[data-name="showMoreComments"]').show();
    } else {
        target.find('[data-name="showMoreComments"]').hide();
    }
}


var instancePostCommentCard_Create = function(postid, callback) {
    var card = $(
        '<div class="post clearfix col-12">\n' +
        '      <div class="row"><div class="col-12">\n' +
        '        <button type="submit" class="btn btn-danger" data-name="send-item-comment">Make Comment</button>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '</div>');
    card.on('click', '[data-name="send-item-comment"]', function() {
        var sendComment = new CreatePostCommentController(postid, 1, "", callback);
    });
    return card;
}

var Post_Comment_Block = function(comment, callback) {
    var card = $(
        '<div class="post clearfix col-12 post-comment-block" data-id="' + comment.id + '">\n' +
        '    <div class="user-block">\n' +
        '      <img class="img-circle img-bordered-sm" style="cursor:pointer;" data-name="comment-from-uimage" src="" alt="" data-action="go-uknow" data-id="' + comment.from_uid + '">\n' +
        '      <span class="username">\n' +
        '        <a href="javascript:void(0);" data-name="comment-from-uname" data-action="go-uknow" data-id="' + comment.from_uid + '"></a>\n' +
        '        <a href="javascript:void(0);" data-name="comment-from-and"></a>\n' +
        '        <a href="javascript:void(0);" data-name="comment-to-uname" data-action="go-uknow"></a>\n' +
        '        <a href="javascript:void(0);" class="float-right btn-tool" data-name="remove-comment" style="display:none;" title="delete"><i class="fa fa-times"></i></a>\n' +
        '        <a href="javascript:void(0);" class="float-right btn-tool" data-name="comment-action" style="display:none;" title="reply"><i class="fa fa-commenting-o" aria-hidden="true"></i></a>\n' +
        '        <a href="javascript:void(0);" class="float-right btn-tool" data-name="report-action" style="display:none;" title="report"><i class="fa fa-frown-o"></i></a>\n' +
        '      </span>\n' +
        '      <span class="description" data-name="comment-datetime"></span>\n' +
        '    </div>\n' +
        '    <div data-name="comment-content" class="comment-content" style="margin-bottom:10px;margin-left:48px;"></div>\n' +
        '</div>');
    if (comment.isOwner == 1) {
        card.find('[data-name="comment-action"]').hide();
        card.find('[data-name="report-action"]').hide();
        card.find('[data-name="remove-comment"]').show();
    } else if (comment.canReply == 0) {
        card.find('[data-name="comment-action"]').hide();
        card.find('[data-name="report-action"]').hide();
        card.find('[data-name="remove-comment"]').hide();
    } else if (comment.canReply == 1) {
        card.find('[data-name="comment-action"]').show();
        card.find('[data-name="report-action"]').show();
    }
    if (comment.type == 1) {
        card.find('[data-name="comment-from-uimage"]').attr("src", comment.from_uimage);
        card.find('[data-name="comment-from-uname"]').text(comment.from_uname);
        card.find('[data-name="comment-datetime"]').text(formatDatetime(comment.datetime));
        card.find('[data-name="comment-content"]').html(comment.content);
    } else {
        card.find('[data-name="comment-from-uimage"]').attr("src", comment.from_uimage);
        card.find('[data-name="comment-from-uname"]').text(comment.from_uname);
        card.find('[data-name="comment-from-and"]').text("@");
        card.find('[data-name="comment-to-uname"]').attr("data-id", comment.to_uid);
        card.find('[data-name="comment-to-uname"]').text(comment.to_uname);
        card.find('[data-name="comment-datetime"]').append(formatDatetime(comment.datetime));
        card.find('[data-name="comment-content"]').html(comment.content);
    }
    card.on('click', '[data-action="go-uknow"]', function() {
        window.open(ImageURLPrefix + "CodeRecall/user/" + $(this).attr("data-id"), "_blank");
    });
    card.on('click', '[data-name="remove-comment"]', function() {
        var obj = $(this);
        callConfirm('Delete', 'Confirm to delete？',
            function() {
                ajaxGetAction(URL_Delete_Post_Comment, {
                    id: comment.id
                }, function() {
                    messageAlert("Error", "error",
                        function() {});
                }, function(data) {
                    messageAlert("Done", "done",
                        function() {
                            callback();
                        });
                });
            },
            function() {});
    });

    card.on('click', '[data-name="comment-action"]', function() {
        var commentBack = new CreatePostCommentController(comment.refer_id, 2, comment.from_uid, function() {
            callback();
        });
    });
    return card;
}

var CreatePostCommentController = function(postid, type, tuid, callback) {
    var html = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" >\n' +
        '	<div class="modal-dialog" style="min-width:60vw;">\n' +
        '		<div class="modal-content">\n' +
        '			<div class="modal-body">\n' +
        '				<div class="main-content">\n' +
        '					<div class="modal-header">\n' +
        '						<h4 class="modal-title" data-name="modal-title">Make Comment</h4>\n' +
        '						<button type="button" class="close" data-dismiss="modal">&times</button>\n' +
        '					</div>\n' +
        '					<div style="margin: 10px;">\n' +
        '					    <div class="form-group" data-name="comment_block">\n' +
        '					    </div>\n' +
        '					</div>\n' +
        '				</div>\n' +
        '				<div class="modal-footer">\n' +
        '                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>\n' +
        '                  <button type="button" class="btn btn-primary" data-action="submit">Confirm</button>\n' +
        '				</div>\n' +
        '			</div>\n' +
        '		</div>\n' +
        '	</div>\n' +
        '</div>');
    var getData = function() {
        return {
            type: type,
            to_uid: tuid,
            content: html.find('.editor').html(),
            refer_id: postid
        };
    };
    // 初始化
    (function() {
        html.find('[data-name="comment_block"]').append(getEditor_Min(postid));
        if (type == 1) {
            html.find('[data-name="modal-title"]').text("Please make your comment");
        } else {
            html.find('[data-name="modal-title"]').text("Please make your reply");
        }
        html.find('[data-action="submit"]').on(
            'click',
            function() {
                var data = getData();
                if (data.content == "") {
                    messageAlert("The content can not be empty", "error",
                        function() {});
                    return;
                }
                ajaxPostAction(URL_SEND_COMMENT, data,
                    function() {
                        messageAlert("Comment Failed!", "error",
                            function() {});
                    },
                    function(result) {
                        if (result == 1) {
                            messageAlert("Comment Done!", "done",
                                function() {
                                    html.modal('hide');
                                    callback();

                                });
                        } else if (result == 1) {
                            messageAlert("The content can not exceed 500KB", "error",
                                function() {});
                        }

                    });

            });
        html.on('hidden.bs.modal', function() {
            $("body").css("padding-right", "0px");
            $(this).remove();
        });

        html.modal('show');
    })();
};