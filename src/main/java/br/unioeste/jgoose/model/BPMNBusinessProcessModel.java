/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNBusinessProcessModel {
    // Estrutura de dados que armazena elementos e ligações
    private List<BPMNActivity> activities;
    private List<BPMNGateway> gateways;
    private List<BPMNEvent> events;
    private List<BPMNArtifact> artifacts;
    private List<BPMNParticipant> participants;
    private List<BPMNLink> links;

    public BPMNBusinessProcessModel(List<BPMNActivity> activities, List<BPMNGateway> gateways, List<BPMNEvent> events, List<BPMNParticipant> participants, List<BPMNLink> links) {
        this.activities = activities;
        this.artifacts = artifacts;
        this.gateways = gateways;
        this.events = events;
        this.participants = participants;
        this.links = links;
    }

    public BPMNBusinessProcessModel() {
        this.activities = new ArrayList<>();
        this.artifacts = new ArrayList<>();
        this.gateways = new ArrayList<>();
        this.events = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    
    public List<BPMNActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<BPMNActivity> activities) {
        this.activities = activities;
    }

    public List<BPMNGateway> getGateways() {
        return gateways;
    }

    public void setGateways(List<BPMNGateway> gateways) {
        this.gateways = gateways;
    }

    public List<BPMNEvent> getEvents() {
        return events;
    }

    public void setEvents(List<BPMNEvent> events) {
        this.events = events;
    }

    public List<BPMNArtifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<BPMNArtifact> artifacts) {
        this.artifacts = artifacts;
    }

    public List<BPMNParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BPMNParticipant> participants) {
        this.participants = participants;
    }

    public List<BPMNLink> getLinks() {
        return links;
    }

    public void setLinks(List<BPMNLink> links) {
        this.links = links;
    }

    public void addEvent(BPMNEvent event){
        this.events.add(event);
    }
    
    public void addGateway(BPMNGateway gateway){
        this.gateways.add(gateway);
    }
    
    public void addArtifact(BPMNArtifact artifact){
        this.artifacts.add(artifact);
    }
    
    public void addActivity(BPMNActivity activity){
        this.activities.add(activity);
    }
    
    public void addParticipant(BPMNParticipant participant){
        this.participants.add(participant);
    }
    
    public void addLink(BPMNLink link){
        this.links.add(link);
    }
    
    @Override
    public String toString() {
        return "BPMNBusinessProcessModel{" + "activities=" + activities + ", gateways=" + gateways + ", events=" + events + ", artifacts=" + artifacts + ", participants=" + participants + ", links=" + links + '}';
    }
}
