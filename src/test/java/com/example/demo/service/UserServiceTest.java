package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/user-service-test-data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete-all-data.sql")
})
class UserServiceTest {
    @Autowired
    private UserService userService;

    // JavaMailSender는 실제로 메일을 보내는 것이 아니라, 테스트에서는 Mock으로 대체한다.
    // SpringBoot의 빈을 MockBean으로 덮어쓰기 하는 작업
    @MockBean
    private JavaMailSender javaMailSender;

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

    // 맨 처음 시작하면, 테스트가 통과하지 않는다.
    // 1. 이메일 발송 문제
    // 2. 인증 코드가 UUID로 생성되는 문제
    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("jeohyoo1229@gmail.com")
                .address("서울시 강남구")
                .nickname("jeohyoo1229")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //when
        UserEntity result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("서울시 서대문구")
                .nickname("jeohyoo")
                .build();

        //when
        userService.update(1L, userUpdateDto);

        //then
        UserEntity result = userService.getById(1L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("jeohyoo");
        assertThat(result.getAddress()).isEqualTo("서울시 서대문구");
    }
}
