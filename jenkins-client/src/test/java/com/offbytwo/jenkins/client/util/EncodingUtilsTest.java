/*
 * Copyright (c) 2019 Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is a unit test for the helper class {@link EncodingUtils}.
 *
 *  @author Karl Heinz Marbaise
 */
class EncodingUtilsTest {

    @Test
    void encodeShouldReturnEncodedDoubleQuoteAndSpace() {
        String result = EncodingUtils.encode("!\"& ");
        assertThat(result).isEqualTo("%21%22%26+");
    }

    @Test
    void encodeShouldReturnNotEncodeSafeChars() {
        String result = EncodingUtils.encode("-._~!$'()*,;&=@:+");
        assertThat(result).isEqualTo("-._%7E%21%24%27%28%29*%2C%3B%26%3D%40%3A%2B");
    }
    @Test
    void encodeShouldReturnNotEncodeAlpha() {
        String result = EncodingUtils.encode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertThat(result).isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    @Test
    void encodeShouldReturnEncodingUmlautAndOthers() {
        String result = EncodingUtils.encode("äöü#{}");
        assertThat(result).isEqualTo("%C3%A4%C3%B6%C3%BC%23%7B%7D");
    }

    @Test
    void encodeParamShouldReturnEncodedExclamationMarkDoubleQuoteAmpersampSpace() {
        String result = EncodingUtils.formParameter("!\"& ");
        assertThat(result).isEqualTo("%21%22%26+");
    }

    @Test
    void encodeParamShouldReturnNotEncodeSafeChars() {
        String result = EncodingUtils.formParameter("-_.*");
        assertThat(result).isEqualTo("-_.*");
    }

    @Test
    void encodeParamShouldReturnEncodedUmlautAndOthers() {
        String result = EncodingUtils.formParameter("äöü#{}");
        assertThat(result).isEqualTo("%C3%A4%C3%B6%C3%BC%23%7B%7D");
    }

    @Test
    void encodeParamShouldReturnEncodedCharacters() {
        String result = EncodingUtils.formParameter("-._~!$'()*,;&=@:+");
        assertThat(result).isEqualTo("-._%7E%21%24%27%28%29*%2C%3B%26%3D%40%3A%2B");
    }

}