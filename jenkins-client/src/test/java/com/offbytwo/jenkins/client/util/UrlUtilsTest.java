/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client.util;

import com.offbytwo.jenkins.model.FolderJob;
import java.net.URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;



/**
 *
 * @author Dell Green
 */
public class UrlUtilsTest {

    

    @Test
    public void testToBaseUrl_NullFolderJob() {
        assertEquals("/", UrlUtils.toBaseUrl(null));
    }

    @Test
    public void testToBaseUrl_DefaultFolderJob() {
        assertNull("/", UrlUtils.toBaseUrl(new FolderJob()));
    }

    
    @Test
    public void testToBaseUrl() {
        final String fpath = "http://localhost/jobs/someFolder/";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        assertEquals(fpath, UrlUtils.toBaseUrl(folderJob));
    }
    
    
    @Test
    public void testToJobBaseUrl() {
        final String fpath = "http://localhost/jobs/someFolder/";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/job/someJob";
        assertEquals(expected, UrlUtils.toJobBaseUrl(folderJob, "someJob"));
    }
    
    @Test
    public void testToJobBaseUrl_NoTrailingFolderSlash() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/job/someJob";
        assertEquals(expected, UrlUtils.toJobBaseUrl(folderJob, "someJob"));
    }
    
    
    @Test
    public void testToJobBaseUrl_NullFolderJob() {
        assertEquals("/job/someJob", UrlUtils.toJobBaseUrl(null, "someJob"));
    }
    
    
    @Test
    public void testToJobBaseUrl_EmptyJobName() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/job/";
        assertEquals(expected, UrlUtils.toJobBaseUrl(folderJob, ""));
    }
    
    @Test(expected = NullPointerException.class)
    public void testToJobBaseUrl_NullJobName() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        UrlUtils.toJobBaseUrl(folderJob, null);
    }
    
    
    @Test
    public void testToViewBaseUrl() {
        final String fpath = "http://localhost/jobs/someFolder/";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/view/someView";
        assertEquals(expected, UrlUtils.toViewBaseUrl(folderJob, "someView"));
    }
    
    @Test
    public void testToViewBaseUrl_NoTrailingFolderSlash() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/view/someView";
        assertEquals(expected, UrlUtils.toViewBaseUrl(folderJob, "someView"));
    }
    
    
    @Test
    public void testToViewBaseUrl_NullFolderJob() {
        assertEquals("/view/someView", UrlUtils.toViewBaseUrl(null, "someView"));
    }
    
    
    @Test
    public void testToViewBaseUrl_EmptyViewName() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        final String expected = "http://localhost/jobs/someFolder/view/";
        assertEquals(expected, UrlUtils.toViewBaseUrl(folderJob, ""));
    }
    
    @Test(expected = NullPointerException.class)
    public void testToViewBaseUrl_NullViewName() {
        final String fpath = "http://localhost/jobs/someFolder";
        final FolderJob folderJob = new FolderJob("someFolder", fpath);
        UrlUtils.toViewBaseUrl(folderJob, null);
    }
    
    
    @Test
    public void testToFullJobPath_JobNameOnly() {
        assertEquals("someJob", UrlUtils.toFullJobPath("someJob"));
    }
    
    
    @Test
    public void testToFullJobPath_JobNameWithSingleFolder() {
        final String expected = "someFolder/job/someJob";
        assertEquals(expected, UrlUtils.toFullJobPath("someFolder/someJob"));
    }
    
    @Test
    public void testToFullJobPath_JobNameWithMultipleFolders() {
        final String expected = "someFolder1/job/someFolder2/job/someJob";
        assertEquals(expected, UrlUtils.toFullJobPath("someFolder1/someFolder2/someJob"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testToFullJobPath_NullJobName() {
        UrlUtils.toFullJobPath(null);
    }
    
    
    @Test
    public void testToFullJobPath_EmptyJobName() {
        assertEquals("", UrlUtils.toFullJobPath(""));
    }
    
    
    @Test
    public void testJoin_EmptyBothPaths() {
        assertEquals("", UrlUtils.join("", ""));
    }
    
    @Test
    public void testJoin_SlashesOnly() {
        assertEquals("/", UrlUtils.join("/", "/"));
    }
    
    @Test
    public void testJoin_EmptyPath2() {
        assertEquals("1/2/3", UrlUtils.join("1/2/3", ""));
    }
    
    @Test
    public void testJoin_EmptyPath1() {
        assertEquals("4/5/6", UrlUtils.join("", "4/5/6"));
    }
    
    @Test
    public void testJoin_NoTrailingLeadingSlashes() {
        assertEquals("1/2/3/4/5/6", UrlUtils.join("1/2/3", "4/5/6"));
        
    }
    
    @Test
    public void testJoin_TrailingLeadingSlashes() {
        assertEquals("/1/2/3/4/5/6/", UrlUtils.join("/1/2/3/", "/4/5/6/"));
    }
    
    @Test
    public void testJoin_Path1Trailing_Path2NoLeading() {
        assertEquals("/1/2/3/4/5/6/", UrlUtils.join("/1/2/3/", "4/5/6/"));
    }
    
    @Test
    public void testJoin_Path1NoTrailing_Path2Leading() {
        assertEquals("/1/2/3/4/5/6/", UrlUtils.join("/1/2/3", "/4/5/6/"));
    }
    
    
    
    
    
    @Test(expected = NullPointerException.class)
    public void testJoin_NullPath1() {
        UrlUtils.join(null, "4/5/6");
    }
    
    @Test(expected = NullPointerException.class)
    public void testJoin_NullPath2() {
        UrlUtils.join("1/2/3", null);
    }
    
    
    @Test
    public void testToQueryUri_WithoutQuery() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "jenkins", "job/somejob");
        final String expected = "http://localhost/jenkins/job/somejob/api/json";
        assertEquals(expected, uri.toString());
    }
    
    @Test
    public void testToQueryUri_EmptyContext() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "", "job/somejob");
        final String expected = "http://localhost/job/somejob/api/json";
        assertEquals(expected, uri.toString());
    }
    
    @Test
    public void testToQueryUri_EmptyPathAndContext() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "", "");
        final String expected = "http://localhost/api/json";
        assertEquals(expected, uri.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testToQueryUri_NullUri() throws Exception {
        UrlUtils.toJsonApiUri(null, "jenkins", "job/somejob");
    }
     
    
    @Test(expected = NullPointerException.class)
    public void testToQueryUri_NullPath() throws Exception {
        UrlUtils.toJsonApiUri(new URI("http://localhost/jenkins"), "jenkins", null);
    }
        
    @Test(expected = NullPointerException.class)
    public void testToQueryUri_NullContext() throws Exception {
        UrlUtils.toJsonApiUri(new URI("http://localhost/jenkins"), null, "job/ajob");
    }
    
    @Test
    public void testToQueryUri_EmptyPath() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "jenkins", "");
        final String expected = "http://localhost/jenkins/api/json";
        assertEquals(expected, uri.toString());
    }
    
    @Test
    public void testToQueryUri_UpperCase() throws Exception {
        final String suri = "HTTP://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "jenkins", "job/somejob");
        final String expected = "HTTP://localhost/jenkins/job/somejob/api/json";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test
    public void testToQueryUri_WithQuery() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "jenkins", "job/somejob?pretty=true");
        final String expected = "http://localhost/jenkins/job/somejob/api/json?pretty=true";
        assertEquals(expected, uri.toString());
    }
    
    
    
    @Test
    public void testToQueryUri_PathContainsSchemeAndContext() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toJsonApiUri(new URI(suri), "jenkins", "http://localhost/jenkins/job/somejob");
        final String expected = "http://localhost/jenkins/job/somejob/api/json";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test
    public void testToNoQueryUri_PathContainsSchemeAndContext() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toNoApiUri(new URI(suri), "jenkins", "http://localhost/jenkins/job/somejob");
        final String expected = "http://localhost/jenkins/job/somejob";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test
    public void testToNoQueryUri_UpperCase() throws Exception {
        final String suri = "HTTP://localhost/jenkins";
        final URI uri = UrlUtils.toNoApiUri(new URI(suri), "jenkins", "job/somejob");
        final String expected = "HTTP://localhost/jenkins/job/somejob";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test
    public void testToNoQueryUri_EmptyPath() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toNoApiUri(new URI(suri), "jenkins", "");
        final String expected = "http://localhost/jenkins";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test
    public void testToNoQueryUri_EmptyContext() throws Exception {
        final String suri = "http://localhost/jenkins";
        final URI uri = UrlUtils.toNoApiUri(new URI(suri), "", "job/somejob");
        final String expected = "http://localhost/job/somejob";
        assertEquals(expected, uri.toString());
    }
    
    
    @Test(expected = NullPointerException.class)
    public void testToNoQueryUri_NullContext() throws Exception {
        UrlUtils.toNoApiUri(new URI("http://localhost/jenkins"), null, "job/ajob");
    }
    
}