package org.hyoj.mysbbp.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.dto.ErrorDto;
import org.hyoj.mysbbp.dto.GoogleJsonStyleGuideDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 404 에러
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response<ErrorDto>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Data Not Found Exception");
        ex.printStackTrace();

        ErrorDto errorDto = new ErrorDto(ApiResultStatus.NOT_FOUND, ex.getMessage());

        GoogleJsonStyleGuideDto<ErrorDto> errorResponse = GoogleJsonStyleGuideDto.ErrorDtoContainer
                .builder()
                .apiVersion("v1")
                .requestId((String) httpServletRequest.getAttribute("requestId"))
                .error(errorDto).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 인증(401) 관련 에러 처리
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Response<ErrorDto>> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized Exception - {}", ex.getCode() + "," + ex.getMessage() + "," + ex.getDescription());
        ex.printStackTrace();

        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage(), ex.getDescription());

        GoogleJsonStyleGuideDto<ErrorDto> errorResponse = GoogleJsonStyleGuideDto.ErrorDtoContainer
                .builder()
                .apiVersion("v1")
                .requestId((String) httpServletRequest.getAttribute("requestId"))
                .error(errorDto).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 인가(403) 관련 에러 처리
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Response<ErrorDto>> handleForbiddenException(ForbiddenException ex) {
        log.error("Forbidden Exception - {}", ex.getCode() + "," + ex.getMessage() + "," + ex.getDescription());
        ex.printStackTrace();

        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage(), ex.getDescription());

        GoogleJsonStyleGuideDto<ErrorDto> errorResponse = GoogleJsonStyleGuideDto.ErrorDtoContainer
                .builder()
                .apiVersion("v1")
                .requestId((String) httpServletRequest.getAttribute("requestId"))
                .error(errorDto).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * 비즈니스 예외처리
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<ErrorDto>> handleBusinessException(BusinessException ex) {
        log.error("Business Exception - {}", ex.getCode() + "," + ex.getMessage() + "," + ex.getDescription());

        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage(), ex.getDescription());

        GoogleJsonStyleGuideDto<ErrorDto> errorResponse = GoogleJsonStyleGuideDto.ErrorDtoContainer
                .builder()
                .apiVersion("v1")
                .requestId((String) httpServletRequest.getAttribute("requestId"))
                .error(errorDto).build();

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * 서버 에러
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<ErrorDto>> handleException(Exception ex) {
        log.error("Internal Server Error Exception");
        log.error(ex.getMessage());

        ex.printStackTrace();

        ErrorDto errorDto = new ErrorDto(ApiResultStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        GoogleJsonStyleGuideDto<ErrorDto> errorResponse = GoogleJsonStyleGuideDto.ErrorDtoContainer
                .builder()
                .apiVersion("v1")
                .requestId((String) httpServletRequest.getAttribute("requestId"))
                .error(errorDto).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
