package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.interfaces.IRecruiterService;
import shared.interfaces.ICandidateView;
import shared.models.Job;
import shared.models.Application;
import shared.models.Applicant;
import shared.models.Interview;
import shared.models.Recruiter;
import shared.models.User;


import shared.models.*;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecruiterServiceTest {

    private IRecruiterService service;
    private Session session;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public RecruiterServiceTest(RMIClient client, Session session) {
        this.service = client.getRecruiterService();
        this.session = session;
    }

    public RecruiterServiceTest(RMIClient client) {
        this.service = client.getRecruiterService();
        this.session = new Session("test-recruiter-123", "test@recruiter.com", "RECRUITER");
    }

    public void runOnce() {
        showMenu();
        int choice = InputHelper.getInt();
        System.out.println();
    
        try {
            switch (choice) {
    
                case 1 -> viewProfile();
                case 2 -> viewMyJobPostings();
                case 3 -> closeJobPosting();
    
                case 4 -> viewAllApplications();
                case 5 -> matchCandidatesToJob();
                case 6 -> searchCandidatesBySkills();
                case 7 -> searchCandidatesByExperience();
    
                case 8 -> scheduleInterview();
    
                default -> System.out.println("❌ Invalid choice");
            }
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED");
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║  Recruiter Service TEST MENU    ║");
        System.out.println("╚══════════════════════════════════╝");
    
        System.out.println("1. View My Profile");
        System.out.println("2. View My Job Postings");
        System.out.println("3. Close Job Posting");
    
        System.out.println("4. View All Applications");
        System.out.println("5. Match Candidates to Job");
        System.out.println("6. Search Candidates by Skills");
        System.out.println("7. Search Candidates by Experience");
    
        System.out.println("8. Schedule Interview");
        System.out.print("\nChoice: ");
    }
    
    // Profile  Managment
    
    private void viewProfile() {
        try {
            Recruiter r = service.getRecruiterById(session.getUserId());
            System.out.println("ID: " + r.getId());
            System.out.println("Name: " + r.getName());
            System.out.println("Email: " + r.getEmail());
            System.out.println("Phone: " + r.getPhone());
            System.out.println("Company: " + r.getCompany());
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    
    private void viewMyJobPostings() {
        try {
            List<Job> jobs = service.getMyJobPostings(session.getUserId());
            if (jobs.isEmpty()) System.out.println("⚠️ No jobs posted.");
            for (Job job : jobs) displayJob(job);
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void closeJobPosting() {
        try {
            System.out.print("Job ID to close: ");
            String jobId = InputHelper.getString();
            boolean closed = service.closeJobPosting(jobId, session.getUserId());
            System.out.println(closed ? "✅ Closed!" : "❌ Failed!");
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

// applications functions : 

    private void viewAllApplications() {
        System.out.println("Not implemented: You can loop through all jobs and fetch applications.");
    }

  


   
    //Candidiate Functions 

    private void matchCandidatesToJob() {
        try {
            System.out.print("Job ID: ");
            String jobId = InputHelper.getString();
            List<Applicant> matches = service.matchCandidatesToJob(jobId);
            if (matches.isEmpty()) System.out.println("⚠️ No matches found.");
            for (Applicant a : matches) displayApplicant(a);

            if (!matches.isEmpty()) {
                System.out.print("Finalize hire? (Applicant ID or Enter to skip): ");
                String applicantId = InputHelper.getString();
                if (!applicantId.isEmpty()) {
                    boolean hired = service.matchFinalCandidateToJob(jobId, applicantId);
                    System.out.println(hired ? "✅ Hired!" : "❌ Failed!");
                }
            }

        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

 

    private void searchCandidatesBySkills() {
        try {
            System.out.print("Skills: ");
            String skills = InputHelper.getString();
            List<ICandidateView> list = service.searchCandidatesBySkillsReadOnly(skills);
            if (list.isEmpty()) System.out.println("⚠️ No matches.");
            for (ICandidateView cv : list) {
                System.out.println(cv.getName() + " - " + cv.getSkills());
            }
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchCandidatesByExperience() {
        try {
            System.out.print("Minimum experience (years): ");
            int minYears = InputHelper.getInt();
            List<ICandidateView> list = service.searchCandidatesByMinExperience(minYears);
            if (list.isEmpty()) System.out.println("⚠️ No matches.");
            for (ICandidateView cv : list) {
                System.out.println(cv.getName() + " - " + cv.getExperience());
            }
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

   //Interview functions 


    private void scheduleInterview() {
        try {
            System.out.print("Job ID: ");
            String jobId = InputHelper.getString();
            System.out.print("Applicant ID: ");
            String applicantId = InputHelper.getString();
            System.out.print("Interview Date (dd/MM/yyyy HH:mm): ");
            String dateStr = InputHelper.getString();
            Date date;
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("❌ Invalid date format! Using current date.");
                date = new Date();
            }
            System.out.print("Location: ");
            String location = InputHelper.getString();
            System.out.print("Notes: ");
            String notes = InputHelper.getString();

            Interview interview = new Interview(jobId, applicantId, session.getUserId(), date, location);
            interview.setNotes(notes);

            String id = service.createInterview(interview);
            System.out.println("✅ Interview scheduled! ID: " + id);
        } catch (RemoteException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

 




  
    // ===========================
    // Display Helpers
    // ===========================
    private void displayJob(Job job) {
        System.out.println("Job ID: " + job.getJobId());
        System.out.println("Title: " + job.getTitle());
        System.out.println("Company: " + job.getCompany());
        System.out.println("Location: " + job.getLocation());
        System.out.println("Salary: " + job.getSalary());
        System.out.println("Requirements: " + job.getRequirements());
        System.out.println("Status: " + job.getStatus());
        System.out.println("----------------------");
    }



    private void displayApplicant(Applicant a) {
        System.out.println("ID: " + a.getId());
        System.out.println("Name: " + a.getName());
        System.out.println("Email: " + a.getEmail());
        System.out.println("Skills: " + a.getSkills());
        System.out.println("Experience: " + a.getExperience());
        System.out.println("----------------------");
    }

 
}
