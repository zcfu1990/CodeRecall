package recall;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import sql.MD;
import tools.Methods;
public class RecallComment {
	public static String table="recall_comments";
	private static  int showSize = 24;

	public static int POST_COMMENT=1, USER_COMMENT=2;
	public static Document getRecallCommentInstance(String refer_id, String fromid, String toid, String content, int type)
	{
		//String crate=Item.getRateFormatString(rate);
		Document temp = new Document();
		String id=toid+"_"+Methods.getStringTimeID()+"_comment";
		temp.append("id", id);
		temp.append("refer_id", refer_id);
		temp.append("from_uid", fromid);
		temp.append("to_uid", toid);
		temp.append("content",content);
		temp.append("type", type);
		temp.append("datetime",Methods.getCurrentTime());
		return temp;
	}
	
	public static boolean addNewRecallComment(String refer_id, String fromid, String toid, String content, int type)
	{
		try (ClientSession clientSession = MD.mongoClient.startSession()) {
			MongoDatabase db = MD.getDatabaseConnection();	
			Document temp=RecallComment.getRecallCommentInstance(refer_id, fromid, toid, content, type);			
			MongoCollection<Document> itemMongoDatabase_Comment = db.getCollection(RecallComment.table);
			
			MongoCollection<Document> uknowMongoDatabase_Post = db.getCollection(Recall.table);
			Document query_1 = new Document();
			query_1.append("id", refer_id);
			Document update_1 = new Document();
			update_1.append("$inc", new Document("total_comments", 1));

			clientSession.startTransaction();
			itemMongoDatabase_Comment.insertOne(clientSession, temp);
			uknowMongoDatabase_Post.updateOne(clientSession, query_1, update_1);
			clientSession.commitTransaction();			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Document getSingleRecallComment(  String id) {
		// TODO Auto-generated method stub
		Document instance=null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> itemMongoDatabase = db.getCollection(RecallComment.table);
			Document query = new Document();
			query.append("id",id);
			MongoCursor<Document> cursor = itemMongoDatabase.find(query).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				break;
			}
			cursor.close();
			
			return instance;
		} catch (Exception e) {
			
			return instance;
		}
	}
	
	
	public static int getRecallCommentNumber( String refer_id)
	{
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			//Item item=Item.getSingleItem(db, id);
			MongoCollection<Document> itemMongoDatabase = db.getCollection(RecallComment.table);
			Document query = new Document();
			query.append("refer_id", refer_id);
			int count=(int) itemMongoDatabase.count(query);
			 
			return count;
		} catch (Exception e) {
			
			return 0;
		}
	}
	
	
	
	public static boolean deleteRecallComment(String id)
	{
		try (ClientSession clientSession = MD.mongoClient.startSession()) {
			MongoDatabase db = MD.getDatabaseConnection();		
			MongoCollection<Document> itemMongoDatabase_Comment = db.getCollection(RecallComment.table);
			Document query = new Document();
			query.append("id", id);	
			
			Document pc=RecallComment.getSingleRecallComment(id);
			MongoCollection<Document> uknowMongoDatabase_Post = db.getCollection(Recall.table);
			Document query_1 = new Document();
			query_1.append("id", pc.getString("refer_id"));
			Document update_1 = new Document();
			update_1.append("$inc", new Document("total_comments", -1));

			clientSession.startTransaction();
			itemMongoDatabase_Comment.deleteOne(clientSession, query);
			uknowMongoDatabase_Post.updateOne(clientSession, query_1, update_1);
			clientSession.commitTransaction();
			
			return true;
		} catch (Exception e) {
			
			return false;
		}
	}
	
	
	public static LinkedList<Document> getRecallComments_Public(String refer_id,  List<String> ids)
	{
		LinkedList<Document> comments=new LinkedList<>();
		Document instance=null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> itemMongoDatabase = db.getCollection(RecallComment.table);
			Document from_user=null, to_user=null;
			Document query = new Document();
			query.append("refer_id", refer_id);
			if(ids.size()>0)
			{
				query.append("id", new Document("$nin", ids));
			}
			MongoCursor<Document> cursor = itemMongoDatabase.find(query).sort(new Document("_id", -1)).limit(RecallComment.showSize).iterator();
			while (cursor.hasNext()) {
				instance = cursor.next();
				if(instance!=null)
				{
					instance.append("canComment", 0);
					from_user=User.getUserByID(instance.getString("from_uid"));
					if(from_user!=null)
					{
						instance.append("from_uname",from_user.getString("name"));
						instance.append("from_uimage",from_user.getString("image"));
						instance.append("from_uid",from_user.getString("id"));
					}
					
					if(instance.getInteger("type")==RecallComment.USER_COMMENT)
					{
						to_user=User.getUserByID(instance.getString("to_uid"));
						if(to_user!=null)
						{
							instance.append("to_uname",to_user.getString("name"));
							instance.append("to_uimage",to_user.getString("image"));
							instance.append("to_uid",to_user.getString("id"));						
						}
					}
					
					comments.add(instance);
				}
			}			
			return comments;
		} catch (Exception e) {
			return comments;
		}
	}
	
	public static LinkedList<Document> getPostComments( String uid, int isFollower, String refer_id, List<String> ids)
	{
		LinkedList<Document> comments=new LinkedList<>();
		Document instance=null;
		try {
			MongoDatabase db = MD.getDatabaseConnection();
			MongoCollection<Document> itemMongoDatabase = db.getCollection(RecallComment.table);
			Document from_user=null, to_user=null;
			Document query = new Document();
			query.append("refer_id", refer_id);
			if(ids.size()>0)
			{
				query.append("id", new Document("$nin", ids));
			}
			//query.append("status", 1);
			MongoCursor<Document> cursor = itemMongoDatabase.find(query).sort(new Document("_id", -1)).limit(RecallComment.showSize).iterator();
			//cursor.sort(new Document("time", -1));
			while (cursor.hasNext()) {
				instance = cursor.next();
				instance.append("canReply", isFollower);
				if(instance!=null)
				{
					from_user=User.getUserByID(instance.getString("from_uid"));
					if(from_user!=null)
					{
						if(from_user.getString("id").equals(uid))
						{
							instance.append("isOwner", 1);
						}
						else
						{
							instance.append("isOwner", 0);
						}						
						instance.append("from_uname",from_user.getString("name"));
						instance.append("from_uimage",from_user.getString("image"));
						instance.append("from_uid",from_user.getString("id"));
					}
					
					if(instance.getInteger("type")==RecallComment.USER_COMMENT)
					{
						to_user=User.getUserByID(instance.getString("to_uid"));
						if(to_user!=null)
						{
							instance.append("to_uname",to_user.getString("name"));
							instance.append("to_uimage",to_user.getString("image"));
							instance.append("to_uid",to_user.getString("id"));
						}
					}
					
					comments.add(instance);
				}
			}
			cursor.close();
			
			return comments;
		} catch (Exception e) {
			
			return comments;
		}
	}
	
	
}
