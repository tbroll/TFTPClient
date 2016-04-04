import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.nio.ByteBuffer;

public class Client{
    private DatagramSocket socket;
    private final int MAXPACKETSIZE = 516;
    private DatagramPacket packet;
    private FileInputStream fis;
    private FileOutputStream fos;
    private ByteBuffer bb;

    private InetAddress serverAddress; 

    private String server = "192.168.0.16";
    private int initialPort = 69;

    private String file = "test.txt";
    private String mode = "octet";

    private final int TIMEOUT = 10000;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    private void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to read or write?");
        String value = scanner.next();
        switch(value){
            case "read": ReadRequest(file, mode);
                         break;
            default: WriteRequest(file, mode); 
                     break;
        }
    }
    // sends the initial read request
    private void ReadRequest(String file, String mode){
        try(DatagramSocket socket = new DatagramSocket()){ 
            serverAddress = InetAddress.getByName(server);
            Packet tftpPacket= new Packet((byte)1, file, mode);
            DatagramPacket packet = new DatagramPacket( tftpPacket.BuildPacket12(), tftpPacket.BuildPacket12().length, serverAddress, initialPort); 
            socket.setSoTimeout(TIMEOUT);
            if(tftpPacket.BuildPacket12().length <= MAXPACKETSIZE){
                socket.send(packet);
            }
            else{
                System.out.println("Packet too big");
            }
        }
        catch(IOException e){
            System.out.println("There was an issue with the ReadRequest"); 
        }
    }
    // the server sends the data back to the client (aka me)
    private void continueRead(){
    }
    // sends the initial write request
    private void WriteRequest(String file, String mode){
        try(DatagramSocket socket = new DatagramSocket()){ 
            serverAddress = InetAddress.getByName(server);
            Packet tftpPacket= new Packet((byte)2, file, mode);
            DatagramPacket packet = new DatagramPacket( tftpPacket.BuildPacket12(), tftpPacket.BuildPacket12().length, serverAddress, initialPort); 
            socket.setSoTimeout(TIMEOUT);
            if(tftpPacket.BuildPacket12().length <= MAXPACKETSIZE){
                socket.send(packet);
            }
            else{
                System.out.println("Packet too big");
            }
        }
        catch(IOException e){
            System.out.println("There was an issue with the ReadRequest"); 
        }
    }
    //I send the data to the server
    private void continueWrite(){
    }
}
