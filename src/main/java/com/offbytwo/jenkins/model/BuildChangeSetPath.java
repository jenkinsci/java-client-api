package com.offbytwo.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class BuildChangeSetPath {

    /**
     * Usually {@code edit} etc.
     */
    // TODO: Think about it if its possible to use an enum type?
    private String editType; // edit, ?
    private String file;

    public String getEditType() {
        return editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((editType == null) ? 0 : editType.hashCode());
        result = prime * result + ((file == null) ? 0 : file.hashCode());
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
        BuildChangeSetPath other = (BuildChangeSetPath) obj;
        if (editType == null) {
            if (other.editType != null)
                return false;
        } else if (!editType.equals(other.editType))
            return false;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        return true;
    }

}
