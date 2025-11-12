# Sistema de Control de Tráfico Urbano

Sistema de simulación de red vial que implementa el algoritmo A* para la optimización de rutas, desarrollado en Java con interfaz web interactiva.

## Características

- **Algoritmo A***: Búsqueda de caminos óptimos con heurística euclidiana
- **Red de tráfico dinámica**: Grid configurable de intersecciones con semáforos adaptativos
- **Visualización interactiva**: Interfaz web en tiempo real para planificación de rutas
- **Tipos de vías**: Calles (horizontales) y Avenidas (verticales) con multiplicadores diferenciados
- **Control de semáforos**: Ajuste automático de tiempos según carga de tráfico
- **Exportación de datos**: Generación de JSON y CSV para análisis

## Estructura del Proyecto

- [`Main.java`](Main.java): Punto de entrada, configuración inicial
- [`Controller.java`](Controller.java): Gestión del grafo y aristas
- [`TrafficController.java`](TrafficController.java): Implementación del algoritmo A*
- [`Interseccion.java`](Interseccion.java): Nodos de la red
- [`Arista.java`](Arista.java) / [`Calle.java`](Calle.java) / [`Avenida.java`](Avenida.java): Aristas del grafo
- [`Semaforo.java`](Semaforo.java): Control adaptativo de semáforos
- [`Vista.java`](Vista.java): Exportación de datos
- [`index.html`](index.html): Interfaz de visualización

## Uso

### Ejecución
```bash
javac *.java
java Main

### Interfaz Web
Abrir `index.html` en el navegador para:
- Seleccionar origen y destino
- Calcular rutas óptimas
- Ajustar flujo vehicular
- Visualizar congestión en tiempo real

## Configuración

Ver `config.json` para parámetros globales:
- `maxVehPerMin`: Capacidad máxima (vehículos/minuto)
- `multCalle`: Multiplicador de peso para calles
- `multAvenida`: Multiplicador de peso para avenidas

## Datos

- `red_vial.json`: Estado completo de la red
- `red_vial_ruta.json`: Incluye ruta calculada
- `red_vial.csv`: Formato tabular

