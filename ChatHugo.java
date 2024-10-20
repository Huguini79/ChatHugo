import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatHugo {
    private static PrintWriter salida;
    private static BufferedReader entrada;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Hugo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel label = new JLabel("Introduce tu mensaje:");
        JTextField texto = new JTextField(20);
        JButton boton = new JButton("Enviar");

        panel.add(label);
        panel.add(texto);
        panel.setBackground(Color.YELLOW);
        panel2.setBackground(Color.YELLOW);
        panel2.add(boton);
        texto.setPreferredSize(new Dimension(123, 80));
        boton.setPreferredSize(new Dimension(123, 80));

        // Listener para el botón de enviar
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje(texto);
            }
        });

        panel.setLayout(new FlowLayout());
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        frame.add(panel, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(Color.YELLOW);

        // Establecer el icono de la ventana
        Image icono = Toolkit.getDefaultToolkit().getImage("Comunicación icono.png");
        frame.setIconImage(icono);

        frame.setResizable(false);
        frame.setSize(660, 550);
        frame.setVisible(true);

        // Intentar conectar al servidor
        try {
            Socket conexion = new Socket("2a0c:5a80:120e:d000:bc:b5bb:3fe5:9f39", 5656);
            entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            salida = new PrintWriter(conexion.getOutputStream(), true);
            new Thread(new RecepcionMensajes()).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void enviarMensaje(JTextField texto) {
        String mensaje = texto.getText();
        if (!mensaje.isEmpty()) {
            salida.println(mensaje);
            texto.setText(""); // Limpiar el campo de texto
        }
    }

    private static class RecepcionMensajes implements Runnable {
        @Override
        public void run() {
            String mensaje;
            try {
                while ((mensaje = entrada.readLine()) != null) {
                    mostrarMensaje("Mensaje nuevo: " + mensaje);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mostrarMensaje(String mensaje) {
        // Mostrar el mensaje en un JOptionPane
        JOptionPane.showMessageDialog(null, mensaje, "Nuevo Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
}
