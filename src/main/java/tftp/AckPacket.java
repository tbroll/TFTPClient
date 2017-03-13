import java.lang.*;
public class AckPacket{
    private byte opcode;
    private byte[] blocknumber;
// This creates a Acknowledgement Packet
    public AckPacket(byte [] blocknumber){
        this.opcode = 4;
        this.blocknumber = blocknumber;
    }
    // this builds the acknowledgement packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] build(){
        byte[] packet = new byte [4];
        byte[] opcode = {0, 4};
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(blocknumber, 0, packet, opcode.length, 2);
        return packet; 
    }
}
