import { Controller } from './Controller.js';
import { Vista } from './Vista.js';

let globalController;
let globalVista;
/**
 * Clase Main - Punto de entrada y coordinador de la aplicación
 */
class Main {
    
    constructor() {
        this.controller = null;
        this.vista = null;
        this.initialized = false;
    }

    /**
     * Inicializa toda la aplicación
     */
    async init() {
        try {
            console.log('🚦 Inicializando Sistema de Control de Tráfico...');

            // Inicializar controlador
            this.controller = new Controller(6);
            const resultado = this.controller.inicializar();
            console.log('✅', resultado.mensaje);

            // Inicializar vista
            this.vista = new Vista(this.controller);
            this.vista.inicializar('trafficCanvas');
            console.log('✅ Vista inicializada');

            // Configurar event listeners
            this.setupEventListeners();

            // Render inicial
            this.vista.render();
            this.vista.actualizarEstadisticas();

            this.initialized = true;
            this.mostrarMensaje('Sistema inicializado correctamente', 'success');

        } catch (error) {
            console.error('❌ Error al inicializar:', error);
            this.mostrarMensaje('Error al inicializar el sistema: ' + error.message, 'error');
        }
    }

    /**
     * Configura todos los event listeners de la UI
     */
    setupEventListeners() {
        // Botones de tráfico
        document.getElementById('btnTraficoAleatorio')?.addEventListener('click', () => {
            this.controller.generarTraficoAleatorio();
            this.vista.render();
            this.vista.actualizarEstadisticas();
            this.mostrarMensaje('Tráfico aleatorio generado', 'info');
        });

        document.getElementById('btnLimpiarTrafico')?.addEventListener('click', () => {
            this.controller.limpiarTrafico();
            this.vista.render();
            this.vista.actualizarEstadisticas();
            this.mostrarMensaje('Tráfico limpiado', 'info');
        });

        // Slider de tráfico global
        const sliderTrafico = document.getElementById('sliderTrafico');
        const labelTrafico = document.getElementById('labelTrafico');
        
        sliderTrafico?.addEventListener('input', (e) => {
            const valor = e.target.value;
            if (labelTrafico) {
                labelTrafico.textContent = valor + '%';
            }
        });

        sliderTrafico?.addEventListener('change', (e) => {
            const valor = parseInt(e.target.value);
            this.controller.establecerTraficoGlobal(valor);
            this.vista.render();
            this.vista.actualizarEstadisticas();
        });

        // Selección de algoritmo
        document.getElementById('algoritmoSelect')?.addEventListener('change', () => {
            if (this.vista.selectedOrigin && this.vista.selectedDestination) {
                this.vista.calcularYMostrarRuta();
                this.vista.render();
            }
        });

        // Botón resetear selección
        document.getElementById('btnResetear')?.addEventListener('click', () => {
            this.vista.resetearSeleccion();
            document.getElementById('rutaInfo').innerHTML = '';
            this.mostrarMensaje('Selección reseteada', 'info');
        });

        // Botón comparar algoritmos
        document.getElementById('btnComparar')?.addEventListener('click', () => {
            this.compararAlgoritmos();
        });

        // Botón undo
        document.getElementById('btnUndo')?.addEventListener('click', () => {
            const resultado = this.controller.undo();
            this.mostrarMensaje(resultado.mensaje, resultado.exito ? 'info' : 'warning');
            if (resultado.exito) {
                this.vista.render();
                this.vista.actualizarEstadisticas();
            }
        });

        // Botón redo
        document.getElementById('btnRedo')?.addEventListener('click', () => {
            const resultado = this.controller.redo();
            this.mostrarMensaje(resultado.mensaje, resultado.exito ? 'info' : 'warning');
            if (resultado.exito) {
                this.vista.render();
                this.vista.actualizarEstadisticas();
            }
        });

        // Importar JSON
        document.getElementById('btnImportarJSON')?.addEventListener('click', () => {
            document.getElementById('inputJSON').click();
        });

        document.getElementById('inputJSON')?.addEventListener('change', (e) => {
            this.importarJSON(e.target.files[0]);
        });

        // Exportar JSON
        document.getElementById('btnExportarJSON')?.addEventListener('click', () => {
            this.exportarJSON();
        });

        // Input manual de coordenadas
        document.getElementById('btnCalcularManual')?.addEventListener('click', () => {
            this.calcularRutaManual();
        });

        // Atajos de teclado
        document.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey) {
                if (e.key === 'z' && !e.shiftKey) {
                    e.preventDefault();
                    document.getElementById('btnUndo')?.click();
                } else if (e.key === 'z' && e.shiftKey || e.key === 'y') {
                    e.preventDefault();
                    document.getElementById('btnRedo')?.click();
                }
            }
        });
    }

    /**
     * Importa datos de tráfico desde un archivo JSON
     */
    async importarJSON(file) {
        if (!file) return;

        try {
            const texto = await file.text();
            const datos = JSON.parse(texto);
            
            const resultado = this.controller.cargarTraficoDesdeJSON(datos);
            
            this.mostrarMensaje(resultado.mensaje, resultado.exito ? 'success' : 'error');
            
            if (resultado.exito) {
                this.vista.render();
                this.vista.actualizarEstadisticas();
            }

        } catch (error) {
            this.mostrarMensaje('Error al leer el archivo JSON: ' + error.message, 'error');
        }
    }

    /**
     * Exporta el estado actual del tráfico a JSON
     */
    exportarJSON() {
        const datos = this.controller.exportarTraficoJSON();
        const json = JSON.stringify(datos, null, 2);
        
        // Crear blob y descargar
        const blob = new Blob([json], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `trafico_${new Date().toISOString().split('T')[0]}.json`;
        a.click();
        URL.revokeObjectURL(url);

        this.mostrarMensaje('Tráfico exportado correctamente', 'success');
    }

    /**
     * Calcula ruta usando inputs manuales
     */
    calcularRutaManual() {
        const origenRow = parseInt(document.getElementById('inputOrigenRow').value);
        const origenCol = parseInt(document.getElementById('inputOrigenCol').value);
        const destinoRow = parseInt(document.getElementById('inputDestinoRow').value);
        const destinoCol = parseInt(document.getElementById('inputDestinoCol').value);

        if (isNaN(origenRow) || isNaN(origenCol) || isNaN(destinoRow) || isNaN(destinoCol)) {
            this.mostrarMensaje('Por favor ingresa coordenadas válidas', 'warning');
            return;
        }

        const origen = this.controller.getInterseccion(origenRow, origenCol);
        const destino = this.controller.getInterseccion(destinoRow, destinoCol);

        if (!origen || !destino) {
            this.mostrarMensaje('Coordenadas fuera de rango', 'error');
            return;
        }

        this.vista.selectedOrigin = origen;
        this.vista.selectedDestination = destino;
        this.vista.calcularYMostrarRuta();
        this.vista.render();
    }

    /**
     * Compara ambos algoritmos de pathfinding
     */
    compararAlgoritmos() {
        if (!this.vista.selectedOrigin || !this.vista.selectedDestination) {
            this.mostrarMensaje('Selecciona origen y destino primero', 'warning');
            return;
        }

        const comparacion = this.controller.calcularRuta(
            this.vista.selectedOrigin.row,
            this.vista.selectedOrigin.col,
            this.vista.selectedDestination.row,
            this.vista.selectedDestination.col,
            'comparar'
        );

        // Mostrar modal con comparación
        this.mostrarComparacion(comparacion);
    }

    /**
     * Muestra modal con comparación de algoritmos
     */
    mostrarComparacion(comparacion) {
        const modal = document.getElementById('modalComparacion');
        const contenido = document.getElementById('contenidoComparacion');

        const html = `
            <div class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div class="bg-blue-50 p-4 rounded">
                        <h4 class="font-bold text-blue-900 mb-2">Dijkstra</h4>
                        <p><strong>Tiempo:</strong> ${comparacion.dijkstra.tiempo.toFixed(2)}ms</p>
                        <p><strong>Peso:</strong> ${comparacion.dijkstra.pesoTotal.toFixed(3)}</p>
                        <p><strong>Nodos:</strong> ${comparacion.dijkstra.ruta.length}</p>
                    </div>
                    <div class="bg-purple-50 p-4 rounded">
                        <h4 class="font-bold text-purple-900 mb-2">A*</h4>
                        <p><strong>Tiempo:</strong> ${comparacion.aStar.tiempo.toFixed(2)}ms</p>
                        <p><strong>Peso:</strong> ${comparacion.aStar.pesoTotal.toFixed(3)}</p>
                        <p><strong>Nodos:</strong> ${comparacion.aStar.ruta.length}</p>
                    </div>
                </div>
                <div class="bg-gray-50 p-4 rounded">
                    <h4 class="font-bold mb-2">Análisis</h4>
                    <p>${comparacion.dijkstra.tiempo < comparacion.aStar.tiempo ? 
                        'Dijkstra fue más rápido en este caso.' : 
                        'A* fue más rápido en este caso.'}</p>
                    <p class="mt-2 text-sm text-gray-600">
                        Ambos algoritmos garantizan encontrar la ruta óptima, pero A* 
                        puede ser más eficiente en grafos grandes gracias a su heurística.
                    </p>
                </div>
            </div>
        `;

        contenido.innerHTML = html;
        modal.classList.remove('hidden');
    }

    /**
     * Muestra un mensaje toast
     */
    mostrarMensaje(texto, tipo = 'info') {
        const container = document.getElementById('toastContainer');
        if (!container) return;

        const colores = {
            success: 'bg-green-500',
            error: 'bg-red-500',
            warning: 'bg-yellow-500',
            info: 'bg-blue-500'
        };

        const toast = document.createElement('div');
        toast.className = `${colores[tipo]} text-white px-6 py-3 rounded-lg shadow-lg mb-2 animate-slide-in`;
        toast.textContent = texto;

        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
}

// Exportar instancia global
const app = new Main();

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => app.init());
} else {
    app.init();
}

// Exportar para uso global
window.TrafficApp = app;

export default app;