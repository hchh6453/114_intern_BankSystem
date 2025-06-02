package fxtransaction;

import java.math.BigDecimal;

import banksystem.entity.AccountInfo;
import banksystem.service.AccountBalanceService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 存款
 * @author 990809
 * 
 */
@Component
public class BankDepositAction {
	/**
	 * 
	 * 存入外幣
	 * 
	 */
	@Transactional
	public static BigDecimal FxDeposit(AccountBalanceService abservice, AccountInfo account,String currency, BigDecimal txAmt) {
		if(txAmt.compareTo(BigDecimal.ZERO)<0) {
			System.out.println("無法進行負數款項的存款");
			return null;
		}
		BigDecimal tempbalance = abservice.setBalance(account, account.getId(), currency, txAmt);
		return tempbalance; // 不存在此幣別帳戶就建立
	}
	
	/**
	 * 
	 * 存入台幣
	 * 
	 */
	@Transactional
	public static BigDecimal TwDeposit(AccountBalanceService abservice, AccountInfo account, BigDecimal txAmt) {
		if(txAmt.compareTo(BigDecimal.ZERO)<0) {
			System.out.println("無法進行負數款項的存款");
			return null;
		}
		BigDecimal tempbalance = abservice.setBalance(account, account.getId(), "TWD", txAmt);;
		return tempbalance; // 不存在台幣帳戶就建立
	}
}
