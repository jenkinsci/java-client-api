/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.BaseUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainViewTest extends BaseUnitTest {

    @Test
    public void defaultConstructorShouldInitializeWithEmptyJobs() {
        // when
        MainView mainView = new MainView();

        // then
        assertNotNull(mainView.getJobs());
        assertEquals(0, mainView.getJobs().size());
    }
}
