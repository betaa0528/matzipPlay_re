package com.restaurantProject.famousrestaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.net.URLEncoder;


@Component
public class KakaoMapService {
    public String[] map(String address) {
        String[] result = new String[2];

        String REST_API_KEY = "4fb2e14196899532232a39b95c2a1218";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 쿼리 문자열을 URL 인코딩
            String encodedQuery = URLEncoder.encode(address, "UTF-8");

            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedQuery;
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Authorization", "KakaoAK " + REST_API_KEY);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseString = EntityUtils.toString(entity);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseString);
                String x = rootNode.get("documents").get(0).get("address").get("x").asText();
                String y = rootNode.get("documents").get(0).get("address").get("y").asText();

                result[0] = x;
                result[1] = y;

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
