/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.BPMNToUC;

import br.unioeste.jgoose.controller.BPMNController;
import br.unioeste.jgoose.model.BPMNActivity;
import br.unioeste.jgoose.model.BPMNElement;
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

    private Integer actualInstance;
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

    public void derivation() {
        System.out.println("Mapping Derivation");
        searchActors();
        searchInitialInstances();
        searchUseCases();

        System.out.println("instances: " + instances.toString());
    }

    // Categoria 1 - Identificação dos atores
    private void searchActors() {

        // DRD1 - Cada Pool originará um ator
        for (BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()) {

            if (bpmnParticipant.getParticipantType().equals(BPMNParticipant.POOL)) {
                UCActor actor = new UCActor();
                actor.setCode(bpmnParticipant.getCode());
                actor.setName(bpmnParticipant.getLabel());

                actors.add(actor);
            }
        }

        // DRD2 - Cada lane originará um ator. Lane será associada com a Pool na qual está inserida
        for (BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()) {
            if (bpmnParticipant.getParticipantType().equals(BPMNParticipant.LANE)) {
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

    // Categoria 2 - DRD 3 - Verifica todos os eventos de inicio que não possuem um fluxo de recebimento
    private void searchInitialInstances() {
        boolean possuiFluxoRecebimento;
        boolean internoSubprocesso;

        List<BPMNElement> nextElements; //armazena os próximos elementos que podem ser atingidos a partir do elemento atual

        for (BPMNEvent event : BPMNController.getTokensBPMN().getEvents()) {

            // É um evento de início
            if (event.getEventType().equals(BPMNEvent.START)
                    || event.getEventType().equals(BPMNEvent.START_LINK)
                    || event.getEventType().equals(BPMNEvent.START_MESSAGE)
                    || event.getEventType().equals(BPMNEvent.START_MULTIPLE)
                    || event.getEventType().equals(BPMNEvent.START_RULE)
                    || event.getEventType().equals(BPMNEvent.START_TIMER)) {
                internoSubprocesso = false;

                // Verifica se o evento não é interno a um subprocesso
                if (event.getParent() != null) {
                    System.out.println("parent: " + event.getParent());
                    for (BPMNActivity activity : BPMNController.getTokensBPMN().getActivities()) {
                        if (event.getParent().equals(activity.getCode())) {
                            internoSubprocesso = true;
                            break;
                        }
                    }
                }

                if (!internoSubprocesso) { // Não é interno à um subprocesso
                    possuiFluxoRecebimento = false;
                    nextElements = new ArrayList<>();

                    // Percorre os links, identificando os links vinculados ao elemento
                    for (BPMNLink link : event.getLinks()) {

                        if (link.getType() != BPMNLink.ASSOCIATION) { //Associações não são consideradas

                            // Link é direcionado ao Evento atual -> possui fluxo de recebimento
                            if (link.getTo().getCode().equals(event.getCode())) {
                                possuiFluxoRecebimento = true;
                                break;
                            }

                            // Link é originado pelo evento atual
                            if (link.getFrom().getCode().equals(event.getCode())) {
                                nextElements.add(link.getTo()); // armazena o próximo elemento
                            }
                        }

                    }

                    // Não possui fluxo de recebimento. Diretriz se aplica
                    if (!possuiFluxoRecebimento) {

                        for (BPMNElement nextElement : nextElements) {

                            BPMNToUCInstance instance = new BPMNToUCInstance();

                            instance.setInstanceCode(countInstance++);
                            instance.setOriginator(event);
                            instance.setNext(nextElement);

                            instances.add(instance);
                        }
                    }
                }

            }
        }
    }

    // Categoria 2
    private void searchUseCases() {
        actualInstance = instances.size() - 1;
        int countLinks; //conta a quantidade de links de 'saída' de um dado elemento

        // Identifica qual a instância a ser analisada
        while (actualInstance != -1) {
            if (instances.get(actualInstance).getFinished()) { //instância já avaliada
                actualInstance--;
            } else { //instância ainda não avaliada
                // obtem o proximo elemento a ser avaliado
                BPMNElement actualElement = instances.get(actualInstance).getNext();

                // verifica se o elemento já nao foi avaliado
                if (!avaliationOrder.contains(actualElement.getCode())) { // ainda não foi avaliado

                    countLinks = 0;

                    // verifica se o elemento possui ou não multiplas opcoes de sequencia ou mensagem
                    for (BPMNLink link : actualElement.getLinks()) {
                        if (link.getType() != BPMNLink.ASSOCIATION) { //associacoes não são consideradas                            
                            if (actualElement.getCode().equals(link.getFrom().getCode())) {
                                countLinks++;
                            }
                        }
                    }

                    if (countLinks > 1) { // DRD9 - multiplas instancias devem ser criadas
                        //Cria uma nova instância para cada fluxo de saída
                        // 1º - Instancias provenientes de fluxos de sequencia
                        for (BPMNLink link : actualElement.getLinks()) {
                            if (link.getType() == BPMNLink.SEQUENCE) {
                                addInstance(link);
                            }
                        }

                        // 2º - Instancias provenientes de fluxos de mensagem
                        for (BPMNLink link : actualElement.getLinks()) {
                            if (link.getType() == BPMNLink.MESSAGE) {
                                addInstance(link);
                            }
                        }

                        //marca a instância atual como avaliada
                        instances.get(actualInstance).setFinished(true);

                        //marca o elemento atual como analisado
                        // atualiza a proxima instância a ser avaliada
                        actualInstance = instances.size() - 1;
                    } else //Verifica o tipo do elemento                    
                    if (actualElement instanceof BPMNActivity) {
                        analyzeActivity((BPMNActivity) actualElement);
                    } else if (actualElement instanceof BPMNEvent) {
                        analyzeEvent((BPMNEvent) actualElement);
                    } // atualiza instância a ser avaliada

                } else { // elemento já avaliado
                    //finaliza avaliação da instância atual
                    instances.get(actualInstance).setFinished(true);
                    actualInstance--;
                }
            }
        }
    }

    // Armazena nova instancia obtida
    private void addInstance(BPMNLink link) {
        BPMNToUCInstance instance = new BPMNToUCInstance();

        instance.setInstanceCode(countInstance++);
        instance.setOriginator(link.getFrom());
        instance.setNext(link.getTo());

        instances.add(instance);
    }

    private void addInstance(BPMNLink link, boolean subprocess) {
        BPMNToUCInstance instance = new BPMNToUCInstance();

        instance.setInstanceCode(countInstance++);
        instance.setOriginator(link.getFrom());
        instance.setNext(link.getTo());
        instance.setSubprocess(subprocess);

        instances.add(instance);
    }

    //Avalia activity
    private void analyzeActivity(BPMNActivity activity) {        
        if (activity.getActivityType().equals(BPMNActivity.TASK)) { // é uma task
            analyzeTask(activity);
        } else { //é um subprocess
            analyzeSubprocess(activity);
        }
    }

    //Avalia task
    private void analyzeTask(BPMNActivity activity) {
        System.out.println("task");
        //Avalia se a tarefa possui fluxo de mensagem para outra atividade
        for(BPMNLink link : activity.getLinks()){
            if(link.getType() == BPMNLink.SEQUENCE){
                if(link.getFrom().getCode().equals(activity.getCode()) && link.getTo() instanceof BPMNActivity){
                    // DRD5 - Possui fluxo de saída para outra atividade
                    analyzeTaskWithLinks(activity);
                }
            }
        }
    }

    //DRD5 - Avalia task com fluxo de saída para outra atividade
    private void analyzeTaskWithLinks(BPMNActivity activity) {
        
    }
    
    //Avalia subprocess
    private void analyzeSubprocess(BPMNActivity activity) {
        System.out.println("subprocess");
        
    }

    //Avalia event
    private void analyzeEvent(BPMNEvent event) {
        System.out.println("event");
        //é task, subprocess, fluxos 
    }

    // Retorna Actor com o id informado
    private UCActor getActor(String code) {
        for (UCActor uCActor : actors) {
            if (uCActor.getCode().equals(code)) {
                return uCActor;
            }
        }

        return null;
    }

}
