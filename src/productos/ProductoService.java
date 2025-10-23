package productos;

import excepciones.productos.PrecioInvalidoException;
import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoYaExistenteException;
import excepciones.productos.StockInsuficienteException;
import excepciones.productos.StockInvalidoException;
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
        altaProducto(producto.getNombre(), producto.getPrecio(), producto.getStock());
      }

    } catch (ProductoYaExistenteException e) {
      System.out.println(e.getMessage());
    }
  }

  public void altaProducto(String nombre, float precio, int cantidad) {
    validarProductoConMismoNombreNoExiste(nombre);
    validarPrecioNuevoProducto(precio);
    validarCantidadNuevoProducto(cantidad);
    Producto nuevoProducto = new Producto(nombre, precio, cantidad);
    productos.add(nuevoProducto);
    System.out.println("Producto creado exitosamente...");
    nuevoProducto.mostrarProducto();
  }

  public void bajaProducto(int idProducto) {
    validarProductoExiste(idProducto);
    Producto productoAQuitar = buscarProductoPorId(idProducto);
    productos.remove(productoAQuitar);
    System.out.println("Producto eliminado exitosamente...");
  }

  public void actualizarPrecioProducto(int idProducto, float precio) {
    validarProductoExiste(idProducto);
    validarPrecioNuevoProducto(precio);
    buscarProductoPorId(idProducto).setPrecio(precio);
    System.out.println("Precio del producto id " + idProducto + " actualizado correctamente a " + precio);
  }

  public void actualizarNombreProducto(int idProducto, String nombre) {
    validarProductoExiste(idProducto);
    validarProductoConMismoNombreNoExiste(nombre);
    buscarProductoPorId(idProducto).setNombre(nombre);
    System.out.println("Nombre del producto id " + idProducto + " actualizado correctamente a " + nombre);
  }

  public void actualizarStockProducto(int idProducto, int stock) {
    validarProductoExiste(idProducto);
    validarCantidadNuevoProducto(stock);
    buscarProductoPorId(idProducto).setStock(stock);
    System.out.println("Stock del producto id " + idProducto + " actualizado correctamente a " + stock);
  }

  public void mostrarProductos() {
    for (Producto producto : productos) {
      producto.mostrarProducto();
    }
  }

  public int getStockProducto(int idProducto) {
    validarProductoExiste(idProducto);
    return buscarProductoPorId(idProducto).getStock();
  }

  public void sumarStockProducto(int idProducto, int cantidad) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).sumarStock(cantidad);
    System.out.println("Se sumaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void restarStockProducto(int idProducto, int cantidad) {
    validarProductoExiste(idProducto);
    validarStockMayorA(idProducto, cantidad);
    buscarProductoPorId(idProducto).restarStock(cantidad);
    System.out.println("Se restaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void validarStockMayorA(int idProducto, int cantidad) {
    if (getStockProducto(idProducto) < cantidad) {
      throw new StockInsuficienteException("No hay stock suficiente para restar el producto id " + idProducto + " al pedido");
    }
  }

  private void validarProductoConMismoNombreNoExiste(String nombre) {
    if (buscarProductoPorNombre(nombre) != null) {
      throw new ProductoYaExistenteException("El producto con nombre " + nombre + " ya existe");
    }
  }

  private void validarCantidadNuevoProducto(int cantidad) {
    if (cantidad < 0) {
      throw new StockInvalidoException("No se puede crear un producto con stock negativo");
    }
  }

  private void validarPrecioNuevoProducto(float precio) {
    if (precio < 0) {
      throw new PrecioInvalidoException("No se puede crear un producto con stock negativo");
    }

  }
  public void validarProductoExiste(int idProducto) {
    if (buscarProductoPorId(idProducto) == null) {
      throw new ProductoInexistenteException("El producto con ID " + idProducto + " no existe");
    }
  }

  public void validarProductoExistePorNombre(String nombre) {
    if (buscarProductoPorNombre(nombre) == null) {
      throw new ProductoInexistenteException("El producto con el nombre " + nombre + " no existe");
    }
  }


  public Producto buscarProductoPorNombre(String nombre) {
    return productos.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().orElse(null);
  }

  public Producto buscarProductoPorId(int idProducto) {
    return productos.stream().filter(p -> p.getId() == idProducto).findFirst().orElse(null);
  }

  public Producto buscarProductoPorPosicion(int posicion) {
    if (posicion >= 0 && posicion < productos.size()) {
      System.out.println("Producto encontrado: ");
      Producto productoEncontrado = productos.get(posicion);
      return productoEncontrado;
    }
    throw new ProductoInexistenteException("No existe ningún producto con en la posicion" + posicion);
  }

  public void mostrarProductoPorId(int idProducto) {
    validarProductoExiste(idProducto);
    buscarProductoPorId(idProducto).mostrarProducto();
  }

  public void mostrarProductoPorPosicion(int posicion) {
    buscarProductoPorPosicion(posicion).mostrarProducto();
  }

  public void mostrarProductoPorNombre(String nombre) {
    validarProductoExistePorNombre(nombre);
    buscarProductoPorNombre(nombre).mostrarProducto();
  }



}
