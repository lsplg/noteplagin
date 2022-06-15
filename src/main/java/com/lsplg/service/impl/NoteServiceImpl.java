package com.lsplg.service.impl;


import com.lsplg.model.Note;
import com.lsplg.service.NoteService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteServiceImpl implements NoteService {

    private final static String PATH = "C:\\Diplom\\";
    private final static String FILE_NAME = "\\save.txt";

    @Override
    public Note save(Note note) {
        List<Note> notes = findAllByProject(note.getProjectName());
        Note savesNote = notes.stream()
                .filter(note1 -> note1.getFileName().equals(note.getFileName())
                && note1.getLineNumber() == note.getLineNumber())
                .findFirst()
                .orElse(null);
        try (ObjectOutputStream objectOutputStream = getObjectOutputStream(note.getProjectName())) {
                notes.add(note);
                if (savesNote != null) {
                    notes.remove(savesNote);
                }
            objectOutputStream.writeObject(notes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return note;
    }

    @Override
    public void save(String projectName, List<Note> savedNotes) {
        try (ObjectOutputStream objectOutputStream = getObjectOutputStream(projectName)) {
            objectOutputStream.writeObject(savedNotes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public List<Note> findAllByProject(String projectName) {
        try (ObjectInputStream objectInputStream = getObjectInputStream(projectName)) {
            final Object object = objectInputStream.readObject();
            if (object instanceof List) {
                final List<Note> allNotes = (List<Note>) object;
                return allNotes.stream()
                        .filter(note -> note.getProjectName().equals(projectName))
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (FileNotFoundException exception) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Note findByProjectAndFileNameAndLine(String projectName, String fileName, int lineNumber) {
        final List<Note> savedNotes = findAllByProject(projectName);
        return savedNotes.stream()
                .filter(note -> note.getFileName().equals(fileName) && note.getLineNumber() == lineNumber)
                .findFirst()
                .orElse(null);
    }


    private ObjectInputStream getObjectInputStream(String projectName) throws FileNotFoundException {
        try {
            return new ObjectInputStream(getInputStream(projectName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
    }

    private FileInputStream getInputStream(String projectName) {
        try {
            return new FileInputStream(PATH+projectName+FILE_NAME);
        } catch (FileNotFoundException ex) {
            try {
                new File(PATH + projectName).mkdir();
                File file = new File(PATH+projectName+FILE_NAME);
                file.createNewFile();
                return new FileInputStream(file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private ObjectOutputStream getObjectOutputStream(String projectName) {
        try {
            return new ObjectOutputStream(getOutputStream(projectName));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    private FileOutputStream getOutputStream(String projectName) {
        try {
            return new FileOutputStream(PATH+projectName+FILE_NAME);
        } catch (FileNotFoundException ex) {
            File notes = new File(PATH);
            try {
                return new FileOutputStream(notes);
            } catch (FileNotFoundException e) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void delete(Note note) {
        var savedNotes = findAllByProject(note.getProjectName());
        var noteToDelete = savedNotes.stream()
                .filter(note1 -> note1.getFileName().equals(note.getFileName()) &&
                        note1.getLineNumber() == note.getLineNumber())
                .findFirst().orElse(null);
        savedNotes.remove(noteToDelete);
        save(note.getProjectName(), savedNotes);
    }


    @Override
    public String[][] getAllAsMatrixByProject(String projectName) {
        NoteService noteService = new NoteServiceImpl();
        List<Note> savedNotes = noteService.findAllByProject(projectName);
        String [][] savedNotesAsMatrix = new String[savedNotes.size()][5];
        for (int i = 0; i < savedNotes.size(); i++) {
            savedNotesAsMatrix[i][0] = i + 1 + "";
            savedNotesAsMatrix[i][1] = savedNotes.get(i).getLineNumber() + 1 + "";
            savedNotesAsMatrix[i][2] = savedNotes.get(i).getLineText();
            savedNotesAsMatrix[i][3] = savedNotes.get(i).getFileName();
            savedNotesAsMatrix[i][4] = savedNotes.get(i).getNote();
        }
        return savedNotesAsMatrix;
    }

}
