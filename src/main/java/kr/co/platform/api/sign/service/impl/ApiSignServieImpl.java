package kr.co.platform.api.sign.service.impl;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.platform.util.advice.exception.Code700Exception;
import kr.co.platform.util.advice.exception.ForbiddenException;
import kr.co.platform.api.sign.dto.Member;
import kr.co.platform.api.sign.dao.ApiSignDAO;
import kr.co.platform.api.sign.service.ApiSignService;
import kr.co.platform.util.common.IsEmpty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("apiSignService")
public class ApiSignServieImpl implements ApiSignService {

    private ApiSignDAO apiSignDao;
    
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Member loginUserProcessService(Member login) throws Exception {
		Member result = apiSignDao.selectUserInfoById(login);
		if (IsEmpty.check(result)) {
			throw new Code700Exception("There is no Result Data");
		}
		
		if (!passwordEncoder.matches(login.getMemberPassword(), result.getMemberPassword())) {
			throw new ForbiddenException("Passwords do not match");
		}
		return result;
	}
	
    @Override
    public boolean insertUserInfo(Member info) {
    	info.setMemberPassword(passwordEncoder.encode(info.getMemberPassword()));		
		if(IsEmpty.check(apiSignDao.insertUserInfo(info))) 
			throw new Code700Exception("The query was executed normally, but not a single data was affected");

		return true;
    }

}


