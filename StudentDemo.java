import java.util.Scanner;

class Student{
    String USN;
    String name;
    String branch;
    String phone;
//Using constructor to initialize student details 

    Student(String USN, String name, String branch, String phone){
        this.USN = USN;
        this.name = name;
        this.branch = branch;
        this.phone = phone;
    }
// Method to display student details
    void display(){
        System.out.println("USN: " + USN);
        System.out.println("Name: " + name);
        System.out.println("Branch: " + branch);
        System.out.println("Phone: " + phone);
    }
}

public class StudentDemo{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int n = sc.nextInt();
        sc.nextLine(); // Consume newline

        Student[] students = new Student[n];

        for(int i = 0; i < n; i++){
            System.out.println("Enter details for student " + (i + 1) + ":");
            System.out.print("USN: ");
            String USN = sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Branch: ");
            String branch = sc.nextLine();
            System.out.print("Phone: ");
            String phone = sc.nextLine();

            students[i] = new Student(USN, name, branch, phone);
        }

        System.out.println("\nStudent Details:");
        System.out.println("----------------");
        for(Student student : students){
            student.display();
            System.out.println();
        }

        sc.close();
    }
}