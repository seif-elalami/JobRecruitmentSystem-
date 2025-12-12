package Server.services;

import java.util.List;

public class SimpleReport<T> extends GenerateReport<T> {

    public SimpleReport(List<T> items, String title) {
        super(items, title);
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Simple Report ---").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Total items: ").append(items == null ? 0 : items.size()).append(System.lineSeparator());
        sb.append("---------------------").append(System.lineSeparator());
        return sb.toString();
    }
}
