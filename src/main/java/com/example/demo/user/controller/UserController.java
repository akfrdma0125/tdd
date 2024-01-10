package com.example.demo.user.controller;

import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.domain.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "유저(users)")
@RestController
@Builder
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserReadService userReadService;
    private final AuthenticationService authenticationService;
    private final UserUpdateService userUpdateService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponse.from(userReadService.getById(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        authenticationService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }


    //이런 식으로 MyInfo와 같이 리턴하는 종류가 다를 경우, 컨트롤러 분리도 고려할 수 있음
    @GetMapping("/me")
    public ResponseEntity<MyProfileResponse> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
    ) {
        User user = userReadService.getByEmail(email);
        user = authenticationService.login(user.id());
        return ResponseEntity
            .ok()
            .body(MyProfileResponse.from(user));
    }

    //UserUpdate의 경우, 사용자에게 직접 노출이 되므로, request 패키지 안에 새로 클래스 생성할 수도 있음
    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<MyProfileResponse> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
        @RequestBody UserUpdate userUpdate
    ) {
        User user = userReadService.getByEmail(email);
        user = userUpdateService.update(user.id(), userUpdate);
        return ResponseEntity
            .ok()
            .body(MyProfileResponse.from(user));
    }

}