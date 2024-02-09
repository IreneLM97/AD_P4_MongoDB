package vistas;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import repositories.products.ProductsRepository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.json.JsonWriterSettings;


public class VistaFiltrar extends JPanel {
	private static final long serialVersionUID = 1L;
	private VistaPrincipal vistaPrincipal;
    private final MongoCollection<Document> collection;
    
    // Declaramos variables como atributos para poder acceder a ellas desde toda la clase
    private JPanel contenedor; 
    private JPanel panelCheckboxes;
    private JPanel cuerpo;
    
    @SuppressWarnings("unused")
	private int panelCounter = 1; // Contador para identificar los paneles agregados
   
    public VistaFiltrar(VistaPrincipal vistaPrincipal, MongoCollection<Document> collection) {
        this.vistaPrincipal = vistaPrincipal;
		this.collection = collection;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Crear contenedor
        contenedor = new JPanel();
        contenedor.setBorder(new LineBorder(new Color(238, 238, 238), 25));
        contenedor.setPreferredSize(new Dimension(700, 600));
        add(contenedor, BorderLayout.CENTER);
        contenedor.setLayout(new BorderLayout(0, 0));
        
        // Crear panel para los checkboxes y las etiquetas
        JPanel panelSuperior = new JPanel(new BorderLayout());
        contenedor.add(panelSuperior, BorderLayout.NORTH);
        
        // Crear JLabel encima de los checkboxes
        JLabel labelCampos = new JLabel("¿QUE CAMPOS QUIERES MOSTRAR?");
        labelCampos.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(labelCampos, BorderLayout.NORTH);
        
        // Crear panel para los checkboxes
        panelCheckboxes = new JPanel(new GridLayout(0, 4)); // Organiza los checkboxes en 4 columnas
        cargarClavesCheckBox(panelCheckboxes);
        panelSuperior.add(panelCheckboxes, BorderLayout.CENTER);
        
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[25px]"));
        panelSuperior.add(cabecera, BorderLayout.SOUTH);
        
        JLabel claveLabel = new JLabel("CLAVE");
        claveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(claveLabel, "cell 0 0,grow");
        
        JLabel operadorLabel = new JLabel("OPERADOR");
        operadorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(operadorLabel, "cell 1 0,grow");
        
        JLabel valorLabel = new JLabel("VALOR");
        valorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(valorLabel, "cell 2 0,grow");
        
        // Envuelve el panel cuerpo dentro de un JScrollPane para habilitar el desplazamiento vertical
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Ajusta la política de la barra de desplazamiento vertical
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contenedor.add(scrollPane, BorderLayout.CENTER);
        
        cuerpo = new JPanel();
        cuerpo.setBackground(new Color(0, 128, 128));
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS)); // Utiliza BoxLayout con orientación vertical
        scrollPane.setViewportView(cuerpo);
        
        JPanel panel_1 = new JPanel();
        cuerpo.add(panel_1);
        panel_1.setLayout(new MigLayout("", "[50px,grow][100px][50px,grow][100px][50px,grow][100px][50px,grow]", "[50px]"));
        
        JButton agregarFiltro = new JButton("Añadir Filtro");
        agregarFiltro.addActionListener(e -> {
            JPanel nuevoFiltro = crearNuevoFiltro();
            int componentCount = cuerpo.getComponentCount(); // Obtener la cantidad de componentes en el cuerpo
            int insertIndex = Math.max(0, componentCount - 1); // Obtener el índice de inserción antes del último panel
            cuerpo.add(nuevoFiltro, insertIndex); // Agregar el nuevo panel antes del último panel
            panelCounter++; // Incrementar el contador de paneles
            revalidate(); // Actualizar el diseño del panel para reflejar los cambios
            repaint(); // Volver a pintar el panel
        });

        // Configuración del resto de componentes
        panel_1.add(agregarFiltro, "cell 1 0");
        
        JButton eliminarFiltro = new JButton("Eliminar Filtro");
        eliminarFiltro.addActionListener(e -> {
            int componentCount = cuerpo.getComponentCount();
            if (componentCount > 1) { // Asegurarse de que haya al menos un panel para eliminar
                int indexToRemove = Math.max(0, componentCount - 2); // Obtener el índice del último panel
                cuerpo.remove(indexToRemove); // Eliminar el último panel
                revalidate(); // Actualizar el diseño del panel para reflejar los cambios
                repaint(); // Volver a pintar el panel
            } else {
                JOptionPane.showMessageDialog(null, "No hay filtros para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel_1.add(eliminarFiltro, "cell 3 0");
        
        JButton filtrarBtn = new JButton("Filtrar");
        filtrarBtn.addActionListener(e -> {
        	obtenerCamposSeleccionados(); // Recoger los campos seleccionados
            ProductsRepository pr = new ProductsRepository();
            String resultados = obtenerJsonFiltros().toString(); 
            
            // Verificar si resultados está vacío
            if (resultados.equals("{}")) {
                // Si no hay filtros, llamar a findAllWithProjections
                List<Document> allDocuments = pr.findAllWithProjections(camposSeleccionados, collection);
                vistaPrincipal.agregarTablas(allDocuments);
            } else {
                // Si hay filtros, aplicar proyecciones utilizando findByFieldsWithProjection
                List<Document> filteredDocuments = pr.findByFieldsWithProjection(resultados, camposSeleccionados, collection);
                vistaPrincipal.agregarTablas(filteredDocuments);
            }

            // Aplicar proyecciones utilizando el método findByFieldsWithProjection
            List<Document> filteredDocuments = pr.findByFieldsWithProjection(resultados, camposSeleccionados, collection);
            vistaPrincipal.agregarTablas(filteredDocuments);
            vistaPrincipal.setFiltroJson(resultados);
			
			((JButton)e.getSource()).getRootPane().getParent().setVisible(false);
        });
        panel_1.add(filtrarBtn, "cell 5 0");
    }
    
    private void cargarClavesCheckBox(JPanel panelCheckboxes) {
        List<String> campos = obtenerClavesDesdeBaseDeDatos();

        for (String campo : campos) {
            JCheckBox checkBox = new JCheckBox(campo);
            panelCheckboxes.add(checkBox);
        }
    }

    private void cargarClavesComboBox(JComboBox<String> comboBox) {
        // Lógica para obtener las claves de la base de datos y cargarlas en el JComboBox
        List<String> claves = obtenerClavesDesdeBaseDeDatos();
        for (String clave : claves) {
            comboBox.addItem(clave);
        }
    }
    
    // Lista para almacenar los campos seleccionados
    private List<String> camposSeleccionados = new ArrayList<>();

    // Método para recoger los campos seleccionados de los checkboxes
    private void obtenerCamposSeleccionados() {
        camposSeleccionados.clear(); // Limpiar la lista para evitar duplicados
        for (Component component : panelCheckboxes.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    camposSeleccionados.add(checkBox.getText());
                }
            }
        }
    }

    private List<String> obtenerClavesDesdeBaseDeDatos() {
        ProductsRepository pr = new ProductsRepository();
        List<Document> documentos = pr.findAll(collection); 
        Set<String> clavesUnicas = new HashSet<>();
        for (Document documento : documentos) {
            clavesUnicas.addAll(documento.keySet());
        }
        List<String> clavesOrdenadas = new ArrayList<>(clavesUnicas);
        Collections.sort(clavesOrdenadas); // Ordenar alfabéticamente de A a Z
        return clavesOrdenadas;
    }

    // Método para cerrar esta vista
    @SuppressWarnings("unused")
	private void cerrar() {
    	Window window = SwingUtilities.getWindowAncestor(this);
	    window.dispose();
    }

    // Método para agregar un nuevo componente al panel
    public void agregarComponente(Component componente) {
        contenedor.add(componente);
        revalidate(); // Actualiza el diseño del panel para reflejar los cambios
        repaint(); // Vuelve a pintar el panel
    }
    
    private JPanel crearNuevoFiltro() {
        JPanel nuevoPanel = new JPanel();
        nuevoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        nuevoPanel.setName("filtroPanel"); // Etiqueta el panel como "filtroPanel"
        nuevoPanel.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[50px]"));

        JComboBox<String> nuevoComboBox = new JComboBox<>();
        cargarClavesComboBox(nuevoComboBox);
        nuevoPanel.add(nuevoComboBox, "cell 0 0,growx");

        JComboBox<String> nuevoComboBox_1 = new JComboBox<String>();
        nuevoComboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"==", "!=", "<=", ">=", "<", ">"}));
        nuevoPanel.add(nuevoComboBox_1, "cell 1 0,growx");

        JTextField nuevoTextField = new JTextField();
        nuevoPanel.add(nuevoTextField, "cell 2 0,growx");
        nuevoTextField.setColumns(10);

        return nuevoPanel;
    }

    
    private String obtenerJsonFiltros() {
    	// Documento con los filtros de búsqueda
        Document filtro = new Document();
        
        // Variable para verificar si se han agregado filtros
        boolean hayFiltros = false; 

        // Iterar sobre los paneles dentro del cuerpo
        for (Component component : cuerpo.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getName() != null && panel.getName().equals("filtroPanel")) {
                	hayFiltros = true;
                    @SuppressWarnings("unchecked")
					JComboBox<String> claveComboBox = (JComboBox<String>) panel.getComponent(0);
                    String clave = (String) claveComboBox.getSelectedItem();

                    @SuppressWarnings("unchecked")
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

                    JTextField valorTextField = (JTextField) panel.getComponent(2);
                    String valor = valorTextField.getText();

                    // Agregar la condición al filtro
                    filtro.put(clave, new Document(operador, obtenerValorConTipo(valor)));
                }
            }
        }
        
        // Si no se han agregado filtros, devolver un JSON vacío
        if (!hayFiltros) {
            return "{}";
        }
        
        // Convertir el documento filtro a una cadena JSON válida
        return filtro.toJson(JsonWriterSettings.builder().indent(true).build());
    }

    private Object obtenerValorConTipo(String value) {
    	try {
        	return Integer.parseInt(value.toString());
        }catch(Exception e1){
        	try {
        		return Double.parseDouble(value.toString());
        	}catch(Exception e2) {
        		return value.toString();
        	}
        }
    }
}
