import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Producto {
    private int id;
    private int cantidad;
    private final Lock lock = new ReentrantLock();

    public Producto(int id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public boolean comprar(int cantidadComprada) {
        lock.lock();
        try {
            if (cantidad >= cantidadComprada) {
                cantidad -= cantidadComprada;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public void agregar(int cantidadAgregada) {
        lock.lock();
        try {
            cantidad += cantidadAgregada;
        } finally {
            lock.unlock();
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "," + cantidad;
    }

    public static Producto fromString(String data) {
        String[] parts = data.split(",");
        return new Producto(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}

public class ServidorTienda {
    private static final int PUERTO = 5000;
    private static final String ARCHIVO_INVENTARIO = "inventario.txt";
    private static final List<Producto> productos = new ArrayList<>();

    public static void main(String[] args) {
        cargarInventarioDesdeArchivo();

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor de tienda en ejecución en el puerto " + PUERTO);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                new Thread(new ManejadorCliente(clienteSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorCliente implements Runnable {
        private final Socket socket;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())
            ) {
                String accion = (String) entrada.readObject();

                if ("consultar".equalsIgnoreCase(accion)) {
                    StringBuilder inventario = new StringBuilder();
                    for (Producto p : productos) {
                        inventario.append("Producto ").append(p.getId()).append(": ")
                                .append(p.getCantidad()).append(" unidades\n");
                    }
                    salida.writeObject(inventario.toString());
                    return;
                }

                int productoId = entrada.readInt();
                int cantidad = entrada.readInt();

                Producto producto = productos.stream()
                    .filter(p -> p.getId() == productoId)
                    .findFirst()
                    .orElse(null);

                if (producto == null) {
                    salida.writeObject("Producto no encontrado");
                    return;
                }

                if ("comprar".equalsIgnoreCase(accion)) {
                    boolean exito = producto.comprar(cantidad);
                    if (exito) {
                        salida.writeObject("Compra realizada. Stock restante: " + producto.getCantidad());
                    } else {
                        salida.writeObject("Compra fallida. Stock insuficiente.");
                    }
                } else if ("agregar".equalsIgnoreCase(accion)) {
                    producto.agregar(cantidad);
                    salida.writeObject("Stock actualizado. Nuevo stock: " + producto.getCantidad());
                } else {
                    salida.writeObject("Acción no reconocida");
                }

                guardarInventarioEnArchivo();

                System.out.println("Estado actual del inventario:");
                for (Producto p : productos) {
                    System.out.println(p);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void cargarInventarioDesdeArchivo() {
        File archivo = new File(ARCHIVO_INVENTARIO);
        if (!archivo.exists()) {
            inicializarInventario();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                productos.add(Producto.fromString(linea));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void guardarInventarioEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_INVENTARIO))) {
            for (Producto p : productos) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void inicializarInventario() {
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            productos.add(new Producto(i, random.nextInt(20) + 10));
        }
        guardarInventarioEnArchivo();
    }
}
