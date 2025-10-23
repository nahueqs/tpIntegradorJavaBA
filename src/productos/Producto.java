package productos;

import excepciones.productos.StockInsuficienteException;
import java.util.concurrent.atomic.AtomicInteger;

public class Producto {
  private static AtomicInteger contadorProductos = new AtomicInteger(0);
  private int id;

  private String nombre;
  private float precio;
  private int stock;

  public Producto(String nombre, float precio, int stock) {
    this.id = contadorProductos.incrementAndGet();
    this.nombre = nombre;
    this.precio = precio;
    this.stock = stock;
  }

  public void restarStock(int cantidad){
    if(stock >= cantidad){
      stock -= cantidad;
    } else {
      throw  new StockInsuficienteException("No hay stock suficiente para el producto " + nombre);
    }
  }

  public void sumarStock(int cantidad){
    if (cantidad > 0) {
      stock += cantidad;
      System.out.println("Se agregaron " + cantidad + " unidades al stock del producto id " + id);
    } else {
      throw new IllegalArgumentException("Para sumar al stock del producto, la cantidad debe ser mayor a 0");
    }
  }

  public void mostrarProducto(){
    System.out.println("ID: " + id + " Nombre: " + nombre + " Precio: " + precio + " Stock: " + stock);
  }

  public int getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public float getPrecio() {
    return precio;
  }

  public void setPrecio(float precio) {
    this.precio = precio;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }
}
