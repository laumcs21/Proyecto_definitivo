package co.edu.uniquindio.poo;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.Random;

public class EnviarCorreo {
    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String remitente = "lauram.cardenass@uqvirtual.edu.co";
    private final String password = "0108lomejor";

    //Este método envía un correo electrónico que contiene un código de activación generado aleatoriamente al destinatario especificado.
    public String enviarCorreo(String destinatario) {
        // Deshabilitar validación de certificados (solo para pruebas)
        TrustAllCertificates.disableSSL();

        // Generar código aleatorio
        String codigo = generarCodigoAleatorio();

        //  Configura las propiedades necesarias para la conexión con el servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host); // Agregar esta línea

        // La sesión se crea utilizando Session.getInstance, pasando las propiedades y un autenticador que utiliza el correo y la contraseña del remitente.
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        //çIntenta enviar un correo electrónico configurando un mensaje MIME con la información del remitente, destinatario, asunto y cuerpo del mensaje. Si el envío es exitoso, se imprime un mensaje de confirmación en la consola. Si ocurre un error durante el proceso, la excepción es capturada y se imprime un mensaje de error junto con la traza de la pila para facilitar la depuración.
        try {
            //  Se crea un mensaje MIME con el remitente, destinatario, asunto y cuerpo del mensaje
            Message message = new MimeMessage(session); //Es una implementación concreta de la clase Message que maneja mensajes de correo en formato MIME (Multipurpose Internet Mail Extensions).
            message.setFrom(new InternetAddress(remitente)); 
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Código de activación de cuenta"); 
            message.setText("Su código de activación de cuenta es: " + codigo); 

            // Envío del mensaje con transport.send
            Transport.send(message);
            System.out.println("Correo enviado satisfactoriamente.");
        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }

        return codigo; // Devuelve el código generado
    }

    public void enviarCorreoConCupon(String destinatario) {
        // Deshabilitar validación de certificados (solo para pruebas)
        TrustAllCertificates.disableSSL();

        // Configura las propiedades necesarias para la conexión con el servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host); // Agregar esta línea

        // Creación de la sesión
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            // Se crea un mensaje MIME con el remitente, destinatario, asunto y cuerpo del mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Código de Cupón de Bienvenida");
            message.setText("Gracias por registrarse. Aquí tiene su código de cupón de bienvenida: NUEVO15");

            // Envío del mensaje
            Transport.send(message);
            System.out.println("Correo con cupón enviado satisfactoriamente.");
        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generarCodigoAleatorio() {
        // Generar un código aleatorio de 6 dígitos
        Random rand = new Random();
        int codigo = rand.nextInt(900000) + 100000; // Número aleatorio entre 100000 y 999999
        return String.valueOf(codigo);
    }

    public static void main(String[] args) {
        EnviarCorreo enviarCorreo = new EnviarCorreo();
        String destinatario = "laumcs21@gmail.com";
        enviarCorreo.enviarCorreo(destinatario);
    }
}
