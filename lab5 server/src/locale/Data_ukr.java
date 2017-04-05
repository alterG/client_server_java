package locale;

import java.util.ListResourceBundle;

/**
 * Created by alterG on 25.01.2017.
 */
public class Data_ukr extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }
    public  Object[][] contents = {
            {"chooseX","Виберіть значення X:"},
            {"chooseY","Виберіть значення Y:"},
            {"chooseR","Виберіть значення R:"},
            {"addDot","Нова точка"},
            {"removeDot","Прибрати точку"}
    };
}
