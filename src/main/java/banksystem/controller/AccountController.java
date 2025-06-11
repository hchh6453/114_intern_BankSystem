package banksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import banksystem.dto.CreateAccountRequest;
import banksystem.service.CreateAccountService;

/**
 * 
 * 	創建帳戶的 Web API 接口
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	@Autowired
	private CreateAccountService createaccountservice;
	
	@PostMapping("/create")
	public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request){
		try {
            String accountNum = createaccountservice.create(request);
            return ResponseEntity.ok("開戶成功！帳號為：" + accountNum);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}
}
