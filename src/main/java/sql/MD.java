package sql;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MD {
	public static MongoDatabase db=null;
	public static MongoClient mongoClient=null;
	public static String database = "ren_recall";
	public static void initMongoClient() {
	    mongoClient = MongoClients.create("mongodb://zhuzhu:comeon99@129.89.35.212:27017/?authSource=ren_recall");
		MD.db=mongoClient.getDatabase(database);
	}

	
	public static MongoClient getDatabaseClient() {

		if(mongoClient==null)
		{
			MD.initMongoClient();
		}
		return mongoClient;
	}
	
	public static MongoDatabase getDatabaseConnection() {

		if(MD.db==null)
		{
			MD.initMongoClient();
		}
		return MD.db;
	}
	

	public static void closeConnection(MongoClient mongoClient) {
		if(mongoClient!=null)
		{
		   mongoClient.close();
		}
	}
	
}
