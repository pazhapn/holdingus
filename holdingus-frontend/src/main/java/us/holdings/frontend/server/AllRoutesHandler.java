package us.holdings.frontend.server;

import static spark.Spark.get;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

public class AllRoutesHandler {
	private static final Logger log = LoggerFactory.getLogger(AllRoutesHandler.class);
	private WebConfig webConfig;
	
	public AllRoutesHandler(WebConfig webConfig){
		this.webConfig = webConfig;
	}
	
	public void setupRoutes() {
		get("/", (req, res) -> {
			return new ModelAndView(null, "base");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
	}
}
