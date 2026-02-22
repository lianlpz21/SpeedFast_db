package view;

import dao.RepartidorDAO;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RepartidorPanel extends JPanel {

    private JTextField txtNombre;
    private JTable table;
    private DefaultTableModel model;
    private RepartidorDAO dao = new RepartidorDAO();

    public RepartidorPanel() {

        setLayout(new BorderLayout());

        // FORM
        JPanel form = new JPanel();
        txtNombre = new JTextField(15);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(btnAgregar);
        form.add(btnEditar);
        form.add(btnEliminar);

        add(form, BorderLayout.NORTH);

        // TABLE
        model = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cargarTabla();

        // EVENTOS
        btnAgregar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre obligatorio");
                return;
            }
            dao.create(new Repartidor(txtNombre.getText()));
            cargarTabla();
            txtNombre.setText("");
        });

        btnEditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) return;

            int id = (int) model.getValueAt(fila, 0);
            dao.update(new Repartidor(id, txtNombre.getText()));
            cargarTabla();
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) return;

            int id = (int) model.getValueAt(fila, 0);
            dao.delete(id);
            cargarTabla();
        });
    }

    private void cargarTabla() {
        model.setRowCount(0);
        List<Repartidor> lista = dao.readAll();
        for (Repartidor r : lista) {
            model.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }
}