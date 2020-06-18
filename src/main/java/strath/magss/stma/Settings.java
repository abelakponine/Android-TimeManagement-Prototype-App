package strath.magss.stma;
/* Settings Class */
public class Settings {
    private static boolean notification = true; // notification settings
    /* Get notification status method */
    public static boolean getNotificationStatus(){
        return notification;
    }
    /* Set notification status method */
    public static void setNotification(boolean value){
        notification = value;
    }
}
