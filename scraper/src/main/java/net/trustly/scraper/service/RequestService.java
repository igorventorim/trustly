package net.trustly.scraper.service;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Value("${url.server.prefix}")
    private String URL_PREFIX;

    private Response sendGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = (new Request.Builder()).url(url).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public String getHtmlPage(String url) {
        try {
            Response response = sendGetRequest(url);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in send request to url: " + url);
        }
    }

    public boolean isUrlRequestSuccessful(String url) {
        try {
            Response response = sendGetRequest(url);
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHtmlPageWithoutPrefix(String uri) {
        return getHtmlPage(this.URL_PREFIX + this.URL_PREFIX);
    }
}

