# DeepPlant
El proyecto usa AGP 8.13.0 y requiere ejecutar Gradle con JDK 11+. Ajusta tu entorno (JAVA_HOME) para evitar el fallo de compilación por usar Java 8.
## Requisito de compilación

Existe prueba de composición básica en `androidTest` verificando color primario no nulo.
## Test rápido

5. Añadir pruebas de snapshot visual si se desea.
4. Añadir vector asset como placeholder para foto inicial.
3. Sustituir flags de sistema UI deprecados por API Insets (Accompanist o nueva API).
2. Añadir preferencia (DataStore) para activar/desactivar dynamic color.
1. Integrar fuente Inter/Nunito con Downloadable Fonts y reemplazar `AppFontFamily`.
## Próximos pasos sugeridos

- Preparación para modo dynamic color (parámetro `dynamicColor` en `DeepPlantTheme`).
- Refactor de `PlantAnalysisScreen` para usar el tema y componentes, reducir colores hardcode.
- Componentes reutilizables en `ui/components`: `PrimaryButton`, `SecondaryButton`, `CardPlantResult`, `HistoryItemCard`, `LoadingOverlay`.
- Shapes redondeados consistentes (4, 8, 16, 24, 32 dp).
- Tipografía jerárquica (display, headline, title, body, label) lista para integrar fuente descargable (reemplazar `FontFamily.Default`).
- Paleta verde natural (primario, secundarios, contenedores) y colores semánticos (success, warning, danger).
Se implementó un nuevo tema unificado `DeepPlantTheme` con:

## Diseño (Rediseño)

Aplicación de análisis de plantas (Jetpack Compose).


