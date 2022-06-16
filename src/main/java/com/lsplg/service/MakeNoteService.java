package com.lsplg.service;


import com.lsplg.model.Note;

import java.util.List;

public interface MakeNoteService {
    Note save(Note note);
    void save(String projectName, List<Note> savedNotes);
    List<Note> findAllByProject(String projectName);
    void delete(Note note);
    String[][] getAllAsMatrixByProject(String projectName);
}
