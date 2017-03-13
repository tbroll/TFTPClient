package tftp;
import java.lang.*;
import java.nio.ByteBuffer;
public class ErrorPacket {

    private final short opcode = 5;
    private short errorcode;
    private String errmsg;

    // This creates a Error Packet Packet

    public ErrorPacket(short errorcode, String errmsg){
        this.errorcode = errorcode;
        this.errmsg = errmsg;
    }

    // this builds the error packet to be put into a DatagramPacket and sent to
    // or received from the server

    public byte[] createPacket(){

        byte[] em = errmsg.getBytes();
        byte zero = 0;

        ByteBuffer packet = ByteBuffer.allocate(5+em.length);

        packet.putShort(opcode);
        packet.putShort(errorcode);
        packet.put(em);
        packet.put(zero);

        return packet.array();

    }
}
