package com.lsplg.service;


import com.lsplg.model.Note;
import org.apache.tools.ant.Project;

import java.util.List;

public interface NoteService {
    Note save(Note note);
    void save(Note note, List<Note> savedNotes);
    List<Note> findAllByProject(String projectName);
    Note findByProjectAndFileNameAndLine(String projectName, String className, int line);
    void delete(Note note);
}
