
package Proyecto;


public class Cliente {
    private String nombre;
    private String tipo;
    private String documento;
    private String telefono;
    private String direccion;

    public Cliente() {
    }

    public Cliente(String nombre, String tipo, String documento, String telefono, String direccion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.documento = documento;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    
    public String mostrarComoLinea() {
        return nombre + ";" + tipo + ";" + documento + ";" + telefono + ";" + direccion;
    }

    public Object[] aFila() {
        return new Object[]{nombre, tipo, documento, telefono, direccion};
    }
}
