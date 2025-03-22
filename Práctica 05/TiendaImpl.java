import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TiendaImpl extends UnicastRemoteObject implements Tienda {
    private List<Producto> productos;

    public TiendaImpl() throws RemoteException {
        productos = new ArrayList<>();
    }

    @Override
    public void agregarProducto(Producto producto) throws RemoteException {
        productos.add(producto);
        System.out.println("Producto agregado: " + producto);
    }

    @Override
    public void comprarProducto(int id, int cantidad) throws RemoteException {
        for (Producto p : productos) {
            if (p.getId() == id && p.getCantidad() >= cantidad) {
                p.setCantidad(p.getCantidad() - cantidad);
                System.out.println("Producto comprado: " + p);
                return;
            }
        }
        System.out.println("Producto no disponible o cantidad insuficiente.");
    }

    @Override
    public List<Producto> obtenerProductos() throws RemoteException {
        return productos;
    }
}
