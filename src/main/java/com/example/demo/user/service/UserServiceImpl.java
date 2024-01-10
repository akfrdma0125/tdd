package com.example.demo.user.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class UserServiceImpl implements UserCreateService, UserReadService, UserUpdateService, AuthenticationService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private static final String RESOURCE_NAME = "Users";

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Email", email));
    }

    @Override
    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    // "createUser" 하지 않아도, 이미 User 서비스이기 때문에, 유저 생성의 의미를 가지게 됨
    @Transactional
    @Override
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.certificationCode(), user.id());
        return user;
    }

    @Transactional
    @Override
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        return userRepository.save(user.update(userUpdate, clockHolder));
    }

    @Transactional
    @Override
    public User login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
        return userRepository.save(user.login(clockHolder));
    }

    //이제 외부 연결성이 끊어져서 save 해줘야 함
    @Transactional
    @Override
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
        user = user.certificate(certificationCode, clockHolder);
        userRepository.save(user);
    }

}