import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorTienda {
    public static void main(String[] args) {
        try {
            // Iniciar el registro RMI
            LocateRegistry.createRegistry(2000);

            // Crear la instancia de la tienda
            Tienda tienda = new TiendaImpl();

            // Registrar el objeto remoto
            Naming.rebind("rmi://localhost:2000/Tienda", tienda);

            System.out.println("Servidor de Tienda listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
