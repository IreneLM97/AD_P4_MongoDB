package vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaInsertar extends JPanel {
	private static final long serialVersionUID = 1L;
	private VistaPrincipalAux vistaPrincipalAux;

    public VistaInsertar(VistaPrincipalAux vistaPrincipalAux) {
        this.vistaPrincipalAux = vistaPrincipalAux;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
    }

    // Método para cerrar esta vista
    private void cerrar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }
}
