const API_URL = '/api';

document.addEventListener('DOMContentLoaded', () => {
    // Establecer la fecha de hoy por defecto en el formulario de partidos
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fechaEncuentro').value = today;

    // Cargar datos iniciales
    cargarEquipos();
    cargarTablaPosiciones();
    cargarEncuentros();

    // Event Listener: Guardar Equipo
    document.getElementById('form-equipo').addEventListener('submit', async (e) => {
        e.preventDefault();
        const nombreInput = document.getElementById('nombreEquipo');
        const nombre = nombreInput.value.trim();

        try {
            const res = await fetch(`${API_URL}/equipos`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre })
            });

            if (res.ok) {
                mostrarAlerta('Equipo registrado exitosamente en la liga.', 'success');
                nombreInput.value = '';
                cargarEquipos();
                cargarTablaPosiciones();
            } else {
                const errorMsg = await res.text();
                mostrarAlerta(errorMsg, 'error');
            }
        } catch (err) {
            mostrarAlerta('Error de conexión con el servidor backend.', 'error');
        }
    });

    // Event Listener: Guardar Partido
    document.getElementById('form-encuentro').addEventListener('submit', async (e) => {
        e.preventDefault();
        const equipoLocalId = parseInt(document.getElementById('selectLocal').value);
        const equipoVisitanteId = parseInt(document.getElementById('selectVisitante').value);
        const golesLocal = parseInt(document.getElementById('golesLocal').value);
        const golesVisitante = parseInt(document.getElementById('golesVisitante').value);
        const fecha = document.getElementById('fechaEncuentro').value;

        try {
            const res = await fetch(`${API_URL}/encuentros`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ equipoLocalId, equipoVisitanteId, golesLocal, golesVisitante, fecha })
            });

            if (res.ok) {
                mostrarAlerta('Resultado del encuentro guardado correctamente.', 'success');
                cargarEncuentros();
                cargarTablaPosiciones();
            } else {
                const errorMsg = await res.text();
                mostrarAlerta(errorMsg, 'error');
            }
        } catch (err) {
            mostrarAlerta('Error de conexión con el servidor backend.', 'error');
        }
    });
});

async function cargarEquipos() {
    try {
        const res = await fetch(`${API_URL}/equipos`);
        const equipos = await res.json();

        // Actualizar estadísticas KPI
        document.getElementById('stat-equipos-count').textContent = equipos.length;
        document.getElementById('badge-equipos-count').textContent = `${equipos.length} equipos`;

        // Actualizar lista en pantalla
        const listaUl = document.getElementById('lista-equipos');
        if (equipos.length === 0) {
            listaUl.innerHTML = '<li class="text-xs text-slate-500 italic py-3 text-center bg-slate-900/40 rounded-xl">No hay equipos registrados aún</li>';
        } else {
            listaUl.innerHTML = equipos.map(eq => `
                <li class="flex items-center justify-between bg-slate-900/80 border border-slate-800 hover:border-slate-700 px-3.5 py-2.5 rounded-xl text-xs font-medium text-slate-200 transition">
                    <div class="flex items-center gap-2.5">
                        <span class="w-6 h-6 rounded-lg bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 flex items-center justify-center font-bold font-mono text-[10px]">
                            ${eq.nombre.substring(0, 2).toUpperCase()}
                        </span>
                        <span>${eq.nombre}</span>
                    </div>
                    <span class="text-[10px] text-slate-400 font-mono bg-slate-800 border border-slate-700/60 px-2 py-0.5 rounded-md">ID: ${eq.id}</span>
                </li>
            `).join('');
        }

        // Actualizar los dropdowns del formulario de encuentros
        const selectLocal = document.getElementById('selectLocal');
        const selectVisitante = document.getElementById('selectVisitante');

        if (equipos.length === 0) {
            selectLocal.innerHTML = '<option value="">Registra equipos primero</option>';
            selectVisitante.innerHTML = '<option value="">Registra equipos primero</option>';
        } else {
            const options = equipos.map(eq => `<option value="${eq.id}">${eq.nombre}</option>`).join('');
            selectLocal.innerHTML = options;
            selectVisitante.innerHTML = options;
            // Seleccionar automáticamente opciones distintas si hay más de 1 equipo
            if (equipos.length > 1) {
                selectVisitante.selectedIndex = 1;
            }
        }
    } catch (err) {
        console.error('Error al cargar equipos:', err);
    }
}

async function cargarTablaPosiciones() {
    try {
        const res = await fetch(`${API_URL}/encuentros/tabla-posiciones`);
        const tabla = await res.json();

        // Actualizar KPI Líder
        const statLider = document.getElementById('stat-lider-nombre');
        if (tabla.length > 0 && tabla[0].partidosJugados > 0) {
            statLider.textContent = tabla[0].equipoNombre;
        } else if (tabla.length > 0) {
            statLider.textContent = `${tabla[0].equipoNombre} (0 pts)`;
        } else {
            statLider.textContent = 'Sin Líder';
        }

        const tbody = document.querySelector('#tabla-posiciones tbody');
        if (tabla.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="py-8 text-slate-500 italic">No hay equipos ni partidos registrados en la tabla de posiciones.</td></tr>';
            return;
        }

        tbody.innerHTML = tabla.map((fila, index) => {
            let posBadge = '';
            let rowStyle = 'hover:bg-slate-800/40 transition';

            if (index === 0) {
                posBadge = '<span class="w-6 h-6 rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/40 inline-flex items-center justify-center font-bold text-xs"><i class="fa-solid fa-crown text-[10px]"></i></span>';
                rowStyle = 'bg-amber-500/5 hover:bg-amber-500/10 font-semibold border-l-2 border-l-amber-400';
            } else if (index === 1) {
                posBadge = '<span class="w-6 h-6 rounded-full bg-slate-300/20 text-slate-200 border border-slate-300/40 inline-flex items-center justify-center font-bold text-xs">2</span>';
            } else if (index === 2) {
                posBadge = '<span class="w-6 h-6 rounded-full bg-amber-700/20 text-amber-600 border border-amber-700/40 inline-flex items-center justify-center font-bold text-xs">3</span>';
            } else {
                posBadge = `<span class="text-slate-400 font-mono text-xs">${index + 1}</span>`;
            }

            const difStyle = fila.diferenciaGoles > 0 
                ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
                : (fila.diferenciaGoles < 0 ? 'bg-rose-500/10 text-rose-400 border border-rose-500/20' : 'bg-slate-800 text-slate-400 border border-slate-700');

            const difTexto = fila.diferenciaGoles > 0 ? `+${fila.diferenciaGoles}` : `${fila.diferenciaGoles}`;

            return `
                <tr class="${rowStyle}">
                    <td class="py-3.5 px-3 text-left">${posBadge}</td>
                    <td class="py-3.5 px-4 text-left font-semibold text-white">
                        <div class="flex items-center gap-2">
                            <span class="w-2 h-2 rounded-full ${index === 0 ? 'bg-amber-400' : 'bg-slate-600'}"></span>
                            ${fila.equipoNombre}
                        </div>
                    </td>
                    <td class="py-3.5 px-3 text-slate-300 font-mono">${fila.partidosJugados}</td>
                    <td class="py-3.5 px-3 text-emerald-400 font-mono">${fila.partidosGanados}</td>
                    <td class="py-3.5 px-3 text-amber-400 font-mono">${fila.partidosEmpatados}</td>
                    <td class="py-3.5 px-3 text-rose-400 font-mono">${fila.partidosPerdidos}</td>
                    <td class="py-3.5 px-3 text-slate-300 font-mono">${fila.golesAFavor}</td>
                    <td class="py-3.5 px-3 text-slate-400 font-mono">${fila.golesEnContra}</td>
                    <td class="py-3.5 px-3">
                        <span class="px-2 py-0.5 rounded-md text-xs font-mono font-bold ${difStyle}">${difTexto}</span>
                    </td>
                    <td class="py-3.5 px-4 text-right">
                        <span class="text-base font-extrabold text-emerald-400 font-mono bg-emerald-500/10 border border-emerald-500/20 px-3 py-1 rounded-lg">
                            ${fila.puntos} pts
                        </span>
                    </td>
                </tr>
            `;
        }).join('');
    } catch (err) {
        console.error('Error al cargar tabla de posiciones:', err);
    }
}

async function cargarEncuentros() {
    try {
        const res = await fetch(`${API_URL}/encuentros`);
        const encuentros = await res.json();

        // Actualizar estadísticas KPI
        document.getElementById('stat-partidos-count').textContent = encuentros.length;
        
        const totalGoles = encuentros.reduce((acc, enc) => acc + enc.golesLocal + enc.golesVisitante, 0);
        document.getElementById('stat-goles-count').textContent = totalGoles;

        const tbody = document.querySelector('#tabla-encuentros tbody');
        if (encuentros.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="py-8 text-slate-500 italic">No hay encuentros disputados registrados.</td></tr>';
            return;
        }

        tbody.innerHTML = encuentros.map(enc => `
            <tr class="hover:bg-slate-800/40 transition">
                <td class="py-3.5 px-4 text-left font-mono text-xs text-slate-400">
                    <i class="fa-regular fa-calendar text-slate-500 mr-1.5"></i>${enc.fecha}
                </td>
                <td class="py-3.5 px-4 text-right font-semibold text-slate-200">
                    ${enc.equipoLocalNombre}
                </td>
                <td class="py-3.5 px-4">
                    <span class="inline-flex items-center gap-2 bg-slate-950 border border-slate-700/80 px-3 py-1 rounded-xl text-sm font-extrabold font-mono text-white shadow-inner">
                        <span class="${enc.golesLocal > enc.golesVisitante ? 'text-emerald-400' : 'text-slate-300'}">${enc.golesLocal}</span>
                        <span class="text-slate-600 text-xs">-</span>
                        <span class="${enc.golesVisitante > enc.golesLocal ? 'text-blue-400' : 'text-slate-300'}">${enc.golesVisitante}</span>
                    </span>
                </td>
                <td class="py-3.5 px-4 text-left font-semibold text-slate-200">
                    ${enc.equipoVisitanteNombre}
                </td>
            </tr>
        `).join('');
    } catch (err) {
        console.error('Error al cargar encuentros:', err);
    }
}

function mostrarAlerta(mensaje, tipo) {
    const box = document.getElementById('alert-box');
    
    if (tipo === 'success') {
        box.className = 'animate-fade-in mb-6 p-4 rounded-xl font-medium text-sm flex items-center gap-3 shadow-xl bg-emerald-500/10 text-emerald-300 border border-emerald-500/30';
        box.innerHTML = `<i class="fa-solid fa-circle-check text-emerald-400 text-lg"></i> <span>${mensaje}</span>`;
    } else {
        box.className = 'animate-fade-in mb-6 p-4 rounded-xl font-medium text-sm flex items-center gap-3 shadow-xl bg-rose-500/10 text-rose-300 border border-rose-500/30';
        box.innerHTML = `<i class="fa-solid fa-circle-exclamation text-rose-400 text-lg"></i> <span>${mensaje}</span>`;
    }

    setTimeout(() => {
        box.classList.add('hidden');
    }, 4500);
}
