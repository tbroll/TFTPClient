import java.lang.*;
public class ErrorPacket {
    private byte opcode;
    private byte errorcode;
    private String errmsg;
// This creates a Error Packet Packet
    public ErrorPacket(byte errorcode, String errmsg){
        this.opcode = 5;
        this.errorcode = errorcode;
        this.errmsg = errmsg;
    }
    // this builds the error packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] build(){
        byte[] optcode = {0,5};
        byte[] errorCode = {0, errorcode};
        byte[] errorMsg = errmsg.getBytes();
        byte[] packet = new byte [optcode.length+errorCode.length+errorMsg.length];
        System.arraycopy(optcode, 0, packet, 0, 2);
        System.arraycopy(errorCode, 0, packet, optcode.length, 2);
        System.arraycopy(errorMsg, 0, packet, optcode.length, errorMsg.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
}
