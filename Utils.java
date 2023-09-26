package com.mycompany.xbox360s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Slam
 */
public class Utils {

    public byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
    public String hexToAscii(byte[] hex){
        return new String(hex,StandardCharsets.US_ASCII);
    }

    public String hexToAscii(String hexString) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexString.length(); i += 2) {
            String hex = hexString.substring(i, i + 2);
            int decimal = Integer.parseInt(hex, 16);
            output.append((char) decimal);
        }

        return output.toString();
    }

    public String asciiToHex(String asciiString) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < asciiString.length(); i++) {
            char c = asciiString.charAt(i);
            String hex = Integer.toHexString((int) c);
            if (hex.length() < 2) {
                hex = "0" + hex; // Agregar cero a la izquierda si es necesario
            }
            output.append(hex);
        }

        return output.toString().toUpperCase();
    }

    public void sendData(String data, String hostname, int rport) {
        try {
            DatagramSocket sock = new DatagramSocket();
            sock.setSoTimeout(500);
            byte[] sdata = data.getBytes();
            sock.send(new DatagramPacket(sdata, sdata.length, new InetSocketAddress(hostname, rport)));

            byte[] buf = new byte[Byte.MAX_VALUE];
            DatagramPacket rdp = new DatagramPacket(buf, buf.length);
            sock.receive(rdp);
            String response = new String(rdp.getData(), 0, rdp.getLength());
            if (!response.isEmpty()) {
                System.out.println("Response data: " + response);
                System.out.println("Hex data: " + asciiToHex(response));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // validating if are correct
    public boolean isAnIP(String ip) {

        if (ip.contains(".") && ip.split("\\.").length == 4) {
            String[] parts = ip.split("\\.");
            for (String part : parts) {
                try {
                    int value = Integer.parseInt(part);
                    if (value < 0 || value > 255) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String checkIP(String ip) {
        return ip.replace('(', ' ').replace(')', ' ').trim();
    }
}
