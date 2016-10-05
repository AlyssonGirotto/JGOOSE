/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

import java.util.ArrayList;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNElement {
    public static final Integer EVENT = 1;
    public static final Integer GATEWAY = 2;
    public static final Integer ARTIFACT = 3;
    public static final Integer ACTIVITY = 4;
    public static final Integer SWIMLANE = 5;
    
    private String code;
    private String label;
    private Integer type;
    private String parent; //father's element
    private ArrayList<String> links; //links codes

    public BPMNElement() {
        this.links = new ArrayList<>();
    }

    public BPMNElement(String code, String label, Integer type, String parent, ArrayList<String> links) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.parent = parent;
        this.links = links;
    }    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }       

    @Override
    public String toString() {
        return "BPMNElement{" + "code=" + code + ", label=" + label + ", type=" + type + ", parent=" + parent + ", links=" + links + '}';
    }
}
