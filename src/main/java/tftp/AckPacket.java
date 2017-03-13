package tftp;
import java.lang.*;
import java.nio.ByteBuffer;
public class AckPacket{

    private final short opcode = 4;
    private short blocknumber;

    // This creates a Acknowledgement Packet

    public AckPacket(short blocknumber){
        this.blocknumber = blocknumber;
    }

    // this builds the acknowledgement packet to be put into a DatagramPacket
    // and sent to or received from the server

    public byte[] createPacket(){

        ByteBuffer packet = ByteBuffer.allocate(4);

        packet.putShort(opcode);
        packet.putShort(blocknumber);

        return packet.array();

    }
}

