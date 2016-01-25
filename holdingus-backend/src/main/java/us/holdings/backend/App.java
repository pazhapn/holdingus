package us.holdings.backend;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import us.holdings.backend.collector.ZacksMFCollector;

public class App {
	
	public static void main(String[] args) {
		try {
			new App().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() throws Exception{
    	try(ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("backend-app.xml")){
        	ctx.getBean(ZacksMFCollector.class).getAllMFStocks();
            ctx.registerShutdownHook();
    	};
    }
    
    
}

