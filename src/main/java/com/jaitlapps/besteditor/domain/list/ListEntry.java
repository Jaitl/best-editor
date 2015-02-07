package com.jaitlapps.besteditor.domain.list;

import com.jaitlapps.besteditor.domain.Entry;

import java.util.ArrayList;
import java.util.List;

public class ListEntry<T extends Entry> {
    private List<T> list;

    public ListEntry() {
        list = new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void add(T entry) {
        list.add(entry);
    }

    public void update(T entry) {
        T t = list.stream().filter(g -> g.getId() == entry.getId()).findFirst().get();
    }

    public void delete(T entry) {
        T t = list.stream().filter(g -> g.getId() == entry.getId()).findFirst().get();

        list.remove(t);
    }
}
