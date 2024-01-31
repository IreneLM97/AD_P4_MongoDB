package vistas;

import javax.swing.*; // Importa clases base de Swing
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*; // Importa clases para manejo de componentes gráficos
import java.awt.event.*;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import net.miginfocom.swing.MigLayout; // Importa clases para manejar eventos

public class VistaPrincipalAux extends JPanel { // Declara la clase VistaPrincipalAux que extiende JPanel
    // Variables de instancia
    private static final long serialVersionUID = 1L;
    JPanel displayPanel;
    /**
     * Constructor de la clase VistaPrincipalAux
     */
    public VistaPrincipalAux() {
    	setBounds(new Rectangle(0, 0, 900, 600));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setLayout(new BorderLayout(0, 0));

        // Configuración de la cabecera
        JPanel cabecera = new JPanel();
        cabecera.setBorder(null);
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
        cuerpo_botones.setBackground(new Color(238, 238, 238));
        cuerpo_botones.setPreferredSize(new Dimension(900, 30));
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
        insertar.setIcon(new ImageIcon(VistaPrincipalAux.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Indent-White.png")));
        insertar.setForeground(new Color(255, 255, 255));
        insertar.setBackground(new Color(0, 102, 153));
        insertar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("BOTON INSERTAR");
            }
        });
        GridBagConstraints gbc_insertar = new GridBagConstraints();
        gbc_insertar.gridx = 3;
        gbc_insertar.gridy = 1;
        cuerpo_botones.add(insertar, gbc_insertar);

        // Botón FILTRAR
        JButton filtrar = new JButton("FILTRAR");
        filtrar.setIcon(new ImageIcon(VistaPrincipalAux.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Paste-White.png")));
        filtrar.setForeground(new Color(255, 255, 255));
        filtrar.setBackground(new Color(0, 102, 153));
        GridBagConstraints gbc_filtrar = new GridBagConstraints();
        gbc_filtrar.gridx = 5;
        gbc_filtrar.gridy = 1;
        cuerpo_botones.add(filtrar, gbc_filtrar);
        
        JPanel cuerpo_info = new JPanel();
        panel.add(cuerpo_info, BorderLayout.CENTER);
        cuerpo_info.setLayout(new MigLayout("", "[50px][800px,grow][50px]", "[420px,grow][50px]"));
        
        // Envuelve el panel de información con un JScrollPane
        JScrollPane scrollPane = new JScrollPane();
        cuerpo_info.add(scrollPane, "cell 1 0,grow");

        // Panel para mostrar los elementos (Display)
        displayPanel = new JPanel();
        displayPanel.setBackground(new Color(223, 221, 255));
        scrollPane.setViewportView(displayPanel);
        
     // Agregar un ComponentListener para detectar cambios en el tamaño de la ventana
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarColumnasDisplayPanel();
            }
        });
    }
    
    // Método para escribir en el JTextArea
    public void escribirEnTextArea(String texto) {
//        textArea.append(texto + "\n");
    }    
    
    public void agregarTablas(List<Document> documents) {
        displayPanel.removeAll();

        // Determinar el número máximo de columnas basado en el ancho de la ventana
        int screenWidth = this.getWidth();
        int preferredTableWidth = 300; // Ancho preferido de la tabla
        int numColumns = Math.max(screenWidth / preferredTableWidth, 1);

        GridLayout gl_displayPanel = new GridLayout(0, numColumns);
        gl_displayPanel.setHgap(5);
        gl_displayPanel.setVgap(5);
        displayPanel.setLayout(gl_displayPanel);

        for (Document doc : documents) {
            JTable table = createTableFromDocument(doc);
            JScrollPane scrollpane = new JScrollPane(table);
            scrollpane.setMinimumSize(new Dimension(10, 10));
            scrollpane.setPreferredSize(new Dimension(0, 300));
            displayPanel.add(scrollpane);
        }

        displayPanel.revalidate();
        ajustarColumnasDisplayPanel(); // Ajustar columnas después de agregar tablas
    }

    private JTable createTableFromDocument(Document doc) {
        String[] columnNames = {"CLAVE", "VALOR"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };

        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30); // Ajustar la altura de las filas

        // Establecer estilos para el encabezado de la tabla
        table.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(52, 0, 111));

        // Establecer renderizador de celdas personalizado para centrar el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Establecer estilos para las celdas de la tabla
        table.setFont(new Font("Calibri", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return table;
    }

    private void ajustarColumnasDisplayPanel() {
        int screenWidth = this.getWidth();
        int preferredTableWidth = 500; // Ancho preferido de la tabla
        int numColumns = Math.max(screenWidth / preferredTableWidth, 1);
        ((GridLayout) displayPanel.getLayout()).setColumns(numColumns);
        displayPanel.revalidate();
    }
}