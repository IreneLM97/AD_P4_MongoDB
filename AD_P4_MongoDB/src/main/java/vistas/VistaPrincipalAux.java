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
        cuerpo_info.setLayout(new MigLayout("", "[50px][800px,grow][50px]", "[400px,grow][50px]"));
        
        // Envuelve el panel de información con un JScrollPane
        JScrollPane scrollPane = new JScrollPane();
        cuerpo_info.add(scrollPane, "cell 1 0,grow");

        // Panel para mostrar los elementos (Display)
        displayPanel = new JPanel();
        displayPanel.setBorder(null);
        displayPanel.setBackground(new Color(234, 221, 255));
        scrollPane.setViewportView(displayPanel); // Establece el panel como vista del JScrollPane
        displayPanel.setLayout(new GridLayout(0, 1, 0, 5)); // GridLayout con 1 columna y filas dinámicas
    }
    
    // Método para escribir en el JTextArea
    public void escribirEnTextArea(String texto) {
//        textArea.append(texto + "\n");
    }    
    
    public void agregarTablas(List<Document> documents) {
        for (Document doc : documents) {

            // Crear tabla para el documento
            JTable table = createTableFromDocument(doc);
            JScrollPane tableScrollPane = new JScrollPane(table);
            tableScrollPane.setBackground(Color.BLACK);
            tableScrollPane.setPreferredSize(new Dimension(200, 55));
            tableScrollPane.setBackground(Color.green);
            displayPanel.add(tableScrollPane, BorderLayout.CENTER);
        }
        displayPanel.revalidate();
    }

    private JTable createTableFromDocument(Document doc) {
        String[] columnNames = new String[doc.size()];
        int index = 0;
        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            columnNames[index++] = entry.getKey();
        }

        Object[][] data = new Object[1][doc.size()];
        int columnIndex = 0;
        for (Object value : doc.values()) {
            data[0][columnIndex++] = value;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Desactiva el ajuste automático de columnas

        // Renderizador de celdas para centrar el texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Aplica el renderizador a todas las columnas de la tabla
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ajusta el ancho de las columnas para adaptarse al contenido más amplio
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Ancho mínimo inicial
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 20, width);
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }

        return table;
    }



}
