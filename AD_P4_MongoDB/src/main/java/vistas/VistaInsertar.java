package vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import repositories.products.ProductsRepository;
import utils.JsonStringBuilder;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class VistaInsertar extends JPanel {
	private static final long serialVersionUID = 1L;
	private VistaPrincipal vistaPrincipal;
    private final MongoCollection<Document> collection;
    
    // Declaramos variables como atributos para poder acceder a ellas desde toda la clase
    private JPanel displayPanel;
    private JTable table;

    public VistaInsertar(VistaPrincipal vistaPrincipal, MongoCollection<Document> collection) {
        this.vistaPrincipal = vistaPrincipal;
		this.collection = collection;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Contenedor principal
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(700, 600));
        add(container, BorderLayout.CENTER);
        container.setLayout(new MigLayout("", "[50px][600px,grow][50px]", "[50px][500px,grow][50px]"));

        // Panel para la tabla y los campos clave-valor
        JPanel panel = new JPanel();
        panel.setBackground(new Color(238, 238, 238));
        container.add(panel, "cell 1 1,grow");
        panel.setLayout(new MigLayout("", "[800px,grow]", "[50px][400px,grow][50px]"));

        // Cuerpo para los botones de añadir y eliminar campo
        JPanel cuerpo_botones = new JPanel();
        cuerpo_botones.setPreferredSize(new Dimension(700, 40));
        cuerpo_botones.setBackground(new Color(238, 238, 238));
        panel.add(cuerpo_botones, "cell 0 0,alignx center,aligny center");
        cuerpo_botones.setLayout(new MigLayout("", "[175px,grow][100px][50px][100px][175px,grow]", "[21px]"));

        // Botón para añadir nuevo campo
        JButton btnAgregarCampo = new JButton("Añadir campo");
        btnAgregarCampo.setIcon(new ImageIcon(VistaPrincipal.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Indent-White.png")));
        btnAgregarCampo.setForeground(new Color(255, 255, 255));
        btnAgregarCampo.setBackground(new Color(0, 102, 153));
        cuerpo_botones.add(btnAgregarCampo, "cell 1 0,grow");
        btnAgregarCampo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) { // Si hay una fila seleccionada
                    model.insertRow(selectedRow + 1, new Object[]{null, null}); // Insertar debajo de la fila seleccionada
                } else {
                    model.addRow(new Object[]{null, null}); // Agregar al final de la tabla
                }
                table.setEnabled(true);
        	}
        });

        // Botón para eliminar nuevo campo
        JButton btnEliminarCampo = new JButton("Eliminar campo");
        btnEliminarCampo.setIcon(new ImageIcon(VistaPrincipal.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Cut-White.png")));
        btnEliminarCampo.setForeground(Color.WHITE);
        btnEliminarCampo.setBackground(new Color(195, 72, 46));
        btnEliminarCampo.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
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
        cuerpo_botones.add(btnEliminarCampo, "cell 3 0,grow");

        // Creación del cuerpo principal
        JPanel cuerpo_info = new JPanel();
        cuerpo_info.setBackground(new Color(128, 255, 0));
        cuerpo_info.setPreferredSize(new Dimension(900, 500));
        panel.add(cuerpo_info, "cell 0 1,grow");
        cuerpo_info.setLayout(new BorderLayout(0, 0));

        // Creamos el scrollPane
        JScrollPane scrollPane = new JScrollPane();
        cuerpo_info.add(scrollPane, BorderLayout.CENTER);

        // Panel para mostrar los elementos (Display)
        displayPanel = new JPanel();
        displayPanel.setBackground(new Color(223, 221, 255));
        scrollPane.setViewportView(displayPanel);
        displayPanel.setLayout(new GridLayout(1, 0, 5, 5));

        table = createTable();
        scrollPane.setViewportView(table);

        JPanel cuerpo_guardar = new JPanel();
        cuerpo_guardar.setPreferredSize(new Dimension(900, 50));
        cuerpo_guardar.setBackground(new Color(238, 238, 238));
        cuerpo_guardar.setLayout(new MigLayout("", "[400px,grow][100px][400px,grow]", "[40px]"));
        panel.add(cuerpo_guardar, "cell 0 2,growx,aligny top");

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// Guardamos el registro en la base de datos
        		guardarCambiosEnBD();
        		
        		// Cerramos la ventana de insertar
        		cerrar();
        	}
        });
        cuerpo_guardar.add(btnGuardar, "cell 1 0,grow");
    }
    
    private JTable createTable() {
        String[] columnNames = {"CLAVE", "VALOR"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 1L;
        };

        @SuppressWarnings("serial")
		JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        // Añadimos una celda por defecto
        model.addRow(new Object[] {null, null});

        table.setRowHeight(30); // Ajustar la altura de las filas
        table.setEnabled(true); // Habilitar edición de la tabla

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

    // Método para cerrar esta vista
	private void cerrar() {
		Window window = SwingUtilities.getWindowAncestor(this);
	    window.dispose();
    }
	
	private void guardarCambiosEnBD() {
        // Construir el JSON a partir de los datos de la tabla
        JsonStringBuilder jsonInsertBuilder = new JsonStringBuilder();
        Object valueObject;

        int emptyFieldsCount = 0; // Contador para campos vacíos

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String clave = (String) model.getValueAt(i, 0);
            valueObject = model.getValueAt(i, 1);
            
            // Omitir la fila si la columna clave o valor están a null o cadena vacía
            if (clave == null || clave.isEmpty() || valueObject == null) {
                emptyFieldsCount++; // Incrementar el contador de campos vacíos
            } else {
                // Agregar clave y valor al JSON
            	jsonInsertBuilder.append(clave, valueObject);
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

        // Ingresar en la base de datos el registro
        ProductsRepository pr = new ProductsRepository();
        pr.insertOne(jsonInsertBuilder.build(), collection);
        
        // Mostrar mensaje
        JOptionPane.showMessageDialog(null, "Insertado correctamente", "Registro insertado correctamente", JOptionPane.INFORMATION_MESSAGE);
        
        // Mostrar todas las tablas con los datos actualizados
        this.vistaPrincipal.agregarTablas(pr.findAll(collection));
    }

}
