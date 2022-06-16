package com.example.demo.service;

import com.example.demo.dao.LogoutTokenDao;
import com.example.demo.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchedulerService {

    @Autowired
    private LogoutTokenDao logoutTokenDao;

    @Transactional
    public void cleanUpExpiredLogoutToken() {
        logoutTokenDao.deleteLogoutTokenPOSByTokenExpIsBefore(DateTimeUtil.getCurrentTime());
    }
}
