import java.lang.*;
public class ReadPacket {
    private final String opcode = "1";
    private String filename;
    private String mode;

    public ReadPacket(String filename, String mode){
        this.filename = filename;
        this.mode = mode;
    }

    // this builds the read or write request packet to be put into a
    // DatagramPacket and sent to or received from the server

    public byte[] createPacket(){

        byte[] packet = new byte [4 + filename.length() + mode.length()];

        byte[] oc = opcode.getBytes(); 
        byte[] filename1 = filename.getBytes();
        byte[] mode1 = mode.getBytes();

        System.arraycopy(oc, 0, packet, 0, oc.length);
        System.arraycopy(filename1, 0, packet, oc.length, filename1.length);

        packet[oc.length + filename1.length] = 0;

        System.arraycopy(mode1, 0, packet, oc.length+filename1.length+1, mode1.length);
        packet[packet.length-1] = 0;

        return packet; 
    }
}

