package com.example.demo.medium;



import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

    //save 메서드의 경우,
    // 1. 전체 테스트시, 병렬성에 문제가 생길 수 있다.
    // 2. 중복되는 코드가 많다.
    // -> @Sql을 사용하여, 테스트 데이터를 미리 넣어두고 테스트를 진행한다.

    @Autowired
    private UserJpaRepository userJPARepository;

    @Test
    void findByIdAndStatus_유저_데이터를_조회한다() {
        //given
        //when
        Optional<UserEntity> savedUserEntity = userJPARepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        //then
        assertThat(savedUserEntity).isPresent();
    }

    @Test
    void findByIdAndStatus_유저_데이터가_없으면_Optinal_empty를_반환한다() {
        //given
        //when
        Optional<UserEntity> savedUserEntity = userJPARepository.findByIdAndStatus(1L, UserStatus.PENDING);

        //then
        assertThat(savedUserEntity).isNotPresent();
    }

    @Test
    void findByEmailAndStatus_유저_데이터를_조회한다() {
        //given
        //when
        Optional<UserEntity> savedUserEntity = userJPARepository.findByEmailAndStatus("jeohyoo1229@gmail.com", UserStatus.ACTIVE);

        //then
        assertThat(savedUserEntity.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_유저_데이터가_없으면_Optinal_empty를_반환한다() {
        //given
        //when
        Optional<UserEntity> savedUserEntity = userJPARepository.findByEmailAndStatus("jeohyoo1229@gmail.com", UserStatus.PENDING);

        //then
        assertThat(savedUserEntity).isNotPresent();
    }

}
