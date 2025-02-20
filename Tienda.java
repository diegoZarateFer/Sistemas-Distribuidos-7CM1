import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Producto {
    private int id;
    private int cantidad;
    private Lock lock = new ReentrantLock();

    public Producto(int id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public boolean comprar(int cantidadComprada) {
        lock.lock();
        try {
            if (cantidad >= cantidadComprada) {
                cantidad -= cantidadComprada;
                System.out.println(Thread.currentThread().getName() + " compró " + cantidadComprada + " unidades del producto " + id + ".");
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() + " intentó comprar " + cantidadComprada + " pero solo hay " + cantidad + " unidades del producto " + id + ".");
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
            System.out.println(Thread.currentThread().getName() + " agregó " + cantidadAgregada + " unidades al producto " +  id + ".");
        } finally {
            lock.unlock();
        }
    }

    public int getCantidad() {
        return cantidad;
    }
}

class Comprador implements Runnable {
    private Producto producto;
    private int cantidad;

    public Comprador(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        producto.comprar(cantidad);
    }
}

class Proveedor implements Runnable {
    private Producto producto;
    private int cantidad;

    public Proveedor(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        producto.agregar(cantidad);
    }
}

public class Tienda {
    public static void main(String[] args) {
        List<Producto> productos = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            productos.add(new Producto(i, random.nextInt(20) + 10));
        }

        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Producto producto = productos.get(random.nextInt(productos.size()));
            threads.add(new Thread(new Comprador(producto, random.nextInt(10) + 1), "Comprador " + i));
        }

        for (int i = 1; i <= 6; i++) {
            Producto producto = productos.get(random.nextInt(productos.size()));
            threads.add(new Thread(new Proveedor(producto, random.nextInt(15) + 5), "Proveedor " + i));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
