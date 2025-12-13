package Server.services;

import shared.interfaces.IReportService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ReportServiceImpl extends UnicastRemoteObject implements IReportService {

    private static final long serialVersionUID = 1L;

    public ReportServiceImpl() throws RemoteException {
        super();
        System.out.println("✅ ReportService initialized");
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
    public String generateFilteredReportBySalary(List<?> items, String title, double minSalary, double maxSalary) throws RemoteException {
        try {
            FilteredReport<?> report = new FilteredReport<>(items, title);
            return report.generateBySalary(minSalary, maxSalary);
        } catch (Exception e) {
            throw new RemoteException("Error generating filtered report", e);
        }
    }

    @Override
    public String generateFilteredReportByLocation(List<?> items, String title, String location) throws RemoteException {
        try {
            FilteredReport<?> report = new FilteredReport<>(items, title);
            return report.generateByLocation(location);
        } catch (Exception e) {
            throw new RemoteException("Error generating filtered report", e);
        }
    }
}