
package Proyecto;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class JRegistrarClientes extends javax.swing.JInternalFrame {

    /**
     * Creates new form JClientes
     */
    public JRegistrarClientes() {
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTelefono = new javax.swing.JTextField();
        txtDocumento = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        cbTipo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TbMostrarCliente = new javax.swing.JTable();

        setClosable(true);
        setResizable(true);
        setTitle("Registrar Nuevo Cliente");
        setToolTipText("");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTelefono.setBorder(javax.swing.BorderFactory.createTitledBorder("Teléfono:"));
        getContentPane().add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 260, 43));

        txtDocumento.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Documento o Ruc:"));
        getContentPane().add(txtDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 260, 43));

        txtDireccion.setBorder(javax.swing.BorderFactory.createTitledBorder("Dirección:"));
        getContentPane().add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 620, 43));

        btnAgregar.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Agregar.png"))); // NOI18N
        btnAgregar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_BOTTOM, new java.awt.Font("Segoe UI Black", 0, 14))); // NOI18N
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, 130, 100));

        txtNombre.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre:"));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 371, 43));

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--SELECCIONE--", "DNI", "RUC" }));
        cbTipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Documento"));
        getContentPane().add(cbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 70, 200, -1));

        jLabel1.setFont(new java.awt.Font("Swis721 Blk BT", 0, 24)); // NOI18N
        jLabel1.setText("INGRESE DATOS DE CLIENTE A REGISTRAR");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 580, 30));

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 670, 230));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void limpiarCampos() {
        txtNombre.setText("");
        cbTipo.setSelectedIndex(0);
        txtDocumento.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }
    
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
       
        String nombre = txtNombre.getText().trim();
        String tipo = cbTipo.getSelectedItem().toString();
        String documento = txtDocumento.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (nombre.isEmpty() || documento.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }
       
        Object[] opciones ={"Sí", "No"};
        int respuesta = JOptionPane.showOptionDialog(this,
                "¿Está seguro de que desea registrar este cliente?",
                "Confirmar Registro", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                opciones,opciones[0]);

        if (respuesta == JOptionPane.NO_OPTION) {
            return; 
        }

        
        Cliente existente = MantenimientoCliente.obtenerClientePorDocumento(documento);
        if (existente != null) {
            JOptionPane.showMessageDialog(this, "El cliente ya está registrado.");
            return;
        }

        try {
            File archivo = new File("clientes.txt");
            boolean archivoVacio = !archivo.exists() || archivo.length() == 0;

            try (FileWriter fw = new FileWriter(archivo, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {

                if (archivoVacio) {
                    pw.println("Nombre;Tipo;Documento;Teléfono;Dirección");
                }

                pw.println(nombre + ";" + tipo + ";" + documento + ";" + telefono + ";" + direccion);
            }

            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
            limpiarCampos();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
        }
    }//GEN-LAST:event_btnAgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TbMostrarCliente;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
