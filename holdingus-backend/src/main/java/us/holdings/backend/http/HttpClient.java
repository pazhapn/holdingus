package us.holdings.backend.http;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

@Component
@Scope("singleton")
public class HttpClient {
	private AsyncHttpClient asyncHttpClient;
	
	private HttpClient(){
		Builder builder = new AsyncHttpClientConfig.Builder();
	    builder.setCompressionEnforced(true)
	        .setAllowPoolingConnections(true)
	        .setRequestTimeout(30000)
	        .build();
		this.asyncHttpClient = new AsyncHttpClient(builder.build());
	}
	
	@PreDestroy
	public void shutdown(){
		this.asyncHttpClient.close();
	}
}
