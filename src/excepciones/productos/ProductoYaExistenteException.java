package excepciones.productos;

public class ProductoYaExistenteException extends RuntimeException {

  public ProductoYaExistenteException(String message) {
    super(message);
  }
}
