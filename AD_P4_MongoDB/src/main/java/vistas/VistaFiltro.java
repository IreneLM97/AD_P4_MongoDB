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


public class VistaFiltro extends JPanel {
	private static final long serialVersionUID = 1L;
	private VistaPrincipalAux vistaPrincipalAux;
    private final MongoCollection<Document> collection;
    private JPanel contenedor; // Definimos el panel como atributo para poder acceder a él desde otros métodos
    private JComboBox<String> comboBox;
    private JTextField textField;
    @SuppressWarnings("unused")
	private int panelCounter = 1; // Contador para identificar los paneles agregados
    private JPanel cuerpo;


    public VistaFiltro(VistaPrincipalAux vistaPrincipalAux, MongoCollection<Document> collection) {
        this.vistaPrincipalAux = vistaPrincipalAux;
		this.collection = collection;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        contenedor = new JPanel();
        contenedor.setBorder(new LineBorder(new Color(238, 238, 238), 25));
        contenedor.setBackground(new Color(0, 128, 128));
        contenedor.setPreferredSize(new Dimension(600, 600));
        add(contenedor, BorderLayout.CENTER);
        contenedor.setLayout(new BorderLayout(0, 0));
        
        JPanel Cabecera = new JPanel();
        contenedor.add(Cabecera, BorderLayout.NORTH);
        Cabecera.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[25px]"));
        
        JLabel calveLabel = new JLabel("CLAVE");
        calveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Cabecera.add(calveLabel, "cell 0 0,grow");
        
        JLabel operadorLabel = new JLabel("OPERADOR");
        operadorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Cabecera.add(operadorLabel, "cell 1 0,grow");
        
        JLabel valorLabel = new JLabel("VALOR");
        valorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Cabecera.add(valorLabel, "cell 2 0,grow");
        
//        JPanel cuerpo = new JPanel();
//        cuerpo.setBackground(new Color(0, 128, 128));
//        contenedor.add(cuerpo, BorderLayout.CENTER);
//        cuerpo.setLayout(new GridLayout(7, 1, 0, 0));
        
     // Envuelve el panel cuerpo dentro de un JScrollPane para habilitar el desplazamiento vertical
        JScrollPane scrollPane = new JScrollPane();
        contenedor.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Ajusta la política de la barra de desplazamiento vertical
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        cuerpo = new JPanel();
        cuerpo.setBackground(new Color(0, 128, 128));
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS)); // Utiliza BoxLayout con orientación vertical
        scrollPane.setViewportView(cuerpo); // Establece el cuerpo como vista del scroll pane

        
        JPanel panel = new JPanel();
        panel.setName("filtroPanel"); // Etiqueta el panel como "filtroPanel"
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        cuerpo.add(panel);
        panel.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[50px]"));
        
        // Aquí creamos un JComboBox estándar
        comboBox = new JComboBox<>();
        cargarClaves(comboBox);
        panel.add(comboBox, "cell 0 0,growx");
        
        JComboBox<String> comboBox_1 = new JComboBox<String>();
        comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"==", "!=", "<=", ">=", "<", ">"}));
        panel.add(comboBox_1, "cell 1 0,growx");
        
        textField = new JTextField();
        panel.add(textField, "cell 2 0,growx");
        textField.setColumns(10);
        
        JPanel panel_1 = new JPanel();
        cuerpo.add(panel_1);
        panel_1.setLayout(new MigLayout("", "[50px,grow][100px][50px,grow][100px][50px,grow][100px][50px,grow]", "[50px]"));
        
        JButton AñadirFiltro = new JButton("Añadir Filtro");
        AñadirFiltro.addActionListener(e -> {
            JPanel nuevoPanel = crearNuevoPanel();
            int componentCount = cuerpo.getComponentCount(); // Obtener la cantidad de componentes en el cuerpo
            int insertIndex = Math.max(0, componentCount - 1); // Obtener el índice de inserción antes del último panel
            cuerpo.add(nuevoPanel, insertIndex); // Agregar el nuevo panel antes del último panel
            panelCounter++; // Incrementar el contador de paneles
            revalidate(); // Actualizar el diseño del panel para reflejar los cambios
            repaint(); // Volver a pintar el panel
        });

        // Configuración del resto de componentes
        panel_1.add(AñadirFiltro, "cell 1 0");
        
        JButton EliminarFiltro = new JButton("Eliminar Filtro");
        EliminarFiltro.addActionListener(e -> {
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

        panel_1.add(EliminarFiltro, "cell 3 0");
        
        JButton Filtrar = new JButton("Filtrar");
        Filtrar.addActionListener(e -> {
        	ProductsRepository pr = new ProductsRepository();
        	String resultados = filtrar().toString();
            System.out.println(resultados.toString());
            System.out.println(pr.findByFields(resultados, collection));
            vistaPrincipalAux.agregarTablas(pr.findByFields(resultados, collection));
			
			((JButton)e.getSource()).getRootPane().getParent().setVisible(false);
        });
        panel_1.add(Filtrar, "cell 5 0");
    }

    private void cargarClaves(JComboBox<String> comboBox) {
        // Lógica para obtener las claves de la base de datos y cargarlas en el JComboBox
        List<String> claves = obtenerClavesDesdeBaseDeDatos();
        for (String clave : claves) {
            comboBox.addItem(clave);
        }
    }

    private List<String> obtenerClavesDesdeBaseDeDatos() {
        ProductsRepository pr = new ProductsRepository();
        List<Document> documentos = pr.findAll(collection); // Esto supone que "collection" es tu colección MongoDB
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
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }

    // Método para agregar un nuevo componente al panel
    public void agregarComponente(Component componente) {
        contenedor.add(componente);
        revalidate(); // Actualiza el diseño del panel para reflejar los cambios
        repaint(); // Vuelve a pintar el panel
    }
    
    private JPanel crearNuevoPanel() {
        JPanel nuevoPanel = new JPanel();
        nuevoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        nuevoPanel.setName("filtroPanel"); // Etiqueta el panel como "filtroPanel"
        nuevoPanel.setLayout(new MigLayout("", "[183px,grow][100px,grow][183px,grow]", "[50px]"));

        JComboBox<String> nuevoComboBox = new JComboBox<>();
        cargarClaves(nuevoComboBox);
        nuevoPanel.add(nuevoComboBox, "cell 0 0,growx");

        JComboBox<String> nuevoComboBox_1 = new JComboBox<String>();
        nuevoComboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"==", "!=", "<=", ">=", "<", ">"}));
        nuevoPanel.add(nuevoComboBox_1, "cell 1 0,growx");

        JTextField nuevoTextField = new JTextField();
        nuevoPanel.add(nuevoTextField, "cell 2 0,growx");
        nuevoTextField.setColumns(10);

        return nuevoPanel;
    }

    
    private String filtrar() {
        Document filtro = new Document();

        // Iterar sobre los paneles dentro del cuerpo
        for (Component component : cuerpo.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getName() != null && panel.getName().equals("filtroPanel")) {
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
