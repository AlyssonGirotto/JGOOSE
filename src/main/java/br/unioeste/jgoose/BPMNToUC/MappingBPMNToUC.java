/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.BPMNToUC;

import br.unioeste.jgoose.controller.BPMNController;
import br.unioeste.jgoose.model.BPMNActivity;
import br.unioeste.jgoose.model.BPMNArtifact;
import br.unioeste.jgoose.model.BPMNElement;
import br.unioeste.jgoose.model.BPMNEvent;
import br.unioeste.jgoose.model.BPMNGateway;
import br.unioeste.jgoose.model.BPMNLink;
import br.unioeste.jgoose.model.BPMNParticipant;
import br.unioeste.jgoose.model.BPMNToUCInstance;
import br.unioeste.jgoose.model.UCActor;
import br.unioeste.jgoose.model.UCUseCase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alysson Girotto
 */
public class MappingBPMNToUC {

    private Integer currentInstance;
    private Integer countInstance;
    private Integer countUseCase;

    private List<BPMNToUCInstance> instances;
    private List<UCActor> actors;
    private List<UCUseCase> useCases;
    
    private List<String> avaliationOrder;

    public MappingBPMNToUC() {
        countInstance = 0;
        this.instances = new ArrayList<>();
        this.avaliationOrder = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.useCases = new ArrayList<>();
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
        currentInstance = instances.size() - 1;
        int countLinks; //conta a quantidade de links de 'saída' de um dado elemento

        // Analisa as instâncias (de acordo com a ordem contrária em que foram inseridas, última para primeira)
        while (currentInstance != -1) {
            // atualiza para a última instância gerada
            // currentInstance = instances.size() - 1;
            
            if (instances.get(currentInstance).getFinished()) { //instância já avaliada
                currentInstance--;
            } else { //instância ainda não avaliada
                // obtem o proximo elemento a ser avaliado
                BPMNElement currentElement = instances.get(currentInstance).getNext();

                // verifica se o elemento já nao foi avaliado
                if (!avaliationOrder.contains(currentElement.getCode())) { // ainda não foi avaliado

                    // Avalia o elemento
                    if (currentElement instanceof BPMNActivity) { 
                        analyzeActivity((BPMNActivity) currentElement);
                    } else if (currentElement instanceof BPMNEvent) {
                        analyzeEvent((BPMNEvent) currentElement);
                    } else if (currentElement instanceof BPMNGateway) {
                        analyzeGateway((BPMNGateway) currentElement);
                    } else if (currentElement instanceof BPMNArtifact) {
                        analyzeArtifact((BPMNArtifact) currentElement);
                    }
                    
                    countLinks = 0;

                    // verifica se o elemento possui ou não multiplas opcoes de sequencia ou mensagem
                    for (BPMNLink link : currentElement.getLinks()) {
                        if (link.getType() != BPMNLink.ASSOCIATION) { //associacoes não são consideradas                            
                            if (currentElement.getCode().equals(link.getFrom().getCode())) {
                                countLinks++;
                            }
                        }
                    }

                    // Determina qual o próximo elemento a ser avaliado
                    if (countLinks > 1) { // DRD9 - multiplas instancias devem ser criadas
                        //Cria uma nova instância para cada fluxo de saída
                        // 1º - Instancias provenientes de fluxos de sequencia
                        for (BPMNLink link : currentElement.getLinks()) {
                            if (link.getType() == BPMNLink.SEQUENCE) {
                                addInstance(link);
                            }
                        }

                        // 2º - Instancias provenientes de fluxos de mensagem
                        for (BPMNLink link : currentElement.getLinks()) {
                            if (link.getType() == BPMNLink.MESSAGE) {
                                addInstance(link);
                            }
                        }

                        //marca a instância atual como avaliada
                        instances.get(currentInstance).setFinished(true);

                        //marca o elemento atual como analisado
                        // atualiza a proxima instância a ser avaliada
                        currentInstance = instances.size() - 1;
                    } else{ //Verifica o tipo do elemento    
                        // atualiza instância a ser avaliada
                    }
                } else { // elemento já avaliado
                    //finaliza avaliação da instância atual
                    finishCurrentInstance();
                }
            }
        }
    }

    // finaliza avaliação da instância atual
    private void finishCurrentInstance(){
        instances.get(currentInstance).setFinished(true);
        currentInstance--;
    }
    
    // Armazena nova instancia obtida
    private void addInstance(BPMNLink link) {
        System.out.println("Adicionando instância");
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
    
    //Avalia gateway
    private void analyzeGateway(BPMNGateway gateway) {        
        System.out.println("Analisando gateway");
    }
    
    //Avalia activity
    private void analyzeArtifact(BPMNArtifact artifact) {        
        System.out.println("Analisando artifact");
    }    

    //Avalia task
    private void analyzeTask(BPMNActivity activity) {
        System.out.println("analisando task");
        
        boolean possuiFluxoMensagemParaOutraAtividade = false;
                
        //Avalia se a tarefa possui fluxo de mensagem para outra atividade
        for(BPMNLink link : activity.getLinks()){
            if(link.getType() == BPMNLink.MESSAGE){
                if(link.getFrom().getCode().equals(activity.getCode()) && link.getTo() instanceof BPMNActivity){
                    possuiFluxoMensagemParaOutraAtividade = true;
                    break;
                }
            }
        }
        
        if(possuiFluxoMensagemParaOutraAtividade){
            // DRD5 - Possui fluxo de saída de mensagem para outra atividade
            analyzeTaskWithMessageLinksToActivities(activity);
        } else{ 
           // DRD4 - Não possui fluxo de saída de mensagem para outra atividade 
           // Adiciona o caso de uso obtido
            addUseCase(activity, "DRD4");
        }
    }

    //Dada a atividade originadora e a respectiva diretriz, adiciona um caso de uso
    private void addUseCase(BPMNActivity activity, String guidelineUsed){
        // Gera um caso de Uso
        UCUseCase useCase = new UCUseCase();
        
        useCase.setCode(countUseCase++);
        useCase.setName(activity.getLabel());
        useCase.setInstanceCod(countInstance);
        useCase.setGuidelineUsed(guidelineUsed);
        
        useCases.add(useCase);
    }
    
    //DRD5 - Avalia task com fluxo de saída de mensagem para outra atividade
    private void analyzeTaskWithMessageLinksToActivities(BPMNActivity activity) {                
        // Adiciona o caso de uso obtido
        addUseCase(activity, "DRD5");
        
        // Finaliza-se a instância atual
        finishCurrentInstance();
        
        // Cria novas instâncias a partir da instância atual
        //Cria uma nova instância para cada fluxo de saída
        
        // 1º - Instancias provenientes de fluxos de sequencia
        for (BPMNLink link : activity.getLinks()) {
            if (link.getType() == BPMNLink.SEQUENCE) {
                addInstance(link);
            }
        }

        // 2º - Instancias provenientes de fluxos de mensagem
        for (BPMNLink link : activity.getLinks()) {
            if (link.getType() == BPMNLink.MESSAGE) {
                addInstance(link);
            }
        }
        
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
