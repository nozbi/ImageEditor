import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


public class ImageEditor 
{
    public enum GrayscaleType
    {
        R,
        G,
        B,
        RGB,
        YUV
    }

    private ImageEditor(){};

    public static BufferedImage toGrayScale(BufferedImage bufferedImageParameter, GrayscaleType grayscaleTypeParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int oldRGB = bufferedImage.getRGB(x, y);
                Color oldColor = new Color(oldRGB);
                int grayValue = 0;
                switch(grayscaleTypeParameter)
                {
                    case R: 
                        grayValue = oldColor.getRed();
                        break;
                    case G: 
                        grayValue = oldColor.getGreen();
                        break;
                    case B:
                        grayValue = oldColor.getBlue();
                        break;
                    case RGB:
                        grayValue = (oldColor.getRed() + oldColor.getGreen() + oldColor.getBlue()) / 3;
                        break;
                    case YUV:
                        grayValue = (int)((0.299 * oldColor.getRed()) + (0.587 * oldColor.getGreen()) + (0.114 * oldColor.getBlue()));
                        break;
                }
                Color newColor = new Color(grayValue, grayValue, grayValue);
                int rgb = newColor.getRGB();
                bufferedImage.setRGB(x, y, rgb);   
            }
        }
        return bufferedImage;
    }

    public static BufferedImage toNegative(BufferedImage bufferedImageParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        Color maxColor = ImageEditor.getMaxColor(bufferedImage);
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int oldRGB = bufferedImage.getRGB(x, y);
                Color oldColor = new Color(oldRGB);
                int r = maxColor.getRed() - oldColor.getRed();
                int g = maxColor.getGreen() - oldColor.getGreen();
                int b = maxColor.getBlue() - oldColor.getBlue();
                Color newColor = new Color(r, g, b);
                int rgb = newColor.getRGB();
                bufferedImage.setRGB(x, y, rgb);   
            }
        }
        return bufferedImage;
    }

    public static BufferedImage changeBrightnessRange(BufferedImage bufferedImageParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        Color minColor = ImageEditor.getMinColor(bufferedImage);
        Color maxColor = ImageEditor.getMaxColor(bufferedImage);
        if((maxColor.getRed() - minColor.getRed() == 0) || (maxColor.getGreen() - minColor.getGreen() == 0) || (maxColor.getBlue() - minColor.getBlue() == 0))
        {
            return bufferedImage;
        }
        else
        {
            for(int y = 0; y < sizeY; y++)
            {
                for(int x = 0; x < sizeX; x++)
                {
                    int oldRGB = bufferedImage.getRGB(x, y);
                    Color oldColor = new Color(oldRGB);
                    int r = (255 * (oldColor.getRed() - minColor.getRed())) / (maxColor.getRed() - minColor.getRed());
                    int g = (255 * (oldColor.getGreen() - minColor.getGreen())) / (maxColor.getGreen() - minColor.getGreen());
                    int b = (255 * (oldColor.getBlue() - minColor.getBlue())) / (maxColor.getBlue() - minColor.getBlue());
                    Color newColor = new Color(ImageEditor.clampValue(r), ImageEditor.clampValue(g), ImageEditor.clampValue(b));
                    int rgb = newColor.getRGB();
                    bufferedImage.setRGB(x, y, rgb);   
                }
            }
            return bufferedImage;
        }
    }

    public static BufferedImage changeBrightness(BufferedImage bufferedImageParameter, int brightnessValueParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int oldRGB = bufferedImage.getRGB(x, y);
                Color oldColor = new Color(oldRGB);
                int r = oldColor.getRed() + brightnessValueParameter;
                int g = oldColor.getGreen() + brightnessValueParameter;
                int b = oldColor.getBlue() + brightnessValueParameter;
                Color newColor = new Color(ImageEditor.clampValue(r), ImageEditor.clampValue(g), ImageEditor.clampValue(b));
                int rgb = newColor.getRGB();
                bufferedImage.setRGB(x, y, rgb);   
            }
        }
        return bufferedImage;
    }

    public static BufferedImage changeContrast(BufferedImage bufferedImageParameter, double contrastValueParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int oldRGB = bufferedImage.getRGB(x, y);
                Color oldColor = new Color(oldRGB);
                int r = (int)(oldColor.getRed() * contrastValueParameter);
                int g = (int)(oldColor.getGreen() * contrastValueParameter);
                int b = (int)(oldColor.getBlue() * contrastValueParameter);
                Color newColor = new Color(ImageEditor.clampValue(r), ImageEditor.clampValue(g), ImageEditor.clampValue(b));
                int rgb = newColor.getRGB();
                bufferedImage.setRGB(x, y, rgb);   
            }
        }
        return bufferedImage;
    }


    private static BufferedImage duplicateBufferedImage(BufferedImage bufferedImageParameter) 
    {
        ColorModel colorModel = bufferedImageParameter.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImageParameter.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private static Color getMinColor(BufferedImage bufferedImageParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        int minR = 255;
        int minG = 255;
        int minB = 255;
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int rgb = bufferedImage.getRGB(x, y);
                Color Color = new Color(rgb);
                if(Color.getRed() < minR)
                {
                    minR = Color.getRed();
                }
                if(Color.getGreen() < minG)
                {
                    minG = Color.getGreen();
                }
                if(Color.getBlue() < minB)
                {
                    minB = Color.getBlue();
                }
            }
        }
        return new Color(minR, minG, minB);
    }

    private static Color getMaxColor(BufferedImage bufferedImageParameter)
    {
        BufferedImage bufferedImage = ImageEditor.duplicateBufferedImage(bufferedImageParameter);
        int sizeX = bufferedImage.getWidth();
        int sizeY = bufferedImage.getHeight();
        int maxR = 0;
        int maxG = 0;
        int maxB = 0;
        for(int y = 0; y < sizeY; y++)
        {
            for(int x = 0; x < sizeX; x++)
            {
                int rgb = bufferedImage.getRGB(x, y);
                Color Color = new Color(rgb);
                if(Color.getRed() > maxR)
                {
                    maxR = Color.getRed();
                }
                if(Color.getGreen() > maxG)
                {
                    maxG = Color.getGreen();
                }
                if(Color.getBlue() > maxB)
                {
                    maxB = Color.getBlue();
                }
            }
        }
        return new Color(maxR, maxG, maxB);
    }

    private static int clampValue(int valueParameter)
    {
        return Math.max(0, Math.min(255, valueParameter));
    }
}
