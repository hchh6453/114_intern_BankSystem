package banksystem.dto;


/**
 * 
 * POST 建立一個新使用者及帳戶
 * Web 傳入建立帳戶所需的格式內容
 */
public class CreateAccountRequest {
	/* 姓名 */
	private String name;
	/* 身分證字號 */
    private String id;
    /* 密碼 */
    private String password;
    /* 確認密碼 */
    private String confirmPassword;
    /* 建立的帳戶類型 */
    private String accountType;
    /* 帳戶幣別 */
    private String currency;
    /* 初始餘額 */
    private String initBalance;
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return this.id;
    }
    
    public void setPassword(String pw) {
    	this.password = pw;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void setInitBalance(String init) {
    	this.initBalance = init;
    }
    
    public String getInitBalance() {
    	return this.initBalance;
    }
    
    public void setConfirmPassword(String confirm) {
    	this.confirmPassword = confirm;
    }
    
    public String getConfirmPassword() {
    	return this.confirmPassword;
    }
    
    public void setAccountType(String type) {
    	this.accountType = type;
    }
    
    public String getAccountType() {
    	return this.accountType;
    }
    
    public void setCurrency(String cur) {
    	this.currency = cur;
    }
    
    public String getCurrency() {
    	return this.currency;
    }
}
