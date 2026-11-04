package demos;

/** 6.1 · los cinco estados de un hilo, observados desde afuera. */
public class DemoEstados {
    public static void main(String[] args) throws InterruptedException {

        Runnable tarea = () -> {
            try { Thread.sleep(400); } catch (InterruptedException e) { }
        };

        Thread h = new Thread(tarea, "consulta");

        System.out.println("recien creado, antes de start()  -> " + h.getState());
        h.start();
        System.out.println("inmediatamente despues de start()-> " + h.getState());
        Thread.sleep(150);
        System.out.println("mientras duerme                  -> " + h.getState());
        System.out.println("isAlive() al final               -> " + h.isAlive());
        h.join();
        System.out.println("despues de join()                -> " + h.getState());
        System.out.println();
        System.out.println("isAlive() al final               -> " + h.isAlive());
    }
}
