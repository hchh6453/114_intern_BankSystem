package fxtransaction;

import java.util.Scanner;

import banksystem.entity.UserInfo;
import banksystem.service.UserService;

public class Login {
	private Scanner s;
	private static UserInfo user = null;
	private UserService userservice;
	
	public Login(Scanner s, UserService userservice) {
		this.s = s;
		this.userservice = userservice;
		runLogin();
	}
	
	public static void setLoginUser(UserInfo storeduser) {
		user = storeduser;
	}
	
	public static UserInfo getLoginUser() {
		return user;
	}
	
	// 執行登出
	public static void Logout() {
		user = null;
	}
	
	public void runLogin() {
		String id = "", password = "";
		boolean exist = false;
		while(true) {
			System.out.printf("請輸入您的身分證字號：");
			id = s.nextLine();
			if(ValidateIdUtil.validateId(id)) {
				break;
			}
			System.out.println("請重新輸入有效的身分證字號");
		}
		// System.out.println("uservice = " + userservice);
		if(userservice.existuser(id)) {
			exist = true; // 檢查是否已有此身分證字號建立的帳戶
		} else {
			System.out.println("目前無此用戶身份，請先進行開戶");
			return;
		}
		
		// 有對應的用戶
		int count = 3;
		while(exist && count>0) {
			System.out.printf("請輸入您的密碼：");
			password = s.nextLine();
			count--;
			user = userservice.validateUser(id, password);
			if(user!=null) {
				System.out.println("登入成功，歡迎 " + user.getName() + "!");
				return;
			} else if(count>=1) {
				System.out.println("密碼錯誤，請重新輸入(剩餘" + count +"次）");
			}
		}
		// 認證失敗
		System.out.println("登入失敗，無法執行此服務");
		return;
	}

}
