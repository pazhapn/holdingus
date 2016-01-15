package us.holdings.backend.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.holdings.backend.http.HttpWorker;

@Service
public class ZackCollector {
	private static Logger log = LoggerFactory.getLogger(ZackCollector.class);
	
	@Autowired
	private HttpWorker httpWorker;
	
	public void getMFStocks(String symbol) throws Exception{
		String url = "http://www.zacks.com/funds/mutual-fund/quote/"+symbol+"/holding";
		String content = httpWorker.getContent(url);
		String[] temp = content.split("\\R+");
		for(String t:temp){
			if(t.trim().startsWith(", data")){
				if(log.isDebugEnabled())log.debug("line {}",t);
			}
		}
	}
	
}
