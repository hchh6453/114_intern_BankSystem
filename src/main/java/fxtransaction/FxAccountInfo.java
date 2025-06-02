package fxtransaction;

import banksystem.entity.AccountInfo;

/**
 * 外幣帳戶資訊
 * 
 */
public class FxAccountInfo implements Account{
	/** 外幣帳戶 */
	private AccountInfo account;
	
	public FxAccountInfo(AccountInfo account) {
		this.account = account;
	}
	
	@Override
	public AccountInfo getAccountInfo() {
		return this.account;
	}
}
