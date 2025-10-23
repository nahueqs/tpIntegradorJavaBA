package excepciones.pedidos;

public class CantidadPedidoInvalidaException extends RuntimeException {

  public CantidadPedidoInvalidaException(String message) {
    super(message);
  }
}
