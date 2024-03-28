package webproject.factoryvision.domain.video.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webproject.factoryvision.domain.video.ClientUtil;
import webproject.factoryvision.domain.video.dto.FlaskResponse;

@Tag(name = "영상 API")
@RestController
@RequestMapping("/factoryvision")
@RequiredArgsConstructor
public class VideoController {

    private final ClientUtil clientUtil;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PostMapping( "/upload")
    @Operation(summary = "ai서버로 쓰러짐 감지할 영상 업로드", description = "fileName은 text로 file은 File로 영상 첨부")
//    public FlaskResponse uploadVideo(@RequestBody String fileName, @RequestBody MultipartFile file) {
    public ResponseEntity<FlaskResponse> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            FlaskResponse flaskResponse = clientUtil.requestToFlask(file);
            return ResponseEntity.status(HttpStatus.OK).body(flaskResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}