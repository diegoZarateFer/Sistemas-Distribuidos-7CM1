import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CounterClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Counter counter = (Counter) registry.lookup("CounterService");

            int newCount = counter.incrementAndGet();
            System.out.println("Visitas actuales: " + newCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
