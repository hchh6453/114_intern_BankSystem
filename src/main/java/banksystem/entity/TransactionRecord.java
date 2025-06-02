package banksystem.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fxtransaction.RateTable;
import jakarta.persistence.*;

/**
 * 
 * 交易紀錄基本資料
 * @author 990809
 * 
 */
@Entity
@Table(name = "transaction_record")
public class TransactionRecord {
	/** 交易紀錄編號 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_record_id")
	private int transactionrecordId;
	
	/** 帳戶編號（多對一）*/
	@ManyToOne
	@JoinColumn(name = "account_id") // 對應 AccountInfo 的 id 欄位
	private AccountInfo account;
	
	/** 交易紀錄類型 */
	@Column(name = "transaction_type")
	private String type;
	
	/** 交易幣別 */
	@Column(name = "currency_code")
	private String currency;
	
	/** 交易金額 */
	@Column(name = "tx_amt")
	private BigDecimal txAmt;
	
	/** 當筆交易餘額 */
	@Column(name = "tempbalance")
	private BigDecimal tempbalance;
	
	/** 交易時間 */
	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	/** 交易字串 */
	@Column(name = "record")
	private String recording;
	
	public int getId() {
		return this.transactionrecordId;
	}
	
	public AccountInfo getAccount() {
		return this.account;
	}
	
	public void setAction(String type) {
		this.type = type;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void settxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}
	
	public void setTempBalance(BigDecimal balance) {
		this.tempbalance = balance;
	}
	
	public String getRecord() {
		return this.recording;
	}
	
	public void setAccount(AccountInfo account) {
		this.account = account;
	}
	
	public TransactionRecord() {};
	
	/** 交易紀錄資料建置 */
	public TransactionRecord(String action, String currency, BigDecimal sell, BigDecimal buy, BigDecimal tempbalance, RateTable rate) {
		setAction(action);
		setCurrency(currency);
		if(sell!=null) {
			settxAmt(sell);
		} else {
			settxAmt(buy);
		}
		setTempBalance(tempbalance);
		setRecord(action, currency, sell, buy, rate);
	}
	
	/**
	 * 新增交易紀錄:
	 * 
	 * 交易行為、交易幣別、賣出金額、買入金額、交易當下匯率
	 * 
	 */
	public void setRecord(String action, String currency, BigDecimal sell, BigDecimal buy, RateTable rate) {
		String record = "";
		// 紀錄交易時間
		this.createAt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DecimalFormat numformatter = new DecimalFormat("#,###.####");
		
		if(action.equals("BuyInFx")) {
			// 買入外幣交易紀錄
			record = "【" + createAt.format(formatter) + "】買入外幣" + currency + "：TWD " + numformatter.format(sell) + " -> " + currency + " " + numformatter.format(buy) + " @" + rate.getRate(currency).getSellPrice().toString() + "\n";
		} else if(action.equals("SellOutFx")) {
			// 賣出外幣交易紀錄
			record = "【" + createAt.format(formatter) + "】賣出外幣" + currency + "：" + currency + " " + numformatter.format(sell) + " -> TWD " + numformatter.format(buy) + " @" + rate.getRate(currency).getBuyPrice().toString() + "\n";
		} else if(action.equals("TwDeposit")){
			// 台幣存款
			record = "【" + createAt.format(formatter) + "】存入TWD：" + numformatter.format(buy) + " 元\n";
		} else if(action.equals("TwWithdraw")){
			// 台幣提款
			record = "【" + createAt.format(formatter) + "】提出TWD：" + numformatter.format(sell) + " 元\n";
		} else if(action.equals("FxDeposit")){
			// 外幣存款
			record = "【" + createAt.format(formatter) + "】存入" + currency + "：" + numformatter.format(buy) + " 元\n";
		} else if(action.equals("FxWithdraw")){
			// 外幣提款
			record = "【" + createAt.format(formatter) + "】提出" + currency + "：" + numformatter.format(sell) + " 元\n";
		} else {
			System.out.println("無法紀錄交易");
		}
		this.recording = record;
	}
	
}
