import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.util.IOUtils.copy;

public class AuthenticationExample {

  public static void main(String[] args) {
    try {

      String url = "<AmazonS3URI>";
      final AmazonS3URI amazonS3URI = new AmazonS3URI(url);
      final String bucketName = amazonS3URI.getBucket();
      final String key = amazonS3URI.getKey();

      AWSCredentials awsCredentials = new BasicAWSCredentials("access_key","secret_key");
      Regions clientRegion = Regions.US_EAST_1;
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion)
          .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
          .build();

      S3Object fullObject;

      System.out.println("Downloading an object");

      fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      copy(fullObject.getObjectContent(), baos);
      byte[] bytes = baos.toByteArray();

      System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
      String validityResponse = validateTokenMetaDataCSV( new ByteArrayInputStream(bytes));
      if(validityResponse.contains("error")){
        System.out.println(validityResponse);
      }


      String res = createSeatsArray(new ByteArrayInputStream(bytes));
      System.out.println(res);

    } catch (AmazonServiceException | IOException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
    } catch (SdkClientException | IllegalArgumentException e) {
      e.printStackTrace();
      System.out.println(("{\"error\": \""+e.getMessage()+"\"}"));
    }

  }

  private static String createSeatsArray(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String line = null;
    reader.readLine();
    List<Seat> seats = new ArrayList<>();
    while ((line = reader.readLine()) != null) {
      String[] data = line.split(",");
      Seat seat = new Seat(data[0],data[1],Integer.parseInt(data[2]));
      seats.add(seat);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int count = 0;
    for (Seat seat : seats) {
      if (count > 0) sb.append(",");
      sb.append(seat.toJSON());
      count++;
    }
    sb.append("]");
//    LOGGER.info("array"+sb.toString());
    return sb.toString();
  }

  private static String validateTokenMetaDataCSV(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String line = null;
    int colNumber =  (reader.readLine().split(",").length);  //get the number of columns from the header
    int count = 0;
    while ((line = reader.readLine()) != null) {
      count ++;
      String[] data = line.split(",");
      if(data.length<colNumber){
        return String.format("{\"error\":\"Empty value @ row %d, column %d \"}", count, colNumber);
      }

      for (int i = 0; i < colNumber; i++) {
        if (data[i].isEmpty()) {
          return String.format("{\"error\":\"Empty value @ row %d, column %d\"}", count, i+1);
        }
      }

      try{
        Integer.parseInt(data[2]);
      }catch (NumberFormatException e){
        return String.format("{\"error\":\"invalid number format @ row %d, col %d \"}",count,3);
      }
    }
    if(count != 11){
      return String.format("{\"error\":\"seats(%d) does not equal to supplyLimit(%d)\"}",count,11);
    }
    return "valid";
  }



  private static class Seat {
    private String c;
    private String r;
    private int s;

    public Seat(String c, String r, int s) {
      this.c = c;
      this.r = r;
      this.s = s;
    }

    public String getC() {
      return c;
    }

    public void setC(String c) {
      this.c = c;
    }

    public String getR() {
      return r;
    }

    public void setR(String r) {
      this.r = r;
    }

    public int getS() {
      return s;
    }

    public void setS(int s) {
      this.s = s;
    }


    public String toJSON() {
      StringBuilder sb = new StringBuilder();
      sb.append("{")
          .append("\"c\": \"").append(c).append("\"")
          .append(",\"r\": \"").append(r).append("\"")
          .append(",\"s\": \"").append(s).append("\"");
      sb.append("}");
      return sb.toString();
    }
  }
}
