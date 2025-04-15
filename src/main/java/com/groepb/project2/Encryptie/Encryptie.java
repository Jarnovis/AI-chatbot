package com.groepb.project2.Encryptie;

import org.mindrot.jbcrypt.BCrypt;

public class Encryptie {
    public String encrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
