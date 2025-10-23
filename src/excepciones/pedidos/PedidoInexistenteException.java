package excepciones.pedidos;

public class PedidoInexistenteException extends RuntimeException {

  public PedidoInexistenteException(String message) {
    super(message);
  }
}
