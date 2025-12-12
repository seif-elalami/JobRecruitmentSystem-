package Server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilteredReport<T> extends GenerateReport<T> {

    private Predicate<T> filter;

    public FilteredReport(List<T> items, String title, Predicate<T> filter) {
        super(items, title);
        this.filter = filter;
    }

    @Override
    public String generate() {
        List<?> source = items == null ? new ArrayList<>() : items;
        @SuppressWarnings("unchecked")
        List<T> matched = source.stream()
                .filter(item -> filter.test((T) item))
                .map(item -> (T) item)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Filtered Report ~~~").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Matched: ").append(matched.size()).append(System.lineSeparator());
        for (T item : matched) {
            sb.append(item == null ? "null" : item.toString()).append(System.lineSeparator());
        }
        sb.append("~~~~~~~~~~~~~~~~~~~~~~").append(System.lineSeparator());
        return sb.toString();
    }
}
