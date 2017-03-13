import java.lang.*;
public class ReadWritePacket {
    private final byte opcode;
    private String filename;
    private String mode;

    public ReadWritePacket(byte opcode,String filename, String mode){
        this.opcode = opcode;
        this.filename = filename;
        this.mode = mode;
    }
    // this builds the read or write request packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] build(){
        byte[] packet = new byte [4 + filename.length() + mode.length()];
        byte[] optcode = {0, opcode};
        byte[] filename1 = filename.getBytes();
        byte[] mode1 = mode.getBytes();
        System.arraycopy(optcode, 0, packet, 0, optcode.length);
        System.arraycopy(filename1, 0, packet, optcode.length, filename1.length);
        packet[optcode.length + filename1.length] = 0;
        System.arraycopy(mode1, 0, packet, optcode.length+filename1.length+1, mode1.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
}


