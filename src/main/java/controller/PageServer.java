package controller;
import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import SessionInfo.SessionMonitor;

@Controller
public class PageServer {

	@RequestMapping("/")
	public String welcome(Model model,  @CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token) {
	    try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				model.addAttribute("data_id", "recall");
				model.addAttribute("data_key", "");    
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");
				model.addAttribute("data_uid", uid);

			    return "customer";
			} else {
				model.addAttribute("data_id", "recall");
				model.addAttribute("data_key", ""); 
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "");
			    return "index";
			}
		} catch (Exception e) {
			return "error";
		}
	}
	
	@RequestMapping("/registration")
	public String registrationUser(Model model) throws ServletException, IOException{
		 try {
				model.addAttribute("head_path", "./");
			    return "registration";
			} catch (Exception e) {
				return "error";
			}
	}
	
	@RequestMapping("/user/{id}")
	public String view_user_profile(Model model, @CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @PathVariable String id) {
	    try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				model.addAttribute("data_id", id);
				model.addAttribute("data_type", "user");
				model.addAttribute("data_key", "");
				model.addAttribute("head_path", "../");
				model.addAttribute("data_uid", uid);
			    return "customer";
			} else {
				model.addAttribute("data_id", id);
				model.addAttribute("data_type", 1);
				model.addAttribute("data_key", "");
				model.addAttribute("head_path", "../");
			    return "index";
			}
		} catch (Exception e) {
			return "error";
		}
	}

	
	@RequestMapping("/user")
	public String customer_user(Model model, @CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				model.addAttribute("data_id", "user");
				model.addAttribute("data_key", "");    
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");
				model.addAttribute("data_uid", uid);

			    return "customer";
			} else {
				model.addAttribute("data_id", "recall");
				model.addAttribute("data_key", "");
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");

			    return "index";
			}

		} catch (Exception e) {
			return "error";
		}
	}
	
	
	@RequestMapping("/recalls")
	public String customer_post(Model model, @CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token) {
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				model.addAttribute("data_id", "recall");
				model.addAttribute("data_key", "");    
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");
				model.addAttribute("data_uid", uid);

			    return "customer";
			} else {
				model.addAttribute("data_id", "recall");
				model.addAttribute("data_key", "");
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");

			    return "index";
			}

		} catch (Exception e) {
			return "error";
		}
	}
	
	@RequestMapping("/search")
	public String customer_data_search(Model model,@CookieValue(value = "uid_recall", defaultValue = "none") String uid, @CookieValue(value = "token_recall", defaultValue = "none") String token, @RequestParam("type") String type, @RequestParam("q") String key) {
		String section="search_recall";
		if(type.equals("recall"))
		{
			section="search_recall";
		}
		else if(type.equals("tag_recall"))
		{
			section="search_recall_tag";
		}
		try {
			if (SessionMonitor.isUserInSession(uid, token)) {
				model.addAttribute("data_id", section);
				model.addAttribute("data_key", key);
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");
				model.addAttribute("data_uid", uid);
			    return "customer";
			} else {
				model.addAttribute("data_id", section);
				model.addAttribute("data_key", key);
				model.addAttribute("data_type", 0);
				model.addAttribute("head_path", "./");

			    return "index";
			}

		} catch (Exception e) {
			return "error";
		}
	}
}
