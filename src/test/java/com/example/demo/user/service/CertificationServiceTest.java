package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내진다() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        //when
        certificationService.send("jeohyoo1229@gmail.com","1234",1L);

        //then
        assertThat(fakeMailSender.email).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=1234");
    }
}