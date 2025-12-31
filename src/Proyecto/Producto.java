package Proyecto;
import java.util.ArrayList;
import java.util.List;
public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    
     private static List<Producto> listaProductos = new ArrayList<>();

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static List<Producto> getListaProductos() {
        return listaProductos;
    }

    public static void setListaProductos(List<Producto> listaProductos) {
        Producto.listaProductos = listaProductos;
    }
     
     // Registrar un nuevo producto
    public static void registrarProducto(Producto p) {
        listaProductos.add(p);
        System.out.println("Producto registrado correctamente.");
    }

    // Eliminar producto por código
    public static boolean eliminarProducto(String codigo) {
        for (Producto p : listaProductos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                listaProductos.remove(p);
                System.out.println("Producto eliminado correctamente.");
                return true;
            }
        }
        System.out.println("Producto no encontrado.");
        return false;
    }

    // Mostrar todos los productos
    public static void mostrarProductos() {
        if (listaProductos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            System.out.println("Lista de productos:");
            for (Producto p : listaProductos) {
                System.out.println(p);
            }
        }
    }

    // Editar producto por código
    public static boolean editarProducto(String codigo, String nuevoNombre, double nuevoPrecio, int nuevoStock) {
        for (Producto p : listaProductos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                p.setNombre(nuevoNombre);
                p.setPrecio(nuevoPrecio);
                p.setStock(nuevoStock);
                System.out.println("Producto editado correctamente.");
                return true;
            }
        }
        System.out.println("Producto no encontrado.");
        return false;
    }

    // Representación en texto
    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock;
    }
    
    public String mostrarComoLineas() {
        return codigo + ";" + nombre + ";" + precio + ";" + stock;
    }
}
    
