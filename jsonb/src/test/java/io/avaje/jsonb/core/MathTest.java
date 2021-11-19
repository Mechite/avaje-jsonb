package io.avaje.jsonb.core;

import io.avaje.jsonb.JsonType;
import io.avaje.jsonb.Jsonb;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MathTest {

  Jsonb jsonbMathAsString = Jsonb.newBuilder().mathAsString(true).build();
  Jsonb jsonbMathAsNumber = Jsonb.newBuilder().build();

  @Test
  void mathAsString_true_bigDecimal() throws IOException {

    JsonType<BigDecimal> type = jsonbMathAsString.type(BigDecimal.class);
    String asJson = type.toJson(new BigDecimal("123.4567"));
    assertThat(asJson).isEqualTo("\"123.4567\"");

    BigDecimal fromJson = type.list().fromJson("[\"123.4567\"]").get(0);
    assertThat(fromJson).isEqualTo(new BigDecimal("123.4567"));
  }

  @Test
  void mathAsString_false_bigDecimal() throws IOException {

    JsonType<BigDecimal> type = jsonbMathAsNumber.type(BigDecimal.class);
    String asJson = type.toJson(new BigDecimal("123.4567"));
    assertThat(asJson).isEqualTo("123.4567");

    BigDecimal fromJson = type.list().fromJson("[123.4567]").get(0);
    assertThat(fromJson).isEqualTo(new BigDecimal("123.4567"));
  }

  @Test
  void mathAsString_true_bigInteger() throws IOException {

    JsonType<BigInteger> type = jsonbMathAsString.type(BigInteger.class);
    String asJson = type.toJson(new BigInteger("12456789012345678901234567890"));
    assertThat(asJson).isEqualTo("\"12456789012345678901234567890\"");

    BigInteger fromJson = type.list().fromJson("[\"12456789012345678901234567890\"]").get(0);
    assertThat(fromJson).isEqualTo(new BigInteger("12456789012345678901234567890"));
  }

  @Test
  void mathAsString_false_bigInteger_ArithmeticException() {

    assertThatThrownBy(() -> {
      JsonType<BigInteger> type = jsonbMathAsNumber.type(BigInteger.class);
      type.toJson(new BigInteger("12456789012345678901234567890"));
    }).isInstanceOf(ArithmeticException.class)
      .hasMessageContaining("BigInteger out of long range");

  }

  @Test
  void mathAsString_false_bigInteger() throws IOException {

    JsonType<BigInteger> type = jsonbMathAsNumber.type(BigInteger.class);
    String asJson = type.toJson(new BigInteger("124567890123456789"));

    assertThat(asJson).isEqualTo("124567890123456789");

    BigInteger fromJson = type.list().fromJson("[124567890123456789]").get(0);
    assertThat(fromJson).isEqualTo(new BigInteger("124567890123456789"));
  }
}
