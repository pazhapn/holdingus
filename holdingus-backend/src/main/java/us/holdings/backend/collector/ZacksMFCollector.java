package us.holdings.backend.collector;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import us.holdings.backend.exception.BackOpsException;
import us.holdings.backend.helpers.model.ModelsBuilder;
import us.holdings.backend.http.HttpWorker;
import us.holdings.backend.service.ZacksMFService;
import us.holdings.backend.util.JSONUtil;
import us.holdings.common.dao.MongoDAO;
import us.holdings.common.model.ZacksMF;
import us.holdings.common.service.BaseService;

public class ZacksMFCollector {
	private static Logger log = LoggerFactory.getLogger(ZacksMFCollector.class);
	
	private HttpWorker httpWorker;
	private BaseService<ZacksMF, MongoDAO<ZacksMF>> zacksMFService;
	private String zacksMFSymbolFile, zacksMFSymbolMissedFile;
	private boolean zacksMFSymbolToRetrieveIsMissed;
	
	public ZacksMFCollector(HttpWorker httpWorker, ZacksMFService zacksMFService, 
			String zacksMFSymbolFile, String zacksMFSymbolMissedFile,
			boolean zacksMFSymbolToRetrieveIsMissed) throws Exception{
		this.httpWorker = httpWorker;
		this.zacksMFService = zacksMFService;
		this.zacksMFSymbolFile = zacksMFSymbolFile;
		this.zacksMFSymbolMissedFile = zacksMFSymbolMissedFile;
		this.zacksMFSymbolToRetrieveIsMissed = zacksMFSymbolToRetrieveIsMissed;
	}
	
	public void getAllMFStocks() throws Exception{
		String fileToRead;
		if(zacksMFSymbolToRetrieveIsMissed) fileToRead = zacksMFSymbolMissedFile;
		else fileToRead = zacksMFSymbolFile;
		List<String> mfSymbols = Files.readLines(new File(fileToRead), Charsets.UTF_8);
		String[] temp;
		long lastAccess = 0, diff;
		String content, mfSymbol;
		long interval = 2 * 1000;
		int mfSymbolsDone = 0;
		for(String line: mfSymbols){
			mfSymbolsDone++;
			temp = line.split("~");
			if(zacksMFSymbolToRetrieveIsMissed) mfSymbol = temp[1];
			else mfSymbol = temp[0];
			try{
				content = null;
				lastAccess = System.currentTimeMillis();
				content = getMFContent(mfSymbol);
				retrieveMFData(mfSymbol, content);
				if(log.isDebugEnabled())log.debug("Success zacksMF {}, {}", mfSymbol, mfSymbolsDone);
			}catch(BackOpsException bops){
				if(log.isDebugEnabled())log.debug("No Data zacksMF {}, {}", mfSymbol, mfSymbolsDone);
			}catch(Exception e){
				if(log.isDebugEnabled())log.debug("Failure zacksMF {}, {}", mfSymbol, mfSymbolsDone);
				log.error(e.getMessage(), e);
				try{Thread.sleep(4 * 1000);}catch(Exception es){}
			}
			diff = interval - (System.currentTimeMillis() - lastAccess);
			if(diff > 0){
				try{Thread.sleep(diff);}catch(Exception e){}
			}
		}
		if(log.isDebugEnabled())log.debug("All Done");
	}
	
	public void getMFStocks(String mfSymbol) throws Exception{
		retrieveMFData(mfSymbol, getMFContent(mfSymbol));
	}
	private String getMFContent(String mfSymbol) throws Exception{
		//if(log.isDebugEnabled())log.debug("retrieving mfSymbol {} {}", mfSymbol, "http://www.zacks.com/funds/mutual-fund/quote/"+mfSymbol+"/holding");
		return httpWorker.getContent("http://www.zacks.com/funds/mutual-fund/quote/"+mfSymbol+"/holding");
	}
	
	private void retrieveMFData(String mfSymbol, String content) throws BackOpsException, Exception{
		//Files.write(content, new File("c:/temp/zacks.txt"), Charsets.UTF_8);
		String[] temp = content.split("\\R+");
		String te, symbol;
		int startPos, endPos;
		ZacksMF model;
		boolean dataNotFound = true;
		for(String t:temp){
			if(t.trim().startsWith(", data")){
				startPos = t.indexOf('[');
				endPos = t.lastIndexOf(']');
				if(startPos > 0 && endPos > (0+startPos)){
					te = t.substring(startPos, endPos+1);
					List<List<String>> data = JSONUtil.read(te, new TypeReference<List<List<String>>>(){});
					if(data.size() > 0) 
						dataNotFound = false;
					for(List<String> d: data){
						symbol = d.get(0).replaceAll("</span></a>", "");
						symbol = symbol.substring(symbol.lastIndexOf('>')+1, symbol.length());
						Map<String, String> z = new HashMap<>();
						z.put(ZacksMF.AS_OF_DATE, d.get(1));
						z.put(ZacksMF.COMPANY_NAME, d.get(5));
						z.put(ZacksMF.ISSUE, d.get(6));
						z.put(ZacksMF.MARKET_VALUE, d.get(3));
						z.put(ZacksMF.MF_SYMBOL,mfSymbol);
						z.put(ZacksMF.SHARES, d.get(2));
						z.put(ZacksMF.SYMBOL, symbol);
						z.put(ZacksMF.WEIGHT, d.get(4));
						model = ModelsBuilder.getZacksMF(z);//save
						zacksMFService.save(model);
					}
				}
			}
		}
		if(dataNotFound) 
			throw new BackOpsException("data not found");
	}
	
}
