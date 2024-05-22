package co.edu.uniquindio.poo;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//se utiliza para representar una celda en una tabla que muestra imágenes en lugar de texto. Esta clase está diseñada para trabajar con objetos de tipo Evento y mostrar imágenes cuyos URLs son de tipo String. 
public class ImagenTablaCelda extends TableCell<Evento, String> {
    private final ImageView imageView = new ImageView();

    public ImagenTablaCelda() {
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            System.out.println("Loading image from URL: " + item);
            Image image = new Image(item);
            imageView.setImage(image);
            setGraphic(imageView);
        }
    }
}
