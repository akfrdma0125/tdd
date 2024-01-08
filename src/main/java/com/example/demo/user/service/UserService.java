package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;
    private static final String RESOURCE_NAME = "Users";

    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, email));
    }

    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    // "createUser" 하지 않아도, 이미 User 서비스이기 때문에, 유저 생성의 의미를 가지게 됨
    @Transactional
    public User create(UserCreate userCreate) {
        User user = userRepository.save(User.from(userCreate));
        certificationService.send(userCreate.getEmail(), user.certificationCode(), user.id());
        return user;
    }

    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        return userRepository.save(user.update(userUpdate));
    }

    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
        userRepository.save(user.login());
    }

    //이제 외부 연결성이 끊어져서 save 해줘야 함
    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
        user.certificate(user.certificationCode());
        userRepository.save(user);
    }

}