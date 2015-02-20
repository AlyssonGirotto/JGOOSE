package br.unioeste.jgoose.view;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.UseCases.ActorISA;
import br.unioeste.jgoose.UseCases.Step;
import br.unioeste.jgoose.UseCases.UseCase;
import br.unioeste.jgoose.controller.Controller;
import br.unioeste.jgoose.model.ExportXMIAction;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Diego Peliser
 */
public class UseCasesDiagramView extends JFrame {

    private Actor actor;
    private String useCase;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem fileExportXMI;
    private ArrayList<ActorISA> isas;
    private ArrayList<String> casesPainted = new ArrayList<>();
    private ArrayList<Point> pointsPainted = new ArrayList<>();
    private ArrayList<UseCase> useCases = new ArrayList<>();

    public UseCasesDiagramView(Actor actor, String caseSelected) {
        isas = Controller.getIsas();
        this.setTitle("Diagrama de Casos de Uso");
        this.setSize(550, 600);
        this.setLocationRelativeTo(null);
        this.actor = actor;
        this.useCase = caseSelected;
        this.menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        this.menuFile = new JMenu("Arquivo");
        this.menuBar.add(menuFile);
        this.fileExportXMI = new JMenuItem("Exportar XMI");
        ActionListener exportarXMI = new ExportXMIAction(this.actor, this.useCase);
        this.fileExportXMI.addActionListener(exportarXMI);
        this.menuFile.add(fileExportXMI);
        Image Icone;
        Icone = Toolkit.getDefaultToolkit().getImage("./src/main/resources/icons/usecase_diagram_wiz.gif");
        setIconImage(Icone);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (actor == null) {
            return;
        }
        Point actorLocation = new Point(50, 400);
        // pinta o Ator
        paintActor(actor.getName(), actorLocation, g);
        Point locationUseCase = new Point(400, 50);
        useCases = actor.getUseCases();
        for (UseCase useCase : useCases) {
            // pinta o caso de uso
            paintUseCase(useCase.getName(), locationUseCase, g);
            // guarda os casos de usos pintados e sua localização
            casesPainted.add(useCase.getCod());
            pointsPainted.add(new Point((int) locationUseCase.getX(), (int) locationUseCase.getY()));
            // pinta a ligação do ator com o caso de uso
            paintLink("", actorLocation, locationUseCase, g);
            locationUseCase.y += 100;
            locationUseCase.x -= 40;
        }
        for (UseCase useCase : useCases) {
            for (Step step : useCase.getSteps()) {
                if (step.isInclude()) {
                    
                    String codInclude = this.getFather(step.getCod());
                    Point pai = this.getPoint(useCase.getCod());
                    Point filho = this.getPoint(codInclude);
                    paintLink("<<include>>", pai, filho, g);
                }
            }
        }
        Point pAtor = new Point(20, 250);
        Point pAtor2 = new Point(50, 400);
        for (ActorISA isa : isas) {
            if (isa.getName().equals(actor.getName())) {
                // pinta ator Pai
                paintActor(isa.getNameFathers().get(0), pAtor, g);
                pAtor.setLocation(40, 280);
                pAtor2.setLocation(30, 360);
                // pinta ligação do entra Pai e Filho
                paintLink("<<generalization>>", pAtor2, pAtor, g);
            }
        }

    }

    /**
     *
     * Função que desenha um ator p = new Point(50, 400);
     */
    public void paintActor(String name, Point p, Graphics g) {
        //cabeça
        g.drawOval((int) p.getX(), (int) p.getY(), 20, 20);
        //tronco
        g.drawLine((int) (p.getX() + 10), (int) (p.getY() + 20), (int) (p.getX() + 10), (int) p.getY() + 40);
        //braços
        g.drawLine((int) (p.getX() + 10), (int) (p.getY() + 25), (int) (p.getX() + 0), (int) p.getY() + 30);
        g.drawLine((int) (p.getX() + 10), (int) (p.getY() + 25), (int) (p.getX() + 20), (int) p.getY() + 30);
        //pernas
        g.drawLine((int) (p.getX() + 10), (int) (p.getY() + 40), (int) (p.getX() + 0), (int) p.getY() + 45);
        g.drawLine((int) (p.getX() + 10), (int) (p.getY() + 40), (int) (p.getX() + 20), (int) p.getY() + 45);
        //nome
        g.drawString(name, (int) (p.getX() + -10), (int) p.getY() + -5);
    }

    /**
     *
     * Função que desenha um Caso de Uso
     *
     */
    public void paintUseCase(String name, Point p, Graphics g) {
        //circulo do caso de uso
        g.drawOval((int) (p.getX()), (int) (p.getY()), 100, 50);
        //pinta o nome do caso de usos
        g.drawString(name, (int) (p.getX()) + 5, (int) (p.getY()) + 25);
    }

    /**
     *
     * Função que desenha uma ligação
     *
     */
    public void paintLink(String name, Point p1, Point p2, Graphics g) {
        //pinta ligação
        if (name.equals("<<include>>")) {
            //Caso de Uso Pai abaixo
            if (p1.y < p2.y) {
                g.drawLine((int) p1.getX() + 20, (int) p1.getY() + 44, (int) p2.getX() + 40, (int) p2.getY());
                //pinta seta
                g.drawLine((int) p2.getX() + 33, (int) p2.getY() - 10, (int) p2.getX() + 40, (int) p2.getY());
                g.drawLine((int) p2.getX() + 50, (int) p2.getY() - 10, (int) p2.getX() + 40, (int) p2.getY());
            } else if (p1.y > p2.y) { // Caso de Uso Pai acima
                g.drawLine((int) p1.getX() + 30, (int) p1.getY(), (int) p2.getX() + 20, (int) p2.getY() + 45);
                //pinta seta
                g.drawLine((int) p2.getX() + 8, (int) p2.getY() + 52, (int) p2.getX() + 20, (int) p2.getY() + 45);
                g.drawLine((int) p2.getX() + 25, (int) p2.getY() + 55, (int) p2.getX() + 20, (int) p2.getY() + 45);
            }
        } else if (name.equals("ISA")) {
            g.drawLine((int) (p1.getX()) + 30, (int) (p1.getY()) + 20, (int) (p2.getX()) - 10, (int) (p2.getY()) + 30);
            //pinta seta
            g.drawLine((int) (p2.getX()) - 10, (int) (p2.getY()) + 30, (int) p2.getX() - 15, (int) p2.getY() + 40);
            g.drawLine((int) (p2.getX()) - 10, (int) (p2.getY()) + 30, (int) p2.getX(), (int) p2.getY() + 35);
            g.drawLine((int) p2.getX() - 15, (int) p2.getY() + 40, (int) p2.getX(), (int) p2.getY() + 35);
            name = "";

        } else {
            g.drawLine((int) (p1.getX()) + 20, (int) (p1.getY()) + 20, (int) (p2.getX()), (int) (p2.getY()) + 20);
        }
        //pinta nome - pode ser nada, include ou extend
        g.drawString(name, (int) (p1.getX() + p2.getX()) / 2, (int) ((p1.getY() + p2.getY()) / 2) + 20);
    }

    /**
     * Returns the point of the actor
     *
     * @param cod
     * @return the point
     */
    public Point getPoint(String cod) {
        int n = casesPainted.size();
        for (int i = 0; i < n; i++) {
            if (casesPainted.get(i).equals(cod)) {
                return pointsPainted.get(i);
            }
        }
        return null;
    }

    private String getFather(String cod) {
        for (UseCase useCase : useCases) {
                if (useCase.getCodDecomposedElement().equals(cod)) {
                    return useCase.getCod();
                }
        }
        return null;
    }
}
