package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql("/sql/user-service-test-data.sql")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_조회할_수_있다(){
        //given
        String email = "jeohyoo1229@gmail.com";

        //when
        UserEntity result = userService.getByEmail("jeohyoo1229@gmail.com");

        //then
        assertThat(result.getNickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_조회할_수_없다(){
        //given
        String email = "jeohyoo1228@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail("jeohyoo1228@gmail.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
