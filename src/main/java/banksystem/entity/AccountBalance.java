package banksystem.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

/**
 * 帳戶擁有所有幣別餘額
 * @author 990809
 * 
 */
@Entity
@Table(name = "account_balance")
public class AccountBalance {
	/** 複合主鍵（帳戶編號 + 幣別） */
	@EmbeddedId
	private AccountBalanceId id;
	
	/** 帳戶編號（外鍵）*/
	@ManyToOne
	@MapsId("accountId")
	@JoinColumn(name = "account_id")
	private AccountInfo account;
	
	/** 餘額 */
	@Column(name = "balance")
	private BigDecimal balance;
	
	public void setId(AccountBalanceId id) {
		this.id = id;
	}
	
	public AccountBalanceId getId() {
		return this.id;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		return this.balance;
	}
	
	public void setAccount(AccountInfo account) {
		this.account = account;
	}
	
	/** JPA要求的預設建構子 */
	public AccountBalance() {}
	
	/** 帳戶幣別餘額資料建置 */
	public AccountBalance(AccountInfo account, int accountid, String currency, BigDecimal balance) {
		setId(new AccountBalanceId(accountid, currency));
		setAccount(account);
		setBalance(balance);
	}

}


