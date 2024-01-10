package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {
    private ClockHolder clockHolder = TestClockHolder.builder().millis(1000L).build();

    @Test
    void User는_UserCreate로_유저_정보를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();


        //when
        User user = User.from(userCreate, new TestUuidHolder("test-uuid"));

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isEqualTo("test-uuid");
        assertThat(user.id()).isNull();
    }

    @Test
    void User는_UseUpdate로_유저_정보를_수정할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeohyoo229")
                .address("서울시 서대문구")
                .build();

        User user = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();

        //when
        user = user.update(userUpdate, clockHolder);

        //then
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo229");
        assertThat(user.address()).isEqualTo("서울시 서대문구");
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.certificationCode()).isEqualTo("1234-1234-1234-1234");
    }

    @Test
    void User는_로그인을_할_수_있고_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();
        TestClockHolder clockHolder = TestClockHolder.builder().millis(1000L).build();

        //when
        user = user.login(clockHolder);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.certificationCode()).isEqualTo("1234-1234-1234-1234");
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.lastLoginAt()).isEqualTo(1000L);
    }

    @Test
    void User는_유효한_인증_코드로_계정을_활성화할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.PENDING)
                .certificationCode("1234-1234-1234-1234")
                .build();
        TestClockHolder testClockHolder = TestClockHolder.builder().millis(1000L).build();

        //when
        user = user.certificate("1234-1234-1234-1234", testClockHolder);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.certificationCode()).isEqualTo("1234-1234-1234-1234");
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.lastLoginAt()).isEqualTo(1000L);
    }

    @Test
    void User는_유효하지_않은_인증_코드로_시도시_에러가_발생한다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.PENDING)
                .certificationCode("1234-1234-1234-1234")
                .build();
        TestClockHolder testClockHolder = TestClockHolder.builder().millis(1000L).build();

        //when
        //then
        assertThatThrownBy(() -> user.certificate("1234-1234-1234-1235", testClockHolder))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}