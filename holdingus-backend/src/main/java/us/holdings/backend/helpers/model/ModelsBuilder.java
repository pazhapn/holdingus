package us.holdings.backend.helpers.model;

import java.util.Map;

import us.holdings.common.model.ZacksMF;

public class ModelsBuilder {

	public static ZacksMF getZacksMF(Map<String, String> data){
		ZacksMF z = new ZacksMF(data.get(ZacksMF.SYMBOL), 
				data.get(ZacksMF.MF_SYMBOL), getAsOfDate(data.get(ZacksMF.AS_OF_DATE)));
		z.setCompanyName(data.get(ZacksMF.COMPANY_NAME));
		z.setIssue(data.get(ZacksMF.ISSUE));
		z.setMarketValue(getMarketValue(data.get(ZacksMF.MARKET_VALUE)));
		z.setShares(Long.parseLong(data.get(ZacksMF.SHARES).replaceAll(",", "")));
		z.setWeight(getWeight(data.get(ZacksMF.WEIGHT)));
		return z;
	}
	
	private static int getWeight(String weight){
		weight = weight.trim();
		weight = weight.substring(0, weight.indexOf(' '));
		return (int)(Double.parseDouble(weight) * 100);
	}
	
	private static int getAsOfDate(String asOfDate){
		String[] t = asOfDate.trim().split("/");
		return Integer.parseInt(t[2]+t[0]+t[1]);
	}
	
	private static long getMarketValue(String mv){
		mv = mv.replaceAll(",", "");
		mv = mv.substring(0, mv.indexOf('.'));
		return Long.parseLong(mv);
	}
}
