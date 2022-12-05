package com.lambdapractice.service;

import com.lambdapractice.domain.User;
import com.lambdapractice.exception.AppException;
import com.lambdapractice.exception.ErrorCode;
import com.lambdapractice.repositroy.UserRepository;
import com.lambdapractice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String key;
    private long expiireTimeMs = 1000*60*60l;
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

    public String login(String userName, String password) {

        //userName없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));
        //Password틀림
        if(!encoder.matches(password, selectedUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못입력하셨습니다.");
        }

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(),key, expiireTimeMs   );

        return token;
    }
}
