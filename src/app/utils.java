package app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class utils {

    static BufferedImage readImage(File imageFile) throws IOException {
        return ImageIO.read(imageFile);
    }

    static Color hex2rgb(String hex) throws IOException {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() != 3 && hex.length() != 6) throw new IOException("Invalid hex value");

        int l = hex.length() / 3;
        return new Color(
                getValue(hex.substring(0, l)),
                getValue(hex.substring(l, l * 2)),
                getValue(hex.substring(l * 2, l * 3)));
    }

    static int getValue(String p) {
        return Integer.valueOf(p.length() == 1 ? p + p : p, 16);
    }

    static boolean writeToDisk(SquareIt squareIt, BufferedImage sqBI) throws IOException {
        final String
                path = squareIt.getImageFile().getAbsolutePath(),
                sqPath = path.substring(0, path.lastIndexOf('.')) + "_sq_" + squareIt.getPadding(),
                sqType = path.substring(path.lastIndexOf('.') + 1);

        final File sqFile = validateFileName(sqPath, sqType);

        return ImageIO.write(sqBI, sqType, sqFile);
    }

    static File validateFileName(String path, String type) {
        int i = 0;
        File file = new File(path + '.' + type);

        while (file.exists()) file = new File(path + (++i) + '.' + type);

        return file;
    }

}