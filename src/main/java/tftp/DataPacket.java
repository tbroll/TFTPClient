package tftp;
import java.lang.*;
import java.nio.ByteBuffer;
public class DataPacket {

    private final short opcode = 3;
    private short blocknumber;
    private byte [] data;

    // This creates a Data Packet

    public DataPacket(short blocknumber, byte[] data){
        this.blocknumber = blocknumber;
        this.data = data;
    }

    // this method builds the data packet to be put into a DatagramPacket and
    // sent to or received from the server

    public byte[] createPacket(){

        ByteBuffer packet = ByteBuffer.allocate(4+data.length);

        packet.putShort(opcode);
        packet.putShort(blocknumber);
        packet.put(data);

        return packet.array();
    }
}

