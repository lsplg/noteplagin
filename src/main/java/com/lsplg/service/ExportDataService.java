package com.lsplg.service;

import com.lsplg.model.Note;

import java.util.List;

public interface ExportDataService {
    void toPDF(List<Note> savedNotes, String projectName);

}
