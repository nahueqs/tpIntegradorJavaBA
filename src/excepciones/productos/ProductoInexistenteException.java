package excepciones.productos;

public class ProductoInexistenteException extends RuntimeException{
  public ProductoInexistenteException(String message) {
    super(message);
  }

}
