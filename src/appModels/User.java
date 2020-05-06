package appModels;

public class User {
    
    private static String current_Username;
    private static String current_User_ID;
    private static boolean is_Active;

    public User(String curr_username, String curr_userid, boolean bool_Active) {
        User.current_Username = curr_username;
        User.current_User_ID = curr_userid;
        User.is_Active = bool_Active;
    }

    public static void set_Current_Username(String curr_username) {
        User.current_Username = curr_username;
    }

    public static void set_Current_User_ID(String curr_userid) {
        User.current_User_ID = curr_userid;
    }

    public static void set_Is_Active(boolean bool_Active) {
        User.is_Active = bool_Active;
    }

    public static String get_Current_Username() {
        return current_Username;
    }

    public static String get_Current_User_ID() {
        return current_User_ID;
    }   
}
    
     