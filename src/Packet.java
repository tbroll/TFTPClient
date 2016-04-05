import java.lang.*;
public class Packet{

    protected byte[] opcode;
    protected byte[] filename;
    protected byte[] mode;
    protected byte delimiter = 0;
    protected byte[] blocknumber = new byte[2];
    protected byte[] data;
    protected byte[] errorcode; 
    protected byte[] errmsg;

// This creates a Read or Write Request Packet
    public Packet(byte opcode, String filename, String mode){
        this.opcode =new byte[]{0, opcode};
        this.filename = filename.getBytes();
        this.mode = mode.getBytes();
    }
// This creates a Data Packet
    public Packet(byte opcode, byte[] blocknumber, byte[] data){
        this.opcode = new byte[]{0, opcode};
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
        this.data = data;
    }
// This creates a Acknowledgement Packet
    public Packet(byte opcode, byte [] blocknumber){
        this.opcode = new byte[]{0, opcode};
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
    }
// This creates a Error Packet Packet
    public Packet(byte opcode, byte errorcode, String errmsg){
        this.opcode = new byte[]{0, opcode};
        this.errorcode = new byte[]{0, errorcode};
        this.errmsg = errmsg.getBytes();
    }
    // this builds the read or write request packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] BuildPacket12(){
        byte[] packet = new byte [4 + filename.length + mode.length];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(filename, 0, packet, opcode.length, filename.length);
        packet[opcode.length + filename.length] = 0;
        System.arraycopy(mode, 0, packet, opcode.length+filename.length+1, mode.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
    // this builds the data packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] BuildPacket3(){
        byte[] packet = new byte [4 + blocknumber.length + data.length];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(blocknumber, 0, packet, opcode.length, blocknumber.length);
        System.arraycopy(data, 0, packet, opcode.length+blocknumber.length, data.length);
        return packet; 

    }
    // this builds the acknowledgement packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] BuildPacket4(){
        byte[] packet = new byte [4];
        System.arraycopy(opcode, 0, packet, 0, opcode.length);
        System.arraycopy(blocknumber, 0, packet, opcode.length, 2);
        return packet; 
    }
    // this builds the error packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] BuildPacket5(){
        byte[] packet = new byte [opcode.length+errorcode.length+errmsg.length];
        System.arraycopy(opcode, 0, packet, 0, 2);
        System.arraycopy(errorcode, 0, packet, opcode.length, 2);
        System.arraycopy(errmsg, 0, packet, opcode.length, errmsg.length);
        packet[packet.length-1] = 0;
        return packet; 
    }
    //haven't decided yet if I actually need this.
    public void setOpcode(byte[] opcode){
        this.opcode = opcode;
    }
    //returns the opcode to determine the type of packet
    public byte[] getOpcode(){
        return opcode;
    }
    //haven't decided yet if I actually need this.
    public void setFileName(String filename){
        this.filename = filename.getBytes();
    }
    //returns the filename from read and write packets
    public byte[] getFilename(){
        return filename;
    }
    //haven't decided yet if I actually need this.
    public void setMode(String mode){
        this.mode = mode.getBytes();
    }
    //extracts the mode type (netascii or octet) from a data packet
    public byte[] getMode(){
        return mode;
    }
    //haven't decided yet if I actually need this.
    public void setblockNumber(byte[] blocknumber){
        this.blocknumber[0] = blocknumber[0];
        this.blocknumber[1] = blocknumber[1];
    }
    //extracts the block number from a data packet to put into the acknowledgement
    public byte[] getblockNumber(){
        return blocknumber;
    }
    //haven't decided yet if I actually need this.
    public void setData(byte[] data){
        this.data = data;
    }
    //extracts the data from a data packet
    public byte[] getData(){
        return data;
    }
}


