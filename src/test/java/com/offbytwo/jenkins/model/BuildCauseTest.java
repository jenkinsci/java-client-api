/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Karl Heinz Marbaise
 */
public class BuildCauseTest {

    private BuildCause bc;

    @Before
    public void before() {
	this.bc = new BuildCause();
    }

    @Test
    public void getUpstreamBuildShouldNotResultWithNPE() {
	assertThat(bc.getUpstreamBuild()).isEqualTo(0);
    }

    @Test
    public void getShortDescriptionShouldReturnNull() {
	assertThat(bc.getShortDescription()).isNull();
    }

    @Test
    public void getUpstreamProjectShouldReturnNull() {
	assertThat(bc.getUpstreamProject()).isNull();
    }

    @Test
    public void getUpstreamUrlShouldReturnNull() {
	assertThat(bc.getUpstreamUrl()).isNull();
    }

    @Test
    public void getUserIdShouldReturnNull() {
	assertThat(bc.getUserId()).isNull();
    }

    @Test
    public void getUserNameShouldReturnNull() {
	assertThat(bc.getUserName()).isNull();
    }

}
