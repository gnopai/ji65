package com.gnopai.ji65.linker;

import com.gnopai.ji65.Address;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class UnnamedLabels {
    private final LinkedList<Integer> addresses = new LinkedList<>();

    public void add(int address) {
        ListIterator<Integer> iterator = addresses.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next() > address) {
                iterator.previous();
                break;
            }
        }
        iterator.add(address);
    }

    public List<Integer> getAll() {
        return List.copyOf(addresses);
    }

    public Optional<Integer> getFromAddress(Address address, int offset) {
        if (addresses.isEmpty() || offset == 0) {
            return Optional.empty();
        }

        int targetIndex = determineTargetIndex(address.getValue(), offset);

        if (targetIndex < 0 || targetIndex >= addresses.size()) {
            return Optional.empty();
        }
        return Optional.of(addresses.get(targetIndex));
    }

    private int determineTargetIndex(int startValue, int offset) {
        int i = 0;
        for (int value : addresses) {
            if (startValue < value) {
                break;
            }
            i++;
        }

        if (offset > 0) {
            offset--;
        }
        return i + offset;
    }
}
