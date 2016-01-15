package us.holdings.backend;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import us.holdings.backend.collector.ZackCollector;

@Configuration
@ComponentScan({ "us.holdings.backend" })
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
    	try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(App.class)){
        	ctx.getBean(ZackCollector.class).getMFStocks("VGENX");
            ctx.registerShutdownHook();
    	};
    }
    
    
}

