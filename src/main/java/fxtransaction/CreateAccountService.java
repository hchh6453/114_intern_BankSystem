package fxtransaction;

import java.math.BigDecimal;
import java.util.Scanner;

import banksystem.entity.UserInfo;
import banksystem.service.UserService;

/**
 * 
 * 開戶服務
 * @author 990809
 * 
 */
public class CreateAccountService {
	private Scanner s;
	private UserService userservice;
	
	public CreateAccountService(Scanner s, UserInfo user, UserService service) {
		this.s = s;
		this.userservice = service;
		runCreateService(user);
	}
	
	public void runCreateService(UserInfo user) {
		boolean legal = false;
		if(user == null) {
			System.out.println("開始建立帳戶...");
			String name = "", id = "", password = "";
			boolean exist = false;
			while(!legal) {
				System.out.printf("請輸入身分證字號(開頭字母需為大寫)：");
				id = s.nextLine();
				if(ValidateIdUtil.validateId(id)) { // 驗證身分證字號
					legal = true;
				} else {
					System.out.println("請重新輸入有效的身分證字號");
				}
			}
			if(userservice.existuser(id)) {
				exist = true; // 檢查是否已有此身分證字號建立的帳戶
			}
			// 無此用戶存在
			if(!exist) {
				legal = false;
				// 設定姓名
				while(!legal) {
					System.out.printf("請輸入您的姓名：");
					name = s.nextLine();
					if(name.matches("^[^\\d]+$")) { // 名字不可為數字
						legal = true;
					} else {
						System.out.println("姓名不可為數字或空值，請重新輸入");
					}
				}
				legal = false;
				// 設定密碼
				while(!legal) {
					System.out.printf("請輸入密碼：");
					String temp = s.nextLine();
					if(temp=="") {
						System.out.println("密碼不可為空值，請重新輸入");
						continue;
					} else if(!temp.matches("^[a-zA-Z0-9]+$")) {
						System.out.println("密碼只能為數字或英文字母，請重新輸入");
						continue;
					} else if(temp.length()<6) {
						System.out.println("密碼至少需6碼，請重新輸入");
						continue;
					}
					System.out.printf("再次輸入密碼：");
					String check = s.nextLine();
					if(!temp.equals(check)) {
						System.out.println("密碼不一致，請重新設定密碼");
					} else {
						password = check;
						legal = true;
					}
				}
				userservice.addUser(id, name, password);
				user = userservice.getUser(id);
				Login.setLoginUser(user);
			} else {
				// 有此用戶但未登入
				int count = 3;
				System.out.printf("已有此身分證字號的用戶 ");
				while(true) {
					System.out.printf("請輸入密碼進行開戶：");
					password = s.nextLine();
					count--;
					if(userservice.validateUser(id, password)!=null) {
						System.out.println("以此用戶身份進行開戶");
						user = userservice.getUser(id);
						Login.setLoginUser(user);
						break;
					} else if(count>0){
						System.out.println("密碼錯誤，請重新輸入(剩餘" + count +"次）");
					} else {
						System.out.println("密碼錯誤已達3次，無法進行開戶");
						return;
					}
				}
			}
		} else {
			// 目前已有用戶登入
			System.out.println("目前的登入身份為 " + user.getName());
			System.out.printf("是否使用目前的用戶身份？（Y->繼續)：");
			String input = s.nextLine();
			// 可登出切換帳戶進行開戶
			if(!input.toUpperCase().equals("Y")) {
				Login.Logout();
				System.out.println("登出此用戶身份，請重新選擇開戶服務");
				return;
			}
			// 確認使用已登入的身份，輸入密碼驗證開戶
			String id = user.getID(), password = "";
			int count = 3;
			while(true) {
				System.out.printf("請輸入密碼進行開戶：");
				password = s.nextLine();
				count--;
				if(userservice.validateUser(id, password)!=null) {
					System.out.println("以此用戶身份進行開戶");
					break;
				} else if(count>0){
					System.out.println("密碼錯誤，請重新輸入(剩餘" + count +"次）");
				} else {
					System.out.println("密碼錯誤已達3次，無法進行開戶");
					return;
				}
			}
		}
		// 建立帳戶
		legal = false;
		String type = "";
		BigDecimal initbalance = BigDecimal.ZERO;
		while(!legal) {
			System.out.printf("請輸入欲建立的帳戶類型（TWD->台幣帳戶，FX->外幣帳戶)：");
			type = s.nextLine().toUpperCase();
			if(!type.equals("TWD") && !type.equals("FX")){
				System.out.println("不支援此帳戶類型，請重新輸入");
				continue;
			} else {
				legal = true;
			}
		}
		
		legal = false;
		String inputCurrency = "";
		while(!legal) {
			switch(type) {
				case "TWD":
					System.out.printf("建立台幣帳戶，請輸入初始資產：");
					String twtemp = s.nextLine();
					if(!twtemp.matches("^\\d+$")) {
						System.out.printf("請重新輸入有效數字（不可有小數或為負數）");
						continue;
					}
					initbalance = new BigDecimal(twtemp);
					legal = true;
					break;
				case "FX":
					System.out.printf("建立外幣帳戶，請輸入幣別：");
					inputCurrency = s.nextLine().toUpperCase();
			    	RateTable rate = new RateTable();
			    	boolean find = false;
			    	for(String currency : rate.getAllRates().keySet()) {
			    		if(inputCurrency.equals(currency)) {
			    			find = true; // 有對應幣別
			    			break;
			        	}
			    	}
			    	if(!find) {
			    		System.out.println("不存在的幣別，請重新輸入");
			    		continue;
			    	}
			    	while(find) {
			    		System.out.printf("建立" + rate.getRate(inputCurrency).getTranslation() + "帳戶，請輸入初始資產：");
				    	String fxtemp = s.nextLine();
						if(!fxtemp.matches("^\\d+\\.?\\d+$")) {
							System.out.println("請重新輸入有效（非負正數）數字");
							continue;
						}
						initbalance = new BigDecimal(fxtemp);
						if(initbalance.scale()>rate.getRate(inputCurrency).getScaleNums()) {
							System.out.println("無法建立小數位數不符該幣別規則的外幣帳戶，請重新輸入");
						} else {
							legal = true;
							break;
						}
			    	}
					break;
				default:
					System.out.println("未傳入正確帳戶類型參數，結束服務");
					break;
			}
		}
		if(legal) {
			String accountNum = userservice.addUserAccount(user, type, inputCurrency, initbalance);
			System.out.println("開戶成功！帳號為：" + accountNum);
		} else {
			System.out.println("建立帳戶失敗");
		}
	}
}
