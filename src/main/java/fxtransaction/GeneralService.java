package fxtransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import banksystem.entity.AccountBalance;
import banksystem.entity.AccountInfo;
import banksystem.entity.UserInfo;
import banksystem.service.AccountBalanceService;
import banksystem.service.AccountService;
import banksystem.service.UserService;

/**
 * 
 * 一般存匯服務 1. 台幣存提款 2. 外幣存提款
 * @author 990809
 * 
 */
public class GeneralService {
	private Scanner s;
	private UserService uservice;
	private AccountService aservice;
	private AccountBalanceService abservice;
	
	public GeneralService(Scanner s, UserInfo user, UserService uservice, AccountService aservice, AccountBalanceService abservice) {
		this.s = s;
		this.uservice = uservice;
		this.aservice = aservice;
		this.abservice = abservice;
		runGeneralService(user);
	}
	
	public void runGeneralService(UserInfo user) {
		UserInfo loginuser = null;
		// 目前無用戶登入資訊
		if(user == null) {
			System.out.println("一般存匯需進行登入");
			System.out.printf("您已有帳戶進行登入？（Y->進行登入，若無請輸入任意字元跳出進行開戶）：");
			String in = s.nextLine();
			if(!in.toUpperCase().equals("Y")) {
				return;
			}
			new Login(s, uservice);
			loginuser = Login.getLoginUser();
			if(loginuser == null) {
				return;
			} else {
				user = loginuser; // 在這裡登入
			}
		} else {
			// 目前已有用戶登入 
			System.out.println("目前的登入身份為 " + user.getName());
			System.out.println("即將進入一般存匯服務...");
			System.out.printf("是否使用目前的用戶身份？（Y->繼續)：");
			String in = s.nextLine();
			if(!in.toUpperCase().equals("Y")) {
				Login.Logout();
				System.out.println("登出此用戶身份，重新進入登入流程...");
				System.out.printf("您已有帳戶進行登入？（Y->進行登入，若無請輸入任意字元跳出進行開戶）：");
				in = s.nextLine();
				if(!in.toUpperCase().equals("Y")) {
					return;
				}
				new Login(s, uservice);// 切換其他使用者
				loginuser = Login.getLoginUser();
				if(loginuser == null) {
					return;
				} else {
					user = loginuser; // 在這裡登入
				}
			}
		}
		
		AccountInfo account = null;
		if(user!=null) {
			System.out.println("您擁有的帳戶如下：");
			ArrayList<AccountInfo> accounts = new ArrayList<>(); // 暫存擁有的帳戶列表
			int count = 1;
			for(AccountInfo accountlist : uservice.initAccountList(user).getAccounts()) {
				System.out.println(count + "、" + accountlist.getFormatAccountNum());
				accounts.add(accountlist);
				count++;
			}
			while(true) {
				System.out.printf("請選擇欲進行交易的帳戶編號：");
				String choose = s.nextLine();
				if(!choose.matches("^\\d+$")) {
					System.out.println("請輸入有效數字");
					continue;
				}
				int choice = Integer.valueOf(choose);
				if(choice>0 && choice<=count) {
					account = accounts.get(choice-1);
					break;
				} else {
					System.out.println("不存在此帳戶選項，請重新輸入");
				}
			}
		}
		
		if(account!=null) {
			while(true) {
				System.out.printf("進入一般存匯服務，請輸入欲存匯的幣別（1->台幣，2->外幣）：");
				String input = s.nextLine();
				switch(input) {
					case "1":
						TwTransaction(account);
						return;
					case "2":
						FxTransaction(account);
						return;
					default:
						System.out.println("不存在的操作選項，請重新輸入");
				}
			}
		}
		return;
	}
	
	/**
	 * 台幣一般存匯
	 * @author 990809
	 * 
	 */
	public void TwTransaction(AccountInfo account) {
		while(true) {
			System.out.printf("進入台幣的存匯服務，請輸入欲進行的項目：1->存款，2->提款：");
			String choice = s.nextLine();
			
			switch(choice) {
				case "1":
					while(true) {
						System.out.printf("請輸入欲存入的金額：");
						String input = s.nextLine();
						try {
							BigDecimal txAmt = new BigDecimal(input);
							BigDecimal temp = BankDepositAction.TwDeposit(abservice, account, txAmt);
							if(temp!=null) {
								System.out.println("交易成功");
								aservice.addAccountTransactionRecord(account, "TwDeposit", "TWD", null, txAmt, temp, null);
							} else {
								System.out.println("交易失敗");
							}
							return;
						} catch (NumberFormatException e) {
							System.out.println("請輸入數字");
		    			}
					}
				case "2":
					while(true) {
						System.out.printf("請輸入欲提出的金額：");
						String input = s.nextLine();
						try {
							BigDecimal txAmt = new BigDecimal(input);
							BigDecimal temp = BankWithdrawalAction.TwWithdrawal(abservice, account, txAmt);
							if(temp!=null) {
								System.out.println("交易成功");
								aservice.addAccountTransactionRecord(account, "TwWithdraw", "TWD", txAmt, null, temp, null);
							} else {
								System.out.println("交易失敗");
							}
							return;
						} catch (NumberFormatException e) {
							System.out.println("請輸入數字");
		    			}
					}
				default:
					System.out.println("不存在的操作選項，請重新輸入");
			}
		}
	}
	
	/**
	 * 外幣一般存匯
	 * @author 990809
	 * 
	 */
	public void FxTransaction(AccountInfo account) {
		System.out.printf("請輸入欲進行服務的幣別（若欲提款請確認您擁有此幣別）：");
		String inputcurrency = s.nextLine().toUpperCase();
		RateTable rate = new RateTable();
		boolean find = false;// 確認是否擁有此幣別
		if(rate.getAllRates().containsKey(inputcurrency)) {
			for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())) {
				if(inputcurrency.equals(ab.getId().getCurrency())) {
					find = true;
				}
			}
		} else {
			System.out.printf("不存在的幣別");
			return;
		}
		
		while(true) {
			System.out.printf("進入外幣的存匯服務，請輸入欲進行的項目：1->存款，2->提款：");
			String choice = s.nextLine();
			
			switch(choice) {
				case "1":
					while(true) {
						System.out.printf("請輸入欲存入的金額：");
						String input = s.nextLine();
						try {
							BigDecimal txAmt = new BigDecimal(input);
							BigDecimal temp = BankDepositAction.FxDeposit(abservice, account, inputcurrency, txAmt);
							if(temp!=null) {
								System.out.println("交易成功");
								aservice.addAccountTransactionRecord(account, "FxDeposit", inputcurrency, null, txAmt, temp, null);
								return;
							} else {
								System.out.println("交易失敗");
								return;
							}
						} catch (NumberFormatException e) {
							System.out.println("請輸入數字");
							return;
		    			}
					}
				case "2":
					while(find) {
						System.out.printf("請輸入欲提出的金額：");
						String input = s.nextLine();
						try {
							BigDecimal txAmt = new BigDecimal(input);
							BigDecimal temp = BankWithdrawalAction.FxWithdrawal(abservice, account, inputcurrency, txAmt);
							if(temp!=null) {
								System.out.println("交易成功");
								aservice.addAccountTransactionRecord(account, "FxWithdraw", inputcurrency, txAmt, null, temp, null);
								return;
							} else {
								System.out.println("交易失敗");
								return;
							}
						} catch (NumberFormatException e) {
							System.out.println("請輸入數字");
							return;
		    			}
					}
					System.out.println("您未擁有此幣別，無法進行提款");
					return;
				default:
					System.out.println("不存在的操作選項，請重新輸入");
			}
		}
	}
}
