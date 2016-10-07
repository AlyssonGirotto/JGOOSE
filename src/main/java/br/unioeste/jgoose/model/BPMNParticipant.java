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
public class BPMNParticipant extends BPMNElement{
    public static final Integer POOL = 1;
    public static final Integer LANE = 2;
    
    private Integer participantType;
    private ArrayList<String> children; //children's id

    public BPMNParticipant(){
        super();
        setType(BPMNElement.SWIMLANE);
        this.children = new ArrayList<>();
    }
    
    public BPMNParticipant(Integer participantType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.participantType = participantType;
        this.children = new ArrayList<>();
    }

    public Integer getParticipantType() {
        return participantType;
    }

    public void setParticipantType(Integer participantType) {
        this.participantType = participantType;
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
        return "BPMNParticipant{" + "participantType=" + participantType + " children=" + children + '}' + super.toString();
    }
}
