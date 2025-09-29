package app.springdev.socket.udp.thread;

import java.net.*;

public class UDPClientHandler implements Runnable{
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UDPClientHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        byte[] dataCopy = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), dataCopy, 0, packet.getLength());
        this.packet = new DatagramPacket(dataCopy, dataCopy.length, packet.getAddress(), packet.getPort());
    }

    @Override
    public void run() {
        try {
            String msg = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Server Response["+Thread.currentThread().getName()+"]: " + msg);
            String response = "Server Response: " + msg;
            byte[] sendData = response.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);

        } catch (Exception e) {
            System.err.println("Error on Processing: " + e.getMessage());
        }
    }
}
