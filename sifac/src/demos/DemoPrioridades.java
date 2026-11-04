package demos;

import java.util.*;

/**
 * 6.2 · por que no se debe confiar en el orden de ejecucion.
 *
 * El hilo h8 arranca de ULTIMO pero tiene prioridad MAXIMA.
 * El hilo h1 arranca de PRIMERO pero tiene prioridad MINIMA.
 * Si la prioridad decidiera, h8 llegaria siempre primero y h1 siempre ultimo.
 */
public class DemoPrioridades {

    static final int HILOS = 8;
    static final int RONDAS = 6;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("nucleos disponibles: " +
                Runtime.getRuntime().availableProcessors());
        System.out.println("h1 = prioridad MIN (" + Thread.MIN_PRIORITY + "), arranca primero");
        System.out.println("h8 = prioridad MAX (" + Thread.MAX_PRIORITY + "), arranca ultimo");
        System.out.println();

        int vecesQueGanoElMax = 0;
        Set<String> ordenesDistintos = new LinkedHashSet<>();

        for (int ronda = 1; ronda <= RONDAS; ronda++) {
            List<Integer> llegada = Collections.synchronizedList(new ArrayList<>());
            Thread[] hilos = new Thread[HILOS];

            for (int i = 0; i < HILOS; i++) {
                final int n = i + 1;
                hilos[i] = new Thread(() -> {
                    long s = 0;
                    for (int k = 0; k < 400_000; k++) s += k % 7;
                    if (s == -1) System.out.print("");   // evita que lo optimicen
                    llegada.add(n);
                }, "h" + n);
                hilos[i].setPriority(n == 1 ? Thread.MIN_PRIORITY
                                   : n == HILOS ? Thread.MAX_PRIORITY
                                   : Thread.NORM_PRIORITY);
            }
            for (Thread h : hilos) h.start();
            for (Thread h : hilos) h.join();

            StringBuilder sb = new StringBuilder();
            for (int n : llegada) sb.append(n).append(" ");
            String orden = sb.toString().trim();
            ordenesDistintos.add(orden);
            if (!llegada.isEmpty() && llegada.get(0) == HILOS) vecesQueGanoElMax++;

            System.out.printf("ronda %d -> %s%n", ronda, orden);
        }

        System.out.println();
        System.out.println("ordenes distintos observados : " + ordenesDistintos.size()
                           + " de " + RONDAS + " rondas");
        System.out.println("veces que h8 (MAX) llego 1ro : " + vecesQueGanoElMax
                           + " de " + RONDAS);
        System.out.println();
        System.out.println("La prioridad es una SUGERENCIA al planificador del sistema");
        System.out.println("operativo. No es una garantia de orden.");
    }
}
