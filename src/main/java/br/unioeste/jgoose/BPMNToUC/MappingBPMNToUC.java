/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.BPMNToUC;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.controller.BPMNController;
import br.unioeste.jgoose.model.BPMNEvent;
import br.unioeste.jgoose.model.BPMNLink;
import br.unioeste.jgoose.model.BPMNParticipant;
import br.unioeste.jgoose.model.BPMNToUCInstance;
import br.unioeste.jgoose.model.UCActor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alysson Girotto
 */
public class MappingBPMNToUC {
    
    private Integer countInstance;

    private Integer countUseCase;
    
    private List<BPMNToUCInstance> instances;
    private List<UCActor> actors;
    
    private List<String> avaliationOrder;
    
    public MappingBPMNToUC() {
        countInstance = 0;
        this.instances = new ArrayList<>();
        this.avaliationOrder = new ArrayList<>();
        this.actors = new ArrayList<>();
    }
    
    public void derivation(){
        System.out.println("Mapping Derivation");
        searchActors();
        searchInitialInstances();
        
        System.out.println("instances: " + instances.toString());
    }
    
    // Categoria 1 - Identificação dos atores
    private void searchActors(){
        
        // DRD1 - Cada Pool originará um ator
        for(BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()){
            
            if(bpmnParticipant.getParticipantType().equals(BPMNParticipant.POOL)){
                UCActor actor = new UCActor();
                actor.setCode(bpmnParticipant.getCode());
                actor.setName(bpmnParticipant.getLabel());

                actors.add(actor);       
            }
        }
        
        // DRD2 - Cada lane originará um ator. Lane será associada com a Pool na qual está inserida
        for(BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()){
            if(bpmnParticipant.getParticipantType().equals(BPMNParticipant.LANE)){
                UCActor actor = new UCActor();
                actor.setCode(bpmnParticipant.getCode());
                actor.setName(bpmnParticipant.getLabel());
                
                UCActor father = getActor(bpmnParticipant.getParent()); // Get father actor                 
                actor.setFather(father); //Set father actor            
                father.addChild(actor);
                                
                actors.add(actor);           
            }
        }
    }   
    
    // DRD 3 - Verifica todos os eventos de inicio que não possuem um fluxo de recebimento
    private void searchInitialInstances(){        
        boolean possuiFluxoRecebimento;
        List<String> nextElements; //armazena código dos próximos elementos que podem ser atingios a partir do elemento atual
                
        for(BPMNEvent event : BPMNController.getTokensBPMN().getEvents()){                        
            
            // É um evento de início
            if(event.getEventType().equals(BPMNEvent.START) ||
                event.getEventType().equals(BPMNEvent.START_LINK) ||
                event.getEventType().equals(BPMNEvent.START_MESSAGE) ||
                event.getEventType().equals(BPMNEvent.START_MULTIPLE) ||
                event.getEventType().equals(BPMNEvent.START_RULE) ||
                event.getEventType().equals(BPMNEvent.START_TIMER)){
                System.out.println("eh evento de inicio");
                
                //if(event.get)
                
                possuiFluxoRecebimento = false;
                nextElements = new ArrayList<>();
                
                // Percorre os links
                for (BPMNLink link : BPMNController.getTokensBPMN().getLinks()){
                    // Link é direcionado ao Evento atual -> possui fluxo de recebimento
                    if(link.getTo().equals(event.getCode())){
                        System.out.println("para o atual break");
                        possuiFluxoRecebimento = true;
                        break;
                    }
                    
                    // Link é originado pelo evento atual
                    if(link.getFrom().equals(event.getCode())){
                        System.out.println("parte do atual");
                        nextElements.add(link.getTo()); // armazena o código do próximo elemento
                    }
                }
                
                System.out.println("next: " + nextElements);
                
                // Não possui fluxo de recebimento. Diretriz se aplica
                if(!possuiFluxoRecebimento){
                    System.out.println("OK: " + event.getLabel());
                    for(String nextElementCode : nextElements){
                        BPMNToUCInstance instance = new BPMNToUCInstance();
                            
                        instance.setInstanceCode(countInstance++);
                        instance.setOriginator(event.getCode());
                        instance.setNext(nextElementCode);
                    }                                        
                }
                
            }
        }
    }
    
    // Retorna Actor com o id informado
    private UCActor getActor(String code){
        for(UCActor uCActor : actors){
            if(uCActor.getCode().equals(code)){
                return uCActor;
            }
        }
        
        return null;
    }
    
}
