package com.example.myapplication.models;

public class WorkflowConfig {

    private String workflowName;
    private String triggerName;
    private String responseName;
    private Boolean status;
    private String response;

    public WorkflowConfig(String workflowName, String triggerName, String responseName, Boolean status, String response) {
        this.workflowName = workflowName;
        this.triggerName = triggerName;
        this.responseName = responseName;
        this.status = status;
        this.response = response;
    }

    public String getWorkflowName() {
        return this.workflowName;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public String getResponseName() {
        return this.responseName;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public String getResponse() {
        return this.response;
    }

    @Override
    public String toString() {
        return "WorkflowConfig{" +
                "workflowName='" + workflowName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", responseName='" + responseName + '\'' +
                ", status=" + status +
                ", response='" + response + '\'' +
                '}';
    }
}
