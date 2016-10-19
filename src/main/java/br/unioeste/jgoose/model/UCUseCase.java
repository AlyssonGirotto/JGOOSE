/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

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
   private List<Integer> includedUseCases; //Código dos casos de uso incluídos

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

    public List<Integer> getIncludedUseCases() {
        return includedUseCases;
    }

    public void setIncludedUseCases(List<Integer> includedUseCases) {
        this.includedUseCases = includedUseCases;
    }
   
    @Override
    public String toString(){
        return ("Cod: " + code + " Name: " + name);
    }
   
   
}
