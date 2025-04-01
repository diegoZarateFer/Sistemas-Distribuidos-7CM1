import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Counter extends Remote {
    int incrementAndGet() throws RemoteException;
    int getCount() throws RemoteException;
}
