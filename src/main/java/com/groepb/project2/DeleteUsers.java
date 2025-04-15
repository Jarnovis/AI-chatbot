package com.groepb.project2;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.DeleteConversations;
import com.groepb.project2.Database.DeleteUser;
import com.groepb.project2.data.Environment;

public class DeleteUsers {
    public static void main(String[] args) {
        new DeleteConversations(new ConnectDB(new Environment())).deleteAllConversations();
        new DeleteUser(new ConnectDB(new Environment())).deleteAllUsers();
    }
}
