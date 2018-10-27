
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.io.Serializable;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket cs = new Socket("localhost", 5000);
            DataOutputStream dout = new DataOutputStream(cs.getOutputStream());
            DataInputStream din = new DataInputStream(cs.getInputStream());

            ObjectOutputStream oout = new ObjectOutputStream(
                    cs.getOutputStream());
            ObjectInputStream oin = new ObjectInputStream(cs.getInputStream());
            int ch;
            String name;
            int rollNo;
            double marks;

            // Student class of Client
            Student st = null;
            while (true) {
                System.out.print("\n***** Select Operation *****\n");
                System.out
                        .print("\n\t1. Add Student\n\t2. Delete Student\n\t3. Search Student\n\t4. Failed Student\n\t5. Exit");
                System.out.print("\nEnter choice: ");
                ch = sc.nextInt();
                dout.writeInt(ch);
                switch (ch) {
                    case 1:
                        System.out.print("\nEnter Roll No - ");
                        rollNo = sc.nextInt();
                        System.out.print("\nEnter Name - ");
                        name = sc.next();
                        System.out.print("\nEnter Marks - ");
                        marks = sc.nextDouble();
                        st = new Student(rollNo, name, marks);
                        oout.writeObject(st);

                        System.out.print("\nAdded Succesfully " + rollNo
                                + "\nCurrent Students ->" + din.readUTF());
                        break;
                    case 2:
                        System.out.print("\nEnter rollNo to be deleted : ");
                        rollNo = sc.nextInt();
                        dout.writeInt(rollNo);
                        System.out.print("\nDeleted Succesfully " + rollNo
                                + "\nCurrent Students List" + din.readUTF());
                        break;
                    case 3:
                        System.out.print("\nEnter rollNo to be searched : ");
                        rollNo = sc.nextInt();
                        dout.writeInt(rollNo);
                        Student s = (Student) oin.readObject();
                        if (s != null) {
                            System.out.print("\nSearched Student -> " + s);
                        } else {
                            System.out.println("User not found !!!");
                        }

                        break;
                    case 4:
                        System.out.print("\nEnter passing marks : ");
                        double pm = sc.nextDouble();
                        dout.writeDouble(pm);
                        System.out.print("\nFailed List !!!\n" + din.readUTF());
                        break;
                }
                if (ch == 5) {
                    System.out.println(din.readUTF());
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
class Student implements Serializable {

    public int rollNo;
    public String name;
    public double marks;

    public Student(int rollNo, String name, double marks){
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Student [rollNo=" + rollNo + ", name=" + name + ", marks="
                + marks + "]";
    }


}
