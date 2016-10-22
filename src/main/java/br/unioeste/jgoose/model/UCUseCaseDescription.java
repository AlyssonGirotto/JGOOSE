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
public class UCUseCaseDescription {
    private String name;
    private String goal;
    private String scope;
    private String preConditions;
    private String endSucess;
    private String endFailure;
    private String trigger;
    private String primaryActor;
    private List<String> scenario;
    private String priority;
    private String performance;
    private String frequency;
    private String useCaseFather;
    private String includedUseCases;
    private String secondaryActors;
    private String additionalInformation;

    public UCUseCaseDescription() {
        scenario = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getPreConditions() {
        return preConditions;
    }

    public void setPreConditions(String preConditions) {
        this.preConditions = preConditions;
    }

    public String getEndSucess() {
        return endSucess;
    }

    public void setEndSucess(String endSucess) {
        this.endSucess = endSucess;
    }

    public String getEndFailure() {
        return endFailure;
    }

    public void setEndFailure(String endFailure) {
        this.endFailure = endFailure;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getPrimaryActor() {
        return primaryActor;
    }

    public void setPrimaryActor(String primaryActor) {
        this.primaryActor = primaryActor;
    }

    public List<String> getScenario() {
        return scenario;
    }

    public void setScenario(List<String> scenario) {
        this.scenario = scenario;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUseCaseFather() {
        return useCaseFather;
    }

    public void setUseCaseFather(String useCaseFather) {
        this.useCaseFather = useCaseFather;
    }

    public String getIncludedUseCases() {
        return includedUseCases;
    }

    public void setIncludedUseCases(String includedUseCases) {
        this.includedUseCases = includedUseCases;
    }

    public String getSecondaryActors() {
        return secondaryActors;
    }

    public void setSecondaryActors(String secondaryActors) {
        this.secondaryActors = secondaryActors;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }            

    @Override
    public String toString() {
        return "UCUseCaseDescription{" + "name=" + name + ", goal=" + goal + ", scope=" + scope + ", preConditions=" + preConditions + ", endSucess=" + endSucess + ", endFailure=" + endFailure + ", trigger=" + trigger + ", primaryActor=" + primaryActor + ", scenario=" + scenario + ", priority=" + priority + ", performance=" + performance + ", frequency=" + frequency + ", useCaseFather=" + useCaseFather + ", includedUseCases=" + includedUseCases + ", secondaryActors=" + secondaryActors + ", additionalInformation=" + additionalInformation + '}';
    }
    
    
}
