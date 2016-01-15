package us.holdings.backend.http;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

@Component
@Scope("singleton")
public class HttpWorker {
	private static Logger log = LoggerFactory.getLogger(HttpWorker.class);
	private AsyncHttpClient asyncHttpClient;
	
	private HttpWorker(){
		Builder builder = new AsyncHttpClientConfig.Builder();
	    builder.setCompressionEnforced(true)
	        .setAllowPoolingConnections(true)
	        .setRequestTimeout(30000)
	        .build();
		this.asyncHttpClient = new AsyncHttpClient(builder.build());
	}
	
	public String getContent(String url) throws Exception{
		return this.asyncHttpClient.prepareGet(url).execute().get().getResponseBody();
	}
	
	@PreDestroy
	public void shutdown(){
		this.asyncHttpClient.close();
	}
}
