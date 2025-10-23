package excepciones.productos;

public class PrecioInvalidoException extends RuntimeException {

  public PrecioInvalidoException(String message) {
    super(message);
  }
}
