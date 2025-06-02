package fxtransaction;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import banksystem.entity.AccountInfo;
import banksystem.service.AccountBalanceService;

/**
 * 外幣交易的存提
 * @author 990809
 * 
 */
@Component
public class FxExchangeAction {
	/**
	 * 
	 * 買入外幣
	 * 
	 */
	@Transactional
	public static BigDecimal FxBuyIn(AccountBalanceService abservice, AccountInfo account, String currency, BigDecimal TwPay, BigDecimal buyin) {
		BigDecimal tempbalance = BankWithdrawalAction.TwWithdrawal(abservice, account, TwPay); 
		if(tempbalance!=null) {// 台幣有成功扣款才匯入外幣款項
			// System.out.println("台幣扣款成功");
			BigDecimal temp = BankDepositAction.FxDeposit(abservice, account, currency, buyin);
			if(temp!=null) {
				// System.out.println("外幣加值成功");
				return tempbalance; // 購買外幣回傳台幣餘額
			}
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 賣出外幣
	 * 
	 */
	@Transactional
	public static BigDecimal FxSellOut(AccountBalanceService abservice, AccountInfo account, String currency, BigDecimal sellout, BigDecimal TwIn) {
		BigDecimal tempbalance = BankWithdrawalAction.FxWithdrawal(abservice, account, currency, sellout); 
		if(tempbalance!=null) {// 外幣有成功扣款才匯入外幣款項
			// System.out.println("外幣扣款成功");
			BigDecimal temp = BankDepositAction.TwDeposit(abservice, account, TwIn); 
			if(temp!=null) {
				// System.out.println("台幣加值成功");
				return tempbalance; // 賣出外幣回傳外幣餘額
			}
			return null;
		} else {
			return null;
		}
	}
}
