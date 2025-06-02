package banksystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import banksystem.entity.UserInfo;
/**
 * 連結 UserInfo 和資料庫的控制層，會自動生成 SQL 語法操作資料庫
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
	// 裡面有save(), findById(), findAll(), deleteById(), existsById()
}
