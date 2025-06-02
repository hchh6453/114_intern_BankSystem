package fxtransaction;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import banksystem.entity.UserInfo;
import banksystem.service.*;

/**
 * 
 * 所有服務入口
 * （除了開戶和查詢即期匯率和貸款試算服務其餘皆需使用身分證字號及密碼進行登入）
 * 1. 開戶服務
 * 2. 即期匯率服務
 * 3. 總資產服務
 * 4. 交易紀錄服務
 * 5. 一般存匯服務
 * 6. 貸款試算服務
 * @author 990809
 * 
 */
public class Service{
	private Scanner s;
	@Autowired
	private ServiceContainer container;
	
	public Service(Scanner s, ServiceContainer container) {
		this.s = s;
		this.container = container;
		runService();
	}
	
	public void runService() {
		while(true) {
			// 進入服務抓取目前登入的使用者身份
			UserInfo user = Login.getLoginUser();
			
			System.out.printf("請輸入欲進行的服務選項（1->開戶服務，2->即期匯率服務，3->總資產服務，4->交易紀錄服務，5->一般存匯，6->貸款試算服務）：");
			String choice = s.nextLine();
			
			switch(choice) {
				case "1":
					new CreateAccountService(s, user, container.getUserService());
					break;
				case "2":
					new FxExchangeService(s, user, container.getUserService(), container.getAccountService(), container.getAccountBalanceService());
					break;
				case "3":
					new TotalAssetService(s, user, container.getUserService(), container.getAccountBalanceService());
					break;
				case "4":
					new TotalTransactionRecordService(s, user, container.getUserService(), container.getTransactionrecordService());
					break;
				case "5":
					new GeneralService(s, user, container.getUserService(), container.getAccountService(), container.getAccountBalanceService());
					break;
				case "6":
					new LoanSimulationService(s);
					break;
				default:
					System.out.println("不存在的服務選項");
			}
			System.out.printf("是否繼續進行服務？（Y->繼續）：");
			String Continue = s.nextLine().toUpperCase();
			
			if(Continue.equals("Y")) {
				continue;
			}
			else {
				System.out.println("服務結束");
				break;
			}
		}
	}
}
