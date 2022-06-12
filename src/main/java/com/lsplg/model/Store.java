//package com.lsplg.model;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Store implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private String projectName;
//    private List<Note> notes;
//    private Long counterId;
//
//    public Store(String projectName) {
//        this.projectName = projectName;
//        this.notes = new ArrayList<>();
//        this.counterId = 1L;
//    }
//
//    public String getProjectName() {
//        return projectName;
//    }
//
//    public void setProjectName(String projectName) {
//        this.projectName = projectName;
//    }
//
//    public List<Note> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(List<Note> notes) {
//        this.notes = notes;
//    }
//
//    public Long getCounterId() {
//        return counterId;
//    }
//
//    public void setCounterId(Long counterId) {
//        this.counterId = counterId;
//    }
//
//    public void addNote(Note note) {
//        if (note.getId() != null) {
//            var noteToUpdate = this.notes.stream()
//                    .filter(savedNote -> savedNote.getId().equals(note.getId()))
//                    .findFirst().orElseThrow(null);
//            noteToUpdate.setNote(note.getNote());
//        } else {
//            note.setId(counterId++);
//            this.notes.add(note);
//        }
//    }
//}
