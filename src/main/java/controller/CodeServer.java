package controller;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import SessionInfo.SessionMonitor;
import recall.Recall;
import recall.RecallComment;
import recall.User;
import tools.EmailSender;
import tools.Methods;


@RestController
public class CodeServer {

	@RequestMapping(value = "/action/login", method = RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestParam(value = "email") String email,@RequestParam(value = "password") String password) {
		try {
			password = Methods.genernateMD5(password);
			Document user = User.getUserByEmail(email, password);
			if (user != null) {
				String token = String.valueOf(System.currentTimeMillis());
				Document result = new Document();
				result.append("uid", user.getString("id"));
				result.append("token", token);
				result.append("path", "Recall");
				SessionMonitor.addUserToSession(user.getString("id"), token);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("0", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/logout", method = RequestMethod.POST)
	public ResponseEntity<Object> logout(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				SessionMonitor.removeUserFromSession(uid);
				return new ResponseEntity<>("1", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	
	@RequestMapping(value = "/action/get_current_user", method = RequestMethod.POST)
	public ResponseEntity<Object> getCurrentUserInstance(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				Document user=User.getUserByID(uid);
				HashMap result = new HashMap();
				result.put("user", user);
				result.put("notices", new ArrayList<String>());
				return new ResponseEntity<>(result, HttpStatus.OK);

			} else {
				return new ResponseEntity<>("0", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/get_following_users_customer", method = RequestMethod.POST)
	public ResponseEntity<Object> getFollowing_Users_Customer(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<Document> users=User.getUserFollowingList_Users(uid);
				return new ResponseEntity<>(users, HttpStatus.OK);

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "/action/get_recommend_recalls_customer", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecommend_Post_Customer(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "ids") String ids) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> posts = Recall.getCustomerRecommendRecalls(uid, ids_list);
				return new ResponseEntity<>(posts, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/action/get_recommend_users_public", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecommend_Users_Public(@RequestParam(value = "ids") String ids) {
		try {
			List<String> ids_list = Methods.convertStringtoList(ids);
			List<Document> users = User.getRecommend_Users_Public(ids_list);
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/action/get_public_user_instance", method = RequestMethod.GET)
	public ResponseEntity<Object> getPublic_User_Instance(@RequestParam(value = "id") String id) {
		try {
			Document user = User.getSinlgeUseProfile(id);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/action/get_recalls_public", method = RequestMethod.POST)
	public ResponseEntity<Object> getPublicRecalls(@RequestParam(value = "ids") String ids) {
		try {
			List<String> ids_list = Methods.convertStringtoList(ids);
			List<Document> posts = Recall.getPublicRecalls(ids_list);
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/get_recalls_customer", method = RequestMethod.POST)
	public ResponseEntity<Object> getCustomerRecalls(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "ids") String ids) {
		try {
			List<String> ids_list = Methods.convertStringtoList(ids);
			List<Document> posts = Recall.getPublicRecalls(ids_list);
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/action/get_recommend_users_customer", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecommend_Users_Customer(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "ids") String ids) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> users = User.getRecommend_Users_Customer(uid, ids_list);
				return new ResponseEntity<>(users, HttpStatus.OK);

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/action/get_instance_followers", method = RequestMethod.GET)
	public ResponseEntity<Object> getInstanceFollowers( @RequestParam(value = "id") String id) {
		try {
			
			List<Document> followers=User.getFollowersList(id);
			return new ResponseEntity<>(followers, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
		
		
	}
	
	
	@RequestMapping(value = "/action/get_my_followers", method = RequestMethod.POST)
	public ResponseEntity<Object> getMineFollowers(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token) {
		
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<Document> followers=User.getFollowersList(uid);
				return new ResponseEntity<>(followers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
		
		
	}
	
	
	
	@RequestMapping(value = "/action/get_star_recalls", method = RequestMethod.POST)
	public ResponseEntity<Object> getUserStarRecalls(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token,  @RequestParam(value = "ids") String ids_string) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> ids=Methods.convertStringtoList(ids_string);
				List<Document> result=Recall.getUserStarRecalls(uid, ids);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	
	
	@RequestMapping(value = "/action/do_instance_following_action", method = RequestMethod.GET)
	public ResponseEntity<Object> doInstanceFollowingAction(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String id, @RequestParam(value = "type") int type, @RequestParam(value = "value") int value) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if(type==1&&value==0)// add
				{
					if (User.addUserFollowing(uid, id)) {
						return new ResponseEntity<>("1", HttpStatus.OK);

					} else {
						return new ResponseEntity<>("0", HttpStatus.OK);

					}
				}
				else if(type==1&&value==1)// remove
				{
					if (User.deleteUserFollowing(uid, id)) {
						return new ResponseEntity<>("1", HttpStatus.OK);

					} else {
						return new ResponseEntity<>("0", HttpStatus.OK);

					}
				} 
				return new ResponseEntity<>("0", HttpStatus.OK);

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/undo_instance_following_action", method = RequestMethod.GET)
	public ResponseEntity<Object> undoInstanceFollowingAction(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String id) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if (User.deleteUserFollowing(uid, id)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}
			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/action/get_user_followings", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserFollowings(@RequestParam(value = "id") String id) {
		try {
			List<Document> user_followings = User.getUserFollowingList_Users(id);
			return new ResponseEntity<>(user_followings, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "/action/get_single_user_customer", method = RequestMethod.GET)
	public ResponseEntity<Object> getSingleUser_Customer(@CookieValue(value = "uid_recall", defaultValue = "none") String uid,
			@CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				Document profile = User.getSinlgeUseProfile(uid, uid);
				return new ResponseEntity<>(profile, HttpStatus.OK);

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/action/update_user_settings", method = RequestMethod.POST)
	public ResponseEntity<Object> updateUserSettings(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token,
			@RequestParam(value = "name") String name, @RequestParam(value = "image") String image,
			@RequestParam(value = "introduction") String introduction, @RequestParam(value = "tags") String tags) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> tags_list = Methods.convertStringtoList(tags);
				if (User.updateUserProfileInfo(uid, name, image, tags_list,introduction)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	

	@RequestMapping(value = "/action/get_single_recall", method = RequestMethod.GET)
	public ResponseEntity<Object> getSinglePost(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token,
			@RequestParam(value = "id") String id) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				Document post=Recall.getRecall(uid, id);
				return new ResponseEntity<>(post, HttpStatus.OK);

			} else {
				Document post=Recall.getRecall(id);
				return new ResponseEntity<>(post, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	

	@RequestMapping(value = "/action/add_post", method = RequestMethod.POST)
	public ResponseEntity<Object> sendPost(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "recall_id") String recall_id,
			@RequestParam(value = "title") String title, @RequestParam(value = "cover_image") String cover_image, @RequestParam(value = "need_convert_image") int need_convert_image,  @RequestParam(value = "content") String content,
			@RequestParam(value = "tags") String tags, @RequestParam(value = "dtypes") String dtypes,@RequestParam(value = "date") String date, @RequestParam(value = "company") String company,  @RequestParam(value = "device") String device, @RequestParam(value = "url") String url_link,  @RequestParam(value = "level") int level,  @RequestParam(value = "status") int status) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> tags_list = Methods.convertStringtoList(tags);
				List<String> device_types = Methods.convertStringtoList(dtypes);
				String pid=Methods.getUniqueID();
				if(content.getBytes().length>5000000)
				{
					return new ResponseEntity<>("2", HttpStatus.OK);
				}
				if(need_convert_image==1)
				{
					URL url = new URL(cover_image); 
					InputStream is = url.openStream(); 
					byte[] bytes = IOUtils.toByteArray(is); 
					cover_image="data:image/jpeg;base64,"+Base64.encodeBase64String(bytes);
				}

				if (Recall.addNewPost(pid, uid, recall_id, title,cover_image, content, tags_list, device_types, level, company, device, url_link, date, status)) {
					if(status==Recall.ON)
					{
						return new ResponseEntity<>("1", HttpStatus.OK);
					}
					else
					{
						return new ResponseEntity<>(new Document("id", pid), HttpStatus.OK);
					}

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
//	@RequestMapping(value = "/action/add_share_post", method = RequestMethod.POST)
//	public ResponseEntity<Object> sendSharePost(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "refer_id") String refer_id,
//			@RequestParam(value = "title") String title, @RequestParam(value = "sharepid") String sharepid, 
//			@RequestParam(value = "tags") String tags) {
//		try {
//			if (SessionMonitor.isUserInSession(uid, token)) {
//				List<String> tags_list = Methods.convertStringtoList(tags);
//				String pid=Methods.getUniqueID();
//				if (Post.addNewPost(pid, uid, refer_id,  title, "", "",  tags_list,1,"","", 1)) {
//					return new ResponseEntity<>("1", HttpStatus.OK);
//
//				} else {
//					return new ResponseEntity<>("0", HttpStatus.OK);
//
//				}
//
//			} else {
//				return new ResponseEntity<>("-1", HttpStatus.OK);
//			}
//
//		} catch (Exception e) {
//			return new ResponseEntity<>("0", HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/action/update_recall", method = RequestMethod.POST)
	public ResponseEntity<Object> updateRecall(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String pid,
			@RequestParam(value = "title") String title, @RequestParam(value = "cover_image") String cover_image, @RequestParam(value = "need_convert_image") int need_convert_image, @RequestParam(value = "content") String content,
			@RequestParam(value = "tags") String tags, @RequestParam(value = "company") String company,  @RequestParam(value = "device") String device, @RequestParam(value = "level") int level, @RequestParam(value = "status") int status) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> tags_list = Methods.convertStringtoList(tags);
				if(need_convert_image==1)
				{
					URL url = new URL(cover_image); 
					InputStream is = url.openStream(); 
					byte[] bytes = IOUtils.toByteArray(is); 
					cover_image="data:image/jpeg;base64,"+Base64.encodeBase64String(bytes);
				}
				if (Recall.updateRecall(uid, pid, title, cover_image, content,tags_list, level, company, device, status)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/delete_recall", method = RequestMethod.GET)
	public ResponseEntity<Object> deleteRecall(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String pid) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if (Recall.deleteRecall(pid)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/like_recall_action", method = RequestMethod.GET)
	public ResponseEntity<Object> likeRecallAction(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String pid,  @RequestParam(value = "value") int value) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if (Recall.doLikeAction(pid, uid, value)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/do_star_recall_action", method = RequestMethod.GET)
	public ResponseEntity<Object> doStarRecallAction(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String pid,  @RequestParam(value = "value") int value) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if (Recall.doStarAction(pid, uid, value)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/send_recall_comment", method = RequestMethod.POST)
	public ResponseEntity<Object> sendRecallComment(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token,@RequestParam(value = "refer_id") String refer_id,  @RequestParam(value = "to_uid") String to_uid, @RequestParam(value = "content") String content,@RequestParam(value = "type") int type) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if(content.getBytes().length>500000)
				{
					return new ResponseEntity<>("2", HttpStatus.OK);
				}
				if (RecallComment.addNewRecallComment(refer_id, uid, to_uid, content, type)) {
					return new ResponseEntity<>("1", HttpStatus.OK);
				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/delete_recall_comment", method = RequestMethod.GET)
	public ResponseEntity<Object> deleteRecallComment(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "id") String id) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				if (RecallComment.deleteRecallComment(id)) {
					return new ResponseEntity<>("1", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("0", HttpStatus.OK);

				}

			} else {
				return new ResponseEntity<>("-1", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	

	@RequestMapping(value = "/action/get_recall_comments", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecallComments(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "pid") String id,  @RequestParam(value = "ids") String ids_string) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<String> ids=Methods.convertStringtoList(ids_string);
				int canComment=1;
				List<Document> comments=RecallComment.getPostComments(uid, canComment, id, ids);
				Document result=new Document("canComment", canComment);
				result.append("comments", comments);
				return new ResponseEntity<>(result, HttpStatus.OK);

			} else {
				List<String> ids=Methods.convertStringtoList(ids_string);
				List<Document> comments=RecallComment.getRecallComments_Public(id, ids);
				Document result=new Document("canComment", 0);
				result.append("comments", comments);
				return new ResponseEntity<>(result, HttpStatus.OK);			
				}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "/action/get_user_search", method = RequestMethod.POST)
	public ResponseEntity<Object> getUserSearch(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "key") String q, @RequestParam(value = "ids") String ids) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {				
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> results=User.getUserSearch_Customer(uid, q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> results=User.getUserSearch_Public(q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/get_user_search_tag", method = RequestMethod.POST)
	public ResponseEntity<Object> getUserSearch_Tag(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "key") String q, @RequestParam(value = "ids") String ids) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {				
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> results=User.getUserSearch_Customer_Tag(uid, q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				List<String> ids_list = Methods.convertStringtoList(ids);
				List<Document> results=User.getUserSearch_Public_Tag(q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/action/get_recall_search", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecallSearch(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "key") String q, @RequestParam(value = "ids") String ids) {
		try {
			List<String> ids_list = Methods.convertStringtoList(ids);
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<Document> results=Recall.getRecallSearch_Customer(uid, q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				List<Document> results=Recall.getRecallSearch_Public(q, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/get_recall_search_tag", method = RequestMethod.POST)
	public ResponseEntity<Object> getRecallSearch_Tag(@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam(value = "key") String tag, @RequestParam(value = "ids") String ids) {
		try {
			List<String> ids_list = Methods.convertStringtoList(ids);
			if (SessionMonitor.isUserInSession(uid, token)) {
				List<Document> results=Recall.getRecallSearch_Customer_Tag(uid, tag, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				List<Document> results=Recall.getRecallSearch_Public_Tag(tag, ids_list);
				return new ResponseEntity<>(results, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/get_validation_code", method = RequestMethod.GET)
	public ResponseEntity<Object> getValidationCode(@RequestParam(value = "email") String email) {
		return new ResponseEntity<>(User.getValidationCode(email), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/action/forget_password", method = RequestMethod.GET)
	public ResponseEntity<Object> forgetPasswordAction(@RequestParam(value = "email") String email) {
		try
		{
	    long time=System.currentTimeMillis();	
	    Random rnd = new Random();
		int n = 100000 + rnd.nextInt(900000);
		String code = String.valueOf(n);
	    String info=email+"@@"+time+"@@"+code;
	    String emailCoder=Methods.encodeString(info);
	    String link="http://"+User.localIPAddress+"/User/forget_password?info="+emailCoder;
	    
	    EmailSender es = new EmailSender();
		es.sendEmailToForgetPassword_CN(email,code, link);
		return new ResponseEntity<>("1", HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>("0", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/action/active_password_email", method = RequestMethod.POST)
	public ResponseEntity<Object> activePasswordByEmail(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		if(User.validateUserPassword(password))
		{
			String new_password = Methods.genernateMD5(password);
			if(User.updateUserPasswordByEmail(email,new_password))
			{
				return new ResponseEntity<>("1", HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("0", HttpStatus.OK);
			}
		}
		else
		{
			return new ResponseEntity<>("2", HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value = "/action/registration", method = RequestMethod.POST)
	public ResponseEntity<Object> doRegisteration(@RequestParam(value = "name") String name, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		String uid = Methods.getUniqueID();
		int status=User.addNewUserProfile(uid, name, email, password, User.Member);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	
}
