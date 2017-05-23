package com.offbytwo.jenkins.model;

public class JacocoCoverageReport extends BaseModel {

    private JacocoCoverageResult lineCoverage;
    private JacocoCoverageResult classCoverage;
    private JacocoCoverageResult complexityScore;
    private JacocoCoverageResult instructionCoverage;
    private JacocoCoverageResult branchCoverage;

    public JacocoCoverageResult getLineCoverage() {
        return lineCoverage;
    }
    public void setLineCoverage(JacocoCoverageResult lineCoverage) {
        this.lineCoverage = lineCoverage;
    }
    public JacocoCoverageResult getClassCoverage() {
        return classCoverage;
    }
    public void setClassCoverage(JacocoCoverageResult classCoverage) {
        this.classCoverage = classCoverage;
    }
    public JacocoCoverageResult getComplexityScore() {
        return complexityScore;
    }
    public void setComplexityScore(JacocoCoverageResult complexityScore) {
        this.complexityScore = complexityScore;
    }
    public JacocoCoverageResult getInstructionCoverage() {
        return instructionCoverage;
    }
    public void setInstructionCoverage(JacocoCoverageResult instructionCoverage) {
        this.instructionCoverage = instructionCoverage;
    }
    public JacocoCoverageResult getBranchCoverage() {
        return branchCoverage;
    }
    public void setBranchCoverage(JacocoCoverageResult branchCoverage) {
        this.branchCoverage = branchCoverage;
    }

}