package pedidos;

import excepciones.pedidos.PedidoInexistenteException;
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

  public void agregarProductoAlPedido(int idPedido, int idProducto, int cantidad){
    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);
    validarPuedoAgregarCantidadDeUnProducto(idProducto, cantidad);
    pedido.agregarProducto(producto, cantidad);
    restarStockProducto(idProducto, cantidad);
  }

  public void eliminarProductoDelPedido(int idPedido, int idProducto, int cantidad){
    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);
    validarPuedoRestarCantidadDeUnProducto(idProducto, cantidad);
    pedido.quitarProducto(idProducto);
    sumarStockProducto(idProducto, cantidad);
  }

  public void restarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);
    validarPuedoRestarCantidadDeUnProducto(idProducto, cantidad);
    pedido.quitarCantidadProductoPedido(idProducto, cantidad);
    sumarStockProducto(idProducto, cantidad);
  }

  public void sumarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);
    validarPuedoAgregarCantidadDeUnProducto(idProducto, cantidad);
    pedido.agregarCantidadProductoPedido(idProducto, cantidad);
    restarStockProducto(idProducto, cantidad);
  }

  public void confirmarPedido(int idPedido) {
    Pedido pedidoAConfirmar = buscarPedidoPorId(idPedido);
    if (pedidoAConfirmar.getEstado() == EstadoPedido.PENDIENTE) {
      pedidoAConfirmar.confirmarPedido();
    };
  }

  public void cancelarPedido(Pedido pedido){
    Pedido pedidoACancelar = buscarPedidoPorId(pedido.getId());
    if (pedidoACancelar.getEstado() == EstadoPedido.PENDIENTE) {




      pedidoACancelar.cancelarPedido();
    };

  }

  public void mostrarTodos() {
  }


  private Pedido buscarPedidoPorId(int idPedido){
    return pedidos.stream().filter(p -> p.getId() == idPedido).findFirst().orElseThrow(() ->
        new PedidoInexistenteException("No existe ningun pedido con el id"+ idPedido)
    );
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
