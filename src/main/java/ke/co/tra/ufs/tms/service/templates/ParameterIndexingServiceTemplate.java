package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigIndices;
import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.wrappers.ParameterCreateRequest;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigIndexingRepository;
import ke.co.tra.ufs.tms.repository.ParMenuIndexingRepository;
import ke.co.tra.ufs.tms.service.ParameterIndexingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ParameterIndexingServiceTemplate implements ParameterIndexingService {

    private final ParMenuIndexingRepository parMenuIndexingRepository;
    private final ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository;

    public ParameterIndexingServiceTemplate(ParMenuIndexingRepository parMenuIndexingRepository, ParGlobalConfigIndexingRepository parGlobalConfigIndexingRepository) {
        this.parMenuIndexingRepository = parMenuIndexingRepository;
        this.parGlobalConfigIndexingRepository = parGlobalConfigIndexingRepository;
    }

    @Override
    public List<BigDecimal> saveAllMenus(ParameterCreateRequest<ParMenuIndices> request) {
        // get all indices -- check whether cast is possible
        BigDecimal customerType = BigDecimal.valueOf((int) request.getCategory());
        List<ParMenuIndices> indices = parMenuIndexingRepository.findAllByCustomerType(customerType, Sort.by(Sort.Direction.ASC, "menuItem"));

        PriorityQueue<ParMenuIndices> queue = new PriorityQueue<>(indices);
        PriorityQueue<ParMenuIndices> newQueue = new PriorityQueue<>(request.getIndices());


        while (!queue.isEmpty()) {
            ParMenuIndices old = queue.remove();
            ParMenuIndices newItem = newQueue.remove(); // TODO consider a case where menu is not approved and its id is less than

            old.setMenuIndex(newItem.getMenuIndex());
        }

        // save the old indices
        parMenuIndexingRepository.saveAll(indices);

        // save the remaining new items
        Iterable<ParMenuIndices> savedIndices = parMenuIndexingRepository.saveAll(newQueue);

        return StreamSupport.stream(savedIndices.spliterator(), false)
                .map(ParMenuIndices::getMenuItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<BigDecimal> saveAllConfigs(ParameterCreateRequest<ParGlobalConfigIndices> request) {
        BigDecimal configType = BigDecimal.valueOf((int) request.getCategory());
        //get configs indices by type
        List<ParGlobalConfigIndices> indices = parGlobalConfigIndexingRepository.findAllByConfigTypeAndActionStatus(configType, "Approved", Sort.by(Sort.Direction.ASC, "configItem"));

        // create hash map -- use hashmap since with priority queue --- strings are not incremented with time of saving
        Map<String, ParGlobalConfigIndices> map = new HashMap<>();
        for (ParGlobalConfigIndices index : indices) {
            map.put(index.getConfigItem(), index);
        }

        List<ParGlobalConfigIndices> newItems = new ArrayList<>();
        PriorityQueue<ParGlobalConfigIndices> newQueue = new PriorityQueue<>(request.getIndices());

        // todo check what does not exist -- have already been removed
        while (!newQueue.isEmpty()) {
            ParGlobalConfigIndices newItem = newQueue.remove(); // TODO consider a case where config is not approved and its id is less than
            if (map.containsKey(newItem.getConfigItem())) {
                map.get(newItem.getConfigItem()).setConfigIndex(newItem.getConfigIndex());
            } else {
                newItems.add(newItem);
            }
        }
        // save the old indices
        parGlobalConfigIndexingRepository.saveAll(indices);

        // save the remaining new items
        Iterable<ParGlobalConfigIndices> savedIndices = parGlobalConfigIndexingRepository.saveAll(newItems);

        return StreamSupport.stream(savedIndices.spliterator(), false)
                .map(ParGlobalConfigIndices::getId)
                .collect(Collectors.toList());
    }


//    @Override
//    public List<BigDecimal> saveAllConfigs(ParameterCreateRequest<ParGlobalConfigIndices> request) {
//        BigDecimal configType = BigDecimal.valueOf((int) request.getCategory());
//        //get configs indices by type
//        List<ParGlobalConfigIndices> indices = parGlobalConfigIndexingRepository.findAllByConfigTypeAndActionStatus(configType, "Approved", Sort.by(Sort.Direction.ASC, "configItem"));
//
//        PriorityQueue<ParGlobalConfigIndices> queue = new PriorityQueue<>(indices);
//        PriorityQueue<ParGlobalConfigIndices> newQueue = new PriorityQueue<>(request.getIndices());
//
//        while (!queue.isEmpty()) {
//            ParGlobalConfigIndices old = queue.remove();
//            ParGlobalConfigIndices newItem = newQueue.remove(); // TODO consider a case where config is not approved and its id is less than
//
//            old.setConfigIndex(newItem.getConfigIndex());
//        }
//
//        // save the old indices
//        parGlobalConfigIndexingRepository.saveAll(indices);
//
//        // save the remaining new items
//        Iterable<ParGlobalConfigIndices> savedIndices = parGlobalConfigIndexingRepository.saveAll(newQueue);
//
//        return StreamSupport.stream(savedIndices.spliterator(), false)
//                .map(ParGlobalConfigIndices::getId)
//                .collect(Collectors.toList());
//    }
}
