package fxtransaction;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import banksystem.entity.AccountBalance;
import banksystem.entity.AccountInfo;
import banksystem.service.AccountBalanceService;

/**
 * 提款
 * @author 990809
 * 
 */
@Component
public class BankWithdrawalAction {
	/**
	 * 
	 * 提款台幣
	 * 
	 */
	@Transactional
	public static BigDecimal TwWithdrawal(AccountBalanceService abservice, AccountInfo account, BigDecimal txAmt) {
		if(txAmt.compareTo(BigDecimal.ZERO)<0) {
			System.out.println("無法進行負數款項的扣款");
			return null;
		}
		AccountBalance selected = null; // 暫存帳戶餘額
		for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())){
			// System.out.println(ab.getId().getCurrency());
			if(ab.getId().getCurrency().equals("TWD")) {
				selected = ab;
				break;
			}
		}
		if(selected==null) {
			System.out.println("您未持有台幣無法進行交易");
			return null;
		}
		else if(selected.getBalance().compareTo(txAmt)<0) {
			System.out.println("台幣餘額不足無法進行扣款");
			return null;
		}
		BigDecimal tempbalance = abservice.setBalance(account, selected.getId().getId(), "TWD", txAmt.negate());
		return tempbalance;
	}
	
	/**
	 * 
	 * 提款外幣
	 * 
	 */
	@Transactional
	public static BigDecimal FxWithdrawal(AccountBalanceService abservice, AccountInfo account, String currency, BigDecimal txAmt) {
		if(txAmt.compareTo(BigDecimal.ZERO)<0) {
			System.out.println("無法進行負數款項的扣款");
			return null;
		}
		AccountBalance selected = null; // 暫存帳戶餘額
		for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())){
			if(ab.getId().getCurrency().equals(currency)) {
				selected = ab;
				break;
			}
		}
		if(selected==null) {
			System.out.println("您未持有該幣別無法進行交易");
			return null;
		}
		else if(selected.getBalance().compareTo(txAmt)<0) {
			System.out.println("外幣餘額不足無法進行扣款");
			return null;
		}
		BigDecimal tempbalance = abservice.setBalance(account, selected.getId().getId(), currency, txAmt.negate());
		return tempbalance;
	}
}
