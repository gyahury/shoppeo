package com.shoppeo.admin.user;

import org.springframework.data.repository.CrudRepository;

import com.shoppeo.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
