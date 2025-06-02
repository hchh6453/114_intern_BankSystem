package banksystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import banksystem.entity.AccountInfo;

/**
 * 連結 AccountInfo 和資料庫的控制層，會自動生成 SQL 語法操作資料庫
 * 
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountInfo, Integer> {
	// 裡面有save(), findById(), findAll(), deleteById(), existsById()
	
	// 因為帳號也是獨立的，可以透過帳號查找，但底層主鍵還是accountid
	Optional<AccountInfo> findByAccountNum(String accountNum);
	
	// 確認是否有已存在的帳號
	boolean existsByAccountNum(String accountNum);
}
