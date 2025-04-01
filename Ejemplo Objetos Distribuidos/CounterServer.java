import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CounterServer {
    public static void main(String[] args) {
        try {
            Counter counter = new CounterImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("CounterService", counter);

            System.out.println("Servidor RMI listo...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
