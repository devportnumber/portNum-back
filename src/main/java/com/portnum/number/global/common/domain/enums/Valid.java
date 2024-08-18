package com.portnum.number.global.common.domain.enums;

import com.portnum.number.global.common.domain.converter.AbstractEnumCodeConverter;
import jakarta.persistence.Converter;
import java.util.EnumSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 유효여부 enum
 *
 * @author 송병선
 */
@Getter
@RequiredArgsConstructor
public enum Valid implements Codable {
  TRUE("Y"),
  FALSE("N");

  private final String code;
  
  public static boolean containCode(String code) {
    return EnumSet.allOf(Valid.class).stream().anyMatch(e -> e.getCode().equals(code));
  }

  public static Valid fromCode(final String code) {
    return Codable.fromCode(Valid.class, code);
  }

  @Converter
  public static class TypeCodeConverter extends AbstractEnumCodeConverter<Valid> {

    @Override
    public Valid convertToEntityAttribute(String dbData) {
      return this.toEntityAttribute(Valid.class, dbData);
    }
  }

}
