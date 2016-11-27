import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;

public class Json_Reader
{
    private Shape shapes[];

    public Json_Reader()
    {
        try {
            JsonReader reader = new JsonReader(new FileReader("D:/cg/lab1_task2_v1/image.json"));
            shapes = new Gson().fromJson(reader, Shape[].class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Shape[] getShapes(){
        return shapes;
    }
}