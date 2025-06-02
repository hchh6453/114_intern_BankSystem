package banksystem.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * 建立AccountBalance的複合主鍵
 * 
 */
@Embeddable
public class AccountBalanceId implements Serializable{
	/** 序列化 */
	// 加上以避免序列化相容性問題
	private static final long serialVersionUID = 1L;
	/** 帳戶編號 */
	@Column(name = "account_id")
	private int accountId;
	/** 幣別 */
	@Column(name = "currency_code")
    private String currency;
    
    public AccountBalanceId() {}
    
    public AccountBalanceId(int id, String currency) {
    	this.accountId = id;
    	this.currency = currency;
    }
    
    public int getId() {
    	return this.accountId;
    }
    
    public String getCurrency() {
    	return this.currency;
    }

    // 必須實作 equals() 和 hashCode()
    // 兩個主鍵物件是否代表同一筆資料
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountBalanceId)) return false;
        AccountBalanceId that = (AccountBalanceId) o;
        return Objects.equals(accountId, that.accountId) &&
               Objects.equals(currency, that.currency);
    }

    // 讓快取與查找可以正確運作
    @Override
    public int hashCode() {
        return Objects.hash(accountId, currency);
    }
    
}
