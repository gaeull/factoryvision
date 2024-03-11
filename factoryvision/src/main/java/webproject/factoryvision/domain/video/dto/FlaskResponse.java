package webproject.factoryvision.domain.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.FileOutputStream;
import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class FlaskResponse {
    private String videoUrl;
//    private FileOutputStream videoPath;
    private String message;
}
