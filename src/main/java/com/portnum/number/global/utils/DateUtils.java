package com.portnum.number.global.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * 날짜/시간 유틸
 *
 * @author 송병선
 */
@UtilityClass
public class DateUtils {

  public final String DATE_PATTERN = "yy.MM.dd";

  /**
   * 날짜에 해당하는 요일 반환
   *
   * @param date 날짜
   * @return 요일
   */
  public String getDayOfWeek(LocalDate date) {
    // 요일을 가져옵니다.
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    // 요일을 문자열로 반환합니다.
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
  }

  /**
   * yy.MM.dd 문자열 -> LocalDate 반환
   *
   * @param dateString 날짜 문자열
   * @return LocalDate
   */
  public LocalDate convertToDate(String dateString) {
    if (!StringUtils.hasText(dateString)) {
      return null;
    }
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_PATTERN));
  }

  /**
   * 시작 날짜와 종료 날짜 사이에 특정 날짜 포함 여부 반환
   *
   * @param startDate 시작 날짜
   * @param endDate   종료 날짜
   * @param date      특정 날짜
   * @return 포함 여부
   */
  public boolean isBetween(LocalDate startDate, LocalDate endDate, LocalDate date) {
    if (Objects.isNull(date)) {
      return false;
    }
    if (Objects.isNull(startDate) && Objects.nonNull(endDate)) {
      return !date.isAfter(endDate);
    }
    if (Objects.nonNull(startDate) && Objects.isNull(endDate)) {
      return !date.isBefore(startDate);
    }

    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }
}
