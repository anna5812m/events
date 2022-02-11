package com.example.events.model;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RepetitionRepository implements CommonRepository<Repetition> {

    private Map<String,Repetition> repetitions = new HashMap<>();

    @Override
    public Repetition save(Repetition domain) {
        Repetition result = repetitions.get(domain.getId());
        if(result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(domain.getDescription());
            result.setCompleted(domain.isCompleted());
            domain = result;
        }
        repetitions.put((String) domain.getId(), domain);
        return repetitions.get(domain.getId());
    }

    @Override
    public Iterable<Repetition> save(Collection<Repetition> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(Repetition domain) {
        repetitions.remove(domain.getId());
    }

    @Override
    public Repetition findById(String id) {
        return repetitions.get(id);
    }

    @Override
    public Iterable<Repetition> findAll() {
        return repetitions.entrySet().stream().sorted(entrycomparator).map
                (Map.Entry::getValue).collect(Collectors.toList());
    }

    private Comparator<Map.Entry<String,Repetition>> entrycomparator =
            (Map.Entry<String, Repetition> ol, Map.Entry<String, Repetition> o2) -> {
        return ol.getValue().getCreated().compareTo
                (o2.getValue().getCreated());
    };
}
