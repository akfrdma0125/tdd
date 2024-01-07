package com.example.demo.user.controller;


import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/user-controller-test-data.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sql/delete-all-data.sql")
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //주소같은 경우, 일반 조회에서는 나오지 않음 -> 테스트에서는 명시적으로 나오지 않는다는 걸 표현
    //이런 코드가 하나하나 모여 정책이 됨

    @Test
    void 사용자는_특정_유저의_개인정보인_주소를_제외한_정보를_전달받을_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("jeohyoo1229"))
                .andExpect(jsonPath("$.email").value("jeohyoo1229@gmail.com"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소를_포함해_가져온다() throws Exception {
        mockMvc.perform(get("/api/users/me").header("EMAIL","jeohyoo1229@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("jeohyoo1229"))
                .andExpect(jsonPath("$.email").value("jeohyoo1229@gmail.com"))
                .andExpect(jsonPath("$.address").value("서울시 강남구"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디를_호출하는_경우_404응답을_받는다() throws Exception {
        mockMvc.perform(get("/api/users/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 3를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화시킬_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                        .param("certificationCode", "1235-1234-1234-1234"))
                .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자가_잘못된_인증코드를_입력할_경우_에러가_발생한다() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                        .param("certificationCode", "1235-1234-1234-1230"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 사용자는_내정보를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                        .nickname("jeohyoo1229-n")
                        .address("서울시 서대문구")
                        .build();
        //when
        //then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "jeohyoo1229@gmail.com")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("jeohyoo1229-n"))
                .andExpect(jsonPath("$.email").value("jeohyoo1229@gmail.com"))
                .andExpect(jsonPath("$.address").value("서울시 서대문구"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }



}
