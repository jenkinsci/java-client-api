package com.offbytwo.jenkins.model;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@gmail.com>
 */
public class Crumb extends BaseModel {
    private String crumbRequestField;
    private String crumb;

    public Crumb() {}

    public Crumb(String crumbRequestField, String crumb) {
        this.crumbRequestField = crumbRequestField;
        this.crumb = crumb;
    }

    public String getCrumbRequestField() {
        return crumbRequestField;
    }

    public String getCrumb() {
        return crumb;
    }
}
