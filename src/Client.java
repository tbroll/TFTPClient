import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.ByteBuffer;

public class Client{
    private DatagramSocket socket;
    private FileInputStream fis;
    private FileOutputStream fos;
    private DatagramPacket inboundPacket;
    private DatagramPacket outboundPacket;
    private int serverPort; 
    private InetAddress serverAddress; 

    private final int MAXPACKETSIZE = 516;
    private String server = "10.19.105.48";
    private int initialPort = 69;
    private String file = "test.txt";
    private String mode = "octet";
    private final int TIMEOUT = 10000;

    private final byte optRead = 1;
    private final byte optWrite = 2;

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
            socket = new DatagramSocket() ;
            serverAddress = InetAddress.getByName(server);
            ReadWritePacket ReadPacket = new ReadWritePacket(optRead, file, mode);
            DatagramPacket packet = new DatagramPacket(ReadPacket.build(),
                    ReadPacket.build().length, serverAddress, initialPort);
            socket.setSoTimeout(TIMEOUT);
            socket.send(packet);
            receiveData(file);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    // the server sends the data back to the client (aka me)
    private void receiveData(String file){
        byte[] block = {0,1};
        byte[] data = new byte[MAXPACKETSIZE];
        byte errorCode; 
        inboundPacket= new DatagramPacket(data,
                data.length, serverAddress, serverPort);
        do {
            try{
                socket.receive(inboundPacket);
                serverPort = inboundPacket.getPort();
                if(inboundPacket.getData()[1] == 3){
                    block[0] = inboundPacket.getData()[2];
                    block[1] = inboundPacket.getData()[3];
                    sendAck(block);
                    saveBitsToFile(file);
                }
                else if(inboundPacket.getData()[1] == 5){
                    errorCode = inboundPacket.getData()[3];
                    sendError(errorCode);
                }
                else{

                }
            }
            catch(IOException e){
                System.out.println("error:" + e.getMessage());
            }
        }
        while(inboundPacket.getLength() == 516);
    }
    private void saveBitsToFile(String file) {
        try{
            fos = new FileOutputStream(file, true);
            if(inboundPacket.getLength() <= 511){ 
                fos.write(inboundPacket.getData(), 4, inboundPacket.getLength()-4);
            }
            else{
                fos.write(inboundPacket.getData(), 4, inboundPacket.getData().length-4);
            }
            fos.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    // sends an ack packet back to the server
    private void sendAck(byte[] blocknumber){
        AckPacket ack = new AckPacket(blocknumber);
        DatagramPacket ACK = new DatagramPacket(ack.build(),
                ack.build().length, serverAddress, serverPort);
        try{
            socket.send(ACK);
        }
        catch(IOException e){
            System.out.println("Failed to send ack packet");
        }
    }
    //builds and sends an error packet
    private void sendError(byte errorCode){
        ErrorPacket error = new ErrorPacket(errorCode, Errmsg(errorCode)); 
        DatagramPacket packet = new DatagramPacket(error.build(),
                error.build().length, serverAddress, serverPort);
        try{
            socket.send(packet);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Error: " + errorCode + " " + Errmsg(errorCode));
    }
    //returns the error msg that corresponds to the error code
    private String Errmsg(byte errorCode){
        if(errorCode == 0){
            return "Not defined, see error message if any";
        }
        else if(errorCode == 1){
            return "file not found";
        }
        else if(errorCode == 2){
            return "Access Violation";
        }
        else if(errorCode == 3){
            return "disk full or allocation exceeded";
        }
        else if(errorCode == 4){
            return "Illegal TFTP operation";
        }
        else if(errorCode == 5){
            return "Unknown transfer ID";
        }
        else if(errorCode == 6){
            return "file already exists";
        }
        else{
            return "no such user";
        }
    }
    // sends the initial write request
    private void WriteRequest(String file, String mode){
        try{
            socket = new DatagramSocket() ;
            serverAddress = InetAddress.getByName(server);
            ReadWritePacket writePacket= new ReadWritePacket(optWrite, file, mode);
            outboundPacket = new DatagramPacket( writePacket.build(),
                    writePacket.build().length, serverAddress, initialPort); 
            socket.setSoTimeout(TIMEOUT);
            socket.send(outboundPacket);
            initializeConnection();
        }
        catch(IOException e){
            System.out.println("There was an issue with the WriteRequest"); 
        }
    }
    private void initializeConnection(){
        byte[] blocknumber = {0,0};
        byte[] ack = new byte[4];
        DatagramPacket Ack = new DatagramPacket(ack, ack.length, serverAddress, serverPort);
        try{
           socket.receive(Ack);
           serverPort = Ack.getPort();
           incrementCount(blocknumber);
           if(Ack.getData()[1] == 4){
           continueWrite(blocknumber);
           }
           else{
               System.out.println("Cannot initialize connection");
               System.exit(1);
           }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    //I send the data to the server
    private void continueWrite(byte[] blocknumber){
            try{
                File newfile = new File(file);
                fis = new FileInputStream(newfile);
                byte[] data =putDataIntoByteArray(fis, newfile);
                System.out.println("Name of file: " + file);
                System.out.println("remaining data to send: " + fis.available());
                System.out.println("Size of file: " + newfile.length());
                fis.close();
                DataPacket Data = new DataPacket(blocknumber, data);
                outboundPacket = new DatagramPacket(Data.build(),
                        Data.build().length-2, serverAddress, serverPort);
                socket.send(outboundPacket);
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    //builds my data packet
    private byte[] putDataIntoByteArray(FileInputStream fis, File file){
        byte[] a = new byte[Math.min(512, (int)file.length())];
                System.out.println("Length of data array: " + a.length);
        try{
            fis.read(a, 0, Math.min(512,(int)file.length()));
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
