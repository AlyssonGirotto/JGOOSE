/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNLink {
    public static final Integer SEQUENCE = 1;
    public static final Integer ASSOCIATION = 2;
    public static final Integer DATA_ASSOCIATION = 3;
    public static final Integer MESSAGE = 4;

    private String code;
    private String label;
    private Integer type;
    private String from;
    private String to;	

    public BPMNLink(String code, String label, int type, String from, String to) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.from = from;
        this.to = to;
    }   

    public BPMNLink() {
        this.code = null;
        this.label = null;
        this.type = null;
        this.from = null;
        this.to = null;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }     

    @Override
    public String toString() {
        return "BPMNLink{" + "code=" + code + ", label=" + label + ", type=" + type + ", from=" + from + ", to=" + to + '}';
    }        
}
