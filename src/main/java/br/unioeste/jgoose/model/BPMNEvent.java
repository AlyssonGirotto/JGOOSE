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
public class BPMNEvent extends BPMNElement{
    public static final Integer START = 1;
    public static final Integer INTERMEDIATE = 2;
    public static final Integer END = 3;
    public static final Integer END_CANCEL = 4;
    public static final Integer END_COMPENSATION = 5;
    public static final Integer END_ERROR = 6;
    public static final Integer END_LINK = 7;
    public static final Integer END_MESSAGE = 8;
    public static final Integer END_MULTIPLE = 9;
    public static final Integer END_TERMINATE = 10;
    public static final Integer INTERMEDIATE_CANCEL = 11;
    public static final Integer INTERMEDIATE_COMPENSATION = 12;
    public static final Integer INTERMEDIATE_ERROR = 13;
    public static final Integer INTERMEDIATE_LINK = 14;
    public static final Integer INTERMEDIATE_MESSAGE = 15;
    public static final Integer INTERMEDIATE_MULTIPLE = 16;
    public static final Integer INTERMEDIATE_RULE = 17;
    public static final Integer INTERMEDIATE_TIMER = 18;
    public static final Integer START_LINK = 19;
    public static final Integer START_MESSAGE = 20;
    public static final Integer START_MULTIPLE = 21;
    public static final Integer START_RULE = 22;
    public static final Integer START_TIMER = 23;
    
    private Integer eventType;

    public BPMNEvent(){
        super();
        setType(BPMNElement.EVENT);
    }
    
    public BPMNEvent(Integer eventType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.eventType = eventType;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }         

    @Override
    public String toString() {
        return "BPMNEvent{" + "eventType=" + eventType + '}' + super.toString();
    }  
    
}
