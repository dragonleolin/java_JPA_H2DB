package com.example.demo.dao;

import com.example.demo.po.LogoutTokenPO;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface LogoutTokenDao extends CrudRepository<LogoutTokenPO, String> {

    void deleteLogoutTokenPOSByTokenExpIsBefore(Timestamp timeLimit);
}
