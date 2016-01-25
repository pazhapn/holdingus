package us.holdings.frontend.server;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.port;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;

public class WebConfig {
	private final PebbleEngine engine;
	
	public WebConfig(String staticFolder, int port, String templates, String mode) throws Exception {
		externalStaticFileLocation(staticFolder); // Static files
    	port(port);
    	FileLoader loader = new FileLoader();
		loader.setPrefix(templates);
		loader.setSuffix(".html");
		if(mode.equalsIgnoreCase("dev")){
			this.engine = new PebbleEngine.Builder().loader(loader).templateCache(null).build();
		}else{
			this.engine = new PebbleEngine.Builder().loader(loader).build();
		}
	}
	
	public String render(Map<String, Object> context, String peb) throws Exception{
		Writer writer = new StringWriter();
		if(context != null && !context.isEmpty())
			engine.getTemplate(peb).evaluate(writer, context);
		else
			engine.getTemplate(peb).evaluate(writer);
		return writer.toString();
	}
	
	public String render(String peb) throws Exception{
		return render(null, peb);
	}
	public PebbleEngine getEngine() {
		return engine;
	}
	
}