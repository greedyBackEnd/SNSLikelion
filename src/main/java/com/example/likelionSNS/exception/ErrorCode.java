package com.example.likelionSNS.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
public enum ErrorCode {
    NO_SUCH_ELEMENT(HttpStatus.NO_CONTENT, "해당 컨텐츠를 찾을 수 없습니다"),
    MOVED_PERMANT(HttpStatus.MOVED_PERMANENTLY, "URI가 변경되었습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컨텐츠가 존재하지 않습니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 접근 방식입니다"),
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "접근 요청시간이 만료되었습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부적인 서버 에러가 발생했습니다"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "구현되지 않은 기능입니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}