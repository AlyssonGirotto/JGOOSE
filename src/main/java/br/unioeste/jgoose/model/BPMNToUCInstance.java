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
    private BPMNElement originator; // Elemento que originou a instância
    private BPMNElement next; // Próximo elemento
    private Boolean subprocess; // Marcador de sub-processo
    private Boolean messageFlow; // Marcador que indica instância proveniente de fluxo de mensagem
    private Boolean finished; // Instância avaliada
    
    public BPMNToUCInstance() {
        messageFlow = false;
        subprocess = false;
        finished = false;
    }
    
    public BPMNToUCInstance(Integer instanceID, BPMNElement originator, BPMNElement next, Boolean subprocess) {
        this.instanceCode = instanceID;
        this.originator = originator;
        this.next = next;
        this.subprocess = subprocess;
        finished = false;
    }

    public Integer getInstanceCode() {
        return instanceCode;
    }

    public void setInstanceCode(Integer instanceCode) {
        this.instanceCode = instanceCode;
    }

    public BPMNElement getOriginator() {
        return originator;
    }

    public void setOriginator(BPMNElement originator) {
        this.originator = originator;
    }

    public BPMNElement getNext() {
        return next;
    }

    public void setNext(BPMNElement next) {
        this.next = next;
    }

    public Boolean getMessageFlow() {
        return messageFlow;
    }

    public void setMessageFlow(Boolean messageFlow) {
        this.messageFlow = messageFlow;
    }        

    public Boolean getSubprocess() {
        return subprocess;
    }

    public void setSubprocess(Boolean subprocess) {
        this.subprocess = subprocess;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    
    @Override
    public String toString() {
        return "\n\tBPMNToUCInstance{" + 
                " - instanceID=" + instanceCode + 
                " - toriginator=" + originator.getLabel() +
                " - next=" + next.getLabel() +
                " - subprocess=" + subprocess + 
                " - finished=" + finished + 
                "\n}";
    }        
    
}
