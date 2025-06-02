package fxtransaction;

import java.math.BigDecimal;
import java.security.SecureRandom; // 確保隨機性高(相對Random)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import banksystem.entity.AccountInfo;
import banksystem.service.AccountService;

/**
 * 帳戶工廠
 * 
 * @author 990809
 * 生成隨機帳號，同時將新建立的帳戶加入資料庫
 */
@Component
public class AccountFactory {
	@Autowired
	private AccountService aservice;
	
	public Account createAccount(String type, String currency, BigDecimal initbalance) {
		String accountNum = "";
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();
		switch(type.toUpperCase()) {
			case "TWD":
				// 隨機生成14碼，第5、6碼不為22
				while(true) {
					sb.setLength(0);
					for(int i=0; i<14; i++) {
						sb.append(random.nextInt(10));
					}
					if(!sb.substring(5, 7).equals("22") && !aservice.existAccountNum(sb.toString())) { // 帳號不重複
						accountNum = sb.toString();
						break;
					}
				}
				AccountInfo twaccount = new AccountInfo("TWD", accountNum, initbalance);
				return new TwAccountInfo(twaccount);
			case "FX":
				// 隨機生成14碼，第5、6碼為22
				while(true) {
					sb.setLength(0);
					for(int i=0; i<4; i++) {
						sb.append(random.nextInt(10));
					}
					sb.append("22");
					for(int i=0; i<8; i++) {
						sb.append(random.nextInt(10));
					}
					if(!aservice.existAccountNum(sb.toString())) { // 帳號不重複
						accountNum = sb.toString();
						break;
					}
				}
				AccountInfo fxaccount = new AccountInfo(currency, accountNum, initbalance);
				return new FxAccountInfo(fxaccount);
			default:
				return null;
		}
	}
}
