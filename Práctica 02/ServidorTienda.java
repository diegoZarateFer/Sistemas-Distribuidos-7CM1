import java.io.*;
import java.net.*;
import java.util.*;

class Producto {
    private int id;
    private int cantidad;

    public Producto(int id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public boolean comprar(int cantidadComprada) {
        if (cantidad >= cantidadComprada) {
            cantidad -= cantidadComprada;
            return true;
        }
        return false;
    }

    public void agregar(int cantidadAgregada) {
        cantidad += cantidadAgregada;
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
}

public class ServidorTienda {
    private static final int PUERTO = 5000;
    private static final Producto[] productos = new Producto[10];
    private static final String INVENTARIO_ARCHIVO = "inventario.txt";

    public static void main(String[] args) {
        cargarInventarioDesdeArchivo();
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor en ejecución...");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {
                    
                    String accion = (String) entrada.readObject();

                    if ("consultar".equalsIgnoreCase(accion)) {
                        StringBuilder inventario = new StringBuilder();
                        for (Producto p : productos) {
                            inventario.append(p.getId()).append(",").append(p.getCantidad()).append("\n");
                        }
                        salida.writeObject(inventario.toString());
                    } else {
                        int productoId = entrada.readInt();
                        int cantidad = entrada.readInt();
                        Producto producto = productos[productoId - 1];

                        if ("comprar".equalsIgnoreCase(accion)) {
                            boolean exito = producto.comprar(cantidad);
                            salida.writeObject(exito ? "Compra exitosa. Stock restante: " + producto.getCantidad() : "Stock insuficiente");
                        } else if ("agregar".equalsIgnoreCase(accion)) {
                            producto.agregar(cantidad);
                            salida.writeObject("Stock actualizado. Nuevo stock: " + producto.getCantidad());
                        } else {
                            salida.writeObject("Acción no válida");
                        }
                        guardarInventarioEnArchivo();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void cargarInventarioDesdeArchivo() {
        File archivo = new File(INVENTARIO_ARCHIVO);
        if (!archivo.exists()) {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                productos[i] = new Producto(i + 1, random.nextInt(20) + 10);
            }
            guardarInventarioEnArchivo();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTARIO_ARCHIVO))) {
            String linea;
            int i = 0;
            while ((linea = reader.readLine()) != null && i < 10) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    int id = Integer.parseInt(partes[0].trim());
                    int cantidad = Integer.parseInt(partes[1].trim());
                    productos[i++] = new Producto(id, cantidad);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void guardarInventarioEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTARIO_ARCHIVO))) {
            for (Producto p : productos) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}