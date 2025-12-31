/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Proyecto;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author cuent
 */
public class MantenimientoProducto {
    private static final String ARCHIVO = "productos.txt";
    private static final String CABECERA = "Código;Nombre;Precio;Stock";

    public static void guardarProducto(Producto producto) {
        try {
            File archivoP = new File(ARCHIVO);
            boolean archivoVacio = !archivoP.exists() || archivoP.length() == 0;

            try (FileWriter fw = new FileWriter(archivoP, true);
                 PrintWriter pw = new PrintWriter(fw)) {

                if (archivoVacio) {
                    pw.println(CABECERA);
                }

                pw.println(producto.mostrarComoLineas());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar producto.");
        }
    }

    public static Producto obtenerProductoPorCodigo(String codigo) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("Código;")) continue;
                String[] partes = linea.split(";");
                if (partes.length == 4 && partes[0].equals(codigo)) {
                    String cod = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    return new Producto(cod, nombre, precio, stock);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto por código.");
        }
        return null;
    }

    public static List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("Código;")) continue;
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String cod = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    Producto p = new Producto(cod, nombre, precio, stock);
                    lista.add(p);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de productos.");
        }
        return lista;
    }

    public static boolean eliminarProductoPorCodigo(String codigo) {
        List<Producto> lista = obtenerTodos();
        boolean eliminado = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(CABECERA); // reescribir cabecera
            for (Producto p : lista) {
                if (!p.getCodigo().equals(codigo)) {
                    pw.println(p.mostrarComoLineas());
                } else {
                    eliminado = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto.");
        }
        return eliminado;
    }

    public static boolean editarProducto(Producto productoActualizado) {
        List<Producto> lista = obtenerTodos();
        boolean actualizado = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(CABECERA);
            for (Producto p : lista) {
                if (p.getCodigo().equals(productoActualizado.getCodigo())) {
                    pw.println(productoActualizado.mostrarComoLineas());
                    actualizado = true;
                } else {
                    pw.println(p.mostrarComoLineas());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al editar producto.");
        }

        return actualizado;
    }
}
