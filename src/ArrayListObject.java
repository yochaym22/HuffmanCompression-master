import java.io.Serializable;

public class ArrayListObject implements Serializable {
    private static final byte UNSIGNED_BYTE = 127;
    private Byte value;
    private int frequency;
    ArrayListObject left;
    ArrayListObject right;

    boolean isFather;

    public ArrayListObject(Byte value) {
        this.value = value;
        this.frequency = 1;
        ArrayListObject left = null;
        ArrayListObject right = null;
        isFather = false;
    }

    public ArrayListObject(ArrayListObject one, ArrayListObject two) {

        this.frequency = one.getFrequency() + two.getFrequency();
        //right = bigger
        if (one.getFrequency() < two.getFrequency()) {
            this.right = one;
            this.left = two;
        } else {
            this.right = two;
            this.left = one;
        }
        this.value = 0;
        isFather = true;
    }
    public ArrayListObject(ArrayListObject one, ArrayListObject two,boolean specialCase) {

        this.frequency = one.getFrequency() + two.getFrequency();
        //right = bigger
        if (one.isFather) {
            this.right = one;
            this.left = two;
        } else {
            this.right = two;
            this.left = one;
        }
        isFather = true;
    }


    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incAmount() {
        this.frequency++;
    }

    @Override
    public String toString() {
        return "arrayListObject {" +
                "value=" + value +
                ", amount=" + frequency +
                '}';
    }

    public ArrayListObject getLeft() {
        return left;
    }

    public void setLeft(ArrayListObject left) {
        this.left = left;
    }

    public ArrayListObject getRight() {
        return right;
    }

    public void setRight(ArrayListObject right) {
        this.right = right;
    }

    public boolean isFather() {
        return isFather;
    }

}
