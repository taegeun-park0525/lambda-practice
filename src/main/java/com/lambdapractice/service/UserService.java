package com.lambdapractice.service;

import com.lambdapractice.domain.User;
import com.lambdapractice.exception.AppException;
import com.lambdapractice.exception.ErrorCode;
import com.lambdapractice.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final BCryptPasswordEncoder encoder;


    public String join(String userName, String password) {

        //userName 중복 check
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 있습니다.");
                });

        // 저장 (나중에는 암호화해서 저장할것)
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password)) //암호화 해서 저장
                .build();
        userRepository.save(user);


        return "SUCCESS";
    }
}
