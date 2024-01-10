package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
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
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/user-service-test-data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sql/delete-all-data.sql")
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
        User result = userService.getByEmail(email);

        //then
        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_조회할_수_없다(){
        //given
        String email = "jeohyoo1228@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_조회할_수_있다(){
        //when
        User result = userService.getById(1L);

        //then
        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getById은_PENDING_상태인_유저를_조회할_수_없다(){
        //when
        //then
        assertThatThrownBy(() -> userService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // 맨 처음 시작하면, 테스트가 통과하지 않는다.
    // 1. 이메일 발송 문제
    // 2. 인증 코드가 UUID로 생성되는 문제
    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreateDto = UserCreate.builder()
                .email("kok202@kakao.com")
                .address("Gyeongi")
                .nickname("kok202-k")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        User result = userService.create(userCreateDto);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.status()).isEqualTo(UserStatus.PENDING);
        // assertThat(result.getCertificationCode()).isEqualTo("T.T"); // FIXME
    }
    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("서울시 서대문구")
                .nickname("jeohyoo")
                .build();

        //when
        userService.update(1L, userUpdateDto);

        //then
        User result = userService.getById(1L);
        assertThat(result.id()).isNotNull();
        assertThat(result.nickname()).isEqualTo("jeohyoo");
        assertThat(result.address()).isEqualTo("서울시 서대문구");
    }

    // 마찬가지로, Clock 의존성이 존재하기 때문에, 테스트를 할 수 없다.
    @Test
    void 유저_로그인시키면_마지막_로그인_시간이_변경된다(){
        //given
        //when
        userService.login(1L);

        //then
        User result = userService.getById(1L);
        assertThat(result.lastLoginAt()).isPositive(); //FIXME: 시간을 비교할 방법을 찾자
    }

//    private UserService userService;
//    @BeforeEach
//    void init(){
//        FakeMailSender fakeMailSender = new FakeMailSender();
//        userService = UserService.builder()
//                .userRepository(new FakeUserRepository())
//                .certificationService(new CertificationService(fakeMailSender))
//                .clockHolder(TestClockHolder.builder().millis(1000L).build())
//                .uuidHolder(new TestUuidHolder("test-uuid"))
//                .build();
//    }
//
//    @Test
//    void getByEmail은_ACTIVE_상태인_유저를_조회할_수_있다(){
//        //given
//        String email = "jeohyoo1229@gmail.com";
//
//        //when
//        User result = userService.getByEmail(email);
//
//        //then
//        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
//    }
//
//    @Test
//    void getByEmail은_PENDING_상태인_유저를_조회할_수_없다(){
//        //given
//        String email = "jeohyoo1228@gmail.com";
//
//        //when
//        //then
//        assertThatThrownBy(() -> userService.getByEmail(email))
//                .isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    @Test
//    void getById은_ACTIVE_상태인_유저를_조회할_수_있다(){
//        //when
//        User result = userService.getById(1L);
//
//        //then
//        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
//    }
//
//    @Test
//    void getById은_PENDING_상태인_유저를_조회할_수_없다(){
//        //when
//        //then
//        assertThatThrownBy(() -> userService.getById(2L))
//                .isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    // 맨 처음 시작하면, 테스트가 통과하지 않는다.
//    // 1. 이메일 발송 문제
//    // 2. 인증 코드가 UUID로 생성되는 문제
//    @Test
//    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
//        // given
//        UserCreate userCreateDto = UserCreate.builder()
//                .email("kok202@kakao.com")
//                .address("Gyeongi")
//                .nickname("kok202-k")
//                .build();
//        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        // when
//        User result = userService.create(userCreateDto);
//
//        // then
//        assertThat(result.id()).isNotNull();
//        assertThat(result.status()).isEqualTo(UserStatus.PENDING);
//        // assertThat(result.getCertificationCode()).isEqualTo("T.T"); // FIXME
//    }
//    @Test
//    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
//        //given
//        UserUpdate userUpdateDto = UserUpdate.builder()
//                .address("서울시 서대문구")
//                .nickname("jeohyoo")
//                .build();
//
//        //when
//        userService.update(1L, userUpdateDto);
//
//        //then
//        User result = userService.getById(1L);
//        assertThat(result.id()).isNotNull();
//        assertThat(result.nickname()).isEqualTo("jeohyoo");
//        assertThat(result.address()).isEqualTo("서울시 서대문구");
//    }
//
//    // 마찬가지로, Clock 의존성이 존재하기 때문에, 테스트를 할 수 없다.
//    @Test
//    void 유저_로그인시키면_마지막_로그인_시간이_변경된다(){
//        //given
//        //when
//        userService.login(1L);
//
//        //then
//        User result = userService.getById(1L);
//        assertThat(result.lastLoginAt()).isPositive(); //FIXME: 시간을 비교할 방법을 찾자
//    }
//
//    @Test
//    void 유저_이메일_인증코드를_확인하면_유저의_상태가_ACTIVE로_변경된다(){
//        //given
//        String certificationCode = "1235-1234-1234-1234";
//
//        //when
//        userService.verifyEmail(2L, certificationCode);
//
//        //then
//        User result = userService.getById(2L);
//        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
//    }
//
//    @Test
//    void 유저_이메일_인증코드가_틀리면_에러가_발생한다(){
//        //given
//        String certificationCode = "1236-1234-1234-1234";
//
//        //when
//        //then
//        assertThatThrownBy(() -> userService.verifyEmail(2L, certificationCode))
//                .isInstanceOf(CertificationCodeNotMatchedException.class);
//    }


}
