package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParReceiptItems;
import ke.co.tra.ufs.tms.repository.ParReceiptItemsRepository;
import ke.co.tra.ufs.tms.service.ParReceiptItemsService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.stereotype.Service;


@Service
public class ParReceiptItemsServiceTemplate implements ParReceiptItemsService {

    private final ParReceiptItemsRepository receiptItemsRepository;

    public ParReceiptItemsServiceTemplate(ParReceiptItemsRepository receiptItemsRepository) {
        this.receiptItemsRepository = receiptItemsRepository;
    }

    @Override
    public ParReceiptItems findByItemName(String name) {
        return receiptItemsRepository.findByNameAndIntrash(name, AppConstants.NO);
    }
}
