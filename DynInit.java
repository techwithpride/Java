/*
public class DynInit {
    public static void main(String args[]) {
        double a=3.0, b=4.0;
        double c=Math.sqrt(a*a+b*b);
        System.out.println("Hypotenuse is "+c);
    }

}
*/

/* 
public class DynInit {
    public static void main(String args[]){
        double radius=4, height=5;
        double volume=3.1416 *radius*radius*height;
        System.out.println("Volume is "+volume);
    }
}
        
*/

/* 

public class DynInit {
    public static void main(String args[]){
        int x=10;
        if(x==10){
            int y=20;
            System.out.println("x and y: "+x+" "+y);
            x=y*2;
        }
        System.out.println("x: "+x);
    }
}

*/

/* 
public class DynInit {
    public static void main(String args[]){
        int x;
        for(x=0;x<3;x++){
            int y=-1;
            System.out.println("y is: "+y);
            y=100;
            System.out.println("y is now: "+y);
        }
    }
}

*/

public class DynInit{
    public static void main(String args[]){
        byte b;
        int i=257;
        double d=323.142;
        System.out.println("\nConversion of int to byte.");
        b=(byte)i;
        System.out.println("i and b: "+i+" "+b);   
        i=(int)d;
        System.out.println("d and i: "+d+" "+i);
        System.out.println("\nConversion of double to int.");
        b=(byte)d;
        System.out.println("d and b: "+d+" "+b);
    }
}
