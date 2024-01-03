package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/user-service-test-data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete-all-data.sql")
})
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

    @Test
    void getById은_ACTIVE_상태인_유저를_조회할_수_있다(){
        //given
        String email = "jeohyoo1229@gmail.com";

        //when
        UserEntity result = userService.getById(1L);

        //then
        assertThat(result.getNickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getById은_PENDING_상태인_유저를_조회할_수_없다(){
        //given
        String email = "jeohyoo1228@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
