package quickquiz;

/**
 * @author Kun
 * This class is the normal Doubly-Linked List Manipulation class
 * While Toshimi Minoura's method is for Circular doubly linked list
 * Method include prepend, append, insert, remove, print
 */
public class DLL 
{
    Node head;
    
    class Node
    {
        Node prev;
        Node next;
        DLLData data;
        
        Node(String s1, String s2)
        {
            data = new DLLData(s1, s2);
        }
    }
    
    public void prepend(String s1, String s2)
    {
        Node newNode = new Node(s1, s2);
        newNode.next = head;
        
        if (head != null) 
        {
            head.prev = newNode;
        }
        head = newNode;
    }
     
    public void append(String s1, String s2)
    {
        Node newNode = new Node(s1, s2);
        //new_node.next = null;
        
        if (head == null)
        {
            head = newNode;
        } 
        else 
        {
            Node current = head;
            while (current.next != null)
            {
                current = current.next;
            }
            current.next = newNode;
            newNode.prev = current;
        }
    }
    
    public void insertBefore(Node iB_node, String s1, String s2)
    {
        if (iB_node == null)
        {
            System.out.println("Node cannot be NULL");
        }
       
        Node newNode = new Node(s1, s2);
        newNode.next = iB_node;
        newNode.prev = iB_node.prev;
        
        if (iB_node.prev == null)
        {
            head = newNode;
        }
        else
        {
             iB_node.prev.next = newNode;
        }
       iB_node.prev = newNode;
    }
    
    // the first attribute of data supplied
    public void remove(String s1, String s2)
    {
        Node current = head;
        
        if (head.data.question.equals(s1) && head.data.number.equals(s2))
        {
            head.next.prev = null;
            head = head.next;
            return;
        }
        
        while(!current.data.question.equals(s1) || !current.data.number.equals(s2))
        {
            current =  current.next;
            if (current == null)
            {
                System.out.println("No record found");
                return;
            }
        }
            
        if (current.next == null)
        {
            current.prev.next = null;
        }
        else
        {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
    }
    
    // Searching method
    public void search(String s1)
    {
        Node current = head;
        
        if (current == null)
        {
            System.out.println("Empty DLL");
            return;
        }
        
        while( current.data.question.compareToIgnoreCase(s1) != 0 && current.next != null)
        {
            current = current.next;
        }
        
        if(current.next == null  && current.data.question.compareToIgnoreCase(s1) != 0)
        {
            System.out.println("Data not found");
        }
        else
        {
            System.out.println("Data found");
        }

    }
    
    public String printList()
    {
        String str = "HEAD <-->";
        Node current = head;
        while (current != null) 
        {
            str = str + " " + current.data.question + " " + current.data.number + " student(s)" + "<-->";
            current = current.next;
        }
        str = str + " TAIL";
        return str;
    }
    
    // Test this class
//    public static void main(String[] args)
//    {        
//        DLL dll = new DLL();
//        dll.search("lll");
//        dll.append("sdf","sdfss");
//        dll.append("ggh", "wers");
//        dll.insertBefore(dll.head.next, "LL","DD");        
//        System.out.println(dll.printList());
//        dll.remove("LL", "DD");
//        dll.prepend("hello", "World");
//        System.out.println(dll.printList());
//        System.out.println(dll.head.data.question + dll.head.next.data.question + dll.head.next.next.data.question);
//        dll.search("lll");
//    }
}
