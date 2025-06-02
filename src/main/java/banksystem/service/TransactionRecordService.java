package banksystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banksystem.entity.TransactionRecord;
import banksystem.repository.TransactionRecordRepository;

/**
 * 針對交易紀錄的資料庫操作
 * 
 */
@Service
public class TransactionRecordService {
	@Autowired
	private TransactionRecordRepository trrepo;
	
	/** 儲存交易紀錄 */
	@Transactional
	public TransactionRecord addTransactionRecord(TransactionRecord tr) {
		try {
			TransactionRecord record = trrepo.save(tr);
			return record;
		} catch (Exception e) {
			throw new RuntimeException("設置交易紀錄失敗", e);
		}
	}
	
	/** 取得單筆交易紀錄 */
	public TransactionRecord getRecord(int id){
		Optional<TransactionRecord> temp = trrepo.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}
	
	/** 取得某帳戶的所有交易紀錄 */
	public String getAllRecord(int accountid) {
		List<TransactionRecord> trs = trrepo.findByAccount_Id(accountid);
		if(trs.isEmpty()) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			for(TransactionRecord tr : trs) {
				sb.append(tr.getRecord());
			}
			return sb.toString();
		}
	}
}
