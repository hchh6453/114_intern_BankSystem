package banksystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import banksystem.entity.AccountBalance;
import banksystem.entity.AccountBalanceId;



/**
 * 連結 AccountBalance 和資料庫的控制層
 * 
 */
@Repository
public interface AccountBalanceRepository  extends JpaRepository<AccountBalance, AccountBalanceId>{
	// 裡面有save(), findById(), findAll(), deleteById(), existsById()
	
	// 因為是複合主鍵，若要找某帳戶底下的所有餘額，需要找id.accountid
	List<AccountBalance> findByAccount_Id(int accountid);
}
