package banksystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import banksystem.entity.TransactionRecord;

public interface TransactionRecordRepository  extends JpaRepository<TransactionRecord, Integer> {
	// 裡面有save(), findById(), findAll(), deleteById(), existsById()
	
	List<TransactionRecord> findByAccount_Id(int accountid); 
}
