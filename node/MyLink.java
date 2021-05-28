package node;

public class MyLink {

    String Label;
    int id;
    static int edgeCount = 0;
    static int edgeCount_Directed = 0;

    public MyLink(){
        this.id = edgeCount_Directed++;
    } 

    public String toString() {
        return "E"+id;
    }
    public String Link_Property(){
        String Link_prop = Label;
        return(Link_prop);
    }
}