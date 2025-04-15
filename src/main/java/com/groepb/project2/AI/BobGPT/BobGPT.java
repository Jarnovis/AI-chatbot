package com.groepb.project2.AI.BobGPT;

import com.groepb.project2.AI.IAI;
import com.groepb.project2.data.Conversation;

public class BobGPT implements IAI {

    private FileOperations fileOperations;

    public BobGPT() {
        this.fileOperations = new FileOperations();
    }

    @Override
    public String getResponse(String question, String language, Conversation conversation) {
        return ConversationHandler.getResponse(question, language, conversation);
    }
}
