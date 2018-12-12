package com.offbytwo.jenkins.model;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "hudson.model.StringParameterDefinition")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "description", "defaultValue" })
public class StringParameterDefinition {

    @XmlElement
    private String name;
    @XmlElement
    private String description;
    @XmlElement
    private String defaultValue;

    public StringParameterDefinition() {
    }

    public StringParameterDefinition(String name, String description, String defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public StringParameterDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StringParameterDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public StringParameterDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
