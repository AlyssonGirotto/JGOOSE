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
    private ArrayList<String> children; //children's id
        
    public BPMNActivity(){
        super();
        this.children = new ArrayList<>();
        this.setType(BPMNElement.ACTIVITY);
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
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
        return "BPMNActivity{" + "activityType=" + activityType + " children=" + children + '}' + super.toString();
    }
         
}
