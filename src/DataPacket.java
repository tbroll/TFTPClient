import java.lang.*;
public class DataPacket {
    private byte opcode;
    private byte[] blocknumber;
    private byte [] data;
// This creates a Data Packet
    public DataPacket(byte[] blocknumber, byte[] data){
        this.opcode = 3;
        this.blocknumber = blocknumber;
        this.data = data;
    }
    // this method builds the data packet to be put into a DatagramPacket and sent to or received from the server
    public byte[] build(){
        byte[] packet = new byte [4 + blocknumber.length + data.length];
        byte[]optcode = {0,3};
        System.arraycopy(optcode, 0, packet, 0, optcode.length);
        System.arraycopy(blocknumber, 0, packet, optcode.length, blocknumber.length);
        System.arraycopy(data, 0, packet, optcode.length+blocknumber.length, data.length);
        return packet; 
    }
}
