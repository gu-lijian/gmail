package com.poc.gmail.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;

@Component
public class GmailRegistrationAction {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
//    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailRegistrationAction.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void getGmailAccess(String gmailAccount) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the labels in the user's account.
        String user = gmailAccount;
        // TODO release below for checking labels
//        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
//        List<Label> labels = listResponse.getLabels();
//        if (labels.isEmpty()) {
//            System.out.println("No labels found.");
//        } else {
//            System.out.println("Labels:");
//            for (Label label : labels) {
//                System.out.printf("- %s\n", label.getName());
//            }
//        }
        // TODO release below for checking threads and message
        String query = "from:Grab OR subject:Grab OR from:Lazada OR subject:Lazada";
        ListThreadsResponse listThreadsResponse = service.users().threads().list(user).setQ(query).execute();
//        List<com.google.api.services.gmail.model.Thread> threads = listThreadsResponse.getThreads();
        List<com.google.api.services.gmail.model.Thread> threads = new ArrayList<com.google.api.services.gmail.model.Thread>();
        while(listThreadsResponse.getThreads() != null) {
            threads.addAll(listThreadsResponse.getThreads());
            if(listThreadsResponse.getNextPageToken() != null) {
              String pageToken = listThreadsResponse.getNextPageToken();
              listThreadsResponse = service.users().threads().list(user).setQ(query).setPageToken(pageToken).execute();
            } else {
              break;
            }
        }
        System.out.println("Start to print threads info");
        for(com.google.api.services.gmail.model.Thread thread : threads) {
        	System.out.println(thread.toPrettyString());
//        	System.out.printf("- %s\n", thread.getSnippet());
//        	System.out.printf("- %s\n", thread.getMessages());
          }
        System.out.println("Total related e-commence threads count is: " + threads.size());
        System.out.println("Completed printing threads info");
        System.out.println();

        ListMessagesResponse response = service.users().messages().list(user).setQ(query).execute();
            List<Message> messages = new ArrayList<Message>();
            while (response.getMessages() != null) {
              messages.addAll(response.getMessages());
              if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(user).setPageToken(pageToken).execute();
              } else {
                break;
              }
            }
            
            System.out.println("Start to print messages info");
            for (Message message : messages) {
//              System.out.println(message.toPrettyString());
            	System.out.println(message.getId());
            }
            System.out.println("Total related e-commence messages count is: " + messages.size());
            System.out.println("Completed printing threads info");
            
//        if (threads.isEmpty()) {
//            System.out.println("No threads found.");
//        } else {
//            System.out.println("Threads:");
//            for (com.google.api.services.gmail.model.Thread thread : threads) {
//            	if(thread..containsValue(GRAB) || thread.containsValue(LAZADA) || thread.containsValue(AMAZON)) {
//            		System.out.printf("- %s\n", thread.getSnippet());
//            	}
//            }
//        }
        
    }
}
