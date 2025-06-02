package fxtransaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;

/**
 * 
 * 匯率資料表
 * 
 */
public class RateTable {
	/** 從 api 抓的目前匯率 */
    private HashMap<String, Rate> rateMap = new HashMap<>();
    /** 小數點位數規則 */
    private static final HashMap<String, Integer> currencyscale = new HashMap<>();
    
    // 每種幣別的小數點位數規則
    static {
    	currencyscale.put("USD", 2);
    	currencyscale.put("USD-C", 2);
    	currencyscale.put("GBP", 2);
    	currencyscale.put("AUD", 2);
    	currencyscale.put("HKD", 2);
    	currencyscale.put("HKD-C", 2);
    	currencyscale.put("SGD", 2);
    	currencyscale.put("CAD", 2);
    	currencyscale.put("CHF", 2);
    	currencyscale.put("ZAR", 2);
    	currencyscale.put("SEK", 2);
    	currencyscale.put("JPY", 0);
    	currencyscale.put("JPY-C", 0);
    	currencyscale.put("THB", 2);
    	currencyscale.put("EUR", 2);
    	currencyscale.put("EUR-C", 2);
    	currencyscale.put("NZD", 2);
    	currencyscale.put("CNY-C", 2);
    	currencyscale.put("CNY", 2);
        // ... 其他幣別
    }
    
    public RateTable() {
    	JspApi connect = new JspApi();
        JsonArray ratetable = connect.fetchExchangeRate();
        
        for(JsonElement element : ratetable) {
        	JsonObject rate = element.getAsJsonObject();
        	String currency = rate.get("curname").getAsString().replaceAll(" ", "");// 去除空格
        	Pattern p = Pattern.compile("^(.+)\\((.+)\\)$");// 開頭字串group1；左右括弧中間為group2
        	Matcher m = p.matcher(currency);
        	String curchinese = "", curname = "";
        	if(m.find()) {
        		curchinese = m.group(1);
        		curname = m.group(2);
        	} else {
        		System.out.println("幣別未成功獲取");
        	}
        	BigDecimal buy = rate.get("buy").getAsBigDecimal();
        	BigDecimal sell = rate.get("sell").getAsBigDecimal();
        	rateMap.put(curname, new Rate(curname, curchinese, buy, sell));
        }
    }
    
    public static int getScales(String currency) {
    	Integer scales = currencyscale.get(currency.toUpperCase());
    	if(scales == null) {
    		System.out.println("無對應幣別");
    		return 0; // 預設無小數點
    	}
    	return scales;
    }

    public Rate getRate(String currency) {
        return rateMap.get(currency.toUpperCase());
    }

    public HashMap<String, Rate> getAllRates() {
        return rateMap;
    }
}
