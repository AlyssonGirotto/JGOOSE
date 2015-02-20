package br.unioeste.jgoose.model;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Classe Responsável por leitura do File Telos e armazenamento dos dados
 * (Elementos e Links) em uma Estrutura de Dados especificado pelas classes
 * IStar.
 *
 * @author Diego Peliser
 * @author Blade
 */
public class TokensOpenOME {

    //Estrutura de Dados que armazena os Elementos e Ligações
    private ArrayList<IStarActorElement> actors, agents, positions, roles;
    private ArrayList<IStarElement> goals, softgoals, tasks, resources;
    private ArrayList<IStarLink> dependencies, meansEnds, decompositions, isas;
    private ArrayList<IStarLink> inss, contributions, isPartOfs, coverss, occupiess, playss;
    private boolean openOME; //verifica se o arquivo está no formato OpenOME
    private String dirIn;
    FileInOut fileIn;

    /**
     * Construtor, instancia as ArrayLists de: - OMEElements:
     * actors,goals,softgoals,tasks,resources - OMELinks:
     * dependencies,meansEnds,decompositions,isas
     */
    public TokensOpenOME() {
        this.openOME = true;
        this.actors = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.positions = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.softgoals = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.meansEnds = new ArrayList<>();
        this.decompositions = new ArrayList<>();
        this.isas = new ArrayList<>();
        this.inss = new ArrayList<>();
        this.contributions = new ArrayList<>();
        this.isPartOfs = new ArrayList<>();
        this.coverss = new ArrayList<>();
        this.occupiess = new ArrayList<>();
        this.playss = new ArrayList<>();
    }

    /**
     * Método utilizado para abrir um arquivo de Entrada Telos Associa o
     * atributo fileIn ao construtor de File.
     */
    public void openFile() {
        fileIn = new FileInOut();
        dirIn = fileIn.getDirIn();
    }

    /**
     * Procura dentro do File Telos e armazena em uma estrutura de dados os
     * Elementos e Links
     */
    public void searchFile() {
        String linha = fileIn.getLine();
        String codigo = null;
        int tipoElementoLink = 99;
        while (linha != null) {
            if (checkToken(linha)) {
                codigo = storeCod(linha);
                linha = fileIn.getLine(); // IN OMEElement, OMEElementClass ou IN OMELink, OMELinkClass,
                tipoElementoLink = checkElementLink(linha); //valor entre 0 e 17
                switch (tipoElementoLink) {
                    case 0: {
                        IStarActorElement act = new IStarActorElement(); //cria um Actor i*
                        act = createActor(act, codigo, linha);
                        actors.add(act);
                    }
                    break;
                    case 1: {
                        IStarElement goal = new IStarElement(); //cria um Objetivo i*
                        goal = (IStarElement) createElement(goal, codigo, linha);
                        goals.add(goal);
                    }
                    break;
                    case 2: {
                        IStarElement task = new IStarElement(); //cria uma Tarefa i*
                        task = (IStarElement) createElement(task, codigo, linha);
                        tasks.add(task);
                    }
                    break;
                    case 3: {
                        IStarElement softgoal = new IStarElement(); //cria um Objetivo Soft i*
                        softgoal = (IStarElement) createElement(softgoal, codigo, linha);
                        softgoals.add(softgoal);
                    }
                    break;
                    case 4: {
                        IStarElement resource = new IStarElement(); //cria um Recurso i*
                        resource = (IStarElement) createElement(resource, codigo, linha);
                        resources.add(resource);
                    }
                    break;
                    case 5: {
                        IStarLink dependency = new IStarLink();// cria uma Ligação de Dependência
                        dependency = (IStarLink) createLink(dependency, codigo, linha);
                        dependencies.add(dependency);
                    }
                    break;
                    case 6: {
                        // cria uma Ligação de Decomposição de Tarefas
                        IStarLink decomposition = new IStarLink();
                        decomposition = (IStarLink) createLink(decomposition, codigo, linha);
                        decompositions.add(decomposition);
                    }
                    break;
                    case 7: {
                        // cria uma Ligação de Meio-Fim
                        IStarLink meanEnd = new IStarLink();
                        meanEnd = (IStarLink) createLink(meanEnd, codigo, linha);
                        meansEnds.add(meanEnd);
                    }
                    break;
                    case 8: {
                        // cria uma Ligação ISA
                        IStarLink isa = new IStarLink(); //cria uma Ligação de Dependência
                        isa = (IStarLink) createLink(isa, codigo, linha);
                        isas.add(isa);
                    }
                    break;
                    case 9: {
                        // cria uma Ligação INS
                        IStarLink ins = new IStarLink(); //cria uma Ligação de Dependência
                        ins = (IStarLink) createLink(ins, codigo, linha);
                        inss.add(ins);
                    }
                    break;
                    case 10: {
                        // cria uma Ligação Is-Part-Of
                        IStarLink ispartof = new IStarLink(); //cria uma Ligação de Dependência
                        ispartof = (IStarLink) createLink(ispartof, codigo, linha);
                        isPartOfs.add(ispartof);
                    }
                    break;
                    case 11: {
                        // cria uma Ligação Occupies
                        IStarLink occupies = new IStarLink(); //cria uma Ligação de Dependência
                        occupies = (IStarLink) createLink(occupies, codigo, linha);
                        occupiess.add(occupies);
                    }
                    break;
                    case 12: {
                        // cria uma Ligação Plays
                        IStarLink plays = new IStarLink(); //cria uma Ligação de Dependência
                        plays = (IStarLink) createLink(plays, codigo, linha);
                        playss.add(plays);
                    }
                    break;
                    case 13: {
                        // cria uma Ligação Covers
                        IStarLink covers = new IStarLink(); //cria uma Ligação de Dependência
                        covers = (IStarLink) createLink(covers, codigo, linha);
                        coverss.add(covers);
                    }
                    break;
                    case 14: {
                        IStarActorElement agent = new IStarActorElement(); //cria um Agent i*
                        agent = (IStarActorElement) createActor(agent, codigo, linha);
                        agents.add(agent);
                    }
                    break;
                    case 15: {
                        IStarActorElement role = new IStarActorElement(); //cria um Role i*
                        role = (IStarActorElement) createActor(role, codigo, linha);
                        roles.add(role);
                    }
                    break;
                    case 16: {
                        IStarActorElement position = new IStarActorElement(); //cria um Position i*
                        position = (IStarActorElement) createActor(position, codigo, linha);
                        positions.add(position);
                    }
                    break;
                    case 17: {
                        // cria uma Ligação Contribution
                        IStarLink contribution = new IStarLink();
                        contribution = (IStarLink) createLink(contribution, codigo, linha);
                        contributions.add(contribution);
                    }
                    break;
                    default: {
                        while (!(linha.equals("END"))) {//lê atributos até encontrar o token END
                            linha = fileIn.getLine();
                        }
                    }
                    break;
                }
            }
            linha = fileIn.getLine();
        }
        //Criar os atributos Links
        createLinksElements();
    }

    /**
     * Verifica se é um Token Elemento ou Token Ligação
     *
     * @param line
     * @return Retorna T/F se é Elemento ou Ligação
     */
    private boolean checkToken(String line) {
        return (line.indexOf("Token Element_") != -1) || (line.indexOf("Token Link") != -1);
    }

    private String storeCod(String line) {
        StringTokenizer tokens = new StringTokenizer(line);
        tokens.nextToken();
        return tokens.nextToken();
    }

    /**
     * Verifica qual tipo de elemento ou link é o token atual 0.
     * IStarActorElement 1. IStarGoalElement 2. IStarTaskElement 3.
     * IStarSoftGoalElement 4. IStarResourceElement 5. IStarDependencyLink 6.
     * IStarDecompositionLink 7. IStarMeansEndsLink 8. IStarISALink
     *
     * @param line
     * @return tipo_elemento (valor inteiro de 0 a xx)
     */
    private int checkElementLink(String line) {
        StringTokenizer tokens = new StringTokenizer(line, ", ");
        String ele_link = null; //ActorElement....
        String token;
        int posIStar;
        int tipoElemento = -1; //retorna um numero correspondente ao elemento
        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();
            posIStar = token.indexOf("IStar");
            if (posIStar != -1) {
                ele_link = token.substring(posIStar + 5, token.length());
            }
        }
        if (ele_link.equals("ActorElement")) { //Elemento Actor
            tipoElemento = 0;
        } else if (ele_link.equals("GoalElement")) { //Elemento Objetivo
            tipoElemento = 1;
        } else if (ele_link.equals("TaskElement")) {//Elemento Tarefa
            tipoElemento = 2;
        } else if (ele_link.equals("SoftGoalElement")) {//Elemento Objetivo Soft
            tipoElemento = 3;
        } else if (ele_link.equals("ResourceElement")) { //Elemento Recurso
            tipoElemento = 4;
        } else if (ele_link.equals("DependencyLink")) {//Ligação de Dependência
            tipoElemento = 5;
        } else if (ele_link.equals("DecompositionLink")) {//Ligação de Decomposição de Tarefas
            tipoElemento = 6;
        } else if (ele_link.equals("MeansEndsLink")) {//Ligação Meio-Fim
            tipoElemento = 7;
        } else if (ele_link.equals("ISALink")) {//Ligação ISA
            tipoElemento = 8;
        } else if (ele_link.equals("INSLink")) {//Ligação INS
            tipoElemento = 9;
        } else if (ele_link.equals("PartsLink")) {//Ligação Is-Part-Of
            tipoElemento = 10;
        } else if (ele_link.equals("OccupiesLink")) {//Ligação Occupies
            tipoElemento = 11;
        } else if (ele_link.equals("PlaysLink")) {//Ligação Plays
            tipoElemento = 12;
        } else if (ele_link.equals("CoversLink")) {//Ligação Covers
            tipoElemento = 13;
        } else if (ele_link.equals("AgentElement")) {//Elemento Agent
            tipoElemento = 14;
        } else if (ele_link.equals("RoleElement")) {//Elemento Role
            tipoElemento = 15;
        } else if (ele_link.equals("PositionElement")) {//Elemento Position
            tipoElemento = 16;
        } else if (ele_link.contains("Contribution")) { //Elemento Contribution
            tipoElemento = 17;
        }
        return tipoElemento;
    }

    /**
     * Armazena atributos de Elementos ou Links (name, from, to, children,
     * parent)
     *
     * @param line linha do arquivo texto
     * @return retorna o tipo do atributo e o nome (label) do atributo
     */
    private String storeAttributes(String line) {
        String Atributo = null;

        StringTokenizer tokens = new StringTokenizer(line, ", ");
        tokens.nextToken(); // "attribute, "
        String tipoAtributo = tokens.nextToken(); // name, from, to, children, parent
        line = fileIn.getLine(); //lê a próxima linha
        tokens = new StringTokenizer(line, ":");
        tokens.nextToken(); // ": "
        String atributo = tokens.nextToken(); //armazena o atributo
        if (atributo.charAt(0) == ' ') {
            int i = 0;
            while (atributo.charAt(i) == ' ') {
                i = i + 1;
            }
            atributo = atributo.substring(i, atributo.length());
        }

        Atributo = tipoAtributo + " " + atributo;

        return Atributo;
    }

    /**
     * Cria um Elemento IStarActorElement
     *
     * @param element elemento Ator
     * @param cod codigo do elemento Ator
     * @param line linha do arquivo texto
     * @return IStarActorElement um Ator i*
     */
    private IStarActorElement createActor(IStarActorElement actor, String cod, String line) {
        actor.setCod(cod); //armazena o Código Element_x
        line = fileIn.getLine(); // WITH
        line = fileIn.getLine();
        String atributo, tipoAtributo, nomeAtributo = null;
        while (!(line.equals("END"))) {//lê atributos até encontrar o token END
            while (line.compareTo("") == 0) {
                line = fileIn.getLine();
            }
            atributo = storeAttributes(line);
            StringTokenizer tokens = new StringTokenizer(atributo);
            tipoAtributo = tokens.nextToken();
            if (tipoAtributo.equals("children")) {
                nomeAtributo = atributo.substring(atributo.indexOf("children") + 9, atributo.length());
                actor.setChildren(nomeAtributo);
            } else if (tipoAtributo.equals("name")) {
                //nomeAtributo = tokens.nextToken();
                nomeAtributo = atributo.substring(atributo.indexOf("name") + 6, atributo.length() - 1);
                actor.setName(nomeAtributo);
            } else if (tipoAtributo.equals("links")) {
                openOME = false;
                //nomeAtributo = tokens.nextToken();
                nomeAtributo = atributo.substring(atributo.indexOf("links") + 5, atributo.length());
                actor.setLink(nomeAtributo);
            }
            line = fileIn.getLine();
        }
        return actor;
    }

    /**
     * Cria um Elemento IStar (Objetivo, Tarefa, SoftGoal ou Recurso)
     *
     * @param element elemento
     * @param cod codigo do elemento
     * @param line linha do arquivo texto
     * @return IStarElement um elemento (Objetivo, Tarefa, SoftGoal ou Recurso)
     */
    private IStarElement createElement(IStarElement element, String cod, String line) {
        element.setCod(cod); //armazena o Código Element_x
        line = fileIn.getLine(); // WITH
        line = fileIn.getLine();
        String atributo, tipoAtributo, nomeAtributo = null;
        while (!(line.equals("END"))) { //lê atributos até encontrar o token END
            while (line.compareTo("") == 0) {
                line = fileIn.getLine();
            }
            atributo = storeAttributes(line);
            StringTokenizer tokens = new StringTokenizer(atributo);
            tipoAtributo = tokens.nextToken();
            if (tipoAtributo.equals("parent")) {
                nomeAtributo = atributo.substring(atributo.indexOf("parent") + 8, atributo.length());
                element.setParent(nomeAtributo);
            } else if (tipoAtributo.equals("name")) {
                nomeAtributo = tokens.nextToken();
                nomeAtributo = atributo.substring(atributo.indexOf("name") + 4, atributo.length());
                element.setName(nomeAtributo);
            } else if (tipoAtributo.equals("links")) {
                openOME = false;
                nomeAtributo = tokens.nextToken();
                nomeAtributo = atributo.substring(atributo.indexOf("links") + 5, atributo.length());
                element.setLink(nomeAtributo);
            }
            line = fileIn.getLine();
        }
        return element;
    }

    /**
     * Cria um Link IStar
     *
     * @param link elemento
     * @param cod codigo do link (Element_x)
     * @param line linha do arquivo texto
     * @return IStarElement um elemento
     */
    private IStarLink createLink(IStarLink link, String cod, String line) {
        link.setCod(cod); //armazena o Código Element_x
        line = fileIn.getLine(); // WITH
        line = fileIn.getLine();
        String atributo, tipoAtributo, nomeAtributo = null;
        while (!(line.equals("END"))) { //lê atributos até encontrar o token END
            while (line.compareTo("") == 0) {
                line = fileIn.getLine();
            }
            atributo = storeAttributes(line);
            StringTokenizer tokens = new StringTokenizer(atributo);
            tipoAtributo = tokens.nextToken();
            if (tipoAtributo.equals("from")) {
                nomeAtributo = atributo.substring(atributo.indexOf("from") + 5, atributo.length());
                link.setFrom(nomeAtributo);
            } else if (tipoAtributo.equals("to")) {
                nomeAtributo = atributo.substring(atributo.indexOf("to") + 3, atributo.length());
                link.setTo(nomeAtributo);
            } else {
                nomeAtributo = tokens.nextToken();
                nomeAtributo = atributo.substring(atributo.indexOf("name") + 4, atributo.length());
                link.setName(nomeAtributo);
            }
            line = fileIn.getLine();
        }
        return link;
    }

    /**
     * Método responsável por preencher o atributo links dos actors, goals,
     * softgoals, tasks e resourcesprivate private ArrayList
     * <IStarActorElement>actors; private ArrayList
     * goals,softgoals,tasks,resources; //OMEElements private ArrayList
     * dependencies,meansEnds,decompositions,isas, inss, contributions,
     * isPartOfs; //OMELinks
     */
    private void createLinksElements() {
        int cont = 0;
        String from, to, nameLink; //da onde vem, para onde vai e o nome do link
        int tipoLink;
        for (cont = 0; cont < dependencies.size(); cont++) {
            tipoLink = 0;
            IStarLink dependencia = dependencies.get(cont);
            from = dependencia.getFrom();
            to = dependencia.getTo();
            nameLink = dependencia.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links de Dependência Criados");
        for (cont = 0; cont < isas.size(); cont++) {
            tipoLink = 1;
            IStarLink ligacaoISA = isas.get(cont);
            from = ligacaoISA.getFrom();
            to = ligacaoISA.getTo();
            nameLink = ligacaoISA.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links ISA Criados");
        for (cont = 0; cont < decompositions.size(); cont++) {
            tipoLink = 2;
            IStarLink decomposicaoDeTarefas = decompositions.get(cont);
            from = decomposicaoDeTarefas.getFrom();
            to = decomposicaoDeTarefas.getTo();
            nameLink = decomposicaoDeTarefas.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links de Decomposição de Tarefas Criados");
        for (cont = 0; cont < meansEnds.size(); cont++) {
            tipoLink = 3;
            IStarLink meioFim = meansEnds.get(cont);
            from = meioFim.getFrom();
            to = meioFim.getTo();
            nameLink = meioFim.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links Meio-Fim Criados");
        for (cont = 0; cont < inss.size(); cont++) {
            tipoLink = 4;
            IStarLink ligacaoINS = inss.get(cont);
            from = ligacaoINS.getFrom();
            to = ligacaoINS.getTo();
            nameLink = ligacaoINS.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links INS Criados");
        for (cont = 0; cont < isPartOfs.size(); cont++) {
            tipoLink = 5;
            IStarLink ligacaoIsPartOf = isPartOfs.get(cont);
            from = ligacaoIsPartOf.getFrom();
            to = ligacaoIsPartOf.getTo();
            nameLink = ligacaoIsPartOf.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links Is-Part-Of Criados");
        for (cont = 0; cont < occupiess.size(); cont++) {
            tipoLink = 6;
            IStarLink ligacaoOccupies = occupiess.get(cont);
            from = ligacaoOccupies.getFrom();
            to = ligacaoOccupies.getTo();
            nameLink = ligacaoOccupies.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links Occupies Criados");
        for (cont = 0; cont < playss.size(); cont++) {
            tipoLink = 7;
            IStarLink ligacaoPlays = playss.get(cont);
            from = ligacaoPlays.getFrom();
            to = ligacaoPlays.getTo();
            nameLink = ligacaoPlays.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
        //System.out.println("Links Plays Criados");
        for (cont = 0; cont < coverss.size(); cont++) {
            tipoLink = 8;
            IStarLink ligacaoCovers = coverss.get(cont);
            from = ligacaoCovers.getFrom();
            to = ligacaoCovers.getTo();
            nameLink = ligacaoCovers.getCod();
            createLinksElementsFromTo(from, to, nameLink, tipoLink);
        }
    }

    private void createLinksElementsFromTo(String from, String to, String nameLink, int typeLink) {
        setLinkActorElementCod(from, nameLink, typeLink);
        setLinkActorElementCod(to, nameLink, typeLink);
    }

    private void setLinkActorElementCod(String cod, String nameLink, int typeLink) {
        int contElementoLink = 0;
        boolean encontrou = false;

        while ((encontrou == false) && (contElementoLink <= 4)) {
            switch (contElementoLink) {
                case 0: { //	IStarActorElement
                    int posAtor = searchActorCod(cod);
                    if (posAtor != -1) {
                        encontrou = true;
                        IStarActorElement actor = actors.get(posAtor);
                        actor.setLink(typeLink, nameLink);
                    }
                    contElementoLink++;
                }
                break;
                case 1: { //IStarGoalElement goal
                    int posGoal = searchElementCod(contElementoLink, cod);
                    if (posGoal != -1) {
                        encontrou = true;
                        IStarElement goal = goals.get(posGoal);
                        goal.setLink(typeLink, nameLink);
                    }
                    contElementoLink++;
                }
                break;
                case 2: { //IStarTaskElement task 
                    int posTask = searchElementCod(contElementoLink, cod);
                    if (posTask != -1) {
                        encontrou = true;
                        IStarElement task = tasks.get(posTask);
                        task.setLink(typeLink, nameLink);
                    }
                    contElementoLink++;
                }
                break;
                case 3: { //IStarSoftGoalElement softgoal 
                    int posSoftGoal = searchElementCod(contElementoLink, cod);
                    if (posSoftGoal != -1) {
                        encontrou = true;
                        IStarElement softgoal = softgoals.get(posSoftGoal);
                        softgoal.setLink(typeLink, nameLink);
                    }
                    contElementoLink++;
                }
                break;
                case 4: { //IStarResourceElement resource
                    int posResource = searchElementCod(contElementoLink, cod);
                    if (posResource != -1) {
                        encontrou = true;
                        IStarElement resource = resources.get(posResource);
                        resource.setLink(typeLink, nameLink);
                    }
                    contElementoLink++;
                }
                break;
                default: {
                }
                break;
            }
        }
    }

    /**
     * Procura o Ator pelo código
     *
     * @param cod codigo do ator
     * @return posAtor retorna a posição do Ator, se não achou retorna -1
     */
    public int searchActorCod(String cod) {
        int contAtor = 0, posAtor = -1;
        boolean encontrou = false; //verifica se encontrou algum ator com o código (cod)

        // varre a lista de atores e compara os códigos (String)
        while ((encontrou == false) && (contAtor < actors.size())) {
            IStarActorElement act = actors.get(contAtor);
            if (cod.equalsIgnoreCase(act.getCod())) {
                encontrou = true;
                posAtor = contAtor;
            }
            contAtor++;
        }
        return posAtor;
    }

    /**
     * Procura o Elemento pelo código
     *
     * @param typeElement tipo do Elemento 0(goal),1(task)...
     * @param cod codigo procurado
     * @return pos retorna a posição do Elemento, se não achou retorna -1
     */
    public int searchElementCod(int typeElement, String cod) {
        boolean encontrou = false; //verifica se encontrou algum objetivo com o código (cod)
        int pos = -1;

        switch (typeElement) {
            case 1: { //IStarGoalElement goal
                int cont = 0;
                IStarElement goal = new IStarElement(); //cria um Objetivo i*
                while ((encontrou == false) && (cont < goals.size())) {
                    goal = goals.get(cont);
                    if (cod.compareTo(goal.getCod()) == 0) {
                        encontrou = true;
                        pos = cont;
                    }
                    cont++;
                }

            }
            break;
            case 2: { //IStarTaskElement task 
                int cont = 0;
                IStarElement task = new IStarElement(); //cria uma Tarefa i*
                while ((encontrou == false) && (cont < tasks.size())) {
                    task = tasks.get(cont);
                    if (cod.compareTo(task.getCod()) == 0) {
                        encontrou = true;
                        pos = cont;
                    }
                    cont++;
                }
            }
            break;
            case 3: { //IStarSoftGoalElement softgoal 
                int cont = 0;
                IStarElement softgoal = new IStarElement(); //cria um Objetivo Soft i*
                while ((encontrou == false) && (cont < softgoals.size())) {
                    softgoal = softgoals.get(cont);
                    if (cod.compareTo(softgoal.getCod()) == 0) {
                        encontrou = true;
                        pos = cont;
                    }
                    cont++;
                }
            }
            break;
            case 4: { //IStarResourceElement resource
                int cont = 0;
                IStarElement resource = new IStarElement(); //cria um Recurso i*
                while ((encontrou == false) && (cont < resources.size())) {
                    resource = resources.get(cont);
                    if (cod.compareTo(resource.getCod()) == 0) {
                        encontrou = true;
                        pos = cont;
                    }
                    cont++;
                }
            }
            break;
            default: {
            }
            break;
        }
        return pos;
    }

    /**
     * Verifica a posição do ator que representa o sistema no vetor de Atores
     *
     * @param systemActor codigo do ator do Sistema
     * @return pos retorna a posicao
     */
    private int getPositionActorSystem(String systemActor) {
        ArrayList<IStarActorElement> actorsOME = (ArrayList<IStarActorElement>) actors.clone();
        int posActorSistema = -1;

        for (int i = 0; i < actorsOME.size(); i++) {
            if ((actorsOME.get(i).getCod().equals(systemActor))) {
                posActorSistema = i;
            }
        }
        return posActorSistema;
    }

    /**
     * Verifica se o Diagrama é SD ou SR
     *
     * @param systemActor código do ator do Sistema
     * @return SR se for SR retorna true, se for SD retorna False
     */
    public boolean checkSDSR(String systemActor) {
        ArrayList<IStarActorElement> actorsOME = (ArrayList<IStarActorElement>) actors.clone();
        ArrayList childrens;
        childrens = actorsOME.get(getPositionActorSystem(systemActor)).getChildrens();
        boolean SR;
        if (!childrens.isEmpty()) {
            SR = true; //verifica se o Diagrama � SD ou SR
            //System.out.println("Diagrama SR - Razões Estratégicas");
        } else {
            SR = false;
            //System.out.println("Diagrama SD - Dependências Estratégicas");
        }
        return SR;
    }

    /**
     * @return Returns the actors.
     */
    public ArrayList getActors() {
        return actors;
    }

    /**
     * Retorna o Elemento Ator e seus atributos
     *
     * @param i posicao do Ator no vetor de Atores
     * @return actor
     */
    public IStarActorElement getActor(int i) {
        return (IStarActorElement) actors.get(i);
    }

    /**
     * @param actors The actors to set.
     */
    public void setActors(ArrayList actors) {
        this.actors = actors;
    }

    /**
     * @return Returns the agents.
     */
    public ArrayList getAgents() {
        return agents;
    }

    /**
     * Retorna o Elemento Agent e seus atributos
     *
     * @param i posicao do Agent no vetor de Agents
     * @return agent
     */
    public IStarActorElement getAgent(int i) {
        return (IStarActorElement) agents.get(i);
    }

    /**
     * @param agents The agents to set.
     */
    public void setAgents(ArrayList agents) {
        this.agents = agents;
    }

    /**
     * @return Returns the roles.
     */
    public ArrayList getRoles() {
        return roles;
    }

    /**
     * Retorna o Elemento Role e seus atributos
     *
     * @param i posicao do Role no vetor de Roles
     * @return role
     */
    public IStarActorElement getRole(int i) {
        return (IStarActorElement) roles.get(i);
    }

    /**
     * @param roles The roles to set.
     */
    public void setRoles(ArrayList roles) {
        this.roles = roles;
    }

    /**
     * @return Returns the positions.
     */
    public ArrayList getPositions() {
        return positions;
    }

    /**
     * Retorna o Elemento Position e seus atributos
     *
     * @param i posicao do Position no vetor de Positions
     * @return position
     */
    public IStarActorElement getPosition(int i) {
        return (IStarActorElement) positions.get(i);
    }

    /**
     * @param positions The positions to set.
     */
    public void setPositions(ArrayList positions) {
        this.positions = positions;
    }

    /**
     * @return Returns the decompositions.
     */
    public ArrayList<IStarLink> getDecompositions() {
        return decompositions;
    }

    /**
     * @return Returns the goals.
     */
    public ArrayList<IStarElement> getGoals() {
        return goals;
    }

    /**
     * @return Returns the isas.
     */
    public ArrayList<IStarLink> getIsas() {
        return isas;
    }

    /**
     * @return Returns the inss.
     */
    public ArrayList<IStarLink> getInss() {
        return inss;
    }

    /**
     * @return Returns the occupiess.
     */
    public ArrayList<IStarLink> getOccupiess() {
        return occupiess;
    }

    /**
     * @return Returns the playss.
     */
    public ArrayList<IStarLink> getPlayss() {
        return playss;
    }

    /**
     * @return Returns the coverss.
     */
    public ArrayList<IStarLink> getCoverss() {
        return coverss;
    }

    /**
     * @return Returns the contributions.
     */
    public ArrayList<IStarLink> getContributions() {
        return contributions;
    }

    /**
     * @return Returns the isPartOfs.
     */
    public ArrayList<IStarLink> getIsPartOfs() {
        return isPartOfs;
    }

    /**
     * @return Returns the meansEnds.
     */
    public ArrayList<IStarLink> getMeansEnds() {
        return meansEnds;
    }

    /**
     * @return Returns the resources.
     */
    public ArrayList<IStarElement> getResourcess() {
        return resources;
    }

    /**
     * @return Returns the dependencies.
     */
    public ArrayList getDependenciess() {
        return dependencies;
    }

    /**
     * @return Returns the softgoals.
     */
    public ArrayList<IStarElement> getSoftgoals() {
        return softgoals;
    }

    /**
     * @return Returns the tasks.
     */
    public ArrayList<IStarElement> getTasks() {
        return tasks;
    }

    /**
     * @param dependencies The dependencies to set.
     */
    public void setDependencies(ArrayList dependencies) {
        this.dependencies = dependencies;
    }

    public IStarElement getElement(String cod) {
        for (IStarElement task : tasks) {
            if (task.getCod().equals(cod)) {
                return task;
            }
        }
        for (IStarElement goal : goals) {
            if (goal.getCod().equals(cod)) {
                return goal;
            }
        }
        for (IStarElement resource : resources) {
            if (resource.getCod().equals(cod)) {
                return resource;
            }
        }
        return null;
    }

    public String getDirIn() {
        return dirIn;
    }

    /**
     * @return Returns the cod of the goal
     */
    public String getCodGoal(String cod, String name) {
        for (IStarElement goal : goals) {
            if (goal.getName().equals(name) && !cod.equals(goal.getCod())) {
                return goal.getCod();
            }
        }
        return "";
    }
    
    public void openFile(File inputFile, File outputFile) {
        fileIn = new FileInOut(inputFile, outputFile);
        dirIn = fileIn.getDirIn();
    }
}
