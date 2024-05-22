package co.edu.uniquindio.poo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Interfaz extends Application {

    private VBox root;
    private LoginCliente loginCliente;
    private LoginAdministrador loginAdministrador;
    private RegistroCliente registroCliente;
    private Administrador administrador;
    private EnviarCorreo enviarCorreo;
    private Label mensajeLabel;

    private boolean enRegistro = false;
    private List<Localidad> localidades = new ArrayList<>();
    private List<Evento> eventos = new ArrayList<>();
    private List<Cupon> cupones = new ArrayList<>();
    private List<Compra> compras = new ArrayList<>();

    private static final String FILE_CUPONES = "cupones.dat";
    private static final String FILE_EVENTOS = "eventos.dat";
    private static final String FILE_LOCALIDADES = "localidades.dat";
    private static final String FILE_COMPRAS = "compras.dat";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        cargarDatos();

        // Inicialización de los diferentes componentes de la aplicación.
        loginCliente = new LoginCliente(); // Maneja el proceso de inicio de sesión del cliente.
        enviarCorreo = new EnviarCorreo(); // Se encarga de enviar correos electrónicos.
        registroCliente = new RegistroCliente(enviarCorreo); // Maneja el proceso de registro del cliente y envíacorreos
                                                             // de confirmación.
        administrador = Administrador.obtenerInstancia(); // Obtiene una instancia única del administrador del sistema.
        loginAdministrador = new LoginAdministrador(administrador); // Maneja el inicio de sesión del administrador.

        // Crear el contenedor principal con un espacio de 10 píxeles entre los nodos.
        root = new VBox(10);
        // Establecer un padding de 20 píxeles alrededor del contenedor principal.
        root.setPadding(new Insets(20));

        // Crear y configurar el campo de texto para el usuario (correo electrónico).
        TextField usuarioField = new TextField();
        usuarioField.setPromptText("Correo electrónico");

        // Crear y configurar el campo de texto para la contraseña.
        PasswordField contraseñaField = new PasswordField();
        contraseñaField.setPromptText("Contraseña");

        // Crear y configurar el campo de texto para el nombre.
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");

        // Crear y configurar el campo de texto para la cédula.
        TextField cedulaField = new TextField();
        cedulaField.setPromptText("Cédula");

        // Crear y configurar el campo de texto para el teléfono.
        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Teléfono");

        // Crear y configurar el botón de acción (iniciar sesión).
        Button accionButton = new Button("Iniciar Sesión");

        // Crear y configurar el botón de registro.
        Button registroButton = new Button("Registrarse");

        // Crear y configurar el botón de volver a iniciar sesión.
        Button volverButton = new Button("Volver a Iniciar Sesión");

        // Crear y configurar la etiqueta para mostrar mensajes al usuario.
        mensajeLabel = new Label();

        // Establece la acción a realizar cuando se presiona el botón de registro.
        registroButton.setOnAction(e -> {
            if (!enRegistro) {
                // Si no está en modo registro, cambia la interfaz a la vista de registro.
                root.getChildren().clear();
                root.getChildren().addAll(nombreField, cedulaField, telefonoField, usuarioField, contraseñaField,
                        registroButton, volverButton, mensajeLabel);
                enRegistro = true;
                mensajeLabel.setVisible(false);
            } else {
                // Si está en modo registro, intenta registrar al usuario.
                String nombre = nombreField.getText();
                String cedula = cedulaField.getText();
                String telefono = telefonoField.getText();
                String usuario = usuarioField.getText();
                String contraseña = contraseñaField.getText();

                if (registroCliente.validarCorreoElectronico(usuario)) {
                    if (registroCliente.registrarUsuario(nombre, cedula, telefono, usuario, contraseña)) {
                        mensajeLabel.setText("Registro exitoso. Por favor, verifique su correo electrónico.");
                        mensajeLabel.setVisible(true);
                        // Limpia los campos de entrada después de un registro exitoso.
                        nombreField.clear();
                        cedulaField.clear();
                        telefonoField.clear();
                        usuarioField.clear();
                        contraseñaField.clear();
                    } else {
                        mensajeLabel.setText("El usuario ya está en uso");
                        mensajeLabel.setVisible(true);
                    }
                } else {
                    mensajeLabel.setText("El usuario debe ser una dirección de correo electrónico válida.");
                    mensajeLabel.setVisible(true);
                }
            }
        });

        // Establece la acción a realizar cuando se presiona el botón de volver.
        volverButton.setOnAction(e -> {
            // Cambia la interfaz de regreso a la vista de inicio de sesión.
            root.getChildren().clear();
            root.getChildren().addAll(usuarioField, contraseñaField, accionButton, registroButton, mensajeLabel);
            enRegistro = false;
            mensajeLabel.setVisible(false);
        });

        // Establece la acción a realizar cuando se presiona el botón de iniciar sesión.
        accionButton.setOnAction(e -> {
            String usuario = usuarioField.getText();
            String contraseña = contraseñaField.getText();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                mensajeLabel.setText("Por favor, complete todos los campos.");
                mensajeLabel.setVisible(true);
            } else {
                boolean isAdminLoggedIn = loginAdministrador.login(usuario, contraseña);
                boolean isClientLoggedIn = false;

                if (isAdminLoggedIn) {
                    mensajeLabel.setText("Inicio de sesión exitoso como administrador.");
                    mensajeLabel.setVisible(true);
                    mostrarPantallaAdministrador();
                } else {
                    if (!registroCliente.haIniciadoSesionAntes(usuario)) {
                        mostrarVentanaCodigoVerificacion(usuario, contraseña);
                    }
                    isClientLoggedIn = loginCliente.login(usuario, contraseña);

                    if (isClientLoggedIn) {
                        mensajeLabel.setText("Inicio de sesión exitoso como cliente.");
                        mensajeLabel.setVisible(true);
                        mostrarPantallaCliente();
                    } else {
                        mensajeLabel.setText("Credenciales incorrectas.");
                        mensajeLabel.setVisible(true);
                    }
                }
            }
        });

        // Agrega los campos de usuario, contraseña, botones de acción y el label de
        // mensajes al contenedor principal.
        root.getChildren().addAll(usuarioField, contraseñaField, accionButton, registroButton, mensajeLabel);

        // Configura y muestra la escena principal.
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.setTitle("Login y Registro");
        primaryStage.setOnCloseRequest(event -> guardarDatos()); // Guarda los datos al cerrar la aplicación.
        primaryStage.show();
    }

    // Muestra un cuadro de diálogo para que el usuario ingrese un código de verificación enviado a su correo electrónico
    private void mostrarVentanaCodigoVerificacion(String usuario, String contraseña) {
        // Crear un cuadro de diálogo para ingresar el código de verificación.
        TextInputDialog dialogoCodigo = new TextInputDialog();
        dialogoCodigo.setTitle("Verificación de Código");
        dialogoCodigo.setHeaderText("Ingrese el código de verificación enviado a su correo electrónico:");

        // Mostrar el cuadro de diálogo y esperar a que el usuario ingrese el código.
        Optional<String> resultado = dialogoCodigo.showAndWait();

        // Si el usuario ingresó un código y presionó OK.
        if (resultado.isPresent()) {
            String codigoIngresado = resultado.get();
            // Verificar si el código ingresado es correcto.
            if (registroCliente.verificarCodigoVerificacion(usuario, codigoIngresado)) {
                mensajeLabel.setText("Código de verificación correcto. Iniciando sesión...");
                // Intentar iniciar sesión como cliente o administrador.
                if (loginCliente.login(usuario, contraseña) || loginAdministrador.login(usuario, contraseña)) {
                    mensajeLabel.setText("Inicio de sesión exitoso.");
                    mostrarPantallaAdministrador(); // Llamar a mostrarPantallaAdministrador después de un inicio de
                                                    // sesión exitoso.
                } else {
                    mensajeLabel.setText("Error al iniciar sesión después de la verificación.");
                }
            } else {
                mensajeLabel.setText("Código de verificación incorrecto.");
            }
            mensajeLabel.setVisible(true);
        }
    }

    private void mostrarPantallaAdministrador() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear los botones de acción para el administrador.
        Button crearEventoButton = new Button("Crear Evento");
        Button crearLocalidadButton = new Button("Crear Localidad");
        Button crearCuponButton = new Button("Crear Cupón");
        Button eliminarCuponButton = new Button("Eliminar Cupón");
        Button eliminarEventoButton = new Button("Eliminar Evento");
        mensajeLabel = new Label();

        // Establecer las acciones para cada botón.
        crearEventoButton.setOnAction(e -> mostrarCrearEvento());
        crearLocalidadButton.setOnAction(e -> mostrarCrearLocalidad());
        crearCuponButton.setOnAction(e -> mostrarCrearCupon());
        eliminarCuponButton.setOnAction(e -> eliminarCupon());
        eliminarEventoButton.setOnAction(e -> eliminarEvento());

        // Crear la tabla de eventos y configurar sus columnas.
        TableView<Evento> eventosTable = new TableView<>(FXCollections.observableArrayList(eventos));

        TableColumn<Evento, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Evento, String> ciudadCol = new TableColumn<>("Ciudad");
        ciudadCol.setCellValueFactory(new PropertyValueFactory<>("ciudad"));

        TableColumn<Evento, String> descripcionCol = new TableColumn<>("Descripción");
        descripcionCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Evento, TipoEvento> tipoCol = new TableColumn<>("Tipo");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipoEvento"));

        TableColumn<Evento, String> imagenCol = new TableColumn<>("Imagen");
        imagenCol.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        imagenCol.setCellFactory(column -> new ImagenTablaCelda());

        TableColumn<Evento, LocalDate> fechaCol = new TableColumn<>("Fecha");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Evento, String> direccionCol = new TableColumn<>("Dirección");
        direccionCol.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Crear una columna para mostrar la cantidad de localidades.
        TableColumn<Evento, Integer> localidadesCol = new TableColumn<>("Localidades");
        localidadesCol.setCellValueFactory(cellData -> {
            int numLocalidades = cellData.getValue().getLocalidades().size();
            return new SimpleIntegerProperty(numLocalidades).asObject();
        });

        // Agregar todas las columnas a la tabla de eventos.
        eventosTable.getColumns().addAll(nombreCol, ciudadCol, descripcionCol, tipoCol, imagenCol, fechaCol,
                direccionCol, localidadesCol);

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(crearEventoButton, crearLocalidadButton, crearCuponButton, eliminarCuponButton,
                eliminarEventoButton, new Label("Eventos:"), eventosTable, mensajeLabel);
    }

    //Muestra la interfaz gráfica para que el administrador pueda ingresar los detalles de un nuevo evento y crearlo.
    private void mostrarCrearEvento() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear campos de texto para ingresar los detalles del evento.
        TextField nombreEventoField = new TextField();
        nombreEventoField.setPromptText("Nombre del evento");

        TextField ciudadField = new TextField();
        ciudadField.setPromptText("Ciudad");

        TextField descripcionField = new TextField();
        descripcionField.setPromptText("Descripción");

        // Crear un ComboBox para seleccionar el tipo de evento.
        ComboBox<TipoEvento> tipoComboBox = new ComboBox<>();
        tipoComboBox.setItems(FXCollections.observableArrayList(TipoEvento.values()));
        tipoComboBox.setPromptText("Tipo (concierto, teatro, deporte, festival, otro)");

        // Crear un campo de texto para la URL de la imagen.
        TextField imagenField = new TextField();
        imagenField.setPromptText("URL de la imagen");

        // Crear un DatePicker para seleccionar la fecha del evento.
        DatePicker fechaPicker = new DatePicker();
        fechaPicker.setPromptText("Fecha del evento");

        // Crear un campo de texto para la dirección del evento.
        TextField direccionField = new TextField();
        direccionField.setPromptText("Dirección");

        // Crear un botón para crear el evento.
        Button crearButton = new Button("Crear Evento");

        // Crear un botón para volver a la pantalla de administración.
        Button volverButton = new Button("Volver");

        // Configurar la acción para el botón de crear evento.
        crearButton.setOnAction(e -> {
            // Obtener los valores ingresados por el usuario.
            String nombre = nombreEventoField.getText();
            String ciudad = ciudadField.getText();
            String descripcion = descripcionField.getText();
            TipoEvento tipo = tipoComboBox.getValue();
            String imagen = imagenField.getText();
            LocalDate fecha = fechaPicker.getValue();
            String direccion = direccionField.getText();

            // Crear un nuevo objeto Evento con los valores ingresados.
            Evento evento = new Evento(nombre, ciudad, descripcion, tipo, imagen, fecha, direccion, new ArrayList<>());
            // Agregar el evento a la lista de eventos y llamar al método del administrador
            // para crear el evento.
            eventos.add(evento);
            administrador.crearEvento(evento);

            // Mostrar un mensaje de éxito y volver a la pantalla de administración.
            mensajeLabel.setText("Evento creado exitosamente.");
            mensajeLabel.setVisible(true);
            mostrarPantallaAdministrador();
        });

        // Configurar la acción para el botón de volver.
        volverButton.setOnAction(e -> mostrarPantallaAdministrador());

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(nombreEventoField, ciudadField, descripcionField, tipoComboBox, imagenField,
                fechaPicker, direccionField, crearButton, volverButton, mensajeLabel);
    }

    //Muestra la interfaz gráfica para que el administrador pueda agregar una nueva localidad a un evento existente.
    private void mostrarCrearLocalidad() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear un ComboBox para seleccionar el evento al que se añadirá la localidad.
        ComboBox<Evento> eventoComboBox = new ComboBox<>(FXCollections.observableArrayList(eventos));
        eventoComboBox.setPromptText("Seleccione un evento");

        // Crear campos de texto para ingresar los detalles de la localidad.
        TextField nombreLocalidadField = new TextField();
        nombreLocalidadField.setPromptText("Nombre de la localidad");

        TextField precioField = new TextField();
        precioField.setPromptText("Precio de la localidad");

        TextField capacidadField = new TextField();
        capacidadField.setPromptText("Capacidad máxima de la localidad");

        // Crear un botón para agregar la localidad.
        Button agregarButton = new Button("Agregar Localidad");

        // Crear un botón para volver a la pantalla de administración.
        Button volverButton = new Button("Volver");

        // Configurar la acción para el botón de agregar localidad.
        agregarButton.setOnAction(e -> {
            // Obtener los valores ingresados por el usuario.
            Evento eventoSeleccionado = eventoComboBox.getValue();
            String nombreLocalidad = nombreLocalidadField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int capacidad = Integer.parseInt(capacidadField.getText());

            // Crear un nuevo objeto Localidad con los valores ingresados.
            Localidad localidad = new Localidad(nombreLocalidad, precio, capacidad);
            // Agregar la localidad al evento seleccionado.
            eventoSeleccionado.getLocalidades().add(localidad);

            // Mostrar un mensaje de éxito y volver a la pantalla de administración.
            mensajeLabel.setText("Localidad agregada exitosamente.");
            mensajeLabel.setVisible(true);
            mostrarPantallaAdministrador();
        });

        // Configurar la acción para el botón de volver.
        volverButton.setOnAction(e -> mostrarPantallaAdministrador());

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(eventoComboBox, nombreLocalidadField, precioField, capacidadField, agregarButton,
                volverButton, mensajeLabel);
    }

    //Muestra la interfaz gráfica para que el administrador pueda crear un nuevo cupón.
    private void mostrarCrearCupon() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear campos de texto para ingresar los detalles del cupón.
        TextField nombreCuponField = new TextField();
        nombreCuponField.setPromptText("Nombre del cupón");

        TextField codigoCuponField = new TextField();
        codigoCuponField.setPromptText("Código del cupón");

        // Crear un DatePicker para seleccionar la fecha de expiración del cupón.
        DatePicker fechaPicker = new DatePicker();
        fechaPicker.setPromptText("Fecha de expiración");

        // Crear un campo de texto para el porcentaje de descuento del cupón.
        TextField descuentoField = new TextField();
        descuentoField.setPromptText("Porcentaje de descuento");

        // Crear un botón para crear el cupón.
        Button crearButton = new Button("Crear Cupón");

        // Crear un botón para volver a la pantalla de administración.
        Button volverButton = new Button("Volver");

        // Configurar la acción para el botón de crear cupón.
        crearButton.setOnAction(e -> {
            // Obtener los valores ingresados por el usuario.
            String nombre = nombreCuponField.getText();
            String codigo = codigoCuponField.getText();
            LocalDate fechaExpiracion = fechaPicker.getValue();
            double descuento = Double.parseDouble(descuentoField.getText());

            // Crear un nuevo objeto Cupon con los valores ingresados.
            Cupon cupon = new Cupon(nombre, codigo, fechaExpiracion, descuento);
            // Agregar el cupón a la lista de cupones.
            cupones.add(cupon);

            // Mostrar un mensaje de éxito y volver a la pantalla de administración.
            mensajeLabel.setText("Cupón creado exitosamente.");
            mensajeLabel.setVisible(true);
            mostrarPantallaAdministrador();
        });

        // Configurar la acción para el botón de volver.
        volverButton.setOnAction(e -> mostrarPantallaAdministrador());

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(nombreCuponField, codigoCuponField, fechaPicker, descuentoField, crearButton,
                volverButton, mensajeLabel);
    }

    //Muestra la interfaz gráfica para que el administrador pueda eliminar un cupón existente.
    private void eliminarCupon() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear un ComboBox para seleccionar el cupón a eliminar.
        ComboBox<Cupon> cuponComboBox = new ComboBox<>(FXCollections.observableArrayList(cupones));
        cuponComboBox.setPromptText("Seleccione un cupón");

        // Crear un botón para eliminar el cupón.
        Button eliminarButton = new Button("Eliminar Cupón");

        // Crear un botón para volver a la pantalla de administración.
        Button volverButton = new Button("Volver");

        // Configurar la acción para el botón de eliminar cupón.
        eliminarButton.setOnAction(e -> {
            // Obtener el cupón seleccionado.
            Cupon cuponSeleccionado = cuponComboBox.getValue();
            // Eliminar el cupón de la lista de cupones.
            cupones.remove(cuponSeleccionado);

            // Mostrar un mensaje de éxito y volver a la pantalla de administración.
            mensajeLabel.setText("Cupón eliminado exitosamente.");
            mensajeLabel.setVisible(true);
            mostrarPantallaAdministrador();
        });

        // Configurar la acción para el botón de volver.
        volverButton.setOnAction(e -> mostrarPantallaAdministrador());

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(cuponComboBox, eliminarButton, volverButton, mensajeLabel);
    }

    //Muestra la interfaz gráfica para que el administrador pueda eliminar un evento existente.
    private void eliminarEvento() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear un ComboBox para seleccionar el evento a eliminar.
        ComboBox<Evento> eventoComboBox = new ComboBox<>(FXCollections.observableArrayList(eventos));
        eventoComboBox.setPromptText("Seleccione un evento");

        // Crear un botón para eliminar el evento.
        Button eliminarButton = new Button("Eliminar evento");

        // Crear un botón para volver a la pantalla de administración.
        Button volverButton = new Button("Volver");

        // Configurar la acción para el botón de eliminar evento.
        eliminarButton.setOnAction(e -> {
            // Obtener el evento seleccionado.
            Evento eventoSeleccionado = eventoComboBox.getValue();
            // Eliminar el evento de la lista de eventos.
            eventos.remove(eventoSeleccionado);

            // Mostrar un mensaje de éxito y volver a la pantalla de administración.
            mensajeLabel.setText("Evento eliminado exitosamente.");
            mensajeLabel.setVisible(true);
            mostrarPantallaAdministrador();
        });

        // Configurar la acción para el botón de volver.
        volverButton.setOnAction(e -> mostrarPantallaAdministrador());

        // Agregar todos los componentes al contenedor principal.
        root.getChildren().addAll(eventoComboBox, eliminarButton, volverButton, mensajeLabel);
    }

    //Muestra la interfaz gráfica principal para los clientes, proporcionando opciones para ver sus compras realizadas y realizar nuevas compras.
    private void mostrarPantallaCliente() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Ocultar el mensaje de etiqueta.
        mensajeLabel.setVisible(false);

        // Crear botones para ver compras y realizar nuevas compras.
        Button verComprasButton = new Button("Ver Compras");
        Button comprarButton = new Button("Comprar");

        // Configurar la acción para el botón de ver compras.
        verComprasButton.setOnAction(e -> mostrarCompras());

        // Configurar la acción para el botón de realizar una compra.
        comprarButton.setOnAction(e -> mostrarCompra());

        // Agregar los botones al contenedor principal.
        root.getChildren().addAll(verComprasButton, comprarButton, mensajeLabel);
    }

    //Muestra una interfaz donde el cliente puede visualizar todas las compras que ha realizado.
    private void mostrarCompras() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Ocultar el mensaje de etiqueta.
        mensajeLabel.setVisible(false);

        // Crear una tabla para mostrar las compras.
        TableView<Compra> comprasTable = new TableView<>(FXCollections.observableArrayList(compras));

        // Definir las columnas de la tabla.

        // Columna para el nombre del cliente.
        TableColumn<Compra, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNombre()));

        // Columna para el nombre del evento.
        TableColumn<Compra, String> eventoCol = new TableColumn<>("Evento");
        eventoCol
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getNombre()));

        // Columna para el número de localidades compradas.
        TableColumn<Compra, Integer> numLocalidadesCol = new TableColumn<>("Localidades");
        numLocalidadesCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroLocalidades()).asObject());

        // Columna para el total de la compra.
        TableColumn<Compra, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalCompra()).asObject());

        // Columna para la fecha de la compra.
        TableColumn<Compra, LocalDate> fechaCol = new TableColumn<>("Fecha");
        fechaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFechaCompra()));

        // Añadir todas las columnas a la tabla.
        comprasTable.getColumns().addAll(nombreCol, eventoCol, numLocalidadesCol, totalCol, fechaCol);

        // Crear un botón para volver a la pantalla principal del cliente.
        Button volverButton = new Button("Volver");
        volverButton.setOnAction(e -> mostrarPantallaCliente());

        // Añadir la tabla y el botón al contenedor principal.
        root.getChildren().addAll(comprasTable, volverButton);
    }

    //Muestra una interfaz para que el cliente seleccione un evento y una localidad, e ingrese la cantidad de localidades que desea comprar. Además, permite la aplicación de un cupón de descuento.
    private void mostrarCompra() {
        // Limpiar todos los componentes del contenedor principal.
        root.getChildren().clear();

        // Crear un ChoiceBox para seleccionar un evento.
        ChoiceBox<Evento> eventosChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(eventos));

        // Crear un ChoiceBox para seleccionar una localidad del evento seleccionado.
        ChoiceBox<Localidad> localidadesChoiceBox = new ChoiceBox<>();

        // Añadir un listener al ChoiceBox de eventos para cargar las localidades
        // correspondientes.
        eventosChoiceBox.setOnAction(e -> {
            Evento eventoSeleccionado = eventosChoiceBox.getValue();
            if (eventoSeleccionado != null) {
                localidadesChoiceBox.setItems(FXCollections.observableArrayList(eventoSeleccionado.getLocalidades()));
            }
        });

        // Crear campos de texto para ingresar la cantidad de localidades y un cupón de
        // descuento.
        TextField cantidadLocalidadesField = new TextField();
        cantidadLocalidadesField.setPromptText("Número de localidades");

        TextField cuponField = new TextField();
        cuponField.setPromptText("Cupón de descuento");

        // Crear botones para realizar la compra y cancelar la operación.
        Button realizarCompraButton = new Button("Realizar Compra");
        Button cancelarButton = new Button("Cancelar");

        // Configurar el botón de realizar compra para procesar la compra cuando sea
        // presionado.
        realizarCompraButton.setOnAction(e -> {
            Evento evento = eventosChoiceBox.getValue();
            Localidad localidad = localidadesChoiceBox.getValue();
            int numLocalidades;

            // Validar que el número de localidades sea un entero válido.
            try {
                numLocalidades = Integer.parseInt(cantidadLocalidadesField.getText());
            } catch (NumberFormatException ex) {
                mensajeLabel.setText("Por favor, ingrese un número válido de localidades.");
                mensajeLabel.setVisible(true);
                return;
            }

            // Obtener el cupón de descuento ingresado.
            String cuponCodigo = cuponField.getText();
            Cupon cupon = cupones.stream()
                    .filter(c -> c.getCodigo().equals(cuponCodigo))
                    .findFirst()
                    .orElse(null);

            // Verificar que se haya seleccionado un evento y una localidad, y que el número
            // de localidades sea mayor a 0.
            if (evento != null && localidad != null && numLocalidades > 0) {
                // Verificar si hay capacidad suficiente en la localidad seleccionada.
                if (localidad.hayCapacidadDisponible(numLocalidades)) {
                    double total = localidad.getPrecio() * numLocalidades;
                    double subtotal = total;

                    // Aplicar el descuento del cupón si se ingresó uno válido.
                    if (cupon != null) {
                        total *= (1 - cupon.getPorcentajeDescuento() / 100);
                    }

                    // Crear la factura de la compra.
                    Factura factura = new Factura(subtotal, total);
                    Cliente clienteActual = loginCliente.getUsuarioActual();
                    Compra compra = new Compra(clienteActual, evento, localidad, cupon, factura);
                    compra.setNumeroLocalidades(numLocalidades);
                    compra.setTotalCompra(total);
                    compra.setFechaCompra(LocalDate.now());

                    // Añadir la compra a la lista y guardar los datos.
                    compras.add(compra);
                    guardarDatos();

                    mensajeLabel.setText("Compra realizada exitosamente. Total: $" + total);
                    mensajeLabel.setVisible(true);

                    // Volver a la pantalla principal del cliente.
                    mostrarPantallaCliente();
                } else {
                    mensajeLabel.setText("No hay capacidad suficiente en la localidad seleccionada.");
                    mensajeLabel.setVisible(true);
                }
            } else {
                mensajeLabel
                        .setText("Por favor, seleccione un evento, una localidad y un número válido de localidades.");
                mensajeLabel.setVisible(true);
            }
        });

        // Configurar el botón de cancelar para volver a la pantalla principal del
        // cliente.
        cancelarButton.setOnAction(e -> mostrarPantallaCliente());

        // Añadir todos los componentes a la interfaz.
        root.getChildren().addAll(
                new Label("Seleccionar Evento"), eventosChoiceBox,
                new Label("Seleccionar Localidad"), localidadesChoiceBox,
                cantidadLocalidadesField, cuponField,
                realizarCompraButton, cancelarButton, mensajeLabel);
    }

    //se encarga de guardar los cupones, eventos, localidades y compras en archivos separados utilizando el método guardarObjeto() para serializar las listas de objetos y escribirlas en archivos de datos.
    private void guardarDatos() {
        //Guardar los cupones, eventos, localidades y compras en archivos separados.
        guardarObjeto(FILE_CUPONES, cupones);
        guardarObjeto(FILE_EVENTOS, eventos);
        guardarObjeto(FILE_LOCALIDADES, localidades);
        guardarObjeto(FILE_COMPRAS, compras);
    }

    // carga los cupones, eventos, localidades y compras desde archivos de datos utilizando el método cargarObjeto() para deserializar las listas de objetos desde los archivos correspondientes.
    private void cargarDatos() {
        // Cargar los cupones, eventos, localidades y compras desde archivos separados.
        cupones = cargarObjeto(FILE_CUPONES);
        eventos = cargarObjeto(FILE_EVENTOS);
        localidades = cargarObjeto(FILE_LOCALIDADES);
        compras = cargarObjeto(FILE_COMPRAS);
    }


    //se encarga de guardar una lista de objetos en un archivo utilizando la serialización de objetos.
    private <T> void guardarObjeto(String fileName, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //carga una lista de objetos desde un archivo especificado utilizando la deserialización de objetos.
    @SuppressWarnings("unchecked")// Esta anotación se usa para suprimir las advertencias del compilador relacionadas con la conversión no verificada de tipos.
    private <T> List<T> cargarObjeto(String fileName) {
        // se encarga de abrir un ObjectInputStream para leer objetos serializados desde el archivo especificado. El recurso ObjectInputStream se cierra automáticamente al salir del bloque try, garantizando una gestión adecuada de recursos.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        } 
        //Si ocurre alguna excepción durante el proceso de lectura o deserialización, se captura la excepción.
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
