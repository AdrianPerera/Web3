import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class VideoImage {
    private static String outputImagePath1 = "propic2.jpg";
    public static void main(String[] args) throws IOException {
        String vidUrl = "https://ipfs.io/ipfs/QmbRh8CU99J8z5cvQ9fRjDegPVMT4zi7FbBcx2PagFga4f";
        BufferedImage bufferedImage = videoFramer(vidUrl);
        ImageIO.write(bufferedImage, "jpg", new File(outputImagePath1));
    }

    public static BufferedImage videoFramer(String ipfsUrl) {
        try {
            Frame frame = null;
            int flag = 0;
            URL url = new URL(ipfsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            while (connection.getResponseCode()!=200){
                connection =(HttpURLConnection) url.openConnection();
            }
            InputStream is = connection.getInputStream();

            final FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(is);

            System.out.println(" Start capturing video :");
            fFmpegFrameGrabber.start();

            final int ftp = fFmpegFrameGrabber.getLengthInFrames();
            System.out.println(" Duration " + fFmpegFrameGrabber.getLengthInTime()/1e6 + " s." );
            BufferedImage bufferedImage = null;
            while (flag <= ftp) {
                frame = fFmpegFrameGrabber.grabImage();
                if (frame != null && flag == 1) {
                     bufferedImage = FrameToBufferedImage(frame);
                    break;
                }
                flag++;
            }
            fFmpegFrameGrabber.stop();
            fFmpegFrameGrabber.close();
            is.close();
            return bufferedImage;
        } catch (Exception E) {
            E.printStackTrace();
            return null;
        }
    }


    public static BufferedImage FrameToBufferedImage(Frame frame) {
        // establish BufferedImage object
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static void save(String imageUrl, String destinationFile) throws IOException {
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
