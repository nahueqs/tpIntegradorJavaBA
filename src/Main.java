import mainMenu.MainMenu;
import pedidos.PedidoService;
import productos.ProductoService;

public class Main {

  public static void main(String[] args) {
    ProductoService productoService = new ProductoService();
    PedidoService pedidoService = new PedidoService(productoService);
    MainMenu menu = new MainMenu(productoService, pedidoService);
    menu.mostrarMenuPrincipal();
  }
}