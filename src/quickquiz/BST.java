package quickquiz;

/**
 *
 * @Kun
 */
public class BST
{
    Node root;
    
    class Node
    {
        int key;
        String topic;
        
        Node leftChild;
        Node rightChild;
        
        Node(int key, String topic)
        {
            this.key = key;
            this.topic = topic;            
        }
        // Useful method to print both of the attributes without call them individually
        public String toString()
        {
            return " " + key + "-" + topic + ",";

	}        
    }
    
    public void add(int key, String topic)
    {
        Node newNode = new Node(key, topic);
        Node parent = root;
        if (root == null)
        {
            root = newNode;
        }
        else
        {
            while (true)
            {
                if (key < parent.key)
                {
                    if (parent.leftChild == null)
                    {
                        parent.leftChild = newNode;
                        return;
                    }
                    parent = parent.leftChild;
                }
                // add both the equal and larger values to rightChild
                else if(key > parent.key)
                {
                    if (parent.rightChild == null)
                    {
                        parent.rightChild = newNode;
                        return;
                    }
                    parent = parent.rightChild;
                }
                else
                {
                    System.out.println("Data already exists");
                    return;
                }
            }
        }       
    }
    
    public String preOrderTraverse(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += start.toString();
            s += preOrderTraverse(start.leftChild);
            s += preOrderTraverse(start.rightChild);
        }
        
        return s;
    }
    
    public String inOrderTraverse(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += inOrderTraverse(start.leftChild);
            s += start.toString();
            s += inOrderTraverse(start.rightChild);
        }
        
        return s;
    }
    
    public String postOrderTraverse(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += postOrderTraverse(start.leftChild);
            s += postOrderTraverse(start.rightChild);
            s += start.toString();
        }
        
        return s;
    }
    
     public String preOrderHash(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += Integer.toString(start.hashCode()) + "_";                    
            s += preOrderHash(start.leftChild);
            s += preOrderHash(start.rightChild);
        }
        
        return s;
    }
    
    public String inOrderHash(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += inOrderHash(start.leftChild);
            s += Integer.toString(start.hashCode()) + "_";
            s += inOrderHash(start.rightChild);
        }
        
        return s;
    }
    
    public String postOrderHash(Node start)
    {
        String s = "";
        if (start != null)
        {
            s += postOrderHash(start.leftChild);
            s += postOrderHash(start.rightChild);
            s += Integer.toString(start.hashCode()) + "_";
        }
        return s;
    }
    
    public String levelOrderTraverse(Node start)
    {
        int h = getHeight(start);
        String s = "";
        for (int i = 1; i <= h; i++)
        {
            s += printGivenLevel(start, i) +"\n";
        }
        return s;
    }
    
    // get the height of the tree, the tree with root node only has height 1.
    public int getHeight(Node start)
    {
        if (start == null) 
        {
            return 0;
        }
        else
        {
            int leftHeight = getHeight(start.leftChild);
            int rightHeight = getHeight(start.rightChild);
            
            if (leftHeight > rightHeight)
            {
                return leftHeight + 1;
            }
            else
            {
                return rightHeight + 1;
            }
        }
    }
    
    public String printGivenLevel(Node start, int level)
    {
        String s = "";
        if (start == null)
        {
            return s;
        }
        if (level == 1)
        {
            s += start + "  ";
        }
        
        else if (level > 1)
        {
            s += printGivenLevel(start.leftChild, level - 1);
            s += printGivenLevel(start.rightChild, level - 1);
        }
        
        return s;
    }
    
     // Search method
    public void search(int key)
    {
        Node current = root;
        if (root == null)
        {
            System.out.println("No Binary Tree");
            return;
        }
        
        while (current.key != key)
        {
            if (current.key > key)
            {
                current = current.leftChild;
            } 
            else
            {
                current = current.rightChild;
            }
            
            if (current == null)
            {
                System.out.println("No record found");
                return;
            }
        }
        
        System.out.println(current + " found");
    }
    
    // test BST class
//    public static void main(String[] args)
//    {
//        BST bT = new BST();       
//        //bT.search(0);
//        bT.add(18, "18er");
//        bT.add(12, "12er");
//        bT.add(9, "9er");
//        bT.add(13, "13er");
//        bT.add(25, "25er");
//        bT.add(24, "24er");
//        bT.add(11, "11er");
//        bT.add(8, "8er");
//        bT.add(14, "14er");
//        //bT.search(1);
//        //System.out.println(bT.root);
//        //bT.preOrderTraverse(bT.root);
//        //bT.inOrderTraverse(bT.root);
//        //bT.postOrderTraverse(bT.root);
//        //System.out.println(bT.preOrderTraverse(bT.root));
//        //System.out.println(bT.inOrderTraverse(bT.root));
//        System.out.println(bT.postOrderTraverse(bT.root));
//        //System.out.println(bT.totalNode);
//        System.out.println(Integer.toString(bT.getHeight(bT.root)));
//        System.out.println(bT.levelOrderTraverse(bT.root));
//    }
}
