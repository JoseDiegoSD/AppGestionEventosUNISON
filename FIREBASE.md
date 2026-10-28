# Proyectos y eventos

La aplicación usa Firebase Realtime Database, por lo que no requiere columnas SQL.
`proyectos/{id}/autorId` identifica al creador y se consulta con el UID de Firebase Auth.
Los proyectos sin autorId no se atribuyen a ninguna cuenta; debe recuperarse su autor real antes de asignarlos.

Las nuevas publicaciones escriben atómicamente `proyectos/{id}` y `eventos/{id}` con la misma clave y autorId. Los eventos creados desde proyectos no tienen imagen ni lugar; fechaHora indica la fecha de publicación, no una fecha de celebración. Los proyectos anteriores no se migran automáticamente.

El contador se calcula desde `interesados` y se actualiza en vivo en la lista y el detalle, sin guardar un contador duplicado. La clave eventoId_usuarioId existente evita duplicados por usuario.

## Configuración de Firebase

Incorporar estos índices a las reglas existentes, conservando las reglas de acceso del entorno:

```json
{
  "rules": {
    "proyectos": { ".indexOn": ["autorId"] },
    "interesados": { ".indexOn": ["eventoId"] }
  }
}
```

La escritura del usuario autenticado debe estar permitida en ambas rutas para que la publicación atómica tenga éxito. El filtro de la aplicación no sustituye las reglas de autorización de Firebase. No se incluyen ni despliegan reglas completas porque el repositorio no contiene las del entorno.

## Comprobación funcional con dos cuentas

1. Publicar con la cuenta A y comprobar que el proyecto aparece en sus proyectos y en eventos, sin espacio reservado para imagen.
2. Iniciar sesión como B: el proyecto de A aparece en eventos y no en sus proyectos.
3. Registrar interés como B y comprobar que el contador pasa de 0 a 1 tanto en detalle como en la lista.
4. Volver al detalle: el botón permanece deshabilitado y el contador sigue en 1.
5. Registrar interés como A y comprobar que el contador pasa a 2.
6. Denegar una de las escrituras en un entorno de pruebas y comprobar que no se guarda ninguna de las dos copias y se permite reintentar.
