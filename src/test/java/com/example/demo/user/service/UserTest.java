package com.example.demo.user.service;

import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void User는_UserCreate로_유저_정보를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();

        //when
        User user = User.from(userCreate);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isNotNull();
        assertThat(user.id()).isNull();
    }

    @Test
    void User는_UseUpdate로_유저_정보를_수정할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();

        //when
        User user = User.from(userCreate);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isNotNull();
        assertThat(user.id()).isNull();
    }

    @Test
    void User는_로그인을_할_수_있고_마지막_로그인_시간이_변경된다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();

        //when
        User user = User.from(userCreate);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isNotNull();
        assertThat(user.id()).isNull();
    }

    @Test
    void User는_유효한_인증_코드로_계정을_활성화할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();

        //when
        User user = User.from(userCreate);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isNotNull();
        assertThat(user.id()).isNull();
    }

    @Test
    void User는_유효하지_않은_인증_코드로_시도시_에러가_발생한다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .build();

        //when
        User user = User.from(userCreate);

        //then
        assertThat(user.email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(user.nickname()).isEqualTo("jeohyoo1229");
        assertThat(user.address()).isEqualTo("서울시 강남구");
        assertThat(user.status()).isEqualTo(UserStatus.PENDING);
        assertThat(user.certificationCode()).isNotNull();
        assertThat(user.id()).isNull();
    }
}