package org.group_b.Database;

public interface ConnectToDatabase {

    void ConnectSynchronous(String message, String type);

    // TODO: Future Development, Temporary comment out.
//    void ConnectAsynchronous(String message, String type);

    void EstablishConnection(String command, String method, String request_type);

}
