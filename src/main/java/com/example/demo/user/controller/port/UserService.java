package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

public interface UserService {
    User login(long id);
    void verifyEmail(long id, String certificationCode);
    User update(long id, UserUpdate userUpdate);
    User create(UserCreate userCreate);
    User getById(long id);
    User getByEmail(String email);
}
