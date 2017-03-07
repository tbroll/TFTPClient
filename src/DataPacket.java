import java.lang.*;
public class DataPacket {

    private final String opcode = "3";
    private byte[] blocknumber;
    private byte [] data;

    // This creates a Data Packet

    public DataPacket(byte[] blocknumber, byte[] data){
        this.blocknumber = blocknumber;
        this.data = data;
    }

    // this method builds the data packet to be put into a DatagramPacket and
    // sent to or received from the server

    public byte[] createPacket(){

        byte[] packet = new byte [4 + blocknumber.length + data.length];

        byte[]oc = opcode.getBytes();

        System.arraycopy(oc, 0, packet, 0, oc.length);
        System.arraycopy(blocknumber, 0, packet, oc.length, blocknumber.length);
        System.arraycopy(data, 0, packet, oc.length+blocknumber.length, data.length);

        return packet; 
    }
}

