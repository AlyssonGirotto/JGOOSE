//package br.unioeste.jgoose.UseCases;
//
//import br.unioeste.jgoose.controller.Controller;
//import br.unioeste.jgoose.model.IStarActorElement;
//import br.unioeste.jgoose.model.IStarElement;
//import br.unioeste.jgoose.model.IStarLink;
//import java.util.ArrayList;
//
///**
// *
// * @author Diego Peliser
// */
//public class Mapping {
//
//    private int posSystemActor = -1;
//    private ArrayList<String> atoresExternos;
//    public ArrayList<Actor> useCases;
//    public ArrayList<ActorISA> isas;
//
//    public Mapping() {
//        this.atoresExternos = new ArrayList<>();
//        this.useCases = new ArrayList<>();
//        this.isas = new ArrayList<>();
//    }
//
//    /**
//     * Passo 1 - Descoberta de Atores - Diretrizes 1, 2, 3 e 4
//     */
//    public void mappingStep1() {
//
//        ArrayList<IStarActorElement> actorsOME = (ArrayList) Controller.getOme().getActors().clone();
//
//        /*
//         * Diretrizes 1 e 2
//         */
//        for (int i = 0; i < actorsOME.size(); i++) {
//            if ((actorsOME.get(i).getCod().equals(Controller.getSystemActor()))) {
//                posSystemActor = i;
//            } else {
//                atoresExternos.add(actorsOME.get(i).getCod());
//            }
//        }
//        /*
//         * Diretriz 3
//         */
//        for (String atorExterno : atoresExternos) {
//            int posAtor = Controller.getOme().searchActorCod(atorExterno); // pega a posição do ator externo ao sistema
//            IStarActorElement ator = Controller.getOme().getActor(posAtor); // cria um ator com o código do ator externo ao sistema
//            IStarActorElement atorSistema = Controller.getOme().getActor(posSystemActor); // cria um ator que representa o sistema
//            boolean flag = false; // flag para testar se o ator tem dependência com o sistema
//            IStarLink dependenciaFrom;
//            IStarLink dependenciaTo;
//            IStarLink dependencia2From;
//            IStarLink dependencia2To;
//            /*
//             * Procura se os atores externos ao sistema possuem depêndencias com o sistema
//             */
//            for (Object link : Controller.getOme().getDependenciess()) {
//                dependenciaFrom = (IStarLink) link;
//                dependencia2To = (IStarLink) link;
//                // Depêndencias com o Sistema
//                if (dependenciaFrom.getFrom().equals(ator.getCod()) || dependencia2To.getTo().equals(ator.getCod())) { // verifica se a depência atual é do ator selecionado
//                    for (Object link2 : Controller.getOme().getDependenciess()) {
//                        dependenciaTo = (IStarLink) link2;
//                        dependencia2From = (IStarLink) link2;
//                        if ((dependenciaFrom.getTo().equals(dependenciaTo.getFrom()) && dependenciaTo.getTo().equals(atorSistema.getCod())) || (dependencia2To.getFrom().equals(dependencia2From.getTo()) && dependencia2From.getFrom().equals(atorSistema.getCod()))) { // Diretriz 3.1
//                            flag = true;
//                        }
//                        // verifica se o Objetivo é decomposto e é filho do sistema
//                        if (dependenciaFrom.getTo().equals(dependenciaTo.getFrom()) && (atorSistema.containsChildren(dependenciaTo.getTo()))) {
//                            flag = true;
//                        }
//                        if (flag) { // testa a flag, ou seja, se o Ator possui uma dependência com o sistema
//                            break;
//                        }
//                    }
//                }
//                if (flag) {  // testa a flag ou seja, se o Ator possui uma dependência com o sistema
//                    break;
//                }
//            }
//            //Se o ator possui depedência com o sistema, adiciona na lista de candidatos
//            if (flag) {
//                useCases.add(new Actor(ator.getCod(), ator.getName()));
//            }
//        }
//        /*
//         * Diretriz 4
//         */
//        int posAtor, pos;
//        IStarActorElement atorPai;
//        IStarActorElement atorFilho;
//        for (String ator : atoresExternos) {
//            posAtor = Controller.getOme().searchActorCod(ator);
//            atorFilho = Controller.getOme().getActor(posAtor);
//            isas.add(new ActorISA(atorFilho.getCod(), atorFilho.getName()));
//            for (Object isa : Controller.getOme().getIsas()) {
//                IStarLink link = (IStarLink) isa;
//                if (link.getTo().equals(ator)) {
//                    posAtor = Controller.getOme().searchActorCod(link.getFrom());
//                    atorPai = Controller.getOme().getActor(posAtor);
//                    pos = isas.size() - 1;
//                    isas.get(pos).setCodFather(atorPai.getCod());
//                    isas.get(pos).setNameFather(atorPai.getName());
//                }
//            }
//            if (isas.get(isas.size() - 1).getCodFathers().isEmpty()) {
//                pos = isas.size() - 1;
//                isas.remove(pos);
//            }
//        }
//    }
//
//    /**
//     * Passo 2 - Descoberta de Casos de Uso - Diretrizes 5 e 6
//     */
//    public void mappingStep2() {
//        for (Actor actor : useCases) {
//            int posAtor = Controller.getOme().searchActorCod(actor.getCod()); // pega a posição do ator externo ao sistema
//            IStarActorElement ator = Controller.getOme().getActor(posAtor); // cria um ator com o código do ator candidato a caso de uso ao sistema
//            IStarActorElement atorSistema = Controller.getOme().getActor(posSystemActor); // cria um ator que representa o sistema
//            /*
//             * Diretriz 5 e 6
//             */
//            // procura casos de uso do tipo Objetivo
//            for (IStarElement goal : Controller.getOme().getGoals()) {
//                // ator -> dependum -> sistema computacional
//                if ((ator.containsLink("" + goal.getLinks().get(0))) && (atorSistema.containsLink("" + goal.getLinks().get(1)))) {
//                    actor.setUseCase(new UseCase(goal.getCod(), goal.getName(), "Objetivo"));
//                }
//                String codTask = Controller.getOme().getCodTask(goal.getName()); // pega o código da Tarefa igual ao Objetivo
//                String codGoal = Controller.getOme().getCodGoal(goal.getCod(), goal.getName()); // pega o código do Objetivo decomposto igual ao Objetivo
//                // verifica se o Objetivo é decomposto
//                if ((ator.containsLink("" + goal.getLinks().get(0)) || ator.containsLink("" + goal.getLinks().get(1))) & (atorSistema.containsChildren(codTask) || atorSistema.containsChildren(codGoal))) {
//                    actor.setUseCase(new UseCase(goal.getCod(), goal.getName(), "Objetivo"));
//                }
//                // sistema computaciona -> dependum -> ator
//                if ((ator.containsLink("" + goal.getLinks().get(1))) && (atorSistema.containsLink("" + goal.getLinks().get(0)))) {
//                    actor.setUseCase(new UseCase(goal.getCod(), goal.getName(), "Objetivo"));
//                }
//            }
//            // verifica se o casos de uso do tipo Objetivo criados são decompostos em outros objetivos
//            // caso sejam, cria um caso de caso para cada Objetivo decomposto criado
//            int n = actor.getUseCases().size();
//            boolean flag = false;
//            IStarElement fromElement;
//            for (int i = 0; i < n; i++) {
//                UseCase useCase = actor.getUseCase(i);
//                String nameCase = useCase.getName().substring(0, useCase.getName().length() - 1);
//                String codGoal = Controller.getOme().getCodGoal(useCase.getCod(), useCase.getName()); // pega o código do Objetivo decomposto igual ao Objetivo
//                for (IStarLink ma : Controller.getOme().getMeansEnds()) {
//                    // verifica se existe um link Meands-End de uma Tarefa para o Caso de Uso Objetivo
//                    if (ma.getTo().equals(codGoal)) {
//                        fromElement = Controller.getOme().getElement(ma.getFrom());
//                        if (flag) {
//                            String name = nameCase + fromElement.getName().replaceAll("\"", "") + "\"";
//                            actor.getUseCases().add(new UseCase(fromElement.getCod(), name, "Objetivo"));
//                        } else {
//                            actor.getUseCase(i).setName(nameCase + fromElement.getName().replaceAll("\"", "") + "\"");
//                            actor.getUseCase(i).setCod(fromElement.getCod());
//                            flag = true;
//                        }
//                    }
//                }
//            }
//            // procura casos de uso do tipo Tarefa
//            for (IStarElement task : Controller.getOme().getTasks()) {
//                // ator -> dependum -> sistema computacional
//                if (task.getLinks().size() > 1 && (ator.containsLink("" + task.getLinks().get(0))) && (atorSistema.containsLink("" + task.getLinks().get(1)))) {
//                    actor.setUseCase(new UseCase(task.getCod(), task.getName(), "Tarefa"));
//                }
//                // sistema computaciona -> dependum -> ator
//                if (task.getLinks().size() > 1 && (ator.containsLink("" + task.getLinks().get(1))) && (atorSistema.containsLink("" + task.getLinks().get(0)))) {
//                    actor.setUseCase(new UseCase(task.getCod(), task.getName(), "Tarefa"));
//                }
//            }
//            // procura casos de uso do tipo Recurso
//            for (IStarElement resource : Controller.getOme().getResourcess()) {
//                // ator -> dependum -> sistema computacional
//                if ((ator.containsLink("" + resource.getLinks().get(0))) && (atorSistema.containsLink("" + resource.getLinks().get(1)))) {
//                    actor.setUseCase(new UseCase(resource.getCod(), resource.getName(), "Recurso"));
//                }
//                // sistema computaciona -> dependum -> ator
//                if ((ator.containsLink("" + resource.getLinks().get(1))) && (atorSistema.containsLink("" + resource.getLinks().get(0)))) {
//                    actor.setUseCase(new UseCase(resource.getCod(), resource.getName(), "Recurso"));
//                }
//            }
//            // procura NFRs (Requisitos Não Funcionais) do tipo Objetivo-Soft
//            for (IStarElement softgoal : Controller.getOme().getSoftgoals()) {
//                // ator -> dependum -> sistema computacional
//                if ((ator.containsLink("" + softgoal.getLinks().get(0))) && (atorSistema.containsLink("" + softgoal.getLinks().get(1)))) {
//                    actor.setNfr(new NFR(softgoal.getCod(), softgoal.getName()));
//                }
//                // sistema computaciona -> dependum -> ator
//                if ((ator.containsLink("" + softgoal.getLinks().get(1))) && (atorSistema.containsLink("" + softgoal.getLinks().get(0)))) {
//                    actor.setNfr(new NFR(softgoal.getCod(), softgoal.getName()));
//                }
//            }
//        }
//    }
//
//    /**
//     * Passo 3 - Descoberta e descrição do fluxo principal e alternativos dos
//     * Casos de Uso - Diretrizes 8 e 10
//     */
//    public void mappingStep3() {
//        IStarElement element;
//        for (Actor actor : useCases) {
//            int n = actor.getUseCases().size();
//            for (int i = 0; i < n; i++) {
//                // pega a tareja com nome igual ao caso de uso objetivo
//                element = Controller.getOme().getElement(actor.getUseCase(i).getCod(), actor.getUseCase(i).getName());
//                // se element == null, pega a tarefa decomposto por Meands-End
//                if (element == null) {
//                    element = Controller.getOme().getElement(actor.getUseCase(i).getCod());
//                }
//                if (element != null) {
//                    actor.getUseCase(i).setSteps(getSteps(element.getCod()));
//                }
//            }
//        }
//        // verifica e seta quais etapas são casos de usos (<<include>>)
//        for (Actor actor : useCases) {
//            for (UseCase caso : actor.getUseCases()) {
//                for (Step step : caso.getSteps()) {
//                    if (isUseCase(step.getCod(), step.getName())) {
//                        step.setInclude(true);
//                    }
//                }
//            }
//        }
//
//    }
//
//    private boolean isUseCase(String cod, String name) {
//        for (Actor actor : useCases) {
//            for (UseCase caso : actor.getUseCases()) {
//                if (!caso.getCod().equals(cod) && caso.getName().equals(name)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param cod cod of the element to return
//     * @return return a list of steps
//     */
//    private ArrayList<Step> getSteps(String cod) {
//        ArrayList<Step> steps = new ArrayList<>();
//        ArrayList<Extend> extend;
//        IStarElement elemento;
//        for (IStarLink link : Controller.getOme().getDecompositions()) {
//            if (link.getTo().equals(cod)) {
//                elemento = Controller.getOme().getElement(link.getFrom());
//                extend = getExtends(link.getFrom());
//                steps.add(new Step(elemento.getCod(), elemento.getName(), extend));
//            }
//        }
//        return steps;
//    }
//
//    /**
//     * @param cod cod of the element to return
//     * @return return a list of extends
//     */
//    private ArrayList<Extend> getExtends(String cod) {
//        ArrayList<Extend> extend = new ArrayList<>();
//        for (IStarLink link : Controller.getOme().getMeansEnds()) {
//            if (link.getTo().equals(cod)) {
//                IStarElement elemento = Controller.getOme().getElement(link.getFrom());
//                extend.add(new Extend(elemento.getCod(), elemento.getName()));
//            }
//        }
//        return extend;
//    }
//}
