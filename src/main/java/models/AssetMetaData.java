package models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "description",
    "image",
    "seller_fee_basis_points",
    "fee_recipient"
})
@Generated("jsonschema2pojo")
public class AssetMetaData {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("image")
    private String image;
    @JsonProperty("seller_fee_basis_points")
    private int sellerFeeBasisPoints;
    @JsonProperty("fee_recipient")
    private String feeRecipient;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public AssetMetaData withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public AssetMetaData withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    public AssetMetaData withImage(String image) {
        this.image = image;
        return this;
    }

    @JsonProperty("seller_fee_basis_points")
    public int getSellerFeeBasisPoints() {
        return sellerFeeBasisPoints;
    }

    @JsonProperty("seller_fee_basis_points")
    public void setSellerFeeBasisPoints(int sellerFeeBasisPoints) {
        this.sellerFeeBasisPoints = sellerFeeBasisPoints;
    }

    public AssetMetaData withSellerFeeBasisPoints(int sellerFeeBasisPoints) {
        this.sellerFeeBasisPoints = sellerFeeBasisPoints;
        return this;
    }

    @JsonProperty("fee_recipient")
    public String getFeeRecipient() {
        return feeRecipient;
    }

    @JsonProperty("fee_recipient")
    public void setFeeRecipient(String feeRecipient) {
        this.feeRecipient = feeRecipient;
    }

    public AssetMetaData withFeeRecipient(String feeRecipient) {
        this.feeRecipient = feeRecipient;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public AssetMetaData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AssetMetaData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("image");
        sb.append('=');
        sb.append(((this.image == null)?"<null>":this.image));
        sb.append(',');
        sb.append("sellerFeeBasisPoints");
        sb.append('=');
        sb.append(this.sellerFeeBasisPoints);
        sb.append(',');
        sb.append("feeRecipient");
        sb.append('=');
        sb.append(((this.feeRecipient == null)?"<null>":this.feeRecipient));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
