/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

import br.unioeste.jgoose.UseCases.UseCase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alysson Girotto
 */
public class UCActor {
    
    private String code;
    private String name;
    private List<UseCase> useCases; 
    private UCActor father;
    private List<UCActor> children;

    public UCActor() {
        useCases = new ArrayList<>();
        father = null;
        children = new ArrayList<>();        
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UseCase> getUseCases() {
        return useCases;
    }

    public void setUseCases(List<UseCase> useCases) {
        this.useCases = useCases;
    }

    public UCActor getFather() {
        return father;
    }

    public void setFather(UCActor father) {
        this.father = father;
    }

    public List<UCActor> getChildren() {
        return children;
    }

    public void setChildren(List<UCActor> children) {
        this.children = children;
    }

    public void addChild(UCActor child){
        children.add(child);
    }
    
    @Override
    public String toString() {
        return "\nUCActor{" + 
                    "\n\tcod=" + code + 
                    "\n\tname=" + name + 
                    "\n\t\tuseCases=" + useCases +
                    "\n\t\tchildren=" + children +
                '}';
    }
    
    
    
}
