package com.shoppeo.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppeo.common.entity.Role;
import com.shoppeo.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		
		User userKH = new User("rudgns1243@naver.com","rud1243","jeong kyunghoon");
		
		userKH.addRole(roleAdmin);
		
		User savedUser = repo.save(userKH);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserTwoRoles() {
		
		User userWang = new User("kws728@naver.com","kws728","kang wangsun");
		
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userWang.addRole(roleEditor);
		userWang.addRole(roleAssistant);
		
		User savedUser = repo.save(userWang);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userWang = repo.findById(2).get();
		System.out.println(userWang);
		assertThat(userWang).isNotNull();
		
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userKyung = repo.findById(1).get();
		userKyung.setEnabled(true);
		userKyung.setEmail("ddddd@gmail.com");
		
		repo.save(userKyung);
		
	}
	
	@Test
	public void testUpdateUserRole() {
		User userWang = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userWang.getRole().remove(roleEditor);
		userWang.addRole(roleSalesperson);
		
		repo.save(userWang);
		
	}
	
	@Test
	public void testDeleteUser() {
		int userId = 2 ;
		repo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "rudgns1243@naver.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountByID() {
		Integer id = 1;
		Long countbyid = repo.countById(id);
		
		assertThat(countbyid).isEqualTo(1);
		assertThat(countbyid).isNotNull().isGreaterThan(0);
	}
	
}
