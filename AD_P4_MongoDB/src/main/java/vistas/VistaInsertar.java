package vistas;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class VistaInsertar extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel displayPanel;
	private JTable table;

    public VistaInsertar() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Contenedor principal
        JPanel container = new JPanel();
        add(container, BorderLayout.CENTER);
        container.setLayout(new MigLayout("", "[50px][800px,grow][50px]", "[50px][500px,grow][50px]"));
        
        // Panel para la tabla y los campos clave-valor
        JPanel panel = new JPanel();
        panel.setBackground(new Color(238, 238, 238));
        container.add(panel, "cell 1 1,grow");
        panel.setLayout(new MigLayout("", "[800px,grow]", "[50px][400px,grow][50px]"));
        
        // Cuerpo para los botones de añadir y eliminar campo
        JPanel cuerpo_botones = new JPanel();
        cuerpo_botones.setPreferredSize(new Dimension(900, 50));
        cuerpo_botones.setBackground(new Color(238, 238, 238));
        panel.add(cuerpo_botones, "cell 0 0,growx,aligny top");
        cuerpo_botones.setLayout(new MigLayout("", "[325px,grow][100px][50px,grow][100px][325px,grow]", "[21px]"));
        
        // Botón para añadir nuevo campo
        JButton btnAddField = new JButton("Añadir campo");
        btnAddField.setForeground(new Color(255, 255, 255));
        cuerpo_botones.add(btnAddField, "cell 1 0,grow");
        btnAddField.setBackground(new Color(14, 161, 41));
        btnAddField.setPreferredSize(new Dimension(30, 30));
        btnAddField.setIcon(new ImageIcon(getClass().getResource("/Iconos/Icono_Añadir.png")));
        btnAddField.addActionListener(new ActionListener() {
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
        JButton btnDeleteField = new JButton("Eliminar campo");
        cuerpo_botones.add(btnDeleteField, "cell 3 0,grow");
        
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
        panel.add(cuerpo_guardar, "cell 0 2,growx,aligny top");
        cuerpo_guardar.setLayout(new MigLayout("", "[400px,grow][100px][400px,grow]", "[40px]"));
        
        JButton btnGuardar = new JButton("Guardar");
        cuerpo_guardar.add(btnGuardar, "cell 1 0,grow");
    }

    // Método para cerrar esta vista
    private void cerrar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
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
}
