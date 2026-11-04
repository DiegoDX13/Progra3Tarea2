package demos;

/** 6.3 · las dos formas, y por que preferimos Runnable. */
public class DemoThreadVsRunnable {

    static class ComoThread extends Thread {
        public ComoThread(String n) { super(n); }
        @Override public void run() {
            System.out.println("  [" + getName() + "] extiende Thread");
        }
    }

    static class ComoRunnable implements Runnable {
        @Override public void run() {
            System.out.println("  [" + Thread.currentThread().getName() + "] implementa Runnable");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new ComoThread("forma-1");
        Thread b = new Thread(new ComoRunnable(), "forma-2");
        a.start(); a.join();
        b.start(); b.join();

        System.out.println();
        System.out.println("El error clasico: llamar run() en vez de start()");
        Thread c = new Thread(new ComoRunnable(), "forma-3");
        c.run();
        System.out.println("  ^ se ejecuto en " + Thread.currentThread().getName()
                           + ", no en forma-3. No se creo ningun hilo.");
        System.out.println("  estado de c -> " + c.getState());
    }
}
