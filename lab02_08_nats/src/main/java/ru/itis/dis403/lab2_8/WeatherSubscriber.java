package ru.itis.dis403.lab2_8;
import io.nats.client.*;

public class WeatherSubscriber {
    public static void main(String[] args) {
        String subject = "Weather";

        try (Connection nc = Nats.connect("nats://localhost:4222")) {

            Dispatcher dispatcher = nc.createDispatcher(msg -> {
                String data = new String(msg.getData());
                System.out.println("Получено: " + data);
            });

            dispatcher.subscribe(subject);

            System.out.println("Слушаем тему Weather...");

            Thread.sleep(600000); // 10 минут живём
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}