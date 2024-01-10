package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserCreateServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init(){
        //stub: uuid, clock, mailSender
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        userServiceImpl = UserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .uuidHolder(new TestUuidHolder("test-uuid"))
                .clockHolder(TestClockHolder.builder().millis(1000L).build())
                .build();

        fakeUserRepository.save(
                User.builder()
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build()
        );

        fakeUserRepository.save(
                User.builder()
                        .email("jeohyoo1228@gmail.com")
                        .nickname("jeohyoo1228")
                        .address("서울시 강남구")
                        .status(UserStatus.PENDING)
                        .certificationCode("1235-1234-1234-1234")
                        .build()
        );
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_조회할_수_있다(){
        //given
        String email = "jeohyoo1229@gmail.com";

        //when
        User result = userServiceImpl.getByEmail(email);

        //then
        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_조회할_수_없다(){
        //given
        String email = "jeohyoo1228@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_조회할_수_있다(){
        //when
        User result = userServiceImpl.getById(1L);

        //then
        assertThat(result.nickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void getById은_PENDING_상태인_유저를_조회할_수_없다(){
        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.getById(2L))
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
        // when
        User result = userServiceImpl.create(userCreateDto);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.status()).isEqualTo(UserStatus.PENDING);
        assertThat(result.certificationCode()).isEqualTo("test-uuid");
    }
    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("서울시 서대문구")
                .nickname("jeohyoo")
                .build();

        //when
        userServiceImpl.update(1L, userUpdateDto);

        //then
        User result = userServiceImpl.getById(1L);
        assertThat(result.id()).isNotNull();
        assertThat(result.nickname()).isEqualTo("jeohyoo");
        assertThat(result.address()).isEqualTo("서울시 서대문구");
    }

    // 마찬가지로, Clock 의존성이 존재하기 때문에, 테스트를 할 수 없다.
    @Test
    void 유저_로그인시키면_마지막_로그인_시간이_변경된다(){
        //given
        //when
        userServiceImpl.login(1L);

        //then
        User result = userServiceImpl.getById(1L);
        assertThat(result.lastLoginAt()).isEqualTo(1000L);
    }

    @Test
    void 유저_이메일_인증코드를_확인하면_유저의_상태가_ACTIVE로_변경된다(){
        //given
        String certificationCode = "1235-1234-1234-1234";

        //when
        userServiceImpl.verifyEmail(2L, certificationCode);

        //then
        User result = userServiceImpl.getById(2L);
        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 유저_이메일_인증코드가_틀리면_에러가_발생한다(){
        //given
        String certificationCode = "1236-1234-1234-1234";

        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.verifyEmail(2L, certificationCode))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}
