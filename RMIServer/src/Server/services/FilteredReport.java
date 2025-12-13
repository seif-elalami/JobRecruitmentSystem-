package Server.services;

import java.util.ArrayList;
import java.util.List;
import shared.models.Job;

public class FilteredReport<T> extends GenerateReport<T> {

    public FilteredReport(List<T> items, String title) {
        super(items, title);
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Filtered Report ~~~").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Matched: ").append(items == null ? 0 : items.size()).append(System.lineSeparator());
        if (items != null) {
            for (T item : items) {
                sb.append(item == null ? "null" : item.toString()).append(System.lineSeparator());
            }
        }
        sb.append("~~~~~~~~~~~~~~~~~~~~~~").append(System.lineSeparator());
        return sb.toString();
    }

    public String generateBySalary(double minSalary, double maxSalary) {
        List<T> matched = new ArrayList<>();

        if (items != null) {
            for (T item : items) {
                if (item instanceof Job) {
                    Job job = (Job) item;
                    if (job.getSalary() >= minSalary && job.getSalary() <= maxSalary) {
                        matched.add(item);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Filtered Report (Salary Range) ~~~").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Salary Range: $").append(minSalary).append(" - $").append(maxSalary).append(System.lineSeparator());
        sb.append("Matched: ").append(matched.size()).append(System.lineSeparator());
        for (T item : matched) {
            sb.append(item == null ? "null" : item.toString()).append(System.lineSeparator());
        }
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(System.lineSeparator());
        return sb.toString();
    }

    public String generateByLocation(String location) {
        List<T> matched = new ArrayList<>();

        if (items != null) {
            for (T item : items) {
                if (item instanceof Job) {
                    Job job = (Job) item;
                    if (job.getLocation().equalsIgnoreCase(location)) {
                        matched.add(item);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Filtered Report (Location) ~~~").append(System.lineSeparator());
        sb.append("Title: ").append(title).append(System.lineSeparator());
        sb.append("Location: ").append(location).append(System.lineSeparator());
        sb.append("Matched: ").append(matched.size()).append(System.lineSeparator());
        for (T item : matched) {
            sb.append(item == null ? "null" : item.toString()).append(System.lineSeparator());
        }
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(System.lineSeparator());
        return sb.toString();
    }
}