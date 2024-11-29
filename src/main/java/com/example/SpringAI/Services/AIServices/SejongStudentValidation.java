//package com.example.SpringAI.Services.AIServices;
//
//import com.example.SpringAI.Model.Request;
//import com.example.SpringAI.Model.SejongAuthResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class SejongStudentValidation {
//
//
//    private RestTemplate restTemplate;
//
//    private String url = "https://auth.imsejong.com/auth?method=DosejongSession";
//
//    public SejongAuthResponse getValidate(String id, String pw) {
//        // Create a Request object with id and pw
//        Request request = new Request(id, pw);
//
//        // Set headers (optional, if required by the API)
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Create the HttpEntity with the body and headers
//        HttpEntity<Request> entity = new HttpEntity<>(request, headers);
//
//        try {
//            // Make the POST request
//            ResponseEntity<SejongAuthResponse> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    entity,
//                    SejongAuthResponse.class
//            );
//
//            // Return the body if the response is successful
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            } else {
//                // Handle non-OK status codes
//                throw new RuntimeException("Failed with HTTP status: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            // Handle exceptions (log and rethrow, or return a default value)
//            throw new RuntimeException("Error during Sejong Student Validation", e);
//        }
//    }
//}
