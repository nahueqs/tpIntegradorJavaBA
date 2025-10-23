package pedidos;

import excepciones.productos.StockInsuficienteException;
import productos.Producto;

public class ProductoPedido {

  private Producto producto;
  private int cantidad;
  private float subtotal;

  public ProductoPedido(Producto producto, int cantidad) {
    this.producto = producto;
    this.cantidad = cantidad;
    this.subtotal = this.producto.getPrecio() * this.cantidad;
  }

  public void restarCantidad(int cantidad){
    if (this.cantidad < cantidad){
      throw new IllegalArgumentException("No se pueden quitar más productos de los que tiene el pedido ");
    }
    this.cantidad -= cantidad;
    this.subtotal = this.producto.getPrecio() * this.cantidad;
  }

  public void sumarCantidad(int cantidad){
    try {
      this.cantidad += cantidad;
      this.subtotal = this.producto.getPrecio() * this.cantidad;

    } catch (StockInsuficienteException e) {
      System.out.println(e.getMessage());
    }
  }

  public void mostrarDetalle(){
    System.out.println("ID Producto: " + getIdProducto() + " Cantidad: " + cantidad + " = $" + subtotal);
  }

  public int getIdProducto() {
    return producto.getId();
  }

  public float getPrecioProducto() {
    return producto.getPrecio();
  }

  public int getCantidad() {
    return cantidad;
  }

  public float getSubtotal() {
    return cantidad * producto.getPrecio();
  }
}
