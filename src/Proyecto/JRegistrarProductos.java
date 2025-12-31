
package Proyecto;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JRegistrarProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form JRegistrarProductos
     */
    public JRegistrarProductos() {
        initComponents();
        limpiarTabla();
        cargarTabla();
    }
     private void limpiarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(null, new String[]{"Producto", "Codigo", "Precio", "Stock"});
        tbProducto.setModel(modelo);
    }
     private void cargarTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Producto", "Codigo", "Precio", "Stock"});

        File archivoP = new File("productos.txt");
        if (!archivoP.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoP))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    model.addRow(partes);
                }
            }
            tbProducto.setModel(model);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos.");
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtnombreP = new javax.swing.JTextField();
        txtcodigoP = new javax.swing.JTextField();
        txtprecio = new javax.swing.JTextField();
        txtstock = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProducto = new javax.swing.JTable();

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Swis721 Blk BT", 0, 24)); // NOI18N
        jLabel1.setText("INGRESE PRODUCTO A REGISTRAR");

        txtnombreP.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre de Producto:"));

        txtcodigoP.setBorder(javax.swing.BorderFactory.createTitledBorder("Código:"));

        txtprecio.setBorder(javax.swing.BorderFactory.createTitledBorder("Precio:"));

        txtstock.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock:"));

        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Agregar.png"))); // NOI18N
        btnAgregarProducto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_BOTTOM, new java.awt.Font("Segoe UI Black", 0, 14))); // NOI18N
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        tbProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Codigo", "Precio", "Stock"
            }
        ));
        jScrollPane1.setViewportView(tbProducto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97)
                                .addComponent(txtstock))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtnombreP, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88)
                                .addComponent(txtcodigoP, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))))
                .addGap(87, 87, 87))
            .addGroup(layout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcodigoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        String codigo = txtcodigoP.getText().trim();
        String nombre = txtnombreP.getText().trim();
        String precioStr = txtprecio.getText().trim();
        String stockStr = txtstock.getText().trim();

        if (nombre.isEmpty() || codigo.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock inválido.");
            return;
        }

        // Confirmar
        Object[] opciones ={"Sí", "No"};
        int confirm = JOptionPane.showOptionDialog(this,
                "¿Está seguro de que desea registrar este cliente?",
                "Confirmar Registro", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                opciones,opciones[0]);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Verificar duplicado
        if (productoExiste(codigo)) {
            JOptionPane.showMessageDialog(this, "El código ya está registrado.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("productos.txt", true)))) {
            File archivo = new File("productos.txt");
            if (archivo.length() == 0) {
                pw.println("Producto;Codigo;Precio;Stock");
            }
            pw.println(nombre + ";" + codigo + ";" + precio + ";" + stock);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el producto.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Producto registrado.");
        limpiarCampos();
        cargarTabla();
    }
       private void limpiarCampos() {
        txtcodigoP.setText("");
        txtnombreP.setText("");
        txtprecio.setText("");
        txtstock.setText("");
    }

    private boolean productoExiste(String codigoBuscado) {
        File archivo = new File("productos.txt");
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length == 4 && partes[1].equalsIgnoreCase(codigoBuscado)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Ignorar
        }

        return false;
    }//GEN-LAST:event_btnAgregarProductoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbProducto;
    private javax.swing.JTextField txtcodigoP;
    private javax.swing.JTextField txtnombreP;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
