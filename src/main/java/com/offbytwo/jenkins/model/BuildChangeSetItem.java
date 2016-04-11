/**
 *
 */
package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * Build ChangeSet Item
 *
 * @author Karl Heinz Marbaise
 *
 */
public class BuildChangeSetItem {

    private List<String> affectedPaths;
    private String commitId;
    private String timestamp; // May be we should date/?
    private BuildChangeSetAuthor author;
    private String comment;
    private String date; // Better use Date
    private String id; // seemed to be the same as the commitId?
    private String msg; // Message difference to comment?
    private List<BuildChangeSetPath> paths;

    public List<String> getAffectedPaths() {
        return affectedPaths;
    }

    public void setAffectedPaths(List<String> affectedPaths) {
        this.affectedPaths = affectedPaths;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BuildChangeSetPath> getPaths() {
        return paths;
    }

    public void setPaths(List<BuildChangeSetPath> paths) {
        this.paths = paths;
    }

    public BuildChangeSetAuthor getAuthor() {
        return author;
    }

    public void setAuthor(BuildChangeSetAuthor author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((affectedPaths == null) ? 0 : affectedPaths.hashCode());
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((msg == null) ? 0 : msg.hashCode());
        result = prime * result + ((paths == null) ? 0 : paths.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildChangeSetItem other = (BuildChangeSetItem) obj;
        if (affectedPaths == null) {
            if (other.affectedPaths != null)
                return false;
        } else if (!affectedPaths.equals(other.affectedPaths))
            return false;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (commitId == null) {
            if (other.commitId != null)
                return false;
        } else if (!commitId.equals(other.commitId))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (msg == null) {
            if (other.msg != null)
                return false;
        } else if (!msg.equals(other.msg))
            return false;
        if (paths == null) {
            if (other.paths != null)
                return false;
        } else if (!paths.equals(other.paths))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }

}
