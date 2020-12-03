import javafx.scene.paint.Color;

public class ColorPalette {

    private Color skyColor;
    private Color groundColor;
    private Color mountainColor;

    public ColorPalette(Color skyColor, Color groundColor, Color mountainColor) {
        this.skyColor = skyColor;
        this.groundColor = groundColor;
        this.mountainColor = mountainColor;
    }

    public final ColorPalette EARTH = new ColorPalette(Color.BLUE, Color.BROWN, Color.ORANGE);

    public Color getSkyColor() {
        return this.skyColor;
    }

    public void setSkyColor(Color skyColor) {
        this.skyColor = skyColor;
    }

    public Color getGroundColor() {
        return this.groundColor;
    }

    public void setGroundColor(Color groundColor) {
        this.groundColor = groundColor;
    }

    public Color getMountainColor() {
        return this.mountainColor;
    }

    public void setMountainColor(Color mountainColor) {
        this.mountainColor = mountainColor;
    }

}