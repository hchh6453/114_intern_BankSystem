package banksystem.service;

import org.springframework.transaction.annotation.*;
import org.hibernate.Hibernate;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.util.Optional;

import banksystem.entity.AccountInfo;
import banksystem.entity.UserInfo;
import banksystem.repository.UserRepository;
import fxtransaction.Account;
import fxtransaction.AccountFactory;

/*
 * AP 內容，針對 users 資料庫的操作寫在這裡
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private AccountFactory factory;
	
	@Autowired
	private AccountService aservice;
	
	/** 接上與資料庫的接口 (建構子注入，官方推薦）
	public UserService(UserRepository userrepo) {
		this.userrepo = userrepo;
	}*/
	
	/** 檢查是否存在此使用者 */
	public boolean existuser(String userid) {
		return userrepo.existsById(userid);
	}
	
	/** 取得單個使用者 */
	public UserInfo getUser(String id) {
		Optional<UserInfo> temp = userrepo.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}
	
	/** 驗證身份 */
	public UserInfo validateUser(String id, String password) {
		Optional<UserInfo> temp = userrepo.findById(id);
		if(!temp.isEmpty()) {
			UserInfo user = temp.get();
			if(BCrypt.checkpw(password, user.getHashedPassword())) {
				return user;
			} else {
				return null;
			}
		} else {
			System.out.println("資料庫中不存在此id");
			return null;
		}
	}
	
	/** 初始化使用者帳戶列表 */
	@Transactional
	public UserInfo initAccountList(UserInfo user) {
		UserInfo managedUser = userrepo.findById(user.getID()).orElse(null); // 從 DB 拿回 "連線中的" 實體
		Hibernate.initialize(managedUser.getAccounts()); // 初始化
		return managedUser;
	}
	
	/** 新增使用者的帳戶 */
	@Transactional
	public String addUserAccount(UserInfo user, String type, String currency, BigDecimal initbalance) {
		try {
			// 再從資料庫中抓取一次 UserInfo 以免出現 Detached entity
			UserInfo manageduser = userrepo.findById(user.getID()).get();
			Account account = factory.createAccount(type, currency, initbalance);// 帳戶工廠建立帳戶
			manageduser.addAccounts(account.getAccountInfo());// 建立雙向關聯
			
			AccountInfo saveda = aservice.addAccount(account.getAccountInfo()); // 建立帳戶
			userrepo.save(manageduser); // 儲存該使用者的帳戶
			return saveda.getFormatAccountNum();
		} catch (Exception e) {
			throw new RuntimeException("使用者新增帳戶失敗", e);
		}
	}
	
	/** 新增使用者 */
	@Transactional
	public boolean addUser(String userid, String username, String password) {
		// 新增的時候再檢查一次沒有此身分證字號用戶存在
		if(existuser(userid)) {
			return false;
		} else {
			UserInfo user = new UserInfo(username, userid, password);
			userrepo.save(user);
			return true;
		}
	}
	
}
