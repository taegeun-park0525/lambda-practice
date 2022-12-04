package com.lambdapractice.service;

import com.lambdapractice.domain.User;
import com.lambdapractice.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepositroy;


    public String join(String userName, String password) {

        //userName 중복 check
        userRepositroy.findByUserName(userName)
                .ifPresent(user -> {
                    throw new RuntimeException(userName + "는 이미 있습니다.");
                });

        // 저장 (나중에는 암호화해서 저장할것)
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();
        userRepositroy.save(user);


        return "SUCCESS";
    }
}