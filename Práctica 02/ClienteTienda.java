import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTienda {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Bienvenido a la tienda.\n¿Qué desea hacer? (comprar / agregar / consultar): ");
            String accion = scanner.nextLine();
            salida.writeObject(accion);
            salida.flush();

            if ("consultar".equalsIgnoreCase(accion)) {
                String respuesta = (String) entrada.readObject();
                System.out.println("Inventario actual:\n" + respuesta);
            } else {
                System.out.println("Ingrese el ID del producto (1-10): ");
                int productoId = scanner.nextInt();
                System.out.println("Ingrese la cantidad: ");
                int cantidad = scanner.nextInt();
                
                salida.writeInt(productoId);
                salida.writeInt(cantidad);
                salida.flush();

                String respuesta = (String) entrada.readObject();
                System.out.println("Respuesta del servidor: " + respuesta);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}