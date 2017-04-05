package locale;
import java.util.ListResourceBundle;

/**
 * Created by alterG on 25.01.2017.
 */
public class Data_eng extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    public  Object[][] contents = {
            {"chooseX","Choose X value:"},
            {"chooseY","Choose Y value:"},
            {"chooseR","Choose R value:"},
            {"addDot","Add new dot"},
            {"removeDot","Remove all dots"}
    };
}
