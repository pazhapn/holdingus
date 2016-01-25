package us.holdings.common.db;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.mongodb.MongoClient;

public class DBStore {
	
	private Jongo db;
	
	public DBStore(String serverAddress, int serverPort, String dbName) throws UnknownHostException{
		this.db = new Jongo(new MongoClient(serverAddress , serverPort).getDB(dbName), 
				  		new JacksonMapper.Builder()
					      	.registerModule(new AfterburnerModule())
					      	.build());
	}
	
	public MongoCollection getCollection(String modelName){
		//return this.db.getCollection(modelName).withWriteConcern(WriteConcern.FSYNCED);
		return this.db.getCollection(modelName);
	}
	
	public void runCommand(String command){
		db.runCommand(command);
	}
	public void shutdown(){
		this.db.getDatabase().getMongo().close();
	}
}
