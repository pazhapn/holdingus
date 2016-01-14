package us.holdings.backend;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
	public void run(){
    	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
    	//new WebConfig(ctx.getBean(MiniTwitService.class));
        ctx.registerShutdownHook();
    }
    
    
}

