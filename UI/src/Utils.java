import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Utils {

    private static int incrementingID = 0;

    private Utils(){
        //Util Class
    }

    public static String getUniqueID(){
        incrementingID++;
        return "IncID" + incrementingID;
    }

    public static String readFileToString(String filePath){
        String output;
        try{
            output = new String(Files.readAllBytes(Path.of(filePath)));
        }
        catch (Exception e){
            throw new RuntimeException("Error reading file " + filePath);
        }
        return output;
    }

    public static int arrayWrapAround (int index, int arrayLength){
        //Returns an index that is inbound of the array
        //If the index goes over the length of the array it will wrap back to the start
        //If the index goes under the start of the array it will wrap back to the end
        return arrayWrapAround(index,0,arrayLength);
    }
    public static int arrayWrapAround(int index, int minIndex, int maxIndex, boolean includeMaxIndexInRange){
        //Returns an index that is inside the bounds of the selected indices
        //Option to include the maxIndex in the range of valid indices
        if (includeMaxIndexInRange){
            return arrayWrapAround(index,minIndex,maxIndex+1);
        }
        return arrayWrapAround(index,minIndex,maxIndex);
    }
    public static int arrayWrapAround(int index, int minIndex, int maxIndex){
        //Returns an index that is inside the bounds of the selected indices
        //If the index is equal or goes over the max index of the array it will wrap back to the min index
        //If the index goes under the min index of the array it will wrap back to the max index
        //NOTE: the maxIndex is not part of the range of valid indices (i.e. [minIndex, maxIndex) )

        if (minIndex > maxIndex) return 0; //Prevent inf while loops that happens when minIndex is greater then maxIndex
        int indexDiff = maxIndex - minIndex;
        while (index >= maxIndex){
            index -= indexDiff;
        }
        while (index < minIndex){
            index += indexDiff;
        }
        return index;
    }


    public static String vect3fToString(Vector3f vec){
        return String.format("(%.1f,%.1f,%.1f)",vec.x,vec.y,vec.z);
    }

    public static Vector4f randomColor(float minCompValue, float maxCompValue){
        // returns a random RGBA color where the RGB components are between the min and max value
        float value = maxCompValue - minCompValue;
        return new Vector4f((float) ((Math.random()*value)+minCompValue), (float) ((Math.random()*value)+minCompValue), (float) ((Math.random()*value)+minCompValue), 1f);
    }
}
