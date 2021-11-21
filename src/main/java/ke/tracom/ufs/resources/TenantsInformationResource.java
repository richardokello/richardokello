package ke.tracom.ufs.resources;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.wrappers.TenantInfoWrapper;
import ke.tracom.ufs.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/tenant-info")
public class TenantsInformationResource {

    @GetMapping()
    public ResponseEntity<ResponseWrapper<List<TenantInfoWrapper>>> fetchTenantsInfo(){
        ResponseWrapper<List<TenantInfoWrapper>> response = new ResponseWrapper<>();
        List<TenantInfoWrapper> tenantinfo;
        try {
            FileReader reader = new FileReader(AppConstants.TENANT_INFORMATION_FILENAME);
            tenantinfo = this.loadTenantsInfo(reader,new Gson());
            reader.close();
        }catch(Exception ex){
            response.setMessage(ex.getMessage());
            response.setCode(500);
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setData(tenantinfo);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    private List<TenantInfoWrapper> loadTenantsInfo(FileReader reader,Gson gson) throws JsonParseException {
        Object data = gson.fromJson(reader,TenantInfoWrapper[].class);
        System.out.println("Fetched tenants information>>"+Arrays.asList((TenantInfoWrapper[])data));
        return Arrays.asList((TenantInfoWrapper[])data);
    }
}
