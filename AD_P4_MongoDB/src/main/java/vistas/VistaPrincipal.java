package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*; 
import java.awt.event.*;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import controller.MongoController;
import net.miginfocom.swing.MigLayout; 
import utils.JsonStringBuilder;

/**
 * Esta clase representa la vista principal de la aplicación.
 */
public class VistaPrincipal extends JPanel {
	private static final long serialVersionUID = 7191141515072057188L;
    private MongoController controller;
	
    // Declaramos variables como atributos para poder acceder a ellas desde toda la clase
    private JPanel displayPanel;
    private JButton eliminarBtn;
    private JButton filtrarBtn;
    private JButton eliminarFiltroBtn;
    private String filtroJson = null;

    /**
     * Constructor de la clase VistaPrincipal.
     * 
     * @param controller El controlador de MongoDB.
     */
    public VistaPrincipal(MongoController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    /**
     * Inicializa la interfaz de usuario de la vista principal.
     */
    private void initializeUI() {
    	// Configuración del panel principal
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

        // Panel contenedor de los botones y las tablas
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        // Panel para los botones
        JPanel cuerpo_botones = new JPanel();
        cuerpo_botones.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        cuerpo_botones.setBackground(new Color(238, 238, 238));
        cuerpo_botones.setPreferredSize(new Dimension(900, 40));
        cuerpo_botones.setSize(new Dimension(0, 50));
        panel.add(cuerpo_botones, BorderLayout.NORTH);

        // Botón INSERTAR
        JButton insertar = new JButton("INSERTAR");
        insertar.setIcon(new ImageIcon(VistaPrincipal.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Indent-White.png")));
        insertar.setForeground(new Color(255, 255, 255));
        insertar.setBackground(new Color(0, 102, 153));
        // Evento del botón INSERTAR
        insertar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Abre la nueva vista de inserción
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(VistaPrincipal.this);
                VistaInsertar vistaInsertar = new VistaInsertar(VistaPrincipal.this, controller);
                JDialog dialog = new JDialog(frame, "Insertar Nuevo Elemento", Dialog.ModalityType.APPLICATION_MODAL);
                dialog.getContentPane().add(vistaInsertar);
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });
        cuerpo_botones.setLayout(new MigLayout("", "[50px][100px][100px,grow][100px][100px][100px,grow][100px][100px][50px]", "[30px]"));
        cuerpo_botones.add(insertar, "cell 1 0,alignx center,aligny center");

        // Botón FILTRAR
        filtrarBtn = new JButton("FILTRAR");
        filtrarBtn.setIcon(new ImageIcon(VistaPrincipal.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Paste-White.png")));
        filtrarBtn.setForeground(new Color(255, 255, 255));
        filtrarBtn.setBackground(new Color(0, 102, 153));
        // Evento del boton FILTRAR
        filtrarBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (filtrarBtn.isEnabled()) {
	                // Abrir la nueva vista de inserción
	                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(VistaPrincipal.this);
	                VistaFiltrar vistaFiltrar = new VistaFiltrar(VistaPrincipal.this, controller);
	                JDialog dialog = new JDialog(frame, "Filtrar elementos", Dialog.ModalityType.APPLICATION_MODAL);
	                dialog.getContentPane().add(vistaFiltrar);
	                dialog.pack();
	                dialog.setLocationRelativeTo(frame);
	                dialog.setVisible(true);
	                
	                // Deshabilitar botón de filtrar y habilita botón de eliminar
	                filtrarBtn.setEnabled(false);
	                eliminarBtn.setEnabled(true);
	                
	                // Crear botón de ELIMINAR FILTRO
	                eliminarFiltroBtn = new JButton("Eliminar Filtro");
	                eliminarFiltroBtn.setForeground(new Color(255, 255, 255));
	                eliminarFiltroBtn.setBackground(new Color(52, 0, 111));
	                // Evento del botón ELIMINAR FILTRO
	                eliminarFiltroBtn.addMouseListener(new MouseAdapter() {
	                    @Override
	                    public void mouseClicked(MouseEvent e) {
	                    	// Habilitar botón de filtrar y deshabilitar botón de eliminar
	                        filtrarBtn.setEnabled(true);
	                        eliminarBtn.setEnabled(false);
	                        
	                        // Mostrar todos los registros de la base de datos
	                        agregarTablas(controller.findAll());
	                        
	                        // Limpiar el filtro de búsqueda
	                        filtroJson = null;
	                        
	                        // Eliminar el botón de ELIMINAR FILTRO
	                        cuerpo_botones.remove(eliminarFiltroBtn);
	                        cuerpo_botones.revalidate();
	                        cuerpo_botones.repaint();
	                    }
	                });
	                if (!filtrarBtn.isEnabled()) {
	                	cuerpo_botones.add(eliminarFiltroBtn, "cell 3 0,alignx center,aligny center"); // Ajusta las celdas según tu diseño
	                }
                
	                // Revalidar el panel de botones
	                cuerpo_botones.revalidate();
	                cuerpo_botones.repaint();
            	}
            }
        });
        cuerpo_botones.add(filtrarBtn, "cell 3 0,alignx center,aligny center");
        
        // Botón ELIMINAR
        eliminarBtn = new JButton("ELIMINAR");
        eliminarBtn.setEnabled(false);
        eliminarBtn.setIcon(new ImageIcon(VistaPrincipal.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Cut-White.png")));
        eliminarBtn.setForeground(Color.WHITE);
        eliminarBtn.setBackground(new Color(195, 72, 46));
        // Evento del botón ELIMINAR
        eliminarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Mostrar un cuadro de diálogo de confirmación
                int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar los datos del filtro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                
                // Verificar la opción seleccionada por el usuario
                if (opcion == JOptionPane.YES_OPTION) {
                	// Eliminar todos los registros que cumplen con el filtro dado
                    controller.deleteManyByCriteria(filtroJson);
                    agregarTablas(controller.findAll());
                    
                    // Habilitar el botón de filtrar y deshabilitar el botón de eliminar
                    eliminarBtn.setEnabled(false);
                    filtrarBtn.setEnabled(true);
                    
                    // Limpiar el filtro de búsqueda
                    filtroJson = null;
                    
                    // Eliminar el botón ELIMINAR FILTRO
                    cuerpo_botones.remove(eliminarFiltroBtn);
                    cuerpo_botones.revalidate();
                    cuerpo_botones.repaint();
                }
            }
        });
        cuerpo_botones.add(eliminarBtn, "cell 4 0");
        
        // Botón INSERTAR_BD
        JButton insertar_bd = new JButton("INSERTAR_BD");
        insertar_bd.setForeground(Color.WHITE);
        insertar_bd.setBackground(new Color(0, 102, 153));
        cuerpo_botones.add(insertar_bd, "cell 6 0");
        // Evento del botón INSERTAR_BD
        insertar_bd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Obtener la ruta del fichero e insertar los datos
                InputStream filePath = getClass().getClassLoader().getResourceAsStream("BD_Test_Samples.json");
                controller.insertJsonData(filePath);
                agregarTablas(controller.findAll());
                
                // Mensaje informativo
                JOptionPane.showMessageDialog(null, "Base de datos insertada correctamente.", "Mensaje informativo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Botón ELIMINAR_BD
        JButton eliminar_bd = new JButton("ELIMINAR_BD");
        eliminar_bd.setForeground(Color.WHITE);
        eliminar_bd.setBackground(new Color(195, 72, 46));
        cuerpo_botones.add(eliminar_bd, "cell 7 0");
        // Evento del botón ELIMINAR_BD
        eliminar_bd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Mostrar un cuadro de diálogo de confirmación
            	int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar todos los registros?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
               
            	// Verificar la opción seleccionada por el usuario
            	if (opcion == JOptionPane.YES_OPTION) {
            		// Eliminar todos los registros
                    controller.deleteAll();
                    agregarTablas(controller.findAll());
                }
            }
        });

        // Panel para las tablas
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

    /**
     * Método que establece el filtro JSON para la vista principal.
     * 
     * @param filtroJson Filtro JSON a establecer.
     */
    public void setFiltroJson(String filtroJson) {
    	this.filtroJson = filtroJson;
    }
    
    /**
     * Método para agregar tablas al panel de visualización.
     * 
     * @param documents Lista de documentos para crear tablas.
     */
    public void agregarTablas(List<Document> documents) {
    	// Limpiar el panel de visualización
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

        // Recorremos los documentos para mostrarlos en el panel
        for (Document doc : documents) {
        	// Creamos la tabla que contiene la información del documento
            JTable table = createTableFromDocument(doc);
            Object idObject = doc.values().iterator().next();
            String tableId = (idObject != null) ? idObject.toString() : ""; // Convertir a cadena
            table.setOpaque(false);
            table.setName(tableId);
            
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
            if (tableWidth <= remainingWidth) { // Si hay suficiente espacio horizontal
                gbc.gridwidth = 1;
                remainingWidth -= tableWidth + gbc.insets.left + gbc.insets.right;
            } else { // No hay suficiente espacio horizontal, mover a la siguiente fila
                gbc.gridwidth = numColumns - currentColumn;
                remainingWidth = screenWidth;
                currentColumn = 0;
                gbc.gridy += gridHeight;
            }
            
            // Crear un JPanel para envolver la tabla y otro para envolver los botones de la tabla
            JPanel panel_table = new JPanel(new BorderLayout());
            JPanel panel_botones = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout para alinear botones a la derecha
            panel_botones.setName(tableId);
            panel_botones.setOpaque(false); // Hacer el panel completamente transparente
            panel_botones.setBorder(null); // Eliminar cualquier borde
            panel_table.setOpaque(false); // Hacer el panel completamente transparente
            panel_table.setBorder(null); // Eliminar cualquier borde

            // Crea los botones y los agrega al panel_botones
            // Botón EDITAR TABLA
            JButton editTableBtn = new JButton("");
            editTableBtn.setPreferredSize(new Dimension(30, 30));
            editTableBtn.setForeground(new Color(255, 255, 255));
            editTableBtn.setBackground(new Color(52, 0, 111));
            editTableBtn.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Edicion.png")));
            editTableBtn.setName(tableId); // Establecer el ID como el nombre del botón
            // Evento del botón EDITAR TABLA
            editTableBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Obtener la tabla por su nombre
                    JTable table = getTableByName(editTableBtn.getName());

                    // Verificar si la tabla existe y realizar operaciones con ella
                    if (table != null) {
                        table.setEnabled(true); // Habilitar edición de la tabla
                        table.setBackground(Color.WHITE);
                        
                        // Creación del botón CONFIRMAR CAMBIOS
                        JButton confirmBtn = new JButton();
                        confirmBtn.setBackground(new Color(14, 161, 41));
                        confirmBtn.setPreferredSize(new Dimension(30, 30));
                        confirmBtn.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Confirmacion.png")));
                        confirmBtn.setName(editTableBtn.getName());
                        // Evento del botón CONFIRMAR CAMBIOS
                        confirmBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                            	// Mostrar mensaje de confirmación
                                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
                                
                                // Verificar respuesta del usuario
                                if (opcion == JOptionPane.YES_OPTION) {
                                    guardarCambiosEnBD(table);
                                }
                                
                                // Mostramos las tablas en la vista
                                agregarTablas(controller.findAll());
                                
                                // Eliminamos el botón de confirmación
                                JPanel panel_botones = getPanelBotonesByName(confirmBtn.getName());
                                panel_botones.remove(confirmBtn);
                                table.setBackground(new Color(238, 238, 238));
                                displayPanel.revalidate();
                                JTable table = getTableByName(confirmBtn.getName());
                                table.setEnabled(false);
                                table.clearSelection();
                            }
                        });
                        JPanel btnTablePanel = getPanelBotonesByName(confirmBtn.getName());
                        
                        // Agregar dos botones adicionales al panel_botones
                        // Botón ELIMINAR CAMPO
                        JButton deleteRowBtn = new JButton("");
                        deleteRowBtn.setBackground(new Color(201, 30, 18));
                        deleteRowBtn.setPreferredSize(new Dimension(30, 30));
                        deleteRowBtn.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Sustraer.png")));
                        // Evento del botón ELIMINAR CAMPO
                        deleteRowBtn.addActionListener(new ActionListener() {
                        	@Override
                        	public void actionPerformed(ActionEvent e) {
                        		// Obtener la tabla por su nombre
                        		JTable table = getTableByName(confirmBtn.getName());
                        		
                        		// Verificar si hay filas seleccionadas
                        		int[] selectedRows = table.getSelectedRows();
                        		if (selectedRows.length > 0) {
                        			// Eliminar las filas seleccionadas comenzando desde la última fila
                        			for (int i = selectedRows.length - 1; i >= 0; i--) {
                        				((DefaultTableModel) table.getModel()).removeRow(selectedRows[i]);
                        			}
                        		} else {
                        			// Si no hay filas seleccionadas, mostrar un mensaje al usuario
                        			JOptionPane.showMessageDialog(null, "Selecciona al menos una fila para eliminar.", "Filas no seleccionadas.", JOptionPane.WARNING_MESSAGE);
                        		}
                        	}
                        });
                        
                        // Botón INSERTAR CAMPO
                        JButton insertRowBtn = new JButton("");
                        insertRowBtn.setBackground(new Color(14, 161, 41));
                        insertRowBtn.setPreferredSize(new Dimension(30, 30));
                        insertRowBtn.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Agregar.png")));
                        // Evento del botón INSERTAR CAMPO
                        insertRowBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                            	// Obtener la tabla por su nombre
                        		JTable table = getTableByName(confirmBtn.getName());
                        		DefaultTableModel model = (DefaultTableModel) table.getModel();
                        		
                        		// Verificar si hay filas seleccionadas
                        		int[] selectedRows = table.getSelectedRows();
                        		if (selectedRows.length > 0) {
                        			// Insertar una nueva fila justo después de la última fila seleccionada
                        		    int lastSelectedRow = selectedRows[selectedRows.length - 1];
                        		    model.insertRow(lastSelectedRow + 1, new Object[]{"", ""});

                        		    // Seleccionar la nueva fila para edición
                        		    table.setRowSelectionInterval(lastSelectedRow + 1, lastSelectedRow + 1);
                        		} else {
                        			// Si no hay ninguna fila seleccionada, insertar la nueva fila al final
                                    int rowCount = model.getRowCount();
                                    model.addRow(new Object[]{"", ""});

                                    // Seleccionar la nueva fila para edición
                                    table.setRowSelectionInterval(rowCount, rowCount);
                        		}
                        	}
                        });
                        btnTablePanel.add(confirmBtn);
                        btnTablePanel.add(deleteRowBtn);
                        btnTablePanel.add(insertRowBtn);

                        // Revalidar el panel que muestra los componentes
                        displayPanel.revalidate();
                    } else {
                    	// Mostrar mensaje de error si no se encontró la tabla
                    	JOptionPane.showMessageDialog(null, "No se ha encontrado la tabla.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            
            // Botón ELIMINAR TABLA
            JButton deleteTableBtn = new JButton("");
            deleteTableBtn.setPreferredSize(new Dimension(30, 30));
            deleteTableBtn.setForeground(new Color(255, 255, 255));
            deleteTableBtn.setBackground(new Color(52, 0, 111));
            deleteTableBtn.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Papelera.png")));
            // Obtener el valor de la clave "id"
            deleteTableBtn.setName(tableId); // Establecer el ID como el nombre del botón
            // Evento del botón ELIMINAR TABLA
            deleteTableBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Obtener el panel que contiene el botón
                    JPanel panelContenedor = (JPanel) deleteTableBtn.getParent().getParent(); // El botón está contenido en el panel2, que a su vez está contenido en el panel1

                    // Mostrar un cuadro de diálogo de confirmación
                    int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar este elemento?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                    // Verificar la opción seleccionada por el usuario
                    if (opcion == JOptionPane.YES_OPTION) {
                        // Si el usuario hace clic en "SI", entonces elimina el elemento
                        controller.deleteOneById(deleteTableBtn.getName());

                        // Remueve el panel que contiene la tabla del displayPanel
                        displayPanel.remove(panelContenedor);

                        // Revalida y repinta el displayPanel para que se actualice la vista
                        displayPanel.revalidate();
                        displayPanel.repaint();
                        ajustarColumnasDisplayPanel();
                    } 
                }
            });
            panel_botones.add(editTableBtn);
            panel_botones.add(deleteTableBtn);

            // Panel contenedor de la tabla
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setOpaque(false); // Hacer el panel completamente transparente
            tablePanel.setBorder(null); // Eliminar cualquier borde
            tablePanel.add(table.getTableHeader(), BorderLayout.NORTH); // Agregar el encabezado de la tabla al panel
            tablePanel.add(table, BorderLayout.CENTER); // Agregar la tabla al panel
            panel_table.add(panel_botones, BorderLayout.NORTH); // Agregar el panel de botones al panel1
            panel_table.add(tablePanel, BorderLayout.CENTER); // Agregar la tabla al panel1

            // Agregar panel_table al displayPanel con las restricciones adecuadas
            displayPanel.add(panel_table, gbc);

            // Actualizar las coordenadas de gridbag para la próxima tabla si se inserta en la misma fila
            if (gbc.gridwidth == 1) {
                currentColumn++;
            }
        }
        
        // Ajustar las tablas en el display
        ajustarColumnasDisplayPanel();
        displayPanel.revalidate();
    }

    /**
     * Crea una tabla a partir de un documento.
     * 
     * @param document Documento que contiene los datos para la tabla.
     * @return Tabla creada con los datos.
     */
    private JTable createTableFromDocument(Document document) {
    	// Definir nombres de columnas
        String[] columnNames = {"CLAVE", "VALOR"};
        
        // Crear modelo de tabla personalizado
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 1L;

			// Sobreescribe el método isCellEditable para controlar la edición de las celdas
            @Override
            public boolean isCellEditable(int row, int column) {
                // Desactiva la edición para la primera fila (id)
                return !(row == 0 && column == 1 || row == 0 && column == 0);
            }
        };

        // Agregar datos del documento al modelo de tabla
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        // Crear la tabla con el modelo personalizado
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
    
    /**
     * Ajusta la distribución de las columnas en el panel de visualización.
     * Este método se utiliza para organizar las tablas en el panel de visualización de acuerdo con el ancho de la ventana.
     */
    private void ajustarColumnasDisplayPanel() {
    	// Obtener el ancho de la ventana
        int screenWidth = this.getWidth();
        int preferredTableWidth = 500; // Ancho preferido de la tabla
        
        // Calcular el número máximo de columnas basado en el ancho de la ventana 
        // y el ancho preferido de la tabla
        int numColumns = Math.max(screenWidth / preferredTableWidth, 1);

        // Configurar el layout del displayPanel como GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        displayPanel.setLayout(new GridBagLayout());
        
        // Establecer las restricciones para la distribución de las tablas
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Asegurar que las tablas se expandan horizontalmente
        
        // Establecer el margen entre las celdas
        gbc.insets = new Insets(5, 5, 5, 5); 
        
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

    /**
     * Obtiene una tabla del panel de visualización mediante su nombre.
     * 
     * @param name Nombre de la tabla que se busca.
     * @return Tabla correspondiente al nombre especificado, o null si no se encuentra.
     */
    private JTable getTableByName(String name) {
    	// Obtener todas las componentes (paneles) del displayPanel
        Component[] components = displayPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel1 = (JPanel) component;
                // Obtener todas las componentes (paneles) dentro del panel1
                Component[] panel1Components = panel1.getComponents();
                for (Component panel1Component : panel1Components) {
                    if (panel1Component instanceof JPanel) {
                        JPanel panel = (JPanel) panel1Component;
                        // Obtener todas las componentes (tablas) dentro del panel
                        Component[] panelComponents = panel.getComponents();
                        for (Component panelComponent : panelComponents) {
                        	// Verificar si la componente es una tabla y su nombre coincide con el especificado
                            if (panelComponent instanceof JTable && panelComponent.getName() != null && panelComponent.getName().equals(name)) {
                            	// Devolver la tabla encontrada
                            	return (JTable) panelComponent;
                            }
                        }
                    }
                }
            }
        }
        // Si no se encuentra la tabla, mostrar un mensaje de advertencia y devolver null
        JOptionPane.showMessageDialog(null, "No se ha encontrado la tabla.", "Error", JOptionPane.WARNING_MESSAGE);
        return null; // Si no se encuentra la tabla
    }

    /**
     * Obtiene un panel de botones del panel de visualización mediante su nombre.
     * 
     * @param name Nombre del panel de botones que se busca.
     * @return Panel de botones correspondiente al nombre especificado, o null si no se encuentra.
     */
    private JPanel getPanelBotonesByName(String name) {
    	// Obtener todas las componentes (paneles) del displayPanel
        Component[] components = displayPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel1 = (JPanel) component;
                // Obtener todas las componentes (paneles) dentro del panel1
                Component[] panel1Components = panel1.getComponents();
                for (Component panel1Component : panel1Components) {
                    if (panel1Component instanceof JPanel) {
                        JPanel panel2 = (JPanel) panel1Component;
                        // Verificar si el nombre del panel2 coincide con el especificado
                        if (panel2.getName() != null && panel2.getName().equals(name)) {
                        	// Devolver el panel de botones encontrado
                        	return panel2;
                        }
                    }
                }
            }
        }
        // Devolver null si no se encuentra el panel de botones
        return null; 
    }

    /**
     * Guarda los cambios realizados en una tabla en la base de datos.
     * 
     * @param table Tabla que contiene los datos a guardar.
     */
    private void guardarCambiosEnBD(JTable table) {
        // Obtener el ID del nombre de la tabla
        String id = table.getName();
        
        // Construir el JSON de actualización a partir de los datos de la tabla
        JsonStringBuilder jsonUpdateBuilder = new JsonStringBuilder();
        Object columnValueObject;

        // Contador para campos vacíos
        int emptyFieldsCount = 0; 

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        // Comenzar desde la segunda fila (la primera es el id)
        for (int i = 1; i < model.getRowCount(); i++) { 
            String clave = (String) model.getValueAt(i, 0);
            columnValueObject = model.getValueAt(i, 1);
            
            // Omitir la fila si la columna clave o valor están a null o cadena vacía
            if (clave == null || clave.isEmpty() || columnValueObject == null) {
            	// Incrementar el contador de campos vacíos
                emptyFieldsCount++; 
            } else {
                // Agregar clave y valor al JSON
                jsonUpdateBuilder.append(clave, columnValueObject);
            }
        }

        // Si se encontraron campos vacíos, preguntar al usuario si desea continuar
        if (emptyFieldsCount > 0) {
            int option = JOptionPane.showConfirmDialog(null, "Se han detectado " + emptyFieldsCount + " campo(s) vacío(s). ¿Desea eliminar la(s) fila(s) correspondiente(s)?",
                    "Campo(s) Vacío(s) Detectado(s)", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.NO_OPTION) {
                return; // No ejecutar la consulta si el usuario elige 'No'
            }
        }

        // Actualizar en la base de datos
        controller.replaceOneById(id, jsonUpdateBuilder.build());
    }
}