package org.example.udptranslation;

import java.io.IOException;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.json.JSONObject;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс конфигурации Spring Boot. Точка входа в программу.
 */

@SpringBootApplication
public class UdpTranslationApplication {

    private static final int INTERVAL = 1000;

    public static void main(String[] args) throws JSONException {
        SpringApplication.run(UdpTranslationApplication.class, args);

        //создаем JSON объект для данных
        JSONObject json = new JSONObject();

        //цикл для организации отправки сообщений с интервалом раз в секунду
        while (true) {
            // заполняем данные
            String message = DataService.fillData(json);

            try {
                //отправляем
                Sender client = new Sender();
                client.sendData(message);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try{
                Thread.sleep(INTERVAL);
            }catch(InterruptedException e) {
                System.out.println("Error while sleeping!");
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
