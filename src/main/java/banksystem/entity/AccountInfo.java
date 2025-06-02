package banksystem.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fxtransaction.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import banksystem.service.AccountBalanceService;

/**
 * 帳戶基本資訊
 * @author 990809
 * 
 */
@Entity
@Table(name = "accounts")
public class AccountInfo {
	
	/** 自動產生的帳戶編號 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private int accountid;
	
	/** 帳號 */
	@Column(unique = true, nullable = false, name = "account_num")
	private String accountNum = "";
	
	/** 初始餘額 */
	@Column(name = "init_balance")
	private BigDecimal initbalance;
	
	/** 帳戶幣別 */
	@Column(name = "currency_code")
	private String currency;
	
	/** 時間標記 */
	@CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
	
	/** 多個 Account 對應一個 User (多對一) */
    @ManyToOne
    @JoinColumn(name = "user_id")  // 對應 user 的 id 欄位
    private UserInfo user;
    
    /** 擁有的帳戶餘額（一對多） */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBalance> accountbalances;
    
    /** 擁有的交易紀錄（一對多） */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionRecord> transactionrecords;
    
	public int getId() {
		return this.accountid;
	}
	
	public String getAccountNum() {
		return this.accountNum;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public BigDecimal getInitBalance() {
		return this.initbalance.setScale(0, RoundingMode.HALF_UP);
	}
	
	public void setUser(UserInfo user) {
		this.user = user;
	}
	
	/** 新增帳戶餘額 */
	public void addAccountBalance(AccountBalance ab) {
		if (this.accountbalances == null) {
	        this.accountbalances = new ArrayList<>();
	    }
		this.accountbalances.add(ab);
		ab.setAccount(this); // 雙向關聯
	}
	
	/** 新增交易紀錄 */
	public void addTransactionRecord(TransactionRecord tr) {
		if (this.transactionrecords == null) {
	        this.transactionrecords = new ArrayList<>();
	    }
		this.transactionrecords.add(tr);
		tr.setAccount(this); // 雙向關聯
	}
	
	/** 預設建構子 */
	public AccountInfo() {};
	
	/** 初始資產建置 */
	public AccountInfo(String currency, String accountNum, BigDecimal initbalance) {
		this.accountbalances = new ArrayList<>();
		this.transactionrecords = new ArrayList<>();
		this.accountNum = accountNum;
		this.initbalance = initbalance;
		this.createdAt = LocalDateTime.now();
		this.currency = currency;
	}
	
	/** 換算總資產（台幣）*/
	public BigDecimal getTotal(AccountInfo account, AccountBalanceService abservice, RateTable rate) {
		BigDecimal result = BigDecimal.ZERO;
		List<AccountBalance> balances = abservice.getAllBalanceByaccountid(account.getId());
		for(AccountBalance balance : balances) {
			if(balance.getId().getCurrency().equals("TWD")) {
				result = result.add(balance.getBalance());
			} else {
				result = result.add(balance.getBalance().multiply(rate.getRate(balance.getId().getCurrency()).getBuyPrice()));
			}
		}
		return result;
	}
	
	/** 顯示帳號(格式: XXXX-XX-XXXXX-X-XX） */
	public String getFormatAccountNum() {
		if(this.accountNum.length()!=14) {
			System.out.println("帳戶長度應為14碼");
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<this.accountNum.length(); i++) {
			if(i==4 || i==6 || i==11 || i==12) {
				sb.append("-");
				sb.append(this.accountNum.charAt(i));
			} else {
				sb.append(this.accountNum.charAt(i));
			}
		}
		return sb.toString();
	}
	
}
