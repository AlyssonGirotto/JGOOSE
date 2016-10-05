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

    public BPMNArtifact(){
        super();
        setType(BPMNElement.ARTIFACT);
    }
    
    public BPMNArtifact(Integer artifactType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.artifactType = artifactType;
    }

    public Integer getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(Integer artifactType) {
        this.artifactType = artifactType;
    }

    @Override
    public String toString() {
        return "BPMNArtifact{" + "artifactType=" + artifactType + '}' + super.toString();
    }       
}
