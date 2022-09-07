package com.shoppeo.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoppeo.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;

	@Test
	public void testCreatefirstRole() {
		Role roleAdmin = new Role("admin","manage Everything");
		Role savedRole= repo.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateRestRole() {
		
	}
}
