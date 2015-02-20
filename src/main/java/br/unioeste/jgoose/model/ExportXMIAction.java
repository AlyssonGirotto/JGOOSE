package br.unioeste.jgoose.model;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.UseCases.ActorISA;
import br.unioeste.jgoose.UseCases.Step;
import br.unioeste.jgoose.UseCases.UseCase;
import br.unioeste.jgoose.controller.Controller;
import br.unioeste.jgoose.e4j.filters.DefaultFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author Diego Peliser
 */
public class ExportXMIAction implements ActionListener {

    private Actor actorSelected; // ator selecionado
    private String nameUseCaseSelected; // nome do caso de uso selecionado
    private int codUseCaseSelected; // código do caso de uso selecionado
    private ArrayList<Integer> codFathers;  // código dos pais do ator selecionado
    private ArrayList<Integer> codUseCases; // código dos casos de uso, menos o selecionado
    private ArrayList<String> nameFathers;// nome dos pais do ator selecionado
    private ArrayList<String> nameUseCases;// nome dos casos de uso, menos o selecionado

    public ExportXMIAction(Actor actor, String caseSelected) {
        this.actorSelected = actor;
        this.nameUseCaseSelected = caseSelected;
        codFathers = new ArrayList<>();
        codUseCases = new ArrayList<>();
        nameFathers = new ArrayList<>();
        nameUseCases = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*
         * Insere início das informações
         */
        String xmiParte1 = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n"
                + "<XMI xmi.version = \"1.1\" xmlns:UML=\"'href://org.omg/UML/1.3\">\n"
                + "<XMI.header>\n"
                + "<XMI.documentation>\n"
                + "<XMI.owner></XMI.owner>\n"
                + "<XMI.contact></XMI.contact>\n"
                + "<XMI.exporter>StarUML.XMI-Addin</XMI.exporter>\n"
                + "<XMI.exporterVersion>1.0</XMI.exporterVersion>\n"
                + "<XMI.notice></XMI.notice>\n"
                + "</XMI.documentation>\n"
                + "<XMI.metamodel xmi.name = \"UML\" xmi.version = \"1.3\"/>\n"
                + "</XMI.header>\n"
                + "<XMI.content>\n"
                + "<UML:Model xmi.id=\"UMLProject.1\">\n"
                + "<UML:Namespace.ownedElement>\n"
                + "<UML:Model xmi.id=\"UMLModel.2\" name=\"Requirements\" visibility=\"public\" isSpecification=\"false\" namespace=\"UMLProject.1\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\">\n"
                + "<UML:Namespace.ownedElement>\n"
                + "<UML:Model xmi.id=\"UMLModel.3\" name=\"Business Concept Model\" visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.2\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\"/>\n"
                + "<UML:Model xmi.id=\"UMLModel.4\" name=\"Use Case Model\" visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.2\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\">\n"
                + "<UML:Namespace.ownedElement>\n";

        String xmiAtor = "";
        String xmiCaso = "";
        String xmiCasoSec = "";
        String xmiAssociacao = "";
        String xmiInclude = "";
        int j = 0;
        int umlAssociation = 1;
        boolean controle = false;

        /*
         * Insere parte do meio das informações
         */
        for (int i = 0; i < actorSelected.getUseCases().size(); i++) {
            // insere Ator ISA
            nameFathers = getFathers(actorSelected.getName());
            int n = nameFathers.size();
            if (n > 0) {
                controle = true;
                for (j = 0; j < n; j++) {
                    xmiAtor = "<UML:Actor xmi.id=\"UMLActor." + 1 + "\" name=\"" + actorSelected.getName() + "\" "
                            + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                            + "isAbstract=\"false\" specialization=\"UMLGeneralization." + codFathers.get(j)
                            + "\" participant=\"UMLAssociationEnd." + 1 + " ";
                }
            } else {
                xmiAtor = "<UML:Actor xmi.id=\"UMLActor." + 1 + "\" name=\"" + actorSelected.getName() + "\" "
                        + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                        + "isAbstract=\"false\" participant=\"UMLAssociationEnd." + 1 + " ";
            }
        }
        int aux = j;
        codUseCaseSelected = 1 + aux;

        /*
         * Insere casos de uso relacionados
         */
        for (UseCase caso : actorSelected.getUseCases()) {
            if (!isSelected(caso.getName())) { // verifica se não é o caso selecionado
                xmiAtor = xmiAtor + "UMLAssociationEnd." + (2 + aux) + " ";
                codUseCases.add(2 + aux++);
                nameUseCases.add(caso.getName());
            }
        }

        /*
         * Finaliza o ator
         */
        xmiAtor = xmiAtor + "\"/>\n";
        if (controle) {
            for (int i = 0; i < nameFathers.size(); i++) {
                xmiAtor = xmiAtor + "<UML:Actor xmi.id=\"UMLActor." + codFathers.get(i) + "\" name=\"" + nameFathers.get(i) + "\" "
                        + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                        + "isAbstract=\"false\" generalization=\"UMLGeneralization." + codFathers.get(i) + "\"/>\n";
            }
            controle = false;
        }

        /*
         * Insere caso de uso principal
         */
        xmiCaso = xmiCaso + "<UML:UseCase xmi.id=\"UMLUseCase." + codUseCaseSelected + "\" name=\"" + nameUseCaseSelected.replaceAll("\"", "") + "\" "
                + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                + "isAbstract=\"false\" participant=\"UMLAssociationEnd." + codUseCaseSelected + "\"";
        xmiCaso = xmiCaso + " include=\"";
        // insere includes do caso de uso
        for (int i = 0; i < nameUseCases.size(); i++) {
            System.out.println("nameUseCases: " + nameUseCases.get(i) + " nameUseCaseSelected: " + nameUseCaseSelected);
            if (isInclude(nameUseCases.get(i), nameUseCaseSelected)) {
                xmiCaso = xmiCaso + "UMLInclude." + codUseCases.get(i) + " ";
            }
        }
        xmiCaso = xmiCaso + "\"/>\n";

        /*
         * Insere casos de uso secundários
         */
        for (int i = 0; i < nameUseCases.size(); i++) {
            if (isInclude(nameUseCases.get(i), nameUseCaseSelected)) {
                xmiCasoSec = xmiCasoSec + "<UML:UseCase xmi.id=\"UMLUseCase." + codUseCases.get(i) + "\" name=\"" + nameUseCases.get(i).replaceAll("\"", "") + "\" "
                        + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                        + "isAbstract=\"false\" participant=\"UMLAssociationEnd." + codUseCases.get(i) + "\" include2=\"UMLInclude." + codUseCases.get(i) + "\"/>\n";
            } else {
                xmiCasoSec = xmiCasoSec + "<UML:UseCase xmi.id=\"UMLUseCase." + codUseCases.get(i) + "\" name=\"" + nameUseCases.get(i).replaceAll("\"", "") + "\" "
                        + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" isRoot=\"false\" isLeaf=\"false\" "
                        + "isAbstract=\"false\" participant=\"UMLAssociationEnd." + codUseCases.get(i) + "\"/>\n";
            }
        }

        /*
         * Insere associações
         */
        xmiAssociacao = "<UML:Association xmi.id=\"UMLAssociation." + umlAssociation + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\">\n"
                + "<UML:Association.connection>\n"
                + "<UML:AssociationEnd xmi.id=\"UMLAssociationEnd." + codUseCaseSelected + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" "
                + "isNavigable=\"true\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" "
                + "association=\"UMLAssociation." + umlAssociation + "\" type=\"UMLActor." + 1 + "\"/>\n"
                + "<UML:AssociationEnd xmi.id=\"UMLAssociationEnd." + codUseCaseSelected + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" "
                + "isNavigable=\"true\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" "
                + "association=\"UMLAssociation." + umlAssociation + "\" type=\"UMLUseCase." + codUseCaseSelected + "\"/>\n"
                + "</UML:Association.connection>\n"
                + "</UML:Association>\n";
        for (int i = 0; i < nameUseCases.size(); i++) {
            umlAssociation++;
            xmiAssociacao = xmiAssociacao + "<UML:Association xmi.id=\"UMLAssociation." + umlAssociation + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\">\n"
                    + "<UML:Association.connection>\n"
                    + "<UML:AssociationEnd xmi.id=\"UMLAssociationEnd." + codUseCases.get(i) + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" "
                    + "isNavigable=\"true\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" "
                    + "association=\"UMLAssociation." + umlAssociation + "\" type=\"UMLActor." + 1 + "\"/>\n"
                    + "<UML:AssociationEnd xmi.id=\"UMLAssociationEnd." + codUseCases.get(i) + "\" name=\"\" visibility=\"public\" isSpecification=\"false\" "
                    + "isNavigable=\"true\" ordering=\"unordered\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" "
                    + "association=\"UMLAssociation." + umlAssociation + "\" type=\"UMLUseCase." + codUseCases.get(i) + "\"/>\n"
                    + "</UML:Association.connection>\n"
                    + "</UML:Association>\n";
        }
        for (int i = 0; i < nameUseCases.size(); i++) {
            if (isInclude(nameUseCases.get(i), nameUseCaseSelected)) {
                //insere include
                xmiInclude = xmiInclude + "<UML:Include xmi.id=\"UMLInclude." + codUseCases.get(i) + "\" name=\"\" "
                        + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" base=\"UMLUseCase." + codUseCaseSelected + "\" "
                        + "addition=\"UMLUseCase." + codUseCases.get(i) + "\"/>\n";
            }

        }
        /*
         * Insere ISAs
         */
        for (int i = 0; i < nameFathers.size(); i++) {
            xmiInclude = xmiInclude + "<UML:Generalization xmi.id=\"UMLGeneralization." + codFathers.get(i) + "\" name=\"\" "
                    + "visibility=\"public\" isSpecification=\"false\" namespace=\"UMLModel.4\" discriminator=\"\" "
                    + "child=\"UMLActor." + codFathers.get(i) + "\" parent=\"UMLActor." + 1 + "\"/>\n";
        }


        /*
         * Parte final de fechamento
         */
        String xmiParte3 = "</UML:Namespace.ownedElement>\n"
                + "</UML:Model>\n"
                + "</UML:Namespace.ownedElement>\n"
                + "</UML:Model> "
                + "</UML:Namespace.ownedElement>\n"
                + "</UML:Model>\n"
                + "</XMI.content>\n"
                + "</XMI>\n";

        /*
         * Salvar em arquivo.
         */
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new DefaultFileFilter("xmi", "XMI"));
        int resultado = fileChooser.showSaveDialog(null);
        if (resultado == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        } else {
            File arquivo = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(arquivo.getPath() + ".xml", true)) {
                String xmi = xmiParte1 + xmiAtor + xmiCaso + xmiCasoSec + xmiAssociacao + xmiInclude + xmiParte3;
                writer.write(xmi);
            } catch (IOException ex) {
            }
        }
    }

    /**
     *
     * @param children Children to get the fathers
     * @return Fathers of the children
     */
    private ArrayList<String> getFathers(String children) {
        ArrayList<String> names = new ArrayList<>();
        int aux = 2;
        for (ActorISA actor : Controller.getIsas()) {
            if (actor.getName().equals(children)) {
                for (String father : actor.getNameFathers()) {
                    names.add(father);
                    codFathers.add(aux++);
                }
            }
        }
        return names;
    }

    /**
     *
     * @param nameCase
     * @return If the use case is the selected
     */
    private boolean isSelected(String nameCase) {
        return this.nameUseCaseSelected.equals(nameCase);
    }

    /**
     *
     *
     * @param nameCase
     * @param nameFather
     * @return If the use case is include
     */
    private boolean isInclude(String nameCase, String nameFather) {
        for (UseCase useCase : actorSelected.getUseCases()) {
            if (useCase.getName().equals(nameFather)) {
                for (Step step : useCase.getSteps()) {
                    if (step.isInclude()) {
                        for (UseCase ucase : actorSelected.getUseCases()) {
                            if (ucase.getCodDecomposedElement().equals(step.getCod())) {
                                return true;
                            }
                        }
                    }
                }
            }
//            for (Step step : useCase.getSteps()) {
//                System.out.println("step.getName(): " + step.getName() + " useCase.getName(): " + useCase.getName());
//                if (step.getName().equals(nameCase) && useCase.getName().equals(nameFather)) {
//                    if (step.isInclude()) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
        }
        return false;
    }
}
