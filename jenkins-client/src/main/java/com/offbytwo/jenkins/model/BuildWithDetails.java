/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.offbytwo.jenkins.helper.BuildConsoleStreamListener;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.collect.Collections2.filter;

/**
 * This class represents build information with details about what has been done
 * like duration start and of course the build result.
 *
 */
public class BuildWithDetails extends Build {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public final static String TEXT_SIZE_HEADER = "x-text-size";
    public final static String MORE_DATA_HEADER = "x-more-data";

    /**
     * This will be returned by the API in cases where the build has never run.
     * For example {@link Build#BUILD_HAS_NEVER_RUN}
     */
    public static final BuildWithDetails BUILD_HAS_NEVER_RUN = new BuildWithDetails() {

        @Override
        public List getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<Artifact> getArtifacts() {
            return Collections.<Artifact>emptyList();
        }

        @Override
        public List<BuildCause> getCauses() {
            return Collections.<BuildCause>emptyList();
        }

        @Override
        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.<BuildChangeSetAuthor>emptyList();
        }

        @Override
        public BuildResult getResult() {
            return BuildResult.NOT_BUILT;
        }

    };

    /**
     * This will be returned by the API in cases where the build has been
     * cancelled. For example {@link Build#BUILD_HAS_BEEN_CANCELLED}
     */
    public static final BuildWithDetails BUILD_HAS_BEEN_CANCELLED = new BuildWithDetails() {

        @Override
        public List getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<Artifact> getArtifacts() {
            return Collections.<Artifact>emptyList();
        }

        @Override
        public List<BuildCause> getCauses() {
            return Collections.<BuildCause>emptyList();
        }

        @Override
        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.<BuildChangeSetAuthor>emptyList();
        }

        @Override
        public BuildResult getResult() {
            return BuildResult.CANCELLED;
        }

    };

    private List actions; // TODO: Should be improved.
    private boolean building;
    private String description;
    private String displayName;
    private long duration;
    private long estimatedDuration;
    private String fullDisplayName;
    private String id;
    private long timestamp;
    private BuildResult result;
    private List<Artifact> artifacts;
    private String consoleOutputText;
    private String consoleOutputHtml;
    private BuildChangeSet changeSet;
    private String builtOn;
    private List<BuildChangeSetAuthor> culprits;

    public BuildWithDetails() {
        // Default ctor is needed to jackson.
    }

    public BuildWithDetails(BuildWithDetails details) {
        this.actions = details.actions;
        this.description = details.description;
        this.displayName = details.displayName;
        this.building = details.building;
        this.duration = details.duration;
        this.estimatedDuration = details.estimatedDuration;
        this.fullDisplayName = details.fullDisplayName;
        this.id = details.id;
        this.timestamp = details.timestamp;
        this.result = details.result;
        this.artifacts = details.artifacts;
        this.consoleOutputHtml = details.consoleOutputHtml;
        this.consoleOutputText = details.consoleOutputText;
        this.changeSet = details.changeSet;
        this.builtOn = details.builtOn;
        this.culprits = details.culprits;
        this.setClient(details.getClient());
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public boolean isBuilding() {
        return building;
    }

    public List<BuildCause> getCauses() {
        // actions is a List<Map<String, List<Map<String, String ..
        // we have a List[i]["causes"] -> List[BuildCause]
        Collection causes = filter(actions, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("causes");
            }
        });

        List<BuildCause> result = new ArrayList<BuildCause>();

        if (causes != null && !causes.isEmpty()) {
            // The underlying key-value can be either a <String, Integer> or a
            // <String, String>.
            List<Map<String, Object>> causes_blob = ((Map<String, List<Map<String, Object>>>) causes.toArray()[0])
                    .get("causes");
            for (Map<String, Object> cause : causes_blob) {

                BuildCause convertToBuildCause = convertToBuildCause(cause);

                result.add(convertToBuildCause);
            }
        }

        return result;
    }

    /**
     * Update <code>displayName</code> and the <code>description</code> of a
     * build.
     * 
     * @param displayName The new displayName which should be set.
     * @param description The description which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public void updateDisplayNameAndDescription(String displayName, String description, boolean crumbFlag)
            throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        Objects.requireNonNull(description, "description is not allowed to be null.");
        // TODO: Check what the "core:apply" means?
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description,
                "core:apply", "", "Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    /**
     * Update <code>displayName</code> and the <code>description</code> of a
     * build.
     * 
     * @param displayName The new displayName which should be set.
     * @param description The description which should be set.
     * @throws IOException in case of errors.
     */
    public void updateDisplayNameAndDescription(String displayName, String description) throws IOException {
        updateDisplayNameAndDescription(displayName, description, false);
    }

    /**
     * Update <code>displayName</code> of a build.
     * 
     * @param displayName The new displayName which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public void updateDisplayName(String displayName, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        String description = getDescription() == null ? "" : getDescription();
        // TODO: Check what the "core:apply" means?
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description,
                "core:apply", "", "Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    /**
     * Update <code>displayName</code> of a build.
     * 
     * @param displayName The new displayName which should be set.
     * @throws IOException in case of errors.
     */
    public void updateDisplayName(String displayName) throws IOException {
        updateDisplayName(displayName, false);
    }

    /**
     * Update the <code>description</code> of a build.
     * 
     * @param description The description which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public void updateDescription(String description, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(description, "description is not allowed to be null.");
        String displayName = getDisplayName() == null ? "" : getDisplayName();
        // TODO: Check what the "core:apply" means?
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description,
                "core:apply", "", "Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    /**
     * Update the <code>description</code> of a build.
     * 
     * @param description The description which should be set.
     * @throws IOException in case of errors.
     */
    public void updateDescription(String description) throws IOException {
        updateDescription(description, false);
    }

    private BuildCause convertToBuildCause(Map<String, Object> cause) {
        BuildCause cause_object = new BuildCause();

        // TODO: Think about it. Can this be done more simpler?
        String description = (String) cause.get("shortDescription");
        if (!Strings.isNullOrEmpty(description)) {
            cause_object.setShortDescription(description);
        }

        Integer upstreamBuild = (Integer) cause.get("upstreamBuild");
        if (upstreamBuild != null) {
            cause_object.setUpstreamBuild(upstreamBuild);
        }

        String upstreamProject = (String) cause.get("upstreamProject");
        if (!Strings.isNullOrEmpty(upstreamProject)) {
            cause_object.setUpstreamProject(upstreamProject);
        }

        String upstreamUrl = (String) cause.get("upstreamUrl");
        if (!Strings.isNullOrEmpty(upstreamProject)) {
            cause_object.setUpstreamUrl(upstreamUrl);
        }

        String userId = (String) cause.get("userId");
        if (!Strings.isNullOrEmpty(userId)) {
            cause_object.setUserId(userId);
        }

        String userName = (String) cause.get("userName");
        if (!Strings.isNullOrEmpty(userName)) {
            cause_object.setUserName(userName);
        }
        return cause_object;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BuildResult getResult() {
        return result;
    }

    public String getBuiltOn() {
        return builtOn;
    }

    public List getActions() {
        return actions;
    }

    public Map<String, String> getParameters() {
        Collection parameters = filter(actions, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("parameters");
            }
        });

        Map<String, String> params = new HashMap<String, String>();

        if (parameters != null && !parameters.isEmpty()) {
            for (Map<String, Object> param : ((Map<String, List<Map<String, Object>>>) parameters.toArray()[0])
                    .get("parameters")) {
                String key = (String) param.get("name");
                Object value = param.get("value");
                params.put(key, String.valueOf(value));
            }
        }

        return params;
    }

    /**
     * @return The full console output of the build. The line separation is done by
     *         {@code CR+LF}.
     *
     * @see streamConsoleOutput method for obtaining logs for running build
     *
     * @throws IOException in case of a failure.
     */
    public String getConsoleOutputText() throws IOException {
        return client.get(getUrl() + "/logText/progressiveText");
    }

    /**
     * The full console output with HTML.
     *
     * @see streamConsoleOutput method for obtaining logs for running build
     *
     * @return The console output as HTML.
     * @throws IOException in case of an error.
     */
    public String getConsoleOutputHtml() throws IOException {
        return client.get(getUrl() + "/logText/progressiveHtml");
    }


    /**
     * Stream build console output log as text using BuildConsoleStreamListener
     * Method can be used to asynchronously obtain logs for running build.
     *
     * @param listener interface used to asynchronously obtain logs
     * @param poolingInterval interval (seconds) used to pool jenkins for logs
     * @param poolingTimeout pooling timeout (seconds) used to break pooling in case build stuck
     *
     */
    public void streamConsoleOutput(final BuildConsoleStreamListener listener, final int poolingInterval, final int poolingTimeout) throws InterruptedException, IOException {
        // Calculate start and timeout
        final long startTime = System.currentTimeMillis();
        final long timeoutTime = startTime + (poolingTimeout * 1000);

        int bufferOffset = 0;
        while (true) {
            Thread.sleep(poolingInterval * 1000);

            ConsoleLog consoleLog = null;
            consoleLog = getConsoleOutputText(bufferOffset);
            String logString = consoleLog.getConsoleLog();
            if (logString != null && !logString.isEmpty()) {
                listener.onData(logString);
            }
            if (consoleLog.getHasMoreData()) {
                bufferOffset = consoleLog.getCurrentBufferSize();
            } else {
                listener.finished();
                break;
            }
            long currentTime = System.currentTimeMillis();

            if (currentTime > timeoutTime) {
                LOGGER.warn("Pooling for build {0} for {2} timeout! Check if job stuck in jenkins",
                        BuildWithDetails.this.getDisplayName(), BuildWithDetails.this.getNumber());
                break;
            }
        }
    }

    /**
     * Get build console output log as text.
     * Use this method to periodically obtain logs from jenkins and skip chunks that were already received
     *
     * @param bufferOffset offset in console lo
     * @return ConsoleLog object containing console output of the build. The line separation is done by
     * {@code CR+LF}.
     * @throws IOException in case of a failure.
     */
    public ConsoleLog getConsoleOutputText(int bufferOffset) throws IOException {
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("start", Integer.toString(bufferOffset)));
        String path = getUrl() + "logText/progressiveText";
        HttpResponse httpResponse = client.post_form_with_result(path, formData, false);

        Header moreDataHeader = httpResponse.getFirstHeader(MORE_DATA_HEADER);
        Header textSizeHeader = httpResponse.getFirstHeader(TEXT_SIZE_HEADER);
        String response = EntityUtils.toString(httpResponse.getEntity());
        boolean hasMoreData = false;
        if (moreDataHeader != null) {
            hasMoreData = Boolean.TRUE.toString().equals(moreDataHeader.getValue());
        }
        Integer currentBufferSize = bufferOffset;
        if (textSizeHeader != null) {
            try {
                currentBufferSize = Integer.parseInt(textSizeHeader.getValue());
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot parse buffer size for job {0} build {1}. Using current offset!", this.getDisplayName(), this.getNumber());
            }
        }
        return new ConsoleLog(response, hasMoreData, currentBufferSize);
    }


    public BuildChangeSet getChangeSet() {
        return changeSet;
    }

    public void setChangeSet(BuildChangeSet changeSet) {
        this.changeSet = changeSet;
    }

    public List<BuildChangeSetAuthor> getCulprits() {
        return culprits;
    }

    public void setCulprits(List<BuildChangeSetAuthor> culprits) {
        this.culprits = culprits;
    }

    public void setResult(BuildResult result) {
        this.result = result;
    }

    public InputStream downloadArtifact(Artifact a) throws IOException, URISyntaxException {
        // We can't just put the artifact's relative path at the end of the url
        // string, as there could be characters that need to be escaped.
        URI uri = new URI(getUrl());
        String artifactPath = uri.getPath() + "artifact/" + a.getRelativePath();
        URI artifactUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), artifactPath, "",
                "");
        return client.getFile(artifactUri);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildWithDetails other = (BuildWithDetails) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (artifacts == null) {
            if (other.artifacts != null)
                return false;
        } else if (!artifacts.equals(other.artifacts))
            return false;
        if (building != other.building)
            return false;
        if (builtOn == null) {
            if (other.builtOn != null)
                return false;
        } else if (!builtOn.equals(other.builtOn))
            return false;
        if (changeSet == null) {
            if (other.changeSet != null)
                return false;
        } else if (!changeSet.equals(other.changeSet))
            return false;
        if (consoleOutputHtml == null) {
            if (other.consoleOutputHtml != null)
                return false;
        } else if (!consoleOutputHtml.equals(other.consoleOutputHtml))
            return false;
        if (consoleOutputText == null) {
            if (other.consoleOutputText != null)
                return false;
        } else if (!consoleOutputText.equals(other.consoleOutputText))
            return false;
        if (culprits == null) {
            if (other.culprits != null)
                return false;
        } else if (!culprits.equals(other.culprits))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (duration != other.duration)
            return false;
        if (estimatedDuration != other.estimatedDuration)
            return false;
        if (fullDisplayName == null) {
            if (other.fullDisplayName != null)
                return false;
        } else if (!fullDisplayName.equals(other.fullDisplayName))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (result != other.result)
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((artifacts == null) ? 0 : artifacts.hashCode());
        result = prime * result + (building ? 1231 : 1237);
        result = prime * result + ((builtOn == null) ? 0 : builtOn.hashCode());
        result = prime * result + ((changeSet == null) ? 0 : changeSet.hashCode());
        result = prime * result + ((consoleOutputHtml == null) ? 0 : consoleOutputHtml.hashCode());
        result = prime * result + ((consoleOutputText == null) ? 0 : consoleOutputText.hashCode());
        result = prime * result + ((culprits == null) ? 0 : culprits.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + (int) (estimatedDuration ^ (estimatedDuration >>> 32));
        result = prime * result + ((fullDisplayName == null) ? 0 : fullDisplayName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
