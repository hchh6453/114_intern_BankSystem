package banksystem.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.mindrot.jbcrypt.BCrypt;
/**
 * 
 * 使用者基本資料
 * @author 990809
 */
@Entity
@Table(name = "users")
public class UserInfo {
	/** 姓名 */
	@Column(name = "user_name")
	private String name;
	
	/** 身分證字號 */
	@Id
	@Column(name = "user_id")
	private String id;
	
	/** 密碼 */
	@Column(name = "password")
	private String password;
	
	/** 擁有的帳戶 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AccountInfo> accounts; // 對應 AccountInfo 裡的 user
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt()); // 雜湊儲存密碼
	}
	
	public String getHashedPassword() {
		return this.password;
	}
	
	/** 新增帳戶 */
	public void addAccounts(AccountInfo account) {
		this.accounts.add(account);
		account.setUser(this); // 設定雙向關聯
	}
	
	public List<AccountInfo> getAccounts(){
		return this.accounts;
	}
	
	/** JPA要求的預設建構子 */
	public UserInfo() {}
	
	/** 使用者初始資料建置 */
	public UserInfo(String name, String id, String password) {
		setID(id);
		setName(name);
		setPassword(password);
		this.accounts = new ArrayList<>();
	}
}
