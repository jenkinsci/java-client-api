package com.offbytwo.jenkins.model;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hudson.model.ParametersDefinitionProperty")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class ParametersDefinitionProperty {

    @XmlElement(name = "parameterDefinitions")
    private ParameterDefinitions pd;

    public ParametersDefinitionProperty() {
    }

    public ParametersDefinitionProperty(ParameterDefinitions pd) {
        this.pd = pd;
    }

    public ParameterDefinitions getPd() {
        return pd;
    }

    public ParametersDefinitionProperty setPd(ParameterDefinitions pd) {
        this.pd = pd;
        return this;
    }
}
