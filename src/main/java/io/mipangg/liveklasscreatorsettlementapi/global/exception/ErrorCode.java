package io.mipangg.liveklasscreatorsettlementapi.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DATE_RANGE_REQUIRED(HttpStatus.BAD_REQUEST, "시작 날짜와 종료 날짜는 함께 입력되어야 합니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작 날짜는 종료 날짜보다 이전이어야 합니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "입력된 날짜는 과거 혹은 현재여야 합니다."),
    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."),
    CREATOR_NOT_FOUND(HttpStatus.NOT_FOUND, "강사를 찾을 수 없습니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "학생을 찾을 수 없습니다."),
    SALE_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "판매 내역을 찾을 수 없습니다."),
    CANCEL_NOT_FOUND(HttpStatus.NOT_FOUND, "취소 내역을 찾을 수 없습니다."),
    SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산을 찾을 수 없습니다."),

    /*
     * 409 CONFLICT
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    SALE_RECORD_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 판매 내역입니다."),
    CANCEL_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 취소 내역입니다."),
    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    API_CALL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 호출에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
