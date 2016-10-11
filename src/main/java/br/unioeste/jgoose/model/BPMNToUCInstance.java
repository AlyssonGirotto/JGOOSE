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
public class BPMNToUCInstance {
    private Integer instanceCode;
    private String originator; // Elemento que originou a instância
    private String next; // Próximo elemento
    private Boolean subprocess; // Marcador de sub-processo

    public BPMNToUCInstance() {
    }
    
    public BPMNToUCInstance(Integer instanceID, String originator, String next, Boolean subprocess) {
        this.instanceCode = instanceID;
        this.originator = originator;
        this.next = next;
        this.subprocess = subprocess;
    }

    public Integer getInstanceCode() {
        return instanceCode;
    }

    public void setInstanceCode(Integer instanceCode) {
        this.instanceCode = instanceCode;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Boolean getSubprocess() {
        return subprocess;
    }

    public void setSubprocess(Boolean subprocess) {
        this.subprocess = subprocess;
    }

    @Override
    public String toString() {
        return "\tBPMNToUCInstance{" + 
                "\t\tinstanceID=" + instanceCode + 
                "\t\toriginator=" + originator +
                "\t\tnext=" + next +
                "\t\tsubprocess=" + subprocess + 
                "\t}";
    }        
    
}
