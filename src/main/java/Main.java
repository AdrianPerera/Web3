import com.amazonaws.services.dynamodbv2.xspec.L;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.slf4j.Logger;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String args[]) throws InterruptedException, IOException {
        //
        //        String[] tokenIds = {"186" , "393" , "185"};
        //        String contractAddress = "0x641D1A2C1E23ce6083512D6639726553c125F37e";
        //        String[] tokenIds = {"181"};
        //        String contractAddress = "0x192e0386735b7743985aF29c0C0dA86425a7757f";
        //        String[] tokenIds = {"32", "11"};
        //        String contractAddress = "0x3cbac7ae60f4aeadf2ad4e19de7e7201ffcdee56";
        //        String[] tokenIds = {"21"};
        //        String contractAddress = "0xae2e0d367db3c58930a17350bd664661b4ce18d7";

        //        for (int tokenId = 33; tokenId < 35; tokenId++) {
        //
        //            String b = getTokenUri(String.valueOf(tokenId), contractAddress);
        //            //                    MetaData metaData = mapper.readValue(b,MetaData.class);
        //            //            String image = metaData.getImage();
        //            System.out.printf("tokenId %s  ,  metadata :  %s \n", tokenId, b);
        //            boolean c = isRedeemable(String.valueOf(tokenId), contractAddress);
        //            System.out.printf("tokenId %s  ,  is redeemable : %b \n\n", tokenId, c);
        //        }

        List<String> arrayIpfs = Arrays.asList("https://ipfs.io/ipfs/QmRZHJi8wE6sw9J6TNcMh4bk1yv1i2NjBzBQ2iAkgwJTWB");

        Set<String> unique = new HashSet<>(arrayIpfs);
        for (String ipfsUrl : unique) {
            String contentType = getContentType(ipfsUrl,5,1000);
//            System.out.println("ipfsUrl : " + ipfsUrl + " type : " + contentType);
            System.out.printf("update contract_state set mediatype ='%s' where assetmediaipfsurl ='%s'",contentType,ipfsUrl);
            System.out.println();
        }

        System.exit(1);

    }

    public static String getTokenUri(final String tokenId, final String contractAddress) {

        try {
            //            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));

            //            final Credentials yellowHeartCred = Credentials.create("95d89bdc0e258a26d709ac4f80b5dff93b0d29db5fe7dac23d188f0d742eb46");

            final Function function = new Function("tokenURI", Collections.singletonList(new Uint256(new BigInteger(tokenId))),
                Collections.singletonList(new TypeReference<Utf8String>() {
                }));
            final String encodedFunction = FunctionEncoder.encode(function);

            final Transaction transaction =
                Transaction.createEthCallTransaction("0x04b00a1af79181e956b3d5572eb1ab491e29c2e4", contractAddress, encodedFunction);

            final EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            final List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            final String response = results.get(0).getValue().toString(); // have to check the response
            System.out.println("tokenURI : " + response);
            final String metaData = get(response);

            return metaData;

        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return "{\"error\":\"Error in getting token URI\"}";
    }

    public static boolean isRedeemable(final String tokenId, final String contractAddress) {

        try {
            //            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            final Function function = new Function("isRedeemable", Collections.singletonList(new Uint256(new BigInteger(tokenId))),
                Collections.singletonList(new TypeReference<Bool>() {
                }));

            final String encodedFunction = FunctionEncoder.encode(function);

            final Transaction transaction =
                Transaction.createEthCallTransaction("0x04b00a1af79181e956b3d5572eb1ab491e29c2e4", contractAddress, encodedFunction);

            final EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            final List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            final boolean response = (boolean) results.get(0).getValue(); // have to check the response

            return response;

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static final String ownerOf(final String contractAddress, final String tokenId) {
        try {
            //            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mainnet.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            // OwnerOf function
            final Function function = new Function("ownerOf", Collections.singletonList(new Uint256(new BigInteger(tokenId))),
                Collections.singletonList(new TypeReference<Address>() {
                }));

            final String encodedFunction = FunctionEncoder.encode(function);

            final Transaction transaction =
                Transaction.createEthCallTransaction("0x63C1623002782612D4Ff7d9C6945Ddaf01684366", contractAddress, encodedFunction);

            final EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            final List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            final String owner = results.get(0).getValue().toString();

            return results.toString();

        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return "{\"error\":\"Error in getting the owner\"}";
    }

    public static Integer totalSupply(String contractAddress) {
        try {
            final Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.infura.io/v3/55df959eede4493abf69725d1a8ce4e8"));
            final Function function =
                new Function("totalSupply", Collections.emptyList(), Collections.singletonList(new TypeReference<Uint256>() {
                }));
            final String encodedFunction = FunctionEncoder.encode(function);

            final Transaction transaction =
                Transaction.createEthCallTransaction("0x04b00a1af79181e956b3d5572eb1ab491e29c2e4", contractAddress, encodedFunction);

            final EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            final List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            //            System.out.println(results.get(0).getValue());

            return Integer.parseInt(results.get(0).getValue().toString());

        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return 0;
    }


    private static String get(final String httpsURL) {
        StringBuffer response = new StringBuffer();
        try {
            final URL obj = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux x86_64) YHAPI/1.0");
            //            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            final int responseCode = con.getResponseCode();
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public final static String getContentType(final String httpsUrl,final int retry, final long sleepDuration) {
        try {
            String contentType = "text/html";
            int attempt= 0;
            while(attempt<retry){
                final URL url = new URL(httpsUrl);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                contentType = connection.getContentType();
                if(!contentType.equals("text/html")){
                    break;
                }
                Thread.sleep(sleepDuration);
                attempt++;
            }

            return contentType;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private static BigInteger getEthGasStationPrice() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        final String url = "https://ethgasstation.info/api/ethgasAPI.json";
        final String response = get(url);
        //Read JSON response and print
        final EthResponse res = mapper.readValue(response.toString(), EthResponse.class);
        final BigDecimal gasPrice = Convert.toWei(String.valueOf(Math.ceil(res.fast)), Convert.Unit.GWEI);

        return gasPrice.toBigInteger();
    }

    public static class EthResponse {
        private int fast;
        private int fastest;
        private int safeLow;
        private int average;
        private float blockTime;
        private int blockNum;
        private float speed;
        private float safeLowWait;
        private float avgWait;
        private float fastWait;
        private float fastestWait;

        public int getFast() {
            return fast;
        }

        public void setFast(int fast) {
            this.fast = fast;
        }

        public int getFastest() {
            return fastest;
        }

        public void setFastest(int fastest) {
            this.fastest = fastest;
        }

        public int getSafeLow() {
            return safeLow;
        }

        public void setSafeLow(int safeLow) {
            this.safeLow = safeLow;
        }

        public int getAverage() {
            return average;
        }

        public void setAverage(int average) {
            this.average = average;
        }

        public float getBlockTime() {
            return blockTime;
        }

        public void setBlockTime(float blockTime) {
            this.blockTime = blockTime;
        }

        public int getBlockNum() {
            return blockNum;
        }

        public void setBlockNum(int blockNum) {
            this.blockNum = blockNum;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getSafeLowWait() {
            return safeLowWait;
        }

        public void setSafeLowWait(float safeLowWait) {
            this.safeLowWait = safeLowWait;
        }

        public float getAvgWait() {
            return avgWait;
        }

        public void setAvgWait(float avgWait) {
            this.avgWait = avgWait;
        }

        public float getFastWait() {
            return fastWait;
        }

        public void setFastWait(float fastWait) {
            this.fastWait = fastWait;
        }

        public float getFastestWait() {
            return fastestWait;
        }

        public void setFastestWait(float fastestWait) {
            this.fastestWait = fastestWait;
        }
    }


    @JsonInclude(JsonInclude.Include.NON_NULL) public static class MetaData {

        @JsonProperty("name") private String name;
        @JsonProperty("description") private String description;
        @JsonProperty("image") private String image;
        @JsonProperty("seller_fee_basis_points") private int sellerFeeBasisPoints;
        @JsonProperty("fee_recipient") private String feeRecipient;

        public MetaData() {
        }

        public MetaData(String name, String description, String image, int sellerFeeBasisPoints, String feeRecipient) {
            super();
            this.name = name;
            this.description = description;
            this.image = image;
            this.sellerFeeBasisPoints = sellerFeeBasisPoints;
            this.feeRecipient = feeRecipient;
        }

        @JsonProperty("name") public String getName() {
            return name;
        }

        @JsonProperty("name") public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("description") public String getDescription() {
            return description;
        }

        @JsonProperty("description") public void setDescription(String description) {
            this.description = description;
        }

        @JsonProperty("image") public String getImage() {
            return image;
        }

        @JsonProperty("image") public void setImage(String image) {
            this.image = image;
        }

        @JsonProperty("seller_fee_basis_points") public int getSellerFeeBasisPoints() {
            return sellerFeeBasisPoints;
        }

        @JsonProperty("seller_fee_basis_points") public void setSellerFeeBasisPoints(int sellerFeeBasisPoints) {
            this.sellerFeeBasisPoints = sellerFeeBasisPoints;
        }

        @JsonProperty("fee_recipient") public String getFeeRecipient() {
            return feeRecipient;
        }

        @JsonProperty("fee_recipient") public void setFeeRecipient(String feeRecipient) {
            this.feeRecipient = feeRecipient;
        }

    }

}
