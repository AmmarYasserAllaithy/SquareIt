package app;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SquareIt {

    private File mImageFile = null;
    private int mPadding = Integer.MIN_VALUE;
    private Color mColor = null;
    private Dimension mRatio = null;

    // region > Constructors
    public SquareIt() {
        this(new Dimension(16, 9));
    }

    public SquareIt(Dimension ratio) {
        mRatio = ratio;
    }

    public SquareIt(File imageFile) {
        mImageFile = imageFile;
    }

    public SquareIt(File imageFile, int padding) {
        this(imageFile);
        mPadding = padding;
    }

    public SquareIt(File imageFile, int padding, Dimension ratio) {
        this(imageFile, padding);
        mRatio = ratio;
    }

    public SquareIt(File imageFile, int padding, Color color, Dimension ratio) {
        this(imageFile, padding, ratio);
        mColor = color;
    }

    public SquareIt(File imageFile, int padding, String hex, Dimension ratio) throws IOException {
        this(imageFile, padding, ratio);
        mColor = utils.hex2rgb(hex);
    }
    // endregion

    // region > Setters
    public SquareIt setImageFile(File imageFile) {
        mImageFile = imageFile;
        return this;
    }

    public SquareIt setPadding(int padding) {
        mPadding = padding;
        return this;
    }

    public SquareIt setColor(Color color) {
        mColor = color;
        return this;
    }

    public SquareIt setColor(String hex) throws IOException {
        setColor(utils.hex2rgb(hex));
        return this;
    }

    public SquareIt setRatio(Dimension ratio) {
        mRatio = ratio;
        return this;
    }
    // endregion

    // region > Getter
    public File getImageFile() {
        return mImageFile;
    }

    public int getPadding() {
        return mPadding;
    }

    public Color getColor() {
        return mColor;
    }

    public Dimension getRatio() {
        return mRatio;
    }
    // endregion

    // region > Functionality
    public boolean start() throws IOException {
        validateArgs();

        // Read original image from disk
        final BufferedImage bi = utils.readImage(mImageFile);

        // Calculate the squared width and height
        final Dimension squareDimensions = calcSquareDimensions(bi.getWidth(), bi.getHeight());

        // Square image
        final BufferedImage sqBI = square(bi, squareDimensions);

        // Write the squared image to disk
        return utils.writeToDisk(this, sqBI);
    }

    private void validateArgs() throws IOException {
        // Validate image file
        if (mImageFile == null) throw new NullPointerException("Image MUST set");
        else if (!mImageFile.exists()) throw new IOException("Image not found.");

        // Validate padding
        if (mPadding == Integer.MIN_VALUE) throw new NullPointerException("Padding MUST set");

        // Validate color
        if (mColor == null) throw new NullPointerException("Color MUST set");

        // Validate ratio
        if (mRatio == null) throw new NullPointerException("Ratio MUST set");
    }

    private Dimension calcSquareDimensions(int width, int height) {
        Dimension dimension;

        if (width > height) dimension = calcAccordingToWidth(width);
        else dimension = calcAccordingToHeight(height);

        final boolean validWidth = dimension.width >= (width + 2 * mPadding);
        final boolean validHeight = dimension.height >= (height + 2 * mPadding);

        // FIXME to adjust square [1 : 1] to portrait [2 : 1] images
        if (!validWidth) return calcAccordingToWidth(width);
        else if (!validHeight) return calcAccordingToHeight(height);

        return dimension;
    }

    private Dimension calcAccordingToWidth(int width) {
        int sqW = width + 2 * mPadding;
        int sqH = sqW * mRatio.height / mRatio.width;
        return new Dimension(sqW, sqH);
    }

    private Dimension calcAccordingToHeight(int height) {
        int sqH = height + 2 * mPadding;
        int sqW = sqH * mRatio.width / mRatio.height;
        return new Dimension(sqW, sqH);
    }

    private BufferedImage square(BufferedImage bi, Dimension squareDimension) {
        int sqW = squareDimension.width;
        int sqH = squareDimension.height;
        BufferedImage sqBI = new BufferedImage(sqW, sqH, bi.getType() == 0 ? 1 : bi.getType());
        Graphics2D g2d = sqBI.createGraphics();
        g2d.setColor(mColor);
        g2d.fillRect(0, 0, sqW, sqH);
        g2d.drawImage(bi, (sqW - bi.getWidth()) / 2, (sqH - bi.getHeight()) / 2, null);
        g2d.dispose();
        return sqBI;
    }
    // endregion
}