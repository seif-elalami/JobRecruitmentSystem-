package shared.interfaces;

import shared.models.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

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
    * Generate a filtered report - salary range (for jobs)
     */
    String generateFilteredReportBySalary(List<?> items, String title, double minSalary, double maxSalary) throws RemoteException;

    /**
    * Generate a filtered report - location (for jobs)
    */
    String generateFilteredReportByLocation(List<?> items, String title, String location) throws RemoteException;
}
