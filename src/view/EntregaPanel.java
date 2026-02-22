package view;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class EntregaPanel extends JPanel {

    private JComboBox<Pedido> cbPedido;
    private JComboBox<Repartidor> cbRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTable table;
    private DefaultTableModel model;

    private EntregaDAO entregaDAO = new EntregaDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private RepartidorDAO repartidorDAO = new RepartidorDAO();

    public EntregaPanel() {

        setLayout(new BorderLayout());

        JPanel form = new JPanel();

        cbPedido = new JComboBox<>();
        cbRepartidor = new JComboBox<>();
        txtFecha = new JTextField(8);
        txtHora = new JTextField(5);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");

        form.add(new JLabel("Pedido:"));
        form.add(cbPedido);
        form.add(new JLabel("Repartidor:"));
        form.add(cbRepartidor);
        form.add(new JLabel("Fecha (YYYY-MM-DD):"));
        form.add(txtFecha);
        form.add(new JLabel("Hora (HH:MM:SS):"));
        form.add(txtHora);
        form.add(btnAgregar);
        form.add(btnEliminar);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID","Pedido","Repartidor","Fecha","Hora"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cargarCombos();
        cargarTabla();

        btnAgregar.addActionListener(e -> {
            try {
                Pedido p = (Pedido) cbPedido.getSelectedItem();
                Repartidor r = (Repartidor) cbRepartidor.getSelectedItem();

                Entrega ent = new Entrega(
                        p.getId(),
                        r.getId(),
                        Date.valueOf(txtFecha.getText()),
                        Time.valueOf(txtHora.getText())
                );

                entregaDAO.create(ent);
                cargarTabla();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Datos inválidos");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) return;

            int id = (int) model.getValueAt(fila,0);
            entregaDAO.delete(id);
            cargarTabla();
        });
    }

    private void cargarCombos() {

        cbPedido.removeAllItems();
        cbRepartidor.removeAllItems();

        for (Pedido p : pedidoDAO.readAll()) {
            cbPedido.addItem(p);
        }

        for (Repartidor r : repartidorDAO.readAll()) {
            cbRepartidor.addItem(r);
        }
    }

    private void cargarTabla() {
        model.setRowCount(0);
        List<Entrega> lista = entregaDAO.readAll();
        for (Entrega e : lista) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getIdPedido(),
                    e.getIdRepartidor(),
                    e.getFecha(),
                    e.getHora()
            });
        }
    }

    public void refrescarDatos() {
        cargarCombos();
        cargarTabla();
    }
}