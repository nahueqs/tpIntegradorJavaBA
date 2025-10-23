package excepciones.productos;

public class StockInvalidoException extends RuntimeException {

  public StockInvalidoException(String message) {
    super(message);
  }
}
