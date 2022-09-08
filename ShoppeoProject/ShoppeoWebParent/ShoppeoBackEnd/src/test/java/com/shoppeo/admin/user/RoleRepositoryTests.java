package com.shoppeo.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
		Role roleSalesperson = new Role("Salesperson","상품 가격, 고객, 배송, 주문, 통계 관리");
		
		Role roleEditor = new Role("Editor","카테고리, 브랜드, 상품, 메뉴 및 아티클 관리");
		
		Role roleShipper = new Role("Shipper","상품, 주문 조회 및 주문 상태 관리");
		
		Role roleAssistant = new Role("Assistant","문의, 리뷰 관리");
		
		repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper,roleAssistant));
	}
}
