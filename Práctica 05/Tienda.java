import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Tienda extends Remote {
    void agregarProducto(Producto producto) throws RemoteException;
    void comprarProducto(int id, int cantidad) throws RemoteException;
    List<Producto> obtenerProductos() throws RemoteException;
}
