package vistas;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import repositories.products.ProductsRepository;
import utils.JsonStringBuilder;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import org.bson.json.JsonWriterSettings;


public class VistaInsertar extends JPanel {
	private static final long serialVersionUID = 1L;
	private VistaPrincipal vistaPrincipal;
    private final MongoCollection<Document> collection;
    
    // Definimos el panel como atributo para poder acceder a él desde otros métodos
    private JPanel contenedor; 
    private JTextField textFieldValor;
    private JPanel cuerpo;
    private JTextField textFieldClave;


    public VistaInsertar(VistaPrincipal vistaPrincipal, MongoCollection<Document> collection) {
        this.vistaPrincipal = vistaPrincipal;
		this.collection = collection;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        contenedor = new JPanel();
        contenedor.setBorder(new LineBorder(new Color(238, 238, 238), 25));
        contenedor.setBackground(new Color(0, 128, 128));
        contenedor.setPreferredSize(new Dimension(700, 600));
        add(contenedor, BorderLayout.CENTER);
        contenedor.setLayout(new BorderLayout(0, 0));
        
        JPanel cabecera = new JPanel();
        contenedor.add(cabecera, BorderLayout.NORTH);
        cabecera.setLayout(new MigLayout("", "[183px,grow][100px][183px,grow]", "[25px]"));
        
        JLabel claveLabel = new JLabel("CLAVE");
        claveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(claveLabel, "cell 0 0,grow");
        
        JLabel valorLabel = new JLabel("VALOR");
        valorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(valorLabel, "cell 2 0,grow");
        
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
        panel.setName("campoPanel"); // Etiqueta el panel como "campoPanel"
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        cuerpo.add(panel);
        panel.setLayout(new MigLayout("", "[183px,grow][100px][183px,grow]", "[50px]"));
        
        textFieldClave = new JTextField();
        panel.add(textFieldClave, "cell 0 0,growx");
        textFieldClave.setColumns(10);
        
        textFieldValor = new JTextField();
        panel.add(textFieldValor, "cell 2 0,growx");
        textFieldValor.setColumns(10);
        
        JPanel panel_botones = new JPanel();
        cuerpo.add(panel_botones);
        panel_botones.setLayout(new MigLayout("", "[50px,grow][100px][50px,grow][100px][50px,grow][100px][50px,grow]", "[50px]"));
        
        JButton agregarCampo = new JButton("Añadir Campo");
        agregarCampo.addActionListener(e -> {
            JPanel nuevoPanel = crearNuevoPanel();
            int componentCount = cuerpo.getComponentCount(); // Obtener la cantidad de componentes en el cuerpo
            int insertIndex = Math.max(0, componentCount - 1); // Obtener el índice de inserción antes del último panel
            cuerpo.add(nuevoPanel, insertIndex); // Agregar el nuevo panel antes del último panel
            revalidate(); // Actualizar el diseño del panel para reflejar los cambios
            repaint(); // Volver a pintar el panel
        });

        // Configuración del resto de componentes
        panel_botones.add(agregarCampo, "cell 1 0");
        
        JButton eliminarCampo = new JButton("Eliminar Campo");
        eliminarCampo.addActionListener(e -> {
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

        panel_botones.add(eliminarCampo, "cell 3 0");
        
        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> {
        	ProductsRepository pr = new ProductsRepository();
        	guardar();
//        	String resultados = guardar().toString();
//            System.out.println(resultados.toString());
//            System.out.println(pr.findByFields(resultados, collection));
//            vistaPrincipalAux.agregarTablas(pr.findByFields(resultados, collection));
        });
        panel_botones.add(guardar, "cell 5 0");
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
        nuevoPanel.setName("fieldPanel"); // Etiqueta el panel como "fieldPanel"
        nuevoPanel.setLayout(new MigLayout("", "[183px,grow][100px][183px,grow]", "[50px]"));

        JTextField nuevoCampoClave = new JTextField();
        nuevoPanel.add(nuevoCampoClave, "cell 0 0,growx");
        nuevoCampoClave.setColumns(10);
        
        JTextField nuevoCampoValor = new JTextField();
        nuevoPanel.add(nuevoCampoValor, "cell 2 0,growx");
        nuevoCampoValor.setColumns(10);

        return nuevoPanel;
    }

    
    private void guardar() {
        // Construir el JSON del registro
        JsonStringBuilder jsonInsertBuilder = new JsonStringBuilder();
        
        int emptyFieldsCount = 0; // Contador para campos vacíos

        // Iterar sobre los paneles dentro del cuerpo
        for (Component component : cuerpo.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getName() != null && panel.getName().equals("campoPanel")) {
                	JTextField claveTextField = (JTextField) panel.getComponent(0);
                    String clave = claveTextField.getText();

                    JTextField valorTextField = (JTextField) panel.getComponent(1);
                    Object valor = valorTextField.getText();
                    
                    // Omitir la fila si la columna clave o valor están a null o cadena vacía
                    if (clave == null || clave.isEmpty() || valor == null) {
                        emptyFieldsCount++; // Incrementar el contador de campos vacíos
                    } else {
                        // Agregar clave y valor al JSON
                    	jsonInsertBuilder.append(clave, valor);
                    }
                    
                }
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
        
        // Insertar el registro en la base de datos
        ProductsRepository pr = new ProductsRepository();
        pr.insertOne(jsonInsertBuilder.build(), collection);
        
        // Recargar los resultados en la base de datos
		List<Document> results = pr.findAll(collection);
		this.vistaPrincipal.agregarTablas(results);
    }
}
