# Pedidos Service — Refactorización Avanzada U11 Post-Contenido 1

**Asignatura:** Patrones de Diseño de Software  
**Unidad:** 11 — Refactorización Avanzada y Clean Code Profundo  
**Técnicas aplicadas:** Extract Method · Extract Class · Value Object (Primitive Obsession)

---

## Code Smells identificados en el código original

| # | Smell | Clase / Método | Descripción |
|---|-------|----------------|-------------|
| 1 | **Long Method** | `PedidoService.procesarPedido()` | Un único método de ~50 líneas que valida, calcula, aplica descuentos, recargos, notifica y persiste. CC = 21. |
| 2 | **Large Class** | `PedidoService` | La clase acumula responsabilidades de validación, cálculo, notificación y persistencia (viola SRP). |
| 3 | **Primitive Obsession / Data Clump** | Firma de `procesarPedido()` | 12 parámetros primitivos (`clienteNombre`, `clienteEmail`, `clienteTelefono`, `clienteDireccion`, …) en lugar de Value Objects. |
| 4 | **Field Injection** | `@Autowired private PedidoRepository repo` | Inyección en campo: dificulta las pruebas unitarias y oculta dependencias. |

---

## Técnicas de refactorización aplicadas

### Paso 2 — Value Object (Eliminate Primitive Obsession)

Se crearon cuatro clases **inmutables** que encapsulan conceptos del dominio:

| Clase | Reemplaza | Validación incluida |
|-------|-----------|---------------------|
| `Direccion` | `clienteDireccion`, `clienteCiudad`, `clienteCodigoPostal` | Ciudad requerida |
| `DatosCliente` | `clienteNombre`, `clienteEmail`, `clienteTelefono` + `Direccion` | Nombre no blanco, email con `@` |
| `LineaPedido` | `productosIds.get(i)`, `cantidades.get(i)` + precio | Cantidad > 0, precio ≥ 0 |
| `CodigoDescuento` | `String codigoDescuento` | Solo acepta VIP10 · NEW20 · SALE30 |

Todas las clases tienen `equals` / `hashCode` y **no tienen setters** (inmutables por diseño).

### Paso 3 — Extract Method (Reducir Long Method)

`procesarPedido()` pasó de ~50 líneas (CC = 21) a 6 líneas (CC = 1) delegando a métodos privados con responsabilidad única:

```
procesarPedido()          → orquesta
  calcularTotal()         → suma de líneas (CC = 1)
  aplicarDescuento()      → ternario (CC = 2)
  aplicarRecargoUrgente() → ternario (CC = 2)
  notificacion.notificarPedido()
  persistirPedido()       → crea Pedido y guarda (CC = 1)
```

### Paso 4 — Extract Class (Separar responsabilidades)

La lógica de notificación se extrajo a `NotificacionService implements Notificador`.  
`PedidoService` recibe la interfaz `Notificador` por **constructor injection** (no por campo):

```java
public PedidoService(PedidoRepository repo, Notificador notificacion) { … }
```

Beneficios: testeable sin Spring, principio DIP cumplido, SRP restaurado.

---

## Métricas SonarQube — Antes vs. Después

| Métrica | Antes (código original) | Después (refactorizado) | Mejora |
|---------|------------------------|-------------------------|--------|
| **CC de `procesarPedido()`** | 21 | 1 | −20 puntos |
| **CC promedio del proyecto** | ~8 | ~2 | −75 % |
| **Code Smells totales** | ~12 | ~1 | −91 % |
| **TDR (Technical Debt Ratio)** | ~8 % | ~1 % | −87 % |
| **Deuda técnica estimada** | ~45 min | ~5 min | −89 % |

> **Nota:** Las capturas del dashboard de SonarQube se encuentran en [`docs/`](docs/).  
> Para reproducir el análisis: `mvn verify sonar:sonar -Dsonar.token=TU_TOKEN`

---

## Checkpoints cumplidos

- [x] El proyecto compila sin errores: `mvn compile`
- [x] `DatosCliente`, `Direccion`, `LineaPedido`, `CodigoDescuento` son inmutables (campos `final`, sin setters)
- [x] `procesarPedido()` tiene **6 líneas** tras la refactorización (límite: 8)
- [x] `NotificacionService` es independiente e inyectado por constructor en `PedidoService`
- [x] 24 tests unitarios pasan: 15 de Value Objects + 8 de servicio + 1 de contexto
- [x] SonarQube reporta menos Code Smells en el segundo análisis

---

## Estructura del proyecto

```
src/main/java/com/universidad/pedidos/
├── domain/
│   ├── CodigoDescuento.java   ← Value Object (nuevo)
│   ├── DatosCliente.java      ← Value Object (nuevo)
│   ├── Direccion.java         ← Value Object (nuevo)
│   ├── LineaPedido.java       ← Value Object (nuevo)
│   ├── Pedido.java
│   └── Producto.java
├── repository/
│   └── PedidoRepository.java
├── service/
│   ├── Notificador.java       ← Interfaz (nueva)
│   ├── NotificacionService.java ← Extract Class (nueva)
│   └── PedidoService.java     ← Refactorizado
└── PedidosApplication.java
```

---

## Ejecutar análisis con SonarQube

```bash
# 1. Levantar SonarQube
docker run -d --name sonarqube -p 9000:9000 sonarqube:community

# 2. Generar token en http://localhost:9000 > Account > Security

# 3. Análisis inicial (código original — commit 1)
git checkout HEAD~1
mvn verify sonar:sonar -Dsonar.token=TU_TOKEN -Dsonar.projectKey=refactoring-u11

# 4. Análisis refactorizado (código actual)
git checkout master
mvn verify sonar:sonar -Dsonar.token=TU_TOKEN -Dsonar.projectKey=refactoring-u11
```
