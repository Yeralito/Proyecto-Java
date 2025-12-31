
package Proyecto;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class MantenimientoCliente {
    private static final String ARCHIVO = "clientes.txt";
    private static final String CABECERA = "Nombre;Tipo;Documento;Teléfono;Dirección";

    public static void guardarCliente(Cliente cliente) {
        try {
            File archivo = new File(ARCHIVO);
            boolean archivoVacio = !archivo.exists() || archivo.length() == 0;

            try (FileWriter fw = new FileWriter(archivo, true);
                 PrintWriter pw = new PrintWriter(fw)) {

                if (archivoVacio) {
                    pw.println(CABECERA);
                }

                pw.println(cliente.mostrarComoLinea());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente.");
        }
    }

    public static Cliente obtenerClientePorDocumento(String doc) {
    try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty() || linea.startsWith("Nombre;")) continue; // ignorar cabecera
            String[] partes = linea.split(";");
            if (partes.length == 5 && partes[2].equals(doc)) {
                return new Cliente(partes[0], partes[1], partes[2], partes[3], partes[4]);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar cliente por documento.");
    }
    return null;
}

    public static List<Cliente> obtenerTodos() {
    List<Cliente> lista = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty() || linea.startsWith("Nombre;")) continue; // ignorar cabecera
            String[] partes = linea.split(";");
            if (partes.length == 5) {
                Cliente c = new Cliente(partes[0], partes[1], partes[2], partes[3], partes[4]);
                lista.add(c);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al leer el archivo de clientes.");
    }
    return lista;
}

    public static boolean eliminarClientePorDocumento(String documento) {
        List<Cliente> lista = obtenerTodos();
        boolean eliminado = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(CABECERA); // volver a escribir cabecera
            for (Cliente c : lista) {
                if (!c.getDocumento().equals(documento)) {
                    pw.println(c.mostrarComoLinea());
                } else {
                    eliminado = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente.");
        }
        return eliminado;
    }

    public static boolean editarCliente(Cliente clienteActualizado) {
        List<Cliente> lista = obtenerTodos();
        boolean actualizado = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(CABECERA);
            for (Cliente c : lista) {
                if (c.getDocumento().equals(clienteActualizado.getDocumento())) {
                    pw.println(clienteActualizado.mostrarComoLinea());
                    actualizado = true;
                } else {
                    pw.println(c.mostrarComoLinea());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al editar cliente.");
        }

        return actualizado;
    }
}
