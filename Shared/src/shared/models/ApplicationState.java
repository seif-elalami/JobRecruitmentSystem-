package shared.models;

import java.io.Serializable;

/**
 * Concrete ApplicationState class that manages state transitions
 */
public class ApplicationState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentStateName;
    private Application application;

    // State enum for internal state management
    public enum State {
        APPLIED,
        UNDER_REVIEW,
        INTERVIEW_SCHEDULED,
        ACCEPTED,
        REJECTED
    }

    private State currentState;

    // Constructor
    public ApplicationState() {
        this.currentState = State.APPLIED;
        this.currentStateName = "APPLIED";
    }

    public ApplicationState(String stateName) {
        setStateByName(stateName);
    }

    /**
     * Handle the application in current state
     * @param app The application to handle
     */
    public void handle(Application app) {
        this.application = app;
        
        switch (currentState) {
            case APPLIED:
                System.out.println("📝 Application is in APPLIED state");
                System.out.println("   Waiting for recruiter review...");
                break;
            case UNDER_REVIEW:
                System.out.println("🔍 Application is UNDER REVIEW");
                System.out.println("   Recruiter is evaluating the candidate...");
                break;
            case INTERVIEW_SCHEDULED:
                System.out.println("📅 Interview SCHEDULED");
                System.out.println("   Waiting for interview outcome...");
                break;
            case ACCEPTED:
                System.out.println("✅ Application ACCEPTED");
                System.out.println("   Congratulations! The candidate has been accepted.");
                break;
            case REJECTED:
                System.out.println("❌ Application REJECTED");
                System.out.println("   The application has been rejected.");
                break;
        }
    }

    /**
     * Get the name of current state
     * @return State name
     */
    public String getStateName() {
        return currentStateName;
    }

    /**
     * Review the application - transition to UNDER_REVIEW
     */
    public void review() {
        switch (currentState) {
            case APPLIED:
                System.out.println("✅ Moving application to UNDER_REVIEW state");
                currentState = State.UNDER_REVIEW;
                currentStateName = "UNDER_REVIEW";
                break;
            case UNDER_REVIEW:
                System.out.println("⚠️  Application is already under review");
                break;
            default:
                System.out.println("❌ Cannot review - Application in " + currentStateName + " state");
        }
    }

    /**
     * Schedule an interview - transition to INTERVIEW_SCHEDULED
     */
    public void scheduleInterview() {
        switch (currentState) {
            case APPLIED:
                System.out.println("❌ Cannot schedule interview - Application must be reviewed first");
                break;
            case UNDER_REVIEW:
                System.out.println("✅ Scheduling interview - Moving to INTERVIEW_SCHEDULED state");
                currentState = State.INTERVIEW_SCHEDULED;
                currentStateName = "INTERVIEW_SCHEDULED";
                break;
            case INTERVIEW_SCHEDULED:
                System.out.println("⚠️  Interview is already scheduled");
                break;
            default:
                System.out.println("❌ Cannot schedule interview from " + currentStateName + " state");
        }
    }

    /**
     * Reject the application - transition to REJECTED
     */
    public void reject() {
        switch (currentState) {
            case ACCEPTED:
                System.out.println("❌ Cannot reject - Application already accepted (final state)");
                break;
            case REJECTED:
                System.out.println("⚠️  Application is already rejected");
                break;
            default:
                System.out.println("✅ Application can be rejected from " + currentStateName + " state");
                currentState = State.REJECTED;
                currentStateName = "REJECTED";
        }
    }

    /**
     * Accept the application - transition to ACCEPTED
     */
    public void accept() {
        switch (currentState) {
            case APPLIED:
                System.out.println("❌ Cannot accept - Application must be reviewed first");
                break;
            case UNDER_REVIEW:
                System.out.println("❌ Cannot accept directly - Must schedule interview first");
                break;
            case INTERVIEW_SCHEDULED:
                System.out.println("✅ Application can be accepted after interview");
                currentState = State.ACCEPTED;
                currentStateName = "ACCEPTED";
                break;
            case ACCEPTED:
                System.out.println("⚠️  Application is already accepted");
                break;
            case REJECTED:
                System.out.println("❌ Cannot accept - Application already rejected (final state)");
                break;
        }
    }

    /**
     * Check if current state is Applied
     */
    public boolean isApplied() {
        return currentState == State.APPLIED;
    }

    /**
     * Check if current state is Under Review
     */
    public boolean isUnderReview() {
        return currentState == State.UNDER_REVIEW;
    }

    /**
     * Check if current state is Interview Scheduled
     */
    public boolean isInterviewScheduled() {
        return currentState == State.INTERVIEW_SCHEDULED;
    }

    /**
     * Check if current state is Accepted
     */
    public boolean isAccepted() {
        return currentState == State.ACCEPTED;
    }

    /**
     * Check if current state is Rejected
     */
    public boolean isRejected() {
        return currentState == State.REJECTED;
    }

    /**
     * Check if state is final (Accepted or Rejected)
     */
    public boolean isFinalState() {
        return currentState == State.ACCEPTED || currentState == State.REJECTED;
    }

    /**
     * Set state by name (for backward compatibility)
     */
    public void setStateByName(String stateName) {
        if (stateName == null) {
            this.currentState = State.APPLIED;
            this.currentStateName = "APPLIED";
            return;
        }

        switch (stateName.toUpperCase()) {
            case "APPLIED":
            case "PENDING":
                this.currentState = State.APPLIED;
                this.currentStateName = "APPLIED";
                break;
            case "UNDER_REVIEW":
                this.currentState = State.UNDER_REVIEW;
                this.currentStateName = "UNDER_REVIEW";
                break;
            case "INTERVIEW_SCHEDULED":
                this.currentState = State.INTERVIEW_SCHEDULED;
                this.currentStateName = "INTERVIEW_SCHEDULED";
                break;
            case "ACCEPTED":
                this.currentState = State.ACCEPTED;
                this.currentStateName = "ACCEPTED";
                break;
            case "REJECTED":
                this.currentState = State.REJECTED;
                this.currentStateName = "REJECTED";
                break;
            default:
                this.currentState = State.APPLIED;
                this.currentStateName = "APPLIED";
        }
    }

    /**
     * Get the internal state enum
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Set the internal state enum
     */
    public void setCurrentState(State state) {
        this.currentState = state;
        this.currentStateName = state.name();
    }

    @Override
    public String toString() {
        return "ApplicationState{" +
                "state=" + currentStateName +
                '}';
    }
}
