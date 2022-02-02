package ke.co.tra.ufs.tms.config;

import lombok.extern.apachecommons.CommonsLog;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;


import java.io.FileReader;
import java.io.IOException;

@Component
@CommonsLog
public class ParseJsonFile {
    public Object parseJsonFile(String filename) throws IOException, ParseException {
        return new JSONParser().parse(new FileReader(filename));
    }
}

