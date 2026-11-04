# Resultados

Tiempos en milisegundos (ms).

| Hilos | Corrida 1 | Corrida 2 | Corrida 3 |  Promedio | Mejora |
|------:|----------:|----------:|----------:|----------:|-------:|
| 1     | 301896 ms | 301900 ms | 302029 ms |301941.67ms| —————— |
| 2     | 151062 ms | 150998 ms | 150920 ms |150993.33ms|  1.9   |
| 4     |  75522 ms |  75497 ms |  75527 ms | 75515.33ms|  3.99  |
| 8     |  37801 ms |  37797 ms |  37786 ms | 37794.67ms|  7.9   |
| 16    |  19652 ms |  19668 ms |  19701 ms | 19673.67ms| 15.35  |

*Mejora porcentual calculada respecto al promedio con 1 hilo.*

Pregunta 1 - Con 4 hilos, ¿la mejora fue de 4 veces? Si no, ¿por qué no?

    Con 4 hilos la mejora fue de 3.99, lo cual es una mejora de casi 4 veces. Esto concuerda con lo esperado.

Pregunta 2 - A partir de cuántos hilos deja de mejorar en su máquina, y qué cree que lo limita?

    A los 70 hilos todavia se mantiene una mejora con un tiempo de ~4000ms. Sin embargo al pasar a 71 hilos, el programa revienta con "Exception in thread "main" java.io.IOException: Fall� hilo-15". 
    
    La limitacion puede deberse al hardware en el que el programa esta siendo procesado, ya que mi dispositivo tiene 8GB de RAM y un CPU "Processor	12th Gen Intel(R) Core(TM) i3-1215U, 1200 Mhz, 6 Core(s), 8 Logical Processor(s)". Con un sistema mas poderoso, es posible que se puedan llegar a mas de 70 hilos a la vez.

Pregunta 3 - Sus hilos comparten el contador de encontradas. ¿Qué podría salir mal y por qué no le salió mal, o sí le salió?

    Podrian perderse actualizaciones, dos hilos podrian leer el mismo valor lo cual crearia un error en la informacion presentada

Pregunta 4 - Si el servidor tuviera un límite de 10 conexiones simultáneas, ¿qué cambiaría en su cliente?

    Si el servidor tuviera un limite de 10 conexiones, los demas tendrian que esperar su turno hasta que uno de los 10 hilos se libere


Pregunta 5 - Proponga una operación que el servidor de padrón debería tener y que hoy no tiene. Escriba su firma, describa en tres líneas qué problema real atiende, y diga qué dificultad traería implementarla con varios hilos. No la programe

    



