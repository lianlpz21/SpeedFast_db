package view;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PedidoPanel extends JPanel {

    private JTextField txtDireccion;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbEstado;
    private JTable table;
    private DefaultTableModel model;
    private PedidoDAO dao = new PedidoDAO();

    public PedidoPanel() {

        setLayout(new BorderLayout());

        JPanel form = new JPanel();

        txtDireccion = new JTextField(10);
        cbTipo = new JComboBox<>(new String[]{"COMIDA","ENCOMIENDA","EXPRESS"});
        cbEstado = new JComboBox<>(new String[]{"PENDIENTE","EN_REPARTO","ENTREGADO"});

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("Dirección:"));
        form.add(txtDireccion);
        form.add(cbTipo);
        form.add(cbEstado);
        form.add(btnAgregar);
        form.add(btnEditar);
        form.add(btnEliminar);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID","Dirección","Tipo","Estado"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cargarTabla();

        btnAgregar.addActionListener(e -> {
            if (txtDireccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,"Dirección obligatoria");
                return;
            }

            dao.create(new Pedido(
                    txtDireccion.getText(),
                    cbTipo.getSelectedItem().toString(),
                    cbEstado.getSelectedItem().toString()
            ));
            cargarTabla();
        });

        btnEditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) return;

            int id = (int) model.getValueAt(fila,0);

            dao.update(new Pedido(
                    id,
                    txtDireccion.getText(),
                    cbTipo.getSelectedItem().toString(),
                    cbEstado.getSelectedItem().toString()
            ));
            cargarTabla();
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) return;

            int id = (int) model.getValueAt(fila,0);
            dao.delete(id);
            cargarTabla();
        });
    }

    private void cargarTabla() {
        model.setRowCount(0);
        List<Pedido> lista = dao.readAll();
        for (Pedido p : lista) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getTipo(),
                    p.getEstado()
            });
        }
    }
}