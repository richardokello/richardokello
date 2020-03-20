package Test;

import ke.tra.com.tsync.services.CoreProcessorService;
import org.jpos.iso.ISOException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TsyncApplicationTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TsyncApplicationTest.class.getName());
    @Autowired
    private CoreProcessorService coreProcessorService;



    @Test
    public void invokeAOPStuff() {

        //String mti, String procode, String actionstatus, String intrash

            coreProcessorService.getTxnTypebyMTIAndProcodeAndActionstatusandIntrash("1233","2343242","1","NO");

    }
}
