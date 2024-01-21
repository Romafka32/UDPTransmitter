package org.example.udptranslation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Класс для отправки данных по UDP
 */

public class Sender {
    /**
     * Поле порта со значением из файла свойств приложения
     */
    @Value("$udp_port")
    private int port;

    /**
     * Поле ip-адреса со значением из файла свойств приложения
     */
    @Value("$ip_address")
    private String ip;

    /**
     * Поле сокета
     */
    private final DatagramSocket socket;

    /**
     * Поле ip-адреса
     */
    private final InetAddress address;

    public Sender() throws UnknownHostException, SocketException {
        socket = new DatagramSocket(port);
        address = InetAddress.getByName(ip);
    }

    /**
     * Класс отправляет датаграмму на заданный порт
     *
     * @param msg строка с информацией о системе
     */
    public void sendData(String msg) throws IOException {
        byte[] buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        System.out.println(msg);
        socket.close();
    }
}
