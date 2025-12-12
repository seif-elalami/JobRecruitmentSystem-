package Server.services;

import shared.interfaces.IReportService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.function.Predicate;

public class ReportServiceImpl extends UnicastRemoteObject implements IReportService {

    private static final long serialVersionUID = 1L;

    public ReportServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String generateSimpleReport(List<?> items, String title) throws RemoteException {
        try {
            SimpleReport<?> report = new SimpleReport<>(items, title);
            return report.generate();
        } catch (Exception e) {
            throw new RemoteException("Error generating simple report", e);
        }
    }

    @Override
    public String generateDetailedReport(List<?> items, String title) throws RemoteException {
        try {
            DetailedReport<?> report = new DetailedReport<>(items, title);
            return report.generate();
        } catch (Exception e) {
            throw new RemoteException("Error generating detailed report", e);
        }
    }

    @Override
    public String generateFilteredReport(List<?> items, String title, Predicate<?> filter) throws RemoteException {
        try {
            @SuppressWarnings("unchecked")
            FilteredReport<Object> report = new FilteredReport<>((List<Object>) items, title, (Predicate<Object>) filter);
            return report.generate();
        } catch (Exception e) {
            throw new RemoteException("Error generating filtered report", e);
        }
    }
}
