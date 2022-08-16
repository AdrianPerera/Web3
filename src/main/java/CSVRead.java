import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CSVRead {

  public static void main(String[] args) {

    Path pathToFile = Paths.get("src/main/resources/sample.csv");
    try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) { // read the first line from the text file

      String line = null;
      int colNumber =  (br.readLine().split(",").length);
      int count = 0;
      while ((line = br.readLine()) != null) {
        count ++;
        String[] data = line.split(",");
        if(data.length<colNumber){
          System.out.printf("{\"error\":\"row %d, column %d is empty\"}", count, colNumber);
          break;
        }

        for (int i = 0; i < colNumber; i++) {
          if (data[i].isEmpty()) {
            System.out.printf("{\"error\":\"empty value : row %d, col %d \"}", count, i+1);
          }
          if(i!=2){
            boolean match = Pattern.compile("\"\\w+\"").matcher(data[i]).matches();
            if (!match){
              System.out.printf("{\"error\":\"invalid double quotes placing : row %d ,col %d \"}", count, i+1);
              break;
            }
          }
        }

        try{
          Integer.parseInt(data[2]);
        }catch (NumberFormatException e){
          System.out.printf("{\"error\":\"invalid number format : row %d, col %d \"}",count,3);
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

  private static byte[] validateCSV(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String line = null;
    reader.readLine();
    while ((line = reader.readLine()) != null) {
      String[] data = line.split(",");
      if(data.length< 3){
        return ("{\"error\":\"ERROR \"}").getBytes();
      }
    }
    StringBuilder sb = new StringBuilder();
    return ("{\"error\":\"ERROR \"}").getBytes();
  }

  private static class Seat {
    private String c;
    private String r;
    private int s;

    public String getC() { return c; }
    public void setC(String c) { this.c = c; }
    public String getR() { return r; }
    public void setR(String r) { this.r = r; }

    public int getS() {
      return s;
    }

    public void setS(int s) {
      this.s = s;
    }


    public String toJSON() {
      StringBuilder sb = new StringBuilder();
      sb.append("{")
          .append("\"c\": ").append(c)
          .append(",\"r\":").append(r)
          .append(",\"s\": ").append(s);
      sb.append("}");
      return sb.toString();
    }
  }

}
