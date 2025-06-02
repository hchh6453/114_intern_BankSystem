package banksystem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banksystem.entity.AccountBalance;
import banksystem.entity.AccountBalanceId;
import banksystem.entity.AccountInfo;
import banksystem.repository.AccountBalanceRepository;

/**
 * 針對帳戶餘額（account_balance）資料庫的操作
 * 
 */
@Service
public class AccountBalanceService {
	@Autowired
	private AccountBalanceRepository abrepo;
	
	/** 檢查此帳戶是否存在此幣別帳戶 */
	public boolean existid(int id, String currency) {
		return abrepo.existsById(new AccountBalanceId(id, currency));
	}
	
	/** 設置餘額(創建或更新)：回傳當筆餘額 */
	@Transactional
	public BigDecimal setBalance(AccountInfo account, int accountid, String currency, BigDecimal balance) {
		try {
			AccountBalanceId id = new AccountBalanceId(accountid, currency);
			AccountBalance accountbalance = abrepo.findById(id) // 看是否有已建立的帳戶餘額
					.orElseGet(() -> {
		                AccountBalance newBalance = new AccountBalance(account, accountid, currency, BigDecimal.ZERO);
		                return newBalance; // 無則新開一個
		            });;
		    accountbalance.setBalance(accountbalance.getBalance().add(balance));
		    abrepo.save(accountbalance);
		    return accountbalance.getBalance();
		} catch (Exception e) {
			throw new RuntimeException("設置餘額失敗", e);
		}
	}
	
	/** 取得單個幣別餘額 */
	public AccountBalance getAccountBalance(AccountBalanceId id) {
		Optional<AccountBalance> temp = abrepo.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}
	
	/** 取得某帳戶底下的所有幣別餘額 */
	public List<AccountBalance> getAllBalanceByaccountid(int accountid){
		return abrepo.findByAccount_Id(accountid);
	}
	
	
}
