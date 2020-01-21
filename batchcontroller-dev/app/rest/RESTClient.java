package rest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RESTClient {

    private static final String url = "http://localhost:8080/engine-rest/";
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    public static int newRequest(String payload, String method){

        int code = 0;

        try{
            StringEntity jsonEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            HttpPost request = new HttpPost(url+method);
            request.addHeader("content-type", "application/json");
            request.setEntity(jsonEntity);
            CloseableHttpResponse response = httpclient.execute(request);
            code = response.getStatusLine().getStatusCode();

        }
        catch(Exception e){

        }

        return code;
    }
}
