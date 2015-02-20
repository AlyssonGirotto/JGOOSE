package br.unioeste.jgoose.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author Leonardo Merlin - leonardo.merlin at unioeste.br
 */
public class EditorWindowListener implements WindowListener {

    private final JFrame jgooseJFrame;
    private final JFrame editorJFrame;

    public EditorWindowListener(final JFrame jgooseView, final JFrame e4jView) {
        this.jgooseJFrame = jgooseView;
        this.editorJFrame = e4jView;
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
        this.windowClosed(we);
    }

    @Override
    public void windowClosed(WindowEvent we) {
        JFrame closedFrame = (JFrame) we.getSource();

        if (closedFrame == this.editorJFrame) {
            this.editorJFrame.setVisible(false);
            this.jgooseJFrame.setVisible(true);
            this.jgooseJFrame.setState(JFrame.NORMAL);
        }else{
            //force jgoose to exit.
            System.exit(0);
        }
    }

    @Override
    public void windowIconified(WindowEvent we) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }
}
