/*
 * Copyright (c) 2016 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.helper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * <ul>
 * <li>{M,N}: From the M-th element (inclusive) to the N-th element (exclusive).
 * </li>
 * <li>{M,}: From the M-th element (inclusive) to the end.</li>
 * <li>{,N}: From the first element (inclusive) to the N-th element (exclusive).
 * The same as {0,N}.</li>
 * <li>{N}: Just retrieve the N-th element. The same as {N,N+1}.</li>
 * </ul>
 *
 * @author Karl Heinz Marbaise
 */
public class RangeTest {

    private String getEscaped(String m) {
        return Range.CURLY_BRACKET_OPEN + m + Range.CURLY_BRACKET_CLOSE;
    }

    @Test
    public void fromToGiven() {
        Range r = Range.build().from(1).to(5);
        assertThat(r.getRangeString()).isEqualTo(getEscaped("1,5"));
    }

    @Test
    public void onlyFromGiven() {
        Range r = Range.build().from(3).build();
        assertThat(r.getRangeString()).isEqualTo(getEscaped("3,"));
    }

    @Test
    public void onlyToGiven() {
        Range r = Range.build().to(5).build();
        assertThat(r.getRangeString()).isEqualTo(getEscaped(",5"));
    }

    @Test
    public void onlyGiven() {
        Range r = Range.build().only(3);
        assertThat(r.getRangeString()).isEqualTo(getEscaped("3,4"));
    }

    @Test
    public void toIsGivenLargerThanFromShouldResultInIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Range.build().from(5).to(1))
                .withMessage("to must be greater than from");
    }

    @Test
    public void fromGivenNegativeValueShouldResultInIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Range.build().from(-1))
                .withMessage("from value must be greater or equal null.");
    }

    @Test
    public void fromGivenPositiveToNegativeValueShouldResultInIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Range.build().from(5).to(-1))
                .withMessage("to must be greater or equal null.");
    }
}
