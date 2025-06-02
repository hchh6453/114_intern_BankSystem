package fxtransaction;

import java.util.ArrayList;
import java.util.Scanner;

import banksystem.entity.AccountInfo;
import banksystem.entity.UserInfo;
import banksystem.entity.AccountBalance;
import banksystem.service.AccountBalanceService;
import banksystem.service.AccountService;
import banksystem.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * 即期匯率服務：1. 查詢 2. 買入 3. 賣出
 * @author 990809
 * 
 */
public class FxExchangeService {
	private Scanner s;
	private UserService uservice;
	private AccountService aservice;
	private AccountBalanceService abservice;
	
	public FxExchangeService(Scanner s, UserInfo user, UserService uservice, AccountService aservice, AccountBalanceService abservice) {
		this.s = s;
		this.uservice = uservice;
		this.aservice = aservice;
		this.abservice = abservice;
		runFxExchangeService(user);
	}
	public void runFxExchangeService(UserInfo user) {
		while(true) {
			System.out.printf("進入即期匯率服務，請輸入需要的服務（1->查詢即期匯率，2->買入外幣，3->賣出外幣）：");
			String input = s.nextLine();
			UserInfo loginuser = null;
			switch(input) {
				case "1":
					FxExchangeChecker();
					return;
				case "2":
					// 目前無用戶登入資訊
					if(user == null) {
						System.out.println("買入外幣需進行登入");
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
						System.out.println("即將進入買入外幣服務...");
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
								AccountInfo selected = accounts.get(choice-1);
								FxBuyIn(selected);
								return;
							} else {
								System.out.println("不存在此帳戶選項，請重新輸入");
							}
						}
					}
				case "3":
					// 目前無用戶登入資訊
					if(user == null) {
						System.out.println("賣出外幣需進行登入");
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
						System.out.println("即將進入賣出外幣服務...");
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
								AccountInfo selected = accounts.get(choice-1);
								FxSellOut(selected);
								return;
							} else {
								System.out.println("不存在此帳戶選項，請重新輸入");
							}
						}
					}
				default:
					System.out.println("不存在的操作選項，請重新輸入");
			}
		}
	}
	
	/**
	 * 查詢對應幣別的即期匯率
	 * @author 990809
	 * 
	 */
    public void FxExchangeChecker(){
    	System.out.printf("輸入欲查詢的幣別：");
    	String inputCurrency = s.nextLine().toUpperCase();
    	RateTable rate = new RateTable(); // 進入查詢更新匯率
    	
    	while(true) {
    		for(String currency : rate.getAllRates().keySet()) {
        		if(inputCurrency.equals(currency)) {
        			System.out.println("您輸入的幣別為" + inputCurrency + "，買入價格：" + rate.getRate(currency).getBuyPrice() + "；賣出價格：" + rate.getRate(currency).getSellPrice());
        			return;
            	}
        	}
        	System.out.println("不存在的幣別，請重新輸入");
    	}
    }
    
    /**
     * 買入對應外幣
     * @author 990809
     * 
     */
    public void FxBuyIn(AccountInfo account) {
    	System.out.printf("輸入欲買入的幣別：");
    	String inputCurrency = s.nextLine().toUpperCase();
    	BigDecimal buyin = BigDecimal.ZERO;
    	RateTable rate = new RateTable(); // 進入交易更新匯率
    	
    	boolean find = false, legaltxAmt = false;
    	for(String currency : rate.getAllRates().keySet()) {
    		if(inputCurrency.equals(currency)) {
    			find = true; // 有對應幣別
    			System.out.printf("您輸入的幣別為" + inputCurrency + "，目前的交易匯率為 " + rate.getRate(inputCurrency).getSellPrice() + "，請輸入欲購買外幣數額：");
    			String input = s.nextLine();
    			try {
    				buyin = new BigDecimal(input);
        			if(buyin.compareTo(BigDecimal.ZERO)>=0 && buyin.scale()<=rate.getRate(inputCurrency).getScaleNums()) {
        				legaltxAmt = true;
        			}
    			} catch(NumberFormatException e) {
    				
    			}
    			break;
        	}
    	}
    	if(!find) {
    		System.out.println("不存在的幣別");
    	} else if(!legaltxAmt){
    		System.out.println("無法購買負數或超過小數位數的外幣");
    	} else {
    		BigDecimal TwPay = BigDecimal.ZERO;
    		TwPay = rate.getRate(inputCurrency).getSellPrice().multiply(buyin).setScale(0, RoundingMode.HALF_UP); // 買入外幣用賣出價格、四捨五入
    		try {
    			BigDecimal tempbalance = FxExchangeAction.FxBuyIn(abservice, account, inputCurrency, TwPay, buyin); 
        		if(tempbalance!=null) {// 整筆外幣交易成功才紀錄
        			aservice.addAccountTransactionRecord(account, "BuyInFx", inputCurrency, TwPay, buyin, tempbalance, rate);
        			System.out.println("交易成功");
        		} 
    		} catch (Exception e) {
    			System.out.println("交易失敗");
    		}
    	}
    }
    
    /**
     * 賣出對應外幣
     * @author 990809
     * 
     */
    public void FxSellOut(AccountInfo account) {
    	System.out.printf("輸入欲賣出的幣別：");
    	String inputCurrency = s.nextLine().toUpperCase();
    	BigDecimal sellout = BigDecimal.ZERO;
    	RateTable rate = new RateTable(); // 進入交易更新匯率
    	
    	boolean find = false, legaltxAmt = false;
    	for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())) {
    		if(inputCurrency.equals(ab.getId().getCurrency())) {
    			find = true; // 有對應幣別
    			System.out.printf("您輸入的幣別為" + inputCurrency + "，目前的交易匯率為 " + rate.getRate(inputCurrency).getBuyPrice()  + "，請輸入欲賣出外幣數額：");
    			String input = s.nextLine();
    			try {
    				sellout = new BigDecimal(input);
        			if(sellout.compareTo(BigDecimal.ZERO)>=0 && sellout.scale()<=rate.getRate(inputCurrency).getScaleNums()) {
        				legaltxAmt = true;
        			}
    			} catch (NumberFormatException e) {
    				
    			}
    			break;
        	}
    	}
    	if(!find) {
    		System.out.println("您未持有此外幣或為不存在的幣別");
    	} else if(!legaltxAmt){
    		System.out.println("無法賣出負數或超過小數位數的外幣");
    	} else {
    		BigDecimal TwIn = BigDecimal.ZERO;
    		TwIn = rate.getRate(inputCurrency).getBuyPrice().multiply(sellout).setScale(0, RoundingMode.HALF_UP); // 賣出外幣用買入價格、四捨五入
    		try {
    			BigDecimal tempbalance = FxExchangeAction.FxSellOut(abservice, account, inputCurrency, sellout, TwIn); 
        		if(tempbalance!=null) {// 整筆外幣交易成功才紀錄
        			aservice.addAccountTransactionRecord(account, "BuyInFx", inputCurrency, sellout, TwIn, tempbalance, rate);
        			System.out.println("交易成功");
        		} 
    		} catch (Exception e) {
    			System.out.println("交易失敗");
    		}
    	}
    }
}
