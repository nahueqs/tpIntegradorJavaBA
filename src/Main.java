import java.util.ArrayList;
import java.util.List;
import mainMenu.MainMenu;
import pedidos.PedidoService;
import productos.Producto;
import productos.ProductoService;

public class Main {

  public static void main(String[] args) {
    ArrayList<Producto> productos = new ArrayList<>(
        List.of(
            new Producto("Producto 1", 100, 10),
            new Producto("Producto 2", 200, 20),
            new Producto("Producto 3", 300, 30)
        )
    );

    ProductoService productoService = new ProductoService(productos);
    PedidoService pedidoService = new PedidoService(productoService);
    MainMenu menu = new MainMenu(productoService, pedidoService);
    menu.mostrarMenuPrincipal();
  }

}