package recall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import sql.MD;
import tools.EmailSender;
import tools.Methods;
public class User {
	
	private static final int GUEST = 2;
	public static String table="recall_user";		
	private static int showSize=24;
	public static String face_image="data:image/png;base64,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";
    public static int Member=0, ADMIN=2, OWNER=1;
	public static String localIPAddress="";
	public static Document getUserProfileObject(String uid, String name, String email, String password, int role) {
		String currentTime = Methods.getCurrentTime();
		Document temp = new Document();
		temp.append("id", uid);
		temp.append("name", name);
		temp.append("email", email);
		temp.append("password", password);
		temp.append("image", face_image);
		temp.append("tags", new ArrayList<>());
		temp.append("introduction", "");
		temp.append("followers", new ArrayList<>());
		temp.append("post_stars", new ArrayList<>());
		temp.append("user_followings", new ArrayList<>());
		temp.append("total_followers", 0);
		temp.append("reportinfo", new ArrayList<>());
		temp.append("datetime", currentTime);
		temp.append("role", role);
		temp.append("status", 1);
		return temp;
	}
	
	public static int addNewUserProfile(String uid, String name, String email, String password, int role)
	{
		
		try {
			
			if (User.isEmailExisted(email)) {
				return 2; // 3 phone ,4 //web
			}
			if (!User.validateUserPassword(password)) {
				return 5;
			}
			if (!User.validateUserName(name)) {
				return 6;
			}
			password=Methods.genernateMD5(password);
			Document profile = User.getUserProfileObject(uid, name, email, password, role);
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			uknowMongoDatabase.insertOne(profile);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static boolean updateUserProfileInfo(String id, String name, String image, List<String> tags, String introduction) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", id);
			Document info = new Document();
			info.append("name", name);
			info.append("image", image);
			info.append("tags", tags);
			info.append("introduction", introduction);
			Document update = new Document();
			update.append("$set", info);
			uknowMongoDatabase.updateOne(query, update);
			return true;
		} catch (Exception e) {

			return false;
		}
	}
	
	
	public static boolean updateUserRole(String id, int role) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", id);
			Document info = new Document();
			info.append("role", role);
			Document update = new Document();
			update.append("$set", info);
			uknowMongoDatabase.updateOne(query, update);
			return true;
		} catch (Exception e) {

			return false;
		}
	}
	
	public static List<Document> getUserFollowingList_Users(String id) {
		LinkedList<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			Document instance = User.getSinlgeUseProfile(id);
			List<String> user_followings = instance.get("user_followings", List.class);
			if (user_followings.size() > 0) {
				MongoCollection<Document> dbcollection = db.getCollection(User.table);
				Document query = new Document();
				query.append("id", new Document("$in", user_followings));
				MongoCursor<Document> cursor = dbcollection.find(query).iterator();
				while (cursor.hasNext()) {
					Document member = cursor.next();
					if (member != null) {
						result.add(member);
					}
				}
				cursor.close();
			}
			
			return result;
		} catch (Exception e) {
			return result;
		}
	}	
	
	
	public static List<Document> getFollowersList(String id) {
		LinkedList<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			Document instance = User.getSinlgeUseProfile(id);
			List<String> followers = instance.get("followers", List.class);
			if (followers.size() > 0) {
				MongoCollection<Document> dbcollection = db.getCollection(User.table);
				Document query = new Document();
				query.append("id", new Document("$in", followers));
				MongoCursor<Document> cursor = dbcollection.find(query).iterator();
				while (cursor.hasNext()) {
					Document member = cursor.next();
					if (member != null) {
						result.add(member);
					}
				}
				cursor.close();
			}
			return result;
		} catch (Exception e) {
			return result;
		}
	}
	
	
	public static boolean addUserFollowing(String uid, String targetid)
	{
		try (ClientSession clientSession = MD.mongoClient.startSession()) {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", uid);
			Document update = new Document();
			update.append("$addToSet", new Document("user_followings", targetid));
			
			
			Document query_1 = new Document();
			query_1.append("id", targetid);
			Document update_1 = new Document();
			update_1.append("$addToSet", new Document("followers", uid));
			update_1.append("$inc", new Document("total_followers", 1));

			clientSession.startTransaction();
			uknowMongoDatabase.updateOne(clientSession, query, update);
			uknowMongoDatabase.updateOne(clientSession,query_1, update_1);
			clientSession.commitTransaction();	
		    return true;
			
		} catch (Exception e) {
			return false;
		}	
	}
	
	

	
	public static boolean deleteUserFollowing(String uid, String targetid)
	{
		try (ClientSession clientSession = MD.mongoClient.startSession()) {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", uid);
			Document update = new Document();
			update.append("$pull", new Document("user_followings", targetid));
			
			
			Document query_1 = new Document();
			query_1.append("id", targetid);
			Document update_1 = new Document();
			update_1.append("$pull", new Document("followers", uid));
			update_1.append("$inc", new Document("total_followers", -1));

			clientSession.startTransaction();
			uknowMongoDatabase.updateOne(clientSession, query, update);
			uknowMongoDatabase.updateOne(clientSession,query_1, update_1);
			clientSession.commitTransaction();	
		    return true;
			
		} catch (Exception e) {
			return false;
		}	
	}
	
	
	public static Document getSinlgeUseProfile(String id) {
		// TODO Auto-generated method stub
		Document instance = null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", id);
			MongoCursor<Document> cursor = uknowMongoDatabase.find(query).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				cursor.close();
				break;
			}

			return instance;
		} catch (Exception e) {
			return instance;
		}
	}
	
	
	
	
	public static long checkExistUser(String id) {
		// TODO Auto-generated method stub
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", id);
			long number= uknowMongoDatabase.count(query);
			return number;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	public static long checkExistEmail(String email) {
		// TODO Auto-generated method stub
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("email", email);
			long number= uknowMongoDatabase.count(query);
			return number;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	public static Document getSinlgeUseProfile(String targetid, String uid) {
		// TODO Auto-generated method stub
		Document instance = null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", targetid);
			MongoCursor<Document> cursor = uknowMongoDatabase.find(query).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				if(uid.equals(targetid))
				{
					instance.append("role", User.OWNER);
				}
				else
				{
					instance.append("role", User.GUEST);
				}
				List<String> followers=instance.get("followers", List.class);
				if(followers!=null&&followers.size()>0)
				{
					if(followers.contains(uid))
					{
						instance.append("isFollower", 1);
					}
					else
					{
						instance.append("isFollower", 0);
					}
				}
				else
				{
					instance.append("isFollower", 0);
				}
				
				cursor.close();
				break;
			}

			return instance;
		} catch (Exception e) {
			return instance;
		}
	}

	public static List<Document> getRecommend_Users_Public(List<String> ids_list) {
		// TODO Auto-generated method stub
		List<Document> results=new ArrayList<>();
		Document instance = null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			Document query = new Document();
			if(ids_list!=null&&ids_list.size()>0)
			{
				query.append("id", new Document("$nin", ids_list));
			}
			MongoCursor<Document> cursor = uknowMongoDatabase.find(query).sort(new Document("total_followers", -1)).limit(User.showSize).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				if(instance!=null)
				{
					results.add(instance);
				}
			}
			cursor.close();
			return results;
		} catch (Exception e) {
			return results;
		}
	}
	
	public static List<Document> getRecommend_Users_Customer(String uid, List<String> ids_list) {
		// TODO Auto-generated method stub
		List<Document> results=new ArrayList<>();
		Document instance = null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			Document user=User.getSinlgeUseProfile(uid);
			List<String> followers=user.get("user_followings", List.class);
			List<String> tags=user.get("tags", List.class);
			MongoCollection<Document> uknowMongoDatabase = db.getCollection(User.table);
			if(followers!=null&&followers.size()>0)
			{
				for(String tuid:followers)
				{
					ids_list.add(tuid);
				}
			}
			Document query = new Document();
			ids_list.add(uid);
			if(ids_list!=null&&ids_list.size()>0)
			{
				query.append("id", new Document("$nin", ids_list));
			}
			if(tags!=null&&tags.size()>0)
			{
				ArrayList<Document> query_tags=new ArrayList<>();
				for(String tag:tags)
				{
					query_tags.add(new Document("tags",tag));
				}
				query.append("$or", query_tags);
			}
			MongoCursor<Document> cursor = uknowMongoDatabase.find(query).sort(new Document("total_followers", -1)).limit(User.showSize).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				if(instance!=null)
				{
					results.add(instance);
					ids_list.add(instance.getString("id"));
				}
			}
			if(results.size()<User.showSize)
			{
				Document query_new = new Document();			
				if(ids_list!=null&&ids_list.size()>0)
				{
					query_new.append("id", new Document("$nin", ids_list));
				}					
				cursor = uknowMongoDatabase.find(query_new).sort(new Document("total_followers", -1)).limit(User.showSize-results.size()).iterator();
				while (cursor.hasNext()) {
					instance = cursor.next();
					if(instance!=null)
					{
						results.add(instance);
					}
				}
			}
			cursor.close();
			return results;
		} catch (Exception e) {
			return results;
		}
	}
	
	public static List<Document> getUserSearch_Public(String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(User.table);
		Document query = new Document();
		List<Document> orinfo = new ArrayList<Document>();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		orinfo.add(new Document("name", java.util.regex.Pattern.compile(key, Pattern.CASE_INSENSITIVE)));
		orinfo.add(new Document("tags", key));
		query.append("$or", orinfo);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(User.showSize).iterator();
		// cursor.limit(Post.showSize);
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null) {
				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}
	
	
	
	public static List<Document> getUserSearch_Public_Tag(String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(User.table);
		Document query = new Document();
		List<Document> orinfo = new ArrayList<Document>();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		query.append("tags", key);

		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(User.showSize).iterator();
		// cursor.limit(Post.showSize);
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null) {
				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}

	public static List<Document> getUserSearch_Customer(String uid, String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(User.table);
		Document query = new Document();
		List<Document> orinfo = new ArrayList<Document>();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		orinfo.add(new Document("name", java.util.regex.Pattern.compile(key, Pattern.CASE_INSENSITIVE)));
		orinfo.add(new Document("tags", key));
		query.append("$or", orinfo);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(User.showSize).iterator();
		// cursor.limit(Post.showSize);
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null) {
				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}
	
	
	public static List<Document> getUserSearch_Customer_Tag(String uid, String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(User.table);
		Document query = new Document();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		query.append("tags", key);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(User.showSize).iterator();
		// cursor.limit(Post.showSize);
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null) {
				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}
	
	public static Document getUserByEmail( String email, String password) {
		Document user = null;
		MongoDatabase db = MD.getDatabaseConnection();
		try {
			Document query = new Document();
			query.put("email", email);
			query.put("password", password);
			MongoCollection<Document> userMongoDatabase = db.getCollection(User.table);
			MongoCursor<Document> cursor = userMongoDatabase.find(query).iterator();
			while (cursor.hasNext()) {
				user = cursor.next();
				break;
			}
			cursor.close();
			
			return user;
		} catch (Exception e) {
			
			return user;
		}
	}
	
	public static Document getUserByID( String id) {
		Document user = null;
		MongoDatabase db = MD.getDatabaseConnection();
		try {
			Document query = new Document();
			query.put("id", id);
			MongoCollection<Document> userMongoDatabase = db.getCollection(User.table);
			MongoCursor<Document> cursor = userMongoDatabase.find(query).iterator();
			while (cursor.hasNext()) {
				user = cursor.next();
				break;
			}
			cursor.close();
			
			return user;
		} catch (Exception e) {
			
			return user;
		}
	}
	
	public static boolean updateUserName(String id, String name) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> userAction = db.getCollection(User.table);
			Document query = new Document();
			query.put("id", id);
			Document update = new Document();
			Document info = new Document("name", name);
			update.put("$set", info);
			userAction.updateOne(query, update);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean validateUserName(String password) {
		// TODO Auto-generated method stub
		String passReg = "^[a-zA-Z0-9\u4E00-\u9FA5]{1,10}$";
		if (password.matches(passReg)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateUserPassword(String password) {
		// TODO Auto-generated method stub
		String passReg = "^[a-zA-Z0-9_\\s]{6,10}$";
		if (password.matches(passReg)) {
			boolean hasNumber = false;
			boolean hasCap = false;
			boolean hasLower = false;
			for (int i = 0; i < password.length(); i++) {
				char ch = password.charAt(i);
				if (Character.isDigit(ch)) {
					hasNumber = true;
				} else if (Character.isUpperCase(ch)) {
					hasCap = true;
				} else if (Character.isLowerCase(ch)) {
					hasLower = true;
				}

			}
			if (hasNumber && hasCap && hasLower) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static String getValidationCode(String email) {
		HashMap result = new HashMap<>();
		Gson gson = new Gson();
		if (User.isEmailExisted(email)) {
			result.put("exist", "1");
			return gson.toJson(result);
		} else {
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			String code = String.valueOf(n);
			EmailSender es = new EmailSender();
			es.sendEmailToManagerByEmailInvitaionCode(email, code);
			result.put("exist", "0");
			result.put("code", code);
			return gson.toJson(result);
			// return code;
		}
	}
	
	public static boolean updateUserPasswordByEmail(String email, String new_password) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> user = db.getCollection(User.table);
			Document query = new Document();
			query.put("email", email);
			query.put("status", 1);
			Document update = new Document();
			Document info = new Document("password", new_password);
			update.put("$set", info);
			user.updateOne(query, update);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isEmailExisted(String email) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			Document query = new Document();
			query.put("email", email);
			MongoCollection<Document> userMongoDatabase = db.getCollection(User.table);
			long number = userMongoDatabase.count(query);
			if (number > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
