package webproject.factoryvision.domain.video;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import webproject.factoryvision.domain.video.dto.FlaskResponse;

@Component
@PropertySource("application.yml")
public class ClientUtil {

    @Value("${flask.url}")
    private String url;

    public FlaskResponse requestToFlask(String fileName, MultipartFile file) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Header set
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Body set
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        String imageFileString = getBase64String(file);
        body.add("filename", fileName);
        body.add("file", file.getResource());

        // Message
        HttpEntity<MultiValueMap<String, Object>> requestMessage = new HttpEntity<>(body, httpHeaders);

        // Request
        HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        // Response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        FlaskResponse dto = objectMapper.readValue(response.getBody(), FlaskResponse.class);

        return dto;
    }

}
