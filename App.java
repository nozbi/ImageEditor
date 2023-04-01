import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;

public class App 
{
    private JLabel sourceImageLabel;
    private JLabel resultImageLabel;
    private JMenuItem loadMenuItem;
    private JSpinner brightnessValueSpinner;
    private JSpinner contrastValueSpinner;

    public static void main(String[] args) throws Exception 
    {
        new App();
    }

    private App()
    {
        //FRAME
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("App");
        frame.setVisible(true);
        frame.setLayout(new GridLayout(1, 2, 0, 0));

        //SOURCE PANEL
        JPanel sourceImageBorderPanel = new JPanel();
        frame.add(sourceImageBorderPanel);
        sourceImageBorderPanel.setLayout(new GridLayout(1, 1, 0, 0));

        JPanel sourceImageSlotPanel = new JPanel();
        sourceImageBorderPanel.add(sourceImageSlotPanel);
        sourceImageSlotPanel.setLayout(new GridBagLayout());

        JLabel sourceImageLabel = new JLabel();
        sourceImageSlotPanel.add(sourceImageLabel);

        //RESULT PANEL
        JPanel resultImageBorderPanel = new JPanel();
        frame.add(resultImageBorderPanel);
        resultImageBorderPanel.setLayout(new GridLayout(1, 1, 0, 0));

        JPanel resultImageSlotPanel = new JPanel();
        resultImageBorderPanel.add(resultImageSlotPanel);
        resultImageSlotPanel.setLayout(new GridBagLayout());

        JLabel resultImageLabel = new JLabel();
        resultImageSlotPanel.add(resultImageLabel);
    
        //MENU BAR
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        //IMAGE MENU
        JMenu imageMenu = new JMenu("Image");
        menuBar.add(imageMenu);

        JMenuItem loadMenuItem = new JMenuItem("Load");
        imageMenu.add(loadMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        imageMenu.add(saveMenuItem);

        JMenuItem clearMenuItem = new JMenuItem("Clear");
        imageMenu.add(clearMenuItem);

        JMenuItem switchMenuItem = new JMenuItem("Switch");
        imageMenu.add(switchMenuItem);

        JMenuItem resetMenuItem = new JMenuItem("Reset");
        imageMenu.add(resetMenuItem);

        //GRAYSCALE MENU
        JMenu grayscaleMenu = new JMenu("Grayscale");
        menuBar.add(grayscaleMenu);

        JMenuItem redMenuItem = new JMenuItem("Red");
        grayscaleMenu.add(redMenuItem);

        JMenuItem greenMenuItem = new JMenuItem("Green");
        grayscaleMenu.add(greenMenuItem);

        JMenuItem blueMenuItem = new JMenuItem("Blue");
        grayscaleMenu.add(blueMenuItem);

        JMenuItem rgbMenuItem = new JMenuItem("RGB");
        grayscaleMenu.add(rgbMenuItem);

        JMenuItem yuvMenuItem = new JMenuItem("YUV");
        grayscaleMenu.add(yuvMenuItem);

        //TRANSFORM MENU
        JMenu transformMenu = new JMenu("Transform");
        menuBar.add(transformMenu);

        JMenuItem brightnessRangeMenuItem = new JMenuItem("Brightness range");
        transformMenu.add(brightnessRangeMenuItem);

        JMenuItem negativeMenuItem = new JMenuItem("Negative");
        transformMenu.add(negativeMenuItem);

        //TRANSFORM MENU->BRIGHTNESSMENU
        JMenu brightnessMenu = new JMenu("Brightness:");
        transformMenu.add(brightnessMenu);

        JSpinner brightnessValueSpinner = new JSpinner(new SpinnerNumberModel(0, -255, 255, 1));
        brightnessMenu.add(brightnessValueSpinner);
        
        //TRANSFORM MENU->CONTRASTMENU
        JMenu contrastMenu = new JMenu("Contrast:");
        transformMenu.add(contrastMenu);

        JSpinner contrastValueSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 255, 0.001));
        contrastMenu.add(contrastValueSpinner);

        //SETTING GLOBAL VARIABLES
        this.sourceImageLabel = sourceImageLabel;
        this.resultImageLabel = resultImageLabel;
        this.loadMenuItem = loadMenuItem;
        this.brightnessValueSpinner = brightnessValueSpinner;
        this.contrastValueSpinner = contrastValueSpinner;

        //ADDING ACTION LISTENERS
        loadMenuItem.addActionListener(actionEvent ->{this.onLoadMenuItemActionPerformed(actionEvent);});
        saveMenuItem.addActionListener(actionEvent ->{this.onSaveMenuItemActionPerformed(actionEvent);});
        clearMenuItem.addActionListener(actionEvent ->{this.onClearMenuItemActionPerformed(actionEvent);});
        switchMenuItem.addActionListener(actionEvent ->{this.onSwitchMenuItemActionPerformed(actionEvent);});
        resetMenuItem.addActionListener(actionEvent ->{this.onResetMenuItemActionPerformed(actionEvent);});
        redMenuItem.addActionListener(actionEvent ->{this.onRedMenuItemActionPerformed(actionEvent);});
        greenMenuItem.addActionListener(actionEvent ->{this.onGreenMenuItemActionPerformed(actionEvent);});
        blueMenuItem.addActionListener(actionEvent ->{this.onBlueMenuItemActionPerformed(actionEvent);});
        rgbMenuItem.addActionListener(actionEvent ->{this.onRgbMenuItemActionPerformed(actionEvent);});
        yuvMenuItem.addActionListener(actionEvent ->{this.onYuvMenuItemActionPerformed(actionEvent);});
        brightnessRangeMenuItem.addActionListener(actionEvent ->{this.onBrightnessRangeMenuItemActionPerformed(actionEvent);});
        negativeMenuItem.addActionListener(actionEvent ->{this.onNegativeMenuItemActionPerformed(actionEvent);});
        brightnessValueSpinner.addChangeListener(changeEvent -> {this.onBrightnessValueSpinnerChanged(changeEvent);});
        contrastValueSpinner.addChangeListener(changeEvent -> {this.onContrastValueSpinnerChanged(changeEvent);});

        //DISABLING MENU AT START
        clearMenuItem.doClick();

        //SETTING COLORS
        sourceImageSlotPanel.setBackground(Color.LIGHT_GRAY);
        sourceImageBorderPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(frame.getBackground(), 10), BorderFactory.createLineBorder(Color.WHITE, 5)));

        resultImageSlotPanel.setBackground(Color.LIGHT_GRAY);
        resultImageBorderPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(frame.getBackground(), 10), BorderFactory.createLineBorder(Color.WHITE, 5)));
    }

    //ACTION LISTENERS
    private void onLoadMenuItemActionPerformed(ActionEvent actionEventParameter) 
    {
        try
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image file(.png, .bmp, .jpg)", "png", "bmp", "jpg");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                this.resetBothSpinners();
                this.setSourceImage(image);
                this.setMenuEnabled(true);
            }
        }
        catch(Exception exception){}
    }

    private void onSaveMenuItemActionPerformed(ActionEvent actionEventParameter) 
    {
        try
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image file(.png)", "png");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
            {
                File file = fileChooser.getSelectedFile();
                file = new File(file.toString() + ".png");
                BufferedImage image = this.getResultImage();
                ImageIO.write(image, "png", file);
            }
        }
        catch(Exception exception){}
    }

    private void onClearMenuItemActionPerformed(ActionEvent actionEventParameter) 
    {
        this.resetBothSpinners();

        this.setSourceImage(null);
        this.setMenuEnabled(false);
    }

    private void onSwitchMenuItemActionPerformed(ActionEvent actionEventParameter) 
    {
        this.setSourceImage(this.getResultImage());

        this.resetBothSpinners();
    }

    private void onResetMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(this.getSourceImage());
    }

    private void onRedMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toGrayScale(this.getSourceImage(), ImageEditor.GrayscaleType.R));
    }

    private void onGreenMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toGrayScale(this.getSourceImage(), ImageEditor.GrayscaleType.G));
    }

    private void onBlueMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toGrayScale(this.getSourceImage(), ImageEditor.GrayscaleType.B));
    }

    private void onRgbMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toGrayScale(this.getSourceImage(), ImageEditor.GrayscaleType.RGB));
    }

    private void onYuvMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toGrayScale(this.getSourceImage(), ImageEditor.GrayscaleType.YUV));
    }

    private void onBrightnessRangeMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.changeBrightnessRange(this.getSourceImage()));
    }

    private void onNegativeMenuItemActionPerformed(ActionEvent actionEventParameter)
    {
        this.resetBothSpinners();

        this.setResultImage(ImageEditor.toNegative(this.getSourceImage()));
    }

    private void onBrightnessValueSpinnerChanged(ChangeEvent changeEventParameter)
    {
        this.resetContrastValueSpinner();

        JSpinner spinner = (JSpinner)changeEventParameter.getSource();
        int value = (int)spinner.getValue();
        this.setResultImage(ImageEditor.changeBrightness(this.getSourceImage(), value));
    }

    private void onContrastValueSpinnerChanged(ChangeEvent changeEventParameter)
    {
        this.resetBrightnessValueSpinner();

        JSpinner spinner = (JSpinner)changeEventParameter.getSource();
        double value = (double)spinner.getValue();
        this.setResultImage(ImageEditor.changeContrast(this.getSourceImage(), value));
    }



    //OTHER
    private void setMenuEnabled(boolean enabledParamerter)
    {
        //DISABLING ALL MENU BAR MENUS
        JFrame frame = (JFrame)JFrame.getFrames()[0];
        JMenuBar menuBar = frame.getJMenuBar();
        Component[] menus = menuBar.getComponents();
        for(int i = 0; i < menus.length; i++)
        {
            menus[i].setEnabled(enabledParamerter);
        }

        //DISABLING ALL EXCEPT LOAD MENU ITEM
        JPopupMenu popupMenu = (JPopupMenu)this.loadMenuItem.getParent();
        JMenu menu = (JMenu)popupMenu.getInvoker();
        menu.setEnabled(true);
        Component[] menuItems = ((JMenu)menu).getMenuComponents();
        for(int j = 1; j < menuItems.length; j++)
        {
            menuItems[j].setEnabled(enabledParamerter);

        }
        this.loadMenuItem.setEnabled(true);
    }

    private void setSourceImage(BufferedImage imageParameter)
    {
        JLabel label;
        if(imageParameter == null)
        {
            label = new JLabel();
        }
        else
        {
            label = new JLabel(new ImageIcon(imageParameter));
        }
        Container container = this.sourceImageLabel.getParent();
        container.removeAll();
        container.add(label);
        container.revalidate();
        container.repaint();
        this.sourceImageLabel = label;

        setResultImage(imageParameter);
    }

    private void setResultImage(BufferedImage imageParameter)
    {
        JLabel label;
        if(imageParameter == null)
        {
            label = new JLabel();
        }
        else
        {
            label = new JLabel(new ImageIcon(imageParameter));
        }
        Container container = this.resultImageLabel.getParent();
        container.removeAll();
        container.add(label);
        container.revalidate();
        container.repaint();
        this.resultImageLabel = label;
    }

    private BufferedImage getSourceImage()
    {
        ImageIcon imageIcon = (ImageIcon)this.sourceImageLabel.getIcon();
        BufferedImage bufferedImage = (BufferedImage)imageIcon.getImage();
        return bufferedImage;
    }

    private BufferedImage getResultImage()
    {
        ImageIcon imageIcon = (ImageIcon)this.resultImageLabel.getIcon();
        BufferedImage bufferedImage = (BufferedImage)imageIcon.getImage();
        return bufferedImage;
    }

    private void resetBrightnessValueSpinner()
    {
       this.brightnessValueSpinner.setValue(0);
    }

    private void resetContrastValueSpinner()
    {
        this.contrastValueSpinner.setValue(1.0);
    }

    private void resetBothSpinners()
    {
        this.resetBrightnessValueSpinner();
        this.resetContrastValueSpinner();
    }
}
