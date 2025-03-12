import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteSucursal {
    private static final int[] PUERTOS = {5001, 5002, 5003}; // Puertos de las sucursales

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el ID de la sucursal (0, 1, 2): ");
        int sucursalId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (sucursalId < 0 || sucursalId >= PUERTOS.length) {
            System.out.println("Sucursal inválida.");
            return;
        }

        try (Socket socket = new Socket("localhost", PUERTOS[sucursalId]);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Conectado a la sucursal " + sucursalId);

            System.out.println("Seleccione una acción: ");
            System.out.println("1. Consultar inventario");
            System.out.println("2. Comprar producto");
            System.out.println("3. Agregar producto");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    salida.writeObject("consultar");
                    salida.flush();
                    System.out.println("Inventario:\n" + entrada.readObject());
                    break;

                case 2:
                    System.out.print("Ingrese el ID del producto: ");
                    int productoIdCompra = scanner.nextInt();
                    System.out.print("Ingrese la cantidad a comprar: ");
                    int cantidadCompra = scanner.nextInt();

                    salida.writeObject("comprar");
                    salida.writeInt(productoIdCompra);
                    salida.writeInt(cantidadCompra);
                    salida.flush();

                    System.out.println("Respuesta: " + entrada.readObject());
                    break;

                case 3:
                    System.out.print("Ingrese el ID del producto: ");
                    int productoIdAgregar = scanner.nextInt();
                    System.out.print("Ingrese la cantidad a agregar: ");
                    int cantidadAgregar = scanner.nextInt();

                    salida.writeObject("agregar");
                    salida.writeInt(productoIdAgregar);
                    salida.writeInt(cantidadAgregar);
                    salida.flush();

                    System.out.println("Respuesta: " + entrada.readObject());
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
