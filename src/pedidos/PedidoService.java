package pedidos;

import excepciones.pedidos.PedidoInexistenteException;
import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.StockInsuficienteException;
import java.util.ArrayList;
import productos.Producto;
import productos.ProductoService;

public class PedidoService {
  private static ProductoService productoService;
  private static ArrayList<Pedido> pedidos;

  public PedidoService(ProductoService productoService) {
    this.productoService = productoService;
    this.pedidos = new ArrayList<>();
  }

  public Pedido crearPedido(){
    Pedido nuevoPedido = new Pedido();
    pedidos.add(nuevoPedido);
    System.out.println("Nuevo pedido con id "+ nuevoPedido.getId() + " creado correctamente.");
    return nuevoPedido;
  }

  public void agregarProductoAlPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    validarProductoExiste(idProducto);
    validarPuedoAgregarCantidadDeUnProducto(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);

    pedido.agregarProducto(producto, cantidad);
    restarStockProducto(idProducto, cantidad);
  }

  public void eliminarProductoDelPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    validarProductoExiste(idProducto);
    validarPuedoRestarCantidadDeUnProducto(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.quitarProducto(idProducto);
    sumarStockProducto(idProducto, cantidad);
  }

  public void restarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    validarProductoExiste(idProducto);
    validarPuedoRestarCantidadDeUnProducto(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.quitarCantidadProductoPedido(idProducto, cantidad);
    sumarStockProducto(idProducto, cantidad);
  }

  public void sumarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    validarProductoExiste(idProducto);
    validarPuedoAgregarCantidadDeUnProducto(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.agregarCantidadProductoPedido(idProducto, cantidad);
    restarStockProducto(idProducto, cantidad);
  }

  public void confirmarPedido(int idPedido) {
    validarPedidoExiste(idPedido);
    Pedido pedidoAConfirmar = buscarPedidoPorId(idPedido);
    if (pedidoAConfirmar.getEstado() == EstadoPedido.PENDIENTE) {
      pedidoAConfirmar.confirmarPedido();
    } else {
      System.out.println("El pedido con id " + idPedido + " ya se encuentra confirmado.");
    };
  }

  public void mostrarTodos() {
    pedidos.forEach(p -> p.mostrarDetallePedido());
  }

  private void validarProductoExiste(int idProducto){
    productoService.validarProductoExiste(idProducto);
  }

  private void validarPedidoExiste(int idPedido){
    if (buscarPedidoPorId(idPedido) == null) {
      new PedidoInexistenteException("No existe ningun pedido con el id "+ idPedido);
    }
  }

  private Pedido buscarPedidoPorId(int idPedido){
    return pedidos.stream().filter(p -> p.getId() == idPedido).findFirst().orElse(null);
  }

  private void sumarStockProducto(int idProducto, int cantidad){
    productoService.sumarStockProducto(idProducto, cantidad);
  }

  private void restarStockProducto(int idProducto, int cantidad){
    productoService.restarStockProducto(idProducto, cantidad);
  }

  private void validarPuedoRestarCantidadDeUnProducto(int idProducto, int cantidad){
    if (productoService.getStockProducto(idProducto) < cantidad){
      throw new StockInsuficienteException("No hay stock suficiente para restar el producto id "+ idProducto +" al pedido");
    }
  }

  private void validarPuedoAgregarCantidadDeUnProducto(int idProducto, int cantidad){
    if (productoService.getStockProducto(idProducto) < cantidad){
      throw new StockInsuficienteException("No hay stock suficiente para agregar el producto id "+ idProducto +" al pedido");
    }
  }
}
