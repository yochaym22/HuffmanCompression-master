
import java.io.Serializable;
import java.util.BitSet;

/**
 *
 */
public class mapObject implements Serializable {
    private BitSet hoffmanCode;
    private int length;

    public BitSet getHoffmanCode() {
        return hoffmanCode;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


    public mapObject(BitSet hoffmanCode, int length) {
        this.hoffmanCode = hoffmanCode;
        this.length = length;
    }
}
