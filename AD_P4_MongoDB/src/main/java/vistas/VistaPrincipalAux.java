package vistas;

import javax.swing.*; // Importa clases base de Swing

import java.awt.*; // Importa clases para manejo de componentes gráficos
import java.awt.event.*; // Importa clases para manejar eventos

public class VistaPrincipalAux extends JPanel { // Declara la clase VistaPrincipalAux que extiende JPanel
    // Variables de instancia
    private static final long serialVersionUID = 1L;
    private static JTextArea textArea; // Área de texto

    /**
     * Constructor de la clase VistaPrincipalAux
     */
    public VistaPrincipalAux() {
        // Configuración del panel principal
        setBounds(new Rectangle(0, 0, 900, 600));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setLayout(new BorderLayout(0, 0));

        // Configuración de la cabecera
        JPanel cabecera = new JPanel();
        cabecera.setBackground(new Color(52, 0, 111));
        cabecera.setPreferredSize(new Dimension(900, 100));
        add(cabecera, BorderLayout.NORTH);
        cabecera.setLayout(new BorderLayout(0, 0));

        // Etiqueta de título en la cabecera
        JLabel titulo_cabecera = new JLabel("GESTOR DE INVENTARIO");
        titulo_cabecera.setFont(new Font("Calibri", Font.BOLD, 48));
        titulo_cabecera.setHorizontalAlignment(SwingConstants.CENTER);
        titulo_cabecera.setHorizontalTextPosition(SwingConstants.CENTER);
        titulo_cabecera.setForeground(new Color(255, 255, 255));
        cabecera.add(titulo_cabecera, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        // Panel para los botones
        JPanel cuerpo_botones = new JPanel();
        cuerpo_botones.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        cuerpo_botones.setBackground(new Color(128, 128, 255));
        cuerpo_botones.setPreferredSize(new Dimension(900, 50));
        cuerpo_botones.setSize(new Dimension(0, 50));
        panel.add(cuerpo_botones, BorderLayout.NORTH);
        GridBagLayout gbl_cuerpo_botones = new GridBagLayout();
        gbl_cuerpo_botones.rowHeights = new int[] {12, 25, 12};
        gbl_cuerpo_botones.columnWidths = new int[] {100, 100, 100, 100, 100, 100, 100, 100, 100};
        gbl_cuerpo_botones.columnWeights = new double[]{};
        gbl_cuerpo_botones.rowWeights = new double[]{};
        cuerpo_botones.setLayout(gbl_cuerpo_botones);

        // Botón INSERTAR
        JButton insertar = new JButton("INSERTAR");
        insertar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("BOTON INSERTAR");
            }
        });
        GridBagConstraints gbc_insertar = new GridBagConstraints();
        gbc_insertar.insets = new Insets(0, 0, 5, 5);
        gbc_insertar.gridx = 3;
        gbc_insertar.gridy = 1;
        cuerpo_botones.add(insertar, gbc_insertar);

        // Botón FILTRAR
        JButton filtrar = new JButton("FILTRAR");
        GridBagConstraints gbc_filtrar = new GridBagConstraints();
        gbc_filtrar.insets = new Insets(0, 0, 5, 5);
        gbc_filtrar.gridx = 5;
        gbc_filtrar.gridy = 1;
        cuerpo_botones.add(filtrar, gbc_filtrar);

        // Área de texto
        textArea = new JTextArea();
        textArea.setForeground(new Color(0, 0, 0));
        textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Configura el salto de línea automático
        textArea.setWrapStyleWord(true); // Envolver solo palabras completas
        JScrollPane scrollpane = new JScrollPane(textArea);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollpane, BorderLayout.CENTER);
    }
    
    // Método para escribir en el JTextArea
    public void escribirEnTextArea(String texto) {
        textArea.append(texto + "\n");
    }    
}
