package controller;


import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import recall.Recall;
import sql.MD;

public class CreateDatabase {
	public static String user_table="recall_user";
	public static String recall_table="recall_instances",recall_comment_table="recall_comments";
	
	public static void main(String[] args)
	{
		CreateDatabase temp=new CreateDatabase();
		temp.createCollection();
	}
	public void createCollection()
	{
		MongoDatabase db = MD.getDatabaseConnection();
		db.createCollection(user_table);
		db.createCollection(recall_table);
		db.createCollection(recall_comment_table);
		System.out.println("Recall Table Created");
	}
	
	public void clearData()
	{
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> instance = db.getCollection(Recall.table);
		Document query = new Document();
		instance.deleteMany(query);
		
	}
}
