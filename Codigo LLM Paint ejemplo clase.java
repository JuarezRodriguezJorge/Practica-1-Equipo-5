import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MiniPaint extends JFrame {

    // Componentes de la interfaz
    private JComboBox<String> comboColor;
    private JComboBox<String> comboForma;
    private JCheckBox checkLleno;
    private JButton btnDeshacer;
    private JButton btnBorrar;
    private JLabel labelCoordenadas;
    private PanelLienzo panelLienzo;

    public MiniPaint() {
        setTitle("Mini Paint Basico");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Panel de Controles (Parte Superior)
        JPanel panelControles = new JPanel();
        panelControles.setBackground(new Color(240, 240, 240));

        String[] colores = {"Magenta", "Negro", "Verde", "Azul", "Rojo"};
        comboColor = new JComboBox<>(colores);

        String[] formas = {"Rectangulo", "Ovalo"};
        comboForma = new JComboBox<>(formas);

        checkLleno = new JCheckBox("Lleno");

        btnDeshacer = new JButton("Deshacer");
        btnBorrar = new JButton("Borrar");

        panelControles.add(comboColor);
        panelControles.add(comboForma);
        panelControles.add(checkLleno);
        panelControles.add(btnDeshacer);
        panelControles.add(btnBorrar);

        add(panelControles, BorderLayout.NORTH);

        // 2. Barra de Estado de Coordenadas (Parte Inferior Izquierda)
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelCoordenadas = new JLabel("(0, 0)");
        labelCoordenadas.setForeground(Color.GRAY);
        panelEstado.add(labelCoordenadas);
        add(panelEstado, BorderLayout.SOUTH);

        // 3. Área de Dibujo (Centro)
        panelLienzo = new PanelLienzo();
        panelLienzo.setBackground(Color.WHITE);
        add(panelLienzo, BorderLayout.CENTER);

        // 4. Asignar Acciones a los Botones
        btnDeshacer.addActionListener(e -> panelLienzo.deshacer());
        btnBorrar.addActionListener(e -> panelLienzo.borrarTodo());
    }

    // --- CLASE INTERNA: PANEL DE DIBUJO ---
    class PanelLienzo extends JPanel {
        // La "Memoria" del programa para poder usar la función Deshacer
        private ArrayList<Figura> listaFiguras = new ArrayList<>();
        private Figura figuraActual = null;

        public PanelLienzo() {
            // Manejador de eventos del ratón
            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Al hacer clic, creamos una nueva figura con la configuración actual
                    Color color = obtenerColor(comboColor.getSelectedItem().toString());
                    String forma = comboForma.getSelectedItem().toString();
                    boolean lleno = checkLleno.isSelected();
                    
                    figuraActual = new Figura(e.getX(), e.getY(), color, forma, lleno);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    // Al arrastrar, actualizamos el tamaño de la figura en tiempo real
                    if (figuraActual != null) {
                        figuraActual.setX2(e.getX());
                        figuraActual.setY2(e.getY());
                        repaint(); // Actualiza la pantalla
                    }
                    labelCoordenadas.setText("(" + e.getX() + ", " + e.getY() + ")");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Al soltar el clic, guardamos la figura en la lista definitiva
                    if (figuraActual != null) {
                        figuraActual.setX2(e.getX());
                        figuraActual.setY2(e.getY());
                        listaFiguras.add(figuraActual);
                        figuraActual = null;
                        repaint();
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    // Solo para actualizar el texto de abajo a la izquierda
                    labelCoordenadas.setText("(" + e.getX() + ", " + e.getY() + ")");
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        // El motor gráfico de Swing
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Limpia la pantalla
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibuja el historial de figuras
            for (Figura f : listaFiguras) {
                f.dibujar(g2d);
            }

            // Dibuja la figura que se está trazando en este momento
            if (figuraActual != null) {
                figuraActual.dibujar(g2d);
            }
        }

        // Lógica de los botones
        public void deshacer() {
            if (!listaFiguras.isEmpty()) {
                listaFiguras.remove(listaFiguras.size() - 1);
                repaint();
            }
        }

        public void borrarTodo() {
            listaFiguras.clear();
            repaint();
        }
    }

    // --- CLASE INTERNA: MOLDE DE LAS FIGURAS ---
    class Figura {
        private int x1, y1, x2, y2;
        private Color color;
        private String tipo;
        private boolean lleno;

        public Figura(int x, int y, Color color, String tipo, boolean lleno) {
            this.x1 = x;
            this.y1 = y;
            this.x2 = x;
            this.y2 = y;
            this.color = color;
            this.tipo = tipo;
            this.lleno = lleno;
        }

        public void setX2(int x2) { this.x2 = x2; }
        public void setY2(int y2) { this.y2 = y2; }

        public void dibujar(Graphics g) {
            g.setColor(color);
            
            // Swing requiere el punto superior izquierdo, ancho y alto para dibujar
            int inicioX = Math.min(x1, x2);
            int inicioY = Math.min(y1, y2);
            int ancho = Math.abs(x1 - x2);
            int alto = Math.abs(y1 - y2);

            if (tipo.equals("Rectangulo")) {
                if (lleno) g.fillRect(inicioX, inicioY, ancho, alto);
                else g.drawRect(inicioX, inicioY, ancho, alto);
            } else if (tipo.equals("Ovalo")) {
                if (lleno) g.fillOval(inicioX, inicioY, ancho, alto);
                else g.drawOval(inicioX, inicioY, ancho, alto);
            }
        }
    }

    // Método auxiliar para los colores
    private Color obtenerColor(String nombreColor) {
        switch (nombreColor) {
            case "Magenta": return new Color(200, 100, 200); 
            case "Verde": return new Color(180, 240, 150); // Verde claro similar al de tu imagen
            case "Azul": return Color.BLUE;
            case "Rojo": return Color.RED;
            default: return Color.BLACK;
        }
    }

    // --- PUNTO DE ENTRADA PRINCIPAL ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniPaint app = new MiniPaint();
            app.setVisible(true);
        });
    }
}