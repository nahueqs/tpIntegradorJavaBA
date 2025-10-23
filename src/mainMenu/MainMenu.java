package mainMenu;

import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoNoEstaEnPedidoException;
import excepciones.productos.StockInsuficienteException;
import java.util.Scanner;
import pedidos.Pedido;
import pedidos.PedidoService;
import productos.ProductoService;

public class MainMenu {

  private static ProductoService productoService;
  private static PedidoService pedidoService;

  public MainMenu(ProductoService productoService, PedidoService pedidoService) {
    this.productoService = productoService;
    this.pedidoService = pedidoService;
  }

  Scanner scanner = new Scanner(System.in);

  public void mostrarMenuPrincipal() {
    int opcionPrincipal = 0;

    do {
      System.out.println("\n=== MENÚ PRINCIPAL ===");
      System.out.println("1. Manejo de productos");
      System.out.println("2. Manejo de pedidos");
      System.out.println("3. Mostrar pedidos realizados");
      System.out.println("4. Salir");
      System.out.print("Seleccione una opción: ");

      opcionPrincipal = scanner.nextInt();
      scanner.nextLine(); // limpiar buffer

      switch (opcionPrincipal) {
        case 1 -> menuProductos(scanner);
        case 2 -> menuPedidos(scanner);
        case 3 -> System.out.println("Mostrando pedidos realizados...");
        case 4 -> System.out.println("Saliendo del sistema...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcionPrincipal != 4);

    scanner.close();
  }


  // =================== SUBMENÚ PRODUCTOS ===================
  private static void menuProductos(Scanner scanner) {
    int opcion;
    do {
      System.out.println("\n--- MANEJO DE PRODUCTOS ---");
      System.out.println("1. Alta producto");
      System.out.println("2. Mostrar producto");
      System.out.println("3. Buscar producto");
      System.out.println("4. Actualizar producto");
      System.out.println("5. Eliminar producto");
      System.out.println("6. Mostrar todos los productos");
      System.out.println("7. Volver al menú principal");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> menuAltaProducto(scanner);
        case 2 -> menuMostrarProducto(scanner);
        case 3 -> menuBuscarProducto(scanner);
        case 4 -> menuActualizarProducto(scanner);
        case 5 -> menuEliminarProducto(scanner);
        case 6 -> productoService.mostrarProductos();
        case 7 -> System.out.println("Volviendo al menú principal...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 6);
  }

  private static void menuActualizarProducto(Scanner scanner) {
  }

  private static void menuEliminarProducto(Scanner scanner) {

  }

  private static void menuBuscarProducto(Scanner scanner) {

  }

  private static void menuMostrarProducto(Scanner scanner) {
  }

  private static void menuAltaProducto(Scanner scanner) {
  }

  // =================== SUBMENÚ PEDIDOS ===================
  private static void menuPedidos(Scanner scanner) {
    int opcion;
    do {
      System.out.println("\n--- MANEJO DE PEDIDOS ---");
      System.out.println("1. Crear nuevo pedido");
      System.out.println("2. Mostrar todos los pedidos");
      System.out.println("3. Volver al menú principal");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> menuCrearPedido(scanner);
        case 2 -> pedidoService.mostrarTodos();
        case 3 -> System.out.println("Volviendo al menú principal...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 3);
  }

  // =================== SUBMENÚ CREAR PEDIDO ===================
  private static void menuCrearPedido(Scanner scanner) {
    Pedido nuevoPedido = pedidoService.crearPedido();

    int opcion;
    do {
      System.out.println("\n--- CREAR NUEVO PEDIDO ---");
      System.out.println("1. Agregar producto");
      System.out.println("2. Quitar producto");
      System.out.println("3. Mostrar detalle y subtotal del pedido");
      System.out.println("5. Finalizar pedido");
      System.out.println("6. Cancelar y volver al menú anterior: manejo de pedidos");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> menuAgregarProductoPedido(scanner, nuevoPedido);
        case 2 -> menuQuitarProductoPedido(scanner, nuevoPedido);
        case 3 -> menuMostrarDetallePedido(scanner, nuevoPedido);
        case 5 -> menuConfirmarPedido(scanner, nuevoPedido);
        case 6 -> System.out.println("Volviendo al menú de manejo de pedidos...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 6);

  }

  private static void menuQuitarProductoPedido(Scanner scanner, Pedido nuevoPedido) {
    int opcion;
    do {
      System.out.println("\n--- Quitar producto del pedido ---");
      System.out.println("1. Ingresar ID de producto");
      System.out.println("2. Ingresar posición del producto");
      System.out.println("3. Volver al menú anterior: crear pedido");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> {
          // leo el id del producto, luego verifico si está en el pedido y si la cantidad es correcta. si es asi lo elimino del pedido, sino muestro el error.
          // leo el id del producto, verifico si existe, luego pido la cantidad
          System.out.println("Ingrese el ID del pedido que desea agregar, o ingrese -1 para volver ");
          int idProducto = scanner.nextInt();
          scanner.nextLine();
          while (idProducto != -1) {
            System.out.println("Ingrese la cantidad de ese producto que desea quitar: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            try {
              pedidoService.eliminarProductoDelPedido(nuevoPedido.getId(), idProducto, cantidad);
            } catch (ProductoNoEstaEnPedidoException e) {
              System.out.println("Error al quitar producto al pedido: " + e.getMessage());
              System.out.println("Por favor, ingrese un ID de producto válido.");
            } catch (StockInsuficienteException e) {
              System.out.println("Error al quitar producto al pedido: " + e.getMessage());
              System.out.println("Por favor, ingrese una cantidad válida.");
            }

            System.out.println("Ingrese el ID de otro producto para quitar, o -1 para terminar: ");
            idProducto = scanner.nextInt();
            scanner.nextLine();
          }
        }
        case 2 -> {
          // leo la posicion del producto dentro del array, luego verifico si está en el pedido y si la cantidad es correcta. si es asi lo elimino del pedido, sino muestro el error.
        }
        case 3 -> System.out.println("Volviendo al menú de crear pedido...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 3);
  }

  private static void menuMostrarDetallePedido(Scanner scanner, Pedido nuevoPedido) {
  }

  private static void menuConfirmarPedido(Scanner scanner, Pedido nuevoPedido) {
    int opcion;
    do {
      System.out.println("\n--- Confirmar pedido ---");
      System.out.println("1. Confirmar pedido");
      System.out.println("2. Volver al menú anterior: crear pedido");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> {
          System.out.println("Excelente, confirmando pedido...");
          pedidoService.confirmarPedido(nuevoPedido.getId());
        }
        case 2 -> System.out.println("Volviendo al menú de crear pedido...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 2);

  }

  private static void menuAgregarProductoPedido(Scanner scanner, Pedido nuevoPedido) {

    System.out.println("\n--- Agregar producto al pedido ---");
    System.out.println("\n--- Ingresar ID de producto o ingresar - 1 para volver al menú anterior ---");

    // leo el id del producto, verifico si existe, luego pido la cantidad
    int idProducto = scanner.nextInt();
    scanner.nextLine();
    while (idProducto != -1) {
      System.out.println("Ingrese la cantidad de ese producto que desea agregar: ");
      int cantidad = scanner.nextInt();
      scanner.nextLine();

      try {
        pedidoService.agregarProductoAlPedido(nuevoPedido.getId(), idProducto, cantidad);
      } catch (ProductoInexistenteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese un ID de producto válido.");
      } catch (StockInsuficienteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese una cantidad válida.");
      }

      System.out.println("Ingrese el ID de otro producto para agregar, o -1 para terminar: ");
      idProducto = scanner.nextInt();
      scanner.nextLine();
    }
  }

}







