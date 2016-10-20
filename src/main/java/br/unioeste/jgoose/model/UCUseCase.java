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
public class UCUseCase {

    private Integer code;
    private String name;
    private String guidelineUsed; //Diretriz utilizada
    private Integer instanceCod;
    private String bpmnElementCode;
    private List<UCUseCase> includedUseCases; //Código dos casos de uso incluídos

    public UCUseCase() {
        includedUseCases = new ArrayList<>();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuidelineUsed() {
        return guidelineUsed;
    }

    public void setGuidelineUsed(String guidelineUsed) {
        this.guidelineUsed = guidelineUsed;
    }

    public Integer getInstanceCod() {
        return instanceCod;
    }

    public void setInstanceCod(Integer instanceCod) {
        this.instanceCod = instanceCod;
    }

    public String getBpmnElementCode() {
        return bpmnElementCode;
    }

    public void setBpmnElementCode(String bpmnElementCode) {
        this.bpmnElementCode = bpmnElementCode;
    }

    public List<UCUseCase> getIncludedUseCases() {
        return includedUseCases;
    }

    public void setIncludedUseCases(List<UCUseCase> includedUseCases) {
        this.includedUseCases = includedUseCases;
    }
    
    public void addIncludedUseCase(UCUseCase useCase){
        this.includedUseCases.add(useCase);
    }

    @Override
    public String toString() {
        return ("Cod: " + code + " Name: " + name);
    }
    
    public String printAllInfo(){
        return "\n\tCode=" + code + " Name=" + name + " guidelineUsed=" + guidelineUsed + " instanceCod=" + instanceCod + " bpmnElementCode=" + bpmnElementCode + " includedUseCases=" + includedUseCases + '}';
    }
   
}
