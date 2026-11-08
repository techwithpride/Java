public class charDemo {
    public static void main(String args[]) {
        boolean b=false;
        System.out.println("b contains "+b);
        b=true;
        System.out.println("b contains "+b);
        if(b)
            System.out.println("This is executed.");

        b=false;
        if(b)
            System.out.println("Will not executed.");

        System.out.println("10>9 is "+(10>9));

    }
}
