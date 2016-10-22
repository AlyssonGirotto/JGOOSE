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
import br.unioeste.jgoose.model.UCUseCaseDescription;
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
    
    private Boolean isMessageFlow;
    
    public MappingBPMNToUC() {
        currentInstance = 0;
        countInstance = 0;
        countUseCase = 0;
        this.instances = new ArrayList<>();
        this.avaliationOrder = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.useCases = new ArrayList<>();
        isMessageFlow = false;
    }

    public void derivation() {
        // Etapa 1 - Obtenção da representação diagramática
        searchActors();
        searchInitialInstances();
        searchUseCases();
        
        // Etapa 2 é chamada dentro da etapa de procurar Casos de Uso
        
        for(BPMNToUCInstance instance : instances){
            System.out.println(instance.toString());
        }
        
        for(UCActor actor : actors){
            System.out.println(actor.toString());
        }
        
        for(UCUseCase useCase : useCases){
            System.out.println(useCase.printAllInfo());
            System.out.println(useCase.getDescription().toString());
        }
    }

    // Categoria 1 - Identificação dos atores
    private void searchActors() {

        // DRD1 - Cada Pool originará um ator
        for (BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()) {

            if (bpmnParticipant.getParticipantType().equals(BPMNParticipant.POOL)) {
                UCActor actor = new UCActor();
                actor.setCode(bpmnParticipant.getCode());
                actor.setBpmnElementoCode(bpmnParticipant.getCode());
                actor.setName(bpmnParticipant.getLabel());

                actors.add(actor);
            }
        }

        // DRD2 - Cada lane originará um ator. Lane será associada com a Pool na qual está inserida
        for (BPMNParticipant bpmnParticipant : BPMNController.getTokensBPMN().getParticipants()) {
            if (bpmnParticipant.getParticipantType().equals(BPMNParticipant.LANE)) {
                UCActor actor = new UCActor();
                actor.setCode(bpmnParticipant.getCode());
                actor.setBpmnElementoCode(bpmnParticipant.getCode());
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
                  
                    // Link é direcionado ao Evento atual -> possui fluxo de recebimento
                    for (BPMNLink link : event.getLinksTo()) {
                        possuiFluxoRecebimento = true;
                        break;
                    }
                    
                    // Link é originado pelo evento atual
                    for (BPMNLink link : event.getLinksFrom()) {
                        nextElements.add(link.getTo()); // armazena o próximo elemento
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
            if (instances.get(currentInstance).getFinished()) { //instância já avaliada
                currentInstance--;
            } else { //instância ainda não avaliada
                // obtem instancia atual
                isMessageFlow = instances.get(currentInstance).getMessageFlow();
                
                // obtem o proximo elemento a ser avaliado
                BPMNElement currentElement = instances.get(currentInstance).getNext();

                while(true){ // Permanece avaliando a instância atual
                    
                    // verifica se o elemento já nao foi avaliado
                    if (!avaliationOrder.contains(currentElement.getCode())) { // ainda não foi avaliado

                        // Avalia o elemento
                        if (currentElement instanceof BPMNActivity) { 
                            analyzeActivity((BPMNActivity) currentElement);                    
                        } else if (currentElement instanceof BPMNEvent) {
                            //analyzeEvent((BPMNEvent) currentElement);
                            BPMNEvent event = (BPMNEvent) currentElement;

                            // DRD7 - É evento de fim, deve-se finalizar a instância atual
                            if(event.isEndEvent()){                                
                                // finaliza avaliação da instância atual
                                finishCurrentInstance();
                                break; 
                            }
                        }

                        countLinks = 0;

                        // Determinação da sequência do processo

                        // verifica se o elemento possui ou não multiplas opcoes de sequencia ou mensagem
                        for (BPMNLink link : currentElement.getLinksFrom()) { // elemento atual é origem
                            if (link.getType() != BPMNLink.ASSOCIATION && link.getType() != BPMNLink.DATA_ASSOCIATION) { //associacoes não são consideradas                            
                                countLinks++;                                
                            }
                        }                                                                                  
                        
                        // Determina qual o próximo elemento a ser avaliado
                        if (countLinks > 1) { // DRD9 - multiplas instancias devem ser criadas
                            
                            //Cria uma nova instância para cada fluxo de saída
                            // 1º - Instancias provenientes de fluxos de sequencia
                            for (BPMNLink link : currentElement.getLinksFrom()) {
                                if (link.getType() == BPMNLink.SEQUENCE) {
                                    addInstance(link);                                    
                                }
                            }

                            // 2º - Instancias provenientes de fluxos de mensagem
                            for (BPMNLink link : currentElement.getLinksFrom()) {
                                if (link.getType() == BPMNLink.MESSAGE && currentElement.getCode().equals(link.getFrom().getCode())) {
                                    addInstance(link);
                                }
                            }

                            //marca o elemento atual como analisado
                            avaliationOrder.add(currentElement.getCode());
                            
                            //marca a instância atual como avaliada
                            finishCurrentInstance();
                            break;                                                        
                        } else{ // único fluxo, permanece na mesma instância 
                            
                            //marca o elemento atual como analisado
                            avaliationOrder.add(currentElement.getCode());
                            
                            // atualiza para próximo elemento da instância sendo avaliada
                            try{
                                if (currentElement.getLinksFrom().get(0).getType() == BPMNLink.MESSAGE)
                                    isMessageFlow = true;
                                else isMessageFlow = false;
                                
                                currentElement = currentElement.getLinksFrom().get(0).getTo();
                            } catch(Exception e)    {
                                e.printStackTrace();
                            }
                        }
                    } else { // elemento já avaliado
                        currentElement = instances.get(currentInstance).getNext();
                        
                        // Verifica se é uma atividade já avaliada 
                        if (currentElement instanceof BPMNActivity) {                             
                            
                            if(instances.get(currentInstance).getMessageFlow()){                               
                                UCUseCase ucIncluded = null;
                                
                                // obtem Caso de Uso gerado pela atividade que inicia o message flow
                                for(UCUseCase useCase : useCases){
                                    if(useCase.getBpmnElementCode().equals(instances.get(currentInstance).getOriginator().getCode())){
                                        ucIncluded = useCase;
                                        break;
                                    }                                
                                }
                                
                                // obtem Caso de Uso obtido anteriormente a partir da atividade atual
                                for(UCUseCase useCase : useCases){
                                    if(useCase.getBpmnElementCode().equals(currentElement.getCode())){
                                        // Verifica se o Caso de Uso não foi inserido anteriormente
                                        if(!useCase.getIncludedUseCases().contains(ucIncluded)) {
                                            // Adiciona o Caso de Uso à lista de Incluídos
                                            useCase.addIncludedUseCase(ucIncluded);                                              
                                        }
                                        break;                                        
                                    }                                
                                }
                            }
                        }
                        
                        //finaliza avaliação da instância atual
                        finishCurrentInstance();
                        break;                        
                    }                    
                }                                
            }
        }
    }

    // finaliza avaliação da instância atual
    private void finishCurrentInstance(){
        instances.get(currentInstance).setFinished(true);
        currentInstance = instances.size() - 1;
    }
    
    // Armazena nova instancia obtida
    private void addInstance(BPMNLink link) {        
        BPMNToUCInstance instance = new BPMNToUCInstance();

        instance.setInstanceCode(countInstance++);
        instance.setOriginator(link.getFrom());
        instance.setNext(link.getTo());

        if(link.getType() == BPMNLink.MESSAGE){
            instance.setMessageFlow(true);
        }
        
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
        
        // Verifica se é proveniente de fluxo de mensagem -> inclui outro Caso de Uso
        if(isMessageFlow){
            UCUseCase ucIncluded = null;

            // obtem Caso de Uso gerado pela atividade que inicia o message flow
            for(UCUseCase useCase : useCases){
                if(useCase.getBpmnElementCode().equals(instances.get(currentInstance).getOriginator().getCode())){
                    ucIncluded = useCase;  
                    break;
                }                                
            }
            
            if(ucIncluded != null){
                addUseCase(activity, "5", ucIncluded);
                return;
            }
        }
                
        addUseCase(activity, "DRD4");        
    }

    //Dada a atividade originadora e a respectiva diretriz, adiciona um caso de uso
    private void addUseCase(BPMNActivity activity, String guidelineUsed){
        // Gera um caso de Uso
        UCUseCase useCase = new UCUseCase();
        
        useCase.setCode(countUseCase++);
        useCase.setName(activity.getLabel());
        useCase.setInstanceCod(countInstance);
        useCase.setGuidelineUsed(guidelineUsed);
        useCase.setBpmnElementCode(activity.getCode());
                        
        setTextualDescription(useCase, activity);
        
        useCases.add(useCase);

        // Associa o caso de utor com o respectivo ator primário - swimlane na qual atividade está contida
        associatesUseCaseActor(activity);
        
        // Verifica se Caso de Uso foi gerado a partir de uma instância gerada a partir da expansão de um subprocesso
        // Se sim, inclui o Caso de Uso recentemente gerado no campo Casos de Uso Incluídos do Caso de Uso relac. ao subprocesso
        if(instances.get(currentInstance).getSubprocess()){
            // Procura o Caso de Uso gerado pelo pai
            for(UCUseCase uc : useCases){
                if(uc.getBpmnElementCode().equals(activity.getParent())){ // encontrou o pai
                    uc.addIncludedUseCase(useCase);
                    break;
                }
            }
        }
                
    }
    
    //Dada a atividade originadora e a respectiva diretriz, adiciona um caso de uso
    private void addUseCase(BPMNActivity activity, String guidelineUsed, UCUseCase ucIncluded){
        // Gera um caso de Uso
        UCUseCase useCase = new UCUseCase();
        
        useCase.setCode(countUseCase++);
        useCase.setName(activity.getLabel());
        useCase.setInstanceCod(countInstance);
        useCase.setGuidelineUsed(guidelineUsed);
        useCase.setBpmnElementCode(activity.getCode());
        useCase.addIncludedUseCase(ucIncluded);
        
        useCases.add(useCase);
        
        // Associa o caso de utor com o respectivo ator primário - swimlane na qual atividade está contida
        associatesUseCaseActor(activity);
    }
    
    //Avalia subprocess
    private void analyzeSubprocess(BPMNActivity activity) {        
        // Adiciona o caso de uso obtido
        addUseCase(activity, "DRD6");
        
        // adiciona instâncias provenientes de elementos internos ao subprocesso
        for(BPMNEvent bPMNEvent : BPMNController.getTokensBPMN().getEvents()){
            // é interno ao subprocesso
            if(bPMNEvent.isStartEvent() && bPMNEvent.getParent().equals(activity.getCode())){
                for(BPMNLink bPMNLink : bPMNEvent.getLinksFrom()){
                    BPMNToUCInstance instance = new BPMNToUCInstance();

                    instance.setInstanceCode(countInstance++);
                    instance.setOriginator(bPMNEvent);
                    instance.setNext(bPMNLink.getTo());
                    instance.setSubprocess(true);
                    
                    instances.add(instance);
                }                                            
            }
        }
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

    // DRD11 - Identifica o ator primário associado ao Caso de Uso
    // Swimlane na qual se encontra a activity que originou o Caso de Uso
    private void associatesUseCaseActor(BPMNElement bPMNElement){
        
        // Verifica se o pai proveniente de swimlane -> já mapeado para Ator
        for(UCActor actor : actors){
            if(bPMNElement.getParent().equals(actor.getBpmnElementoCode())){
                actor.addUseCase(useCases.get(useCases.size() - 1)); // Ultimo caso de uso obtido
                return;
            }
        }
        
        // Verifica se o pai é um subprocesso
        // Percorre as activities obtidas
        for(BPMNActivity activity : BPMNController.getTokensBPMN().getActivities()){
            if(activity.getActivityType().equals(BPMNActivity.SUBPROCESS)){ // é subprocesso
                if(bPMNElement.getParent().equals(activity.getCode())){ // está contida no subprocesso
                    return; // Caso de Uso não será associado à nenhum ator
                }                
            }            
        }        
        
        // Está contida em um grupo        
        // Percorre os artifacts obtidos, a fim de obter o objeto no qual está contido
        for(BPMNArtifact artifact : BPMNController.getTokensBPMN().getArtifacts() ){
            if(bPMNElement.getParent().equals(artifact.getCode())){ // está contida no grupo
                // Chama novamente o processo para identificar o elemento no qual o grupo está inserido
                associatesUseCaseActor(artifact);
            }            
        }

    }
    
    // Etapa 2 - Para cada Caso de Uso identifica anteriormente, obtém as representações textuais
    private void setTextualDescription(UCUseCase useCase, BPMNActivity activity){
        
        UCUseCaseDescription description = new UCUseCaseDescription();

        // Passo 1 - Preenchimento do cabeçalho
        description.setName(useCase.getCode() + " - " + useCase.getName());

        // Passo 2 - Associação com atores
        //TODO

        // Passo 3 - Gatilhos
        description.setTrigger(getTriggers(activity));

        // Passo 4 - Pré-condições relacionadas com atividades
        description.setPreConditions(getPreConditionsActivities(activity));

        // Passo 5 - Pré-condições relacionadas com gateways
        description.setPreConditions(getPreConditionsGateways(activity));
        
        // Atualiza as informações no Caso de Uso recentemente adicionado
        useCase.setDescription(description);                        
    }
    
    // Passo 3 - Analisa gatilhos relacionados com o Caso de Uso
    private String getTriggers(BPMNActivity activity){
        
        // Percorre links direcionados à atividade
        for(BPMNLink link : activity.getLinksTo()){
            // Verifica se é fluxo de sequência proveniente de um evento
            if(link.getType() == BPMNLink.SEQUENCE && (link.getFrom() instanceof BPMNEvent)){
                BPMNEvent event = (BPMNEvent) link.getFrom();
                
                // Verifica se é um evento de início
                if(event.isStartEvent()){ // Origina um gatilho
                
                    // Evento de mensagem
                    if(event.getEventType().equals(BPMNEvent.START_MESSAGE)){
                        return ("A mensagem " + link.getLabel() + " chegou de " + link.getFrom().getLabel() + ".");
                    }
                    
                    // Evento com temporizador
                    if(event.getEventType().equals(BPMNEvent.START_TIMER)){
                        return ("O tempo/data " + event.getLabel() + " foi alcançado.");
                    }
                    
                    // Evento com condicional
                    if(event.getEventType().equals(BPMNEvent.START_RULE)){
                        return ("A condição " + link.getLabel() + " tornou-se verdadeira.");
                    }
                                        
                    // Evento múltiplo
                    if(event.getEventType().equals(BPMNEvent.START_MULTIPLE)){
                        // TODO Modificar
                        return ("O evento " + event.getLabel() + " ocorreu."); 
                    }
                    
                    return ("O evento " + event.getLabel() + " ocorreu.");
                }
            }
        }
        
        return null;
    }
    
    // Passo 4 - Pré-condições geradas por atividades relacionadas com o Caso de Uso
    private String getPreConditionsActivities(BPMNActivity activity){
        String preConditions = "";
        
        // Percorre links direcionados à atividade
        for(BPMNLink link : activity.getLinksTo()){
            // Verifica se é fluxo de sequência proveniente de uma atividade
            if(link.getType() == BPMNLink.SEQUENCE && (link.getFrom() instanceof BPMNActivity)){
                preConditions += "Atividade " + link.getFrom().getLabel() + " deve ser concluída; ";                
            }
        }
        
        return preConditions;
    }
    
    // Passo 5 - Pré-condições geradas por gateways
    private String getPreConditionsGateways(BPMNActivity activity){
        String preConditions = "";
        
        // Percorre links direcionados à atividade
        for(BPMNLink link : activity.getLinksTo()){
            // Verifica se é fluxo de sequência proveniente de uma atividade
            if(link.getType() == BPMNLink.SEQUENCE && (link.getFrom() instanceof BPMNGateway)){
                BPMNGateway gateway = (BPMNGateway) link.getFrom();
                                
                // Exclusive Gateway - DRT11 ou DRT14
                if(gateway.getGatewayType().equals(BPMNGateway.EXCLUSIVE)){
                    // DRT11 É gateway de divergência
                    if(gateway.getLinksFrom().size() > 1){
                        preConditions += "Condição do gateway " + gateway.getLabel() + " ser " + link.getLabel() + "; ";
                    }else{ // DRT14
                        preConditions += "Uma das atividades ";
                        for(int i = 0; i < gateway.getLinksTo().size(); i++){
                            if(i < gateway.getLinksTo().size() - 1)
                                preConditions += gateway.getLinksTo().get(i).getLabel() + " ou ";
                            else 
                                preConditions += gateway.getLinksTo().get(i).getLabel() + " ";
                        }
                        preConditions += "tiver sido concluída.";
                    }
                }
                
                preConditions += "Atividade " + link.getFrom().getLabel() + " deve ser concluída; ";                
            }
        }
        
        return preConditions;
    }
    
    // Retorna atores obtidos
    public List<UCActor> getActors(){
        return actors;
    }
    
    // Retorna Casos de uso obtidos
    public List<UCUseCase> getUseCases(){
        return useCases;
    }
}
