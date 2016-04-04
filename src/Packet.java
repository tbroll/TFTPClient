import java.lang.*;
public class Packet{

    protected final int MAXPACKETSIZE = 516;
    protected byte[] opcode;
    protected byte[] filename;
    protected byte[] mode;
    protected byte delimiter = 0;
    protected byte[] blocknumber = new byte[2];
    protected byte[] data;
    protected byte[] errorcode; 
    protected byte[] errmsg;


    public Packet(byte opcode, String filename, String mode){
        this.opcode =new byte[]{0, opcode};
        this.filename = filename.getBytes();
        this.mode = mode.getBytes();
    }
    public Packet(byte opcode, byte[] blocknumber, byte[] data){
        this.opcode = new byte[]{0, opcode};
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
        this.data = data;
    }
    public Packet(byte opcode, byte [] blocknumber){
        this.opcode = new byte[]{0, opcode};
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
    }
    public Packet(byte opcode, byte errorcode, String errmsg){
        this.opcode = new byte[]{0, opcode};
        this.errorcode = new byte[]{0, errorcode};
        this.errmsg = errmsg.getBytes();
    }
    public byte[] BuildPacket12(){
        byte[] packet = new byte [4 + filename.length + mode.length];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(filename, 0, packet, opcode.length, filename.length);
        packet[opcode.length + filename.length] = 0;
        System.arraycopy(mode, 0, packet, opcode.length+filename.length, mode.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
    public byte[] BuildPacket3(){
        byte[] packet = new byte [4 + blocknumber.length + data.length];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(blocknumber, 0, packet, opcode.length, blocknumber.length);
        System.arraycopy(data, 0, packet, opcode.length+blocknumber.length, data.length);
        return packet; 

    }
    public byte[] BuildPacket4(){
        byte[] packet = new byte [4];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(blocknumber, 0, packet, opcode.length, 2);
        return packet; 
    }
    public byte[] BuildPacket5(){
        byte[] packet = new byte [opcode.length+errorcode.length+errmsg.length];
        System.arraycopy(opcode, 0, packet, 0, 2);
        System.arraycopy(errorcode, 0, packet, opcode.length, 2);
        System.arraycopy(errmsg, 0, packet, opcode.length, errmsg.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
    public void setOpcode(byte[] opcode){
        this.opcode = opcode;
    }
    public byte[] getOpcode(){
        return opcode;
    }
    public void setFileName(String filename){
        this.filename = filename.getBytes();
    }
    public byte[] getFilename(){
        return filename;
    }
    public void setMode(String mode){
        this.mode = mode.getBytes();
    }
    public byte[] getMode(){
        return mode;
    }
    public void setblockNumber(byte[] blocknumber){
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
    }
    public byte[] getblockNumber(){
        return blocknumber;
    }
    public void setData(byte[] data){
        this.data = data;
    }
    public byte[] getData(){
        return data;
    }
    public int getMaxPacketSize(){
        return MAXPACKETSIZE;
    }
}


