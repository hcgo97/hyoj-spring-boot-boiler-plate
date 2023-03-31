package org.hyoj.mysbbp.controller;

import lombok.RequiredArgsConstructor;
import org.hyoj.mysbbp.dto.DataDto;
import org.hyoj.mysbbp.dto.Response;
import org.hyoj.mysbbp.dto.ResultDto;
import org.hyoj.mysbbp.dto.UserDto;
import org.hyoj.mysbbp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

    private final UserService userService;

    /**
     * 유저 등록
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Response<DataDto>> signUp(@RequestBody UserDto.SignUpDto inputDto) {

        ResultDto resultDto = userService.signUp(inputDto);
        return new ResponseEntity<>(responseBuilder(resultDto), HttpStatus.CREATED);
    }

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<Response<DataDto>> signIn(@RequestBody UserDto.SignInDto inputDto) {

        UserDto.TokenDto tokenDto = userService.signIn(inputDto);
        return new ResponseEntity<>(responseBuilder(tokenDto), HttpStatus.OK);
    }

    /**
     * 유저 리스트 조회
     */
    @GetMapping
    public ResponseEntity<Response<DataDto>> getUserList() {

        List<UserDto.UserInfoDto> userList = userService.getUserList();
        return new ResponseEntity<>(responseBuilder(userList), HttpStatus.OK);
    }

    /**
     * 유저 상세 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Response<DataDto>> showUserDetail(@PathVariable Long userId) {

        UserDto.UserInfoDto userInfoDto = userService.showUserDetail(userId);
        return new ResponseEntity<>(responseBuilder(userInfoDto), HttpStatus.OK);
    }

    /**
     * 유저 수정
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Response<DataDto>> modifyUser(@PathVariable Long userId,
                                                        @RequestBody UserDto.SignUpDto inputDto) {

        ResultDto resultDto = userService.modifyUser(userId, inputDto);
        return new ResponseEntity<>(responseBuilder(resultDto), HttpStatus.OK);
    }

    /**
     * 유저 삭제
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Response<DataDto>> deleteUser(@PathVariable Long userId) {

        ResultDto resultDto = userService.deleteUser(userId);
        return new ResponseEntity<>(responseBuilder(resultDto), HttpStatus.OK);
    }
}
