import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet(URI);
        List<Cat> cats = new ArrayList<>();
        try(CloseableHttpResponse response = httpClient.execute(request);){
            cats = mapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        cats.stream().filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                .sorted(Comparator.comparingLong(Cat::getUpvotes).reversed())
                .forEach(System.out::println);
    }
}
