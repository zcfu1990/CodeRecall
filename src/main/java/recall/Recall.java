package recall;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import sql.MD;
import tools.Methods;

public class Recall {
	private static final int showSize = 12;
	public static String table = "recall_instances";
	public static int DRAFT = 2, ON = 1;
	public static Document getPostInstance(String pid, String uid, String recall_id, String title,
			String cover_image, String content, List<String> tags,List<String> device_types, int level, String company, String device, String url, String date, int status) {
		Document post = new Document();
		String currentTime = Methods.getCurrentTime();
		post.append("id", pid);
		post.append("uid", uid);
		post.append("recall_id", recall_id);
		post.append("title", title);
		post.append("tags", tags);
		post.append("device_types", device_types);
		post.append("cover_image", cover_image);
		post.append("content", content);
		post.append("analysis", "");
		post.append("datetime", date);
		post.append("likeids", new ArrayList<String>());
		post.append("total_likes", 0);
		post.append("starids", new ArrayList<String>());
		post.append("total_stars", 0);
		post.append("reportids", new ArrayList<String>());// id, [reason]
		post.append("company", company);
		post.append("device", device);
		post.append("url", url);
		post.append("rootCause", "");
		post.append("time", Methods.getStringTimeIDLong());
		post.append("total_comments", 0);
		post.append("status", status);
		return post;
	}

	public static boolean addNewPost(String pid, String uid, String recall_id, String title,
			String cover_image, String content,List<String> tags, List<String> device_types, int level, String company, String device, String url, String date, int status) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document instance = Recall.getPostInstance(pid, uid,recall_id, title, cover_image, content, tags, device_types, level, company, device, url, date, status);
			post.insertOne(instance);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	public static Document getRecall(String id) {
		Document instance = null;
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> post = db.getCollection(Recall.table);
		Document query = new Document();
		query.append("id", id);
		MongoCursor<Document> cursor = post.find(query).iterator();
		while (cursor.hasNext()) {
			instance = cursor.next();
			if (instance != null && Recall.initPostExtraInfo(instance, false)) {

			} else {
				instance = null;
			}
			break;
		}
		cursor.close();
		return instance;
	}

	public static Document getRecall(String uid, String id) {
		Document instance = null;
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> post = db.getCollection(Recall.table);
		Document query = new Document();
		query.append("id", id);
		MongoCursor<Document> cursor = post.find(query).iterator();
		Document userInstance=User.getSinlgeUseProfile(uid);
		int role=userInstance.getInteger("role");
		while (cursor.hasNext()) {
			instance = cursor.next();
			if (instance != null && Recall.initPostExtraInfo(instance, uid, role, false)) {

			} else {
				instance = null;
			}
		}
		cursor.close();
		return instance;
	}

	public static boolean initPostExtraInfo(Document post, String tuid, int role, boolean removeContent) {
		try {
			if(post.getString("cover_image")==null)
			{
				post.append("cover_image","");
			}
			post.append("hasLogin", 1);
			List<String> likeids = post.get("likeids", List.class);
			boolean hasLike = false;
			if (likeids != null && likeids.size() > 0) {
				if (likeids.contains(tuid)) {
					post.append("isLike", 1);
					hasLike = true;
				}
			}
			if (!hasLike) {
				post.append("isLike", 0);
			}

			List<String> starids = post.get("starids", List.class);
			boolean hasStar = false;
			if (starids != null && starids.size() > 0) {
				if (starids.contains(tuid)) {
					post.append("isStar", 1);
					hasStar = true;
				}
			}
			if (!hasStar) {
				post.append("isStar", 0);
			}

			if (role==User.ADMIN||role==User.OWNER) {
				post.append("hasAuthority", 1);
			} else {
				post.append("hasAuthority", 0);
			}
			if (!post.getString("cover_image").equals("") && removeContent) {
				post.remove("content");
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean initPostExtraInfo(Document post, boolean removeContent) {
		try {
			post.append("hasLogin", 0);
			if(post.getString("cover_image")==null)
			{
				post.append("cover_image","");
			}
			post.append("hasAuthority", 0);
			post.append("isLike", 0);
			if (!post.getString("cover_image").equals("") && removeContent) {
				post.remove("content");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public static List<Document> getPublicRecalls(List<String> ids) {
		List<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> postAction = db.getCollection(Recall.table);
			MongoCursor<Document> cursor;
			Document query = new Document();
			if (ids != null && ids.size() > 0) {
				query.append("id", new Document("$nin", ids));
			}
			query.append("status", Recall.ON);
			cursor = postAction.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
			while (cursor.hasNext()) {
				Document temp = cursor.next();
				if (temp != null && Recall.initPostExtraInfo(temp, true)) {
					result.add(temp);
				}
			}
			cursor.close();
			return result;

		} catch (Exception e) {
			return result;
		}
	}
	
	
	public static List<Document> getCustomerRecalls(String uid, List<String> ids) {
		List<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> postAction = db.getCollection(Recall.table);
			MongoCursor<Document> cursor;
			Document query = new Document();
			if (ids != null && ids.size() > 0) {
				query.append("id", new Document("$nin", ids));
			}
			query.append("status", Recall.ON);
			cursor = postAction.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
			Document userInstance=User.getSinlgeUseProfile(uid);
			int role=userInstance.getInteger("role");
			while (cursor.hasNext()) {
				Document temp = cursor.next();
				if (temp != null && Recall.initPostExtraInfo(temp, uid, role, true)) {
					result.add(temp);
				}
			}
			cursor.close();
			return result;

		} catch (Exception e) {
			return result;
		}
	}

	public static LinkedList<Document> getCustomerRecommendRecalls(String uid, List<String> ids) {
		LinkedList<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			Document user = User.getSinlgeUseProfile(uid);
			List<String> tags = user.get("tags", List.class);
			MongoCollection<Document> postAction = db.getCollection(Recall.table);
			MongoCursor<Document> cursor;
			Document query = new Document();
			if (ids != null && ids.size() > 0) {
				query.append("id", new Document("$nin", ids));
			}
			if (tags != null && tags.size() > 0) {
				List<Document> tag_query = new ArrayList<>();
				for (String tag : tags) {
					tag_query.add(new Document("tags", tag));
				}
				query.append("$or", tag_query);
			}
			query.append("status", Recall.ON);
			cursor = postAction.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
			while (cursor.hasNext()) {
				Document temp = cursor.next();
				if (temp != null && Recall.initPostExtraInfo(temp, uid,user.getInteger("role"), true)) {

					result.add(temp);
					ids.add(temp.getString("id"));
				}
			}
			if (result.size() < Recall.showSize) {
				Document query_new = new Document();
				if (ids != null && ids.size() > 0) {
					query_new.append("id", new Document("$nin", ids));
				}
				query_new.append("status", Recall.ON);
				cursor = postAction.find(query_new).sort(new Document("_id", -1)).limit(Recall.showSize - result.size())
						.iterator();
				while (cursor.hasNext()) {
					Document temp = cursor.next();
					if (temp != null && Recall.initPostExtraInfo(temp, uid,user.getInteger("role"), true)) {

						result.add(temp);
					}
				}
			}
			cursor.close();
			return result;

		} catch (Exception e) {
			return result;
		}
	}

	public static long getCustomerPostDraftsNumberByReferID(String refer_id, String uid) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> postAction = db.getCollection(Recall.table);
			Document query = new Document();
			query.append("uid", uid);
			query.append("status", Recall.DRAFT);
			long number = postAction.count(query);
			return number;
		} catch (Exception e) {

			return 20;
		}
	}

	public static LinkedList<Document> getUserStarRecalls(String uid, List<String> ids) {
		LinkedList<Document> result = new LinkedList<>();
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> postAction = db.getCollection(Recall.table);
			Document user = User.getSinlgeUseProfile(uid);
			List<String> pids = user.get("post_stars", List.class);
			if (pids.size() > 0 && ids.size() > 0) {
				pids.removeAll(ids);
			}
			MongoCursor<Document> cursor;
			Document query = new Document();
			if (pids != null && pids.size() > 0) {
				query.append("id", new Document("$in", pids));
			} else {
				return result;
			}
			query.append("status", Recall.ON);
			cursor = postAction.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
			while (cursor.hasNext()) {
				Document temp = cursor.next();
				if (temp != null && Recall.initPostExtraInfo(temp, uid,user.getInteger("role"), true)) {
					result.add(temp);
				}
			}
			cursor.close();

			return result;

		} catch (Exception e) {

			return result;
		}
	}

	public static boolean deleteRecall(String id) {

		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document query = new Document("id",id);
			post.deleteOne(query);

			return true;
		} catch (Exception e) {

			return false;
		}
	}


	public static boolean updateRecall(String uid, String pid, String title, String cover_image, String content,List<String> tags, int level, String company, String device, int status) {
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document query = new Document();
			query.append("id", pid);
			Document update = new Document();
			Document info = new Document("title", title).append("cover_image", cover_image).append("content", content).append("tags", tags).append("level", level).append("company", company).append("device", device).append("status", status);
			update.append("$set", info);
			post.updateOne(query, update);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean updatePostStatus(String uid, String pid, int status) {

		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> post = db.getCollection(Recall.table);
		Document query = new Document();
		query.append("id", pid);
		Document update = new Document();
		Document info = new Document("status", status).append("time", Methods.getStringTimeIDLong());
		update.append("$set", info);
		post.updateOne(query, update);
		return true;
		// post.getCount(null);
	}

	public static boolean putPostAtTOP(String uid, String pid, int value) {

		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> post = db.getCollection(Recall.table);
		Document query = new Document();
		query.append("id", pid);
		Document update = new Document();
		Document info = new Document("top", value);
		if (value == 1) {
			info.append("time", Methods.getStringTimeIDLong());
		}
		update.append("$set", info);
		post.updateOne(query, update);
		return true;
	}

	public static boolean doLikeAction(String id, String uid, int value) {

		try {
			MongoDatabase db = MD.getDatabaseConnection();
			// test
			Document instance = Recall.getRecall(id);
			List<String> likeids = instance.get("likeids", List.class);
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document query = new Document();
			query.append("id", id);
			Document update = new Document();
			if (value == 1)//
			{
				if (likeids.size() > 0 && likeids.contains(uid)) {
					return true;
				} else {
					update.append("$inc", new Document("total_likes", 1));
					update.append("$addToSet", new Document("likeids", uid));
				}

				// update.append("$inc", new Document("total_likes", 1));
			} else {
				update.append("$pull", new Document("likeids", uid));
				update.append("$inc", new Document("total_likes", -1));
			}
			post.updateOne(query, update);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean doStarAction(String id, String uid, int value) {

		try (ClientSession clientSession = MD.mongoClient.startSession()) {
			MongoDatabase db = MD.getDatabaseConnection();
			// test
			MongoCollection<Document> post = db.getCollection(Recall.table);
			MongoCollection<Document> user = db.getCollection(User.table);
			Document query = new Document();
			query.append("id", id);
			Document update = new Document();

			Document query_user = new Document();
			query_user.append("id", uid);
			Document update_user = new Document();

			if (value == 1)//
			{
				update.append("$addToSet", new Document("starids", uid));
				update.append("$inc", new Document("total_stars", 1));
				update_user.append("$addToSet", new Document("post_stars", id));
			} else {
				update.append("$pull", new Document("starids", uid));
				update.append("$inc", new Document("total_stars", -1));
				update_user.append("$pull", new Document("post_stars", id));

			}

			clientSession.startTransaction();
			post.updateOne(clientSession, query, update);
			user.updateOne(clientSession, query_user, update_user);
			clientSession.commitTransaction();

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static boolean updateCommentNumber(String id, int value) {

		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document query = new Document();
			query.append("id", id);
			Document update = new Document();
			update.append("$inc", new Document("total_comments", value));
			post.updateOne(query, update);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static void updatePostStatusByReferID(String uid2, String refer_id, int old_status, int status) {
		// TODO Auto-generated method stub
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> post = db.getCollection(Recall.table);
			Document query = new Document();
			query.append("refer_id", refer_id);
			query.append("status", old_status);
			Document update = new Document();
			Document info = new Document("status", status).append("time", Methods.getStringTimeIDLong());
			update.append("$set", info);
			post.updateMany(query, update);
		} catch (Exception e) {

		}
	}

	public static List<Document> getRecallSearch_Public(String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(Recall.table);
		Document query = new Document();
		List<Document> orinfo = new ArrayList<Document>();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		orinfo.add(new Document("title", java.util.regex.Pattern.compile(key, Pattern.CASE_INSENSITIVE)));
		orinfo.add(new Document("tags", key));
		query.append("$or", orinfo);
		query.append("status", Recall.ON);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
		// cursor.limit(Post.showSize);
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null && Recall.initPostExtraInfo(temp, true)) {

				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}

	public static List<Document> getRecallSearch_Customer(String uid, String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(Recall.table);
		Document query = new Document();
		List<Document> orinfo = new ArrayList<Document>();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		orinfo.add(new Document("title", java.util.regex.Pattern.compile(key, Pattern.CASE_INSENSITIVE)));
		orinfo.add(new Document("tags", key));
		query.append("$or", orinfo);
		query.append("status", Recall.ON);
		Document user = User.getSinlgeUseProfile(uid);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null && Recall.initPostExtraInfo(temp, uid,user.getInteger("role"), true)) {
				result.add(temp);
			}
		}
		cursor.close();

		if (result.size() < Recall.showSize) {
			query = new Document();
			orinfo = new ArrayList<Document>();
			if (ids != null && ids.size() > 0) {
				query.append("id", new Document("$nin", ids));
			}

				orinfo.add(new Document("title", java.util.regex.Pattern.compile(key, Pattern.CASE_INSENSITIVE)));
				orinfo.add(new Document("tags", key));
				query.append("$or", orinfo);
				query.append("status", Recall.ON);
				cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize - result.size())
						.iterator();
				while (cursor.hasNext()) {
					Document temp = cursor.next();
					if (temp != null && Recall.initPostExtraInfo(temp, uid, user.getInteger("role"),true)) {
						result.add(temp);
					}
				}
				cursor.close();
			
		}


		return result;
	}

	public static List<Document> getRecallSearch_Customer_Tag(String uid, String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		Document user = User.getSinlgeUseProfile(uid);

		MongoCollection<Document> group = db.getCollection(Recall.table);
		Document query = new Document();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		query.append("tags", key);
		query.append("status", Recall.ON);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null && Recall.initPostExtraInfo(temp, uid, user.getInteger("role"), true)) {
				result.add(temp);
			}
		}
		cursor.close();

		if (result.size() < Recall.showSize) {
			query = new Document();
			if (ids != null && ids.size() > 0) {
				query.append("id", new Document("$nin", ids));
			}
			query.append("tags", key);
			query.append("status", Recall.ON);
			cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize - result.size())
					.iterator();
			while (cursor.hasNext()) {
				Document temp = cursor.next();
				if (temp != null && Recall.initPostExtraInfo(temp, uid,  user.getInteger("role"),true)) {
					result.add(temp);
				}
			}
			cursor.close();
		}

		return result;
	}

	public static List<Document> getRecallSearch_Public_Tag(String key, List<String> ids) {
		// TODO Auto-generated method stub
		List<Document> result = new LinkedList<>();
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> group = db.getCollection(Recall.table);
		Document query = new Document();
		if (ids != null && ids.size() > 0) {
			query.append("id", new Document("$nin", ids));
		}
		query.append("tags", key);
		query.append("status", Recall.ON);
		MongoCursor<Document> cursor = group.find(query).sort(new Document("_id", -1)).limit(Recall.showSize).iterator();
		while (cursor.hasNext()) {
			Document temp = cursor.next();
			if (temp != null && Recall.initPostExtraInfo(temp, true)) {

				result.add(temp);
			}
		}
		cursor.close();
		return result;
	}

}
