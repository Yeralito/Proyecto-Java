
package Proyecto;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JEliminarProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form JEliminarProducto
     */
    public JEliminarProductos() {
        initComponents();
    }
    private void mostrarProductos() {
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(new String[]{"Producto", "Código", "Precio", "Stock"});

    File archivo = new File("productos.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        boolean primera = true;

        while ((linea = br.readLine()) != null) {
            if (primera) {
                primera = false;
                continue;
            }

            String[] datos = linea.split(";");
            if (datos.length == 4) {
                model.addRow(datos);
            }
        }

        tbTablaProducto.setModel(model); // Cambia "tabla" por el nombre de tu JTable

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer los productos.");
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblEliminar = new javax.swing.JLabel();
        txtcodigoP = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTablaProducto = new javax.swing.JTable();
        btnMostrarClienteEliminar = new javax.swing.JButton();

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Swis721 Blk BT", 0, 24)); // NOI18N
        jLabel1.setText("INGRESE DATOS DE CLIENTE A ELIMINAR");

        lblEliminar.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblEliminar.setText("Lo Eliminaremos por el código de Producto");

        txtcodigoP.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingrese el código:"));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar (1).png"))); // NOI18N
        btnEliminar.setBorder(javax.swing.BorderFactory.createTitledBorder("Eliminar Producto"));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        tbTablaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbTablaProducto);

        btnMostrarClienteEliminar.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnMostrarClienteEliminar.setText("Mostrar Tabla");
        btnMostrarClienteEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarClienteEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(txtcodigoP, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                                .addComponent(btnMostrarClienteEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblEliminar)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(64, 64, 64))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtcodigoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnMostrarClienteEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    String codigoEliminar = txtcodigoP.getText().trim();

    if (codigoEliminar.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el código del producto a eliminar.");
        return;
    }

    File archivo = new File("productos.txt");
    File archivoTemp = new File("productos_temp.txt");

    boolean encontrado = false;

    try (
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemp))
    ) {
        String linea;
        boolean primera = true;

        while ((linea = br.readLine()) != null) {
            if (primera) {
                // Copiamos la cabecera
                bw.write(linea);
                bw.newLine();
                primera = false;
                continue;
            }

            String[] datos = linea.split(";");

            if (datos.length == 4 && datos[1].equalsIgnoreCase(codigoEliminar)) {
                encontrado = true;
                continue; // No escribir la línea a eliminar
            }

            bw.write(linea);
            bw.newLine();
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al procesar el archivo.");
        return;
    }

    if (archivo.delete()) {
        archivoTemp.renameTo(archivo);
    }

    if (encontrado) {
        JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
        mostrarProductos(); // vuelve a cargar la tabla (definimos abajo)
    } else {
        JOptionPane.showMessageDialog(this, "Producto no encontrado.");
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnMostrarClienteEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarClienteEliminarActionPerformed
    mostrarProductos();
        
    }//GEN-LAST:event_btnMostrarClienteEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnMostrarClienteEliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEliminar;
    private javax.swing.JTable tbTablaProducto;
    private javax.swing.JTextField txtcodigoP;
    // End of variables declaration//GEN-END:variables
}
