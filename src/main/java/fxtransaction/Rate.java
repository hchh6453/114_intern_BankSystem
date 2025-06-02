package fxtransaction;

import java.math.BigDecimal;

/**
 * 
 * 匯率資料列
 * 
 * 
 */
public class Rate {
	/** 幣別名稱 */
	private String currency;
	/** 幣別中文名稱 */
	private String currencyinchinese;
	/** 買入價格 */
	private BigDecimal buyprice;
	/** 賣出價格 */
	private BigDecimal sellprice;
	/** 貨幣小數點後位數 */
	private int scalenums;
	
	public String getCurrency() {
		return this.currency;
	}
	
	public String getTranslation() {
		return this.currencyinchinese;
	}
	
	public BigDecimal getBuyPrice() {
		return this.buyprice;
	}
	
	public BigDecimal getSellPrice() {
		return this.sellprice;
	}
	
	public int getScaleNums() {
		return this.scalenums;
	}
	
	/** 格式：貨幣名稱, 貨幣中文名稱, 買入價格, 賣出價格 */
	public Rate(String currency, String currencyinchinese, BigDecimal buyprice, BigDecimal sellprice) {
		this.currency = currency;
		this.currencyinchinese = currencyinchinese;
		this.buyprice = buyprice;
		this.sellprice = sellprice;
		this.scalenums = RateTable.getScales(currency);
	}
}
