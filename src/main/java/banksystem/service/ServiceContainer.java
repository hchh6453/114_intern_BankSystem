package banksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

/**
 * 處理所有串接資料庫的服務，需要時可自行取用所需的服務
 * 
 */
@Component
public class ServiceContainer {
	@Autowired
	private UserService userservice;
	
	@Autowired
	private AccountService aservice;
	
	@Autowired
	private AccountBalanceService abservice;
	
	@Autowired
	private TransactionRecordService trservice;
	
	/** 取得使用者服務 */
	public UserService getUserService() {
		return this.userservice;
	}
	
	/** 取得帳戶服務 */
	public AccountService getAccountService() {
		return this.aservice;
	}
	
	/** 取得帳戶餘額服務 */
	public AccountBalanceService getAccountBalanceService() {
		return this.abservice;
	}
	
	/** 取得帳戶餘額服務 */
	public TransactionRecordService getTransactionrecordService() {
		return this.trservice;
	}

}
