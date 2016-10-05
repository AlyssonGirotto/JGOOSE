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

    public BPMNParticipant(){
        super();
        setType(BPMNElement.SWIMLANE);
    }
    
    public BPMNParticipant(Integer participantType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.participantType = participantType;
    }

    public Integer getParticipantType() {
        return participantType;
    }

    public void setParticipantType(Integer participantType) {
        this.participantType = participantType;
    }        

    @Override
    public String toString() {
        return "BPMNParticipant{" + "participantType=" + participantType + '}' + super.toString();
    }
}
