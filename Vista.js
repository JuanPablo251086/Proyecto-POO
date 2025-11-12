/**
 * Clase Vista - Maneja toda la interfaz gráfica y eventos del usuario
 * Patrón MVC: Separación de lógica y presentación
 */
export class Vista {
    constructor(controller) {
        this.controller = controller;
        this.canvas = null;
        this.ctx = null;
        this.selectedOrigin = null;
        this.selectedDestination = null;
        this.currentPath = null;
        this.hoveredIntersection = null;
        this.scale = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        
        // Configuración visual
        this.config = {
            intersectionRadius: 8,
            edgeWidth: 3,
            pathWidth: 5,
            fontSize: 10,
            spacing: 80,
            margin: 60
        };
    }

    /**
     * Inicializa el canvas y eventos
     */
    inicializar(canvasId) {
        this.canvas = document.getElementById(canvasId);
        if (!this.canvas) {
            throw new Error(`Canvas con id "${canvasId}" no encontrado`);
        }

        this.ctx = this.canvas.getContext('2d');
        this.ajustarTamañoCanvas();
        
        // Event listeners
        window.addEventListener('resize', () => this.ajustarTamañoCanvas());
        this.canvas.addEventListener('click', (e) => this.handleClick(e));
        this.canvas.addEventListener('mousemove', (e) => this.handleMouseMove(e));
        this.canvas.addEventListener('wheel', (e) => this.handleWheel(e));

        return this;
    }

    /**
     * Ajusta el tamaño del canvas al contenedor
     */
    ajustarTamañoCanvas() {
        const container = this.canvas.parentElement;
        this.canvas.width = container.clientWidth;
        this.canvas.height = container.clientHeight;
        this.render();
    }

    /**
     * Convierte coordenadas de canvas a coordenadas de grilla
     */
    canvasToGrid(x, y) {
        const gridX = (x - this.offsetX - this.config.margin) / this.config.spacing;
        const gridY = (y - this.offsetY - this.config.margin) / this.config.spacing;
        
        const col = Math.round(gridX) + 1;
        const row = Math.round(gridY) + 1;
        
        return { row, col };
    }

    /**
     * Convierte coordenadas de grilla a coordenadas de canvas
     */
    gridToCanvas(row, col) {
        const x = (col - 1) * this.config.spacing + this.config.margin + this.offsetX;
        const y = (row - 1) * this.config.spacing + this.config.margin + this.offsetY;
        return { x, y };
    }

    /**
     * Maneja clicks en el canvas
     */
    handleClick(event) {
        const rect = this.canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        const { row, col } = this.canvasToGrid(x, y);
        const interseccion = this.controller.getInterseccion(row, col);

        if (interseccion) {
            if (!this.selectedOrigin) {
                this.selectedOrigin = interseccion;
                this.selectedDestination = null;
                this.currentPath = null;
            } else if (!this.selectedDestination) {
                this.selectedDestination = interseccion;
                this.calcularYMostrarRuta();
            } else {
                // Reset
                this.selectedOrigin = interseccion;
                this.selectedDestination = null;
                this.currentPath = null;
            }
            
            this.render();
            this.actualizarInfoPanel();
        }
    }

    /**
     * Maneja movimiento del mouse
     */
    handleMouseMove(event) {
        const rect = this.canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        const { row, col } = this.canvasToGrid(x, y);
        const interseccion = this.controller.getInterseccion(row, col);

        if (interseccion !== this.hoveredIntersection) {
            this.hoveredIntersection = interseccion;
            this.render();
        }

        // Cambiar cursor si hay intersección
        this.canvas.style.cursor = interseccion ? 'pointer' : 'default';
    }

    /**
     * Maneja zoom con rueda del mouse
     */
    handleWheel(event) {
        event.preventDefault();
        const delta = -Math.sign(event.deltaY) * 0.1;
        this.scale = Math.max(0.5, Math.min(2, this.scale + delta));
        this.render();
    }

    /**
     * Calcula y muestra la ruta
     */
    calcularYMostrarRuta() {
        if (this.selectedOrigin && this.selectedDestination) {
            const algoritmo = document.getElementById('algoritmoSelect')?.value || 'dijkstra';
            
            this.currentPath = this.controller.calcularRuta(
                this.selectedOrigin.row,
                this.selectedOrigin.col,
                this.selectedDestination.row,
                this.selectedDestination.col,
                algoritmo
            );

            this.actualizarInfoRuta();
        }
    }

    /**
     * Renderiza todo el sistema
     */
    render() {
        // Limpiar canvas
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        // Aplicar transformaciones
        this.ctx.save();
        this.ctx.scale(this.scale, this.scale);

        // Renderizar en orden: aristas -> ruta -> intersecciones
        this.renderAristas();
        this.renderRuta();
        this.renderIntersecciones();
        this.renderLeyenda();

        this.ctx.restore();
    }

    /**
     * Renderiza todas las aristas
     */
    renderAristas() {
        for (const arista of this.controller.aristas) {
            const from = this.gridToCanvas(arista.from.row, arista.from.col);
            const to = this.gridToCanvas(arista.to.row, arista.to.col);

            // Color según congestión
            this.ctx.strokeStyle = arista.getColor();
            this.ctx.lineWidth = this.config.edgeWidth;
            this.ctx.globalAlpha = 0.6;

            // Línea con estilo según tipo
            if (arista.tipo === 'avenida') {
                this.ctx.setLineDash([5, 5]);
            } else {
                this.ctx.setLineDash([]);
            }

            this.ctx.beginPath();
            this.ctx.moveTo(from.x, from.y);
            this.ctx.lineTo(to.x, to.y);
            this.ctx.stroke();

            this.ctx.globalAlpha = 1;
            this.ctx.setLineDash([]);
        }
    }

    /**
     * Renderiza la ruta calculada
     */
    renderRuta() {
        if (!this.currentPath || !this.currentPath.encontrada) return;

        this.ctx.strokeStyle = '#8b5cf6';
        this.ctx.lineWidth = this.config.pathWidth;
        this.ctx.globalAlpha = 0.9;
        this.ctx.lineCap = 'round';
        this.ctx.lineJoin = 'round';

        this.ctx.beginPath();
        for (let i = 0; i < this.currentPath.ruta.length; i++) {
            const interseccion = this.currentPath.ruta[i];
            const pos = this.gridToCanvas(interseccion.row, interseccion.col);
            
            if (i === 0) {
                this.ctx.moveTo(pos.x, pos.y);
            } else {
                this.ctx.lineTo(pos.x, pos.y);
            }
        }
        this.ctx.stroke();

        this.ctx.globalAlpha = 1;
    }

    /**
     * Renderiza todas las intersecciones
     */
    renderIntersecciones() {
        for (const [id, interseccion] of this.controller.intersecciones) {
            const pos = this.gridToCanvas(interseccion.row, interseccion.col);
            
            // Determinar color
            let color = '#94a3b8'; // Gris por defecto
            let radius = this.config.intersectionRadius;

            if (this.selectedOrigin && interseccion.equals(this.selectedOrigin)) {
                color = '#22c55e'; // Verde para origen
                radius *= 1.5;
            } else if (this.selectedDestination && interseccion.equals(this.selectedDestination)) {
                color = '#ef4444'; // Rojo para destino
                radius *= 1.5;
            } else if (this.hoveredIntersection && interseccion.equals(this.hoveredIntersection)) {
                color = '#60a5fa'; // Azul para hover
                radius *= 1.2;
            }

            // Dibujar círculo
            this.ctx.fillStyle = color;
            this.ctx.beginPath();
            this.ctx.arc(pos.x, pos.y, radius, 0, Math.PI * 2);
            this.ctx.fill();

            // Borde
            this.ctx.strokeStyle = '#1e293b';
            this.ctx.lineWidth = 2;
            this.ctx.stroke();

            // Etiqueta
            if (this.scale > 0.7) {
                this.ctx.fillStyle = '#1e293b';
                this.ctx.font = `${this.config.fontSize}px monospace`;
                this.ctx.textAlign = 'center';
                this.ctx.textBaseline = 'middle';
                this.ctx.fillText(`${interseccion.row},${interseccion.col}`, pos.x, pos.y - radius - 12);
            }
        }
    }

    /**
     * Renderiza la leyenda
     */
    renderLeyenda() {
        const x = 20;
        const y = this.canvas.height / this.scale - 120;

        this.ctx.fillStyle = 'rgba(255, 255, 255, 0.95)';
        this.ctx.fillRect(x, y, 180, 100);
        
        this.ctx.strokeStyle = '#cbd5e1';
        this.ctx.lineWidth = 1;
        this.ctx.strokeRect(x, y, 180, 100);

        this.ctx.fillStyle = '#1e293b';
        this.ctx.font = 'bold 12px sans-serif';
        this.ctx.textAlign = 'left';
        this.ctx.fillText('Nivel de Congestión:', x + 10, y + 20);

        const niveles = [
            { color: '#4ade80', texto: 'Bajo (0-30%)' },
            { color: '#facc15', texto: 'Medio (30-60%)' },
            { color: '#fb923c', texto: 'Alto (60-80%)' },
            { color: '#ef4444', texto: 'Crítico (80-100%)' }
        ];

        niveles.forEach((nivel, i) => {
            const yPos = y + 40 + i * 15;
            
            this.ctx.fillStyle = nivel.color;
            this.ctx.fillRect(x + 10, yPos - 5, 15, 10);
            
            this.ctx.fillStyle = '#1e293b';
            this.ctx.font = '11px sans-serif';
            this.ctx.fillText(nivel.texto, x + 30, yPos);
        });
    }

    /**
     * Actualiza el panel de información
     */
    actualizarInfoPanel() {
        const panel = document.getElementById('infoPanel');
        if (!panel) return;

        let html = '<h3 class="text-lg font-bold mb-2">Información</h3>';

        if (this.selectedOrigin) {
            html += `<p><strong>Origen:</strong> (${this.selectedOrigin.row}, ${this.selectedOrigin.col})</p>`;
        }

        if (this.selectedDestination) {
            html += `<p><strong>Destino:</strong> (${this.selectedDestination.row}, ${this.selectedDestination.col})</p>`;
        }

        if (!this.selectedOrigin) {
            html += '<p class="text-gray-600 mt-2">Haz clic para seleccionar origen</p>';
        } else if (!this.selectedDestination) {
            html += '<p class="text-gray-600 mt-2">Haz clic para seleccionar destino</p>';
        }

        panel.innerHTML = html;
    }

    /**
     * Actualiza la información de la ruta
     */
    actualizarInfoRuta() {
        const panel = document.getElementById('rutaInfo');
        if (!panel) return;

        if (!this.currentPath || !this.currentPath.encontrada) {
            panel.innerHTML = '<p class="text-red-600">No se encontró ruta</p>';
            return;
        }

        const html = `
            <h3 class="text-lg font-bold mb-2">Ruta Calculada</h3>
            <p><strong>Peso total:</strong> ${this.currentPath.pesoTotal.toFixed(3)}</p>
            <p><strong>Intersecciones:</strong> ${this.currentPath.ruta.length}</p>
            <p><strong>Aristas:</strong> ${this.currentPath.aristas.length}</p>
            <div class="mt-2 text-sm">
                <strong>Camino:</strong>
                <div class="max-h-32 overflow-y-auto mt-1">
                    ${this.currentPath.ruta.map(i => `(${i.row},${i.col})`).join(' → ')}
                </div>
            </div>
        `;

        panel.innerHTML = html;
    }

    /**
     * Actualiza las estadísticas generales
     */
    actualizarEstadisticas() {
        const stats = this.controller.obtenerEstadisticas();
        const panel = document.getElementById('estadisticas');
        
        if (!panel) return;

        const html = `
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <p class="text-sm text-gray-600">Intersecciones</p>
                    <p class="text-2xl font-bold">${stats.totalIntersecciones}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Aristas</p>
                    <p class="text-2xl font-bold">${stats.totalAristas}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Tráfico Promedio</p>
                    <p class="text-2xl font-bold">${stats.traficoPromedio}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Congestión</p>
                    <p class="text-2xl font-bold">${stats.congestionGeneral}</p>
                </div>
            </div>
        `;

        panel.innerHTML = html;
    }

    /**
     * Resetea la selección
     */
    resetearSeleccion() {
        this.selectedOrigin = null;
        this.selectedDestination = null;
        this.currentPath = null;
        this.render();
        this.actualizarInfoPanel();
    }
}