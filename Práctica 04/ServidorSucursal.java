import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class VectorClock {
    private final Map<Integer, Integer> vector;

    public VectorClock(int numSucursales) {
        vector = new HashMap<>();
        for (int i = 0; i < numSucursales; i++) {
            vector.put(i, 0);
        }
    }

    public synchronized void incrementar(int sucursalId) {
        vector.put(sucursalId, vector.get(sucursalId) + 1);
    }

    public synchronized void actualizar(VectorClock otro) {
        for (int key : vector.keySet()) {
            vector.put(key, Math.max(vector.get(key), otro.vector.getOrDefault(key, 0)));
        }
    }

    @Override
    public synchronized String toString() {
        return vector.toString();
    }
}

class Producto {
    private final int id;
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
            }
            return false;
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

    public static Producto fromString(String linea) {
        String[] partes = linea.split(",");
        return new Producto(Integer.parseInt(partes[0].trim()), Integer.parseInt(partes[1].trim()));
    }
}

public class ServidorSucursal {
    private static final int[] PUERTOS = {5001, 5002, 5003};
    private static final int SUCURSAL_ID;
    private static final String INVENTARIO_ARCHIVO;
    private static final List<Producto> productos = Collections.synchronizedList(new ArrayList<>());
    private static final VectorClock vectorClock = new VectorClock(PUERTOS.length);
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    static {
        int puerto = 5001; // Valor por defecto
        String puertoStr = System.getProperty("puerto");
        if (puertoStr != null) {
            puerto = Integer.parseInt(puertoStr);
        }
        SUCURSAL_ID = getSucursalId(puerto);
        if (SUCURSAL_ID == -1) {
            throw new IllegalArgumentException("Puerto inválido. Debe ser uno de los siguientes: " + Arrays.toString(PUERTOS));
        }
        INVENTARIO_ARCHIVO = "inventario_" + SUCURSAL_ID + ".txt";
    }

    private static int getSucursalId(int puerto) {
        for (int i = 0; i < PUERTOS.length; i++) {
            if (PUERTOS[i] == puerto) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        cargarInventario();
        try (ServerSocket serverSocket = new ServerSocket(PUERTOS[SUCURSAL_ID])) {
            System.out.println("Sucursal " + SUCURSAL_ID + " en ejecución en puerto " + PUERTOS[SUCURSAL_ID]);
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ClienteHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClienteHandler implements Runnable {
        private final Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {

                String accion = (String) entrada.readObject();

                if ("consultar".equalsIgnoreCase(accion)) {
                    salida.writeObject(productos.toString());
                    return;
                }

                int productoId = entrada.readInt();
                int cantidad = entrada.readInt();

                synchronized (productos) {
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
                            vectorClock.incrementar(SUCURSAL_ID);
                            salida.writeObject("Compra realizada. Stock restante: " + producto.getCantidad());
                        } else {
                            salida.writeObject("Compra fallida. Stock insuficiente.");
                        }
                    } else if ("agregar".equalsIgnoreCase(accion)) {
                        producto.agregar(cantidad);
                        vectorClock.incrementar(SUCURSAL_ID);
                        salida.writeObject("Stock actualizado. Nuevo stock: " + producto.getCantidad());
                    } else {
                        salida.writeObject("Acción no reconocida");
                    }

                    guardarInventario();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void cargarInventario() {
        File archivo = new File(INVENTARIO_ARCHIVO);
        if (!archivo.exists()) {
            inicializarInventario();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    productos.add(Producto.fromString(linea));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void inicializarInventario() {
        productos.add(new Producto(1, 10));
        productos.add(new Producto(2, 20));
        productos.add(new Producto(3, 30));
        guardarInventario();
    }

    private static void guardarInventario() {
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