import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Padre {

    private static final Map<Integer, Integer> inventario = new HashMap<>();

    public static void main(String[] args) {
        PipedInputStream pipeInput = new PipedInputStream();
        PipedOutputStream pipeOutput = new PipedOutputStream();

        try {
            pipeInput.connect(pipeOutput);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(new Hijo(pipeInput));

            inventario.put(101, 50);
            inventario.put(102, 30);
            inventario.put(103, 20);

            enviarInventario(pipeOutput);

            while (true) {
                String comando = generarComandoAleatorio();
                System.out.println("Padre: Enviando comando: " + comando);
                pipeOutput.write(comando.getBytes());
                pipeOutput.flush();

                if (comando.equals("salir\n")) break;

                Thread.sleep(1000);
            }

            pipeOutput.close();
            executor.shutdown();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void enviarInventario(PipedOutputStream pipeOutput) throws IOException {
        for (Map.Entry<Integer, Integer> entry : inventario.entrySet()) {
            String producto = "Producto ID: " + entry.getKey() + ", Cantidad: " + entry.getValue() + "\n";
            pipeOutput.write(producto.getBytes());
            pipeOutput.flush();
        }
    }

    private static String generarComandoAleatorio() {
        Random random = new Random();
        int comando = random.nextInt(4);

        switch (comando) {
            case 0: return "consultar\n";
            case 1: return "agregar\n";
            case 2: return "modificar\n";
            case 3: return "salir\n";
            default: return "consultar\n";
        }
    }
}
