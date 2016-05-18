/*
 * Copyright (c) 2016 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.offbytwo.jenkins.helper.Range;

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
 *
 */
public class RangeTest {

    @Test
    public void fromToGiven() {
        Range r = Range.build().from(1).to(5);
        assertThat(r.getRangeString()).isEqualTo("{1,5}");
    }
    
    @Test
    public void onlyFromGiven() {
        Range r = Range.build().from(3).build();
        assertThat(r.getRangeString()).isEqualTo("{3,}");
    }

    @Test
    public void onlyToGiven() {
        Range r = Range.build().to(5).build();
        assertThat(r.getRangeString()).isEqualTo("{,5}");
    }

    @Test
    public void onlyGiven() {
        Range r = Range.build().only(3);
        assertThat(r.getRangeString()).isEqualTo("{3,4}");
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void toIsGivenLargerThanFromShouldResultInIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("to must be greater than from");
        Range.build().from(5).to(1);
    }

    @Test
    public void fromGivenNegativeValueShouldResultInIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("from value must be greater or equal null.");
        Range.build().from(-1);
    }

    @Test
    public void fromGivenPositiveToNegativeValueShouldResultInIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("to must be greater or equal null.");
        Range.build().from(5).to(-1);
    }
}
