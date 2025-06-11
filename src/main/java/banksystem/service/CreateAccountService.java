package banksystem.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import banksystem.dto.CreateAccountRequest;
import banksystem.entity.UserInfo;
import fxtransaction.ValidateIdUtil;

/** 
 * 
 * 創建帳戶的 Web 業務流程
 */
@Service
public class CreateAccountService {
	@Autowired
    private UserService userservice;

    public String create(CreateAccountRequest req) {
        // 驗證身分證、密碼格式、幣別、金額等
        if (!ValidateIdUtil.validateId(req.getId())) {
            throw new IllegalArgumentException("身分證格式錯誤");
        }

        if (userservice.existuser(req.getId())) {
            UserInfo user = userservice.validateUser(req.getId(), req.getPassword());
            if (user == null) throw new IllegalArgumentException("已有帳號但密碼錯誤");
        } else {
            // 新使用者建立
            if (!req.getPassword().equals(req.getConfirmPassword())) {
                throw new IllegalArgumentException("密碼不一致");
            }
            userservice.addUser(req.getId(), req.getName(), req.getPassword());
        }

        BigDecimal init = new BigDecimal(req.getInitBalance());
        return userservice.addUserAccount(userservice.getUser(req.getId()), req.getAccountType(), req.getCurrency(), init);
    }
}
