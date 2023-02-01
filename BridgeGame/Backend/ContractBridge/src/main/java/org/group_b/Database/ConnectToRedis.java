package org.group_b.Database;
import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class ConnectToRedis implements ConnectToDatabase{

    public final StringBuilder responseHandler = new StringBuilder();
    private final String password = "A46779EE024D4";
    private final String salt = "salt";
    EncodeMD5 encode = new EncodeMD5(salt+password);
    private final String hash = encode.hashedText;

    /*
     * Specifically Constructor for "#Issue 19: Connect Backend To Redis" only.
     * NOTE, Create and initialize the constructor to enable testing:
     *     Example:  ConnectToRedis newConnect = new ConnectToRedis();
     */
    public ConnectToRedis() throws NoSuchAlgorithmException {

        String command = GetFromInput();
        // Only process when both Key and Value are legal:
        if(!command.equals(""))
            ConnectSynchronous(String.format(command), "POST");
    }

    @Override
    public void EstablishConnection(String command, String request_type, String method){

        switch(method) {
            case "Sync": {
                // Synchronous request:
                System.out.println("Synchronous Connection..");
                ConnectSynchronous(command, request_type);
            }
            case "Async": {
                // Asynchronous request:
                System.out.println("Asynchronous Connection..");
                // TODO: Future Development
//                ConnectAsynchronous(command, request_type);
            }
        }
    }

    public HttpURLConnection InitializeRequest(String message, String request_type) throws URISyntaxException {

        final String server_name = "https://agile.bu.edu/ec500_scripts/redis.php?";

        try {
            // Append by using http.uri
            URI getUri = new URI(server_name);
            // Append two query parameter into the request:
            getUri = appendQuery(getUri, "salt", salt);
            getUri = appendQuery(getUri, "hash", hash);
            getUri = appendQuery(getUri, "message", message);

            // Build the default url from the URI:
            URL url = new URL(getUri.toString());
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            // Setting both the connection and read timeouts for 5 seconds.
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(5000);
            httpCon.setInstanceFollowRedirects(false);

           switch (request_type) {
                case "GET": {
                    httpCon.setRequestMethod("GET");
                }
                case "POST": {
                    httpCon.setRequestMethod("POST");
                }
                default: {
                    httpCon.setRequestMethod("POST");
                }
            }
            return httpCon;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void ConnectSynchronous(String message, String request_type) {

        try {
            HttpURLConnection newConnection = InitializeRequest(message, request_type);
            int status = newConnection.getResponseCode();
            System.out.printf("URL: %s\n", newConnection.getURL().toString());

            // Handle failing and successful case:
            if (status == HttpURLConnection.HTTP_OK) {
                // Using Java input stream and buffer reader to get the response:
                InputStream inputStream = newConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String response = "";
                StringBuilder content = new StringBuilder();
                while ((response = reader.readLine()) != null) {
                    content.append(response);
                }
                System.out.printf("Successfully request, response: \n%s\n", content);
                // Only read key value of successful message:
                responseHandler.append(content.substring(content.length() - 2)).append("; ");
                reader.close();
                newConnection.disconnect();

            } else {
                // Unsuccessful Read:
                if (status > 299) {
                    InputStream inputStream = newConnection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String response = "";
                    StringBuilder content = new StringBuilder();
                    while ((response = reader.readLine()) != null) {
                        content.append(response);
                    }
                    System.out.printf("Unsuccessful request: \n%s\n", content);
                    reader.close();
                }
                else {
                    System.out.printf("Something wrong, Status code: %s%n \n", status);
                }

                newConnection.disconnect();
                throw new RuntimeException(String.valueOf(status));
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String GetFromInput()
    {
        // Loop until we get a valid key and value
        BufferedReader readBuffer = new BufferedReader(new InputStreamReader(System.in));
        boolean valid_Input = false;
        StringBuilder redis_Command = new StringBuilder();
        System.out.println("Enter your Redis Command:");

        try {
            String command = readBuffer.readLine();
            boolean legal = true;
            for (int i=0; i < command.length(); i++) {
                // If contain any lowercase with the command
                if (Character.isLowerCase(command.charAt(i))) {
                    legal = false;
                    break;
                }
            }
            // Check if contains SPACE or lowercase within the command:
            if(command.contains(" ") || !legal) {
                System.out.println("Invalid command (Including \"SPACE\" or \"small case\", or illegal characters.)\n");
                throw new Exception();
            }
            else {
                boolean isFinished = false;
                redis_Command.append(command).append(" ");

                do {
                    System.out.println("\nEnter your following \"Key\" or \"Value\" or \"(Key Value) - seperated by space\" for this current command: \n" +
                            "NOTE: (Add a \"space\" and mark \" -f\" at the end of line if you are finished, or \" -c\" if you wish to continue");
                    String key_value = readBuffer.readLine();
                    if (key_value.contains("-f"))
                    {
                        System.out.println("Your request is being processed...");
                        redis_Command.append(key_value, 0, key_value.length() - 3);
                        isFinished = true;
                    }
                    else if(key_value.contains("-c"))
                    {
                        redis_Command.append(key_value, 0, key_value.length() - 3);
                    }
                    else
                    {
                        System.out.println("Sorry, please try again..");
                    }
                } while (!isFinished);
            }

        } catch (Exception e) {
            System.out.println("Sorry, Please try again.");

        }

        System.out.printf("Your redis command: %s \n", redis_Command);
        return redis_Command.toString();
    }


    public URI appendQuery(URI oldUri, String queryKey, String queryValue) throws URISyntaxException {

        String newQuery = oldUri.getQuery();

        if (newQuery == null) { // If no query parameters existed:
            newQuery = queryKey + "=" + queryValue;
        } else { // Append new query parameters into query:
            newQuery += "&" + queryKey + "=" + queryValue;
        }

        return new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
    }

}
