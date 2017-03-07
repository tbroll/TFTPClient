import java.lang.*;
import java.nio.*;
public class ReadPacket {

    private final short opcode = 1;
    private String filename;
    private String mode;

    public ReadPacket(String filename, String mode){
        this.filename = filename;
        this.mode = mode;
    }

    // this builds the read or write request packet to be put into a
    // DatagramPacket and sent to or received from the server

    public byte[] createPacket(){

        byte[] fn = filename.getBytes();
        byte[] mo = mode.getBytes();
        byte zero = 0;

        ByteBuffer packet = ByteBuffer.allocate(2+fn.length+1+mo.length+1);

        packet.putShort(opcode);
        packet.put(fn);
        packet.put(zero);
        packet.put(mo);
        packet.put(zero);

        return packet.array();
    }
}

