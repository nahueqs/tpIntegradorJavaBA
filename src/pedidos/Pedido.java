package pedidos;

import excepciones.pedidos.PedidoInexistenteException;
import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoNoEstaEnPedidoException;
import excepciones.productos.StockInsuficienteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import productos.Producto;
import productos.ProductoService;

public class Pedido {

  private static AtomicInteger contadorPedidos = new AtomicInteger(0);


  private int id;
  private ArrayList<ProductoPedido> productos;
  private float total;
  private EstadoPedido estado;


  public Pedido() {
    this.id = contadorPedidos.incrementAndGet();
    this.productos = new ArrayList<ProductoPedido>();
    this.total = 0;
    this.estado = EstadoPedido.PENDIENTE;
  }

  public void agregarProducto(Producto producto, int cantidad) {
    ProductoPedido productoPedido = new ProductoPedido(producto, cantidad);
    this.productos.add(productoPedido);
    this.total += productoPedido.getSubtotal();
  }

  protected void verificarProductoEstaEnPedido(int idProducto) {
    ProductoPedido producto = this.productos.stream().filter(p -> p.getIdProducto() == idProducto).findFirst()
        .orElse(null);
    if (producto == null) {
      throw new ProductoNoEstaEnPedidoException("El producto con ID " + idProducto + " no existe en el pedido");
    }
  }

  public void quitarProducto(int idProducto) {
    verificarProductoEstaEnPedido(idProducto);

    for (ProductoPedido producto : productos) {
      if (producto.getIdProducto() == idProducto) {
        this.productos.remove(producto);
        this.total -= producto.getSubtotal();
        break;
      }
    }
  }

  private ProductoPedido getProductoPedidoEnProductos(int idProducto) {
    return this.productos.stream().filter(p -> p.getIdProducto() == idProducto).findFirst().orElse(null);
  }

  public void quitarCantidadProductoPedido(int idProducto, int cantidad) {
    verificarProductoEstaEnPedido(idProducto);
    ProductoPedido productoEnPedido = getProductoPedidoEnProductos(idProducto);
    productoEnPedido.restarCantidad(cantidad);
    this.total -= cantidad * productoEnPedido.getPrecioProducto();
  }

  public void agregarCantidadProductoPedido(int idProducto, int cantidad) {
    verificarProductoEstaEnPedido(idProducto);
    ProductoPedido productoEnPedido = getProductoPedidoEnProductos(idProducto);
    productoEnPedido.sumarCantidad(cantidad);
    this.total += cantidad * productoEnPedido.getPrecioProducto();
  }

  public void confirmarPedido() {
    this.estado = EstadoPedido.FINALIZADO;
  }

  public void cancelarPedido() {
    this.estado = EstadoPedido.CANCELADO;
  }

  public int getId() {
    return this.id;
  }

  public EstadoPedido getEstado() {
    return this.estado;
  }
}
