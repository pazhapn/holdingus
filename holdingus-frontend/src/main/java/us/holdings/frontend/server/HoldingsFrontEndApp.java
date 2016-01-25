package us.holdings.frontend.server;

import static spark.Spark.awaitInitialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HoldingsFrontEndApp {
	private static final Logger log = LoggerFactory.getLogger(HoldingsFrontEndApp.class);
	
	public void run() throws Exception{
    	try(ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("frontend-app.xml")){
        	ctx.getBean(AllRoutesHandler.class).setupRoutes();
            ctx.registerShutdownHook();
    		awaitInitialization();
    	};
    	log.info("Server started");
    }

	public static void main(String[] args) {
		try {
			new HoldingsFrontEndApp().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
