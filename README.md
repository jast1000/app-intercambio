# API app-intercambio

## Identificar usuario [POST]

http://localhost:8080/app-intercambio/webresources/api/usuarios/identificacion

Permite identificar un usuario si existe en el sistema, si este existe muestra su información de lo contrario el status de la petición será distinto a exitoso.


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

## Listar usuarios [GET]

http://localhost:8080/app-intercambio/webresources/api/usuarios/participacion/*estado*

Lista los usuarios registrados en el sistema, recibe un parámetro opcional *estado*:
* 1 Usuarios que aún no registran su perfil y no participan en el intercambio
* 2 Usuarios que cuentan con perfil y han aceptado el intercambio
* 3 Usuarios que cuentan con perfil y no aceptaron el intercambio

### Respuesta
```json
[
    {
        "usuario": "jast1000@gmail.com",
        "password": null,
        "tipoUsuario": 1
    }
]
```

## Registrar usuario [POST]

http://localhost:8080/app-intercambio/webresources/api/usuarios


Permite dar de alta (invitar) a un usuario. El tipo debe ser 2. (Tipo 1 únicamente para administrador).

### Solicitud
```json
{
  "usuario": "jalbertostecalco@gmail.com",
  "tipoUsuario": "2"
}
```

## Eliminar un usuario [DELETE]

http://localhost:8080/app-intercambio/webresources/api/usuarios/*usuario*


Permite eliminar un usuario, donde usuario es el correo electrónico (id) del usuario.

## Consultar perfil de participante [GET]

http://localhost:8080/app-intercambio/webresources/api/participantes/*usuario*

Permite consultar el perfil de un participante, si este existe se obtiene su información, en caso contrario el status será distinto a 200.


### Respuesta
```json
{
    "idParticipante": "308d5295-a31c-4a91-944f-364154a5ea03",
    "nombres": "Jesús Alberto",
    "edad": 23,
    "sexo": "Masculino",
    "grado": "NA",
    "grupo": "NA",
    "area": "Sistemas",
    "gustos": "* Tacos al pastor",
    "usuario": "jast1000@gmail.com"
}
```

## Guardar perfil participante [POST]

http://localhost:8080/app-intercambio/webresources/api/participantes

Guardar el perfil de un participante. Si al consultar un perfil no existe respuesta para un usuario pero si se encuentra registrado, esta función debe llamarse para crear su perfil. El atributo usuario indica a qué usuario pertenece el perfil.

### Solicitud
```json
{
    "nombres": "Juan",
    "edad": 25,
    "sexo": "Masculino",
    "grado": "NA",
    "grupo": "NA",
    "area": "Contabilidad",
    "gustos": "* Futbol",
    "usuario": "juan@gmail.com"
}
```

## Modificar perfil participante [PUT]

http://localhost:8080/app-intercambio/webresources/api/participantes

Actualiza los datos del perfil de un participante.

### Solicitud
```json
{
    "nombres": "Juan Ramón",
    "edad": 25,
    "sexo": "Masculino",
    "grado": "1",
    "grupo": "A",
    "area": "Computación",
    "gustos": "* Futbol",
    "usuario": "juan@gmail.com"
}
```