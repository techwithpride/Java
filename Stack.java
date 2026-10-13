public class Stack{
    private int[]array;
    private int top;
    private static final int MAX_SIZE=10;

    public Stack(){
        array=new int[MAX_SIZE];
        top=-1;
    }

    public boolean isEmpty(){
        return top==-1;
    }

    public boolean isFull(){
        return top==MAX_SIZE-1;
    }

    public void push(int value){
        if(isFull()){
            System.out.println("Stack is full. Cannot push " + value);
            return;
        }
        array[++top]=value;
        System.out.println("Pushed " + value);
    }

    public int pop(){
        if(isEmpty()){
            System.out.println("Stack is empty. Cannot pop.");
            return -1;
        }
        int poppedValue=array[top--];
        System.out.println("Popped " + poppedValue);
        return poppedValue;
    }

    public int peek(){
        if(isEmpty()){
            System.out.println("Stack is empty. Cannot peek.");
            return -1;
        }
        return array[top];
    }

    public void display(){
        if(isEmpty()){
            System.out.println("Stack is empty.");
            return;
        }
        System.out.print("Stack: ");
        for(int i=0;i<=top;i++){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    public static void main(String[] args){
        Stack stack=new Stack();
        stack.push(5);
        stack.push(10);
        stack.push(15);
        stack.display();
        stack.pop();
        stack.display();
        System.out.println("Top element : " + stack.peek());
    }
}