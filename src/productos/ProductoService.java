package productos;

import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoYaExistenteException;
import java.util.ArrayList;

public class ProductoService {

  private ArrayList<Producto> productos;

  public ProductoService() {
    this.productos = new ArrayList<>();
  }

  public ProductoService(ArrayList<Producto> productos) {
    this.productos = new ArrayList<>();
    try {
      for (Producto producto : productos) {
        altaProducto(producto);
      }

    } catch (ProductoYaExistenteException e) {
      System.out.println(e.getMessage());
    }
  }

  public void altaProducto(Producto producto) {
    validarProductoNoExiste(producto.getId());
    productos.add(producto);

  }

  public void bajaProducto(int idProducto) {
    validarProductoExiste(idProducto);
    Producto productoAQuitar = buscarProductoPorId(idProducto);
    productos.remove(productoAQuitar);
  }

  public void mostrarProductos() {
    for (Producto producto : productos) {
      producto.mostrarProducto();
    }
  }

  public int getStockProducto(int idProducto) {
    validarProductoExiste(idProducto);
    return buscarProductoPorId(idProducto).getStock(idProducto);
  }

  public void actualizarPrecioProducto(int idProducto, float precio) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).setPrecio(precio);
    System.out.println("Precio del producto id " + idProducto + " actualizado correctamente a " + precio);
  }

  public void sumarStockProducto(int idProducto, int cantidad) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).sumarStock(cantidad);
    System.out.println("Se sumaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void restarStockProducto(int idProducto, int cantidad) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).restarStock(cantidad);
    System.out.println("Se restaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void actualizarNombreProducto(int idProducto, String nombre) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).setNombre(nombre);
    System.out.println("Nombre del producto id " + idProducto + " actualizado correctamente a " + nombre);
  }

  public void validarProductoExiste(int idProducto) {
    if (buscarProductoPorId(idProducto) == null) {
      throw new ProductoInexistenteException("El producto con ID " + idProducto + " no existe");
    }
  }

  public void validarProductoNoExiste(int idProducto) {
    if (buscarProductoPorId(idProducto) != null) {
      throw new ProductoYaExistenteException("El producto con ID " + idProducto + " ya existe");
    }
  }

  public Producto buscarProductoPorId(int idProducto) {
    return productos.stream().filter(p -> p.getId() == idProducto).findFirst().orElse(null);
  }

  public Producto buscarProductoPorPosicion(int posicion) {
    if (posicion >= 0 && posicion < productos.size()) {
      System.out.println("Producto encontrado: ");
      Producto productoEncontrado = productos.get(posicion);
      productoEncontrado.mostrarProducto();
      return productoEncontrado;
    }
    System.out.println("Producto no encontrado");
    throw new ProductoInexistenteException("No existe ningún producto con en la posicion" + posicion);
  }

}
