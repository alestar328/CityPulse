# Origen de la idea

**CityPulse** es una aplicación orientada a los usuarios aventureros que les gustaría localizar eventos o lugares de ocio cercanos (o no tan cercanos).

Es una aplicación que utiliza un mapa como página principal, en el cual se mostrarán los puntos de interés creados por otros usuarios. Estos puntos de interés pueden ser desde fiestas hasta eventos gastronómicos o socioculturales. Los usuarios podrán apuntarse u organizar estos "eventos" desde la propia aplicación. También dispondrán de una "lista de amigos" en la cual podrán verificar si algún amigo asistirá al mismo evento que ellos.

Los usuarios también dispondrán de varios filtros para poder encontrar el evento ideal, principalmente podrán filtrar por: distancia, tipo de evento y valoración del organizador. Los usuarios dispondrán de dos tipos principales de usuario: el usuario estándar y el usuario organizador.

- **Usuario estándar**: Podrá apuntarse a eventos y agregar a amigos.
- **Usuario organizador/asociación**: Tendrá las mismas características que el estándar junto a la adición de poder crear y gestionar eventos. Si esta función se le da un uso no deseado, podrá ser vetado de la aplicación indefinidamente o tendrá un impacto negativo en su cuenta.

---

# Requerimientos de CityPulse

## **Categorías de Requerimientos**

### **1. Autenticación y Gestión de Usuarios**
- **RF01**: "Login": Se podrá logear cualquier usuario.

![Login Screen](https://drive.google.com/uc?export=view&id=1aEK6xUqxdZHFbHrP2sIqLTzpOb71xlR-)

- **RF02**: "Registro": Mediante redes sociales o correo electrónico.

![Register Screen 1](https://drive.google.com/uc?export=view&id=1wfIin2QSDH0EONqwx4FOyZHKwq9dZrUe)

![Register Screen 2](https://drive.google.com/uc?export=view&id=1Co4h9_cLI85iLUt4TAZYXTOs2NFj12ET)

![Register Screen 3](https://drive.google.com/uc?export=view&id=1wL7yB5HFa9r8vqouOMk4TsqzVvoe7hOE)

- **RF03**: "Recuperar contraseña": Redirección a cuenta de email.

![RecuperarContraseña](https://drive.google.com/uc?export=view&id=14BRuSe1Iayq9inL19NLKau_f9r5DI4h-)

- **RF26**: "Login y Register con Google Sign In".

![Image Description](https://drive.google.com/uc?export=view&id=1BtPon-qQ3YgNV5NyVT3OF1V5bqZrU1vu)

- **RF30**: "Editar perfil de usuario": El usuario podrá modificar sus datos.

![Profile Screen](https://drive.google.com/uc?export=view&id=1BtPon-qQ3YgNV5NyVT3OF1V5bqZrU1vu)

- **RF32 propio**: El usuario podrá acceder y gestionar una "Friends List".

![Friends List Screen](https://drive.google.com/uc?export=view&id=1j8jeEgebzq90YJ5AzAPKen72up9nel0I)
---

### **2. Eventos (Ítems)**
- **RF10**: "Crear nuevo ítem": El usuario podrá crear un nuevo evento.

![Create Event Screen](https://drive.google.com/uc?export=view&id=1F0p163t8v_7na9jv2HfF4K8TxaroRk2K)

- **RF11**: "Listar ítems": Se podrá listar los eventos en función de la zona.

![List Events Screen](https://drive.google.com/uc?export=view&id=1kSNKK50ow1WZW6SnQQu-Jqo3TrTdbAgb)

- **RF12**: "Filtrar ítems por categoría": Tenemos 3 categorías: Fiesta, cultura y juegos.

![Filter by Category Screen](https://drive.google.com/uc?export=view&id=1EgyE7VNvQUYK2g3btwbhiNurSpVO4Inu)

- **RF13**: "Filtrar ítems por nombre": Filtrado por coincidencia de palabras.
- **RF14**: "Filtrar ítems por distancia": Filtramos por ubicación del usuario.
- **RF15**: "Filtrar por el número de estrellas de valoración": El usuario podrá puntuar los eventos.
- **RF16**: "Ordenar por valoración": Ordenar por mejor o peor puntuación.
- **RF17**: "Ordenar ítems por distancia": Ordenar por distancia en km.
- **RF18**: "Ordenar ítems por fecha de creación reciente": Usuario administrador podrá ordenar su lista de eventos creados.
- **RF19**: "Ampliar información del ítem": Todos los usuarios podrán ampliar la información del evento seleccionado.

![Additional Information Screen](https://drive.google.com/uc?export=view&id=1NL7rIjY_pjvRa3kFVIKfbhmbnr8AalAm)

- **RF20**: "Crear valoración del ítem": Los usuarios podrán valorar los eventos.
- **RF21**: "Mostrar valoración del ítem": Los usuarios podrán ver la valoración de los eventos.
- **RF22**: "Modificar ítem": El usuario podrá modificar su evento creado.

- **RF23**: "Eliminar ítem": El usuario podrá eliminar su evento creado.

- **RF24**: "Eliminar valoración": El usuario podrá eliminar la valoración que ha hecho a un evento.
- **RF33 propio**: El usuario podrá marcar algunos eventos a una lista de favoritos.

---

### **3. Filtros y Búsquedas**
- **RF04**: "Crear nueva subcategoría": Crear nueva categoría de evento.
- **RF05**: "Listar categorías": Listar categoría de eventos por orden alfabético/puntuación.
- **RF06**: "Filtrar categorías": Filtrar por palabra, puntuación y categoría.
- **RF07**: "Ampliar información de categoría": Al seleccionar categoría o evento.
- **RF08**: "Modificar categoría": El usuario deberá de poder cambiar la categoría de su evento.

---

### **4. Comentarios **
- **RF25**: "Censurar comentario": Los usuarios podrán ocultar comentarios. --> Falta por realizar

---

### **5. Requerimientos No Funcionales**
- **RN01**: La app debe estar en catalán, castellano e inglés.
- **RN02**: Diseño visual (UI) atractivo, con coherencia en colores, fuentes, iconos, buena distribución y agrupación de componentes. El diseño debe ser el mismo para todas las pantallas.
- **RN03**: Responsividad: En caso de variar el tamaño de la pantalla del móvil (no es necesario para tablets), el contenido debe adaptarse de forma proporcional.
- **RN04**: Usabilidad (UX): Interfaz amigable, efectiva e intuitiva. Debe quedar muy claro qué se puede hacer. Además, debe haber coherencia entre las funcionalidades disponibles y no disponibles en cada momento, evitando que el usuario pueda realizar incoherencias funcionales.
- **RN05**: App accesible, fácil de usar y que tenga en cuenta discapacidades visuales, motrices, dislexia, etc., como por ejemplo tipografías de alto contraste o tamaño grande.
- **RN06**: Fluidez: La app debe responder a las entradas del usuario en todo momento. Esto significa que en ningún caso puede quedarse "congelada" mientras realiza cualquier operación.
- **RN07**: App eficiente: No debe haber pasos innecesarios entre lo que se quiere hacer y cómo hacerlo.
- **RN08**: Gestión de excepciones: Todas las posibles situaciones excepcionales deben gestionarse de forma correcta y proporcionar mensajes de error descriptivos y útiles para el usuario cuando ocurra algún fallo.
- **RN09**: Se deben usar las clases, interfaces y métodos de forma óptima y adecuada.
- **RN10**: Se deben usar estructuras de paquetes de manera adecuada.
- **RN11**: El código debe estar optimizado, ser eficiente y sin redundancias.
- **RN12**: Todas las entradas por teclado del usuario deben validarse y filtrarse para garantizar que los datos recopilados sean correctos, coherentes y seguros.
- **RN13**: Todas las cabeceras de métodos y clases deben estar debidamente comentadas en formato JavaDOC.
- **RN14**: Se debe usar Firebase para la gestión de bases de datos y archivos.
- **RN15**: Se debe usar Firebase para la autenticación.
- **RN16**: Se deben utilizar Jetpack Compose y vistas en XML para implementar la interfaz gráfica.
- **RN17**: Se debe usar un menú de navegación inferior (Bottom Navigation) para la navegación hacia las funcionalidades principales.

---

### ** 6. Posibles Expansiones **
- 1 - Posible aumento en los tipos de categorias principales.
- 2 - Minijuegos dentro de la aplicación.
- 3 - Agregar sistema de comentarios.
