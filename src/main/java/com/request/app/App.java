package com.request.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.request.utils.Logging;
import com.request.utils.Settings;
import com.request.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class App implements Runnable {

    private static String url = new String();
    private static final Settings SETTINGS = new Settings();
    private static final Logging log = new Logging(App.class);
    private static List<HashMap<String,String>> responses = new ArrayList<>();
    private static HashMap<Integer,Integer> summary = new HashMap<>();
    private static ExecutorService messageExecutor = Executors.newFixedThreadPool(SETTINGS.getChild_threads_max());


    public static void main(String[] args) {
        log.info(Utilities.prelogString("", Utilities.getCodelineNumber(), "Application booting up !!"));
        Scanner scan = new Scanner(System.in);
        System.out.println("Application started enter below valid urls : ");
        while (true) {
            url = scan.nextLine();
            if(url.equalsIgnoreCase("exit") | url.equalsIgnoreCase("close") | url.equalsIgnoreCase("q")){
                System.out.println("System shutting down");
                log.info(Utilities.prelogString("", Utilities.getCodelineNumber(), "Application Exiting GoodBye !!"));
                break;
            }
            App job = new App();
            messageExecutor.execute(job);

        }
        scan.close();
        printResults();
        printSummary();
        //

        
    }

    @Override
    public void run() {
        processRequest();
    }

    private synchronized void processRequest() {
        HttpResponse<String> response = this.sendRequst(url);
        HashMap<String, String> result = processResponse(response);
        responses.add(result);
        summaryProcessor(result);
    }

    public  HttpResponse<String> sendRequst(String url) {

        HttpClient client = HttpClient.newHttpClient();
        
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(SETTINGS.getConnection_timeout()))
            .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.error(Utilities.prelogString("", Utilities.getCodelineNumber(), "Processing request "));
        } catch ( IllegalArgumentException | IOException | InterruptedException e) {
            log.error(Utilities.prelogString("", Utilities.getCodelineNumber(), "Found error processing request : "+e.getStackTrace().toString()));
        }
        return response;
    }

    private  HashMap<String, String> processResponse(HttpResponse<String> response) {

        HashMap<String, String> output = new HashMap<>();

        if (response == null) {
            output.put("Error", "Invalid Url");
            output.put("Url", url);
            return output;
        }
        output.put("Status_code", "" + response.statusCode());
        output.put("Url", response.uri().toString());
        output.put("Date", response.headers().map().get("date").get(0));
        output.put("Content_length", "" + response.body().length());

        return output;
    }

    private  void summaryProcessor(HashMap<String,String> response){
        if(!response.containsKey("Status_code")){
            return;
        }

        int status = Integer.parseInt(response.get("Status_code"));
        if(summary.containsKey(status)){
            summary.put(status, summary.get(status)+1) ;
        }else{
            summary.put(status, 1);
        }

    }

    private static void printResults(){
        for(HashMap<String,String> response : responses){
            log.info(Utilities.prelogString(response.toString(), Utilities.getCodelineNumber(), "Responses printing to console "));
            System.out.println( new JSONObject(response));
        }
    }

    private static void printSummary(){
        JSONArray array = new JSONArray();

        for(Integer key : summary.keySet()){
            JSONObject obj = new JSONObject();

            obj.put("Status_code", key);
            obj.put("Number_of_responses", summary.get(key));
            array.put(obj);
        }
        System.out.println(array);
    }

    

}
