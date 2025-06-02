package fxtransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import banksystem.entity.AccountInfo;
import banksystem.entity.UserInfo;
import banksystem.entity.AccountBalance;
import banksystem.service.AccountBalanceService;
import banksystem.service.UserService;

/**
 * 
 * 總資產服務
 * @author 990809
 * 
 */
public class TotalAssetService {
	private Scanner s;
	private UserService uservice;
	private AccountBalanceService abservice;
	
	public TotalAssetService(Scanner s, UserInfo user, UserService uservice, AccountBalanceService abservice) {
		this.s = s;
		this.abservice = abservice;
		this.uservice = uservice;
		runTotalAssetService(user);
	}
	
	public void runTotalAssetService(UserInfo user) {
		UserInfo loginuser = null;
		// 目前無用戶登入資訊
		if(user == null) {
			System.out.println("總資產服務需進行登入");
			System.out.printf("您已有帳戶進行登入？（Y->進行登入，若無請輸入任意字元跳出進行開戶）：");
			String input = s.nextLine();
			if(!input.toUpperCase().equals("Y")) {
				return;
			}
			// System.out.println("uservice = " + uservice);
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
			System.out.println("即將進入總資產服務...");
			System.out.printf("是否使用目前的用戶身份？（Y->繼續)：");
			String input = s.nextLine();
			if(!input.toUpperCase().equals("Y")) {
				Login.Logout();
				System.out.println("登出此用戶身份，重新進入登入流程...");
				System.out.printf("您已有帳戶進行登入？（Y->進行登入，若無請輸入任意字元跳出進行開戶）：");
				input = s.nextLine();
				if(!input.toUpperCase().equals("Y")) {
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
				System.out.printf("請選擇欲查詢資產的帳戶編號：");
				String choose = s.nextLine();
				if(!choose.matches("^\\d+$")) {
					System.out.println("請輸入有效數字");
					continue;
				}
				int choice = Integer.valueOf(choose);
				if(choice>0 && choice<=count) {
					AccountInfo selected = accounts.get(choice-1);
					TotalAsset(selected);
					return;
				} else {
					System.out.println("不存在此帳戶選項，請重新輸入");
				}
			}
		}
	}
	
	public void TotalAsset(AccountInfo account) {
		RateTable rate = new RateTable();// 換算總資產當下匯率
		DecimalFormat numformatter = new DecimalFormat("#,###.####");// 每三位逗點
		System.out.println("-------------------------");
		// 初始設置為台幣或外幣帳戶分別顯示
		System.out.println(account.getCurrency().equals("TWD") ? "您的初始資產：TWD " + numformatter.format(account.getInitBalance()) : "您的初始資產：" + account.getCurrency() + " " + numformatter.format(account.getInitBalance()));
		
		for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())) {
			if (ab.getId().getCurrency().equals("TWD")) {
				System.out.println("目前持有台幣：TWD " + numformatter.format(ab.getBalance().setScale(0, RoundingMode.HALF_UP)));
			}
			else if(!ab.getBalance().equals(BigDecimal.ZERO)) {
				System.out.println("目前持有" + rate.getRate(ab.getId().getCurrency()).getTranslation() + "：" + ab.getId().getCurrency() + " " + numformatter.format(ab.getBalance()));
			}
		}
		System.out.printf("目前換算匯率："); // 外幣轉換成台幣看待 -> 賣出外幣 -> 看買入價格
		for(AccountBalance ab : abservice.getAllBalanceByaccountid(account.getId())) {
			if(!ab.getBalance().equals(BigDecimal.ZERO) && !ab.getId().getCurrency().equals("TWD")) {
				System.out.printf(ab.getId().getCurrency() + "/TWD = " + rate.getRate(ab.getId().getCurrency()).getBuyPrice() + " ");
			}
		}
		System.out.println("\n目前總資產為：TWD " + numformatter.format(account.getTotal(account, abservice, rate).setScale(0, RoundingMode.HALF_UP)));
		System.out.println("-------------------------");
	}
}
