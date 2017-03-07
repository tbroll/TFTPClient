import java.lang.*;
public class AckPacket{

    private final String opcode = "4";
    private byte[] blocknumber;

    // This creates a Acknowledgement Packet

    public AckPacket(byte [] blocknumber){
        this.blocknumber = blocknumber;
    }

    // this builds the acknowledgement packet to be put into a DatagramPacket
    // and sent to or received from the server

    public byte[] createPacket(){

        byte[] packet = new byte [4];

        byte[] oc = opcode.getBytes(); 

        System.arraycopy(oc, 0, packet, 0, oc.length);
        System.arraycopy(blocknumber, 0, packet, oc.length, 2);

        return packet; 
    }
}

