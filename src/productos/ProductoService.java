package productos;

import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoYaExistenteException;
import java.util.ArrayList;

public class ProductoService {
  private ArrayList<Producto> productos;
  private int cantidadTotalProductos;

  public ProductoService() {
    this.productos = new ArrayList<>();
    this.cantidadTotalProductos = 0;
  }

  public ProductoService(ArrayList<Producto> productos) {
    try {
      for (Producto producto : productos) {
      altaProducto(producto);
      }
      this.cantidadTotalProductos = productos.size();

    } catch (ProductoYaExistenteException e) {
      System.out.println(e.getMessage());
    }
  }

  public void altaProducto(Producto producto){
    if (buscarProductoPorId(producto.getId()) == null){
      productos.add(producto);
      cantidadTotalProductos++;
    } else {
      throw new ProductoYaExistenteException("El producto con ID " + producto.getId() + " ya existe");
    }
  }

  public void bajaProducto(int idProducto){
    Producto productoAQuitar = buscarProductoPorId(idProducto);
      productos.remove(productoAQuitar);
      cantidadTotalProductos = this.productos.size();
  }

  public void mostrarProductos(){
    for (Producto producto : productos) {
      producto.mostrarProducto();
    }
  }

  public int getStockProducto(int idProducto){
    return buscarProductoPorId(idProducto).getStock(idProducto);
  }

  public int getCantidadTotalProductos(){
    return cantidadTotalProductos;
  }

  public void actualizarPrecioProducto(int idProducto, float precio){
      buscarProductoPorId(idProducto).setPrecio(precio);
      System.out.println("Precio del producto id " + idProducto + " actualizado correctamente a " + precio);
  }

  public void sumarStockProducto(int idProducto, int cantidad){
      buscarProductoPorId(idProducto).sumarStock(cantidad);
      System.out.println("Se sumaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void restarStockProducto(int idProducto, int cantidad){
      buscarProductoPorId(idProducto).restarStock(cantidad);
      System.out.println("Se restaron " + cantidad + " unidades al stock del producto id " + idProducto);
  }

  public void actualizarNombreProducto(int idProducto, String nombre){
      buscarProductoPorId(idProducto).setNombre(nombre);
      System.out.println("Nombre del producto id " + idProducto + " actualizado correctamente a " + nombre);
  }


  public Producto buscarProductoPorId(int idProducto) {
    for (Producto producto : productos) {
      if (producto.getId() == idProducto) {
        System.out.println("Producto encontrado: ");
        producto.mostrarProducto();
        return producto;
      }
    }
    System.out.println("Producto no encontrado");
    throw new ProductoInexistenteException("No existe ningún producto con el id "+ idProducto);
  }

  public Producto buscarProductoPorPosicion(int posicion){
    if (posicion >= 0 && posicion < productos.size()){
      System.out.println("Producto encontrado: ");
      Producto productoEncontrado = productos.get(posicion);
      productoEncontrado.mostrarProducto();
      return productoEncontrado;
    }
    System.out.println("Producto no encontrado");
    throw new ProductoInexistenteException("No existe ningún producto con en la posicion" + posicion);
  }

}
