//package com.lsplg.service.impl;
//
//import com.lsplg.model.Store;
//import com.lsplg.service.StoreService;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class StoreServiceImpl implements StoreService {
//
//    private static final String PATH = "C:\\Diplom\\save.txt";
//
//    @Override
//    public Store findByProjectName(String projectName) {
//        final List<Store> savedStores = findALl();
//        return savedStores.stream()
//                .filter(store -> store.getProjectName().equals(projectName))
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Override
//    public Store save(Store store) {
//        List<Store> stores = findALl();
//        try (ObjectOutputStream objectOutputStream = getObjectOutputStream()) {
//            for (Store savedStore : stores) {
//                if (savedStore.getProjectName().equals(store.getProjectName())) {
//                    savedStore.setNotes(store.getNotes());
//                    objectOutputStream.writeObject(stores);
//                    return store;
//                }
//            }
//            stores.add(store);
//            objectOutputStream.writeObject(stores);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return store;
//    }
//
//
//    private List<Store> findALl() {
//        try (ObjectInputStream objectInputStream = getObjectInputStream()) {
//            final Object object = objectInputStream.readObject();
//            if (object instanceof List) {
//                return (List<Store>) object;
//            } else {
//                return new ArrayList<>();
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//
//    private ObjectInputStream getObjectInputStream() {
//        try {
//            return new ObjectInputStream(getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }
//
//    private FileInputStream getInputStream() {
//        try {
//            return new FileInputStream(PATH);
//        } catch (FileNotFoundException ex) {
//            // создать файл и вернуть филе инпут стрим
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//    private ObjectOutputStream getObjectOutputStream() {
//        try {
//            return new ObjectOutputStream(getOutputStream());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            throw new RuntimeException();
//        }
//    }
//
//    private FileOutputStream getOutputStream() {
//        try {
//            return new FileOutputStream(PATH);
//        } catch (FileNotFoundException ex) {
//            // создать файл и вернуть филе аутпут стрим
//            ex.printStackTrace();
//            return null;
//        }
//    }
//}
