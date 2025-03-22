import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ClienteTienda {
    public static void main(String[] args) {
        try {
            // Conectar con el servidor RMI
            Tienda tienda = (Tienda) Naming.lookup("rmi://localhost:2000/Tienda");

            Scanner scanner = new Scanner(System.in);
            int opcion;

            do {
                // Mostrar menú de operaciones
                System.out.println("\n--- Menú de operaciones ---");
                System.out.println("1. Agregar producto");
                System.out.println("2. Ver productos disponibles");
                System.out.println("3. Comprar producto");
                System.out.println("4. Salir");
                System.out.print("Elija una opción: ");
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        // Agregar un producto
                        System.out.print("Ingrese el ID del producto: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Limpiar el buffer
                        System.out.print("Ingrese el nombre del producto: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingrese el precio del producto: ");
                        double precio = scanner.nextDouble();
                        System.out.print("Ingrese la cantidad disponible: ");
                        int cantidad = scanner.nextInt();

                        tienda.agregarProducto(new Producto(id, nombre, precio, cantidad));
                        System.out.println("Producto agregado exitosamente.");
                        break;

                    case 2:
                        // Ver los productos disponibles
                        List<Producto> productos = tienda.obtenerProductos();
                        System.out.println("\nProductos disponibles:");
                        for (Producto p : productos) {
                            System.out.println(p);
                        }
                        break;

                    case 3:
                        // Comprar producto
                        System.out.print("Ingrese el ID del producto a comprar: ");
                        int productoId = scanner.nextInt();
                        System.out.print("Ingrese la cantidad a comprar: ");
                        int cantidadCompra = scanner.nextInt();

                        tienda.comprarProducto(productoId, cantidadCompra);
                        System.out.println("Compra realizada.");
                        break;

                    case 4:
                        System.out.println("Saliendo...");
                        break;

                    default:
                        System.out.println("Opción no válida, intente de nuevo.");
                }

            } while (opcion != 4);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
