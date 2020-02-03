package com.project.dreamsquad.trackmykid.others;

public class Validation_Signup {

    public int validateDataSignup(String parentName, String userName, String password, String confirmPassword, String mobile){
        if(parentName_isValid(parentName)){
            if(username_isvalid(userName)){
                if(passwords_areMatching(password,confirmPassword)){
                    if(password_isvalid(password)){
                        if(phoneNumber_isvalid(mobile)){
                            return 0;
                        }else
                            return 105;
                    }else
                        return 104;
                }else
                    return 103;
            }else
                return 102;
        } else
            return 101;
    }

    public int validateDataChangePassword(String password, String confirmPassword){
        if(passwords_areMatching(password,confirmPassword)){
            if(password_isvalid(password)){
                return 0;
            }else
                return 104;
        }else
            return 103;
    }

    public int validateEditProfile(String parentName, String mobile, String kidName){
        if(parentName_isValid(parentName)){
            if(phoneNumber_isvalid(mobile)){
                if(parentName_isValid(kidName)){
                    return 0;
                }return 106;
            }else
                return 105;
        }else
            return 101;
    }

    private boolean parentName_isValid(String parentName){
        int length=parentName.length();
        for(int i=0;i<length;i++)
        {
            char c = parentName.charAt(i);
            if(!((c>='a' && c<='z')||(c>='A' && c<='Z')) && c!=' ')
            {
                return false;
            }

        }
        return  true;
    }

    private boolean username_isvalid(String username) {
        int length = username.length();
        boolean valid = username.matches("[A-Za-z0-9_]+");
        return(length > 5 && valid == true);
    }

    private boolean password_isvalid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";
        return (password.matches(pattern));
    }

    private boolean passwords_areMatching(String password, String confirmPassword){
        return(password.equals(confirmPassword));
    }

    private boolean phoneNumber_isvalid(String mobile) {
        String pattern = "^[0-9]{10}$";
        return (mobile.matches(pattern));
    }
}
