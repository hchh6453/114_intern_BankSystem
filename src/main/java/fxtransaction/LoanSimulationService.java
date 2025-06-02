package fxtransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * 
 * 貸款試算服務
 * @author 990809
 * 
 */
public class LoanSimulationService {
	private Scanner s;
	
	public LoanSimulationService(Scanner s) {
		this.s = s;
		runLoanSimulation();
	}
	
	public void runLoanSimulation() {
		System.out.println("進入貸款試算服務，請分別輸入貸款金額、年數、年利率進行試算");
		BigDecimal loan = BigDecimal.ZERO, interest = BigDecimal.ZERO;
		int year = 0;
		while(true) {
			System.out.printf("請輸入貸款金額：");
			String tempmoney = s.nextLine();
			if(tempmoney.length()>0 && tempmoney.matches("^\\d+$")) {
				loan = new BigDecimal(tempmoney);
				if(loan.compareTo(BigDecimal.ZERO)>0) {
					break;
				}
			}
			System.out.println("請重新輸入有效貸款金額（非負正整數）");
		}
		while(true) {
			System.out.printf("請輸入貸款年數：");
			String tempyear = s.nextLine();
			if(tempyear.length()>0 && tempyear.matches("^\\d+$")) {
				year = Integer.valueOf(tempyear);
				if(year>0) {
					break;
				}
			}
			System.out.println("請重新輸入有效貸款年數（非負正整數）");
		}
		while(true) {
			System.out.printf("請輸入年利率：");
			String tempinterest = s.nextLine();
			if((tempinterest.length()>0) && (tempinterest.matches("^\\d+\\.?\\d+$") || tempinterest.matches("^\\d+$"))) {
				interest = new BigDecimal(tempinterest);
				break;
			}
			System.out.println("請重新輸入有效年利率（非負數）");
		}
		
		BigDecimal monthly = MonthlyLoan(loan, year, interest);
		BigDecimal total = monthly.multiply(new BigDecimal(12)).multiply(new BigDecimal(year));
		DecimalFormat numformatter = new DecimalFormat("#,###");// 每三位逗點
		System.out.println("-------------------------");
		System.out.println("每月應繳金額：" + numformatter.format(monthly) + "元");
		System.out.println("總還款金額：" + numformatter.format(total) + "元");
		System.out.println("總利息金額：" + numformatter.format(total.subtract(loan)) + "元");
		System.out.println("-------------------------");
	}
	
	/** 貸款每月應繳金額 */
	public static BigDecimal MonthlyLoan(BigDecimal loan, int year, BigDecimal interest) {
		BigDecimal monthly = loan; // 回傳每個月應付貸款
		// 使用本息平均攤還計算
		BigDecimal monthrate = interest.divide(new BigDecimal("100")).divide(new BigDecimal(12), 10, RoundingMode.HALF_UP);
		BigDecimal temp = BigDecimal.ONE;// 求(1+r)的n次方
		for(int i=1; i<=year*12; i++) {
			temp = temp.multiply(BigDecimal.ONE.add(monthrate));
		}
		monthly = loan.multiply(monthrate.multiply(temp)).divide(temp.subtract(BigDecimal.ONE), 0, RoundingMode.HALF_UP);
		return monthly;
	}
}
