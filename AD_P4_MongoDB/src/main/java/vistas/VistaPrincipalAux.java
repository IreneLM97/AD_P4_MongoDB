package vistas;

import javax.swing.*; // Importa clases base de Swing
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*; // Importa clases para manejo de componentes gráficos
import java.awt.event.*;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import net.miginfocom.swing.MigLayout; // Importa clases para manejar eventos
import repositories.products.ProductsRepository;

public class VistaPrincipalAux extends JPanel { // Declara la clase VistaPrincipalAux que extiende JPanel
	
	private MongoCollection<Document> collection;
	
    // Variables de instancia
    private static final long serialVersionUID = 1L;
    JPanel displayPanel;
    /**
     * Constructor de la clase VistaPrincipalAux
     */
    public VistaPrincipalAux(MongoCollection<Document> collection) {
    	this.collection = collection;
    	
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
        displayPanel.setLayout(new GridLayout(1, 0, 5, 5));
        
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

        // Configurar el layout del displayPanel como GridBagLayout
        displayPanel.setLayout(new GridBagLayout());

        // Restricciones iniciales para el GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Determinar el número máximo de columnas basado en el ancho de la ventana
        int screenWidth = this.getWidth();
        int preferredTableWidth = 300; // Ancho preferido de la tabla
        int numColumns = Math.max(screenWidth / preferredTableWidth, 1);
        int currentColumn = 0;
        int remainingWidth = screenWidth; // Ancho restante disponible

        for (Document doc : documents) {
            JTable table = createTableFromDocument(doc);

            // Calcular la altura de la tabla multiplicando el número de filas por la altura de cada fila
            int tableHeight = table.getRowCount() * table.getRowHeight();

            // Calcular gridheight basado en la altura de la tabla y la altura de fila deseada
            int gridHeight = Math.max(tableHeight / 30, 1); // Si la tabla mide menos de 30px, gridHeight será 1

            gbc.gridheight = gridHeight;
            gbc.gridx = currentColumn;
            gbc.gridy = gbc.gridy;
            gbc.weightx = 1; // Asegurar que la tabla se expanda horizontalmente

            // Verificar si hay suficiente espacio horizontal para insertar otra tabla
            int tableWidth = table.getPreferredSize().width;
            if (tableWidth <= remainingWidth) {
                gbc.gridwidth = 1;
                remainingWidth -= tableWidth + gbc.insets.left + gbc.insets.right;
            } else {
                // No hay suficiente espacio horizontal, mover a la siguiente fila
                gbc.gridwidth = numColumns - currentColumn;
                remainingWidth = screenWidth;
                currentColumn = 0;
                gbc.gridy += gridHeight;
            }
            Object idObject = doc.values().iterator().next();
            String tableId = (idObject != null) ? idObject.toString() : ""; // Convertir a cadena

            // Crear un JPanel para envolver la tabla y hacerla transparente
            JPanel panel1 = new JPanel(new BorderLayout());
            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout para alinear botones a la derecha
            panel2.setName(tableId);
            panel2.setOpaque(false); // Hacer el panel completamente transparente
            panel2.setBorder(null); // Eliminar cualquier borde
            panel1.setOpaque(false); // Hacer el panel completamente transparente
            panel1.setBorder(null); // Eliminar cualquier borde

            // Crea los botones y los agrega al panel2
            JButton boton1 = new JButton("");
            JButton boton2 = new JButton("");
            boton1.setPreferredSize(new Dimension(30, 30));
            boton1.setForeground(new Color(255, 255, 255));
            boton1.setBackground(new Color(52, 0, 111));
            boton1.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Edicion.png")));
            boton1.setName(tableId); // Establecer el ID como el nombre del botón
         // ActionListener para el botón de edición
                boton1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Obtener la tabla por su nombre
                        JTable table = getTableByName(boton2.getName());

                        // Verificar si la tabla existe y realizar operaciones con ella
                        if (table != null) {
                        	table.setEnabled(true); // Habilitar edición de la tabla
                            table.setBackground(Color.WHITE);
                            JButton confirmButton = new JButton();
                            confirmButton.setBackground(new Color(14, 161, 41));
                			confirmButton.setPreferredSize(new Dimension(30, 30));
                			confirmButton.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Confirmacion.png")));
                            confirmButton.setName(boton2.getName());
                            confirmButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    int opcion = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
                                    if (opcion == JOptionPane.YES_OPTION) {
                                	guardarCambiosEnBD(table);
                                    }else {
                                    	ProductsRepository pr = new ProductsRepository();
                                    	agregarTablas(pr.findAll(collection));
                                    }
                                	JPanel panel2 = getPanel2ByName(confirmButton.getName());
                                    panel2.remove(confirmButton);
                                    table.setBackground(new Color(238, 238, 238));
                                    displayPanel.revalidate(); 
                                    JTable table = getTableByName(confirmButton.getName());
                                    table.setEnabled(false);
                                    table.clearSelection();
                                }
                            });
                            JPanel panel2 = getPanel2ByName(boton2.getName());
                            panel2.add(confirmButton);
                            displayPanel.revalidate();                            
                        }else {
                        	System.out.println("TABLA NO ENCONTRADA");
                        }
                    }
                });


            boton2.setPreferredSize(new Dimension(30, 30));
            boton2.setForeground(new Color(255, 255, 255));
            boton2.setBackground(new Color(52, 0, 111));
            boton2.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Papelera.png")));
         // Obtener el valor de la clave "id"
            boton2.setName(tableId); // Establecer el ID como el nombre del botón
            boton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Obtén el panel que contiene el botón
                    JPanel panelContenedor = (JPanel) boton2.getParent().getParent(); // El botón está contenido en el panel2, que a su vez está contenido en el panel1
                    
                    // Muestra un cuadro de diálogo de confirmación
                    int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar este elemento?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    
                    // Verifica la opción seleccionada por el usuario
                    if (opcion == JOptionPane.YES_OPTION) {
                        // Si el usuario hace clic en "SI", entonces elimina el elemento
                        ProductsRepository pr = new ProductsRepository();
                        pr.deleteOneById(boton2.getName(), collection);
                        
                        // Remueve el panel que contiene la tabla del displayPanel
                        displayPanel.remove(panelContenedor);

                        // Revalida y repinta el displayPanel para que se actualice la vista
                        displayPanel.revalidate();
                        displayPanel.repaint();
                        ajustarColumnasDisplayPanel();
                    } else {
                        // Si el usuario hace clic en "NO" o cierra la ventana, no hagas nada
                        // Opcionalmente, puedes mostrar un mensaje o realizar otra acción aquí
                    }
                }
            });
            panel2.add(boton1);
            panel2.add(boton2);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false); // Hacer el panel completamente transparente
            panel.setBorder(null); // Eliminar cualquier borde
            table.setOpaque(false);
            table.setName(tableId);
            panel.add(table.getTableHeader(), BorderLayout.NORTH); // Agregar el encabezado de la tabla al panel
            panel.add(table, BorderLayout.CENTER); // Agregar la tabla al panel
            panel1.add(panel2, BorderLayout.NORTH); // Agregar el panel de botones al panel1
            panel1.add(panel, BorderLayout.CENTER); // Agregar la tabla al panel1

            // Agregar panel1 al displayPanel con las restricciones adecuadas
            displayPanel.add(panel1, gbc);

            // Actualizar las coordenadas de gridbag para la próxima tabla si se inserta en la misma fila
            if (gbc.gridwidth == 1) {
                currentColumn++;
            }
        }
        ajustarColumnasDisplayPanel();
        displayPanel.revalidate();
    }

    private JTable createTableFromDocument(Document doc) {
        String[] columnNames = {"CLAVE", "VALOR"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Agregar datos del documento
        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30); // Ajustar la altura de las filas
        table.setEnabled(false); // Habilitar edición de la tabla

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
        table.setBackground(new Color(235, 235, 235));
        table.setGridColor(new Color(200, 200, 200));
        table.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return table;
    }

    private void ajustarColumnasDisplayPanel() {
        int screenWidth = this.getWidth();
        int preferredTableWidth = 500; // Ancho preferido de la tabla
        int numColumns = Math.max(screenWidth / preferredTableWidth, 1);

        // Configurar el layout del displayPanel como GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        displayPanel.setLayout(new GridBagLayout());
        
        // Establecer las restricciones para la distribución de las tablas
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Asegurar que las tablas se expandan horizontalmente
        
        gbc.insets = new Insets(5, 5, 5, 5); // Establecer el margen entre las celdas
        
        

        // Obtener todas las componentes (tablas) del displayPanel
        Component[] components = displayPanel.getComponents();
        
        // Iterar sobre las componentes y establecerlas en el layout
        for (Component component : components) {
            displayPanel.add(component, gbc);
            
            // Actualizar las coordenadas de gridbag para la próxima tabla
            gbc.gridx++;
            if (gbc.gridx == numColumns) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }
        
        // Validar el panel para que se actualice la disposición
        displayPanel.revalidate();
    }
    
 // Método para obtener la tabla por su nombre
    private JTable getTableByName(String name) {
        Component[] components = displayPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel1 = (JPanel) component;
                Component[] panel1Components = panel1.getComponents();
                for (Component panel1Component : panel1Components) {
                    if (panel1Component instanceof JPanel) {
                        JPanel panel = (JPanel) panel1Component;
                        Component[] panelComponents = panel.getComponents();
                        for (Component panelComponent : panelComponents) {
                            if (panelComponent instanceof JTable && panelComponent.getName() != null && panelComponent.getName().equals(name)) {
                                return (JTable) panelComponent;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("TABLA NO ENCONTRADA");
        return null; // Si no se encuentra la tabla
    }
    
    private JPanel getPanel2ByName(String name) {
        Component[] components = displayPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel1 = (JPanel) component;
                Component[] panel1Components = panel1.getComponents();
                for (Component panel1Component : panel1Components) {
                    if (panel1Component instanceof JPanel) {
                        JPanel panel2 = (JPanel) panel1Component;
                        if (panel2.getName() != null && panel2.getName().equals(name)) {
                            return panel2;
                        }
                    }
                }
            }
        }
        return null; // Si no se encuentra el panel2
    }



    
    // Método para guardar los cambios en la base de datos
    private void guardarCambiosEnBD(JTable table) {
        // Obtener el ID del nombre de la tabla
        String id = table.getName();
        // Construir el JSON de actualización a partir de los datos de la tabla
        StringBuilder jsonUpdateBuilder = new StringBuilder("{");
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 1; i < model.getRowCount(); i++) { // Comenzar desde la segunda fila
            String columnName = (String) model.getValueAt(i, 0);
            Object columnValueObject = model.getValueAt(i, 1);
            String columnValue = (columnValueObject != null) ? columnValueObject.toString() : "";
            jsonUpdateBuilder.append("\"").append(columnName).append("\": \"").append(columnValue).append("\", ");
        }
        // Eliminar la coma adicional al final y cerrar el JSON
        jsonUpdateBuilder.delete(jsonUpdateBuilder.length() - 2, jsonUpdateBuilder.length());
        jsonUpdateBuilder.append("}");
        String jsonUpdate = jsonUpdateBuilder.toString();

        // Actualizar en la base de datos
        ProductsRepository pr = new ProductsRepository();
        pr.updateOneByCriteria(id, jsonUpdate, collection);
    }




}