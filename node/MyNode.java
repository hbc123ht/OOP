package node;

public class MyNode {
    //static int edgeCount = 0;   // This works with the inner MyEdge class
    String id;
    public MyNode(String id) {
        this.id = id;
    }
    public String toString() {    
        return "V"+id;  
    } 
    public String Node_Property(){
        String node_prop = id;
        return(node_prop);
    }
}

