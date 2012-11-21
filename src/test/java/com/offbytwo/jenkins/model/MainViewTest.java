/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainViewTest {
    @Test
    public void defaultConstructorShouldInitializeWithEmptyJobs() {
        MainView mainView = new MainView();
        assertNotNull(mainView.getJobs());
        assertEquals(0, mainView.getJobs().size());
    }
}
