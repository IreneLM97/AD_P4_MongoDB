package vistas;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

import controller.MongoController;

import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.json.JsonWriterSettings;

/**
 * Clase que representa la vista para filtrar los datos de la base de datos MongoDB.
 * Permite al usuario seleccionar los campos a mostrar y aplicar filtros sobre los datos.
 */
public class VistaFiltrar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Referencia a la vista principal
	private VistaPrincipal vistaPrincipal;
	// Controlador de MongoDB
    private MongoController controller;
    
    // Declaramos variables como atributos para poder acceder a ellas desde toda la clase
    private JPanel container; 
    private JPanel cuerpo_filtros;
    private JPanel panelCheckboxes;
    
    // Lista para almacenar las claves seleccionadas de los JCheckBox
    private List<String> clavesSeleccionadas = new ArrayList<>();
       
    /**
     * Constructor de la clase VistaFiltrar.
     * 
     * @param vistaPrincipal Referencia a la vista principal.
     * @param controller Controlador de MongoDB.
     */
    public VistaFiltrar(VistaPrincipal vistaPrincipal, MongoController controller) {
        this.vistaPrincipal = vistaPrincipal;
		this.controller = controller;
        initializeUI();
    }

    /**
     * Inicializa la interfaz de usuario de la vista filtrar.
     */
    private void initializeUI() {
    	// Configuración del panel principal
        setLayout(new BorderLayout());
        
        // Contenedor principal de la ventana
        container = new JPanel();
        container.setBorder(new LineBorder(new Color(238, 238, 238), 25));
        container.setPreferredSize(new Dimension(700, 600));
        add(container, BorderLayout.CENTER);
        container.setLayout(new BorderLayout(0, 0));
        
        // Panel para los checkboxes y las etiquetas
        JPanel cuerpo_superior = new JPanel(new BorderLayout());
        container.add(cuerpo_superior, BorderLayout.NORTH);
        
        // Crear JLabel encima de los checkboxes
        JLabel labelCampos = new JLabel("¿QUE CAMPOS QUIERES MOSTRAR?");
        labelCampos.setHorizontalAlignment(SwingConstants.CENTER);
        cuerpo_superior.add(labelCampos, BorderLayout.NORTH);
        
        // Panel para los checkboxes
        panelCheckboxes = new JPanel(new GridLayout(0, 4)); // Organiza los checkboxes en 4 columnas
        cargarClavesCheckBox(panelCheckboxes);
        cuerpo_superior.add(panelCheckboxes, BorderLayout.CENTER);
        
        // Panel para la cabecera con las label
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[25px]"));
        cuerpo_superior.add(cabecera, BorderLayout.SOUTH);
        
        // Label CLAVE
        JLabel claveLabel = new JLabel("CLAVE");
        claveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(claveLabel, "cell 0 0,grow");
        
        // Label OPERADOR
        JLabel operadorLabel = new JLabel("OPERADOR");
        operadorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(operadorLabel, "cell 1 0,grow");
        
        // Label VALOR
        JLabel valorLabel = new JLabel("VALOR");
        valorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(valorLabel, "cell 2 0,grow");
        
        // Envuelve el panel cuerpo dentro de un JScrollPane para habilitar el desplazamiento vertical
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        container.add(scrollPane, BorderLayout.CENTER);
        
        // Panel para los filtros
        cuerpo_filtros = new JPanel();
        cuerpo_filtros.setBackground(new Color(0, 128, 128));
        cuerpo_filtros.setLayout(new BoxLayout(cuerpo_filtros, BoxLayout.Y_AXIS)); // Utiliza BoxLayout con orientación vertical
        scrollPane.setViewportView(cuerpo_filtros);
        
        // Panel de los botones
        JPanel panel_botones = new JPanel();
        cuerpo_filtros.add(panel_botones);
        panel_botones.setLayout(new MigLayout("", "[50px,grow][100px][50px,grow][100px][50px,grow][100px][50px,grow]", "[50px]"));
        
        // Botón INSERTAR FILTRO
        JButton insertFilterBtn = new JButton("Añadir Filtro");
        // Evento del botón INSERTAR FILTRO
        insertFilterBtn.addActionListener(e -> {
        	// Creamos el nuevo filtro
            JPanel nuevoFiltro = crearNuevoFiltro();
            
            // Obtener la cantidad de componentes en el cuerpo
            int componentCount = cuerpo_filtros.getComponentCount(); 
            // Obtener el índice de inserción antes del último panel
            int insertIndex = Math.max(0, componentCount - 1); 
            
            // Agregar el nuevo panel antes del último panel
            cuerpo_filtros.add(nuevoFiltro, insertIndex); 
            
            // Actualizar el diseño del panel y volver a pintar
            revalidate(); 
            repaint(); 
        });
        panel_botones.add(insertFilterBtn, "cell 1 0");
        
        // Botón ELIMINAR FILTRO
        JButton deleteFilterBtn = new JButton("Eliminar Filtro");
        // Evento del botón ELIMINAR FILTRO
        deleteFilterBtn.addActionListener(e -> {
        	// Obtener la cantidad de componentes en el cuerpo
            int componentCount = cuerpo_filtros.getComponentCount();
            
            // Asegurarse de que haya al menos un panel para eliminar
            if (componentCount > 1) { 
            	// Obtener el índice del último panel
                int indexToRemove = Math.max(0, componentCount - 2); 
                // Eliminar el último panel
                cuerpo_filtros.remove(indexToRemove); 
                
                // Actualizar el diseño del panel y volver a pintar
                revalidate(); 
                repaint(); 

            } else {
            	// Mostrar mensaje si no hay filtros para eliminar
                JOptionPane.showMessageDialog(null, "No hay filtros para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel_botones.add(deleteFilterBtn, "cell 3 0");
        
        // Botón FILTRAR
        JButton filtrarBtn = new JButton("Filtrar");
        // Evento del botón FILTRAR
        filtrarBtn.addActionListener(e -> {
        	// Recoger los campos seleccionados
        	obtenerCamposSeleccionados(); 
            String resultados = obtenerJsonFiltros().toString(); 
            
            // Verificar hay filtros
            if (resultados.equals("{}")) {
                // Si no hay filtros, llamar a findAllWithProjections
                List<Document> allDocuments = controller.findAllWithProjections(clavesSeleccionadas);
                vistaPrincipal.agregarTablas(allDocuments);
            } else {
                // Si hay filtros, aplicar proyecciones utilizando findByFieldsWithProjection
                List<Document> filteredDocuments = controller.findByFieldsWithProjection(resultados, clavesSeleccionadas);
                vistaPrincipal.agregarTablas(filteredDocuments);
            }

            // Actualizar el valor del filtro JSON
            vistaPrincipal.setFiltroJson(resultados);
			
            // Cerramos la ventana
            cerrar();
        });
        panel_botones.add(filtrarBtn, "cell 5 0");
    }
    
    /**
     * Método para agregar un nuevo componente al contenedor.
     * @param component El componente que se va a agregar.
     */
    public void agregarComponente(Component component) {
    	// Agregar el componente al contenedor
        container.add(component);
        
        // Actualizar el diseño del panel y volver a pintar
        revalidate(); 
        repaint(); 
    }
    
    /**
     * Método que crea un nuevo panel para agregar un filtro.
     * @return Panel creado para agregar un nuevo filtro.
     */
    private JPanel crearNuevoFiltro() {
    	// Crear el nuevo panel
        JPanel nuevoPanel = new JPanel();
        nuevoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        nuevoPanel.setName("filtroPanel"); // Agregamos nombre filtroPanel al panel
        nuevoPanel.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[50px]"));

        // Crear el JComboBox que contiene las claves de la base de datos
        JComboBox<String> comboBoxClaves = new JComboBox<>();
        cargarClavesComboBox(comboBoxClaves);
        nuevoPanel.add(comboBoxClaves, "cell 0 0,growx");

        // Crear el JComboBox que contiene los operadores de filtrado
        JComboBox<String> comboBoxOperadores = new JComboBox<String>();
        comboBoxOperadores.setModel(new DefaultComboBoxModel<String>(new String[] {"==", "!=", "<=", ">=", "<", ">"}));
        nuevoPanel.add(comboBoxOperadores, "cell 1 0,growx");

        // Crear campo de texto para introducir valor de filtrado
        JTextField nuevoTextField = new JTextField();
        nuevoPanel.add(nuevoTextField, "cell 2 0,growx");
        nuevoTextField.setColumns(10);

        return nuevoPanel;
    }

    /**
     * Método que obtiene los filtros seleccionados y los devuelve en formato JSON.
     * 
     * @return Representación JSON de los filtros seleccionados.
     */
    @SuppressWarnings("unchecked")
	private String obtenerJsonFiltros() {
    	// Documento con los filtros de búsqueda
        Document filtrosDocument = new Document();
        
        // Variable para verificar si se han agregado filtros
        boolean hayFiltros = false; 

        // Recorrer los componentes del cuerpo filtros
        for (Component component : cuerpo_filtros.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                // Verificar si el panel es un filtroPanel
                if (panel.getName() != null && panel.getName().equals("filtroPanel")) {
                	hayFiltros = true; 
                	
                	// Obtener la clave del primer JComboBox
					JComboBox<String> claveComboBox = (JComboBox<String>) panel.getComponent(0);
                    String clave = (String) claveComboBox.getSelectedItem();

                    // Obtener el operador del segundo JComboBox
					JComboBox<String> operadorComboBox = (JComboBox<String>) panel.getComponent(1);
                    String operador = (String) operadorComboBox.getSelectedItem();

                    // Transformar operador según la sintaxis de MongoDB
                    switch (operador) {
	                    case "==":
	                        operador = "$eq";
	                        break;
                        case "!=":
                            operador = "$ne";
                            break;
                        case "<=":
                            operador = "$lte";
                            break;
                        case ">=":
                            operador = "$gte";
                            break;
                        case "<":
                            operador = "$lt";
                            break;
                        case ">":
                            operador = "$gt";
                            break;
                        default:
                            // Operador no reconocido
                            break;
                    }

                    // Obtener el valor del campo de texto
                    JTextField valorTextField = (JTextField) panel.getComponent(2);
                    String valor = valorTextField.getText();

                    // Agregar la condición al documento de filtros
                    filtrosDocument.put(clave, new Document(operador, obtenerValorConTipo(valor)));
                }
            }
        }
        
        // Si no se han agregado filtros, devolver un JSON vacío
        if (!hayFiltros) {
            return "{}";
        }
        
        // Convertir el documento filtro a una cadena JSON válida
        return filtrosDocument.toJson(JsonWriterSettings.builder().indent(true).build());
    }

    /**
     * Método que intenta convertir el valor proporcionado en un tipo específico (Integer, Double o String) y lo devuelve.
     * 
     * @param value Valor a convertir.
     * @return Valor convertido en el tipo correspondiente (Integer, Double o String).
     */
    private Object obtenerValorConTipo(String value) {
    	try {
        	return Integer.parseInt(value.toString()); // Intentar converir a Integer
        }catch(Exception e1){
        	try {
        		return Double.parseDouble(value.toString()); // Intentar convertir a Double
        	}catch(Exception e2) {
        		return value.toString(); // Si no se puede convertir, devolver el valor como String
        	}
        }
    }
    
    /**
     * Método para obtener las claves únicas ordenadas de los documentos en la base de datos.
     * 
     * @return Lista de claves únicas ordenadas alfabéticamente.
     */
    private List<String> obtenerClavesDesdeBaseDeDatos() {
    	// Obtener los documentos de la base de datos
        List<Document> documents = controller.findAll(); 
        
        // Crear el conjunto de claves únicas
        Set<String> clavesUnicas = new HashSet<>();
        for (Document document : documents) {
            clavesUnicas.addAll(document.keySet());
        }
        
        // Ordenar alfabéticamente el conjunto de claves únicas
        List<String> clavesOrdenadas = new ArrayList<>(clavesUnicas);
        Collections.sort(clavesOrdenadas); 
        return clavesOrdenadas;
    }
    
    /**
     * Método para cargar las claves obtenidas desde la base de datos como JCheckBox.
     * 
     * @param panelCheckboxes JPanel donde se agregarán los JCheckBox.
     */
    private void cargarClavesCheckBox(JPanel panelCheckboxes) {
    	// Obtenemos las claves de la base de datos
        List<String> claves = obtenerClavesDesdeBaseDeDatos();

        // Mostramos las claves como CheckBox
        for (String clave : claves) {
            JCheckBox checkBox = new JCheckBox(clave);
            panelCheckboxes.add(checkBox);
        }
    }

    /**
     * Método para cargar las claves obtenidas desde la base de datos en un JComboBox.
     * 
     * @param comboBox JComboBox donde se cargarán las claves.
     */
    private void cargarClavesComboBox(JComboBox<String> comboBox) {
    	// Obtenemos las claves de la base de datos
        List<String> claves = obtenerClavesDesdeBaseDeDatos();
        
        // Mostramos las claves en el ComboBox
        for (String clave : claves) {
            comboBox.addItem(clave);
        }
    }
    
    /**
     * Método para obtener las claves seleccionadas a partir de los JCheckBox en el panel de checkboxes.
     */
    private void obtenerCamposSeleccionados() {
    	// Limpiar la lista para evitar duplicados
        clavesSeleccionadas.clear(); 
        
        // Recorrer el panel de checkbox y añadir a la lista los seleccionados
        for (Component component : panelCheckboxes.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    clavesSeleccionadas.add(checkBox.getText());
                }
            }
        }
    }
    
    /**
     * Método para cerrar la ventana actual.
     */
	private void cerrar() {
		// Obtener la ventana principal que contiene este panel
		Window window = SwingUtilities.getWindowAncestor(this);
		// Cerrar la ventana
	    window.dispose();
    }
}
