package fxtransaction;

import java.util.HashMap;

public class ValidateIdUtil {
	private static final HashMap<String, String> transfer = new HashMap<>();
	
	/** 身分證字號字母對照表 */
	static {
		transfer.put("A", "10");
		transfer.put("B", "11");
		transfer.put("C", "12");
		transfer.put("D", "13");
		transfer.put("E", "14");
		transfer.put("F", "15");
		transfer.put("G", "16");
		transfer.put("H", "17");
		transfer.put("I", "34");
		transfer.put("J", "18");
		transfer.put("K", "19");
		transfer.put("L", "20");
		transfer.put("M", "21");
		transfer.put("N", "22");
		transfer.put("O", "35");
		transfer.put("P", "23");
		transfer.put("Q", "24");
		transfer.put("R", "25");
		transfer.put("S", "26");
		transfer.put("T", "27");
		transfer.put("U", "28");
		transfer.put("V", "29");
		transfer.put("W", "32");
		transfer.put("X", "30");
		transfer.put("Y", "31");
		transfer.put("Z", "33");
	}
	
	/** 驗證有效身分證字號 */
	public static boolean validateId(String id) {
		// 身分證字號需為10碼，開頭為大寫英文字母，後面9碼為數字，第一碼數字為1或2
		if(id.length()!=10 || !(id.charAt(0)>='A' && id.charAt(0)<='Z') || !(id.substring(1, 10).matches("^\\d+$")) ||(id.charAt(1)!='1' && id.charAt(1)!='2')) {
			return false;
		}
		String num = transfer.get(id.substring(0, 1));
		int check = 0;
		// 權重為1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1
		for(int i=0; i<10; i++) {
			if(i==0) {
				check += Integer.valueOf(Character.toString(num.charAt(0)));
				check += (Integer.valueOf(Character.toString(num.charAt(1)))*9);
			} else if(i==9) {
				check += Integer.valueOf(Character.toString(id.charAt(i)));
			} else {
				check += (Integer.valueOf(Character.toString(id.charAt(i)))*(9-i));
			}
		}
		// System.out.println(check);
		if(check%10==0) {
			return true;
		}
		return false;
	}
}
