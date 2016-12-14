package cn.com.open.opensass.privilege.model;

/**
 * Created by jh on 2016/12/13.
 */
public class PivilegeToken {
    /*应用id*/
    private String appId;
    /*应用appKey。*/
    private String appKey;
    /*签名结果串*/
    /**
     * signature签名规则：
     对参数进行Hmac—Sha1加密，加密格式appId&appKey&timestamp&signatureNonce，
     加密secret为用户中心分配的密钥,用%2B替换加密结果生成的“+”号字符。（Hmac-Sha1格式为Base64形式）
     */
    private String signature;
    /**
     *
     请求的时间戳。日期格式按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为：
     YYYY-MM-DDThh:mm:ssZ
     例如，2014-05-26T12:00:00Z（为北京时间 2014 年 5 月 26 日 12 点 0 分 0 秒）。
     */
    private String timestamp;
    /**
     * 唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值
     */
    private String signatureNonce;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignatureNonce() {
        return signatureNonce;
    }

    public void setSignatureNonce(String signatureNonce) {
        this.signatureNonce = signatureNonce;
    }
}
