# InmobiliariaPro

Proyecto transversal de la materia **Programación en Dispositivos Móviles**.

Aplicación Android desarrollada en **Java**, basada en arquitectura **MVVM**, que consume una **API REST protegida mediante autenticación JWT** para la gestión de inmuebles, contratos y pagos asociados a propietarios.

---

## Integrantes del equipo

* Romanela Ricchiardi
* (Agregar integrantes)

---

## Objetivo del proyecto

Desarrollar una aplicación móvil que permita a propietarios consultar información sobre sus inmuebles, visualizar contratos vigentes y acceder al historial de pagos mediante el consumo de servicios web provistos por una API REST.

---

## Tecnologías utilizadas

* Java
* Android Studio
* Arquitectura MVVM
* Retrofit
* Gson
* Glide
* Navigation Drawer
* ViewModel + LiveData
* SharedPreferences (persistencia de sesión JWT)
* ViewBinding

---

## Funcionalidades implementadas (estado actual)

* Autenticación mediante JWT
* Persistencia de sesión del usuario
* Consumo de API REST con Retrofit
* Listado de inmuebles del propietario
* Visualización del perfil del propietario
* Edición de datos del perfil
* Navegación mediante Navigation Drawer

---

## Funcionalidades en desarrollo

* Detalle de inmueble
* Visualización de contratos
* Historial de pagos
* Filtro de inmuebles con contrato vigente
* Mejoras en la interfaz de usuario

---

## Arquitectura del proyecto

El proyecto sigue el patrón de arquitectura:

Model → Repository → ViewModel → UI

Esto permite mantener una correcta separación de responsabilidades y facilita el mantenimiento del código.

---

## API utilizada

La aplicación consume una API REST proporcionada en el marco de la materia, que permite:

* Autenticación de propietarios
* Consulta de perfil
* Consulta de inmuebles
* Consulta de contratos
* Consulta de pagos asociados

---

## Estado del proyecto

Proyecto en desarrollo activo como parte del trabajo práctico transversal de la asignatura.
