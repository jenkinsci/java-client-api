package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class TestReport extends BaseModel {

    private int failCount;
    private int skipCount;
    private int totalCount;
    private String urlName;
    
    private List<TestChildReport> childReports;

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public List<TestChildReport> getChildReports() {
        return childReports;
    }

    public void setChildReports(List<TestChildReport> childReports) {
        this.childReports = childReports;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((childReports == null) ? 0 : childReports.hashCode());
        result = prime * result + failCount;
        result = prime * result + skipCount;
        result = prime * result + totalCount;
        result = prime * result + ((urlName == null) ? 0 : urlName.hashCode());
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
        TestReport other = (TestReport) obj;
        if (childReports == null) {
            if (other.childReports != null)
                return false;
        } else if (!childReports.equals(other.childReports))
            return false;
        if (failCount != other.failCount)
            return false;
        if (skipCount != other.skipCount)
            return false;
        if (totalCount != other.totalCount)
            return false;
        if (urlName == null) {
            if (other.urlName != null)
                return false;
        } else if (!urlName.equals(other.urlName))
            return false;
        return true;
    }

}
