package com.offbytwo.jenkins.model;

import java.util.List;
import com.offbytwo.jenkins.model.BaseModel;
import com.offbytwo.jenkins.model.TestPackageResult;

/**
 * @author Jasmeen Kaur
 *
 */
public class TestNGReport extends BaseModel {

    private int failCount;
    private int skipCount;
    private int totalCount;
    private String urlName;
    private int duration;
    
    //Following will not get package level test details as "package" String is used and its a restricted keyword in java.Need to  have solution for this.
    private List<TestPackageResult> testPackageResults; 
        

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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<TestPackageResult> getTestPackageResults() {
		return testPackageResults;
	}

	public void setTestPackageResults(List<TestPackageResult> testPackageResults) {
		this.testPackageResults = testPackageResults;
	}
    

}
