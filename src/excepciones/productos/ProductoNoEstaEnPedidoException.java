package excepciones.productos;

public class ProductoNoEstaEnPedidoException extends RuntimeException {

  public ProductoNoEstaEnPedidoException(String message) {
    super(message);
  }
}
