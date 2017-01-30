# API app-intercambio

```
URL Producción: http://janiserver.servehttp.com:8080/app-intercambio/
```

## Identificar usuario [POST]

http://localhost:8080/app-intercambio/webresources/api/usuarios/identificacion

Permite identificar un usuario si existe en el sistema, si este existe muestra su información de lo contrario el status de la petición será distinto a exitoso.
El tipoUsuario 1 corresponde a un administrador y el tipo 2 a un participante.

### Solicitud
```json
{
  "usuario": "jast1000@gmail.com",
  "password": "chucho25"
}
```
### Respuesta
```json
{
    "usuario": "jast1000@gmail.com",
    "password": null,
    "tipoUsuario": 1
}
```

## Registrar usuario [POST]

http://localhost:8080/app-intercambio/webresources/api/usuarios


Permite dar de alta (invitar) a un usuario. El tipo debe ser 2.
Verificar que el status de la respuesta sea *200* de lo contrario podría existir un error o un mensaje de que el usuario existe.

### Solicitud
```json
{
  "usuario": "jalbertostecalco@gmail.com",
  "tipoUsuario": "2"
}
```
Permite eliminar un usuario, donde usuario es el correo electrónico (id) del usuario.

## Consultar perfil de participante [GET]

http://localhost:8080/app-intercambio/webresources/api/perfiles/{usuario}

Permite consultar el perfil de un participante, si este existe se obtiene su información, en caso contrario el status será distinto a 200.
Nota: Esta función debe lanzarse  en la pantalla de creación de perfil para conocer si está creado dejarlo llenar, si ya existe (hay información) no debe dejarse llenar la pantalla.

### Respuesta
```json
{
    "idParticipante": "7116370e-1f44-45e3-9d11-a2312fb29b72",
    "nombres": "Jesus",
    "edad": 23,
    "sexo": "masculino",
    "grado": "1",
    "grupo": "a",
    "area": "area",
    "gustos": "x",
    "usuario": "jast1000@gmail.com",
    "opcionesIntercambio": "1 Mochila, 2 Sueter, 3 USB 16 GB"
}
```

## Guardar perfil participante [POST]

http://localhost:8080/app-intercambio/webresources/api/perfiles

Permite guardar el perfil de un usuario registrado en el sistema. En caso de no existir el usuario la respuesta tendrá un status distinto a 200

### Solicitud
```json
{
    "nombres": "Jesus",
    "edad": 23,
    "sexo": "masculino",
    "grado": "1",
    "grupo": "a",
    "area": "area",
    "gustos": "x",
    "usuario": "jast1000@gmail.com",
    "opcionesIntercambio": "1 Mochila, 2 Sueter, 3 USB 16 GB"
}
```

## Lista de intercambios del administrador [GET]
http://localhost:8080/app-intercambio/webresources/api/intercambios

Permite visualizar los intercambios creados por el administrador y su estado (únicamente se puede hacer uno en teoría).
El estado 1 corresponde a creado, por lo cual el botón de sorteo podría estar habilitado.
El estado 2 corresponde a sorteado, es decir, ya existe asignación de parejas, no debería ser posible volver a sortear.
Si el status de respuesta es 204 (no content) es posible crear un intercambio
Nota: la fecha está en long (new Date(fechalong); para crear fecha a partir del número en Javascript)

### Respuesta
```json
[
    {
        "idRegla": 1,
        "lugar": "UNIVO",
        "fecha": 1485723141377,
        "monto": 300,
        "estado": 1
    }
]
```

## Crear intercambio [POST]
http://localhost:8080/app-intercambio/webresources/api/intercambios

Permite crear un intercambio, definiendo las reglas para este, lugar, fecha y monto mínimo.
Si existe un intercambio, la respuesta es distinta a 200.
Nota: la fecha está en formato ISO (en Javascript se obtiene con fecha.toISOString())

### Petición
```json
{
  "lugar": "UNIVO",
  "fecha": "2017-01-29T20:52:21.377Z",
  "monto": "300"
}
```

## Ejecutar Sorteo [POST]
http://localhost:8080/app-intercambio/webresources/api/sorteo

Permite generar el sorteo en el cual se realiza la asignación de participantes.
El proceso valida qué no exista otro en proceso por lo cual no es posible ejecutar 2 veces.

## Consultar pareja de intercambio [GET]
http://localhost:8080/app-intercambio/webresources/api/usuarios/{usuario}/pareja

Permite consultar el perfil de la pareja que fue asignada a un *usuario* posterior al proceso de sorteo.
Si no existe pareja asignada para el usuario (antes del sorteo) en el contenido de la respuesta encontrará un error.

### Respuesta
```json
{
    "idParticipante": "cbcf10e9-6eb2-46ee-b141-37161c772d56",
    "nombres": "Jesus23",
    "edad": 23,
    "sexo": "masculino",
    "grado": "1",
    "grupo": "a",
    "area": "area",
    "gustos": "x",
    "usuario": null,
    "opcionesIntercambio": null
}
```

## Consultar estado de invitación [GET]
http://localhost:8080/app-intercambio/webresources/api/usuarios/jast1000@gmail.com/invitacion

Permite mostrar la información del intercambio (lugar, fecha y monto) al que fue invitado. El atributo confirmacion permite conocer si la opción de confirmar será habilitada. Si es null significa que no hay confirmación aún. Si es true o false significa que ha aceptado o la ha rechazado.
Nota: La fecha se encuentra en long.

### Respuesta
```json
{
    "idRegla": null,
    "lugar": "UNIVO",
    "fecha": 1485723141377,
    "monto": 300,
    "estado": null,
    "confirmacion": null
}
```

## Confirmación de asistencia [PUT]
http://localhost:8080/app-intercambio/webresources/api/usuarios/{usuario}/invitacion

Permite confirmar la asistencia al intercambio de un usuario. Si la respuesta es 200 significa que ha sido guardada la conformación. Si el usuario no existe el status será 404 (bad request).

### Petición
```json
{
	"confirmacion": true
}
```