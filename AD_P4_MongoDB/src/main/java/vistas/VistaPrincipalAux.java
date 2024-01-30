package vistas;

import javax.swing.*; // Importa clases base de Swing
import java.awt.*; // Importa clases para manejo de componentes gráficos
import java.awt.event.*; // Importa clases para manejar eventos

public class VistaPrincipalAux extends JPanel { // Declara la clase VistaPrincipalAux que extiende JPanel
    // Variables de instancia
    private static final long serialVersionUID = 1L;
    private JTextArea textArea; // Área de texto

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

    // Método main para probar la clase
    public static void main(String[] args) {
        // Crear una instancia de JFrame para contener la vista principal
        JFrame frame = new JFrame("Vista Principal");

        // Crear una instancia de la ventana VistaPrincipalAux
        VistaPrincipalAux vistaPrincipal = new VistaPrincipalAux();

        // Agregar la vista principal al contenido del JFrame
        frame.getContentPane().add(vistaPrincipal);

        // Configurar el tamaño de la ventana
        frame.setSize(900, 600);

        // Configurar la operación por defecto al cerrar la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        // Hacer visible la ventana
        frame.setVisible(true);

        // Ejemplo de cómo llamar al método para escribir en el JTextArea
        vistaPrincipal.escribirEnTextArea("¿Qué es Lorem Ipsum?\r\n"
        		+ "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.\r\n"
        		+ "\r\n"
        		+ "¿Por qué lo usamos?\r\n"
        		+ "Es un hecho establecido hace demasiado tiempo que un lector se distraerá con el contenido del texto de un sitio mientras que mira su diseño. El punto de usar Lorem Ipsum es que tiene una distribución más o menos normal de las letras, al contrario de usar textos como por ejemplo \"Contenido aquí, contenido aquí\". Estos textos hacen parecerlo un español que se puede leer. Muchos paquetes de autoedición y editores de páginas web usan el Lorem Ipsum como su texto por defecto, y al hacer una búsqueda de \"Lorem Ipsum\" va a dar por resultado muchos sitios web que usan este texto si se encuentran en estado de desarrollo. Muchas versiones han evolucionado a través de los años, algunas veces por accidente, otras veces a propósito (por ejemplo insertándole humor y cosas por el estilo).\r\n"
        		+ "\r\n"
        		+ "\r\n"
        		+ "¿De dónde viene?\r\n"
        		+ "Al contrario del pensamiento popular, el texto de Lorem Ipsum no es simplemente texto aleatorio. Tiene sus raices en una pieza cl´sica de la literatura del Latin, que data del año 45 antes de Cristo, haciendo que este adquiera mas de 2000 años de antiguedad. Richard McClintock, un profesor de Latin de la Universidad de Hampden-Sydney en Virginia, encontró una de las palabras más oscuras de la lengua del latín, \"consecteur\", en un pasaje de Lorem Ipsum, y al seguir leyendo distintos textos del latín, descubrió la fuente indudable. Lorem Ipsum viene de las secciones 1.10.32 y 1.10.33 de \"de Finnibus Bonorum et Malorum\" (Los Extremos del Bien y El Mal) por Cicero, escrito en el año 45 antes de Cristo. Este libro es un tratado de teoría de éticas, muy popular durante el Renacimiento. La primera linea del Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", viene de una linea en la sección 1.10.32\r\n"
        		+ "\r\n"
        		+ "El trozo de texto estándar de Lorem Ipsum usado desde el año 1500 es reproducido debajo para aquellos interesados. Las secciones 1.10.32 y 1.10.33 de \"de Finibus Bonorum et Malorum\" por Cicero son también reproducidas en su forma original exacta, acompañadas por versiones en Inglés de la traducción realizada en 1914 por H. Rackham.");
    }
}
