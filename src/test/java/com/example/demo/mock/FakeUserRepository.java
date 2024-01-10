package com.example.demo.mock;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import com.example.demo.user.service.port.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class FakeUserRepository implements UserRepository {

    // 사실 소형 테스트는 단일 스레드에서 돌아가기 때문에 동기화를 걱정할 필요가 없음
    // 따라서, Long과 ArrayList를 사용해도 문제가 없음
    private final AtomicLong idGenerator = new AtomicLong(1L);
    private final List<User> data = Collections.synchronizedList(new ArrayList<>());


    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream()
                .filter(item -> item.id().equals(id) && item.status().equals(userStatus))
                .findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream()
                .filter(item -> item.email().equals(email) && item.status().equals(userStatus))
                .findAny();
    }

    @Override
    public User save(User user) {
        if (user.id() == null || user.id() == 0) {
            User newUser = User.builder()
                    .id(idGenerator.getAndIncrement())
                    .email(user.email())
                    .nickname(user.nickname())
                    .email(user.email())
                    .address(user.address())
                    .status(user.status())
                    .certificationCode(user.certificationCode())
                    .lastLoginAt(user.lastLoginAt())
                    .build();
            data.add(newUser);
            return newUser;
        }
        data.removeIf(item -> item.id().equals(user.id()));
        data.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return data.stream()
                .filter(item -> item.id().equals(id))
                .findAny();
    }
}
