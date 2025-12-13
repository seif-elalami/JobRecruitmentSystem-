package Server.services;

import java.util.List;

public class DetailedReport<T> extends GenerateReport<T> {

    public DetailedReport(List<T> items, String title) {
        super(items, title);
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Detailed Report ===").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Items:").append(System.lineSeparator());
        if (items != null) {
            for (T item : items) {
                sb.append(item == null ? "null" : item.toString()).append(System.lineSeparator());
            }
        }
        sb.append("=======================").append(System.lineSeparator());
        return sb.toString();
    }
}