import org.joml.Vector3f;

public class Main{

    public static void main(String[] args) {
        //Need to ensure safety with objects using vectors
        //Create new copies when transferred

        try{
            new Engine().start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}