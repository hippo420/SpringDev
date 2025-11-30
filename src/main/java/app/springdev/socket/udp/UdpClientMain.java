package app.springdev.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpClientMain {

    static final String SERVER = "localhost";
    static final int SERVERPORT = 9999;
    static final int TIMEOUT_MS = 1000; // 수신 대기 타임아웃 (옵션)
    public static void main(String[] args) {

        System.out.println("✅ UDP Client started. Sending initial request...");

        try (DatagramSocket clientSocket = new DatagramSocket()) {

            InetAddress serverAddress = InetAddress.getByName(SERVER);

            // 1. 요청 전송 (Only Once)
            // -----------------------------------------------------------------
            String jsonStr =
                    "{"
                            + "\"api_key\":\"testkey123\","
                            + "\"trnm\":\""+UDPConstant.STOCK_CODE_SHORT+"\"," // 예: 등록(REG) 요청
                            + "\"grp_no\":\"10\","
                            + "\"data\":["
                            + "{\"item\":\"001045\",\"type\":\"10\"}"
                            + "],"
                            + "\"refresh\":\"1\""
                            + "}";

            byte[] sendBuffer = jsonStr.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket =
                    new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, SERVERPORT);

            clientSocket.send(sendPacket);
            System.out.println("[SEND ONCE] " + jsonStr);
            // -----------------------------------------------------------------

            // 2. 이후 무한 루프를 통해 서버 메시지 지속 수신
            // -----------------------------------------------------------------
            System.out.println("\n--- Start Continuous Receiving Loop ---");
            // clientSocket.setSoTimeout(TIMEOUT_MS); // 필요 시 타임아웃 설정 가능 (선택 사항)

            while (true) {
                byte[] receiveBuffer = new byte[2048];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // 새로운 데이터가 도착할 때까지 블로킹 (Blocking)
                clientSocket.receive(receivePacket);

                String response = new String(
                        receivePacket.getData(),
                        0,
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("[RECV] "
                        + response
                        + " (From: "
                        + receivePacket.getAddress().getHostAddress()
                        + ":"
                        + receivePacket.getPort()
                        + ")");

                // 실제 애플리케이션에서는 여기서 수신된 데이터를 처리하는 로직을 추가합니다.
            }
            // -----------------------------------------------------------------

        } catch (java.net.SocketTimeoutException e) {
            // setSoTimeout을 설정했을 경우, 타임아웃 발생 시 처리
            System.err.println("❌ 수신 대기 타임아웃 발생: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ UDP 통신 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("🏁 UDP Client terminated.");
        }
    }
}
