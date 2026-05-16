package ru.itis.dis403.lab2_9.httpclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Service
public class ImageServiceForNats {
    private Connection nc;

    @PostConstruct
    public void init() throws Exception {
        nc = Nats.connect(natsUrl);
    }

    private List<String> imgList = new ArrayList<>();

    private String natsUrl = "nats://localhost:4222";
    // Описываем тему сообщения в канал
    private String subject = "request.image.mirror";

    public List<String> getImgList() {
        return imgList;
    }

    // сформировать клиент к брокеру сообщений
    public String processImage(byte[] image) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("service", "mirror");
        requestMap.put("image", Base64.getEncoder().encodeToString(image));

        try {
//            System.out.println("connected");

            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(requestMap);
//            System.out.println("send JSON: " + jsonRequest.substring(0,30));

            Message reply = nc.request(subject, jsonRequest.getBytes(),
                    Duration.ofSeconds(10));

            // Парсим JSON-ответ
            String jsonResponse = new String(reply.getData(), StandardCharsets.UTF_8);
//            System.out.println("response " + jsonResponse);
            Map<String, String> resultMap = mapper.readValue(jsonResponse, Map.class);

            if (resultMap.get("status") != null && resultMap.get("status").equals("success")) {
                imgList.add(resultMap.get("image"));
            }

            return jsonResponse;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Запрос прерван");
        } catch (Exception e) {
            System.out.println("NATS timeout");
        }
        // TODO описание ошибки
        return "{}";
    }


}
