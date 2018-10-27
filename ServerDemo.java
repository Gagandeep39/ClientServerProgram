
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.Serializable;

public class ServerDemo {
    private static final long serialVersionUID = 1L;
    static ArrayList<Student> al = new ArrayList<Student>();

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5000);
            System.out.println("Server waiting for client....");
            Socket cs = ss.accept();

            DataOutputStream dout = new DataOutputStream(cs.getOutputStream());
            DataInputStream din = new DataInputStream(cs.getInputStream());

            ObjectOutputStream oout = new ObjectOutputStream(
                    cs.getOutputStream());
            ObjectInputStream oin = new ObjectInputStream(cs.getInputStream());

            Student st = null;
            int rollNo;
            Operations obj = new Operations();

            while (true) {

                int ch = din.readInt();
                if (ch == 1) {
                    // Reading com.server.Student Object from client

                    st = (Student) oin.readObject();

                    // Calling addStudent() of com.server.Operations class
                    obj.addStudent(st);
                    dout.writeUTF(al.toString());
                } else if (ch == 2) {
                    // Reading rollNo from client for deleting
                    rollNo = din.readInt();

                    // Calling addStudent() of com.server.Operations class
                    obj.deleteStudent(rollNo);
                    dout.writeUTF(al.toString());
                } else if (ch == 3) {
                    // Reading rollNo from client for deleting
                    rollNo = din.readInt();

                    // Calling addStudent() of com.server.Operations class
                    st = obj.searchStudent(rollNo);
                    if (st != null) {
                        // Writing searched Student object back to client
                        oout.writeObject(st);
                    } else {
                        // Writing ArrayList al object back to client
                        oout.writeObject(null);
                    }
                } else if (ch == 4) {
                    // Reading rollNo from client for deleting
                    double pm = din.readDouble();

                    // Calling addStudent() of com.server.Operations class
                    ArrayList<Student> failed = obj.failedStudents(pm);
                    dout.writeUTF(failed.toString());
                } else if (ch == 5) {
                    dout.writeUTF("Bye Bye Client from server!!! ");
                    System.out.println("Socket Closed !!!s");
                    cs.close();
                    break;
                }
            }
            ss.close();

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
class Operations {
    boolean addStudent(Student st) {
        boolean status = false;
        status = ServerDemo.al.add(st);
        return status;
    }

    boolean deleteStudent(int rollNo) {
        boolean status = false;

        for(Student s : ServerDemo.al){
            if(s.rollNo == rollNo){
                int index = ServerDemo.al.indexOf(s);
                ServerDemo.al.remove(index);
                status = true;
                break;
            }
        }
        return status;
    }

    Student searchStudent(int rollNo){
        Student st = null;

        for(Student s : ServerDemo.al){
            if(s.rollNo == rollNo){
                st = s;
                break;
            }
        }

        return st;
    }

    ArrayList<Student> failedStudents(double pm){
        ArrayList<Student> failed = new ArrayList<Student>();

        for(Student s : ServerDemo.al){
            if(s.marks < pm){
                failed.add(s);
            }
        }

        return failed;
    }

}
