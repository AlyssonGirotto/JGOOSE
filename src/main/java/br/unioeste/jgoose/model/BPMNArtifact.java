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
public class BPMNArtifact extends BPMNElement{
    public static final Integer DATA_OBJECT = 1;
    public static final Integer DATA_STORE = 2;
    public static final Integer GROUP = 3;
    public static final Integer TEXT_ANNOTATION = 4;
    
    private Integer artifactType;
    private ArrayList<String> children; //children's id

    public BPMNArtifact(){
        super();
        setType(BPMNElement.ARTIFACT);
        this.children = new ArrayList<>();
    }

    public Integer getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(Integer artifactType) {
        this.artifactType = artifactType;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public void addChildren(String children){
        this.children.add(children);
    }
    
    @Override
    public String toString() {
        return "BPMNArtifact{" + "artifactType=" + artifactType + " children=" + children + '}' + super.toString();
    }       
}
