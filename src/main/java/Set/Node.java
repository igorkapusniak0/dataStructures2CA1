package Set;

public class Node<T> {
    public Node<?> parent=null;
    public T data;
    public Node<?> getParent(){
        return parent;
    }
    public void setParent(Node<?> parent){
        this.parent = parent;
    }
    public T getData(){
        return data;
    }
    public void setData(T data){
        this.data = data;
    }
}
