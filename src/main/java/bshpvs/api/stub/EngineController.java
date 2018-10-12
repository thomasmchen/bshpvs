package bshpvs.api.stub;

import bshpvs.api.stub.Greeting;
import bshpvs.api.stub.HelloMessage;
import bshpvs.model.Map;
import bshpvs.model.Ship;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@CrossOrigin
@Controller
public class EngineController {


    @CrossOrigin
    @MessageMapping("/placeShips")
    @SendTo("/topic/getUserShips")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
