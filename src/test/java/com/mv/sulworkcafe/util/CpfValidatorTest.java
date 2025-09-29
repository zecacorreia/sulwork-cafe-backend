package com.mv.sulworkcafe.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CpfValidatorTest {

    @Test
    void normalize_removesNonDigitsAndPreservesLeadingZeros() {
        assertThat(CpfValidator.normalize("068.759.310-70")).isEqualTo("06875931070");
    }

    @Test
    void isValid_only11DigitsAccepted() {
        assertThat(CpfValidator.isValid("06875931070")).isTrue();
        assertThat(CpfValidator.isValid("123")).isFalse();
        assertThat(CpfValidator.isValid("")).isFalse();
    }
}