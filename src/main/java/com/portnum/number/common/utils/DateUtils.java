package com.portnum.number.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import lombok.experimental.UtilityClass;

/**
 * 날짜/시간 유틸
 *
 * @author 송병선
 */
@UtilityClass
public class DateUtils {

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
}
