package fxtransaction;

import java.util.ArrayList;
import java.util.Scanner;

import banksystem.entity.AccountInfo;
import banksystem.entity.UserInfo;
import banksystem.service.TransactionRecordService;
import banksystem.service.UserService;

/**
 * 
 * 交易紀錄服務
 * @author 990809
 * 
 */
public class TotalTransactionRecordService {
	private Scanner s;
	private UserService uservice;
	private TransactionRecordService trservice;
	
	public TotalTransactionRecordService(Scanner s, UserInfo user, UserService uservice, TransactionRecordService trservice) {
		this.s = s;
		this.uservice = uservice;
		this.trservice = trservice;
		runTransactionRecord(user);
	}
	public void runTransactionRecord(UserInfo user) {
		UserInfo loginuser = null;
		// 目前無用戶登入資訊
		if(user == null) {
			System.out.println("交易紀錄服務需進行登入");
			System.out.printf("您已有帳戶進行登入？（Y->進行登入，若無請輸入任意字元跳出進行開戶）：");
			String input = s.nextLine();
			if(!input.toUpperCase().equals("Y")) {
				return;
			}
			new Login(s, uservice);
			loginuser = Login.getLoginUser();
			if(loginuser == null) {
				return;
			} else {
				user = loginuser; // 在這裡登入
			}
		}
		// 目前已有用戶登入 
		System.out.println("目前的登入身份為 " + user.getName());
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
				System.out.printf("請選擇欲查詢交易紀錄的帳戶編號：");
				String choose = s.nextLine();
				if(!choose.matches("^\\d+$")) {
					System.out.println("請輸入有效數字");
					continue;
				}
				int choice = Integer.valueOf(choose);
				if(choice>0 && choice<=count) {
					AccountInfo selected = accounts.get(choice-1);
					TransactionRecord(selected);
					return;
				} else {
					System.out.println("不存在此帳戶選項，請重新輸入");
				}
			}
		}
	}
	public void TransactionRecord(AccountInfo account) {
		if(trservice.getAllRecord(account.getId()).equals("")) {
			System.out.println("無交易明細");
			return;
		}
		System.out.println("-------------------------");
		System.out.printf("交易明細列表：\n" + trservice.getAllRecord(account.getId()));
		System.out.println("-------------------------");
	}
}
