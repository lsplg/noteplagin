package com.lsplg.model;

import java.io.Serializable;

public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    private String note;
    private int lineNumber;
    private String fileName;
    private String projectName;

    public Note(String note, int lineNumber, String fileName, String projectName) {
        this.note = note;
        this.lineNumber = lineNumber;
        this.fileName = fileName;
        this.projectName = projectName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note='" + note + '\'' +
                ", lineNumber=" + lineNumber +
                ", fileName='" + fileName + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}