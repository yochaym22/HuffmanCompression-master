
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.HashMap;

class MinHeap {
    private static final int NOT_FOUND = -1;
    /**
     * attributes
     */
    private ArrayListObject[] heap;
    private int lastIndex;
    private final int capacity;
    private final String basePath;

    /**
     * @param capacity the size of the heap
     */
    public MinHeap(int capacity,String basePath) {
        this.capacity = capacity + 1;
        this.lastIndex = 1;
        this.basePath=basePath;
        heap = new ArrayListObject[this.capacity + 1];

    }

    /**
     * find parent node index
     *
     * @param index current position
     * @return parent index
     */
    private int parent(int index) {
        if (index / 2 < 1)
            return 1;
        return index / 2;
    }

    /**
     * @param pos position
     * @return left child index
     */
    private int leftChild(int pos) {
        if (2 * pos >= lastIndex)
            return NOT_FOUND;
        else
            return (2 * pos);
    }

    /**
     * @param pos position
     * @return right child index
     */
    private int rightChild(int pos) {
        if ((2 * pos) + 1 >= lastIndex)
            return NOT_FOUND;
        return (2 * pos) + 1;
    }

    /**
     * Check if a given index has any children or meaning he is a leaf
     *
     * @param position given index
     * @return boolean value
     */
    private boolean isLeaf(int position) {
        return position > ((lastIndex - 1) / 2);
    }

    /**
     * swap function
     *
     * @param firstNode  first
     * @param secondNode second
     */
    private void swap(int firstNode, int secondNode) {

        ArrayListObject tmp;
        tmp = heap[firstNode];
        heap[firstNode] = heap[secondNode];
        heap[secondNode] = tmp;
    }

    /**
     * Find the smallest child of a current index
     *
     * @param index given index
     * @return return the index of the smallest child
     */
    private int smallesChild(int index) {

        //validate child index (meaning they are not -1)
        int leftChild = leftChild(index);
        int rightChild = rightChild(index);

        //First case,node has two children
        if (leftChild != NOT_FOUND && rightChild != NOT_FOUND) {
            //if both frequencies are equal return the one who is not a father
            if (heap[leftChild(index)].getFrequency() == heap[rightChild(index)].getFrequency()) {
                if (heap[leftChild(index)].isFather) {
                    return rightChild(index);
                } else {
                    return leftChild(index);
                }
            }
            //return the
            if (heap[leftChild(index)].getFrequency() < heap[rightChild(index)].getFrequency()) {
                return leftChild(index);
            } else {
                return rightChild(index);
            }
        }

        //Second case : if node has only left child.
        if (rightChild == NOT_FOUND) {

            if (heap[index].getFrequency() == heap[leftChild(index)].getFrequency()) {
                if (heap[index].isFather) {
                    return leftChild(index);
                } else {
                    return (index);
                }
            }
            if (heap[leftChild(index)].getFrequency() < heap[index].getFrequency()) {
                return leftChild(index);
            } else {
                return index;
            }
        }
        //Third case: if node has only right child.
        if (leftChild == NOT_FOUND) {

            if (heap[index].getFrequency() == heap[rightChild(index)].getFrequency()) {
                if (heap[rightChild(index)].isFather) {
                    return rightChild(index);

                } else {
                    return index;
                }
            }
            if (heap[rightChild(index)].getFrequency() < heap[index].getFrequency())
                return rightChild(index);
            else {
                return (index);
            }

        }

        return 0;

    }

    /**
     * Heapify function to sort the min heap.
     * the heapify will go from the head of the list
     * down to it's children and will swap if they are smaller
     * in a recursive matter
     *
     * @param index to start the heapify from
     */
    private void heapify(int index) {


        //If the current index is a leaf , then he doesn't have any kids.
        if (isLeaf(index)) {
            return;
        }
        //find the smallest child of the index
        int smallestChild = smallesChild(index);

        //swap the smallest child and recursive call
        if (heap[index].getFrequency() == heap[smallestChild].getFrequency()) {

            swap(index, smallestChild);
        }
        if (heap[index].getFrequency() > heap[smallestChild].getFrequency()) {
            swap(index, smallestChild);
            heapify(smallestChild);
        }

    }


    /**
     * Insert a node into the heap
     *
     * @param node to add
     */
    public void insert(ArrayListObject node) {

        int current = lastIndex;

        if (lastIndex >= capacity) {
            return;
        }
        heap[lastIndex] = node;

        //bubble his way up if needed
        while (heap[current].getFrequency() < heap[parent(current)].getFrequency()) {
            swap(current, parent(current));
            current = parent(current);
        }

        lastIndex++;

    }

    /**
     * Extract 2 min values and combine into one Node
     *
     * @return
     */
    public HashMap<Byte, mapObject> generateHoffmanMap() {

        //root node of hoffman tree
        ArrayListObject tree = null;
        ArrayListObject a = null;
        while (!this.isEmpty()) {
            a = this.extractMin();
            //check end condition
            if (!this.isEmpty()) {
                ArrayListObject b = this.extractMin();
                //last inset special case
                if (this.isEmpty()) {
                    //insert backwards
                    if ((a.isFather && !b.isFather) || (!a.isFather && b.isFather)) {
                        ArrayListObject combined = new ArrayListObject(a, b, true);
                        this.insert(combined);
                    } else {
                        ArrayListObject combined = new ArrayListObject(a, b);
                        this.insert(combined);
                    }
                } else {
                    ArrayListObject combined = new ArrayListObject(a, b);
                    this.insert(combined);
                }

            }
        }
        tree = a;

        //output the tree object for decompress
        ArrayListObject treeKey = tree;

        String outputKey = basePath + "\\MyKey";
        try {
            FileOutputStream fileOutKey =
                    new FileOutputStream(outputKey);
            ObjectOutputStream out = new ObjectOutputStream(fileOutKey);
            out.writeObject(treeKey);
            out.close();
            fileOutKey.close();
        } catch (IOException i) {
            i.printStackTrace();
        }


        return buildHashMap(tree);
    }

    /**
     * Build HashMap<Byte,Coding>;
     *
     * @param tree provided Huffman Tree.
     * @return hash map
     */
    public HashMap<Byte, mapObject> buildHashMap(ArrayListObject tree) {

        HashMap<Byte, mapObject> map = new HashMap<>();
        //key - char \ byte
        //value - frequency
        BitSet path = new BitSet();
        coding(map, -1, path, tree);
        return map;
    }

    /**
     * Recursive method to travel a tree and generate Huffman coding
     *
     * @param map  hashmap to put the info in
     * @param path building path
     * @param node given tree node
     */
    public void coding(HashMap<Byte, mapObject> map, int index, BitSet path, ArrayListObject node) {
        if (!node.isFather) {
            BitSet currentCode = (BitSet) path.clone();
            mapObject temp = new mapObject(currentCode, index + 1);
            map.put(node.getValue(), temp);
            return;
        } else {
            /**recursive call for the tree paths and adding to the byte value
             false by going left
             true by going right
             */
            coding(map, ++index, incBit(path, false, index), node.left);
            index--;
            coding(map, ++index, incBit(path, true, index), node.right);
        }
    }


    /**
     * Extract the min value from the heap ( which is that first index)
     *
     * @return the first index of the heap.
     */
    public ArrayListObject extractMin() {
        //first swap last and first index
        swap(1, --lastIndex);
        //save the last index
        ArrayListObject popped = heap[lastIndex];
        //delete from the heap
        heap[lastIndex] = null;
        //check that the heap is sorted
        heapify(1);
        return popped;
    }

    public boolean isEmpty() {
        return lastIndex == 1;
    }

    public BitSet incBit(BitSet set, boolean bool, int index) {
        set.set(index, bool);
        return set;
    }
}


