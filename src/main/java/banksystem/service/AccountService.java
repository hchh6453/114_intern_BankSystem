package banksystem.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banksystem.entity.AccountBalance;
import banksystem.entity.AccountInfo;
import banksystem.entity.TransactionRecord;
import banksystem.repository.AccountRepository;
import fxtransaction.RateTable;

/**
 * 針對帳戶的資料庫操作
 * 
 */
@Service
public class AccountService {
	@Autowired
	private AccountRepository arepo;
	
	@Autowired
	private AccountBalanceService abservice;
	
	@Autowired
	private TransactionRecordService trservice;
	
	/** 檢查是否存在帳號 */
	public boolean existAccountNum(String accountNum) {
		return arepo.existsByAccountNum(accountNum);
	}
	
	/** 用帳號取得單個帳號 */
	public AccountInfo getAccount(String accountNum) {
		Optional<AccountInfo> temp = arepo.findByAccountNum(accountNum);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}
	
	/** 帳號初始建置 */
	@Transactional
	public AccountInfo addAccount(AccountInfo a) {
		try {
			AccountInfo saveda = arepo.save(a);// 建立新的帳戶
			int accountid = saveda.getId(); // 獲取自動產生的帳戶編號
			AccountBalance ab = new AccountBalance(a, accountid, a.getCurrency(), a.getInitBalance());// 建立帳戶初始餘額
			saveda.addAccountBalance(ab); // 建立雙向關聯
			abservice.setBalance(a, ab.getId().getId(), ab.getId().getCurrency(), ab.getBalance());// 將帳戶的初始餘額存進資料庫
			arepo.save(saveda); // 再儲存一次
			return saveda;
		} catch (Exception e) {
			throw new RuntimeException("新增帳戶失敗", e);
		}
	}
	
	/** 新增帳戶的交易紀錄 */
	@Transactional
	public void addAccountTransactionRecord(AccountInfo account, String action, String currency, BigDecimal sell, BigDecimal buy, BigDecimal temp, RateTable rate) {
		try {
			// 建立交易紀錄
			TransactionRecord tr = new TransactionRecord(action, currency, sell, buy, temp, rate);
			// 重新抓取一次帳戶
			AccountInfo find = arepo.findById(account.getId()).get();
			find.addTransactionRecord(tr); // 建立雙向關聯
			
			trservice.addTransactionRecord(tr); // 建立交易紀錄
			arepo.save(find); // 儲存該帳戶的交易紀錄
			return;
		} catch (Exception e) {
			
		}
	}
}
