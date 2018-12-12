package com.offbytwo.jenkins.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parameterDefinitions")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterDefinitions {

    @XmlElement(name = "hudson.model.StringParameterDefinition")
    List<StringParameterDefinition> stringParams;

    public ParameterDefinitions() {
        stringParams = new ArrayList<StringParameterDefinition>();
    }

    public ParameterDefinitions(List<StringParameterDefinition> stringParams) {
        this.stringParams = stringParams;
    }

    public List<StringParameterDefinition> getStringParams() {
        return stringParams;
    }

    public ParameterDefinitions setStringParams(List<StringParameterDefinition> stringParams) {
        this.stringParams = stringParams;
        return this;
    }

    public ParameterDefinitions addParam(StringParameterDefinition spd) {
        stringParams.add(spd);
        return this;
    }
}
