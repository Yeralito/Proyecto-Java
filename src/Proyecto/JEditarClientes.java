
package Proyecto;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JEditarClientes extends javax.swing.JInternalFrame {


    public JEditarClientes() {
        initComponents();
        txtNombreNuevo.setVisible(false);
        cbTipoNuevo.setVisible(false);
        txtDocumentoNuevo.setVisible(false);
        txtTelefonoNuevo.setVisible(false);
        txtDireccionNueva.setVisible(false);
    }


    @SuppressWarnings("unchecked")
    
    private void buscarClientePorDocumento() {
        String tipo = cbTipo.getSelectedItem().toString();
        String doc = txtDocumento.getText().trim();

        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de documento.");
            return;
        }

        Cliente c = MantenimientoCliente.obtenerClientePorDocumento(doc);

        DefaultTableModel modelo = (DefaultTableModel) TbMostrarCliente.getModel();
        modelo.setRowCount(0); // limpiar tabla

        if (c != null && c.getTipo().equalsIgnoreCase(tipo)) {
            modelo.addRow(new Object[]{
                c.getNombre(),
                c.getTipo(),
                c.getDocumento(),
                c.getTelefono(),
                c.getDireccion()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
        }
    }
    
    private void limpiarCamposEdicion() {
        
        txtNombreNuevo.setText("");
        cbTipoNuevo.setSelectedIndex(0);
        txtDocumentoNuevo.setText("");
        txtTelefonoNuevo.setText("");
        txtDireccionNueva.setText("");
}
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEditarCliente = new javax.swing.JButton();
        cbTipo = new javax.swing.JComboBox<>();
        txtDocumento = new javax.swing.JTextField();
        lblEliminar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnMostrarClienteEditar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TbMostrarCliente = new javax.swing.JTable();
        txtTelefonoNuevo = new javax.swing.JTextField();
        txtDocumentoNuevo = new javax.swing.JTextField();
        txtDireccionNueva = new javax.swing.JTextField();
        txtNombreNuevo = new javax.swing.JTextField();
        cbTipoNuevo = new javax.swing.JComboBox<>();
        btnMostrarCamposEditar = new javax.swing.JButton();

        setClosable(true);
        setResizable(true);
        setTitle("Mostrar Clientes");
        setToolTipText("");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icono-de-edición-un-lápiz-escrib.jpg"))); // NOI18N
        btnEditarCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Editar Cliente"));
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, 121, -1));

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--SELECCIONE--", "DNI", "RUC" }));
        cbTipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Documento"));
        getContentPane().add(cbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 184, -1));

        txtDocumento.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Documento o Ruc:"));
        getContentPane().add(txtDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 340, 43));

        lblEliminar.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblEliminar.setText("Lo Eliminaremos por el Nro de Documento");
        getContentPane().add(lblEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, 40));

        jLabel1.setFont(new java.awt.Font("Swis721 Blk BT", 0, 24)); // NOI18N
        jLabel1.setText("INGRESE DATOS DE CLIENTE A ELIMINAR");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 580, 30));

        btnMostrarClienteEditar.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnMostrarClienteEditar.setText("MOSTRAR CLIENTE A EDITAR");
        btnMostrarClienteEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarClienteEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnMostrarClienteEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 240, 30));

        TbMostrarCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Tipo de Doc", "Nro de Doc", "Teléfono", "Dirección"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TbMostrarCliente);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 670, 70));

        txtTelefonoNuevo.setBorder(javax.swing.BorderFactory.createTitledBorder("Teléfono:"));
        getContentPane().add(txtTelefonoNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 370, 260, 43));

        txtDocumentoNuevo.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Documento o Ruc:"));
        getContentPane().add(txtDocumentoNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 260, 43));

        txtDireccionNueva.setBorder(javax.swing.BorderFactory.createTitledBorder("Dirección:"));
        getContentPane().add(txtDireccionNueva, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 620, 43));

        txtNombreNuevo.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre:"));
        getContentPane().add(txtNombreNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 371, 43));

        cbTipoNuevo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--SELECCIONE--", "DNI", "RUC" }));
        cbTipoNuevo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Documento"));
        getContentPane().add(cbTipoNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 200, -1));

        btnMostrarCamposEditar.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnMostrarCamposEditar.setText("COMENZAR A EDITAR");
        btnMostrarCamposEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarCamposEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnMostrarCamposEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 220, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMostrarClienteEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarClienteEditarActionPerformed

        buscarClientePorDocumento();
    }//GEN-LAST:event_btnMostrarClienteEditarActionPerformed

    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        String tipoAntiguo = cbTipo.getSelectedItem().toString().trim();
    String documentoAntiguo = txtDocumento.getText().trim();

    String nuevoNombre = txtNombreNuevo.getText().trim();
    String nuevoTipo = cbTipoNuevo.getSelectedItem().toString().trim();
    String nuevoDocumento = txtDocumentoNuevo.getText().trim();
    String nuevoTelefono = txtTelefonoNuevo.getText().trim();
    String nuevaDireccion = txtDireccionNueva.getText().trim();

    if (documentoAntiguo.isEmpty() || nuevoNombre.isEmpty() || nuevoDocumento.isEmpty() ||
        nuevoTelefono.isEmpty() || nuevaDireccion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
        return;
    }

    Object[] opciones = {"Sí", "No"};
    int confirmacion = JOptionPane.showOptionDialog(this,
        "¿Estás seguro de editar este cliente?", "Confirmación",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
        null, opciones, opciones[0]);

    if (confirmacion != JOptionPane.YES_OPTION) return;

    File archivo = new File("clientes.txt");
    File archivoTemp = new File("clientes_temp.txt");
    boolean actualizado = false;

    try (
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemp))
    ) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            if (datos.length >= 5 &&
                datos[1].equalsIgnoreCase(tipoAntiguo) &&
                datos[2].equalsIgnoreCase(documentoAntiguo)) {

                // Escribimos nueva línea actualizada
                String nuevaLinea = nuevoNombre + ";" + nuevoTipo + ";" + nuevoDocumento + ";" + nuevoTelefono + ";" + nuevaDireccion;
                bw.write(nuevaLinea);
                actualizado = true;
            } else {
                bw.write(linea);
            }
            bw.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al procesar el archivo: " + e.getMessage());
        return;
    }

    if (archivo.delete()) {
        if (archivoTemp.renameTo(archivo)) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            ((DefaultTableModel) TbMostrarCliente.getModel()).setRowCount(0); // Limpia la tabla
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo renombrar el archivo temporal.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo original. Verifique que no esté abierto.");
    }

    if (!actualizado) {
        JOptionPane.showMessageDialog(this, "No se encontró al cliente para editar.");
    }
    }//GEN-LAST:event_btnEditarClienteActionPerformed

    private void btnMostrarCamposEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarCamposEditarActionPerformed
         // Cargar datos desde la tabla a los campos nuevos
    int fila = TbMostrarCliente.getSelectedRow();
    if (fila >= 0) {
        txtNombreNuevo.setText(TbMostrarCliente.getValueAt(fila, 0).toString());
        cbTipoNuevo.setSelectedItem(TbMostrarCliente.getValueAt(fila, 1).toString());
        txtDocumentoNuevo.setText(TbMostrarCliente.getValueAt(fila, 2).toString());
        txtTelefonoNuevo.setText(TbMostrarCliente.getValueAt(fila, 3).toString());
        txtDireccionNueva.setText(TbMostrarCliente.getValueAt(fila, 4).toString());

        // Limpiamos la tabla para "desconectarla" del archivo
        ((DefaultTableModel) TbMostrarCliente.getModel()).setRowCount(0);
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione una fila de la tabla para editar.");
    }

        txtNombreNuevo.setVisible(true);
        cbTipoNuevo.setVisible(true);
        txtDocumentoNuevo.setVisible(true);
        txtTelefonoNuevo.setVisible(true);
        txtDireccionNueva.setVisible(true);
    }//GEN-LAST:event_btnMostrarCamposEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TbMostrarCliente;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnMostrarCamposEditar;
    private javax.swing.JButton btnMostrarClienteEditar;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JComboBox<String> cbTipoNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEliminar;
    private javax.swing.JTextField txtDireccionNueva;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtDocumentoNuevo;
    private javax.swing.JTextField txtNombreNuevo;
    private javax.swing.JTextField txtTelefonoNuevo;
    // End of variables declaration//GEN-END:variables
}
