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
public class BPMNActivity extends BPMNElement{
    public static final Integer TASK = 1;
    public static final Integer SUBPROCESS = 2;
    
    private Integer activityType;

    public BPMNActivity(){
        super();
        setType(BPMNElement.ACTIVITY);
    }
    
    public BPMNActivity(Integer activityType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.activityType = activityType;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }   

    @Override
    public String toString() {
        return "BPMNActivity{" + "activityType=" + activityType + '}' + super.toString();
    }
         
}
