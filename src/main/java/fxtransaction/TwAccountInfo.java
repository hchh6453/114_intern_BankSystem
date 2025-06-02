package fxtransaction;

import banksystem.entity.AccountInfo;

/**
 * 台幣帳戶資訊
 * 
 */
public class TwAccountInfo implements Account{
	/** 台幣帳戶 */
	private AccountInfo account;
	
	public TwAccountInfo(AccountInfo account) {
		this.account = account;
	}
	
	@Override
	public AccountInfo getAccountInfo() {
		return this.account;
	}
}
