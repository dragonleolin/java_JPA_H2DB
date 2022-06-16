package com.example.demo.dao;

import com.example.demo.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<UserPO, Integer> {

    Optional<UserPO> findByUserCode(String userCode);

    void deleteByUserCode(String userCode);
}
