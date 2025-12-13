package Server.services;

import java.util.List;

public abstract class GenerateReport<T> {

    protected List<T> items;
    protected String title;

    public GenerateReport(List<T> items, String title) {
        this.items = items;
        this.title = title;
    }

    public abstract String generate();
}