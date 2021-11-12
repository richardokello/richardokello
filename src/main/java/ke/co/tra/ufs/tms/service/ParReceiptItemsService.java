package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParReceiptItems;

public interface ParReceiptItemsService {

    ParReceiptItems findByItemName(String name);
}
