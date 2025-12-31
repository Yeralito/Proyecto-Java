package Proyecto;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;




public class FichaVentas extends javax.swing.JFrame {

    DefaultTableModel modelo;
    private String direccionCliente;

    public FichaVentas() {
        initComponents();
        String[] nomColumna = {"Nro","Codigo","Producto","Cantidad","Precio Unitario","Precio Total"};
        modelo = new DefaultTableModel(nomColumna, 0);
        tblMuestra.setModel(modelo);
        
        //Contador de ficha (el Numero de serie)
        int contador = MantenimientoFicha.leerContador();  // Leer el valor actual del contador
        contador++;                                        // Aumenta el contador
        txtContador.setText(String.valueOf(contador));    // Muestra en el textField
        MantenimientoFicha.guardarContador(contador);     // Guardar el nuevo valor
        
        //La fecha que se genera la ficha
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formato);
        txtFecha.setText(fechaFormateada);
        
    }
    private void guardarFichaEnTXT(String nombreCliente, String fecha, String nombreVendedor, String numFicha, DefaultTableModel modelo) {
    try {
        FileWriter fw = new FileWriter("ventas.txt", true); // true para agregar al final
        BufferedWriter bw = new BufferedWriter(fw);

        // Cabecera
        bw.write("=== Ficha de Venta ===");
        bw.newLine();
        bw.write("Número de ficha: " + numFicha);
        bw.newLine();
        bw.write("Cliente: " + nombreCliente);
        bw.newLine();
        bw.write("Vendedor: " + nombreVendedor);
        bw.newLine();
        bw.write("Fecha: " + fecha);
        bw.newLine();

        // Productos vendidos
        for (int i = 0; i < modelo.getRowCount(); i++) {
    String producto = modelo.getValueAt(i, 2).toString();
    String cantidad = modelo.getValueAt(i, 3).toString();
    String precio = modelo.getValueAt(i, 4).toString();
    String total = modelo.getValueAt(i, 5).toString();
    bw.write("Producto: " + producto + " | Cantidad: " + cantidad + " | Precio de Producto: " + precio + " | Total: " + total );
    bw.newLine();
}

        bw.write("=======================");
        bw.newLine();
        bw.newLine();

        bw.close();
        fw.close();

        JOptionPane.showMessageDialog(this, "Ficha guardada exitosamente.");

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la ficha: " + e.getMessage());
    }
    }
    public int contarReportesDelMesActual() {
    int contador = 0;
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    YearMonth mesActual = YearMonth.now();

    try (BufferedReader br = new BufferedReader(new FileReader("ventas.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.startsWith("Fecha: ")) {
                String fechaTexto = linea.substring(7); // Quita "Fecha: "
                LocalDate fecha = LocalDate.parse(fechaTexto, formatoFecha);
                if (YearMonth.from(fecha).equals(mesActual)) {
                    contador++;
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al contar reportes: " + e.getMessage());
    }

    return contador;
    }
    public void exportarReporteMensualAPDF() {
    String archivoTXT = "ventas.txt";
    String archivoPDF = "reporte_" + YearMonth.now() + ".pdf";
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    YearMonth mesActual = YearMonth.now();

    try {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
        documento.open();

        // Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte de Ventas - " + mesActual, tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{2, 2, 2, 3, 2, 2, 2});
        tabla.addCell("Cliente");
        tabla.addCell("Vendedor");
        tabla.addCell("Fecha");
        tabla.addCell("Producto");
        tabla.addCell("Cantidad");
        tabla.addCell("Precio");
        tabla.addCell("Total");

        BufferedReader br = new BufferedReader(new FileReader(archivoTXT));
        String linea;
        String cliente = "", vendedor = "", fecha = "";
        boolean dentroDeFicha = false;

        while ((linea = br.readLine()) != null) {
    if (linea.contains("=== Ficha de Venta ===")) {
        dentroDeFicha = true;
    } else if (dentroDeFicha) {
        if (linea.startsWith("Cliente: ")) {
            cliente = linea.substring(9);
        } else if (linea.startsWith("Vendedor: ")) {
            vendedor = linea.substring(10);
        } else if (linea.startsWith("Fecha: ")) {
            fecha = linea.substring(7);
        } else if (linea.startsWith("Producto: ")) {
            if (!fecha.isEmpty()) {
                try {
                    LocalDate fechaVenta = LocalDate.parse(fecha, formatoFecha);
                    if (YearMonth.from(fechaVenta).equals(mesActual)) {
                        String[] partes = linea.split("\\|");

                        String producto = partes[0].split(":")[1].trim();
                        String cantidad = partes[1].split(":")[1].trim();
                        String precio = partes[2].split(":")[1].trim();
                        String total = (partes.length > 3) ? partes[3].split(":")[1].trim() : "0.00";

                        tabla.addCell(cliente);
                        tabla.addCell(vendedor);
                        tabla.addCell(fecha);
                        tabla.addCell(producto);
                        tabla.addCell(cantidad);
                        tabla.addCell(precio);
                        tabla.addCell(total);
                    }
                } catch (Exception e) {
                    System.out.println("Error al procesar la línea: " + linea);
                    e.printStackTrace();
                }
            }
        } else if (linea.isEmpty()) {
            dentroDeFicha = false;
        }
    }
}

        documento.add(tabla);
        documento.close();
        br.close();

        JOptionPane.showMessageDialog(null, "Reporte mensual exportado a PDF correctamente: " + archivoPDF);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
    }
}
    public void exportarReporteDiarioAPDF() {
    String archivoTXT = "ventas.txt";
    String fechaHoy = LocalDate.now().toString();
    String archivoPDF = "reporte_diario_" + fechaHoy + ".pdf";
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
        documento.open();

        // Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte Diario de Ventas - " + fechaHoy, tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{2, 2, 2, 3, 2, 2, 2});
        tabla.addCell("Cliente");
        tabla.addCell("Vendedor");
        tabla.addCell("Fecha");
        tabla.addCell("Producto");
        tabla.addCell("Cantidad");
        tabla.addCell("Precio");
        tabla.addCell("Total");

        BufferedReader br = new BufferedReader(new FileReader(archivoTXT));
        String linea;
        String cliente = "", vendedor = "", fecha = "";
        boolean dentroDeFicha = false;
        double totalAcumulado = 0.0;

        while ((linea = br.readLine()) != null) {
            if (linea.contains("=== Ficha de Venta ===")) {
                dentroDeFicha = true;
            } else if (dentroDeFicha) {
                if (linea.startsWith("Cliente: ")) {
                    cliente = linea.substring(9);
                } else if (linea.startsWith("Vendedor: ")) {
                    vendedor = linea.substring(10);
                } else if (linea.startsWith("Fecha: ")) {
                    fecha = linea.substring(7);
                } else if (linea.startsWith("Producto: ")) {
                    if (!fecha.isEmpty()) {
                        try {
                            LocalDate fechaVenta = LocalDate.parse(fecha, formatoFecha);
                            if (fechaVenta.equals(LocalDate.now())) {
                                String[] partes = linea.split("\\|");

                                String producto = partes[0].split(":")[1].trim();
                                String cantidad = partes[1].split(":")[1].trim();
                                String precio = partes[2].split(":")[1].trim();
                                String total = (partes.length > 3) ? partes[3].split(":")[1].trim() : "0.00";

                                // Sumar total
                                double totalDouble = Double.parseDouble(total.replace(",", "."));
                                totalAcumulado += totalDouble;

                                tabla.addCell(cliente);
                                tabla.addCell(vendedor);
                                tabla.addCell(fecha);
                                tabla.addCell(producto);
                                tabla.addCell(cantidad);
                                tabla.addCell(precio);
                                tabla.addCell(String.format("%.2f", totalDouble));
                            }
                        } catch (Exception e) {
                            System.out.println("Error en línea: " + linea);
                            e.printStackTrace();
                        }
                    }
                } else if (linea.isEmpty()) {
                    dentroDeFicha = false;
                }
            }
        }

        documento.add(tabla);
        documento.add(Chunk.NEWLINE);

        Paragraph totalFinal = new Paragraph("Total acumulado del día: S/. " + String.format("%.2f", totalAcumulado));
        totalFinal.setAlignment(Element.ALIGN_RIGHT);
        documento.add(totalFinal);

        documento.close();
        br.close();

        JOptionPane.showMessageDialog(null, "Reporte diario generado: " + archivoPDF);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar reporte diario: " + e.getMessage());
    }
}
    
    public void exportarHistorialVentasAPDF() {
    String archivoTXT = "ventas.txt";
    String archivoPDF = "historial_ventas.pdf";

    try {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
        documento.open();

        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Historial de Ventas", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(Chunk.NEWLINE);

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{2, 2, 2, 3, 2, 2, 2});
        tabla.addCell("Cliente");
        tabla.addCell("Vendedor");
        tabla.addCell("Fecha");
        tabla.addCell("Producto");
        tabla.addCell("Cantidad");
        tabla.addCell("Precio");
        tabla.addCell("Total");

        BufferedReader br = new BufferedReader(new FileReader(archivoTXT));
        String linea;
        String cliente = "", vendedor = "", fecha = "";
        boolean dentroDeFicha = false;
        double totalAcumulado = 0.0;

        while ((linea = br.readLine()) != null) {
            if (linea.contains("=== Ficha de Venta ===")) {
                dentroDeFicha = true;
            } else if (dentroDeFicha) {
                if (linea.startsWith("Cliente: ")) {
                    cliente = linea.substring(9);
                } else if (linea.startsWith("Vendedor: ")) {
                    vendedor = linea.substring(10);
                } else if (linea.startsWith("Fecha: ")) {
                    fecha = linea.substring(7);
                } else if (linea.startsWith("Producto: ")) {
                    try {
                        String[] partes = linea.split("\\|");

                        String producto = partes[0].split(":")[1].trim();
                        String cantidad = partes[1].split(":")[1].trim();
                        String precio = partes[2].split(":")[1].trim();
                        String total = (partes.length > 3) ? partes[3].split(":")[1].trim() : "0.00";

                        // Convertir total a double
                        double totalDouble = Double.parseDouble(total.replace(",", "."));
                        totalAcumulado += totalDouble;

                        // Añadir a tabla
                        tabla.addCell(cliente);
                        tabla.addCell(vendedor);
                        tabla.addCell(fecha);
                        tabla.addCell(producto);
                        tabla.addCell(cantidad);
                        tabla.addCell(precio);
                        tabla.addCell(String.format("%.2f", totalDouble));

                    } catch (Exception e) {
                        System.out.println("Error en línea: " + linea);
                        e.printStackTrace();
                    }
                } else if (linea.isEmpty()) {
                    dentroDeFicha = false;
                }
            }
        }

        documento.add(tabla);

        documento.add(Chunk.NEWLINE);
        Paragraph totalFinal = new Paragraph("Total acumulado de ventas: S/. " + String.format("%.2f", totalAcumulado));
        totalFinal.setAlignment(Element.ALIGN_RIGHT);
        documento.add(totalFinal);

        documento.close();
        br.close();

        JOptionPane.showMessageDialog(null, "Historial de ventas exportado correctamente a: " + archivoPDF);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar historial: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        dpEscritorio = new javax.swing.JDesktopPane();
        pnlTitulo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubTitulo = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblImagen = new javax.swing.JLabel();
        txtContador = new javax.swing.JTextField();
        pnlRegistro = new javax.swing.JPanel();
        txtTipoDoc = new javax.swing.JTextField();
        txtCodProducto = new javax.swing.JTextField();
        cbTipoDoc = new javax.swing.JComboBox<>();
        btnBuscarCliente = new javax.swing.JButton();
        txtClienteObtenido = new javax.swing.JTextField();
        lblCodProducto = new javax.swing.JLabel();
        btnBuscarProducto = new javax.swing.JButton();
        txtProductoObtenido = new javax.swing.JTextField();
        txtVendedor = new javax.swing.JTextField();
        btnCancelarRegistro = new javax.swing.JButton();
        txtCantidad = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        lblContadorMes = new javax.swing.JTextField();
        pnlMuestra = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMuestra = new javax.swing.JTable();
        pnlAccion = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCalcular = new javax.swing.JButton();
        btnGenerar = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        btnReportesMensuales = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnExportarReporteDiario = new javax.swing.JButton();
        pnlTotal = new javax.swing.JPanel();
        txtTotal = new javax.swing.JTextField();
        menuBarPrincipal = new javax.swing.JMenuBar();
        menuProducto = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        MenuItemRegistrarP = new javax.swing.JMenuItem();
        MenuItemEliminarP = new javax.swing.JMenuItem();
        MenuItemMostrarP = new javax.swing.JMenuItem();
        MenuItemEditarP = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        MenuItemRegistrarC = new javax.swing.JMenuItem();
        MenuItemEliminarC = new javax.swing.JMenuItem();
        MenuItemMostrarC = new javax.swing.JMenuItem();
        MenuItemEditarC = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        menuAyuda = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ficha de Ventas");
        setResizable(false);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setText("FICHA DE VENTA \"FFIJ LOGISTIC S.A.C\"");

        lblSubTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSubTitulo.setText("Venta de Petrolio ");

        lblTelefono.setText("Telefono: 987654321");

        lblImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/petroleo-_1_.jpg"))); // NOI18N

        txtContador.setEditable(false);
        txtContador.setBorder(javax.swing.BorderFactory.createTitledBorder("Nº Serie"));

        javax.swing.GroupLayout pnlTituloLayout = new javax.swing.GroupLayout(pnlTitulo);
        pnlTitulo.setLayout(pnlTituloLayout);
        pnlTituloLayout.setHorizontalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTituloLayout.createSequentialGroup()
                        .addGroup(pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTituloLayout.createSequentialGroup()
                                .addComponent(lblTelefono)
                                .addGap(45, 45, 45)))
                        .addGap(51, 51, 51)
                        .addComponent(txtContador, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTituloLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(114, 114, 114)))
                .addComponent(lblImagen)
                .addGap(118, 118, 118))
        );
        pnlTituloLayout.setVerticalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTituloLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlTituloLayout.createSequentialGroup()
                                .addComponent(lblSubTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTelefono))
                            .addGroup(pnlTituloLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtContador, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))))
                    .addComponent(lblImagen))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtTipoDoc.setBorder(javax.swing.BorderFactory.createTitledBorder("DNI / RUC"));

        txtCodProducto.setBorder(null);

        cbTipoDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE", "DNI", "RUC" }));
        cbTipoDoc.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de documento"));

        btnBuscarCliente.setText("BUSCAR CLIENTE");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        txtClienteObtenido.setEditable(false);
        txtClienteObtenido.setBorder(javax.swing.BorderFactory.createTitledBorder("CLIENTE"));

        lblCodProducto.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblCodProducto.setText("CODIGO PRODUCTO:");

        btnBuscarProducto.setText("BUSCAR PRODUCTO");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        txtProductoObtenido.setEditable(false);
        txtProductoObtenido.setBorder(javax.swing.BorderFactory.createTitledBorder("PRODUCTO"));

        txtVendedor.setBorder(javax.swing.BorderFactory.createTitledBorder("VENDEDOR"));

        btnCancelarRegistro.setText("CANCELAR REGISTRO");
        btnCancelarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarRegistroActionPerformed(evt);
            }
        });

        txtCantidad.setBorder(javax.swing.BorderFactory.createTitledBorder("Cantidad"));

        txtPrecioProducto.setEditable(false);
        txtPrecioProducto.setBorder(javax.swing.BorderFactory.createTitledBorder("Precio"));

        txtFecha.setEditable(false);
        txtFecha.setBorder(javax.swing.BorderFactory.createTitledBorder("FECHA"));

        lblContadorMes.setEditable(false);
        lblContadorMes.setBorder(javax.swing.BorderFactory.createTitledBorder("Reportes del Mes Actual:"));
        lblContadorMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblContadorMesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRegistroLayout = new javax.swing.GroupLayout(pnlRegistro);
        pnlRegistro.setLayout(pnlRegistroLayout);
        pnlRegistroLayout.setHorizontalGroup(
            pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistroLayout.createSequentialGroup()
                .addContainerGap(183, Short.MAX_VALUE)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnlRegistroLayout.createSequentialGroup()
                        .addComponent(lblContadorMes, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelarRegistro))
                    .addGroup(pnlRegistroLayout.createSequentialGroup()
                        .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistroLayout.createSequentialGroup()
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRegistroLayout.createSequentialGroup()
                                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblCodProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(pnlRegistroLayout.createSequentialGroup()
                                        .addComponent(cbTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTipoDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                    .addComponent(txtCodProducto))))
                        .addGap(18, 18, 18)
                        .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtFecha))
                        .addGap(18, 18, 18)
                        .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                            .addComponent(txtClienteObtenido)
                            .addComponent(txtProductoObtenido))))
                .addGap(40, 40, 40))
        );
        pnlRegistroLayout.setVerticalGroup(
            pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTipoDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                        .addComponent(cbTipoDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtClienteObtenido, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProductoObtenido, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                        .addComponent(lblCodProducto))
                    .addComponent(txtCodProducto)
                    .addComponent(btnBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(txtCantidad)
                    .addComponent(txtVendedor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContadorMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblMuestra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nº", "Codigo", "Producto", "Cantidad", "Precio Unitario", "Precio Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblMuestra);

        javax.swing.GroupLayout pnlMuestraLayout = new javax.swing.GroupLayout(pnlMuestra);
        pnlMuestra.setLayout(pnlMuestraLayout);
        pnlMuestraLayout.setHorizontalGroup(
            pnlMuestraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMuestraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlMuestraLayout.setVerticalGroup(
            pnlMuestraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMuestraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Agregar.png"))); // NOI18N
        btnAgregar.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar"));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar (1).png"))); // NOI18N
        btnEliminar.setBorder(javax.swing.BorderFactory.createTitledBorder("Eliminar"));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCalcular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calcular (1).png"))); // NOI18N
        btnCalcular.setBorder(javax.swing.BorderFactory.createTitledBorder("Calcular"));
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        btnGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generar (1).png"))); // NOI18N
        btnGenerar.setBorder(javax.swing.BorderFactory.createTitledBorder("Generar Ficha"));
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        btnExportarExcel.setText("Exportar Reporte Mensual");
        btnExportarExcel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        btnReportesMensuales.setText("Mostrar reportes del Mes Actual");
        btnReportesMensuales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesMensualesActionPerformed(evt);
            }
        });

        jButton1.setText("Exportar Historial de Ventas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnExportarReporteDiario.setText("Exportar Reporte Diario");
        btnExportarReporteDiario.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnExportarReporteDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarReporteDiarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAccionLayout = new javax.swing.GroupLayout(pnlAccion);
        pnlAccion.setLayout(pnlAccionLayout);
        pnlAccionLayout.setHorizontalGroup(
            pnlAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReportesMensuales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarReporteDiario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        pnlAccionLayout.setVerticalGroup(
            pnlAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAccionLayout.createSequentialGroup()
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReportesMensuales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarReporteDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnCalcular, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGenerar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        txtTotal.setEditable(false);
        txtTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("TOTAL A PAGAR"));

        javax.swing.GroupLayout pnlTotalLayout = new javax.swing.GroupLayout(pnlTotal);
        pnlTotal.setLayout(pnlTotalLayout);
        pnlTotalLayout.setHorizontalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTotalLayout.setVerticalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotal)
                .addContainerGap())
        );

        dpEscritorio.setLayer(pnlTitulo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dpEscritorio.setLayer(pnlRegistro, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dpEscritorio.setLayer(pnlMuestra, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dpEscritorio.setLayer(pnlAccion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dpEscritorio.setLayer(pnlTotal, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dpEscritorioLayout = new javax.swing.GroupLayout(dpEscritorio);
        dpEscritorio.setLayout(dpEscritorioLayout);
        dpEscritorioLayout.setHorizontalGroup(
            dpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dpEscritorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dpEscritorioLayout.createSequentialGroup()
                        .addComponent(pnlAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMuestra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        dpEscritorioLayout.setVerticalGroup(
            dpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dpEscritorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMuestra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlAccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 99, Short.MAX_VALUE))
        );

        menuProducto.setText("Mantenimiento");

        jMenu2.setText("Productos");

        MenuItemRegistrarP.setText("Registrar un nuevo producto");
        MenuItemRegistrarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemRegistrarPActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemRegistrarP);

        MenuItemEliminarP.setText("Eliminar producto");
        MenuItemEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEliminarPActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemEliminarP);

        MenuItemMostrarP.setText("Mostrar producto");
        MenuItemMostrarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemMostrarPActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemMostrarP);

        MenuItemEditarP.setText("Editar producto");
        MenuItemEditarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditarPActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemEditarP);

        menuProducto.add(jMenu2);

        jMenu1.setText("Clientes");

        MenuItemRegistrarC.setText("Registrar un nuevo Cliente");
        MenuItemRegistrarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemRegistrarCActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemRegistrarC);

        MenuItemEliminarC.setText("Eliminar Cliente");
        MenuItemEliminarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEliminarCActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemEliminarC);

        MenuItemMostrarC.setText("Mostrar Cliente");
        MenuItemMostrarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemMostrarCActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemMostrarC);

        MenuItemEditarC.setText("Editar Cliente");
        MenuItemEditarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditarCActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemEditarC);

        menuProducto.add(jMenu1);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Filtrar Ventas");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        menuProducto.add(jCheckBoxMenuItem1);

        menuBarPrincipal.add(menuProducto);

        menuAyuda.setText("Ayuda");

        jMenu4.setText("Versión 2.3");
        menuAyuda.add(jMenu4);

        jMenu3.setText("Informe de actualizaciones");

        jMenu5.setText("Por el momento no hay actualizaciones disponibles.");
        jMenu3.add(jMenu5);

        menuAyuda.add(jMenu3);

        jMenuItem1.setText("Manual del Usuario");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuAyuda.add(jMenuItem1);

        jMenuItem2.setText("Preguntas Frecuentes");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menuAyuda.add(jMenuItem2);

        menuBarPrincipal.add(menuAyuda);

        setJMenuBar(menuBarPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dpEscritorio, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dpEscritorio)
        );

        setSize(new java.awt.Dimension(1247, 799));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        
        String nombreCliente = txtClienteObtenido.getText().trim();

        // Validar nombre vacío
        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente está vacío.");
            return;
        }

        // Validar campos vacíos
        if (txtCodProducto.getText().isEmpty() || txtProductoObtenido.getText().isEmpty() ||
            txtCantidad.getText().isEmpty() || txtPrecioProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos");
            return;
        }
            
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precioUnitario = Double.parseDouble(txtPrecioProducto.getText());
            double precioTotal = MantenimientoFicha.calcularPrecioTotal(cantidad, precioUnitario);
            int nro = modelo.getRowCount() + 1;

            modelo.addRow(new Object[]{
                nro,
                txtCodProducto.getText(),
                txtProductoObtenido.getText(),
                cantidad,
                String.format("%.2f", precioUnitario),
                String.format("%.2f", precioTotal)
            });

            // Crear carpeta si no existe
            File carpeta = new File("fproducto registrado");
            if (!carpeta.exists()) {
               carpeta.mkdirs();
            }

            // Formato para fecha y hora actual
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

            // Limpiar nombre del cliente (quitar caracteres inválidos para nombre de archivo)
            String nombreSeguro = nombreCliente.replaceAll("[^a-zA-Z0-9_-]", "_");

            // Crear archivo único por ficha
            File archivoCliente = new File(carpeta, nombreSeguro + "_" + timestamp + ".txt");

            // Guardar contenido en archivo
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCliente, true))) {
                bw.write(nro + ";" + txtCodProducto.getText() + ";" + txtProductoObtenido.getText() + ";" +
                    txtCantidad.getText() + ";" + 
                    String.format("%.2f", precioUnitario) + ";" +
                    String.format("%.2f", precioTotal));
                bw.newLine();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar en archivo: " + e.getMessage());
            }

            // Limpiar campos
            txtCodProducto.setText("");
            txtProductoObtenido.setText("");
            txtCantidad.setText("");
            txtPrecioProducto.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad o precio inválido.");
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void MenuItemRegistrarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemRegistrarCActionPerformed
       JRegistrarClientes frmCliente = new JRegistrarClientes();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemRegistrarCActionPerformed

    private void MenuItemEliminarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEliminarCActionPerformed
        JEliminarClientes frmCliente = new JEliminarClientes();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemEliminarCActionPerformed

    private void MenuItemMostrarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemMostrarCActionPerformed
        JMostrarClientes frmCliente = new JMostrarClientes();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemMostrarCActionPerformed

    private void MenuItemEditarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEditarCActionPerformed
        JEditarClientes frmCliente = new JEditarClientes();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemEditarCActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        String tipo = cbTipoDoc.getSelectedItem().toString();
        String documento = txtTipoDoc.getText();

        if (documento.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa el número de documento.");
        return;
        }

        String nombreCliente = MantenimientoFicha.buscarNombrePorTipoYDocumento(tipo, documento);
        txtClienteObtenido.setText(nombreCliente);
        direccionCliente = MantenimientoFicha.buscarDireccionPorTipoYDocumento(tipo, documento);
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed

        String codigo = txtCodProducto.getText();
        
        if (codigo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa el número de documento.");
        return;
        }
        String precioProducto = MantenimientoFicha.buscarPrecioPorCodigo(codigo);
        txtPrecioProducto.setText(precioProducto);
        String nombreProducto = MantenimientoFicha.buscarNombreProductoPorCodigo(codigo);
        txtProductoObtenido.setText(nombreProducto);
        
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void MenuItemRegistrarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemRegistrarPActionPerformed
        JRegistrarProductos frmCliente = new JRegistrarProductos();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemRegistrarPActionPerformed

    private void MenuItemEditarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEditarPActionPerformed
        JEditarProductos frmCliente = new JEditarProductos();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemEditarPActionPerformed

    private void MenuItemMostrarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemMostrarPActionPerformed
        JMostrarProductos frmCliente = new JMostrarProductos();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemMostrarPActionPerformed

    private void MenuItemEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEliminarPActionPerformed
        JEliminarProductos frmCliente = new JEliminarProductos();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_MenuItemEliminarPActionPerformed

    private void btnCancelarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarRegistroActionPerformed
        Object[] opciones = {"Sí", "No"};
        int confirmacion = JOptionPane.showOptionDialog(
            this,
            "¿Estás seguro de que deseas cancelar la ficha",
            "Confirmar cancelacion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            opciones, opciones[0]
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            txtTipoDoc.setText("");
            txtCodProducto.setText("");
            txtClienteObtenido.setText("");
            txtProductoObtenido.setText("");
            txtCantidad.setText("");
            txtPrecioProducto.setText("");
            txtVendedor.setText("");
            txtTotal.setText("");

            modelo.setRowCount(0);

            JOptionPane.showMessageDialog(this, "Campos y tabla limpiados correctamente.");
        }
    }//GEN-LAST:event_btnCancelarRegistroActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        int filaSeleccionada = tblMuestra.getSelectedRow();

        String nombreCliente = txtClienteObtenido.getText();
        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente está vacío.");
            return;
        }

        if (filaSeleccionada >= 0) {
            // Confirmacion antes de eliminar
            Object[] opciones = {"Sí", "No"};
            int confirmacion = JOptionPane.showOptionDialog(
            this,
            "¿Estás seguro de que deseas eliminar este producto?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones, opciones[0]
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return; // Si el usuario elige NO, no hacemos nada
            }

            // Obtener el Nº
            int nroEliminar = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 0).toString()) - 1;

            // Eliminar de la tabla
            modelo.removeRow(filaSeleccionada);

            // Reordenar los Nº
            for (int i = 0; i < modelo.getRowCount(); i++) {
                modelo.setValueAt(i + 1, i, 0);
            }

            // Ruta y archivo dentro de la carpeta "fichas registradas"
            File carpeta = new File("fichas registradas");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            File archivo = new File(carpeta, nombreCliente + ".txt");

            ArrayList<String> lineas = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error leyendo el archivo: " + e.getMessage());
                return;
            }

            // Verificamos que exista la línea a eliminar
            if (nroEliminar < 0 || nroEliminar >= lineas.size()) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar la línea en el archivo.");
                return;
            }

            // Eliminar la línea según el Nº
            lineas.remove(nroEliminar);

            // actualizar el archivo actualizando los números
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
                for (int i = 0; i < lineas.size(); i++) {
                    String[] partes = lineas.get(i).split(";", 2);
                    bw.write((i + 1) + ";" + partes[1]);
                    bw.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error escribiendo el archivo: " + e.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        
        double sumaTotal = 0.0;
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valorCelda = modelo.getValueAt(i, 5);
            if (valorCelda != null) {
                double precio = Double.parseDouble(valorCelda.toString());
                sumaTotal += precio;
            }
        }
        txtTotal.setText(String.format("%.2f", sumaTotal));
        
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed

        String nombreCliente = txtClienteObtenido.getText();
        String fechaTexto = LocalDate.now().toString();
        String nombreVendedor = txtVendedor.getText();
        String numFicha = txtContador.getText();
        
        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente está vacío.");
            return;
        }
        
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en la tabla.");
            return;
        }
        
        if (nombreVendedor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del vendedor está vacío.");
            return;
        }
        
        File carpeta = new File("boletas");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String fechaParaArchivo = fechaTexto.replace("/", "-").replace(" ", "_");
        
        String[] opciones = {"Boleta", "Factura"};
        int eleccion = JOptionPane.showOptionDialog(
            this,
            "¿Qué tipo de documento desea generar?",
            "Seleccionar tipo de comprobante",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        // Determinar tipo de documento
        String tipoDocumento;
        if(eleccion == 0){
            tipoDocumento = "BOLETA";
        } else{
            tipoDocumento = "FACTURA";
        }

        // Crear nombre del archivo con tipo, ficha, cliente y fecha
        String archivoNombre = tipoDocumento.toLowerCase() + "_" + numFicha + "_" + nombreCliente.replaceAll(" ", "_") + "_" + fechaParaArchivo + ".pdf";
        File archivoPDF = new File(carpeta, archivoNombre);

        try {
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

            Font negrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Encabezado
            if (eleccion == -1) {
                JOptionPane.showMessageDialog(this, "Operación cancelada.");
                return;
            }
            
            documento.add(new Paragraph(tipoDocumento, negrita));
            documento.add(new Paragraph("Nº Ficha: " + numFicha, normal));
            documento.add(new Paragraph("Cliente: " + nombreCliente, normal));
            documento.add(new Paragraph("Fecha: " + fechaTexto, normal));
            documento.add(new Paragraph("Vendedor: " + nombreVendedor, normal));
            documento.add(new Paragraph(" ")); // salto de línea

            // Tabla con 6 columnas
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1f, 2f, 4f, 2f, 2f, 2f});

            String[] columnas = {"Nro", "Código", "Producto", "Cantidad", "Precio Unitario", "Precio Total"};
            for (String col : columnas) {
                PdfPCell celda = new PdfPCell(new Phrase(col, negrita));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
            }

            for (int i = 0; i < modelo.getRowCount(); i++) {
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    String valor = modelo.getValueAt(i, j) != null ? modelo.getValueAt(i, j).toString() : "";
                    PdfPCell celda = new PdfPCell(new Phrase(valor, normal));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla.addCell(celda);
                }
            }

            documento.add(tabla);
            documento.add(new Paragraph(" ")); // salto de línea
            if (eleccion == 1) {
                // FACTURA: calcular subtotal e IGV
                double total = Double.parseDouble(txtTotal.getText());
                double igv = total * 0.18;
                double subtotal = total - igv;

                documento.add(new Paragraph("Dirección: " + direccionCliente, normal));
                documento.add(new Paragraph("Subtotal: " + String.format("%.2f", subtotal), normal));
                documento.add(new Paragraph("IGV (18%): " + String.format("%.2f", igv), normal));
                documento.add(new Paragraph("TOTAL: " + String.format("%.2f", total), negrita));
            } else {
                // BOLETA: solo mostrar total
                documento.add(new Paragraph("TOTAL: " + txtTotal.getText(), negrita));
            }

            documento.close();
            if(eleccion == 1){
                JOptionPane.showMessageDialog(this, "Factura PDF generada:\n" + archivoPDF.getPath());
            } else {
                JOptionPane.showMessageDialog(this, "Boleta PDF generada:\n" + archivoPDF.getPath());
            }
            

        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
        }
        guardarFichaEnTXT(nombreCliente, fechaTexto, nombreVendedor, numFicha, modelo);
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnReportesMensualesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesMensualesActionPerformed
        int reportesDelMes = contarReportesDelMesActual();
        lblContadorMes.setText("Reportes este mes: " + reportesDelMes);
    }//GEN-LAST:event_btnReportesMensualesActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        exportarReporteMensualAPDF();
    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        FiltrarVentas frmCliente = new FiltrarVentas();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        exportarHistorialVentasAPDF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lblContadorMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblContadorMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblContadorMesActionPerformed

    private void btnExportarReporteDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarReporteDiarioActionPerformed
        exportarReporteDiarioAPDF();
    }//GEN-LAST:event_btnExportarReporteDiarioActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JPreguntas frmCliente = new JPreguntas();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JManualUsuario frmCliente = new JManualUsuario();
        dpEscritorio.add(frmCliente);
        frmCliente.show();
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FichaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FichaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FichaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FichaVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FichaVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MenuItemEditarC;
    private javax.swing.JMenuItem MenuItemEditarP;
    private javax.swing.JMenuItem MenuItemEliminarC;
    private javax.swing.JMenuItem MenuItemEliminarP;
    private javax.swing.JMenuItem MenuItemMostrarC;
    private javax.swing.JMenuItem MenuItemMostrarP;
    private javax.swing.JMenuItem MenuItemRegistrarC;
    private javax.swing.JMenuItem MenuItemRegistrarP;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnCancelarRegistro;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnExportarReporteDiario;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnReportesMensuales;
    private javax.swing.JComboBox<String> cbTipoDoc;
    private javax.swing.JDesktopPane dpEscritorio;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodProducto;
    private javax.swing.JTextField lblContadorMes;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblSubTitulo;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenuBar menuBarPrincipal;
    private javax.swing.JMenu menuProducto;
    private javax.swing.JPanel pnlAccion;
    private javax.swing.JPanel pnlMuestra;
    private javax.swing.JPanel pnlRegistro;
    private javax.swing.JPanel pnlTitulo;
    private javax.swing.JPanel pnlTotal;
    private javax.swing.JTable tblMuestra;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtClienteObtenido;
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtContador;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtProductoObtenido;
    private javax.swing.JTextField txtTipoDoc;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables
}
