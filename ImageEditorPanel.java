import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ImageEditorPanel extends JPanel implements KeyListener {
    Color[][] pixels;

    public ImageEditorPanel() {
        BufferedImage imageIn = null;
        try {
            imageIn = ImageIO.read(new File("aidanking.jpg"));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
    }

    public void run() {
        // pixels = grayscalePhotographicGray(pixels);
        // pixels = grayscale(pixels);
        // pixels = blur(pixels);
        // pixels = blur(pixels);
        // pixels = rainbowFilter(pixels);
        // pixels = contrastMethod(pixels);
        // pixels = vintageImage(pixels);
        // pixels = blurredGrayScale(pixels);
        repaint();
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }

    public static Color[][] flipVertical(Color[][] origJPG) {
        Color[][] flipVert = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                flipVert[i][j] = origJPG[origJPG.length - 1 - i][j];
            }
        }
        return flipVert;
    }

    public static Color[][] flipHorizontal(Color[][] origJPG) {
        Color[][] flipHori = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                flipHori[i][j] = origJPG[i][origJPG[0].length - 1 - j];
            }
        }
        return flipHori;
    }

    public static Color[][] grayscale(Color[][] origJPG) {
        Color[][] newGrayScale = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int newGrayValue = (origJPG[i][j].getRed() + origJPG[i][j].getGreen() + origJPG[i][j].getBlue()) / 3;
                Color newColor = new Color(newGrayValue, newGrayValue, newGrayValue);
                newGrayScale[i][j] = newColor;
            }
        }
        return newGrayScale;
    }

    public static Color[][] grayscalePhotographicGray(Color[][] origJPG) {
        final double RED_WEIGHT = 0.299; // wikipedia values used here
        final double GREEN_WEIGHT = 0.587;
        final double BLUE_WEIGHT = 0.114;
        Color[][] newGrayScale = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int newGrayValue = (int) (RED_WEIGHT * origJPG[i][j].getRed() + GREEN_WEIGHT * origJPG[i][j].getGreen()
                        + BLUE_WEIGHT * origJPG[i][j].getBlue());
                newGrayScale[i][j] = new Color(newGrayValue, newGrayValue, newGrayValue);
            }
        }
        return newGrayScale;
    }

    public static Color[][] blur(Color[][] origJPG) {
        final int radius = 10;
        Color[][] newBlur = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;
                int count = 0;
                for (int x = i - radius; x <= i + radius; x++) {
                    for (int y = j - radius; y <= j + radius; y++) {
                        if (x >= 0 && x < origJPG.length && y >= 0 && y < origJPG[0].length) {
                            totalRed += origJPG[x][y].getRed();
                            totalGreen += origJPG[x][y].getGreen();
                            totalBlue += origJPG[x][y].getBlue();
                            count++;
                        }
                    }
                }
                int redAvg = totalRed / count;
                int greenAvg = totalGreen / count;
                int blueAvg = totalBlue / count;
                newBlur[i][j] = new Color(redAvg, greenAvg, blueAvg);
            }
        }
        return newBlur;
    }

    public static Color[][] rainbowFilter(Color[][] origJPG) { // will fix this horrendous method after some feedback
                                                               // most likely
        Color[][] newRainbowFilter = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int red = (int) (origJPG[i][j].getRed() * Math.random());
                int green = (int) (origJPG[i][j].getGreen() * Math.random());
                int blue = (int) (origJPG[i][j].getBlue() * Math.random());
                newRainbowFilter[i][j] = new Color(red, green, blue);
            }
        }
        return newRainbowFilter;
    }

    public static Color[][] contrastMethod(Color[][] origJPG) {
        final int PRE_INTENSITY_VALUE = 128;
        final int POST_INTENSITY_VALUE = 128;
        Color[][] newContrastJPG = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int red = origJPG[i][j].getRed();
                int green = origJPG[i][j].getGreen();
                int blue = origJPG[i][j].getBlue();
                double contrastAdjustmentValue = 0.75;
                red = rangeChecker(
                        (int) (contrastAdjustmentValue * (red - PRE_INTENSITY_VALUE) + POST_INTENSITY_VALUE));
                green = rangeChecker(
                        (int) (contrastAdjustmentValue * (green - PRE_INTENSITY_VALUE) + POST_INTENSITY_VALUE));
                blue = rangeChecker(
                        (int) (contrastAdjustmentValue * (blue - PRE_INTENSITY_VALUE) + POST_INTENSITY_VALUE));
                newContrastJPG[i][j] = new Color(red, green, blue);
            }
        }
        return newContrastJPG;
    }

    public static int rangeChecker(int rgbValue) {
        int validRangeCheck = Math.min(255, Math.max(0, rgbValue));
        return validRangeCheck;
    }

    public static Color[][] vintageImage(Color[][] origJPG) {
        Color[][] newVintageImage = new Color[origJPG.length][origJPG[0].length];
        for (int i = 0; i < origJPG.length; i++) {
            for (int j = 0; j < origJPG[0].length; j++) {
                int red = rangeChecker((int) (origJPG[i][j].getRed() * 0.393 + origJPG[i][j].getGreen() * 0.769
                        + origJPG[i][j].getBlue() * 0.189)); // values from stack overflow for the weights for the sepia
                                                             // tone
                int green = rangeChecker((int) (origJPG[i][j].getRed() * 0.349 + origJPG[i][j].getGreen() * 0.686
                        + origJPG[i][j].getBlue() * 0.168));
                int blue = rangeChecker((int) (origJPG[i][j].getRed() * 0.272 + origJPG[i][j].getGreen() * 0.534
                        + origJPG[i][j].getBlue() * 0.131));
                newVintageImage[i][j] = new Color(red, green, blue);
            }
        }
        return newVintageImage;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '0') {
            pixels = flipHorizontal(pixels);
        }
        if (e.getKeyChar() == '2') {
            pixels = grayscale(pixels);
        }
        if (e.getKeyChar() == '3') {
            pixels = blur(pixels);

        }
        if (e.getKeyChar() == '4') {
            pixels = vintageImage(pixels);

        }
        if (e.getKeyChar() == '5') {
            pixels = contrastMethod(pixels);

        }
        if (e.getKeyChar() == '1') {
            pixels = flipVertical(pixels);

        }
        if (e.getKeyChar() == '6') {
            pixels = rainbowFilter(pixels);

        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
