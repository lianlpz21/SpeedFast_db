package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private EntregaPanel entregaPanel;

    public MainFrame() {

        setTitle("SpeedFast - Gestión");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Repartidores", new RepartidorPanel());
        tabs.addTab("Pedidos", new PedidoPanel());

        entregaPanel = new EntregaPanel();
        tabs.addTab("Entregas", entregaPanel);

        // Refrescar al actualizar datos
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 2) {
                entregaPanel.refrescarDatos();
            }
        });

        add(tabs, BorderLayout.CENTER);
    }
}