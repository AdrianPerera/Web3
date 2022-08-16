import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageShrink {
    public static void main(String[] args) {
        String imageUrl = "https://ipfs.io/ipfs/QmTxjZx731PBUgiLBzb1VTGinDzpX3YTvZPXczDWGy4o3r";
        String outputImagePath = "transformation.jpg";
        String outputImagePath2 = "propic2.jpg";
        String outputImagePath3 = "propic3.jpg";

        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();

            double percent = 0.5;
//            InputStream is = new BufferedInputStream(new FileInputStream("src/main/resources/sample1.jfif"));

            BufferedImage inputImage = ImageIO.read(is);
            is.close();
            int scaledWidth = (int) (inputImage.getWidth() * percent);
            int scaledHeight = (int) (inputImage.getHeight() * percent);


//            final int imageType = inputImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB : inputImage.getType();

            System.out.println(inputImage.getType());
            BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
            System.out.println(formatName);

            ImageIO.write(outputImage, formatName, new File(outputImagePath));

        } catch (IOException ex) {
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }
    }

    public static void resize(String inputImagePath,
        String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

    public static void resize(String inputImagePath,
        String outputImagePath, int scaledWidth, int scaledHeight)
        throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
            scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
            .lastIndexOf(".") + 1);
        System.out.println(formatName);
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

}
