package webproject.factoryvision.domain.video;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import webproject.factoryvision.domain.video.dto.FlaskResponse;

@Component
public class ClientUtil {

//    public FlaskResponse requestToFlask(String fileName, MultipartFile file) throws Exception {
    public FlaskResponse requestToFlask(MultipartFile file) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("filename", fileName);
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestMessage = new HttpEntity<>(body, httpHeaders);

        HttpEntity<String> response = restTemplate.postForEntity("http://host.docker.internal:5002/upload_video", requestMessage, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        FlaskResponse dto = objectMapper.readValue(response.getBody(), FlaskResponse.class);

        return dto;
    }

}
