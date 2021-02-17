package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParDeviceOptionsIndices;
import ke.tra.ufs.webportal.repository.ParDeviceOptionsIndicesRepository;
import ke.tra.ufs.webportal.service.ParDeviceOptionsIndicesService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ParDeviceOptionsIndicesServiceImpl implements ParDeviceOptionsIndicesService {

    private final ParDeviceOptionsIndicesRepository parDeviceOptionsIndicesRepository;

    public ParDeviceOptionsIndicesServiceImpl(ParDeviceOptionsIndicesRepository parDeviceOptionsIndicesRepository) {
        this.parDeviceOptionsIndicesRepository = parDeviceOptionsIndicesRepository;
    }

    @Override
    public List<BigDecimal> saveAll(List<ParDeviceOptionsIndices> indices) {
        // get all indices
        List<ParDeviceOptionsIndices> deviceIndices = parDeviceOptionsIndicesRepository.findAll(Sort.by(Sort.Direction.ASC, "optionId"));

        // create hash map -- use hashmap since with priority queue --- strings are not incremented with time of saving
        Map<BigDecimal, ParDeviceOptionsIndices> map = new HashMap<>();
        for (ParDeviceOptionsIndices index : deviceIndices) {
            map.put(index.getOptionId(), index);
        }

        List<ParDeviceOptionsIndices> newItems = new ArrayList<>();
        PriorityQueue<ParDeviceOptionsIndices> newQueue = new PriorityQueue<>(indices);

        // todo check what does not exist -- have already been removed
        while (!newQueue.isEmpty()) {
            ParDeviceOptionsIndices newItem = newQueue.remove(); // TODO consider a case where config is not approved and its id is less than
            if (map.containsKey(newItem.getOptionId())) {
                map.get(newItem.getOptionId()).setOptionIndex(newItem.getOptionIndex());
            } else {
                newItems.add(newItem);
            }
        }
        // save the old indices
        parDeviceOptionsIndicesRepository.saveAll(deviceIndices);

        // save the remaining new items
        Iterable<ParDeviceOptionsIndices> savedIndices = parDeviceOptionsIndicesRepository.saveAll(newItems);
        return StreamSupport.stream(savedIndices.spliterator(), false)
                .map(ParDeviceOptionsIndices::getId)
                .collect(Collectors.toList());
    }
}
