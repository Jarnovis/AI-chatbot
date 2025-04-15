package com.groepb.project2.Windows;

import com.groepb.project2.data.Conversation;

public interface IResponse {
    void update(String message);
    void fullResponse(String fullResponse, Conversation conversation);
}
