# Análisis SonarQube — Antes y Después

## Cómo reproducir el análisis

```bash
# Levantar SonarQube (si no está corriendo)
docker run -d --name sonarqube -p 9000:9000 sonarqube:community

# Esperar ~1 minuto y generar token en:
# http://localhost:9000 > Account > Security > Generate Token

# Análisis del código ORIGINAL (commit 1)
git checkout HEAD~1
mvn verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=TU_TOKEN \
  -Dsonar.projectKey=refactoring-u11

# Anotar métricas y tomar captura de pantalla del dashboard

# Volver al código refactorizado (commit 2)
git checkout master
mvn verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=TU_TOKEN \
  -Dsonar.projectKey=refactoring-u11
```

## Métricas obtenidas

### Análisis 1 — Código original

| Métrica | Valor |
|---------|-------|
| Complejidad Ciclomática `procesarPedido()` | 21 |
| Code Smells totales | _completar_ |
| Technical Debt | _completar_ |
| TDR (Technical Debt Ratio) | _completar_ |

> Captura: `docs/sonar-antes.png`

### Análisis 2 — Código refactorizado

| Métrica | Valor |
|---------|-------|
| Complejidad Ciclomática `procesarPedido()` | 1 |
| Code Smells totales | _completar_ |
| Technical Debt | _completar_ |
| TDR (Technical Debt Ratio) | _completar_ |

> Captura: `docs/sonar-despues.png`

## Comparación

| Métrica | Antes | Después | Mejora |
|---------|-------|---------|--------|
| CC `procesarPedido()` | 21 | 1 | −95 % |
| Code Smells | _X_ | _Y_ | _completar_ |
| TDR | _X %_ | _Y %_ | _completar_ |

## Capturas del dashboard

Coloca aquí las capturas tomadas de SonarQube:
- `docs/sonar-antes.png` — análisis antes de refactorizar
- `docs/sonar-despues.png` — análisis después de refactorizar
