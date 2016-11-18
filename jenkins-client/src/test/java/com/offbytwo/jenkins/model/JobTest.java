package com.offbytwo.jenkins.model;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class JobTest
{
  @Test
  public void testRelativeUrl() {
    Job sut = new Job("job1", "http://jenkins3.example.net/job/data/job/bva-dudu-build/");
    
    assertThat(sut.getName()).isEqualTo("job1");
    assertThat(sut.getUrl()).isEqualTo("http://jenkins3.example.net/job/data/job/bva-dudu-build/");
    assertThat(sut.getRelativeUrl()).isEqualTo("/job/data/job/bva-dudu-build/");
  }
}
