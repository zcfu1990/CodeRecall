package recall;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import sql.MD;

public class IndexCreate {
	public static void main(String[] args)
	{
		IndexCreate test=new IndexCreate();
		test.createUserIndex();
	}
	
	public void createPostIndex()
	{
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> collection = db.getCollection(Recall.table);
		collection.createIndex(Indexes.ascending("id"));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("refer_id"),Indexes.ascending("status")));

		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("refer_id"),Indexes.ascending("id"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("refer_id"),Indexes.ascending("id"), Indexes.ascending("authority"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("tags"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"),Indexes.ascending("tags"), Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"),Indexes.ascending("tags"), Indexes.ascending("title"),Indexes.ascending("status")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("tags"), Indexes.ascending("title"),Indexes.ascending("status")));

		
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("time")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("total_likes")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("total_comments")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("time")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("total_likes")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("status"), Indexes.ascending("total_comments")));
		
		
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("refer_id"),Indexes.ascending("status"), Indexes.ascending("tags")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("refer_id"),Indexes.ascending("status"), Indexes.ascending("time")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"),Indexes.ascending("refer_id"),Indexes.ascending("status"), Indexes.ascending("total_likes")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("authority"), Indexes.ascending("refer_id"),Indexes.ascending("status"), Indexes.ascending("total_comments")));
	
		
	}

	
	
	public void createUserIndex()
	{
		MongoDatabase db = MD.getDatabaseConnection();
		MongoCollection<Document> collection = db.getCollection(User.table);
		collection.createIndex(Indexes.ascending("id"));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("tags")));
		collection.createIndex(Indexes.compoundIndex(Indexes.ascending("id"),Indexes.ascending("name")));
	}
	
}
