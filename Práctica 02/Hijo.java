import java.io.*;
import java.util.*;

public class Hijo implements Runnable {

    private PipedInputStream pipeInput;

    public Hijo(PipedInputStream pipeInput) {
        this.pipeInput = pipeInput;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(pipeInput));
            String comando;
            while ((comando = reader.readLine()) != null) {
                System.out.println("Hijo: Comando recibido: " + comando);
                if (comando.trim().equals("salir")) break;
                procesarComando(comando.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarComando(String comando) {
        switch (comando) {
            case "consultar":
                System.out.println("Hijo: Consultando inventario...");
                consultarInventario();
                break;
            case "agregar":
                System.out.println("Hijo: Agregando producto...");
                agregarProducto();
                break;
            case "modificar":
                System.out.println("Hijo: Modificando inventario...");
                modificarProducto();
                break;
            case "salir":
                System.out.println("Hijo: Saliendo...");
                break;
        }
    }

    private void consultarInventario() {
        System.out.println("Hijo: Inventario actual:");
        System.out.println("Producto ID: 101, Cantidad: 50");
        System.out.println("Producto ID: 102, Cantidad: 30");
        System.out.println("Producto ID: 103, Cantidad: 20");
    }

    private void agregarProducto() {
        System.out.println("Hijo: Ingrese el ID del producto a agregar:");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        System.out.println("Hijo: Ingrese la cantidad a agregar:");
        int cantidad = scanner.nextInt();
        System.out.println("Producto " + id + " agregado con " + cantidad + " unidades.");
    }

    private void modificarProducto() {
        System.out.println("Hijo: Ingrese el ID del producto a modificar:");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        System.out.println("Hijo: Ingrese la nueva cantidad:");
        int cantidad = scanner.nextInt();
        System.out.println("Producto " + id + " modificado a " + cantidad + " unidades.");
    }
}
