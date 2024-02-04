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

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Insertar Nuevo Elemento");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Aquí puedes agregar los campos y botones necesarios para insertar un nuevo elemento

        JButton insertButton = new JButton("Insertar");
        insertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes agregar la lógica para insertar el nuevo elemento
                // Una vez completado, puedes cerrar esta vista y actualizar la vista principal si es necesario
                // Por ejemplo:
                // vistaPrincipalAux.actualizarVista();
                // cerrar();
            }
        });

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Agrega aquí los campos necesarios para insertar el nuevo elemento
        contentPanel.add(insertButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Método para cerrar esta vista
    private void cerrar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }
}
