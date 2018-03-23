package com.offbytwo.jenkins.model;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.Assert.*;


import org.junit.Test;


import com.fasterxml.jackson.databind.ObjectMapper;

public class ChangeSetTest {

    private BuildWithDetails getBuildFromJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(json, BuildWithDetails.class);
    }

    final String changeSetExampleJson = "    {" + 
            "      \"_class\" : \"hudson.plugins.git.GitChangeSetList\"," + 
            "      \"items\" : [" + 
            "        {" + 
            "          \"_class\" : \"hudson.plugins.git.GitChangeSet\"," + 
            "          \"affectedPaths\" : [" + 
            "            \"README.md\"" + 
            "          ]," + 
            "          \"commitId\" : \"ba40ff32c60f692918c1d51f5c80842124ed04af\"," + 
            "          \"timestamp\" : 1519306808000," + 
            "          \"author\" : {" + 
            "            \"absoluteUrl\" : \"https://my.jenkins/user/john.doe\","
            + "            \"fullName\" : \"john.doe\"" +
            "          }," + 
            "          \"authorEmail\" : \"john.doe@flap.com\","
            + 
            "          \"comment\" : \"longer\\ncommit message\\n\"," + 
            "          \"date\" : \"2018-02-22 13:40:08 +0000\"," + 
            "          \"id\" : \"ba40ff32c60f692918c1d51f5c80842124ed04af\"," + 
            "          \"msg\" : \"longer\"," + 
            "          \"paths\" : [" + 
            "            {" + 
            "              \"editType\" : \"edit\"," + 
            "              \"file\" : \"README.md\"" + 
            "            }" + 
            "          ]" + 
            "        }" + 
            "      ]," + 
            "      \"kind\" : \"git\"" + 
            "    }";
            

    @Test
    public void getChangeSet__forBuildWithChangeSetAsSingleObject() throws Exception {
        String json = String.format("{ \"changeSet\" : %s }", changeSetExampleJson);

        BuildWithDetails examinee = getBuildFromJson(json);

        BuildChangeSetItem item = examinee.getChangeSet().getItems().get(0);
        assertEquals(item.getAuthor().getFullName(), "john.doe");

    }

    @Test
    public void getChangeSet__forBuildWithChangeSetsAsList() throws Exception {
        String json = String.format("{ \"changeSets\" : [ %s ] }", changeSetExampleJson);

        BuildWithDetails examinee = getBuildFromJson(json);

        BuildChangeSetItem item = examinee.getChangeSet().getItems().get(0);
        assertEquals(item.getAuthor().getFullName(), "john.doe");

        assertEquals(examinee.getChangeSets().size(), 1);
    }
}
