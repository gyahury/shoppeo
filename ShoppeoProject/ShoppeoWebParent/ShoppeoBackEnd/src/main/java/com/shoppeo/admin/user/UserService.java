package com.shoppeo.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shoppeo.common.entity.Role;
import com.shoppeo.common.entity.User;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return (List<User>) userRepo.findAll();
	}
	
	public List<Role> listRoles(){
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		boolean isUpdatingUser = (user.getId() != null); //회원 수정
		
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get(); // 수정하는 계정
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword()); // 기존회원이 비밀번호값 입려하지 않고 그대로 저장시 값은 존재하는 그대로
			} else {
				encodePassword(user); //패스워드가 비어있지 않은 경우(수정값을 입력한 경우) 암호화해서 저장
			}
		}else {
			encodePassword(user); // 신규 가입이면 패스워드 빈값이 될 수 없으니 바로 암호화
		}
		
		
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true; // 이메일이 비어있지 않으면(수정 기능이면) 유니크 체크를 따로 하지 않겠다.
		boolean isCreatingNew = ( id == null ); // id값이 비어있을 경우는 신규 가입인 상황이다.
		if(isCreatingNew) {
			if(userByEmail != null) return false; // 신규가입인 상황에서 email값이 이미 존재하면 false 리턴
		} else {    							  // 회원 수정의 경우	
			if(userByEmail.getId() != id) return false; // 파라미터의 id와 입력돼있는 현재 이메일에서 가져온 id가 다르면 false
		}
		
		return true;
	}

	
	public User get(Integer id) throws UserNotFoundException {
		try {
		return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		userRepo.deleteById(id);
		
	}
	
	public void updateUserEnabledStatus(Integer id , boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
		
	}
	
	
	}

