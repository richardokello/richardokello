package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.ParCustomerConfigChildKeysIndices;
import ke.co.tra.ufs.tms.entities.ParCustomerConfigKeysIndices;
import ke.co.tra.ufs.tms.entities.wrappers.CustomerChildIndexRequest;
import ke.co.tra.ufs.tms.entities.wrappers.CustomerIndexRequest;
import ke.co.tra.ufs.tms.repository.ParCustomerChildConfigKeysRepository;
import ke.co.tra.ufs.tms.repository.ParCustomerConfigIndicesRepository;
import ke.co.tra.ufs.tms.service.ParCustomerConfigKeysService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@CommonsLog
public class ParCustomerConfigKeysServiceTemplate implements ParCustomerConfigKeysService {
    private final ParCustomerConfigIndicesRepository parCustomerConfigIndicesRepository;
    private final ParCustomerChildConfigKeysRepository parCustomerChildConfigKeysRepository;

    public ParCustomerConfigKeysServiceTemplate(ParCustomerConfigIndicesRepository parCustomerConfigIndicesRepository, ParCustomerChildConfigKeysRepository parCustomerChildConfigKeysRepository) {
        this.parCustomerConfigIndicesRepository = parCustomerConfigIndicesRepository;
        this.parCustomerChildConfigKeysRepository = parCustomerChildConfigKeysRepository;
    }


    @Override
    public List<BigDecimal> save(List<CustomerIndexRequest> indices) {
        // get all indices -- check whether cast is possible
        List<ParCustomerConfigKeysIndices> dbIndices = parCustomerConfigIndicesRepository.findAll(Sort.by(Sort.Direction.ASC, "configIndex"));
        log.info(">>>>>>>>>>>>>>>>>>>>>>>" + indices);
        PriorityQueue<ParCustomerConfigKeysIndices> queue = new PriorityQueue<>(dbIndices);
        PriorityQueue<CustomerIndexRequest> newQueue = new PriorityQueue<>(indices);

        Set<CustomerChildIndexRequest> childKeys = new HashSet<>();
        Set<ParCustomerConfigKeysIndices> newIndices = new HashSet<>();

        if (!queue.isEmpty()) {
            ParCustomerConfigKeysIndices old = queue.remove();
            CustomerIndexRequest indexRequest = newQueue.remove();

            while (old != null && indexRequest != null) {

                if (old.getConfigId().equals(indexRequest.getConfig())) {
                    old.setConfigIndex(indexRequest.getConfigIndex());

                    if (indexRequest.getChildIndices() != null) {
                        childKeys.addAll(indexRequest.getChildIndices());
                    }

                    old = !queue.isEmpty() ? queue.remove() : null;
                    indexRequest = !newQueue.isEmpty() ? newQueue.remove() : null;
                } else {
                    // this indicates that there is a new item with id less than old been saved for first time
                    // add it to the last index
                    ParCustomerConfigKeysIndices keysIndices = new ParCustomerConfigKeysIndices();
                    keysIndices.setConfigIndex(indexRequest.getConfigIndex());
                    keysIndices.setConfigId(indexRequest.getConfig());
                    newIndices.add(keysIndices);

                    if (indexRequest.getChildIndices() != null) {
                        childKeys.addAll(indexRequest.getChildIndices());
                    }
                    // set index to next
                    indexRequest = !newQueue.isEmpty() ? newQueue.remove() : null;
                }
            }

            // save the old indices
            parCustomerConfigIndicesRepository.saveAll(dbIndices);
        }

        // get all remaining customer indices in queue and create par customer indices
        while (!newQueue.isEmpty()) {
            CustomerIndexRequest newItem = newQueue.remove();
            ParCustomerConfigKeysIndices keysIndices = new ParCustomerConfigKeysIndices();
            keysIndices.setConfigIndex(newItem.getConfigIndex());
            keysIndices.setConfigId(newItem.getConfig());
            newIndices.add(keysIndices);

            if (newItem.getChildIndices() != null) {
                childKeys.addAll(newItem.getChildIndices());
            }
        }
        // save the remaining new items
        Iterable<ParCustomerConfigKeysIndices> savedIndices = parCustomerConfigIndicesRepository.saveAll(newIndices);

        saveChildIndices(childKeys);

        return StreamSupport.stream(savedIndices.spliterator(), false)
                .map(ParCustomerConfigKeysIndices::getConfigId)
                .collect(Collectors.toList());
    }

    private void saveChildIndices(Set<CustomerChildIndexRequest> childKeys) {
        List<ParCustomerConfigChildKeysIndices> indices = parCustomerChildConfigKeysRepository.findAll();

        // compare the keys
        PriorityQueue<ParCustomerConfigChildKeysIndices> queue = new PriorityQueue<>(indices);
        PriorityQueue<CustomerChildIndexRequest> newQueue = new PriorityQueue<>(childKeys);


        List<ParCustomerConfigChildKeysIndices> newIndices = new ArrayList<>();
        if (!queue.isEmpty()) {
            ParCustomerConfigChildKeysIndices old = queue.remove();
            CustomerChildIndexRequest indexRequest = newQueue.remove();

            while (old != null && indexRequest != null) {
                if (old.getChildConfigId().equals(indexRequest.getConfig())) {
                    old.setConfigIndex(indexRequest.getConfigIndex());

                    old = !queue.isEmpty() ? queue.remove() : null;
                    indexRequest = !newQueue.isEmpty() ? newQueue.remove() : null;
                } else {
                    // this indicates that there is a new item with id less than old been saved for first time
                    // add it to the last index
                    ParCustomerConfigChildKeysIndices keysIndices = new ParCustomerConfigChildKeysIndices();
                    keysIndices.setConfigIndex(indexRequest.getConfigIndex());
                    keysIndices.setChildConfigId(indexRequest.getConfig());
                    newIndices.add(keysIndices);
                    // set index to next
                    indexRequest = !newQueue.isEmpty() ? newQueue.remove() : null;
                }
            }

            // save the old indices
            parCustomerChildConfigKeysRepository.saveAll(indices);
        }

        // get all remaining customer indices in queue and create par customer indices
        while (!newQueue.isEmpty()) {
            CustomerChildIndexRequest newItem = newQueue.remove();
            ParCustomerConfigChildKeysIndices keysIndices = new ParCustomerConfigChildKeysIndices();
            keysIndices.setConfigIndex(newItem.getConfigIndex());
            keysIndices.setChildConfigId(newItem.getConfig());
            newIndices.add(keysIndices);
        }

        // save the remaining new items
        parCustomerChildConfigKeysRepository.saveAll(newIndices);
    }
}
