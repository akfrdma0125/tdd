package com.example.demo.user.controller;


import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class UserControllerTest {


    @Test
    void 사용자는_특정_유저의_개인정보인_주소를_제외한_정보를_전달받을_수_있다()  {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build());


        //when
        ResponseEntity<UserResponse> result = testContainer.userController
                .getById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeohyoo1229");
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소를_포함해_가져온다()  {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1679530673958L).build())
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build());


        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                .getMyInfo("jeohyoo1229@gmail.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getAddress()).isEqualTo("서울시 강남구");
        assertThat(result.getBody().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeohyoo1229");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1679530673958L);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디를_호출하는_경우_404응답을_받는다()  {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build());


        //when
        assertThatThrownBy(() -> testContainer.userController
                .getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화시킬_수_있다()  {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1679530673958L).build())
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(2L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.PENDING)
                        .certificationCode("1234-1234-1234-1234")
                        .build());


        //when
        ResponseEntity result = testContainer.userController
                .verifyEmail(2L, "1234-1234-1234-1234");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        User user = testContainer.userRepository.findById(2L).get();
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자가_잘못된_인증코드를_입력할_경우_에러가_발생한다()  {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(
                User.builder()
                        .id(2L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.PENDING)
                        .certificationCode("1234-1234-1234-1234")
                        .build());


        //when
        //then
        assertThatThrownBy(() -> testContainer.userController
                .verifyEmail(2L, "1234-1234-1234-1235"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내정보를_수정할_수_있다()  {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1679530673958L).build())
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build());

        UserUpdate userUpdateDto = UserUpdate.builder()
                        .nickname("jeohyoo1229-n")
                        .address("서울시 서대문구")
                        .build();
        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                        .updateMyInfo("jeohyoo1229@gmail.com", userUpdateDto);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getAddress()).isEqualTo("서울시 서대문구");
        assertThat(result.getBody().getNickname()).isEqualTo("jeohyoo1229-n");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1679530673958L);
    }



}
