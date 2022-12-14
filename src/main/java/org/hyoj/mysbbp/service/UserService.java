package org.hyoj.mysbbp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.enums.DeletedStatus;
import org.hyoj.mysbbp.common.enums.TokenTypeEnum;
import org.hyoj.mysbbp.common.exception.BusinessException;
import org.hyoj.mysbbp.common.jwt.JwtProvider;
import org.hyoj.mysbbp.dto.ResultDto;
import org.hyoj.mysbbp.dto.UserDto;
import org.hyoj.mysbbp.mapper.UsersMapper;
import org.hyoj.mysbbp.model.Users;
import org.hyoj.mysbbp.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final JwtProvider jwtProvider;

    /**
     * 유저 등록
     */
    @Transactional
    public ResultDto signUp(UserDto.SignUpDto inputDto) {
        if (usersRepository.findByUserId(inputDto.getUserId()).isPresent()) {
            throw new BusinessException(ApiResultStatus.ALREADY_SIGNED_UP);
        }

        Users users = new Users();
        users.setUserId(inputDto.getUserId());
        users.setPassword(passwordEncoder.encode(inputDto.getPassword()));
        users.setNickname(inputDto.getNickname());
        users.setPhone_number(inputDto.getPhoneNumber());
        users.setEmail(inputDto.getEmail());

        usersRepository.save(users);

        return new ResultDto(true);
    }

    /**
     * 로그인
     */
    @Transactional
    public UserDto.TokenDto signIn(UserDto.SignInDto inputDto) {
        // 아이디 체크
        Users users = usersRepository.findByUserId(inputDto.getUserId()).orElseThrow(() -> new BusinessException(ApiResultStatus.LOGIN_FAILED));

        // 패스워드 체크
        if (!passwordEncoder.matches(inputDto.getPassword(), users.getPassword())) {
            throw new BusinessException(ApiResultStatus.LOGIN_FAILED);
        }

        users.setLoginCount(users.getLoginCount() + 1); // 로그인 횟수 +1

        //토큰 설정
        UserDto.UserInfoDto userInfo = usersMapper.Users2UserInfoDto(users);
        String accessToken = jwtProvider.createToken(userInfo, TokenTypeEnum.ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(userInfo, TokenTypeEnum.REFRESH_TOKEN);

        return new UserDto.TokenDto(accessToken, refreshToken);
    }

    /**
     * 유저 리스트 조회
     */
    public List<UserDto.UserInfoDto> getUserList() {
        return usersRepository.findByIsDeleted(DeletedStatus.NOT_DELETED.getCode())
                .stream()
                .map(usersMapper::Users2UserInfoDto)
                .collect(Collectors.toList());
    }

    /**
     * 유저 상세 조회
     */
    public UserDto.UserInfoDto showUserDetail(Long userId) {
        Users users = usersRepository.findById(userId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));
        return usersMapper.Users2UserInfoDto(users);
    }

    /**
     * 유저 수정
     */
    @Transactional
    public ResultDto modifyUser(Long userId, UserDto.SignUpDto inputDto) {
        Users users = usersRepository.findById(userId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));

        users.setUserId(inputDto.getUserId());
        users.setPassword(passwordEncoder.encode(inputDto.getPassword()));
        users.setNickname(inputDto.getNickname());
        users.setPhone_number(inputDto.getPhoneNumber());
        users.setEmail(inputDto.getEmail());
        usersRepository.save(users);

        return new ResultDto(true);
    }

    /**
     * 유저 삭제
     */
    @Transactional
    public ResultDto deleteUser(Long userId) {
        Users users = usersRepository.findById(userId).orElseThrow(() -> new BusinessException(ApiResultStatus.USER_NOT_FOUND));

        users.setIsDeleted(DeletedStatus.DELETED.getCode());
        users.setDeletedAt(LocalDateTime.now());
        usersRepository.save(users);

        return new ResultDto(true);
    }
}
