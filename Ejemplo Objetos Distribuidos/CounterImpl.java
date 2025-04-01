import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CounterImpl extends UnicastRemoteObject implements Counter {
    private int count;

    protected CounterImpl() throws RemoteException {
        this.count = 0;
    }

    @Override
    public synchronized int incrementAndGet() throws RemoteException {
        count++;
        System.out.println("Nueva visita. Contador actualizado: " + count);
        return count;
    }

    @Override
    public int getCount() throws RemoteException {
        return count;
    }
}
