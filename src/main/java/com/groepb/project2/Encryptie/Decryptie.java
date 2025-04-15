package com.groepb.project2.Encryptie;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.GetFromDB;
import com.groepb.project2.data.Environment;
import org.mindrot.jbcrypt.BCrypt;

public class Decryptie {
    public boolean decrypt(String user, String password){
        try{
            String hashedPassword = new GetFromDB(new ConnectDB(new Environment())).getPassword(user);
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
