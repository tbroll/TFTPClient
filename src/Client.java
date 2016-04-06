import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.ByteBuffer;

public class Client{
    private DatagramSocket socket;
    private final int MAXPACKETSIZE = 516;
    private DatagramPacket packet;
    private FileInputStream fis;
    private FileOutputStream fos;
    private DatagramPacket inboundPacket;
    private DatagramPacket outboundPacket;

    private InetAddress serverAddress; 

    private String server = "10.19.80.228";
    private int initialPort = 69;
    private int serverPort;

    private String file = "bugoti.png";
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
        try{
            DatagramSocket socket = new DatagramSocket() ;
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
            receiveData(socket, serverAddress);
        }
        catch(IOException e){
            System.out.println("There was an issue with the ReadRequest"); 
        }
    }
    // the server sends the data back to the client (aka me)
    private void receiveData(DatagramSocket socket, InetAddress serverName){
        byte[] block = new byte[2];
        byte[] data = new byte[512];
        byte[] errorCode = new byte[2];
        byte[] opcode = new byte[2];
        Packet dataPacket = new Packet((byte)1, block, data); 
        DatagramPacket packet = new DatagramPacket(dataPacket.BuildPacket3(), dataPacket.BuildPacket3().length, serverName, serverPort);
        do {
            try{
                socket.receive(packet);
                serverPort = packet.getPort();
                if(packet.getData()[1] == 3){
                    block[0] = packet.getData()[2];
                    block[1] = packet.getData()[3];
                    sendAck(block, serverName, socket);
                    FileOutputStream fos = new FileOutputStream(file);
                    if(packet.getLength() <= 511){ 
                        fos.write(packet.getData(), 4, packet.getLength()-4);
                    }
                    else{
                        fos.write(packet.getData(), 4, packet.getData().length-4);
                    }
                    fos.close();
                }
                else if(packet.getData()[1] == 5){
                    errorCode[0] = packet.getData()[2];
                    errorCode[1] = packet.getData()[3];
                   reportError(errorCode);
                }
                else{

                }
            }
            catch(IOException e){
                System.out.println("error:" + e.getMessage());
            }
        }
        while(dataPacket.BuildPacket3().length == 516);
    }
    // sends an ack packet back to the server
    private void sendAck(byte[] blocknumber, InetAddress serverName, DatagramSocket socket){
        Packet ack = new Packet((byte) 4, blocknumber);
        DatagramPacket ACK = new DatagramPacket(ack.BuildPacket4(), ack.BuildPacket4().length, serverName, serverPort);
        try{
            socket.send(ACK);
        }
        catch(IOException e){
            System.out.println("Failed to send ack packet");
        }
    }
    //builds and sends an error packet
    private void reportError(byte[] errorCode){
            System.out.println("Error: " + errorCode[1] + " " + Errmsg(errorCode));

    }
    //returns the error msg that corresponds to the error code
    private String Errmsg(byte[] errorCode){
        if(errorCode[1] ==     0){
            return "Not defined, see error message if any";
        }
        else if(errorCode[1] == 1){
            return "file not found";
        }
        else if(errorCode[1] == 2){
            return "Access Violation";
        }
        else if(errorCode[1] == 3){
            return "disk full or allocation exceeded";
        }
        else if(errorCode[1] == 4){
            return "Illegal TFTP operation";
        }
        else if(errorCode[1] == 5){
            return "Unknown transfer ID";
        }
        else if(errorCode[1] == 6){
            return "file already exists";
        }
        else{
            return "no such user";
        }
    }
    // sends the initial write request
    private void WriteRequest(String file, String mode){
        try{
            DatagramSocket socket = new DatagramSocket() ;
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
            continueWrite(socket, serverAddress);
        }
        catch(IOException e){
            System.out.println("There was an issue with the ReadRequest"); 
        }
    }
    //I send the data to the server
    private void continueWrite(DatagramSocket socket, InetAddress serverName){
        byte[] blocknumber = new byte[]{0,0};
        byte[] Data = new byte[516];
                    DatagramPacket packet2 = new DatagramPacket(Data, Data.length, serverName, 0);
        try{
                do{
        Packet ackpacket = new Packet((byte)3, blocknumber);
        DatagramPacket packet1 = new DatagramPacket(ackpacket.BuildPacket4(), ackpacket.BuildPacket4().length, serverName, socket.getLocalPort());
            socket.receive(packet1);
            serverPort = packet1.getPort(); 
            if(packet1.getData()[1] == 5){
                System.out.println("error message");
                System.exit(1);
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                    blocknumber = incrementCount(blocknumber);
                     Data = SendData(fis, blocknumber); 
                    packet2 = new DatagramPacket(Data, Data.length, serverName, serverPort);
                    socket.send(packet2);
                }
                }
                while(packet2.getLength() ==516);

            }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    //builds my data packet
    private byte[] SendData(FileInputStream fis, byte[] block){
        byte[] a = new byte[516];
        a[0] = 0;
        a[1] = 3;
        a[2] = block[0];
        a[3] =block[1]; 
        try{
            fis.read(a, 4, 512);
        }
        catch(IOException e){
            System.out.println("error: " + e.getMessage());
        }
        return a;
    }
    //increments the blocknumber in the data packet
    private byte[] incrementCount(byte[] blocknumber){
        if(blocknumber[1] ==-1){
            blocknumber[0]++ ;
        }
        blocknumber[1]++; 
        return blocknumber;
    }


}
