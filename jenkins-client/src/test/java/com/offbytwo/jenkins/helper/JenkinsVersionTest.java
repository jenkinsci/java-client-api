package com.offbytwo.jenkins.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JenkinsVersionTest {

    @Test
    public void isGreaterThanTrue() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isGreaterThan("1.548")).isTrue();
    }

    @Test
    public void isGreaterThanFalse() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isGreaterThan("1.651.1")).isFalse();
    }

    @Test
    public void isGreaterThanTrueJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.548");
        assertThat(a.isGreaterThan(b)).isTrue();
    }

    @Test
    public void isGreaterThanFalseJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.1");
        assertThat(a.isGreaterThan(b)).isFalse();
    }

    @Test
    public void isEqualToTrue() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isEqualTo("1.651.1")).isTrue();
    }

    @Test
    public void isEqualToFalse() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isEqualTo("1.651.0")).isFalse();
    }

    @Test
    public void isEqualToTrueJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.1");
        assertThat(a.isEqualTo(b)).isTrue();
    }

    @Test
    public void isEqualToFalseJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.0");
        assertThat(a.isEqualTo(b)).isFalse();
    }

    @Test
    public void isGreaterOrEqualTrue() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isGreaterOrEqual("1.651.1")).isTrue();
    }

    @Test
    public void isGreaterOrEqualFalse() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isGreaterOrEqual("1.651.2")).isFalse();
    }

    @Test
    public void isGreaterOrEqualTrueJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.1");
        assertThat(a.isGreaterOrEqual(b)).isTrue();
    }

    @Test
    public void isGreaterOrEqualFalseJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.2");
        assertThat(a.isGreaterOrEqual(b)).isFalse();
    }

    @Test
    public void isLessOrEqualTrue() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isLessOrEqual("1.651.1")).isTrue();
    }

    @Test
    public void isLessOrEqualFalse() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isLessOrEqual("1.651.0")).isFalse();
    }

    @Test
    public void isLessOrEqualTrueJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.1");
        assertThat(a.isLessOrEqual(b)).isTrue();
    }

    @Test
    public void isLessOrEqualFalseJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.0");
        assertThat(a.isLessOrEqual(b)).isFalse();
    }

    @Test
    public void isLessThanTrue() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isLessThan("1.651.2")).isTrue();
    }

    @Test
    public void isLessThanFalse() {
        JenkinsVersion jv = new JenkinsVersion("1.651.1");
        assertThat(jv.isLessThan("1.651.1")).isFalse();
    }

    @Test
    public void isLessThanTrueJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.2");
        assertThat(a.isLessThan(b)).isTrue();
    }

    @Test
    public void isLessThanFalseJenkinsVersion() {
        JenkinsVersion a = new JenkinsVersion("1.651.1");
        JenkinsVersion b = new JenkinsVersion("1.651.1");
        assertThat(a.isLessThan(b)).isFalse();
    }

}
