package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "address")
    private String address;

    @Column(name = "certification_code")
    private String certificationCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "last_login_at")
    private Long lastLoginAt;

    @Builder
    public UserEntity(Long id, String email, String nickname, String address, String certificationCode, UserStatus status) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
    }


    public User toModel(){
        return User.builder()
            .id(this.id)
            .email(this.email)
            .nickname(this.nickname)
            .address(this.address)
            .certificationCode(this.certificationCode)
            .status(this.status)
            .lastLoginAt(this.lastLoginAt)
            .build();
    }

    public static UserEntity fromModel(User user){
        return UserEntity.builder()
                .address(user.address())
                .email(user.email())
                .nickname(user.nickname())
                .certificationCode(user.certificationCode())
                .status(user.status())
                .build();
    }
}