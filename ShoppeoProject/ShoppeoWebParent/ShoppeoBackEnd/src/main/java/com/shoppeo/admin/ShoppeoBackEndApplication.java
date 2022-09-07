package com.shoppeo.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shoppeo.common.entity","com.shoppeo.admin.user"})
public class ShoppeoBackEndApplication { 

	public static void main(String[] args) {
		SpringApplication.run(ShoppeoBackEndApplication.class, args);
	}

}
