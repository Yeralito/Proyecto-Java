package Proyecto;

import java.io.*;
import javax.swing.JOptionPane;


public class MantenimientoFicha{
    
    private Producto producto;
    private int cantidad;

    public MantenimientoFicha(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    public static String buscarNombrePorTipoYDocumento(String tipo, String doc) {
    try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty() || linea.startsWith("Nombre;")) continue;
            String[] partes = linea.split(";");
            if (partes.length == 5 && partes[1].equalsIgnoreCase(tipo) && partes[2].equals(doc)) {
                return partes[0]; // devuelve solo el nombre
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar cliente por documento.");
    }
    return "Cliente no encontrado";
    }
    
    public static int leerContador() {
        int valor = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("contador.txt"))) {
            String linea = br.readLine();
            if (linea != null) {
                valor = Integer.parseInt(linea.trim());
            }
        } catch (IOException | NumberFormatException e) {
            // Archivo no existe o no es número válido: empieza en 0
            valor = 0;
        }
        return valor;
    }
    
    public static void guardarContador(int valor) {
    try (PrintWriter pw = new PrintWriter(new FileWriter("contador.txt"))) {
        pw.println(valor);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "No se pudo guardar el contador.");
        }
    }
    
    public static String buscarNombreProductoPorCodigo(String codigoBuscado) {
    try (BufferedReader br = new BufferedReader(new FileReader("productos.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty() || linea.startsWith("Nombre;")) continue;

            String[] partes = linea.split(";");
            // partes[0]=Nombre, partes[1]=Código, partes[2]=Precio, partes[3]=Stock
            if (partes.length == 4 && partes[1].equalsIgnoreCase(codigoBuscado)) {
                return partes[0]; // devuelve solo el nombre del producto
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar producto por código.");
    }

    return "Producto no encontrado";
    }
   
    public static String buscarPrecioPorCodigo(String codigo) {
    try (BufferedReader br = new BufferedReader(new FileReader("productos.txt"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty() || linea.startsWith("Nombre;")) continue;
            String[] partes = linea.split(";");
            if (partes.length == 4 && partes[1].equalsIgnoreCase(codigo)) {
                return partes[2]; // Precio
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar precio.");
    }
    return "Precio no encontrado";
    }
    
    public static double calcularPrecioTotal(int cantidad, double precio) {
        return cantidad * precio;
    }
    
    public static String buscarDireccionPorTipoYDocumento(String tipoDoc, String numDoc) {
    File archivo = new File("clientes.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length >= 5) {
                String tipo = partes[1];
                String documento = partes[2];
                String direccion = partes[4];

                if (tipo.equalsIgnoreCase(tipoDoc) && documento.equals(numDoc)) {
                    return direccion;
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo de clientes: " + e.getMessage());
    }

    return "Dirección no encontrada";
}
    
}
