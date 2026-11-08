class Shape1{
    public void draw(){
        System.out.println("Drawing Shape");
    }
    public void erase(){
        System.out.println("Erasing Shape");
    }
}

class Circle1 extends Shape1{
    @Override 
    public void draw(){
        System.out.println("Drawing Circle:");
    }
    public void erase(){
        System.out.println("Erasing Circle:");
    }
}

class Triangle1 extends Shape1{
    @Override 
    public void draw(){
        System.out.println("Drawing Triangle:");
    }
    public void erase(){
        System.out.println("Erasing Triangle:");
    }
}

class Square1 extends Shape1{
    @Override 
    public void draw(){
        System.out.println("Drawing Square:");
    }
    public void erase(){
        System.out.println("Erasing Square:");
    }
}

public class ShapeDemo{
    public static void main(String[] args) {
        Shape1[] s = new Shape1[3];
        s[0] = new Circle1();
        s[1] = new Triangle1();
        s[2] = new Square1();
        for(Shape1 shape : s){
            shape.draw();
            shape.erase();
            System.out.println();
        }
    }
}