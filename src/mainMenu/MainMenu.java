package mainMenu;

import excepciones.pedidos.CantidadPedidoInvalidaException;
import excepciones.productos.PrecioInvalidoException;
import excepciones.productos.ProductoInexistenteException;
import excepciones.productos.ProductoNoEstaEnPedidoException;
import excepciones.productos.ProductoYaExistenteException;
import excepciones.productos.StockInsuficienteException;
import excepciones.productos.StockInvalidoException;
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
    int opcionPrincipal;

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
      System.out.println("3. Actualizar producto");
      System.out.println("4. Eliminar producto");
      System.out.println("5. Mostrar todos los productos");
      System.out.println("6. Volver al menú principal");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> menuAltaProducto(scanner);
        case 2 -> menuMostrarProducto(scanner);
        case 3 -> menuActualizarProducto(scanner);
        case 4 -> menuEliminarProducto(scanner);
        case 5 -> productoService.mostrarProductos();
        case 6 -> System.out.println("Volviendo al menú principal...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 6);
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
      System.out.println("3. Agregar más de un producto del pedido");
      System.out.println("4. Quitar cantidad de un producto del pedido");
      System.out.println("5. Mostrar detalle y subtotal del pedido");
      System.out.println("6. Confirmar pedido");
      System.out.println("7. Volver al menú anterior");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> menuAgregarNuevoProductoPedido(scanner, nuevoPedido);
        case 2 -> menuQuitarProductoPedido(scanner, nuevoPedido);
        case 3 -> menuAgregarUnidadesProductoPedido(scanner, nuevoPedido);
        case 4 -> menuQuitarUnidadesProductoPedido(scanner, nuevoPedido);
        case 5 -> pedidoService.mostrarDetallePedido(nuevoPedido.getId());
        case 6 -> menuConfirmarPedido(scanner, nuevoPedido);
        case 7 -> System.out.println("Volviendo al menú...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }

    } while (opcion != 6);

  }

  private static void menuQuitarUnidadesProductoPedido(Scanner scanner, Pedido nuevoPedido) {
    System.out.println("\n--- Quitar unidades de un producto del pedido ---");
    nuevoPedido.mostrarDetallePedido();

    System.out.println("1. Ingresar ID de producto o ingrese -1 para salir");

    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    while (idProducto != -1) {

      int cantidad = mostrarSubmenuIngreseCantidad(scanner);

      try {
        pedidoService.restarCantidadProductoPedido(nuevoPedido.getId(), idProducto, cantidad);
        System.out.println("Se quitaron las unidades deseadas del pedido. Ingrese id de un producto para continuar quitando unidades o -1 para salir.");
      } catch (ProductoNoEstaEnPedidoException e) {
        System.out.println("Error al quitar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese un ID de producto válido.");
      } catch (StockInsuficienteException e) {
        System.out.println("Error al quitar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese una cantidad válida.");
      }

      idProducto = mostrarSubmenuPedirIdProducto(scanner);
    }
    System.out.println("Volviendo al menú de crear pedido...");
  }

  private static void menuAgregarUnidadesProductoPedido(Scanner scanner, Pedido nuevoPedido) {
    nuevoPedido.mostrarDetallePedido();

    System.out.println("\n--- Agregar unidades de un producto al pedido ---");
    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    while (idProducto != -1) {

      int cantidad = mostrarSubmenuIngreseCantidad(scanner);

      try {
        pedidoService.sumarCantidadProductoPedido(nuevoPedido.getId(), idProducto, cantidad);
        System.out.println("Unidades del producto agregadas correctamente. Ingrese id de un producto para continuar agregando unidades o -1 para salir.");
        nuevoPedido.mostrarDetallePedido();
      } catch (ProductoInexistenteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");
      } catch (StockInsuficienteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");

      } catch (CantidadPedidoInvalidaException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");
      }

      idProducto = mostrarSubmenuPedirIdProducto(scanner);
    }

  }

  private static void menuQuitarProductoPedido(Scanner scanner, Pedido nuevoPedido) {

    System.out.println("\n--- Quitar producto del pedido ---");
    System.out.println("1. Ingresar ID de producto o ingrese -1 para salir");

    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    while (idProducto != -1) {

      int cantidad = mostrarSubmenuIngreseCantidad(scanner);

      try {
        pedidoService.eliminarProductoDelPedido(nuevoPedido.getId(), idProducto, cantidad);
        System.out.println("Producto quitado del pedido.");
      } catch (ProductoNoEstaEnPedidoException e) {
        System.out.println("Error al quitar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese un ID de producto válido.");
      } catch (StockInsuficienteException e) {
        System.out.println("Error al quitar producto al pedido: " + e.getMessage());
        System.out.println("Por favor, ingrese una cantidad válida.");
      }

      idProducto = mostrarSubmenuPedirIdProducto(scanner);
    }
    System.out.println("Volviendo al menú de crear pedido...");
  }

  private static int mostrarSubmenuIngreseCantidad(Scanner scanner) {
    System.out.println("Ingrese la cantidad: ");
    int cantidad = scanner.nextInt();
    scanner.nextLine();
    return cantidad;
  }

  private static void menuConfirmarPedido(Scanner scanner, Pedido nuevoPedido) {
    System.out.println("\n--- Confirmar pedido ---");
    System.out.println("1. Confirmar pedido");
    System.out.println("2. Volver al menú anterior: crear pedido");
    System.out.print("Seleccione una opción: ");

    int opcion = scanner.nextInt();
    scanner.nextLine();

    if (opcion == 1) {
      pedidoService.confirmarPedido(nuevoPedido.getId());
      System.out.println("Excelente, confirmando pedido...");
      return;
    }
    System.out.println("Volviendo al menú de crear pedido...");
  }

  private static void menuAgregarNuevoProductoPedido(Scanner scanner, Pedido nuevoPedido) {
    productoService.mostrarProductos();
    System.out.println("\n--- Agregar producto al pedido ---");
    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    while (idProducto != -1) {

      int cantidad = mostrarSubmenuIngreseCantidad(scanner);

      try {
        pedidoService.agregarProductoAlPedido(nuevoPedido.getId(), idProducto, cantidad);
        System.out.println("Producto agregado al pedido.");
        nuevoPedido.mostrarDetallePedido();
      } catch (ProductoInexistenteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");
      } catch (StockInsuficienteException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");

      } catch (CantidadPedidoInvalidaException e) {
        System.out.println("Error al agregar producto al pedido: " + e.getMessage());
        System.out.println("Reiniciando agregar producto...");
      }

      idProducto = mostrarSubmenuPedirIdProducto(scanner);
    }
  }

  private static String menuSubmenuPedirNombreProducto(Scanner scanner) {
    System.out.println("\n--- Ingresar nombre o ingresa -1 para salir ---");
    return scanner.nextLine();
  }


  private static void menuAltaProducto(Scanner scanner) {
    System.out.println("\n--- Dar de alta producto ---");

    String nombre = menuSubmenuPedirNombreProducto(scanner);

    while (nombre != "-1") {

      float precio = mostrarSubmenuIngresePrecio(scanner);

      int cantidad = mostrarSubmenuIngreseCantidad(scanner);

      try {
        productoService.altaProducto(nombre, precio, cantidad);
      } catch (ProductoYaExistenteException e) {
        System.out.println(e.getMessage());
        System.out.println("Reiniciando alta producto...");
      } catch (PrecioInvalidoException e) {
        System.out.println(e.getMessage());
        System.out.println("Reiniciando alta producto...");
      } catch (StockInvalidoException e) {
        System.out.println(e.getMessage());
        System.out.println("Reiniciando alta producto...");
      }

      nombre = menuSubmenuPedirNombreProducto(scanner);
    }
  }

  private static float mostrarSubmenuIngresePrecio(Scanner scanner) {
    System.out.println("Ingrese el precio: ");
    return scanner.nextFloat();
  }

  private static void menuActualizarProducto(Scanner scanner) {
    System.out.println("\n--- Actualizar datos de un producto  ---");
    System.out.println("Ingresar el id del producto o -1 para salir: ");

    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    if (idProducto != -1) {
      try {
        productoService.mostrarProductoPorId(idProducto);
        mostrarSubmenuModificarProducto(idProducto, scanner);
      } catch (ProductoInexistenteException e) {
        System.out.println(e.getMessage());
      }
    }
    System.out.println("Volviendo al menú principal...");
  }

  private static void mostrarSubmenuModificarProducto(int idProducto, Scanner scanner) {
    int opcion;
    do {
      System.out.println("\n--- Actualizar datos de un producto  ---");
      System.out.println("1. Modificar nombre: ");
      System.out.println("2. Modificar precio: ");
      System.out.println("3. Modificar stock: ");
      System.out.println("4. Volver al menu anterior: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> {
          String nombre = menuSubmenuPedirNombreProducto(scanner);
          try {
            productoService.actualizarNombreProducto(idProducto, nombre);
          } catch (ProductoYaExistenteException e) {
            System.out.println(e.getMessage());
            System.out.println("Volviendo al menú de actualizar producto...");
          }
        }

        case 2 -> {
          float precio = mostrarSubmenuIngresePrecio(scanner);
          try {
            productoService.actualizarPrecioProducto(idProducto, precio);
          } catch (PrecioInvalidoException e) {
            System.out.println(e.getMessage());
            System.out.println("Volviendo al menú de actualizar producto...");
          }
        }

        case 3 -> {
          int stock = mostrarSubmenuIngreseCantidad(scanner);
          try {
            productoService.actualizarStockProducto(idProducto, stock);
          } catch (StockInvalidoException e) {
            System.out.println(e.getMessage());
            System.out.println("Volviendo al menú de actualizar producto...");
          }
        }

        case 4 -> System.out.println("Volviendo al menú de actualizar producto...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }
    } while (opcion != 4);
  }


  private static void menuEliminarProducto(Scanner scanner) {
    System.out.println("\n--- Eliminar un producto  ---");
    int idProducto = mostrarSubmenuPedirIdProducto(scanner);

    if (idProducto != -1) {
      try {
        productoService.mostrarProductoPorId(idProducto);
        mostrarConfirmacionEliminarProducto(idProducto, scanner);
      } catch (ProductoInexistenteException e) {
        System.out.println(e.getMessage());
      }
    }
    System.out.println("Volviendo al menú principal...");
  }

  private static int mostrarSubmenuPedirIdProducto(Scanner scanner) {
    System.out.println("Ingrese el id del producto o -1 para volver al menú anterior: ");
    int idProducto = scanner.nextInt();
    scanner.nextLine();
    return idProducto;
  }


  private static void mostrarConfirmacionEliminarProducto(int idProducto, Scanner scanner) {
    System.out.println(
        "¿Está seguro de que desea eliminar este producto? ingrese 1 para confirmar o cualquier otro número para cancelar.");
    int opcion = scanner.nextInt();
    scanner.nextLine();
    if (opcion == 1) {
      productoService.bajaProducto(idProducto);
      System.out.println("Producto eliminado correctamente.");
    } else {
      System.out.println("Cancelando operación, volviendo al menú de eliminar producto...");
    }
  }

  private static void menuMostrarProducto(Scanner scanner) {
    int opcion;
    do {
      System.out.println("\n--- Buscar y mostrar producto  ---");
      System.out.println("1. Buscar producto por id");
      System.out.println("2. Buscar producto por posición");
      System.out.println("3. Buscar producto por nombre");
      System.out.println("4. Salir al menú anterior");
      System.out.print("Seleccione una opción: ");

      opcion = scanner.nextInt();
      scanner.nextLine();

      switch (opcion) {
        case 1 -> {

          int idProducto = mostrarSubmenuPedirIdProducto(scanner);
          try {
            productoService.mostrarProductoPorId(idProducto);
          } catch (ProductoInexistenteException e) {
            System.out.println(e.getMessage());
          }
        }
        case 2 -> {
          System.out.println("Ingrese la posicion del producto: ");

          int posProducto = scanner.nextInt();
          scanner.nextLine();
          try {
            productoService.mostrarProductoPorPosicion(posProducto);
          } catch (ProductoInexistenteException e) {
            System.out.println(e.getMessage());
          }
        }
        case 3 -> {
          System.out.println("Ingrese el nombre del producto: ");
          String nombreProducto = scanner.nextLine();
          try {
            productoService.mostrarProductoPorNombre(nombreProducto);
          } catch (ProductoInexistenteException e) {
            System.out.println(e.getMessage());
          }
        }
        case 4 -> System.out.println("Volviendo al menú de crear pedido...");
        default -> System.out.println("Opción inválida. Intente nuevamente.");
      }
    } while (opcion != 4);
  }

}













