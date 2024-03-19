package webproject.factoryvision.domain.video.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import webproject.factoryvision.domain.video.ClientUtil;
import webproject.factoryvision.domain.video.dto.FlaskResponse;

@RestController
@RequestMapping("/factoryvision")
public class VideoController {

    private final ClientUtil clientUtil;

    @Autowired
    public VideoController(ClientUtil clientUtil) {
        this.clientUtil = clientUtil;
    }

    @PostMapping("/upload")
    @Operation(summary = "ai서버로 쓰러짐 감지할 영상 업로드", description = "fileName은 text로 file은 File로 영상 첨부")
    public FlaskResponse uploadVideo(@RequestParam("fileName") String fileName,
                                     @RequestParam("file") MultipartFile file) {
        try {
            return clientUtil.requestToFlask(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
