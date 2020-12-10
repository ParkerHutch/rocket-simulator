package design;
import javafx.scene.paint.Color;

/**
 * A class used to store data for colors to be used in the simulation.
 */
public class ColorPalette {

    private String name; // The name of the color palette
    private Color skyColor;
    private Color groundColor;
    private Color mountainColor;

    public ColorPalette(String name, Color skyColor, Color groundColor, Color mountainColor) {
        this.name = name;
        this.skyColor = skyColor;
        this.groundColor = groundColor;
        this.mountainColor = mountainColor;
    }

    public static final ColorPalette EARTH = new ColorPalette(
        "Earth", Color.DEEPSKYBLUE, Color.GREEN, Color.SADDLEBROWN);
    public static final ColorPalette MARS = new ColorPalette(
        "Mars", Color.ORANGERED, Color.ORANGE, Color.BROWN);
    public static final ColorPalette NIGHT = new ColorPalette(
        "Night", Color.DARKSLATEGRAY, Color.GREEN, Color.SADDLEBROWN);

    /**
     * Gets the name of the color palette.
     * @return the palette's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the color palette.
     * @param name the palette's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the palette's sky color.
     * @return the sky color
     */
    public Color getSkyColor() {
        return this.skyColor;
    }

    /**
     * Sets the palette's sky color.
     * @param skyColor the sky color
     */
    public void setSkyColor(Color skyColor) {
        this.skyColor = skyColor;
    }

    /**
     * Gets the palette's ground color.
     * @return the ground color
     */
    public Color getGroundColor() {
        return this.groundColor;
    }

    /**
     * Sets the palette's ground color.
     * @param groundColor the ground color
     */
    public void setGroundColor(Color groundColor) {
        this.groundColor = groundColor;
    }

    /**
     * Gets the palette's mountain color.
     * @return the mountain color
     */
    public Color getMountainColor() {
        return this.mountainColor;
    }

    /**
     * Sets the palette's mountain color.
     * @param mountainColor the mountain color
     */
    public void setMountainColor(Color mountainColor) {
        this.mountainColor = mountainColor;
    }

    @Override
    public String toString() {
        return name;
    }

}