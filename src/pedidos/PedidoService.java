package pedidos;

import excepciones.pedidos.CantidadPedidoInvalidaException;
import excepciones.pedidos.PedidoInexistenteException;
import excepciones.productos.StockInsuficienteException;
import excepciones.productos.StockInvalidoException;
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
    validarCantidadPedido(cantidad);
    validarPedidoExiste(idPedido);
    productoService.validarProductoExiste(idProducto);
    productoService.validarStockMayorA(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    Producto producto = productoService.buscarProductoPorId(idProducto);

    pedido.agregarProducto(producto, cantidad);
    productoService.restarStockProducto(idProducto, cantidad);
  }

  private void validarCantidadPedido(int cantidad) {
    if (cantidad < 0) {
      throw new CantidadPedidoInvalidaException("La cantidad del producto no puede ser negativa, ingrese otro valor válido.");
    }
  }

  public void eliminarProductoDelPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    validarProductoEstaEnPedido(idPedido, idProducto);
    productoService.validarProductoExiste(idProducto);
    validarCantidadEnPedidoMayorIgualA(idPedido, idProducto, cantidad);

    productoService.sumarStockProducto(idProducto, cantidad);
    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.quitarProducto(idProducto);

  }

  private void validarProductoEstaEnPedido(int idPedido, int idProducto) {
  }



  public void restarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    productoService.validarProductoExiste(idProducto);
    validarProductoEstaEnPedido(idPedido, idProducto);
    if (cantidad == getCantidadProductoEnPedido(idPedido, idProducto)) {
      eliminarProductoDelPedido(idPedido, idProducto, cantidad);
      return;
    }
    validarCantidadEnPedidoMayorIgualA(idPedido, idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.quitarCantidadProductoPedido(idProducto, cantidad);
    productoService.sumarStockProducto(idProducto, cantidad);
  }

  private void validarCantidadEnPedidoMayorIgualA(int idPedido, int idProducto, int cantidad) {
    int cantidadEnPedido = getCantidadProductoEnPedido(idPedido, idProducto);
    if (cantidadEnPedido < cantidad) {
      throw new StockInvalidoException("La cantidad que desea quitar no puede ser mayor a la del pedido.");
    }
  }

  public void sumarCantidadProductoPedido(int idPedido, int idProducto, int cantidad){
    validarPedidoExiste(idPedido);
    productoService.validarProductoExiste(idProducto);
    productoService.validarStockMayorA(idProducto, cantidad);

    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.agregarCantidadProductoPedido(idProducto, cantidad);
    productoService.restarStockProducto(idProducto, cantidad);
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

  public void mostrarDetallePedido(int idPedido){
    validarPedidoExiste(idPedido);
    Pedido pedido = buscarPedidoPorId(idPedido);
    pedido.mostrarDetallePedido();
  }

  private void validarPedidoExiste(int idPedido){
    if (buscarPedidoPorId(idPedido) == null) {
      new PedidoInexistenteException("No existe ningun pedido con el id "+ idPedido);
    }
  }

  private Pedido buscarPedidoPorId(int idPedido){
    return pedidos.stream().filter(p -> p.getId() == idPedido).findFirst().orElse(null);
  }

  private int getCantidadProductoEnPedido(int idPedido, int idProducto){
    validarPedidoExiste(idPedido);
    productoService.validarProductoExiste(idProducto);
    Pedido pedido = buscarPedidoPorId(idPedido);
    return pedido.getCantidadProductoEnPedido(idProducto);
  }

}
