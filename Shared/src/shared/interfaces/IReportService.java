package shared.interfaces;

import shared.models.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Predicate;

public interface IReportService extends Remote {

    /**
     * Generate a simple summary report for a list of items
     */
    String generateSimpleReport(List<?> items, String title) throws RemoteException;

    /**
     * Generate a detailed report listing all items
     */
    String generateDetailedReport(List<?> items, String title) throws RemoteException;

    /**
     * Generate a filtered report with custom filter predicate
     */
    String generateFilteredReport(List<?> items, String title, Predicate<?> filter) throws RemoteException;
}
