import java.lang.*;
public class ErrorPacket {

    private final String opcode = "5";
    private byte errorcode;
    private String errmsg;

    // This creates a Error Packet Packet

    public ErrorPacket(byte errorcode, String errmsg){
        this.errorcode = errorcode;
        this.errmsg = errmsg;
    }

    // this builds the error packet to be put into a DatagramPacket and sent to
    // or received from the server

    public byte[] createPacket(){

        byte[] oc = opcode.getBytes(); 
        byte[] errorCode = {0, errorcode};
        byte[] errorMsg = errmsg.getBytes();

        byte[] packet = new byte [oc.length+errorCode.length+errorMsg.length];

        System.arraycopy(oc, 0, packet, 0, 2);
        System.arraycopy(errorCode, 0, packet, oc.length, 2);
        System.arraycopy(errorMsg, 0, packet, oc.length, errorMsg.length);

        packet[packet.length-1] = 0;

        return packet; 
    }
}
